package isotop.se.isotop15

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import isotop.se.isotop15.contests.DroneRaceFragment
import isotop.se.isotop15.models.Contestant
import java.util.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.container) lateinit var viewPager: ViewPager
    @BindView(R.id.tabs) lateinit var tabLayout: TabLayout

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private var selectedGame = Game.NONE
    private val contestants = ArrayList<Contestant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()

        if (selectedGame == Game.NONE) {
            val gameIntent = Intent(this, SelectGameActivity::class.java)
            startActivityForResult(gameIntent, REQUEST_CODE_SELECT_GAME)
        } else {
            if (contestants.isEmpty()) {
                val contestantsIntent = Intent(this, ContestantsActivity::class.java)
                startActivityForResult(contestantsIntent, REQUEST_CODE_GET_CONTESTANTS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: $requestCode, $resultCode")

        when (requestCode) {
            REQUEST_CODE_SELECT_GAME -> {
                selectedGame = Game.values()[resultCode]
                toolbar.title = selectedGame.title
                Log.d(TAG, "Selected $selectedGame")
            }
            REQUEST_CODE_GET_CONTESTANTS -> {
                contestants.clear()
                val results = data?.getParcelableArrayListExtra<Contestant>(RESULT_CONTESTANTS)
                results?.filterNotNull()
                       ?.forEach { contestants.add(it) }
                Log.d(TAG, "Things: $contestants")
            }
        }
    }

    companion object {
        val REQUEST_CODE_SELECT_GAME = 42
        val REQUEST_CODE_GET_CONTESTANTS = 1337
        val RESULT_CONTESTANTS = "RESULT_CONTESTANTS"
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return DroneRaceFragment.newInstance(contestants)
                1 -> return HighScoreFragment.newInstance(position)
                else -> return DroneRaceFragment.newInstance(contestants)
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Tävling"
                1 -> return "High score"
            }
            return null
        }

        override fun getItemId(position: Int): Long {
            when (position) {
                0 -> return 42L
                1 -> return 1337L
                else -> return 666L
            }
        }
    }
}
