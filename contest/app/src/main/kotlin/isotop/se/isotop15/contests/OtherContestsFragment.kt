package isotop.se.isotop15.contests

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import isotop.se.isotop15.App
import isotop.se.isotop15.ContestantsRecyclerViewAdapter
import isotop.se.isotop15.R
import isotop.se.isotop15.models.Participation
import isotop.se.isotop15.utils.MarginItemDecoration

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/16.
 */
class OtherContestsFragment(app: App): ContestFragment(app) {

    @BindView(R.id.contestants_grid) lateinit var recyclerView: RecyclerView
    @BindView(R.id.other_contests_spinner) lateinit var spinner: Spinner

    lateinit var adapter: ContestantsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_contest_others, container, false)
        ButterKnife.bind(this, rootView)

        adapter = ContestantsRecyclerViewAdapter(context!!)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        val adapter = ArrayAdapter.createFromResource(context,
                                                      R.array.contests_other_names,
                                                      android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

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
        postParticipationForContestant(contestants.first())
        callback.onContestFinished()
    }

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
    }

    override fun getActivityId(): Int {
        Log.d(TAG, "getActivityId")
        return (spinner.selectedItemId + OFFSET).toInt()
    }

    companion object {
        val TAG = "OtherContestsFragment"
        val OFFSET = 5
    }
}