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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Vrpano
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FrostedCard
import com.example.ui.components.UserAvatar
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.PrimaryOrangeDim
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // User Profile Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setPhoneAuthDialogVisible(true) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    initials = user?.avatarInitials ?: "AR",
                    bgColor = Color(0xFFE65100),
                    size = 64.dp,
                    isOnline = user?.isOnline ?: true
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user?.name ?: "Alex Rivera",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = user?.phone ?: "+1 (555) 000-0000",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // GroupBy Pro Banner (bKash integration trigger)
        item {
            FrostedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(PrimaryOrange.copy(alpha = 0.8f), AccentCyan.copy(alpha = 0.4f))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                onClick = { viewModel.openBkashProDialog() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(PrimaryOrange.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = PrimaryOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "GroupBy Pro",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (user?.isPro == true) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ACTIVE",
                                        color = PrimaryOrange,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (user?.isPro == true) "Premium unlocked · bKash Verified" else "Unlock premium riding tools (bKash)",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Section: RIDE & SAFETY
        item {
            Column {
                Text(
                    text = "RIDE & SAFETY",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                FrostedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        SettingsToggleRow(
                            icon = Icons.Default.Security,
                            title = "Crash Detection",
                            subtitle = "Auto-dispatch SOS on impact",
                            checked = settings.crashDetectionEnabled,
                            onCheckedChange = { viewModel.toggleCrashDetection(it) }
                        )

                        SettingsDivider()

                        SettingsToggleRow(
                            icon = Icons.Default.Vrpano,
                            title = "Helmet HUD Mirror",
                            subtitle = "Cast telemetry to smart visor",
                            checked = settings.helmetHudMirrorEnabled,
                            onCheckedChange = { viewModel.toggleHelmetHudMirror(it) }
                        )

                        SettingsDivider()

                        SettingsToggleRow(
                            icon = Icons.Default.Straighten,
                            title = "Metric Units",
                            subtitle = "Switch to km / kmh",
                            checked = settings.metricUnitsEnabled,
                            onCheckedChange = { viewModel.toggleMetricUnits(it) }
                        )
                    }
                }
            }
        }

        // Section: NOTIFICATIONS
        item {
            Column {
                Text(
                    text = "NOTIFICATIONS",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                FrostedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        SettingsToggleRow(
                            icon = Icons.Default.Notifications,
                            title = "Ride Alerts",
                            subtitle = "Starts, stops, group events",
                            checked = settings.rideAlertsEnabled,
                            onCheckedChange = { viewModel.toggleRideAlerts(it) }
                        )

                        SettingsDivider()

                        SettingsClickableRow(
                            icon = Icons.Default.Language,
                            title = "Language",
                            trailingText = settings.language,
                            onClick = { /* language toggle */ }
                        )
                    }
                }
            }
        }

        // Section: ACCOUNT
        item {
            Column {
                Text(
                    text = "ACCOUNT",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                FrostedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        SettingsClickableRow(
                            icon = Icons.Default.HelpOutline,
                            title = "Help & Support",
                            onClick = { /* help */ }
                        )

                        SettingsDivider()

                        SettingsClickableRow(
                            icon = Icons.Default.Security,
                            title = "Emergency SOS & 999 Settings",
                            trailingText = user?.emergencyContactName ?: "Sarah",
                            onClick = { viewModel.triggerSos() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfaceCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextPrimary,
                checkedTrackColor = PrimaryOrange,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceCardElevated
            )
        )
    }
}

@Composable
private fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfaceCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 68.dp, end = 16.dp),
        thickness = 1.dp,
        color = BorderSubtle.copy(alpha = 0.6f)
    )
}
