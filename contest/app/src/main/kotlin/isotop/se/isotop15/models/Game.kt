package isotop.se.isotop15.models

/**
 * @author Ann-Sofi Åhn
 * Created on 17/02/01.
 */
enum class Game(val title: String, val id: Int) {
    DRONE_RACE("Drone race", 1),
    SLOT_CARS("Slot cars", 2),
    ROBOT_WARS("Robot wars", 3),
    VR("VR", 4),
    NONE("Ingen tävling vald", -1)
}
