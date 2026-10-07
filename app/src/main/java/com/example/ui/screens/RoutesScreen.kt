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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RouteEntity
import com.example.ui.components.DifficultyTag
import com.example.ui.components.FrostedCard
import com.example.ui.components.GroupByOutlinePillButton
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun RoutesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val routes by viewModel.allRoutes.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Routes",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Discover and save rides",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // Start a Ride pill button
        item {
            GroupByOutlinePillButton(
                text = "Start a Ride",
                icon = Icons.Outlined.Navigation,
                onClick = { viewModel.openLiveRide() }
            )
        }

        // Route cards
        items(routes) { route ->
            RouteCardItem(
                route = route,
                onStartRoute = { viewModel.startRouteRide(route) }
            )
        }
    }
}

@Composable
fun RouteCardItem(
    route: RouteEntity,
    onStartRoute: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Difficulty tag and arrow icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyTag(difficulty = route.difficulty)

                Icon(
                    imageVector = Icons.Outlined.Navigation,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = route.title,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${route.distanceMiles} mi",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "  •  ${route.durationText}  •  ${"%,d".format(route.elevationFeet)} ft",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Start Route dark pill button with High Density border highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .background(SurfaceCardElevated)
                    .border(1.dp, BorderHighlight, RoundedCornerShape(23.dp))
                    .clickable(onClick = onStartRoute),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start Route",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
