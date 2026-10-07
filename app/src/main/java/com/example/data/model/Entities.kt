package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "u_alex",
    val name: String = "Alex Rivera",
    val phone: String = "+1 (555) 000-0000",
    val avatarInitials: String = "AR",
    val points: Int = 1240,
    val isPro: Boolean = false,
    val isOnline: Boolean = true,
    val emergencyContactName: String = "Sarah Rivera",
    val emergencyContactPhone: String = "+15551234567"
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val initials: String,
    val colorHex: String,
    val memberCount: Int,
    val rideCount: Int,
    val description: String = "",
    val inviteCode: String = ""
)

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val difficulty: String, // "MODERATE", "EASY", "HARD"
    val distanceMiles: Int,
    val durationText: String,
    val elevationFeet: Int,
    val description: String = "",
    val waypointsJson: String = ""
)

@Entity(tableName = "rides")
data class RideEntity(
    @PrimaryKey val id: String,
    val title: String,
    val groupName: String,
    val routeId: String,
    val status: String, // "ACTIVE", "UPCOMING", "COMPLETED"
    val startTime: String,
    val riderCount: Int,
    val currentMiles: Int,
    val totalMiles: Int,
    val eta: String,
    val paceAvgMph: Int,
    val currentSpeedMph: Int = 0,
    val leanAngleDegrees: Int = 0,
    val activePttChannel: String = "Canyon Carvers Comms"
)

@Entity(tableName = "telemetry_logs")
data class TelemetryLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rideId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double,
    val longitude: Double,
    val speedMph: Float,
    val heading: Float,
    val leanAngle: Float,
    val batteryPct: Int = 92,
    val isSynced: Boolean = false
)
