package isotop.se.isotop15.models

import org.junit.Assert.*
import org.junit.Test

class HighScoreTest {

    @Test
    fun testCompareTo_oneIsBigger() {
        val score1 = HighScore(
                1,
                15,
                null,
                null,
                1,
                1,
                null,
                "Contestant One"
        )

        val score2 = HighScore(
        1,
        2,
        null,
        null,
        1,
        1,
        null,
        "Contestant One"
        )

        assertTrue( score1.compareTo(score2) == -1)
    }

    @Test
    fun testCompareTo_equalScores() {
        val score1 = HighScore(
                1,
                1,
                null,
                null,
                1,
                1,
                null,
                "Contestant One"
        )

        val score2 = HighScore(
                1,
                1,
                null,
                null,
                1,
                1,
                null,
                "Contestant One"
        )

        assertTrue( score1.compareTo(score2) == 0)
    }

    @Test
    fun testCompareTo_oneIsSmaller() {
        val score1 = HighScore(
                1,
                1,
                null,
                null,
                1,
                1,
                null,
                "Contestant One"
        )

        val score2 = HighScore(
                1,
                12,
                null,
                null,
                1,
                1,
                null,
                "Contestant One"
        )

        assertTrue( score1.compareTo(score2) == 1)
    }
}