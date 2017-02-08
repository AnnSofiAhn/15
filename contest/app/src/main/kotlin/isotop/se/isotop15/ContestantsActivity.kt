package isotop.se.isotop15

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Rect
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.schedulers.Schedulers
import isotop.se.isotop15.models.Contestant

class ContestantsActivity : AppCompatActivity() {

    val TAG = "ContestantsActivity"

    @BindView(R.id.grid) lateinit var recyclerView: RecyclerView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.main_content) lateinit var coordinatorLayout: CoordinatorLayout

    lateinit var adapter: ContestantsRecyclerViewAdapter
    private lateinit var nfcAdapter: NfcAdapter
    val baseImageUrl = "https://robohash.org/"
    val imageUrlSuffix = ".png?size=256x256&"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contestants)
        ButterKnife.bind(this)

        val context = recyclerView.context
        adapter = ContestantsRecyclerViewAdapter(context)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration())

        toolbar.title = "Välj deltagare"
        toolbar.inflateMenu(R.menu.contestants)
        toolbar.setOnMenuItemClickListener(MenuItemListener())

        adapter.addContestant(Contestant("Veronika Ekström",
                                         "https://robohash.org/Veronika Ekström.png?size=256x256"))
        adapter.addContestant(Contestant("Eric Nilsson",
                                         "https://robohash.org/Eric Nilsson.png?size=256x256"))
        adapter.addContestant(Contestant("Erik Manberger",
                                         "https://robohash.org/Erik Manberger.png?size=256x256"))

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d(TAG, "On new intent: $intent")

        if (intent != null && actionIsOurs(intent)) {

            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val stringBuilder = StringBuilder()
            val id = tag.id
            id.forEachIndexed { index, byte ->
                val part = Integer.toHexString(byte.toInt() and 0xff)
                if (part.length == 1) {
                    stringBuilder.append("0")
                }
                stringBuilder.append(part)
                if (index < id.size - 1) {
                    stringBuilder.append("-")
                }
            }

            val result = stringBuilder.toString().toLowerCase()

            val snackbar = Snackbar.make(coordinatorLayout,
                                         "Hämtar deltagarinfo...",
                                         Snackbar.LENGTH_INDEFINITE)
            snackbar.show()
            Log.d(TAG, "ID: $result")

            // TODO: Add placeholder while we wait for the request
            // TODO: As in...read JSON from tag with name?

            val app = application as App
            app.api.getContestantByTagId(result)
                    .subscribeOn(Schedulers.io())
                    .subscribe ({
                                    // onDone
                                    Log.d(TAG, "Got something: $it")
                                    addContestant(it.copy(image = getImageUrlFromName(it)))
                                }, {
                                    // onError
                                    Log.w(TAG, "Couldn't get the user from the tag ID", it)
                                }, {
                        // onComplete
                        snackbar.dismiss()
                    })
        }
    }

    private fun getImageUrlFromName(it: Contestant): String {
        return baseImageUrl + it.name + imageUrlSuffix
    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        nfcAdapter.disableForegroundDispatch(this)
        super.onPause()
    }

    private fun setupForegroundDispatch() {
        Log.d(TAG, "setupForegroundDispatch")
        val intent = Intent(applicationContext, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    fun addContestant(contestant: Contestant) {
        adapter.addContestant(contestant)
    }

    private fun actionIsOurs(intent: Intent): Boolean {
        return NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
    }

    inner class MenuItemListener : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_forward -> {
                    val intent = Intent()
                    intent.putExtra(MainActivity.RESULT_CONTESTANTS, adapter.getContestants())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                    return true
                }
                else -> return false
            }
        }

    }

    inner class MarginItemDecoration: RecyclerView.ItemDecoration() {
        val offset = resources.getDimensionPixelSize(R.dimen.card_margin)
        override fun getItemOffsets(outRect: Rect,
                                    view: View?,
                                    parent: RecyclerView?,
                                    state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(offset, offset, offset, offset)
        }
    }
}
