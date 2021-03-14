package br.tiagohm.astrum.indi.protocol

enum class State(val text: String) {
    IDLE("Idle"),
    OK("Ok"),
    BUSY("Busy"),
    ALERT("Alert")
}