package isotop.se.isotop15.models

import kotlin.comparisons.compareValuesBy

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/07.
 */
data class HighScore(val name: String, val score: Int): Comparable<HighScore> {
    // This actually returns inverted, to support sorting by descending scores
    override fun compareTo(other: HighScore) = compareValuesBy(other, this, { it.score })
}