package isotop.se.isotop15.contests

import android.util.Log
import isotop.se.isotop15.App
import isotop.se.isotop15.models.Game

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
class VrFragment(app: App): ContestFragment(app) {

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
    }

    override fun getActivityId(): Int {
        return Game.VR.id
    }

    companion object {
        val TAG = "VrFragment"
    }
}
