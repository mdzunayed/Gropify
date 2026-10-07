package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.GroupEntity
import com.example.data.model.RideEntity
import com.example.data.model.RouteEntity
import com.example.data.model.TelemetryLogEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        GroupEntity::class,
        RouteEntity::class,
        RideEntity::class,
        TelemetryLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GroupByDatabase : RoomDatabase() {

    abstract fun dao(): GroupByDao

    companion object {
        @Volatile
        private var INSTANCE: GroupByDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ): GroupByDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroupByDatabase::class.java,
                    "groupby_database.db"
                ).addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.dao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: GroupByDao) {
                dao.insertOrUpdateUser(
                    UserEntity(
                        id = "u_alex",
                        name = "Alex Rivera",
                        phone = "+1 (555) 000-0000",
                        avatarInitials = "AR",
                        points = 1240,
                        isPro = false,
                        isOnline = true,
                        emergencyContactName = "Sarah Rivera",
                        emergencyContactPhone = "+15551234567"
                    )
                )

                val groups = listOf(
                    GroupEntity(
                        id = "g_canyon",
                        name = "Canyon Carvers",
                        initials = "CC",
                        colorHex = "#FF6B1A",
                        memberCount = 24,
                        rideCount = 112,
                        description = "SoCal sportbike and canyon enthusiasts. Weekend carving & coffee runs.",
                        inviteCode = "CC-9021"
                    ),
                    GroupEntity(
                        id = "g_nightowls",
                        name = "Night Owls MC",
                        initials = "NO",
                        colorHex = "#00E5FF",
                        memberCount = 18,
                        rideCount = 67,
                        description = "Late night city cruises, highway sweeps, and scenic lookouts.",
                        inviteCode = "NO-4412"
                    ),
                    GroupEntity(
                        id = "g_coastal",
                        name = "Coastal Cruisers",
                        initials = "CC",
                        colorHex = "#10B981",
                        memberCount = 41,
                        rideCount = 203,
                        description = "Pacific Coast Highway cruisers, weekend tourers, all bikes welcome.",
                        inviteCode = "CC-8820"
                    ),
                    GroupEntity(
                        id = "g_twisty",
                        name = "Twisty Tuesdays",
                        initials = "TT",
                        colorHex = "#8B5CF6",
                        memberCount = 12,
                        rideCount = 44,
                        description = "Weekly Tuesday evening twisties & track practice prep.",
                        inviteCode = "TT-1209"
                    )
                )
                dao.insertGroups(groups)

                val routes = listOf(
                    RouteEntity(
                        id = "r_mulholland",
                        title = "Mulholland Loop",
                        difficulty = "MODERATE",
                        distanceMiles = 42,
                        durationText = "1h 20m",
                        elevationFeet = 3200,
                        description = "Legendary twisty corridor winding through Santa Monica mountains with ocean views."
                    ),
                    RouteEntity(
                        id = "r_pch",
                        title = "PCH Coastal Sweep",
                        difficulty = "EASY",
                        distanceMiles = 68,
                        durationText = "2h 05m",
                        elevationFeet = 1100,
                        description = "Relaxed coastal run with broad sweepers, crisp sea breeze, and cliffside lookouts."
                    ),
                    RouteEntity(
                        id = "r_angeles",
                        title = "Angeles Crest Climb",
                        difficulty = "HARD",
                        distanceMiles = 54,
                        durationText = "1h 50m",
                        elevationFeet = 6800,
                        description = "Technical mountain ascent up to Newcomb's Pass with hairpins and brisk altitude shifts."
                    ),
                    RouteEntity(
                        id = "r_latigo",
                        title = "Latigo Canyon Dash",
                        difficulty = "MODERATE",
                        distanceMiles = 28,
                        durationText = "55m",
                        elevationFeet = 2400,
                        description = "Ultra tight technical canyon section popular with seasoned cornering riders."
                    )
                )
                dao.insertRoutes(routes)

                dao.insertRide(
                    RideEntity(
                        id = "ride_active",
                        title = "Mulholland Loop",
                        groupName = "Canyon Carvers",
                        routeId = "r_mulholland",
                        status = "ACTIVE",
                        startTime = "09:14",
                        riderCount = 3,
                        currentMiles = 14,
                        totalMiles = 42,
                        eta = "10:32",
                        paceAvgMph = 62,
                        currentSpeedMph = 64,
                        leanAngleDegrees = 28,
                        activePttChannel = "Canyon Carvers Comms"
                    )
                )
            }
        }
    }
}
