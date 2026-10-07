package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GroupEntity
import com.example.data.model.RideEntity
import com.example.data.model.RouteEntity
import com.example.ui.components.DifficultyTag
import com.example.ui.components.FrostedCard
import com.example.ui.components.GroupByOrangeButton
import com.example.ui.components.PointsBadge
import com.example.ui.components.UserAvatar
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavTab

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val activeRide by viewModel.activeRide.collectAsStateWithLifecycle()
    val groups by viewModel.allGroups.collectAsStateWithLifecycle()
    val routes by viewModel.allRoutes.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Header Profile & Points
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(
                        initials = user?.avatarInitials ?: "AR",
                        bgColor = Color(0xFFE65100),
                        isOnline = user?.isOnline ?: true,
                        size = 50.dp,
                        modifier = Modifier.clickable { viewModel.setPhoneAuthDialogVisible(true) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Welcome back",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = user?.name ?: "Alex Rivera",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                PointsBadge(points = user?.points ?: 1240)
            }
        }

        // 2. Active Ride Card (mulholland Loop)
        item {
            activeRide?.let { ride ->
                ActiveRideCard(
                    ride = ride,
                    isMetric = settings.metricUnitsEnabled,
                    onResumeRide = { viewModel.openLiveRide() }
                )
            }
        }

        // 3. Quick Action Grid (Create Ride, Join by Code)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Create Ride",
                    icon = Icons.Default.Add,
                    iconBg = PrimaryOrange.copy(alpha = 0.15f),
                    iconTint = PrimaryOrange,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setCreateRideDialogVisible(true) }
                )

                QuickActionCard(
                    title = "Join by Code",
                    icon = Icons.Default.QrCodeScanner,
                    iconBg = AccentCyan.copy(alpha = 0.15f),
                    iconTint = AccentCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setJoinCodeDialogVisible(true) }
                )
            }
        }

        // 4. Your Groups Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Groups",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.selectTab(NavTab.GROUPS) }
                    ) {
                        Text(
                            text = "See all",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(groups.take(4)) { group ->
                        HomeGroupMiniCard(
                            group = group,
                            onClick = { viewModel.openGroupDetail(group) }
                        )
                    }
                }
            }
        }

        // 5. Recent Routes Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Routes",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.selectTab(NavTab.ROUTES) }
                    ) {
                        Text(
                            text = "Explore",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                routes.take(2).forEach { route ->
                    HomeRouteMiniCard(
                        route = route,
                        onStart = { viewModel.startRouteRide(route) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ActiveRideCard(
    ride: RideEntity,
    isMetric: Boolean,
    onResumeRide: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Badge row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryOrange.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(PrimaryOrange)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ACTIVE RIDE",
                            color = PrimaryOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Text(
                    text = "${ride.riderCount} riders",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ride.title,
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Started ${ride.startTime} · ${ride.groupName}",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3 stats columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val distUnit = if (isMetric) "km" else "mi"
                val distMultiplier = if (isMetric) 1.609 else 1.0
                val currentDist = (ride.currentMiles * distMultiplier).toInt()
                val totalDist = (ride.totalMiles * distMultiplier).toInt()

                Column {
                    Text(
                        text = "DISTANCE",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                                append("$currentDist ")
                            }
                            withStyle(SpanStyle(color = TextSecondary, fontSize = 14.sp)) {
                                append("/$totalDist $distUnit")
                            }
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp)
                        .background(BorderSubtle)
                )

                Column {
                    Text(
                        text = "ETA",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ride.eta,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp)
                        .background(BorderSubtle)
                )

                Column {
                    Text(
                        text = "PACE",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = AccentCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                                append("${ride.paceAvgMph} ")
                            }
                            withStyle(SpanStyle(color = AccentCyan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)) {
                                append("avg")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            GroupByOrangeButton(
                text = "Resume Ride",
                icon = Icons.Default.Navigation,
                onClick = onResumeRide
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HomeGroupMiniCard(
    group: GroupEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier.width(170.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            val color = try {
                Color(android.graphics.Color.parseColor(group.colorHex))
            } catch (e: Exception) {
                PrimaryOrange
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.initials,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = group.name,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "👥 ${group.memberCount}  ·  🏍 ${group.rideCount}",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun HomeRouteMiniCard(
    route: RouteEntity,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DifficultyTag(difficulty = route.difficulty)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${route.distanceMiles} mi · ${route.durationText}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = route.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceCardElevated)
                    .border(1.dp, BorderHighlight, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Start",
                    color = PrimaryOrange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
