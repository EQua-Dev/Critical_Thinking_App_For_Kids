package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.LeaderboardEntry
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Score
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.childrenCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.scoresCollectionRef
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LeaderboardViewModel : ViewModel() {

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores

    private val _leaderboardEntries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = _leaderboardEntries

    fun fetchScores(childId: String, category: String, childStage: String, difficulty: String?) {
        viewModelScope.launch {
            var query = scoresCollectionRef
                .whereEqualTo("childId", childId)
                .whereEqualTo("childStage", childStage)
                .whereEqualTo("category", category)

            if (difficulty != null) {
                query = query.whereEqualTo("difficulty", difficulty)
            }

            query.get().addOnSuccessListener { snapshot ->
                val scoreList = snapshot.toObjects(Score::class.java)
                _scores.value = scoreList
                fetchChildNames(scoreList)
            }
        }
    }

    private fun fetchChildNames(scoreList: List<Score>) {
        viewModelScope.launch {
            val leaderboardEntries = mutableListOf<LeaderboardEntry>()
            scoreList.forEach { score ->
                childrenCollectionRef.document(score.childId).get()
                    .addOnSuccessListener { document ->
                        val childName = document.getString("childName") ?: "Unknown"
                        leaderboardEntries.add(
                            LeaderboardEntry(
                                childId =  score.childId,
                                childName = childName,
                                score = score.score,
                                datePlayed = score.datePlayed,
                                category = score.category,
                                difficulty = score.difficulty
                            )
                        )
                        _leaderboardEntries.value =
                            leaderboardEntries.sortedByDescending { it.score }
                    }
            }
        }
    }
}
