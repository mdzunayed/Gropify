package com.example.data.repository

import com.example.data.local.GroupByDao
import com.example.data.model.GroupEntity
import com.example.data.model.RideEntity
import com.example.data.model.RouteEntity
import com.example.data.model.TelemetryLogEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class RideSettings(
    val crashDetectionEnabled: Boolean = true,
    val helmetHudMirrorEnabled: Boolean = false,
    val metricUnitsEnabled: Boolean = false,
    val rideAlertsEnabled: Boolean = true,
    val language: String = "English"
)

data class RiderStatus(
    val id: String,
    val name: String,
    val initials: String,
    val colorHex: String,
    val speedMph: Int,
    val distanceBehindLeadMiles: Double,
    val isTalking: Boolean = false,
    val batteryPct: Int = 90
)

class GroupByRepository(
    private val dao: GroupByDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    val currentUser: Flow<UserEntity?> = dao.getUser()
    val allGroups: Flow<List<GroupEntity>> = dao.getAllGroups()
    val allRoutes: Flow<List<RouteEntity>> = dao.getAllRoutes()
    val activeRide: Flow<RideEntity?> = dao.getActiveRide()

    private val _settings = MutableStateFlow(RideSettings())
    val settings = _settings.asStateFlow()

    // Active riders connected on real-time mesh for current ride
    private val _connectedRiders = MutableStateFlow(
        listOf(
            RiderStatus("r1", "Alex (You)", "AR", "#FF6B1A", 64, 0.0, isTalking = false, batteryPct = 88),
            RiderStatus("r2", "Marcus Kane", "MK", "#00E5FF", 62, 0.2, isTalking = false, batteryPct = 94),
            RiderStatus("r3", "David Vance", "DV", "#10B981", 61, 0.5, isTalking = false, batteryPct = 76)
        )
    )
    val connectedRiders = _connectedRiders.asStateFlow()

    fun updateSettings(transform: (RideSettings) -> RideSettings) {
        _settings.value = transform(_settings.value)
    }

    suspend fun setProStatus(isPro: Boolean) {
        val user = currentUser.firstOrNull() ?: UserEntity()
        dao.insertOrUpdateUser(user.copy(isPro = isPro))
    }

    suspend fun updateEmergencyContact(name: String, phone: String) {
        val user = currentUser.firstOrNull() ?: UserEntity()
        dao.insertOrUpdateUser(user.copy(emergencyContactName = name, emergencyContactPhone = phone))
    }

    suspend fun createGroup(name: String, description: String, initials: String, colorHex: String): GroupEntity {
        val newGroup = GroupEntity(
            id = "g_${System.currentTimeMillis()}",
            name = name,
            initials = initials.take(2).uppercase(),
            colorHex = colorHex,
            memberCount = 1,
            rideCount = 0,
            description = description,
            inviteCode = "GB-${(1000..9999).random()}"
        )
        dao.insertGroup(newGroup)
        return newGroup
    }

    suspend fun startOrResumeRide(route: RouteEntity): RideEntity {
        val existing = activeRide.firstOrNull()
        if (existing != null && existing.routeId == route.id) {
            return existing
        }
        val ride = RideEntity(
            id = "ride_${System.currentTimeMillis()}",
            title = route.title,
            groupName = "Canyon Carvers",
            routeId = route.id,
            status = "ACTIVE",
            startTime = "09:14",
            riderCount = 3,
            currentMiles = 0,
            totalMiles = route.distanceMiles,
            eta = "10:32",
            paceAvgMph = 62,
            currentSpeedMph = 58,
            leanAngleDegrees = 22,
            activePttChannel = "Canyon Carvers Comms"
        )
        dao.insertRide(ride)
        return ride
    }

    suspend fun updateActiveRideTelemetry(miles: Int, speed: Int, leanAngle: Int) {
        val current = activeRide.firstOrNull() ?: return
        val updated = current.copy(
            currentMiles = miles.coerceAtMost(current.totalMiles),
            currentSpeedMph = speed,
            leanAngleDegrees = leanAngle
        )
        dao.updateRide(updated)
        dao.insertTelemetryLog(
            TelemetryLogEntity(
                rideId = current.id,
                latitude = 34.1012,
                longitude = -118.6811,
                speedMph = speed.toFloat(),
                heading = 184.0f,
                leanAngle = leanAngle.toFloat()
            )
        )
    }

    suspend fun endActiveRide() {
        val current = activeRide.firstOrNull() ?: return
        dao.updateRide(current.copy(status = "COMPLETED"))
    }

    fun setRiderTalking(riderId: String, isTalking: Boolean) {
        _connectedRiders.value = _connectedRiders.value.map {
            if (it.id == riderId) it.copy(isTalking = isTalking) else it
        }
    }
}
