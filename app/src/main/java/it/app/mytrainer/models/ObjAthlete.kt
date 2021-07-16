package it.app.mytrainer.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Class used to create the obj athlete, used
 * to add athlete to fireStore
 */

data class ObjAthlete(
    var nameAthlete: String? = null,
    var surnameAthlete: String? = null,
    var dateOfBirth: String? = null,
    var height: String? = null,
    var weight: String? = null,
    var typeOfWO: String? = null,
    var goal: String? = null,
    var level: String? = null,
    var daysOfWO: String? = null,
    var equipment: ArrayList<String>? = null,
    var idAthlete: String? = null,
    var idTrainer: String? = null,
    var urlPhotoAthlete: String? = null,
) : Parcelable {

    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(null) as ArrayList<String>,
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameAthlete)
        parcel.writeString(surnameAthlete)
        parcel.writeString(dateOfBirth)
        parcel.writeString(height)
        parcel.writeString(weight)
        parcel.writeString(typeOfWO)
        parcel.writeString(goal)
        parcel.writeString(level)
        parcel.writeString(daysOfWO)
        parcel.writeList(equipment)
        parcel.writeString(idAthlete)
        parcel.writeString(idTrainer)
        parcel.writeString(urlPhotoAthlete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ObjAthlete> {

        override fun createFromParcel(parcel: Parcel): ObjAthlete {
            return ObjAthlete(parcel)
        }

        override fun newArray(size: Int): Array<ObjAthlete?> {
            return arrayOfNulls(size)
        }
    }
}