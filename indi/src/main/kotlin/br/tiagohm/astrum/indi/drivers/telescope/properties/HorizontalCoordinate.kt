package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class HorizontalCoordinate : NumberElement {
    AZ,
    ALT;

    override val propName = "HORIZONTAL_COORD"

    override val elementName = name
}