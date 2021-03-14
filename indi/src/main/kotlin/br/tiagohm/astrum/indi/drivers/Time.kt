package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.TextProperty

enum class Time : TextProperty {
    UTC,
    OFFSET;

    override val propName = "TIME_UTC"

    override val elementName = name
}