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
class SlotCarsFragment(app: App): ContestFragment(app) {

    @BindView(R.id.contestants_grid) lateinit var recyclerView: RecyclerView
    @BindView(R.id.contest_button) lateinit var sendToBackendButton: Button
    @BindView(R.id.instructions_view) lateinit var instructionsView: TextView

    lateinit var adapter: ContestantsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_contest_simple, container, false)
        ButterKnife.bind(this, rootView)

        adapter = ContestantsRecyclerViewAdapter(context)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        sendToBackendButton.text = "Hoppa in i tävlingen"
        instructionsView.text = ""

        return rootView
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume: ${contestants.size}")
        contestants.forEach {
            adapter.addContestant(it)
        }
    }

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
    }

    override fun getActivityId(): Int {
        return Game.SLOT_CARS.id
    }

    @OnClick(R.id.contest_button)
    fun sendContestantToBackend() {
        val contestant = contestants.getOrNull(0)
        if (contestant != null) {
            // Send to the backend, listen
        }
    }

    companion object {
        val TAG = "SlotCarsFragment"
    }
}
