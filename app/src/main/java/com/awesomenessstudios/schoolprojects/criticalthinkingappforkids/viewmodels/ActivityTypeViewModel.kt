package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.GameDetails
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.childrenCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ActivityTypeViewModel  @Inject constructor (): ViewModel() {

    private val TAG = "ActivityTypeViewModel"
    private val _gameDetails = MutableLiveData<GameDetails?>()
    val gameDetails: LiveData<GameDetails?> get() = _gameDetails


    fun fetchChildDetails(childId: String, categoryKey: String) {
        Log.d(TAG, "fetchChildDetails: $categoryKey")
        viewModelScope.launch {
            try {
                val document = childrenCollectionRef.document(childId).collection("Categories").document(categoryKey).get().await()
                if (document.exists()) {
                    val gameDetails = document.toObject(GameDetails::class.java)
                    Log.d(TAG, "fetchChildDetails: $gameDetails")
                    _gameDetails.value = gameDetails
                } else {
                    // Handle the case where the document does not exist
                }
            } catch (e: Exception) {
                // Handle any errors
            }
        }
    }

}