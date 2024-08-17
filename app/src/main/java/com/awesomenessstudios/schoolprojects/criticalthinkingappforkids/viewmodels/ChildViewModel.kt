package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChildViewModel @Inject constructor(): ViewModel() {

    private val _childDetails = MutableLiveData<Child?>()
    val childDetails: LiveData<Child?> get() = _childDetails

    fun fetchChildDetails(childId: String) {
        Common.childrenCollectionRef
            .document(childId)
            .get()
            .addOnSuccessListener { document ->
                val child = document.toObject(Child::class.java)
                _childDetails.postValue(child)
            }
            .addOnFailureListener {
                // Handle error
                _childDetails.postValue(null)
            }
    }
}