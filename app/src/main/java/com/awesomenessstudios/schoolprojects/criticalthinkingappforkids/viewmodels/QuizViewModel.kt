package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.QuizQuestion
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Score
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.quizCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.scoresCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe.convertToSnakeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {

    val TAG = "QuizViewModel"

    val lives = MutableStateFlow<Int>(1)
    val hintUsed = MutableStateFlow<Boolean>(false)


    private val _scores = MutableLiveData<List<Score>>()
    val scores: LiveData<List<Score>> = _scores

    fun updateLives(operation: String) {
        if (operation == "add") {
            lives.value += 1
        } else {
            lives.value -= 1
        }

    }

    fun updateHintUsed(value: Boolean) {
        hintUsed.value = value

    }


    fun fetchScores(childId: String, category: String, childStage: String, difficulty: String?) {
        var query = scoresCollectionRef
            .whereEqualTo("childId", childId)
            .whereEqualTo("childStage", childStage)
            .whereEqualTo("category", category)

        if (difficulty != null) {
            query = query.whereEqualTo("difficulty", difficulty)
        }

        query.get().addOnSuccessListener { snapshot ->
            _scores.value = snapshot.toObjects(Score::class.java)
        }
    }


    fun saveScoreToFirestore(score: Score) {
        scoresCollectionRef
            .add(score)
    }

    fun isHighScore(currentScore: Int, scores: List<Score>): Boolean {
        return scores.maxOfOrNull { it.score }?.let { currentScore > it } ?: true
    }

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