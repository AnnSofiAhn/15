package isotop.se.isotop15.contests

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isotop.se.isotop15.App
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.models.Game
import isotop.se.isotop15.models.HighScore
import java.util.*

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
abstract class ContestFragment(val app: App) : Fragment() {

    protected lateinit var callback: ContestCallbacks
    protected val contestants = ArrayList<Contestant>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ContestCallbacks) {
            callback = context
        } else {
            throw IllegalStateException("Attaching activity must implement ContestCallbacks")
        }
    }

    fun setContestants(contestants: ArrayList<Contestant>) {
        this.contestants.clear()
        this.contestants.addAll(contestants)
        Log.d("ContestFragment", "Number of contestants: ${this.contestants.size}")
        contestantsUpdated()
    }

    fun postScoreForContestant(contestant: Contestant, points: Int, lapTime: String? = null) {
        val score = HighScore(null,
                              points,
                              null,
                              null,
                              getActivityId(),
                              contestant.id,
                              lapTime,
                              contestant.name)

        val tagId = contestant.slug!!
        app.gameBackend.postContestantScore(tagId, score)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                               Log.d(DroneRaceFragment.TAG, "What did we get back? $it")
                               callback.onContestFinished()
                           }, {
                               // TODO: Skicka upp en snackbar?
                               Log.w(DroneRaceFragment.TAG, "Couldn't post score", it)
                           })
    }

    abstract protected fun contestantsUpdated()
    abstract protected fun getActivityId(): Int

    companion object {
        fun newInstance(app: App, game: Game): ContestFragment {
            val fragment = when (game) {
                Game.DRONE_RACE -> DroneRaceFragment(app)
                Game.SLOT_CARS -> SlotCarsFragment(app)
                Game.ROBOT_WARS -> RobotWarsFragment(app)
                Game.VR -> VrFragment(app)
                Game.NONE -> TODO()
            }

            return fragment
        }
    }
}
