package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Child
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Parent
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.childrenCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.mAuth
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.parentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ParentViewModel @Inject constructor(): ViewModel() {


    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> = _children.asStateFlow()

    val userName = mutableStateOf("")
    val loading = mutableStateOf(false)
    val openDialog = mutableStateOf<Boolean>(false)
    val addChildDialog = mutableStateOf<Boolean>(false)

    fun updateDialogStatus() {
        this.openDialog.value = !this.openDialog.value
    }

    fun updateAddChildDialogStatus() {
        this.addChildDialog.value = !this.addChildDialog.value
    }

    init {
        fetchParentDetails()
        fetchChildren()
    }

    private fun fetchParentDetails() {
        mAuth.currentUser?.let { user ->
            val userId = user.uid
            parentsCollectionRef.document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val parent = document.toObject(Parent::class.java)
                        userName.value = parent?.fullName ?: "User"
                    }
                }
        }
    }

    private fun fetchChildren() {
        mAuth.currentUser?.let { user ->
            val userId = user.uid
            childrenCollectionRef
                .whereEqualTo("childParent", userId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val childrenToAdd = mutableListOf<Child>()
                        childrenToAdd.clear()
                        for (doc in snapshot.documents) {
                            val child = doc.toObject(Child::class.java)
                            if (child != null) {
                                childrenToAdd.add(child)
                            }
                        }
                        _children.value = childrenToAdd
                    }
                }
        }
    }

    fun addChildToFirestore(child: Child, callback: (status: Boolean, message: String) -> Unit) {
        childrenCollectionRef
            .document(child.childId)
            .set(child)
            .addOnSuccessListener {
                callback(true, "Child added successfully")
                fetchChildren()
                // Handle success
            }
            .addOnFailureListener {
                // Handle error
                callback(false, it.message ?: "Error adding child")
            }
    }


    fun logout(onLogoutComplete: () -> Unit) {
        mAuth.signOut()
        onLogoutComplete()
    }

}