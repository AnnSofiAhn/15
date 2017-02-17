package isotop.se.isotop15

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isotop.se.isotop15.models.Contestant
import isotop.se.isotop15.utils.MarginItemDecoration

class ContestantsActivity : AppCompatActivity() {

    val TAG = "ContestantsActivity"

    @BindView(R.id.grid) lateinit var recyclerView: RecyclerView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.main_content) lateinit var coordinatorLayout: CoordinatorLayout
    @BindView(R.id.go_to_contest_button) lateinit var fab: FloatingActionButton
    @BindView(R.id.contestants_info_view) lateinit var infoView: TextView

    lateinit var adapter: ContestantsRecyclerViewAdapter
    private lateinit var nfcAdapter: NfcAdapter
    val baseImageUrl = "https://robohash.org/"
    val imageUrlSuffix = ".png?size=256x256"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contestants)
        ButterKnife.bind(this)

        adapter = ContestantsRecyclerViewAdapter(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(MarginItemDecoration(resources))

        toolbar.title = "Välj deltagare"

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
            app.gameBackend.getContestantByTagId(result)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                    {
                        Log.d(TAG, "Got something: $it")
                        addContestant(it.copy(image = getImageUrlFromName(it)))
                        snackbar.dismiss()
                        infoView.visibility = View.GONE
                        fab.show()
                    }, {
                        Log.w(TAG, "Couldn't get the user from the tag ID", it)
                        snackbar.setText("Kunde inte hämta deltagaren")
                    })
        }
    }

    @OnClick(R.id.go_to_contest_button)
    fun onNext() {
        val intent = Intent()
        intent.putExtra(MainActivity.RESULT_CONTESTANTS, adapter.getContestants())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getImageUrlFromName(it: Contestant): String {
        return baseImageUrl + it.name.hashCode() + imageUrlSuffix
    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()

        if (adapter.getContestants().isEmpty()) {
            fab.hide()
            infoView.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        nfcAdapter.disableForegroundDispatch(this)
        super.onPause()
    }

    private fun setupForegroundDispatch() {
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
}
