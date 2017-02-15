package isotop.se.isotop15.contests

import android.app.AlertDialog
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        sendToBackendButton.text = "Hoppa in i tävlingen"
        instructionsView.text = ""

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

    override fun contestantsUpdated() {
        Log.d(TAG, "contestantsUpdated")
    }

    override fun getActivityId(): Int {
        return Game.SLOT_CARS.id
    }

    @OnClick(R.id.contest_button)
    fun sendContestantToBackend() {
        val contestant = contestants.firstOrNull()
        if (contestant != null) {
            // Send to the backend, listen
            app.slotCarsBackend.startPlaying(contestant.slug!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.d(TAG, "Got something back! $it")
                        val color = getControllerColor(it.controller)
                        var message = it.error ?: "${it.name} har $color kontroll"

                        if (message == "nouser") {
                            message = "Hittade inte användaren, vänligen försök igen om några minuter"
                            app.slotCarsBackend.requestDatabaseRefresh()
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        Log.d(TAG, "Asked the slot cars backend to refresh users")
                                    }, {
                                        Log.w(TAG, "Got an error when asking the slot cars backend to refresh users", it)
                                    })
                        }

                        AlertDialog.Builder(context)
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok, {ignore, id ->
                                    callback.onContestFinished()
                                })
                                .create()
                                .show()
                    }, {
                        Log.w(TAG, "Got an error when entering a player into the game", it)
                    })
        }
    }

    private fun getControllerColor(id: Int): String {
        return when (id) {
            0 -> "grön"
            1 -> "röd"
            2 -> "orange"
            3 -> "vit"
            4 -> "gul"
            5 -> "blå"
            else -> "okänd"
        }
    }

    companion object {
        val TAG = "SlotCarsFragment"
    }
}
