package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class EquatorialCoordinate : NumberElement {
    RA,
    DEC;

    override val propName = "EQUATORIAL_EOD_COORD"

    override val elementName = name
}