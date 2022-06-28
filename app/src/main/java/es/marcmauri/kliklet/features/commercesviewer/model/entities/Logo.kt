package es.marcmauri.kliklet.features.commercesviewer.model.entities

import android.os.Parcel
import android.os.Parcelable

data class Logo(
    val thumbnails: Thumbnails? = null,
    val format: String? = "",
    val url: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Thumbnails::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(thumbnails, flags)
        parcel.writeString(format)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Logo> {
        override fun createFromParcel(parcel: Parcel): Logo {
            return Logo(parcel)
        }

        override fun newArray(size: Int): Array<Logo?> {
            return arrayOfNulls(size)
        }
    }
}
