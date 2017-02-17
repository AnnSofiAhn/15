package isotop.se.isotop15.models

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/12.
 */
data class DronisResponse(val status: Status, val data: Data) {

    data class Status(val success: Boolean, val info: String)

    data class Data(val startTime: Long,
                    val endTime: Long,
                    val duration: Double,
                    val devices: List<String>,
                    val hits: List<String>,
                    val finished: Boolean)
}
