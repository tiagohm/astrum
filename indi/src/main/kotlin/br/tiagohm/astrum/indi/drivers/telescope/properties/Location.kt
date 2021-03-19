package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class Location : NumberElement {
    LAT,
    LONG,
    ELEV;

    override val propName = "GEOGRAPHIC_COORD"

    override val elementName = name
}