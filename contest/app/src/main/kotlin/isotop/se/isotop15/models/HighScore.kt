package isotop.se.isotop15.models

import kotlin.comparisons.compareValuesBy

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/07.
 */
data class HighScore(val id: Int?,
                     val points: Int,
                     val created_at: String?,
                     val updated_at: String?,
                     val activity_id: Int,
                     val contestant_id: Int,
                     val lapTime: String?,
                     val contestantName: String = "") : Comparable<HighScore> {

    // This actually returns inverted, to support sorting by descending scores
    override fun compareTo(other: HighScore) = compareValuesBy(other, this, { it.points })
}