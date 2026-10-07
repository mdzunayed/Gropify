package com.example.audio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class PttConnectionState {
    CONNECTED, CONNECTING, DISCONNECTED
}

data class PttState(
    val connectionState: PttConnectionState = PttConnectionState.CONNECTED,
    val isTransmitting: Boolean = false,
    val activeSpeaker: String? = null,
    val channelName: String = "Canyon Carvers Comms",
    val latencyMs: Int = 28,
    val audioWaveformLevel: Float = 0.0f,
    val connectedRiderCount: Int = 3
)

class PttManager(private val scope: CoroutineScope) {

    private val _state = MutableStateFlow(PttState())
    val state = _state.asStateFlow()

    private var transmitJob: Job? = null
    private var simulatedChatterJob: Job? = null

    init {
        startSimulatedRiderActivity()
    }

    fun startTransmitting() {
        transmitJob?.cancel()
        _state.value = _state.value.copy(
            isTransmitting = true,
            activeSpeaker = "You (Alex)",
            audioWaveformLevel = 0.6f
        )
        transmitJob = scope.launch(Dispatchers.Default) {
            while (isActive && _state.value.isTransmitting) {
                val level = Random.nextFloat() * 0.7f + 0.3f
                _state.value = _state.value.copy(audioWaveformLevel = level)
                delay(120)
            }
        }
    }

    fun stopTransmitting() {
        transmitJob?.cancel()
        _state.value = _state.value.copy(
            isTransmitting = false,
            activeSpeaker = null,
            audioWaveformLevel = 0.0f
        )
    }

    private fun startSimulatedRiderActivity() {
        simulatedChatterJob = scope.launch(Dispatchers.Default) {
            val names = listOf("Marcus Kane", "David Vance", "Sarah Lin")
            while (isActive) {
                delay(Random.nextLong(15000, 25000))
                if (!_state.value.isTransmitting) {
                    val speaker = names.random()
                    _state.value = _state.value.copy(
                        activeSpeaker = speaker,
                        audioWaveformLevel = 0.5f
                    )
                    delay(Random.nextLong(2500, 4500))
                    if (!_state.value.isTransmitting) {
                        _state.value = _state.value.copy(
                            activeSpeaker = null,
                            audioWaveformLevel = 0.0f
                        )
                    }
                }
            }
        }
    }
}
