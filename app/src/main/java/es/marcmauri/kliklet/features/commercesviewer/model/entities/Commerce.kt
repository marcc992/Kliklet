package es.marcmauri.kliklet.features.commercesviewer.model.entities

import android.os.Parcel
import android.os.Parcelable
import es.marcmauri.kliklet.utils.Constants

data class Commerce(
    val name: String? = "Unknown",
    val description: String? = "Unknown",
    val category: String = Constants.Category.OTHER,
    val address: Address? = null,
    val logo: Logo? = null,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readParcelable(Logo::class.java.classLoader),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeParcelable(address, flags)
        parcel.writeParcelable(logo, flags)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Commerce> {
        override fun createFromParcel(parcel: Parcel): Commerce {
            return Commerce(parcel)
        }

        override fun newArray(size: Int): Array<Commerce?> {
            return arrayOfNulls(size)
        }
    }
}