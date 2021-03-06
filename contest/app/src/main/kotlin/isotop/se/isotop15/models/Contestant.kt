package isotop.se.isotop15.models

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import android.util.Log

/**
 * @author Ann-Sofi Åhn
 * Created on 17/02/07.
 */

data class Contestant(val name: String, val image: String, val id: Int, val slug: String?): Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(image)
        dest?.writeInt(id)
        dest?.writeString(slug)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Contestant(name='$name', slug=$slug)"
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Contestant> = object : Parcelable.Creator<Contestant> {

            override fun createFromParcel(source: Parcel?): Contestant {
                Log.d(ContentValues.TAG, "createFromParcel")
                val name = source?.readString() ?: ""
                val image = source?.readString() ?: ""
                val id = source?.readInt() ?: -1
                val slug = source?.readString() ?: ""
                return Contestant(name, image, id, slug)
            }

            override fun newArray(size: Int): Array<out Contestant?> {
                Log.d(ContentValues.TAG, "newArray")
                return kotlin.arrayOfNulls<Contestant>(size)
            }
        }
    }
}
