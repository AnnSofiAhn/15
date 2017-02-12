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
    fun startGame(): Observable<Unit>

    @GET("fake/start")
    fun startFakeGame(@Query("n") size: Int): Observable<Unit>
}