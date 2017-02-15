package isotop.se.isotop15.models

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/14.
 */
interface SlotCarsBackend {

    @GET("json/?action=startDropin")
    fun startPlaying(@Query("u") slug: String): Observable<SlotCarsResponse>

    data class SlotCarsResponse(@SerializedName("Name") val name: String,
                                @SerializedName("Controller") val controller: Int,
                                val error: String?)
}