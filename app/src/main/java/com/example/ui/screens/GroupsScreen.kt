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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GroupEntity
import com.example.ui.components.FrostedCard
import com.example.ui.components.GroupByOrangeButton
import com.example.ui.components.GroupByOutlinePillButton
import com.example.ui.components.UserAvatar
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val groups by viewModel.allGroups.collectAsStateWithLifecycle()
    val selectedGroup by viewModel.selectedGroup.collectAsStateWithLifecycle()

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
                    text = "Groups",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your riding communities",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // Create Group Button
        item {
            GroupByOutlinePillButton(
                text = "Create New Group",
                icon = Icons.Default.Add,
                onClick = { viewModel.setCreateGroupDialogVisible(true) }
            )
        }

        // List of groups
        items(groups) { group ->
            GroupListItemCard(
                group = group,
                onClick = { viewModel.openGroupDetail(group) }
            )
        }
    }

    // Group Detail Modal Bottom Sheet
    selectedGroup?.let { group ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeGroupDetail() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = SurfaceDark,
            contentColor = TextPrimary
        ) {
            GroupDetailSheetContent(
                group = group,
                onClose = { viewModel.closeGroupDetail() },
                onJoinComms = {
                    viewModel.closeGroupDetail()
                    viewModel.openLiveRide()
                }
            )
        }
    }
}

@Composable
fun GroupListItemCard(
    group: GroupEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = try {
                Color(android.graphics.Color.parseColor(group.colorHex))
            } catch (e: Exception) {
                PrimaryOrange
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.initials,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "👥 ${group.memberCount}  •  🏍 ${group.rideCount} rides",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun GroupDetailSheetContent(
    group: GroupEntity,
    onClose: () -> Unit,
    onJoinComms: () -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(group.colorHex))
    } catch (e: Exception) {
        PrimaryOrange
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.initials,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = group.name,
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = group.description.ifEmpty { "Active motorcycle riding collective for canyon carving, safety awareness, and coordinated group navigation." },
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceCard)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${group.memberCount}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "MEMBERS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${group.rideCount}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "RIDES", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = group.inviteCode, color = AccentCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "INVITE CODE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Active Riders Online",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        val riders = listOf(
            Triple("Alex Rivera", "Lead Rider · 64 mph", "#FF6B1A"),
            Triple("Marcus Kane", "Wingman · 62 mph", "#00E5FF"),
            Triple("David Vance", "Sweep · 61 mph", "#10B981")
        )

        riders.forEach { (name, role, hex) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    initials = name.split(" ").map { it.first() }.joinToString(""),
                    bgColor = Color(android.graphics.Color.parseColor(hex)),
                    size = 38.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = role, color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        GroupByOrangeButton(
            text = "Join Group Comms & Ride",
            icon = Icons.Default.Mic,
            onClick = onJoinComms
        )
    }
}
