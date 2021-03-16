package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class CoordinateJ2000 : NumberElement {
    RA,
    DEC;

    override val propName = "EQUATORIAL_COORD"

    override val elementName = name

    companion object {

        fun parse(name: String) = valueOf(name)
    }
}