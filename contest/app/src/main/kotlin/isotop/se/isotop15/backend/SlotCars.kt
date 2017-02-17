package isotop.se.isotop15.backend

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import isotop.se.isotop15.models.SlotCarsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/14.
 */
interface SlotCars {

    @GET("json/?action=startDropin")
    fun startPlaying(@Query("u") slug: String): Observable<SlotCarsResponse>

    @GET("/json/?action=getUsers")
    fun requestDatabaseRefresh(): Observable<Unit>
}