/*
 * Copyright (c) 2024.
 * Luomy EQua
 * Under Awesomeness Studios
 */

package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/*
import org.devstrike.persacg.models.Course
import org.devstrike.persacg.models.Lecturer
import org.devstrike.persacg.models.RegisteredCourse
import org.devstrike.persacg.models.Student
*/

object Common {

    val mAuth = FirebaseAuth.getInstance()
    val fireStoreDB = Firebase.firestore.batch()




    private const val PARENTS_REF = "MindSpark Parents"
    private const val CHILDREN_REF = "MindSpark Children"
    private const val ACCOUNTS_REF = "CariBank Accounts"
    private const val LECTURERS_REF = "Correps Lecturers"
    private const val ACCOUNTS_HISTORY_REF = "CariBank Accounts History"
    private const val COURSES_REF = "Courses"
    private const val LOANS_REF = "Caribank Loans"
    private const val SAVINGS_REF = "Caribank Savings"

    val parentsCollectionRef = Firebase.firestore.collection(PARENTS_REF)
    val childrenCollectionRef = Firebase.firestore.collection(CHILDREN_REF)

    val accountsHistoryCollectionRef = Firebase.firestore.collection(ACCOUNTS_HISTORY_REF)
    val loansCollectionRef = Firebase.firestore.collection(LOANS_REF)
    val savingsCollectionRef = Firebase.firestore.collection(SAVINGS_REF)
    val coursesCollectionRef = Firebase.firestore.collection(COURSES_REF)

    fun logout() {
        mAuth.signOut()
    }


}
