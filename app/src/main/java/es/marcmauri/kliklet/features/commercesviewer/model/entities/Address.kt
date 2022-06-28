package es.marcmauri.kliklet.features.commercesviewer.model.entities

import android.os.Parcel
import android.os.Parcelable

data class Address(
    val street  : String?,
    val country : String?,
    val city    : String?,
    val zip     : String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(street)
        parcel.writeString(country)
        parcel.writeString(city)
        parcel.writeString(zip)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}