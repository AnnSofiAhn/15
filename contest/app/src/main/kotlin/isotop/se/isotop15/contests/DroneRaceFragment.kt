package isotop.se.isotop15.contests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import isotop.se.isotop15.App
import isotop.se.isotop15.R
import isotop.se.isotop15.models.DronisResponse
import isotop.se.isotop15.models.Game
import java.util.concurrent.TimeUnit

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/26.
 */
class DroneRaceFragment(app: App) : ContestFragment(app) {

    @BindView(R.id.contestant_name_view) lateinit var contestantNameView: TextView
    @BindView(R.id.start_race_button) lateinit var startRaceButton: Button
    @BindView(R.id.end_race_button) lateinit var endRaceButton: Button
    @BindView(R.id.info_view) lateinit var infoView: TextView

    private var subscription: Disposable? = null
    private var lastResponse: DronisResponse? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_drone_race, container, false)
        ButterKnife.bind(this, rootView)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume, contestants: ${contestants.size}")
        val contestant = contestants.getOrNull(0)
        contestantNameView.text = contestant?.name ?: "Starta om, något är fel"

        if (lastResponse == null) {
            infoView.text = "Redo att starta spelet"
            startRaceButton.text = "Starta racet"
            endRaceButton.isEnabled = false
            startRaceButton.isEnabled = true
        }
    }

    @OnClick(R.id.start_race_button)
    fun startRaceClicked() {
        if (lastResponse != null) {
            // restart, as in go back to contestant selection
            callback.onContestFinished()
        } else {
            infoView.text = "Nu startar racet!"
            startRaceButton.text = "Starta om"
            endRaceButton.isEnabled = false
            app.dronisBackend.startGame()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(StartRaceConsumer(app), StartRaceErrorConsumer())
        }
    }

    @OnClick(R.id.end_race_button)
    fun endRaceClicked() {
        val contestant = contestants.first()
        val lapTime = lastResponse?.data?.duration

        postScoreForContestant(contestant, POINTS_FOR_COMPLETION, lapTime?.toString())
    }

    override fun contestantsUpdated() {
        lastResponse = null
        postParticipationForContestant(contestants.first())
        Log.d(TAG, "contestantsUpdated: ${contestants.size}")
    }

    override fun getActivityId(): Int {
        return Game.DRONE_RACE.id
    }

    companion object {
        val TAG = "DroneRaceFragment"
        val POINTS_FOR_COMPLETION = 5
    }

    inner class StartRaceConsumer(val app: App): Consumer<DronisResponse> {
        override fun accept(it: DronisResponse) {
            Log.d(TAG, "Started the drone race: $it")
            infoView.text = "${it.data.devices.size} waypoints på banan"
            endRaceButton.isEnabled = false

            lastResponse = it

            subscription?.dispose()
            subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .flatMap { tick -> app.dronisBackend.getTime() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.data.finished) {
                            infoView.text = "Banan avklarad på ${it.data.duration} sekunder"
                            subscription?.dispose()
                            endRaceButton.isEnabled = true
                            startRaceButton.isEnabled = false
                        } else {
                            val left = it.data.devices.size - it.data.hits.size
                            infoView.text = "$left waypoints kvar"
                        }

                        lastResponse = it
                    }, {
                        Log.w(TAG, "Couldn't get time", it)
                    })
        }
    }

    inner class StartRaceErrorConsumer: Consumer<Throwable> {
        override fun accept(it: Throwable) {
            Log.d(TAG, "Couldn't start the race", it)
        }

    }
}