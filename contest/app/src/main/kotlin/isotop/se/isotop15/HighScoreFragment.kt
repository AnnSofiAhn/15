package isotop.se.isotop15

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        adapter.setScores(getFakeScores())

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

        fun getFakeScores(): MutableList<HighScore> {
            val list = mutableListOf<HighScore>()
            list.add(HighScore("Kapten Rabbid", 12))
            list.add(HighScore("Vaniljrabbid", 10))
            list.add(HighScore("Muffinsrabbid", 17))

            return list
        }
    }
}
