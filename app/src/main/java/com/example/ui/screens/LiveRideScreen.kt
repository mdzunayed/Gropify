package com.example.ui.screens

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.PttState
import com.example.data.model.RideEntity
import com.example.data.repository.RiderStatus
import com.example.ui.components.FrostedCard
import com.example.ui.components.UserAvatar
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertRedDim
import com.example.ui.theme.AlertRedGlow
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.PrimaryOrangeDark
import com.example.ui.theme.PrimaryOrangeDim
import com.example.ui.theme.PrimaryOrangeLight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LiveRideScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeRide by viewModel.activeRide.collectAsStateWithLifecycle()
    val pttState by viewModel.pttState.collectAsStateWithLifecycle()
    val connectedRiders by viewModel.connectedRiders.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val ride = activeRide ?: RideEntity(
        id = "ride_fallback",
        title = "Morning Thunder",
        groupName = "Canyon Carvers",
        routeId = "r_mulholland",
        status = "ACTIVE",
        startTime = "09:14",
        riderCount = 8,
        currentMiles = 12,
        totalMiles = 38,
        eta = "10:14",
        paceAvgMph = 62,
        currentSpeedMph = 84,
        leanAngleDegrees = 26
    )

    val speedValue = if (settings.metricUnitsEnabled) (ride.currentSpeedMph * 1.609).toInt() else ride.currentSpeedMph
    val speedUnit = if (settings.metricUnitsEnabled) "km/h" else "mph"
    val distValue = if (settings.metricUnitsEnabled) String.format("%.1f", ride.currentMiles * 1.609) else "${ride.currentMiles}"
    val distUnit = if (settings.metricUnitsEnabled) "km" else "mi"

    // Pulsing animation for active rider indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
    ) {
        // High Density Header: px-4 py-3 flex items-center justify-between
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.closeLiveRide() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceCard)
                        .border(1.dp, BorderSubtle, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Minimize",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = ride.title,
                        color = TextPrimary,
                        fontSize = 18.sp, // text-[18px] font-bold leading-tight
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(PrimaryOrange.copy(alpha = pulseAlpha))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${connectedRiders.size + 1} Riders Active", // text-[12px] text-[#FF6B1A] font-medium
                            color = PrimaryOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Right side avatar: w-11 h-11 rounded-full bg-[#1C1C1E] border border-[#2C2C2E]
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(PrimaryOrange, PrimaryOrangeLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser?.avatarInitials ?: "AR",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Main Frame: flex-1 relative mx-4 rounded-[32px] bg-[#141417] border border-[#2C2C2E] overflow-hidden
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderSubtle, RoundedCornerShape(32.dp))
        ) {
            // Map Canvas with Twisties and High Density Dot Matrix overlay
            CanyonMapCanvas(
                modifier = Modifier.fillMaxSize(),
                riders = connectedRiders
            )

            // Top High Density 3-Column HUD Grid: absolute top-4 left-4 right-4 grid grid-cols-3 gap-2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Speed
                HighDensityMetricCell(
                    label = "SPEED",
                    value = "$speedValue",
                    unit = speedUnit,
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                )

                // Metric 2: Dist
                HighDensityMetricCell(
                    label = "DIST",
                    value = distValue,
                    unit = distUnit,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )

                // Metric 3: ETA
                HighDensityMetricCell(
                    label = "ETA",
                    value = ride.eta,
                    unit = "",
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
            }

            // Compact Turn Warning Banner (Subtle overlay)
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp, start = 14.dp, end = 14.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceCard.copy(alpha = 0.88f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PrimaryOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Next: Right hairpin in 1.2 mi · 26° lean",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // High Density SOS Floating Button: absolute bottom-4 left-4 w-12 h-12 rounded-full bg-[#FF2D55]
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(48.dp)
                    .shadow(16.dp, CircleShape, spotColor = AlertRedGlow)
                    .clip(CircleShape)
                    .background(AlertRed)
                    .clickable { viewModel.triggerSos() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SOS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Section: px-4 py-4/5 with Hold to Talk and WebRTC voice comms
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Speaker status row & Waveform
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (pttState.isTransmitting) AlertRed else if (pttState.activeSpeaker != null) AccentCyan else EasyGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when {
                            pttState.isTransmitting -> "Transmitting..."
                            pttState.activeSpeaker != null -> "${pttState.activeSpeaker} speaking"
                            else -> "Channel: ${pttState.channelName}"
                        },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "End Ride",
                    color = AlertRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AlertRedDim)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .pointerInput(Unit) {
                            detectTapGestures { viewModel.endRide() }
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Audio Waveform Visualization
            WaveformDisplay(
                level = pttState.audioWaveformLevel,
                isTransmitting = pttState.isTransmitting
            )

            Spacer(modifier = Modifier.height(12.dp))

            // High Density Tactile Hold-to-Talk Button: w-full h-[64px] bg-[#FF6B1A] rounded-[24px]
            var isPressed by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp) // h-[64px]
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = PrimaryOrange.copy(alpha = 0.35f))
                    .clip(RoundedCornerShape(24.dp)) // rounded-[24px]
                    .background(if (isPressed) PrimaryOrangeDark else PrimaryOrange)
                    .pointerInteropFilter { event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                isPressed = true
                                viewModel.onPttDown()
                                true
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                isPressed = false
                                viewModel.onPttUp()
                                true
                            }
                            else -> false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // White translucent circle icon capsule: w-8 h-8 rounded-full bg-white/20
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "HOLD TO TALK", // text-[16px] font-bold uppercase tracking-widest text-white
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}



@Composable
fun HighDensityMetricCell(
    label: String,
    value: String,
    unit: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier
) {
    // bg-[#1C1C1E]/90 backdrop-blur-md rounded-2xl p-2.5 border border-white/5
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard.copy(alpha = 0.92f),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderHighlight)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                color = TextMuted, // text-white/40
                fontSize = 10.sp, // text-[10px] uppercase tracking-wider font-bold
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    color = if (isPrimary) PrimaryOrange else TextPrimary,
                    fontSize = 18.sp, // text-[18px] font-mono font-bold
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = unit,
                        color = TextSecondary, // text-[10px] ml-0.5 text-white/60
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WaveformDisplay(level: Float, isTransmitting: Boolean) {
    val barColor = if (isTransmitting) PrimaryOrange else AccentCyan
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until 24) {
            val scale = if (level > 0.05f) {
                ((kotlin.math.sin(i * 0.35 + System.currentTimeMillis() * 0.01) * 0.5 + 0.5) * level).coerceIn(0.15, 1.0).toFloat()
            } else {
                0.2f
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 1.5.dp)
                    .width(3.dp)
                    .height((20 * scale).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (scale > 0.25f) barColor else TextMuted.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun CanyonMapCanvas(
    modifier: Modifier = Modifier,
    riders: List<RiderStatus>
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Dark terrain canvas
        drawRect(color = SurfaceDark)

        // Dot matrix grid pattern: 20px x 20px with #2C2C2E dots
        val dotSpacing = 32f
        val dotRadius = 1.5f
        val dotColor = Color(0xFF2C2C2E)
        var x = 0f
        while (x < w) {
            var y = 0f
            while (y < h) {
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(x, y)
                )
                y += dotSpacing
            }
            x += dotSpacing
        }

        // Draw canyon contour line
        val contourColor = Color(0xFF202026)
        drawCircle(
            color = contourColor,
            radius = w * 0.65f,
            center = Offset(w * 0.2f, h * 0.4f),
            style = Stroke(width = 1.5f)
        )
        drawCircle(
            color = contourColor,
            radius = w * 0.45f,
            center = Offset(w * 0.8f, h * 0.7f),
            style = Stroke(width = 1.5f)
        )

        // Road geometry path
        val roadPath = Path().apply {
            moveTo(w * 0.15f, h * 0.95f)
            cubicTo(
                w * 0.2f, h * 0.8f,
                w * 0.7f, h * 0.75f,
                w * 0.75f, h * 0.6f
            )
            cubicTo(
                w * 0.8f, h * 0.45f,
                w * 0.3f, h * 0.4f,
                w * 0.35f, h * 0.25f
            )
            cubicTo(
                w * 0.4f, h * 0.15f,
                w * 0.65f, h * 0.1f,
                w * 0.7f, h * 0.02f
            )
        }

        // Road border casing
        drawPath(
            path = roadPath,
            color = Color(0xFF25252D),
            style = Stroke(width = 34f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Road asphalt surface
        drawPath(
            path = roadPath,
            color = Color(0xFF18181F),
            style = Stroke(width = 26f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Center navigation guide line (#FF6B1A)
        drawPath(
            path = roadPath,
            color = PrimaryOrange.copy(alpha = 0.85f),
            style = Stroke(
                width = 6f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Position of Lead Rider (Alex)
        val leadPos = Offset(w * 0.52f, h * 0.52f)

        // Pulsing radar ring around lead rider
        drawCircle(
            color = PrimaryOrange.copy(alpha = 0.25f),
            radius = 30f,
            center = leadPos
        )

        // Lead Rider marker
        drawCircle(
            color = PrimaryOrange,
            radius = 14f,
            center = leadPos
        )
        drawCircle(
            color = Color.White,
            radius = 6f,
            center = leadPos
        )

        // Secondary Rider 1: Marcus Kane (Cyan, trailing)
        val rider2Pos = Offset(w * 0.60f, h * 0.58f)
        drawCircle(
            color = AccentCyan,
            radius = 11f,
            center = rider2Pos
        )
        drawCircle(
            color = Color.White,
            radius = 4f,
            center = rider2Pos
        )

        // Secondary Rider 2: David Vance (Green, sweep)
        val rider3Pos = Offset(w * 0.68f, h * 0.66f)
        drawCircle(
            color = EasyGreen,
            radius = 11f,
            center = rider3Pos
        )
        drawCircle(
            color = Color.White,
            radius = 4f,
            center = rider3Pos
        )
    }
}
