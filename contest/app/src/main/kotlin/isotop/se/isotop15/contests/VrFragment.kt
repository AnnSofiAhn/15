package isotop.se.isotop15.contests

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import isotop.se.isotop15.App
import isotop.se.isotop15.ContestantsRecyclerViewAdapter
import isotop.se.isotop15.R
import isotop.se.isotop15.models.Game
import isotop.se.isotop15.utils.MarginItemDecoration

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
class VrFragment(app: App): ContestFragment(app) {

    @BindView(R.id.contestants_grid) lateinit var recyclerView: RecyclerView
    @BindView(R.id.contest_button) lateinit var finishButton: Button
    @BindView(R.id.instructions_view) lateinit var instructionsView: TextView

    lateinit var adapter: ContestantsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_contest_simple, container, false)
        ButterKnife.bind(this, rootView)

        adapter = ContestantsRecyclerViewAdapter(context!!)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        finishButton.text = "Byt tävlande"
        instructionsView.text = "Klicka på knappen nedan när VR-upplevelsen är över för att byta tävlande"

        return rootView
    }

    override fun onResume() {
        super.onResume()

        val contestant = contestants.firstOrNull()
        if (contestant != null){
            Log.d(TAG, "onResume: $contestant")
            adapter.clearContestants()
            adapter.addContestant(contestant)
        }
    }

    @OnClick(R.id.contest_button)
    fun onFinishClicked() {
        callback.onContestFinished()
    }

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
        postParticipationForContestant(contestants.first())
    }

    override fun getActivityId(): Int {
        return Game.VR.id
    }

    companion object {
        val TAG = "VrFragment"
    }
}
