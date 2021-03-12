package br.tiagohm.astrum.indi.protocol.commands

enum class EnableBLOBState(val label: String) {
    NEVER("Never"),
    ALSO("Also"),
    ONLY("Only"),
}