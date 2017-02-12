package isotop.se.isotop15

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import isotop.se.isotop15.models.HighScore

class HighScoreRecyclerViewAdapter() : RecyclerView.Adapter<HighScoreRecyclerViewAdapter.ViewHolder>() {

    val TAG = "ScoreStuff"

    private val scores = mutableListOf<HighScore>()

    fun setScores(scores: List<HighScore>) {
        this.scores.clear()
        this.scores.addAll(scores.sorted())
        notifyDataSetChanged()
    }

    fun addScore(score: HighScore) {
        val newScores = scores.toMutableList()
        newScores.add(score)
        val result = DiffUtil.calculateDiff(ScoreDiffUtilCallback(scores, newScores.sorted()))
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_highscore, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.score = scores[position]
        holder.nameView.text = holder.score.contestantName
        holder.scoreView.text = "${holder.score.points} poäng"
    }

    override fun getItemCount(): Int {
        return scores.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.score_name) lateinit var nameView: TextView
        @BindView(R.id.score_value) lateinit var scoreView: TextView
        lateinit var score: HighScore

        init {
            ButterKnife.bind(this, view)
        }

        override fun toString(): String {
            return score.toString()
        }
    }

    inner class ScoreDiffUtilCallback(val oldList: List<HighScore>,
                                      val newList: List<HighScore>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            Log.d(TAG, "areItemsTheSame: $new vs $old")

            return old == new
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            Log.d(TAG, "areItemsTheSame: $new vs $old")

            if (old.contestant_id == new.contestant_id) {
                return old.points == new.points
            } else {
                return false
            }
        }

        /*
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
        */
    }
}
