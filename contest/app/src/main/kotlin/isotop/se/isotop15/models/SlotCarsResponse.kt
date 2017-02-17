package isotop.se.isotop15.models

import com.google.gson.annotations.SerializedName

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/16.
 */
data class SlotCarsResponse(@SerializedName("Name") val name: String,
                            @SerializedName("Controller") val controller: Int,
                            val error: String?)