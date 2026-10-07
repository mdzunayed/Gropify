package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.audio.PttManager
import com.example.data.model.GroupEntity
import com.example.data.model.RideEntity
import com.example.data.model.RouteEntity
import com.example.data.model.UserEntity
import com.example.data.repository.GroupByRepository
import com.example.data.repository.RideSettings
import com.example.data.repository.RiderStatus
import com.example.service.RideTrackingService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class NavTab {
    HOME, GROUPS, ROUTES, SETTINGS
}

data class BkashPaymentState(
    val step: Int = 1, // 1: invoice overview, 2: phone & otp, 3: pin, 4: success
    val amount: String = "৳ 499",
    val invoiceId: String = "INV-GB-88491",
    val customerPhone: String = "01711-234567",
    val otp: String = "",
    val pin: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(
    private val repository: GroupByRepository,
    private val appContext: Context
) : ViewModel() {

    val pttManager = PttManager(viewModelScope)
    val pttState = pttManager.state

    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allGroups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRoutes: StateFlow<List<RouteEntity>> = repository.allRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeRide: StateFlow<RideEntity?> = repository.activeRide
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val settings: StateFlow<RideSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RideSettings())

    val connectedRiders: StateFlow<List<RiderStatus>> = repository.connectedRiders

    // Navigation & Modal States
    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab = _currentTab.asStateFlow()

    private val _isLiveRideActive = MutableStateFlow(false)
    val isLiveRideActive = _isLiveRideActive.asStateFlow()

    private val _selectedGroup = MutableStateFlow<GroupEntity?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()

    private val _showCreateGroupDialog = MutableStateFlow(false)
    val showCreateGroupDialog = _showCreateGroupDialog.asStateFlow()

    private val _showJoinCodeDialog = MutableStateFlow(false)
    val showJoinCodeDialog = _showJoinCodeDialog.asStateFlow()

    private val _showCreateRideDialog = MutableStateFlow(false)
    val showCreateRideDialog = _showCreateRideDialog.asStateFlow()

    private val _showBkashDialog = MutableStateFlow(false)
    val showBkashDialog = _showBkashDialog.asStateFlow()

    private val _bkashState = MutableStateFlow(BkashPaymentState())
    val bkashState = _bkashState.asStateFlow()

    private val _showSosDialog = MutableStateFlow(false)
    val showSosDialog = _showSosDialog.asStateFlow()

    private val _sosCountdownSeconds = MutableStateFlow(3)
    val sosCountdownSeconds = _sosCountdownSeconds.asStateFlow()

    private val _sosDispatched = MutableStateFlow(false)
    val sosDispatched = _sosDispatched.asStateFlow()

    private val _showPhoneAuthDialog = MutableStateFlow(false)
    val showPhoneAuthDialog = _showPhoneAuthDialog.asStateFlow()

    private var rideSimulationJob: Job? = null
    private var sosJob: Job? = null

    init {
        // Start background telemetry simulation if an active ride exists
        startTelemetryLoop()
    }

    fun selectTab(tab: NavTab) {
        _currentTab.value = tab
    }

    fun openLiveRide() {
        _isLiveRideActive.value = true
        val ride = activeRide.value
        RideTrackingService.startTracking(
            appContext,
            ride?.title ?: "Mulholland Loop",
            ride?.currentSpeedMph ?: 64
        )
    }

    fun closeLiveRide() {
        _isLiveRideActive.value = false
    }

    fun startRouteRide(route: RouteEntity) {
        viewModelScope.launch {
            repository.startOrResumeRide(route)
            openLiveRide()
        }
    }

    fun endRide() {
        viewModelScope.launch {
            repository.endActiveRide()
            _isLiveRideActive.value = false
            RideTrackingService.stopTracking(appContext)
        }
    }

    fun openGroupDetail(group: GroupEntity) {
        _selectedGroup.value = group
    }

    fun closeGroupDetail() {
        _selectedGroup.value = null
    }

    fun setCreateGroupDialogVisible(visible: Boolean) {
        _showCreateGroupDialog.value = visible
    }

    fun setJoinCodeDialogVisible(visible: Boolean) {
        _showJoinCodeDialog.value = visible
    }

    fun setCreateRideDialogVisible(visible: Boolean) {
        _showCreateRideDialog.value = visible
    }

    fun setPhoneAuthDialogVisible(visible: Boolean) {
        _showPhoneAuthDialog.value = visible
    }

    fun createNewGroup(name: String, description: String, initials: String, colorHex: String) {
        viewModelScope.launch {
            repository.createGroup(name, description, initials, colorHex)
            _showCreateGroupDialog.value = false
        }
    }

    // PTT Actions
    fun onPttDown() {
        pttManager.startTransmitting()
    }

    fun onPttUp() {
        pttManager.stopTransmitting()
    }

    // SOS & Emergency
    fun triggerSos() {
        _showSosDialog.value = true
        _sosCountdownSeconds.value = 3
        _sosDispatched.value = false
        sosJob?.cancel()
        sosJob = viewModelScope.launch {
            for (sec in 3 downTo 1) {
                _sosCountdownSeconds.value = sec
                delay(1000)
            }
            _sosCountdownSeconds.value = 0
            _sosDispatched.value = true
        }
    }

    fun cancelSos() {
        sosJob?.cancel()
        _showSosDialog.value = false
        _sosDispatched.value = false
        _sosCountdownSeconds.value = 3
    }

    // Settings
    fun toggleCrashDetection(enabled: Boolean) {
        repository.updateSettings { it.copy(crashDetectionEnabled = enabled) }
    }

    fun toggleHelmetHudMirror(enabled: Boolean) {
        repository.updateSettings { it.copy(helmetHudMirrorEnabled = enabled) }
    }

    fun toggleMetricUnits(enabled: Boolean) {
        repository.updateSettings { it.copy(metricUnitsEnabled = enabled) }
    }

    fun toggleRideAlerts(enabled: Boolean) {
        repository.updateSettings { it.copy(rideAlertsEnabled = enabled) }
    }

    // bKash GroupBy Pro Flow
    fun openBkashProDialog() {
        _bkashState.value = BkashPaymentState(
            invoiceId = "INV-GB-${(10000..99999).random()}",
            step = 1
        )
        _showBkashDialog.value = true
    }

    fun closeBkashDialog() {
        _showBkashDialog.value = false
    }

    fun updateBkashPhone(phone: String) {
        _bkashState.value = _bkashState.value.copy(customerPhone = phone)
    }

    fun updateBkashOtp(otp: String) {
        _bkashState.value = _bkashState.value.copy(otp = otp)
    }

    fun updateBkashPin(pin: String) {
        _bkashState.value = _bkashState.value.copy(pin = pin)
    }

    fun submitBkashStep() {
        val current = _bkashState.value
        viewModelScope.launch {
            _bkashState.value = current.copy(isLoading = true, errorMessage = null)
            delay(1000) // Realistic bKash API handshake delay
            when (current.step) {
                1 -> {
                    _bkashState.value = current.copy(step = 2, isLoading = false)
                }
                2 -> {
                    if (current.customerPhone.length < 10) {
                        _bkashState.value = current.copy(isLoading = false, errorMessage = "Enter valid 11-digit bKash number")
                        return@launch
                    }
                    _bkashState.value = current.copy(step = 3, isLoading = false)
                }
                3 -> {
                    if (current.pin.length < 4) {
                        _bkashState.value = current.copy(isLoading = false, errorMessage = "Please enter your 5-digit bKash PIN")
                        return@launch
                    }
                    // Webhook verification & Pro activation
                    repository.setProStatus(true)
                    _bkashState.value = current.copy(step = 4, isLoading = false)
                }
                4 -> {
                    _showBkashDialog.value = false
                }
            }
        }
    }

    private fun startTelemetryLoop() {
        rideSimulationJob = viewModelScope.launch {
            while (isActive) {
                delay(2500)
                val current = activeRide.value
                if (current != null && current.status == "ACTIVE") {
                    val newSpeed = (60..68).random()
                    val newLean = (15..32).random()
                    val nextMiles = if (current.currentMiles < current.totalMiles) {
                        current.currentMiles + if (Random.nextBoolean()) 1 else 0
                    } else {
                        current.currentMiles
                    }
                    repository.updateActiveRideTelemetry(nextMiles, newSpeed, newLean)
                }
            }
        }
    }

    companion object {
        fun provideFactory(repository: GroupByRepository, context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(repository, context) as T
                }
            }
    }
}
