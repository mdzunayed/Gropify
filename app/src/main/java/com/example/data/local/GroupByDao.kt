package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GroupEntity
import com.example.data.model.RideEntity
import com.example.data.model.RouteEntity
import com.example.data.model.TelemetryLogEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupByDao {

    // User operations
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: String = "u_alex"): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // Group operations
    @Query("SELECT * FROM groups ORDER BY memberCount DESC")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE id = :groupId LIMIT 1")
    suspend fun getGroupById(groupId: String): GroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    // Route operations
    @Query("SELECT * FROM routes ORDER BY distanceMiles ASC")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<RouteEntity>)

    @Query("SELECT * FROM routes WHERE id = :routeId LIMIT 1")
    suspend fun getRouteById(routeId: String): RouteEntity?

    // Ride operations
    @Query("SELECT * FROM rides WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveRide(): Flow<RideEntity?>

    @Query("SELECT * FROM rides ORDER BY startTime DESC")
    fun getAllRides(): Flow<List<RideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRides(rides: List<RideEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: RideEntity)

    @Update
    suspend fun updateRide(ride: RideEntity)

    // Telemetry operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTelemetryLog(log: TelemetryLogEntity)

    @Query("SELECT * FROM telemetry_logs WHERE rideId = :rideId ORDER BY timestamp DESC LIMIT 50")
    fun getRecentTelemetry(rideId: String): Flow<List<TelemetryLogEntity>>
}
