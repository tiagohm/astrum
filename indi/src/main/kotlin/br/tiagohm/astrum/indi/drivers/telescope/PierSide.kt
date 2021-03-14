package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

/**
 * The pointing state of the telescope.
 */
enum class PierSide : SwitchElement {
    /**
     * Through the pole pointing state - Mount on the West side of pier (looking East).
     */
    WEST,

    /**
     * Normal pointing state - Mount on the East side of pier (looking West).
     */
    EAST;

    override val propName = "TELESCOPE_PIER_SIDE"

    override val elementName = "PIER_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(5))
    }
}