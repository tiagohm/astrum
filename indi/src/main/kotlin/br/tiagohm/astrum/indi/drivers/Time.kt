package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.TextElement

enum class Time : TextElement {
    UTC,
    OFFSET;

    override val propName = "TIME_UTC"

    override val elementName = name
}