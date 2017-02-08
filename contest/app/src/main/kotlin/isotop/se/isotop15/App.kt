package isotop.se.isotop15

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.picasso.Picasso
import isotop.se.isotop15.models.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/01/27.
 */
class App: Application() {

    lateinit var api: Api

    override fun onCreate() {
        super.onCreate()

        Log.d("App", "Starting the darned thing")
        val picasso = Picasso.Builder(this)
                .loggingEnabled(true)
                .build()

        try {
            Picasso.setSingletonInstance(picasso)
        } catch (e: IllegalStateException) {
            Log.e("App", "Couldn't set the Picasso singleton instance", e)
        }

        api = Retrofit.Builder()
                .baseUrl("https://isotop-15-birthday.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api::class.java)
    }
}