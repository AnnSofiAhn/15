package isotop.se.isotop15.contests

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import isotop.se.isotop15.App
import isotop.se.isotop15.ContestantsRecyclerViewAdapter
import isotop.se.isotop15.R
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.models.Game
import isotop.se.isotop15.utils.ContestantClickListener
import isotop.se.isotop15.utils.MarginItemDecoration

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
class RobotWarsFragment(app: App): ContestFragment(app), ContestantClickListener {

    @BindView(R.id.contestants_grid) lateinit var recyclerView: RecyclerView

    lateinit var adapter: ContestantsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_contest_simple, container, false)
        ButterKnife.bind(this, rootView)

        adapter = ContestantsRecyclerViewAdapter(context, this)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        return rootView
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume: ${contestants.size}")
        adapter.clearContestants()
        contestants.forEach {
            adapter.addContestant(it)
        }
    }

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
        contestants.forEach {
            postParticipationForContestant(it)
        }
    }

    override fun getActivityId(): Int {
        return Game.ROBOT_WARS.id
    }

    override fun onContestantClicked(contestant: Contestant) {
        Log.d(TAG, "onContestantClicked: $contestant")
        postScoreForContestant(contestant, POINTS_FOR_WINNING)
    }

    @OnClick(R.id.contest_button)
    fun onDraw() {
        Log.d(TAG, "Matchen slutade lika, inga poäng!")
        callback.onContestFinished()
    }

    companion object {
        val TAG = "RobotWarsFragment"
        val POINTS_FOR_WINNING = 5
    }
}
