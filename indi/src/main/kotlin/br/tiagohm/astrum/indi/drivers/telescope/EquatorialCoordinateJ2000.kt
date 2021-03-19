package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class EquatorialCoordinateJ2000 : NumberElement {
    RA,
    DEC;

    override val propName = "EQUATORIAL_COORD"

    override val elementName = name
}