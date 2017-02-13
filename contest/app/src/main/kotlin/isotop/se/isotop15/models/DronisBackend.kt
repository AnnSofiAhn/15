package isotop.se.isotop15.models

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/10.
 */
interface DronisBackend {
    @GET("start")
    fun startGame(): Observable<DronisResponse>

    @GET("fake/start")
    fun startFakeGame(@Query("n") size: Int): Observable<DronisResponse>

    @GET("time")
    fun getTime(): Observable<DronisResponse>

    @GET("fake/time")
    fun getFakeTime(): Observable<DronisResponse>
}