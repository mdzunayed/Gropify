package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.GroupByDatabase
import com.example.data.repository.GroupByRepository
import com.example.ui.components.GroupByBottomNav
import com.example.ui.dialogs.BkashPaymentDialog
import com.example.ui.dialogs.CreateGroupDialog
import com.example.ui.dialogs.CreateRideDialog
import com.example.ui.dialogs.JoinByCodeDialog
import com.example.ui.dialogs.PhoneAuthDialog
import com.example.ui.dialogs.SosDialog
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveRideScreen
import com.example.ui.screens.RoutesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BgDark
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavTab

@Composable
fun GroupByApp(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = GroupByDatabase.getDatabase(context)
    val repository = GroupByRepository(database.dao())
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModel.provideFactory(repository, context.applicationContext)
    )

    // Request permissions for background telemetry & audio comms (Filter 2 & 3)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { /* Permissions granted callback */ }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        val needed = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needed.isNotEmpty()) {
            permissionLauncher.launch(needed.toTypedArray())
        }
    }

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isLiveRideActive by viewModel.isLiveRideActive.collectAsStateWithLifecycle()
    val showCreateGroup by viewModel.showCreateGroupDialog.collectAsStateWithLifecycle()
    val showJoinCode by viewModel.showJoinCodeDialog.collectAsStateWithLifecycle()
    val showCreateRide by viewModel.showCreateRideDialog.collectAsStateWithLifecycle()
    val showBkash by viewModel.showBkashDialog.collectAsStateWithLifecycle()
    val bkashState by viewModel.bkashState.collectAsStateWithLifecycle()
    val showSos by viewModel.showSosDialog.collectAsStateWithLifecycle()
    val sosCountdown by viewModel.sosCountdownSeconds.collectAsStateWithLifecycle()
    val sosDispatched by viewModel.sosDispatched.collectAsStateWithLifecycle()
    val showPhoneAuth by viewModel.showPhoneAuthDialog.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (isLiveRideActive) {
            // Live Ride & Telemetry full screen
            LiveRideScreen(viewModel = viewModel)
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = BgDark,
                bottomBar = {
                    GroupByBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition",
                    modifier = Modifier.padding(innerPadding)
                ) { tab ->
                    when (tab) {
                        NavTab.HOME -> HomeScreen(viewModel = viewModel)
                        NavTab.GROUPS -> GroupsScreen(viewModel = viewModel)
                        NavTab.ROUTES -> RoutesScreen(viewModel = viewModel)
                        NavTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                    }
                }
            }
        }

        // Modals & Dialogs
        if (showCreateGroup) {
            CreateGroupDialog(
                onDismiss = { viewModel.setCreateGroupDialogVisible(false) },
                onCreate = { name, desc, initials, color ->
                    viewModel.createNewGroup(name, desc, initials, color)
                }
            )
        }

        if (showJoinCode) {
            JoinByCodeDialog(
                onDismiss = { viewModel.setJoinCodeDialogVisible(false) },
                onJoin = { code ->
                    viewModel.setJoinCodeDialogVisible(false)
                    viewModel.selectTab(NavTab.GROUPS)
                }
            )
        }

        if (showCreateRide) {
            CreateRideDialog(
                onDismiss = { viewModel.setCreateRideDialogVisible(false) },
                onCreate = {
                    viewModel.setCreateRideDialogVisible(false)
                    viewModel.openLiveRide()
                }
            )
        }

        if (showBkash) {
            BkashPaymentDialog(
                state = bkashState,
                onPhoneChange = { viewModel.updateBkashPhone(it) },
                onOtpChange = { viewModel.updateBkashOtp(it) },
                onPinChange = { viewModel.updateBkashPin(it) },
                onSubmitStep = { viewModel.submitBkashStep() },
                onDismiss = { viewModel.closeBkashDialog() }
            )
        }

        if (showSos) {
            SosDialog(
                countdownSeconds = sosCountdown,
                isDispatched = sosDispatched,
                onCancel = { viewModel.cancelSos() }
            )
        }

        if (showPhoneAuth) {
            PhoneAuthDialog(
                currentPhone = currentUser?.phone ?: "+1 (555) 000-0000",
                onDismiss = { viewModel.setPhoneAuthDialogVisible(false) },
                onVerified = { viewModel.setPhoneAuthDialogVisible(false) }
            )
        }
    }
}
