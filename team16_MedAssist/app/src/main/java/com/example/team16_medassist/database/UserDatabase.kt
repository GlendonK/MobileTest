package com.example.team16_medassist.database


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.team16_medassist.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable


class UserDatabase (){

    private lateinit var database: DatabaseReference
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var userModel =  MutableLiveData<UserModel?>()


    // use completeable from RxJava to remove boilerplate codes
    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    //Login OK , retrieve user data
                    //readLatestCase(firebaseUserId)
                    emitter.onComplete()
                }
                else {
                    Log.d(TAG, "signInWithEmail:fail")
                    emitter.onError(it.exception!!)
                }
            }
        }
    }

    fun currentUser() = firebaseAuth.currentUser!!

    fun position() = firebaseAuth.currentUser!!

    fun logout() = firebaseAuth.signOut()

    fun queryUser(userId : String){
        val query = Firebase.database.getReference("user")
        // to read data at path and listen to changes; an addlistenerforsinglevalue is used
        // to return value from local cache , used for read once operations
        query.child(userId).addValueEventListener(object : ValueEventListener {
            // defined by valueeventlistener if the read is cancelled
            override fun onCancelled(error: DatabaseError) {
                Log.e("onCancelledError", "onCancelled", error.toException())
            }
            // use dataonchange to read static snapshot of the content at the given path
            // this is triggered when the listener attaches and when the data/child changes
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                if (dataSnapShot.exists()) {
                    val user =dataSnapShot.getValue(UserModel::class.java)
                    userModel!!.postValue(user)

                }
            }
        })
    }

    fun getUser(): MutableLiveData<UserModel?> {
        return (this.userModel)
    }

}