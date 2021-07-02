package it.app.mytrainer.firebase.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * In this class we implement all the method that we
 * use to interface us on Storage
 */

class Storage {

    companion object {

        private val storage = Firebase.storage.reference
        private const val TAG = "FIRESTORAGE"
        private const val COLLECTIONPHOTOS = "Photos"

        // Fun to download in the recycle list client and the profile the photo of athlete
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

        // Upload the photo in the storage on firebase
        fun uploadPhoto(userId: String, photo: ByteArray) {
            storage.child(COLLECTIONPHOTOS).child(userId).putBytes(photo)
                .addOnSuccessListener {
                    Log.d(TAG, "Upload picture: success")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Upload picture: failed", e)
                }
        }

        // Deleting the photo in the storage on firebase
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
