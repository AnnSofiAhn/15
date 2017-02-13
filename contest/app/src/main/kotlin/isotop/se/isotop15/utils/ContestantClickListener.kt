package isotop.se.isotop15.utils

import isotop.se.isotop15.models.Contestant

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
interface ContestantClickListener {
    fun onContestantClicked(contestant: Contestant)
}