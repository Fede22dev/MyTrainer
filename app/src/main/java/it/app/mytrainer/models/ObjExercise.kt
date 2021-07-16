package it.app.mytrainer.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Class used to create the object used to fill
 * the objDayOfWO
 */

data class ObjExercise(
    var nameExercise: String?,
    var numSeries: String?,
    var numReps: String?,
    var recovery: Int?,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameExercise)
        parcel.writeString(numSeries)
        parcel.writeString(numReps)
        parcel.writeValue(recovery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ObjExercise> {

        override fun createFromParcel(parcel: Parcel): ObjExercise {
            return ObjExercise(parcel)
        }

        override fun newArray(size: Int): Array<ObjExercise?> {
            return arrayOfNulls(size)
        }
    }
}