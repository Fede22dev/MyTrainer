package it.app.mytrainer.ui.fragments.homeAthlete

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import it.app.mytrainer.R
import it.app.mytrainer.firebase.fireauth.FireAuth
import it.app.mytrainer.firebase.firestore.FireStore
import it.app.mytrainer.firebase.storage.Storage
import it.app.mytrainer.utils.CheckRegistrationFieldAthlete
import kotlinx.android.synthetic.main.fragment_profile_athlete.*
import kotlinx.android.synthetic.main.fragment_profile_athlete.view.*
import java.io.ByteArrayOutputStream

class FragmentProfileAthlete : Fragment() {

    private val REQUEST_IMAGE_GALLERY = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private val currentUserId = FireAuth.getCurrentUserAuth()?.uid!!
    private val fireStore = FireStore()
    private var setForCheckBox = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_athlete, container, false)

        //Loading the photo
        loadPhotoOnImageView()

        val requestPermissionCameraLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }

        view.buttonCameraAthlete.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

            } else {
                requestPermissionCameraLauncher.launch(
                    Manifest.permission.CAMERA)
            }
        }

        val requestPermissionStorageLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
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

            val imageBitmapCamera = data?.extras?.get("data") as Bitmap
            imageViewPersonalAthlete.setImageBitmap(imageBitmapCamera)

            compressAndUploadPhoto(imageBitmapCamera)
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            imageViewPersonalAthlete.setImageURI(selectedImageUri)

            val imageBitmapGallery =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,
                    selectedImageUri)

            compressAndUploadPhoto(imageBitmapGallery)
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
    private fun insertField() {
        currentUserId.let {
            fireStore.getAthlete(it) { athlete ->
                if (athlete != null) {
                    nameFieldAthleteEditable.setText(athlete["Name"].toString())
                    surnameFieldAthleteEditable.setText(athlete["Surname"].toString())
                    textViewDateDataProfileAthlete.text = athlete["BirthDate"].toString()
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
        textViewDateDataProfileAthlete.alpha = 0.5f

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
        textViewDateDataProfileAthlete.alpha = 1f

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

            Toast.makeText(
                requireContext(),
                getString(R.string.error_edit_profile_athlete),
                Toast.LENGTH_LONG
            ).show()

            //Refresh fragment with before data
            insertField()
        }
    }
}

