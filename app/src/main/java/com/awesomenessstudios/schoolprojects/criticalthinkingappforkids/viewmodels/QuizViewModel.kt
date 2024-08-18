package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.QuizQuestion
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.quizCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe.convertToSnakeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(): ViewModel() {

    val TAG = "QuizViewModel"

    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions

    fun fetchQuizQuestions(category: String, level: String, stage: String) {

        viewModelScope.launch {
            quizCollectionRef
                .whereEqualTo("category", category)
                .whereEqualTo("level", level)
                .whereEqualTo("stage", convertToSnakeCase(stage))
                .get()
                .addOnSuccessListener { result ->
                    val questions = result.toObjects(QuizQuestion::class.java)
                    _quizQuestions.value = questions.shuffled()
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    _quizQuestions.value = emptyList()
                }
        }
    }

}