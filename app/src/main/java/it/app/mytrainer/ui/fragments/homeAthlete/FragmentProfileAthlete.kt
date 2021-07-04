package it.app.mytrainer.ui.fragments.homeAthlete

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.utils.CheckRegistrationFieldAthlete
import kotlinx.android.synthetic.main.fragment_profile_athlete.*
import kotlinx.android.synthetic.main.fragment_profile_athlete.view.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class FragmentProfileAthlete : Fragment() {

    private val REQUEST_IMAGE_GALLERY = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private val MAXLENGTHPHOTO = 1024
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()
    private var setForCheckBox = mutableSetOf<String>()
    private lateinit var currentPhotoPath: String
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        prefs = requireContext().getSharedPreferences("it.app.mytrainer", MODE_PRIVATE)

        val view = inflater.inflate(R.layout.fragment_profile_athlete, container, false)

        //Loading the photo
        loadPhotoOnImageView()

        val requestPermissionCameraLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    dispatchTakePictureIntent()
                }
            }

        view.buttonCameraAthlete.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                requestPermissionCameraLauncher.launch(
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

        view.buttonGalleryAthlete.setOnClickListener {
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
                Glide.with(this).load(uri).into(imageViewPersonalAthlete)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmapCamera = BitmapFactory.decodeFile(currentPhotoPath)
            imageViewPersonalAthlete.setImageBitmap(imageBitmapCamera)

            rotateResizeCompressUploadPhoto(imageBitmapCamera, currentPhotoPath)
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data!!
            imageViewPersonalAthlete.setImageURI(selectedImageUri)

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
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(imageBitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(imageBitmap,
                180)
            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(imageBitmap,
                270)
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
        //Call to the method for the insert into the field
        insertField()

        floatingActionButtonEditProfileAthlete.setOnClickListener {
            //Setting the visibility for the edit
            setVisibilityForEdit()
            //Setting all drop down menu
            setDropDownMenusForEdit()

            //Putting the checked one in the list and responds to checkbox being checked/unchecked
            checkbox1ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox2ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox3ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox4ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox5ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox6ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox7ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }

            checkbox8ProfileAthlete.setOnCheckedChangeListener { buttonView, isChecked ->
                managerList(buttonView, isChecked)
            }
        }

        floatingActionButtonSaveProfileAthlete.setOnClickListener {
            //Setting the visibility calling the method
            setVisibilityForSave()
            //Saving the new stats
            saveNewStats()
        }
    }

    //Filling the field with the data on firestore
    @SuppressLint("SetTextI18n")
    private fun insertField() {
        currentUserId.let {
            fireStore.getAthlete(it) { athlete ->
                if (athlete != null) {
                    nameFieldAthleteEditable.setText(athlete["Name"].toString())
                    surnameFieldAthleteEditable.setText(athlete["Surname"].toString())
                    textViewDateDataProfileAthlete.setText(athlete["BirthDate"].toString())
                    heightFieldAthleteEditable.setText(athlete["Height"].toString())
                    layoutAthleteEditTextHeightEditable.endIconMode =
                        TextInputLayout.END_ICON_NONE
                    weightFieldAthleteEditable.setText(athlete["Weight"].toString())
                    layoutAthleteEditTextWeightEditable.endIconMode =
                        TextInputLayout.END_ICON_NONE
                    autoTextViewDropMenuTypeOfWOProfileAthlete.setText(athlete["TypeOfWO"].toString())
                    autoTextViewDropMenuGoalProfileAthlete.setText(athlete["Goal"].toString())
                    autoTextViewDropMenuLevelProfileAthlete.setText(athlete["Level"].toString())
                    autoTextViewDropMenuNumOfWOProfileAthlete.setText(athlete["DaysOfWorkout"].toString())

                    val trainerId = athlete["TrainerId"].toString()
                    if (trainerId != "") {
                        fireStore.getTrainer(trainerId) { trainer ->
                            if (trainer != null) {
                                followedTrainerFieldProfileAthlete.setText(trainer["Name"].toString() + " " + trainer["Surname"].toString())
                            }
                        }
                    } else {
                        followedTrainerFieldProfileAthlete.setText("")
                    }

                    val checkBox = athlete["Equipment"] as ArrayList<*>
                    checkBox.forEach { equip ->
                        when (equip.toString()) {

                            "Dumbells", "Manubri" -> {
                                checkbox1ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.dumbbells_athlete))
                            }

                            "Rope", "Corde" -> {
                                checkbox2ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.rope_athlete))
                            }

                            "Rack" -> {
                                checkbox3ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.rack_athlete))
                            }

                            "Benches", "Panca" -> {
                                checkbox4ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.benches_athlete))
                            }

                            "Pull-up bar" -> {
                                checkbox5ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.pull_up_bar_athlete))
                            }

                            "Dip station" -> {
                                checkbox6ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.dip_station_athlete))
                            }

                            "Barbell and discs", "Bilanciere e dischi" -> {
                                checkbox7ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.barbell_and_discs_athlete))
                            }

                            "Gym membership", "Abbonamento palestra" -> {
                                checkbox8ProfileAthlete.isChecked = true
                                setForCheckBox.add(getString(R.string.gym_membership_athlete))
                            }
                        }
                    }
                }
            }
        }
    }

    //Setting the visibility for edit
    private fun setVisibilityForEdit() {

        floatingActionButtonSaveProfileAthlete.visibility = View.VISIBLE
        floatingActionButtonEditProfileAthlete.visibility = View.INVISIBLE

        layoutAthleteEditTextNameEditable.alpha = 0.5f
        layoutAthleteEditTextSurnameEditable.alpha = 0.5f
        layoutAthleteTextViewDateProfile.alpha = 0.5f

        heightFieldAthleteEditable.isEnabled = true
        layoutAthleteEditTextHeightEditable.endIconMode =
            TextInputLayout.END_ICON_CLEAR_TEXT
        weightFieldAthleteEditable.isEnabled = true
        layoutAthleteEditTextWeightEditable.endIconMode =
            TextInputLayout.END_ICON_CLEAR_TEXT

        checkbox1ProfileAthlete.isEnabled = true
        checkbox2ProfileAthlete.isEnabled = true
        checkbox3ProfileAthlete.isEnabled = true
        checkbox4ProfileAthlete.isEnabled = true
        checkbox5ProfileAthlete.isEnabled = true
        checkbox6ProfileAthlete.isEnabled = true
        checkbox7ProfileAthlete.isEnabled = true
        checkbox8ProfileAthlete.isEnabled = true
    }

    //Filling the dropdown
    private fun setDropDownMenusForEdit() {

        //SETTINGS FOR DROPDOWN TYPEOFWO
        autoTextViewDropMenuTypeOfWOProfileAthlete.isEnabled = true
        layoutDropMenuTypeOfWOProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_DROPDOWN_MENU

        val typeOfWO = arrayOf(
            getString(R.string.weight_workout_registration_athlete),
            getString(R.string.bodyweight_workout_registration_athlete)
        )

        val adapterTypeOfWO =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                typeOfWO
            )

        autoTextViewDropMenuTypeOfWOProfileAthlete.setAdapter(adapterTypeOfWO)

        //SETTINGS FOR DROPDOWN GOAL
        autoTextViewDropMenuGoalProfileAthlete.isEnabled = true
        layoutDropMenuGoalProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_DROPDOWN_MENU

        val goal = arrayOf(
            getString(R.string.fat_loss),
            getString(R.string.gain_muscle),
            getString(R.string.athletic_preparation),
            getString(R.string.self_defence),
            getString(R.string.reduce_stress),
            getString(R.string.endurance)
        )

        val adapterGoal =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                goal
            )

        autoTextViewDropMenuGoalProfileAthlete.setAdapter(adapterGoal)

        //SETTINGS FOR DROPDOWN LEVEL
        autoTextViewDropMenuLevelProfileAthlete.isEnabled = true
        layoutDropMenuLevelProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_DROPDOWN_MENU

        val level = arrayOf(
            getString(R.string.beginner_athlete),
            getString(R.string.intermediate_athlete),
            getString(R.string.advanced_athlete),
            getString(R.string.veteran_athlete)
        )

        val adapterLevel =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                level
            )

        autoTextViewDropMenuLevelProfileAthlete.setAdapter(adapterLevel)

        //SETTINGS FOR DROPDOWN NUMBER OF WORKOUT
        autoTextViewDropMenuNumOfWOProfileAthlete.isEnabled = true
        layoutDropMenuNumOfWOProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_DROPDOWN_MENU

        val days = arrayOf(
            1, 2, 3, 4, 5, 6, 7
        )

        val adapterDays =
            ArrayAdapter(
                requireContext(),
                R.layout.drop_menu_item_list,
                days
            )

        autoTextViewDropMenuNumOfWOProfileAthlete.setAdapter(adapterDays)

    }

    //FOR CHECK BOX
    private fun managerList(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            setForCheckBox.add(buttonView.text.toString())
        } else {
            setForCheckBox.remove(buttonView.text.toString())
        }
    }

    private fun setVisibilityForSave() {
        floatingActionButtonEditProfileAthlete.visibility = View.VISIBLE
        floatingActionButtonSaveProfileAthlete.visibility = View.INVISIBLE

        layoutAthleteEditTextNameEditable.alpha = 1f
        layoutAthleteEditTextSurnameEditable.alpha = 1f
        layoutAthleteTextViewDateProfile.alpha = 1f

        heightFieldAthleteEditable.isEnabled = false
        layoutAthleteEditTextHeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
        weightFieldAthleteEditable.isEnabled = false
        layoutAthleteEditTextWeightEditable.endIconMode = TextInputLayout.END_ICON_NONE
        autoTextViewDropMenuTypeOfWOProfileAthlete.isEnabled = false
        layoutDropMenuTypeOfWOProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_NONE
        autoTextViewDropMenuGoalProfileAthlete.isEnabled = false
        layoutDropMenuGoalProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_NONE
        autoTextViewDropMenuLevelProfileAthlete.isEnabled = false
        layoutDropMenuLevelProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_NONE
        autoTextViewDropMenuNumOfWOProfileAthlete.isEnabled = false
        layoutDropMenuNumOfWOProfileAthlete.endIconMode =
            TextInputLayout.END_ICON_NONE

        checkbox1ProfileAthlete.isEnabled = false
        checkbox2ProfileAthlete.isEnabled = false
        checkbox3ProfileAthlete.isEnabled = false
        checkbox4ProfileAthlete.isEnabled = false
        checkbox5ProfileAthlete.isEnabled = false
        checkbox6ProfileAthlete.isEnabled = false
        checkbox7ProfileAthlete.isEnabled = false
        checkbox8ProfileAthlete.isEnabled = false
    }

    private fun saveNewStats() {
        val newHeight = heightFieldAthleteEditable.text.toString()
        val newWeight = weightFieldAthleteEditable.text.toString()
        val newTypeOfWO = autoTextViewDropMenuTypeOfWOProfileAthlete.text.toString()
        val newGoal = autoTextViewDropMenuGoalProfileAthlete.text.toString()
        val newLevel = autoTextViewDropMenuLevelProfileAthlete.text.toString()
        val newNumOfWO = autoTextViewDropMenuNumOfWOProfileAthlete.text.toString()

        if (newHeight != "" && newWeight != "") {
            if (setForCheckBox.size > 0 && CheckRegistrationFieldAthlete.checkHeight(newHeight.toInt()) && CheckRegistrationFieldAthlete.checkWeight(
                    newWeight.toInt()
                )
            ) {
                // Cast to solve the problem of a single element when cast to array
                val listCheckBox = ArrayList<String>(setForCheckBox)

                fireStore.updateAthlete(
                    currentUserId,
                    newHeight,
                    newWeight,
                    newTypeOfWO,
                    newGoal,
                    newLevel,
                    newNumOfWO,
                    listCheckBox
                )

            } else {
                Snackbar.make(constraintFragmentProfileAthlete,
                    getString(R.string.error_edit_profile_athlete),
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),
                        R.color.app_foreground))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .show()

                //Refresh fragment with before data
                insertField()
            }

        } else {
            Snackbar.make(constraintFragmentProfileAthlete,
                getString(R.string.error_blank_field_fragment_profile_athlete),
                Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.app_foreground))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .show()

            //Refresh fragment with before data
            insertField()
        }

    }


    override fun onResume() {
        super.onResume()
        if (prefs!!.getBoolean("FirstRunProfileAthlete", true)
        ) {

            GuideView.Builder(requireContext())
                .setTitle(getString(R.string.view_case_title_button_camera))
                .setContentText(getString(R.string.view_case_text_button_camera))
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setTargetView(buttonCameraAthlete)
                .setDismissType(DismissType.outside)
                .setGuideListener {

                    GuideView.Builder(requireContext())
                        .setTitle(getString(R.string.view_case_title_button_gallery))
                        .setContentText(getString(R.string.view_case_text_button_gallery))
                        .setTitleTextSize(16)
                        .setContentTextSize(14)
                        .setTargetView(buttonGalleryAthlete)
                        .setDismissType(DismissType.outside)
                        .setGuideListener {

                            scrollViewProfileAthlete.post {
                                scrollViewProfileAthlete.smoothScrollTo(0,
                                    floatingActionButtonEditProfileAthlete.bottom)
                            }

                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    GuideView.Builder(requireContext())
                                        .setTitle(getString(R.string.view_case_title_fab_edit))
                                        .setContentText(getString(R.string.view_case_text_fab_edit))
                                        .setTitleTextSize(16)
                                        .setContentTextSize(14)
                                        .setTargetView(floatingActionButtonEditProfileAthlete)
                                        .setDismissType(DismissType.outside)
                                        .setGuideListener {

                                            scrollViewProfileAthlete.post {
                                                scrollViewProfileAthlete.smoothScrollTo(0, 0)
                                            }

                                            prefs!!.edit().putBoolean("FirstRunProfileAthlete", false).apply()
                                        }
                                        .build()
                                        .show()
                                }, 500)
                        }
                        .build()
                        .show()
                }
                .build()
                .show()

        }
    }
}