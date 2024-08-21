package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Video
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.videoCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(): ViewModel() {

    private val _videoData = MutableLiveData<Video?>()
    val videoData: LiveData<Video?> get() = _videoData

    fun fetchVideoData(childStage: String, category: String) {
        videoCollectionRef
            .whereEqualTo("childStage", childStage)
            .whereEqualTo("category", category)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val video = documents.documents[0].toObject(Video::class.java)
                    _videoData.postValue(video)
                } else {
                    _videoData.postValue(null)
                }
            }
            .addOnFailureListener {
                _videoData.postValue(null)
            }
    }

}