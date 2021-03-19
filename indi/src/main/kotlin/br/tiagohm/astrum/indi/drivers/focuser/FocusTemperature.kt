package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusTemperature : NumberElement {
    TEMPERATURE;

    override val propName = "FOCUS_TEMPERATURE"

    override val elementName = name
}