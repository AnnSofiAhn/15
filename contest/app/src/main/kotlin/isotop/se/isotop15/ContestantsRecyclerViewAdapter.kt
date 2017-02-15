package isotop.se.isotop15

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.florent37.picassopalette.PicassoPalette
import com.squareup.picasso.Picasso
import isotop.se.isotop15.ContestantsRecyclerViewAdapter.ViewHolder
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.utils.ContestantClickListener
import java.util.*


/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/27.
 */
class ContestantsRecyclerViewAdapter(val context: Context,
                                     val listener: ContestantClickListener? = null) : RecyclerView.Adapter<ViewHolder>() {

    val TAG = "ContestantsStuff"

    private val contestants = ArrayList<Contestant>()
    private val defaultColor = context.resources.getColor(R.color.cardview_light_background)

    fun addContestant(contestant: Contestant) {
        Log.d(TAG, "Adding contestant: $contestant")
        contestants.add(contestant)
        notifyItemInserted(contestants.size - 1)
    }

    fun getContestants(): ArrayList<Contestant> {
        return contestants
    }

    fun clearContestants() {
        contestants.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contestant = contestants[position]
        Log.d(TAG, "onBindViewHolder: $contestant at $position")
        holder.nameView.text = contestant.name

        Picasso.with(context)
                .load(contestant.image)
                .error(R.drawable.ic_person_add_black_24dp)
                .into(holder.imageView,
                      PicassoPalette.with(contestant.image, holder.imageView)
                              .intoCallBack { palette ->
                                  val cardView = holder.itemView as CardView
                                  val target = palette.targets.first()
                                  val color = palette.getColorForTarget(target, defaultColor)
                                  cardView.setCardBackgroundColor(color)
                              })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                                 .inflate(R.layout.item_contestant, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contestants.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        @BindView(R.id.contestant_name) lateinit var nameView: TextView
        @BindView(R.id.contestant_image) lateinit var imageView: ImageView

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener({
                Log.d(TAG, "Clicked on ${nameView.text}")
                listener?.onContestantClicked(contestants[adapterPosition])
            })
        }
    }
}