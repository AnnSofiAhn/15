package isotop.se.isotop15

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.picasso.Picasso
import isotop.se.isotop15.models.DronisBackend
import isotop.se.isotop15.models.GameBackend
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/27.
 */
class App: Application() {

    lateinit var gameBackend: GameBackend
    lateinit var dronisBackend: DronisBackend

    override fun onCreate() {
        super.onCreate()

        Log.d("App", "Starting the darned thing")
        val picasso = Picasso.Builder(this)
                .build()

        try {
            Picasso.setSingletonInstance(picasso)
        } catch (e: IllegalStateException) {
            Log.e("App", "Couldn't set the Picasso singleton instance", e)
        }


        gameBackend = Retrofit.Builder()
                .baseUrl("https://isotop-15-birthday.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GameBackend::class.java)

        dronisBackend = Retrofit.Builder()
                .baseUrl("https://dronis.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DronisBackend::class.java)
    }
}