package isotop.se.isotop15.models

import com.google.gson.annotations.SerializedName
import kotlin.comparisons.compareValuesBy

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/07.
 */
data class HighScore(val id: Int? = null,
                     val points: Int,
                     @SerializedName("created_at") val createdAt: String? = null,
                     @SerializedName("updated_at") val updatedAt: String? = null,
                     @SerializedName("activity_id") val activityId: Int,
                     @SerializedName("contestant_id") val contestantId: Int,
                     @SerializedName("lap_time") val lapTime: String? = null,
                     val contestantName: String = "") : Comparable<HighScore> {

    // This actually returns inverted, to support sorting by descending scores
    override fun compareTo(other: HighScore) = compareValuesBy(other, this, { it.points })
}