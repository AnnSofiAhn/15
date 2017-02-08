package isotop.se.isotop15.models

import io.reactivex.Observable
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.models.HighScore
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/27.
 */
interface Api {
    @Headers("Accept: application/json")
    @GET("activities/{id}/scores")
    fun getScores(@Path("id") id: Int): Observable<List<HighScore>>

    @Headers("Accept: application/json")
    @GET("contestants/{tagId}")
    fun getContestantByTagId(@Path("tagId") tagId: String): Observable<Contestant>

    @Headers("Accept: application/json")
    @GET("contestants/")
    fun getContestants(): Observable<List<Contestant>>
}