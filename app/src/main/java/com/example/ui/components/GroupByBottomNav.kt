package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NavTab

@Composable
fun GroupByBottomNav(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BgDark)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = SurfaceCard // border-t border-[#1C1C1E]
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp) // h-[72px]
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTabItem(
                label = "Home",
                selected = currentTab == NavTab.HOME,
                selectedIcon = Icons.Filled.GridView,
                unselectedIcon = Icons.Outlined.GridView,
                onClick = { onTabSelected(NavTab.HOME) }
            )

            NavTabItem(
                label = "Groups",
                selected = currentTab == NavTab.GROUPS,
                selectedIcon = Icons.Filled.People,
                unselectedIcon = Icons.Outlined.People,
                onClick = { onTabSelected(NavTab.GROUPS) }
            )

            NavTabItem(
                label = "Routes",
                selected = currentTab == NavTab.ROUTES,
                selectedIcon = Icons.Filled.Navigation,
                unselectedIcon = Icons.Outlined.Navigation,
                onClick = { onTabSelected(NavTab.ROUTES) }
            )

            NavTabItem(
                label = "Settings",
                selected = currentTab == NavTab.SETTINGS,
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                onClick = { onTabSelected(NavTab.SETTINGS) }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    selected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selected) {
            // High Density active icon with rounded badge
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = selectedIcon,
                    contentDescription = label,
                    tint = PrimaryOrange,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                color = PrimaryOrange,
                fontSize = 10.sp, // text-[10px] font-bold text-[#FF6B1A]
                fontWeight = FontWeight.Bold
            )
        } else {
            // High Density inactive icon (opacity 40%)
            Box(
                modifier = Modifier.size(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = unselectedIcon,
                    contentDescription = label,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.4f), // text-[10px] font-medium opacity-40
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

