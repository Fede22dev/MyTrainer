package it.app.mytrainer.ui.fragments.homeTrainer

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import kotlinx.android.synthetic.main.fragment_profile_trainer.*
import kotlinx.android.synthetic.main.fragment_profile_trainer.view.*
import java.io.ByteArrayOutputStream

class FragmentProfileTrainer : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mapTrainer: Map<String, Any>
    private lateinit var storage: StorageReference
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid
    private val TAG = "FRAGMENT_HOME_PROFILE_TRAINER"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_trainer, container, false)

        storage = Firebase.storage.reference

        if (currentUserId != null) {
            storage.child("Photos").child(currentUserId).downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(view.imageViewPersonalTrainer)
                Log.d(TAG, "Found and downloaded the target picture for: $currentUserId")
            }
        }

        view.buttonCameraTrainer.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val fireStore = FireStore()

        currentUserId?.let {
            fireStore.getTrainer(it) { trainer ->
                if (trainer != null) {
                    mapTrainer = trainer
                    nameFieldTrainerEditable.setText(trainer["Name"].toString())
                    surnameFieldTrainerEditable.setText(trainer["Surname"].toString())
                    textViewDateDataProfileTrainer.text = trainer["BirthDate"].toString()
                    if (trainer["Gym"] != null) {
                        gymFieldTrainerEditable.setText(trainer["Gym"].toString())
                        layoutTrainerEditTextGymEditable.endIconMode = TextInputLayout.END_ICON_NONE
                    }
                    autoTextViewDropMenuSpecializationProfileTrainer.setText(trainer["Specialization"].toString())
                }
            }
        }

        floatingActionButtonEditProfileTrainer.setOnClickListener {

            floatingActionButtonSaveProfileTrainer.visibility = View.VISIBLE
            floatingActionButtonEditProfileTrainer.visibility = View.INVISIBLE

            layoutTrainerEditTextNameEditable.alpha = 0.5f
            layoutTrainerEditTextSurnameEditable.alpha = 0.5f
            textViewDateDataProfileTrainer.alpha = 0.5f

            gymFieldTrainerEditable.isEnabled = true
            layoutTrainerEditTextGymEditable.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            autoTextViewDropMenuSpecializationProfileTrainer.isEnabled = true
            layoutDropMenuSpecializationProfileTrainer.endIconMode =
                TextInputLayout.END_ICON_DROPDOWN_MENU

            val specializations = arrayOf(
                getString(R.string.bodyweight_trainer),
                getString(R.string.crossfit_trainer),
                getString(R.string.functional_trainer),
                getString(R.string.wellness_trainer),
                getString(R.string.powerlifting_trainer),
                getString(R.string.calisthenics_trainer),
                getString(R.string.yoga_trainer),
                getString(R.string.mixed_martial_arts_trainer),
                getString(R.string.bodybuilding_trainer)
            )

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.drop_menu_item_list,
                    specializations
                )

            autoTextViewDropMenuSpecializationProfileTrainer.setAdapter(adapter)
        }

        floatingActionButtonSaveProfileTrainer.setOnClickListener {

            floatingActionButtonEditProfileTrainer.visibility = View.VISIBLE
            floatingActionButtonSaveProfileTrainer.visibility = View.INVISIBLE

            layoutTrainerEditTextNameEditable.alpha = 1f
            layoutTrainerEditTextSurnameEditable.alpha = 1f
            textViewDateDataProfileTrainer.alpha = 1f

            gymFieldTrainerEditable.isEnabled = false
            layoutTrainerEditTextGymEditable.endIconMode = TextInputLayout.END_ICON_NONE
            autoTextViewDropMenuSpecializationProfileTrainer.isEnabled = false
            layoutDropMenuSpecializationProfileTrainer.endIconMode =
                TextInputLayout.END_ICON_NONE

            val newGym = gymFieldTrainerEditable.text.toString()
            val newSpec = autoTextViewDropMenuSpecializationProfileTrainer.text.toString()


            if (currentUserId != null) {
                fireStore.updateTrainer(currentUserId, newGym, newSpec)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewPersonalTrainer.setImageBitmap(imageBitmap)

            val savePathPhoto = currentUserId?.let { storage.child("Photos").child(it) }

            val arrayByte = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayByte)
            val imageByte = arrayByte.toByteArray()

            savePathPhoto?.putBytes(imageByte)?.addOnSuccessListener {
                Log.d(TAG, "Picture uploaded successfully")
            }
        }
    }
}