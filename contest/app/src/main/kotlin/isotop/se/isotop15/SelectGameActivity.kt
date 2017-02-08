package isotop.se.isotop15

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class SelectGameActivity : AppCompatActivity() {
    val TAG = "SelectGameActivity"

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_game)
        ButterKnife.bind(this)

        toolbar.title = "Välj tävling"
    }

    @OnClick(R.id.drone_race_button)
    fun onDroneRaceClicked() {
        returnWithResult(Game.DRONE_RACE)
    }

    @OnClick(R.id.robot_wars_button)
    fun onRobotWarsClicked() {
        returnWithResult(Game.ROBOT_WARS)
    }

    @OnClick(R.id.slot_cars_button)
    fun onSlotCarsClicked() {
        returnWithResult(Game.SLOT_CARS)
    }

    @OnClick(R.id.vr_button)
    fun onVrClicked() {
        returnWithResult(Game.VR)
    }

    private fun returnWithResult(game: Game) {
        Log.d(TAG, "Returning $game")
        setResult(game.ordinal)
        finish()
    }
}
