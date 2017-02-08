package isotop.se.isotop15.contests

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import isotop.se.isotop15.MainActivity
import isotop.se.isotop15.R
import isotop.se.isotop15.models.Contestant
import java.util.*

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/26.
 */
class DroneRaceFragment : Fragment() {

    @BindView(R.id.contestant_name_view) lateinit var contestantNameView: TextView
    @BindView(R.id.info_view) lateinit var infoView: TextView

    val contestants = ArrayList<Contestant>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_drone_race, container, false)
        ButterKnife.bind(this, rootView)



        contestants.addAll(arguments.getParcelableArrayList(MainActivity.RESULT_CONTESTANTS))

        contestantNameView.text = contestants.first().name
        return rootView
    }

    @OnClick(R.id.start_race_button)
    fun startRaceClicked() {
        infoView.text = "Nu startar racet!"
    }

    companion object {
        fun newInstance(contestants: ArrayList<Contestant>): DroneRaceFragment {
            val fragment = DroneRaceFragment()
            val args = Bundle()
            args.putParcelableArrayList(MainActivity.RESULT_CONTESTANTS, contestants)
            fragment.arguments = args
            return fragment
        }
    }
}