package com.eks.instagramclone

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.eks.instagramclone.data.Event
import com.eks.instagramclone.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val USERS = "users"

@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String?>?>(null)

    init {
        auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    fun onSignUp(username: String, email: String, password: String) {
        if (username.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill in all fields")
            return
        }
        inProgress.value = true
        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    handleException(customMessage = "Username Already Exists")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                //Create User Profile
                                creatrOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Signing Failed")
                            }
                            inProgress.value = false
                        }
                }
            }
            .addOnFailureListener { }
    }

    fun onLogin(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill in all fields")
            return
        }
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let{uid->
                        getUserData(uid)
                        handleException(customMessage = "login success")
                    }
                }else{
                    handleException(task.exception, "Login Failed")
                    inProgress.value = false
                }
            }
            .addOnFailureListener { exc->
                handleException(exc, "Login Failed")
                inProgress.value = false
            }
    }

    private fun creatrOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        imageUrl: String? = null,
        bio: String? = null,
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            username = username ?: userData.value?.username,
            bio = bio ?: userData.value?.bio,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
            following = userData.value?.following
        )
        uid?.let { uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, "Cannot update User")
                                inProgress.value = false
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        inProgress.value = false
                    }
                }
                .addOnFailureListener { exc ->
                    handleException(exc, "Cannot create user")
                    inProgress.value = false
                }
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                inProgress.value = false
//                popupNotification.value = Event(
//                    "User data retried successfully" +
//                            ""
//                )
            }
            .addOnFailureListener { exc ->
                handleException(exc, "Cannot retrieve user data")
                inProgress.value = false
            }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage $errorMsg"
        popupNotification.value = Event(message)
    }

}