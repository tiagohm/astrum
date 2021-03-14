package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class Coordinate(override val propName: String) : NumberElement {
    RA("EQUATORIAL_EOD_COORD"),
    DEC("EQUATORIAL_EOD_COORD"),
    AZ("HORIZONTAL_COORD"),
    ALT("HORIZONTAL_COORD"),
    LAT("GEOGRAPHIC_COORD"),
    LONG("GEOGRAPHIC_COORD"),
    ELEV("GEOGRAPHIC_COORD");

    override val elementName = name

    companion object {

        fun parse(name: String) = valueOf(name)
    }
}