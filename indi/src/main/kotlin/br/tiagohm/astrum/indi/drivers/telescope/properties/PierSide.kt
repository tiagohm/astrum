package br.tiagohm.astrum.indi.drivers.telescope.properties

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
}