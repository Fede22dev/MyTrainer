package it.app.mytrainer.firebase.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Storage {

    companion object {

        private val storage = Firebase.storage.reference
        private val TAG = "FIRESTORAGE"
        private val COLLECTIONPHOTOS = "Photos"

        // Fun to download in the recycle listclient and the profile the photo of athlete
        fun getPhotoUrl(userId: String, callback: (Uri?) -> Unit) {
            storage.child(COLLECTIONPHOTOS).child(userId).downloadUrl
                .addOnSuccessListener { uri ->
                    Log.d(TAG, "Download picture: success")
                    callback(uri)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Download picture: failed", e)
                    callback(null)
                }
        }

        // upload the photo in the storage on firebase
        fun uploadPhoto(userId: String, photo: ByteArray) {
            storage.child(COLLECTIONPHOTOS).child(userId).putBytes(photo)
                .addOnSuccessListener {
                    Log.d(TAG, "Picture uploaded: success")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Upload picture: failed", e)
                }
        }

        fun deletePhoto(userId: String) {
            storage.child(COLLECTIONPHOTOS).child(userId).delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Picture delete: success")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Picture delete: failed", e)
                }
        }

    }
}
