package isotop.se.isotop15.models

import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/27.
 */
interface GameBackend {
    @Headers("Accept: application/json")
    @GET("activities/{id}/scores")
    fun getScores(@Path("id") id: Int): Observable<List<HighScore>>

    @Headers("Accept: application/json", "Authorization: Basic YW56bzpjaG9rbGFkYm9sbA==")
    @POST("contestants/{tagId}/scores")
    fun postContestantScore(@Path("tagId") tagId: String,
                            @Body score: HighScore): Observable<HighScore>

    @Headers("Accept: application/json")
    @GET("contestants/{tagId}")
    fun getContestantByTagId(@Path("tagId") tagId: String): Observable<Contestant>

    @Headers("Accept: application/json")
    @GET("contestants/")
    fun getContestants(@Query( "ids") ids: List<Int>): Observable<Array<Contestant>>

    // GET /api/contestants?ids[]=1&ids[]=2&...
}