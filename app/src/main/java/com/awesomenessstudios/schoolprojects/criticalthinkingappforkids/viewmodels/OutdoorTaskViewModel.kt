package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.OutdoorTask
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.outdoorTaskCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutdoorTaskViewModel @Inject constructor() : ViewModel() {

    private val _timeRemaining = MutableStateFlow(0L)
    val timeRemaining: StateFlow<Long> = _timeRemaining

    // LiveData to observe fetched tasks
    private val _tasks = MutableLiveData<List<OutdoorTask>>()
    val tasks: LiveData<List<OutdoorTask>> = _tasks

    // Function to fetch tasks based on the criteria
    fun fetchOutdoorTasks(childStage: String, difficulty: String, category: String) {
        outdoorTaskCollectionRef
            .whereEqualTo("childStage", childStage)
            .whereEqualTo("difficulty", difficulty)
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                val taskList = result.toObjects(OutdoorTask::class.java)
                _tasks.value = taskList
            }
            .addOnFailureListener { exception ->
                Log.e("OutdoorTaskViewModel", "Error fetching tasks: ", exception)
            }
    }

    fun convertDurationToMillis(duration: String): Long {
        // Assuming duration is in format "20 minutes"
        val minutes = duration.split(" ")[0].toInt()
        return minutes * 60 * 1000L
    }

    fun startCountdown(totalTime: Long) {
        viewModelScope.launch {
            for (time in totalTime downTo 0 step 1000L) {
                _timeRemaining.value = time
                delay(1000L)
            }
        }
    }

    fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}