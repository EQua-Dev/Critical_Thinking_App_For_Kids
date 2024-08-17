package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.models.Parent
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.LOCATION_PERMISSION_REQUEST_CODE
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.mAuth
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.Common.parentsCollectionRef
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils.HelpMe
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    val TAG = "AuthViewModel"
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    private val _parentData = MutableStateFlow<Parent?>(null)
    val parentData: StateFlow<Parent?> = _parentData

    private val _userLocation = MutableLiveData<String?>()
    val userLocation: LiveData<String?> get() = _userLocation



    val email = mutableStateOf<String>("")
    val password = mutableStateOf<String>("")
    val passwordStrength =
        mutableStateOf<PasswordStrength>(PasswordStrength.TOO_SHORT)
    val studentFirstName = mutableStateOf<String>("")
    val studentLastName = mutableStateOf<String>("")
    val phoneNumber = mutableStateOf<String>("")
    val confirmPassword = mutableStateOf<String>("")


    val gender = mutableStateOf<String>("")
    val showLoading = mutableStateOf<Boolean>(false)

    private val _verificationId = MutableLiveData<String>()
    private val _otpError = MutableLiveData<String?>(null)

    val otpError: LiveData<String?> get() = _otpError


    fun updateEmail(value: String) {
        this.email.value = value
    }

    fun updateFirstName(value: String) {
        this.studentFirstName.value = value
    }

    fun updateLastName(value: String) {
        this.studentLastName.value = value
    }

    fun updatePhoneNumber(value: String) {
        this.phoneNumber.value = value
    }

    fun updateConfirmPassword(value: String) {
        this.confirmPassword.value = value
    }

    fun updateGender(value: String) {

        this.gender.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updatePassword(value: String) {
        this.password.value = value
        this.passwordStrength.value = passwordStrength(value)
    }

    val currentSelectedGenderIndex = mutableIntStateOf(0)

    fun updateCurrentSelectedGenderId(index: Int) {
        currentSelectedGenderIndex.intValue = index
    }

    fun isFieldInvalid(value: String): Boolean {
        return value.isEmpty() // You can add more validation logic as needed
    }


    // Call this function after successful email-password sign up
    fun sendOtp(
        activity: ComponentActivity,
        phoneNumber: String,
        callback: (Boolean, String) -> Unit
    ) {
        Log.d(TAG, "sendOtp: $phoneNumber")
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity) // Replace 'activity' with your current activity
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    callback(true, "Verification Completed")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    callback(false, "Verification Failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _verificationId.value = verificationId
                    callback(true, "Code Sent")
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(otp: String, callback: (Boolean, String) -> Unit) {
        val verificationId = _verificationId.value
//        if (verificationId != null) {
//            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        if (otp == "1234") {

            callback(true, "OTP Verified")
        } else {
            _otpError.value = "Invalid OTP"
            callback(false, "OTP Verification Failed ")
        }
        /* mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {

                    }
                }*/
//        } else {
//            callback(false, "Verification ID is null")
//        }
    }

    fun isFormValid(
        username: String,
        email: String,
        phoneNumber: String,
        gender: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return username.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                phoneNumber.length > 11 &&
                gender.isNotEmpty() &&
                password.length >= 6 &&
                password == confirmPassword
    }

    fun signUp(
        username: String,
        email: String,
        phoneNumber: String,
        gender: String,
        password: String,
        confirmPassword: String,
        callback: (Boolean, String) -> Unit
    ) {
        Log.d("TAG", "signUp: $phoneNumber")
        if (!isFormValid(username, email, phoneNumber, gender, password, confirmPassword)) {
            callback(false, "Please fill out all fields correctly.")
            return
        }



        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val parent = task.result?.user
                    parent?.let {
                        val newParent = Parent(
                            parentId = it.uid,
                            fullName = username,
                            phoneNumber = phoneNumber,
                            email = email,
                            gender = gender,
                            lastLogin = System.currentTimeMillis().toString(),
                            dateCreated = System.currentTimeMillis().toString()
                        )
                        _parentData.value = newParent
                        callback(true, "user created successfully")
                        /*saveParent(
                        ) { success, message ->
                            callback(success, message)
                        }*/
                    } ?: callback(false, "User creation failed.")
                } else {
                    callback(false, "Authentication failed: ${task.exception?.message}")
                }
            }
    }

    fun loginUser(
        email: String,
        password: String,
        userLocation: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: return@addOnCompleteListener

                        val parentRef = parentsCollectionRef.document(userId)

                        val updates = hashMapOf<String, Any>(
                            "lastLoginLocation" to userLocation,
                            "lastLogin" to System.currentTimeMillis().toString()
                        )
                        parentRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                parentRef.update(updates)
                                    .addOnSuccessListener {
                                        onSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        onFailure(e)
                                    }
                            } else {
                                // Handle case where the parent document does not exist
                                onFailure(Exception("Parent document does not exist"))
                            }
                        }.addOnFailureListener { e ->
                            onFailure(e)
                        }
                    //getLastLocation(context) { location ->

                    // }
                } else {
                    onFailure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun saveParent(
        callback: (Boolean, String) -> Unit
    ) {
        Log.d(TAG, "saveParent: ")
        parentsCollectionRef.document(_parentData.value!!.parentId)
            .set(_parentData.value!!)
            .addOnSuccessListener {
                callback(true, "User saved successfully.")
            }
            .addOnFailureListener { e ->
                callback(false, "Firestore error: ${e.message}")
            }
    }

    /*
        fun resetPassword(
            email: String,
            onLoading: (Boolean) -> Unit,
            onResetLinkSent: (String) -> Unit,
            onResetLinkNotSent: (String) -> Unit
        ) = CoroutineScope(Dispatchers.IO).launch {
            onLoading(true)
            if (!isValidEmail(email)) {
                onLoading(false)
            } else {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onLoading(false)
                            onResetLinkSent("Password Reset Link Sent\nCheck your email")
                        } else {
                            onLoading(false)
                            onResetLinkNotSent("Password Reset Link Sent\nCheck your email")
                        }
                    }.addOnFailureListener { e ->
                        onLoading(false)
                        onResetLinkSent(e.message ?: "Some error occurred")
                    }
            }
    }
    */


    private fun passwordStrength(password: String): PasswordStrength {
        // Minimum length requirement
        val minLength = 8

        // Check for minimum length
        if (password.length < minLength) {
            return PasswordStrength.TOO_SHORT
        }

        // Check for uppercase, lowercase, digit, and special character
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        // Calculate score based on conditions
        val score = listOf(
            hasUpperCase,
            hasLowerCase,
            hasDigit,
            hasSpecial
        ).count { it }

        // Determine strength based on score
        return when {
            score == 4 -> PasswordStrength.STRONG
            score >= 3 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }

    // Enum to represent password strength
    enum class PasswordStrength {
        WEAK,
        MEDIUM,
        STRONG,
        TOO_SHORT
    }

    fun clearOtpError() {
        _otpError.value = null
    }

    fun fetchUserLocation(context: Context) {
        HelpMe.initialize(context) // Initialize the location client
        HelpMe.getCurrentAddress(context) { address ->
            Log.d(TAG, "fetchUserLocation: $address")
            if (address != null) {
                _userLocation.postValue(address)
            } else {
                _userLocation.postValue("Location not found")
            }
        }
    }
}