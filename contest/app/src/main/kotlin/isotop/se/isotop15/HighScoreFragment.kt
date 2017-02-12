package isotop.se.isotop15

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isotop.se.isotop15.models.HighScore

/**
 * A fragment representing a list of HighScores.
 */
class HighScoreFragment : Fragment() {
    lateinit var adapter: HighScoreRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_highscore_list,
                                    container,
                                    false) as RecyclerView

        val context = view.context
        adapter = HighScoreRecyclerViewAdapter()
        view.layoutManager = LinearLayoutManager(context)
        view.adapter = adapter
        view.setHasFixedSize(true)

        val app = context.applicationContext as App
        app.gameBackend.getScores(arguments.getInt(ARG_ACTIVITY_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    // onNext
                    Log.d("HighScoreFragment", "Score: $it")
                    val ids = it.map(HighScore::id)
                    app.gameBackend.getContestants(ids = ids)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ contestants ->
                                // onNext
                                Log.d("HighScoreFragment", "Contestants? $contestants")
                                val scores = it.map { score ->
                                    val contestant = contestants.find { it.id == score.contestant_id }
                                    if (contestant != null) {
                                        score.copy(contestantName = contestant.name)
                                    } else{
                                        score
                                    }
                                }
                                adapter.setScores(scores)
                            }, {
                                // onError
                                Log.d("HighScoreFragment", "Got an error!", it)
                            })
                    }, {
                    // onError
                    Log.d("HighScoreFragment", "Got some error here", it)
                })

        return view
    }

    companion object {
        val ARG_ACTIVITY_ID = "activityId"

        fun newInstance(id: Int): HighScoreFragment {
            val fragment = HighScoreFragment()
            val args = Bundle()
            args.putInt(ARG_ACTIVITY_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}
