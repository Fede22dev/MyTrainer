package it.app.mytrainer.ui.fragments.homeTrainer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import kotlinx.android.synthetic.main.fragment_profile_trainer.*
import kotlinx.android.synthetic.main.fragment_profile_trainer.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class FragmentProfileTrainer : Fragment() {

    private val REQUEST_IMAGE_GALLERY = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private val MAXLENGTHPHOTO = 1024
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_trainer, container, false)

        //Loading the photo
        loadPhotoOnImageView()

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    dispatchTakePictureIntent()
                }
            }

        view.buttonCameraTrainer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
        }

        val requestPermissionStorageLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
                }
            }

        view.buttonGalleryTrainer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)

            } else {
                requestPermissionStorageLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        return view
    }

    private fun loadPhotoOnImageView() {
        Storage.getPhotoUrl(currentUserId) { uri ->
            if (uri != null) {
                Glide.with(this).load(uri).into(imageViewPersonalTrainer)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmapCamera = BitmapFactory.decodeFile(currentPhotoPath)
            imageViewPersonalTrainer.setImageBitmap(imageBitmapCamera)

            rotateResizeCompressUploadPhoto(imageBitmapCamera, currentPhotoPath)
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data!!
            imageViewPersonalTrainer.setImageURI(selectedImageUri)

            val imageBitmapGallery =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,
                    selectedImageUri)

            rotateResizeCompressUploadPhoto(imageBitmapGallery,
                getRealPathFromURI(selectedImageUri)!!)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile = createImageFile()

                // Continue only if the File was successfully created
                photoFile.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "it.app.mytrainer",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor = requireActivity().contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) { // Source is dropbox or other similar local file path
            contentURI.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(index)
            cursor.close()
            path
        }
    }

    private fun rotateResizeCompressUploadPhoto(imageBitmap: Bitmap, photoPath: String) {
        thread {
            val rotatedBitmap = rotateBitmap(imageBitmap, photoPath)

            val resizedBitmap = resizeBitmapWithAspectRatio(rotatedBitmap)

            val arrayByte = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayByte)
            val imageByte = arrayByte.toByteArray()

            Storage.uploadPhoto(currentUserId, imageByte)
        }
    }

    private fun rotateBitmap(imageBitmap: Bitmap, photoPath: String): Bitmap {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(imageBitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(imageBitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(imageBitmap, 270)
            ExifInterface.ORIENTATION_NORMAL -> imageBitmap
            else                                 -> imageBitmap
        }
    }

    private fun resizeBitmapWithAspectRatio(rotatedBitmap: Bitmap): Bitmap {
        val maxLength = MAXLENGTHPHOTO

        if (rotatedBitmap.height >= rotatedBitmap.width) {
            if (rotatedBitmap.height <= maxLength) { // if image height already smaller than the required height
                return rotatedBitmap
            }

            val aspectRatio = rotatedBitmap.width.toDouble() / rotatedBitmap.height.toDouble()
            val targetWidth = (maxLength * aspectRatio).toInt()
            return Bitmap.createScaledBitmap(rotatedBitmap, targetWidth, maxLength, true)
        } else {
            if (rotatedBitmap.width <= maxLength) { // if image width already smaller than the required width
                return rotatedBitmap
            }

            val aspectRatio = rotatedBitmap.height.toDouble() / rotatedBitmap.width.toDouble()
            val targetHeight = (maxLength * aspectRatio).toInt()

            return Bitmap.createScaledBitmap(rotatedBitmap, maxLength, targetHeight, true)
        }
    }

    override fun onStart() {
        super.onStart()

        //Filling the field of the profile fragment
        insertField()

        floatingActionButtonEditProfileTrainer.setOnClickListener {

            //Setting the visibility for the edit
            setVisibilityForEdit()

            //Set the possibility for the dropdownMenu choice
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
                    textViewDateDataProfileTrainer.setText(trainer["BirthDate"].toString())
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
        layoutTrainerTextViewDateProfile.alpha = 0.5f

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
        layoutTrainerTextViewDateProfile.alpha = 1f

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