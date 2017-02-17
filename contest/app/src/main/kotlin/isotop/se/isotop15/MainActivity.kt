package isotop.se.isotop15

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import isotop.se.isotop15.contests.ContestCallbacks
import isotop.se.isotop15.contests.ContestFragment
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.models.Game
import java.util.*

class MainActivity : AppCompatActivity(), ContestCallbacks {

    val TAG = "MainActivity"

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.fragment_container) lateinit var container: FrameLayout

    private var selectedGame = Game.NONE
    private val contestants = ArrayList<Contestant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
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

        when (requestCode) {
            REQUEST_CODE_SELECT_GAME -> {
                selectedGame = Game.values()[resultCode]
                toolbar.title = selectedGame.title

            }
            REQUEST_CODE_GET_CONTESTANTS -> {
                if (resultCode == Activity.RESULT_OK) {
                    contestants.clear()
                    val results = data?.getParcelableArrayListExtra<Contestant>(RESULT_CONTESTANTS)
                    results?.filterNotNull()
                            ?.distinctBy { it.slug }
                            ?.forEach {
                                contestants.add(it)
                            }

                    val app = applicationContext as App
                    val fragment = ContestFragment.newInstance(app, selectedGame)
                    fragment.setContestants(contestants)

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                } else {
                    selectedGame = Game.NONE
                }
            }
        }
    }

    override fun onContestFinished() {
        Log.d(TAG, "onContestFinished")
        contestants.clear()
        onResume()
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
        val app = applicationContext as App
        var contestFragment: ContestFragment? = null
        var highScoreFragment: HighScoreFragment? = null

        override fun getItem(position: Int): Fragment {

            when (position) {
                0 -> {
                    if (contestFragment == null) {
                        contestFragment = ContestFragment.newInstance(app, selectedGame)
                    }
                    return contestFragment as Fragment
                }
                1 -> return highScoreFragment ?: HighScoreFragment.newInstance(selectedGame.id)
                else -> return ContestFragment.newInstance(app, selectedGame)
            }
        }

        override fun getItemPosition(item: Any?): Int {
            return PagerAdapter.POSITION_NONE
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

        override fun destroyItem(container: View?, position: Int, item: Any?) {
            if (position == 0) {
                contestFragment = null
            } else {
                highScoreFragment = HighScoreFragment.newInstance(selectedGame.id)
            }
        }
    }
}
