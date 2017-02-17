package isotop.se.isotop15.models

import com.google.gson.annotations.SerializedName

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/16.
 */
data class Participation(val id: Int? = null,
                         @SerializedName("activity_id") val activityId: Int? = null,
                         @SerializedName("contestant_id") val contestantId: Int,
                         @SerializedName("created_at") val createdAt: String = "",
                         @SerializedName("updated_at") val updatedAt: String = "")