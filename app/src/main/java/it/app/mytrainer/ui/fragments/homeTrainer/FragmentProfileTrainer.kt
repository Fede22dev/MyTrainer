package it.app.mytrainer.ui.fragments.homeTrainer

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import kotlinx.android.synthetic.main.fragment_profile_trainer.*
import kotlinx.android.synthetic.main.fragment_profile_trainer.view.*
import java.io.ByteArrayOutputStream

class FragmentProfileTrainer : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_trainer, container, false)

        //Loading the photo
        loadPhotoOnImageView()

        view.buttonCameraTrainer.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

        return view
    }

    private fun loadPhotoOnImageView() {
        Storage.getPhotoUrl(currentUserId) { uri ->
            Glide.with(this).load(uri).into(imageViewPersonalTrainer)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewPersonalTrainer.setImageBitmap(imageBitmap)

            compressAndUploadPhoto(imageBitmap)
        }
    }

    private fun compressAndUploadPhoto(imageBitmap: Bitmap) {
        val arrayByte = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayByte)
        val imageByte = arrayByte.toByteArray()

        Storage.uploadPhoto(currentUserId, imageByte)
    }

    override fun onStart() {
        super.onStart()

        //Filling the field of the profile fragment
        insertField()

        floatingActionButtonEditProfileTrainer.setOnClickListener {

            //Setting the visibility for the edit
            setVisibilityForEdit()

            //Set the possibility for the dropdownMenu choiche
            setDropDownMenu()

        }

        floatingActionButtonSaveProfileTrainer.setOnClickListener {

            //Setting the visibility for save operation
            setVisibilityForSave()

            //Saving the changes
            saveNewStats()

        }
    }

    //Filling the field with the data on firestore
    private fun insertField() {
        currentUserId.let {
            fireStore.getTrainer(it) { trainer ->
                if (trainer != null) {
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
    }

    //Setting the visibility for edit
    private fun setVisibilityForEdit() {

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
    }

    //Filling the dropdown
    private fun setDropDownMenu() {
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

    //Setting the visibility for save
    private fun setVisibilityForSave() {
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
    }

    //Saving new stats
    private fun saveNewStats() {
        val newGym = gymFieldTrainerEditable.text.toString()
        val newSpec = autoTextViewDropMenuSpecializationProfileTrainer.text.toString()

        fireStore.updateTrainer(currentUserId, newGym, newSpec)
    }
}