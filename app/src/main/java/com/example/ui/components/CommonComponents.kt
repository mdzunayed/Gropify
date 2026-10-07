package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.HardRed
import com.example.ui.theme.ModerateAmber
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.PrimaryOrangeDark
import com.example.ui.theme.PrimaryOrangeLight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun UserAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    bgColor: Color = Color(0xFFE65100),
    isOnline: Boolean = true
) {
    // High Density styling: rounded container border-[#2C2C2E] with gradient from-[#FF6B1A] to-[#FF9D66]
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(SurfaceCard)
            .border(1.dp, BorderSubtle, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size - 8.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(PrimaryOrange, PrimaryOrangeLight)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = ((size.value - 8.dp.value) * 0.42f).sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(BgDark)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(AccentCyan)
            )
        }
    }
}

@Composable
fun PointsBadge(
    points: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = PrimaryOrange,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "%,d".format(points),
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DifficultyTag(
    difficulty: String,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (difficulty.uppercase()) {
        "EASY" -> EasyGreen to "EASY"
        "HARD" -> HardRed to "HARD"
        else -> ModerateAmber to "MODERATE"
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun GroupByOrangeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = PrimaryOrange.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryOrange,
            contentColor = Color.White,
            disabledContainerColor = PrimaryOrange.copy(alpha = 0.4f)
        ),
        enabled = enabled
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun GroupByOutlinePillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceCard.copy(alpha = 0.85f),
            contentColor = TextPrimary
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun FrostedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceCard.copy(alpha = 0.95f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    } else {
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceCard.copy(alpha = 0.95f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
    }

    Box(modifier = cardModifier) {
        content()
    }
}

