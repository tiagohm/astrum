package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.drivers.Driver

interface Focuser : Driver {

    val isIn: Boolean
        get() = switch(FocusDirection.INWARD)

    val isOut: Boolean
        get() = switch(FocusDirection.OUTWARD)

    fun focusIn() = send(FocusDirection.INWARD, true)

    fun focusOut() = send(FocusDirection.OUTWARD, true)

    fun abort() = send(FocusAbortMotion.ABORT, true)

    fun move(ms: Double) = send(FocusTimer.VALUE, ms)

    fun moveAbsolute(n: Double) = send(FocusPosition.ABSOLUTE, n)

    fun canMoveAbsolute() = has(FocusPosition.ABSOLUTE)

    fun moveRelative(n: Double) = send(FocusPosition.RELATIVE, n)

    fun canMoveRelative() = has(FocusPosition.RELATIVE)

    fun hasBacklash() = has(FocusBackslashStep.VALUE)

    fun backslash(steps: Int) {
        // Make sure focus compensation is enabled.
        if (steps != 0) send(FocusBackslashToggle.ENABLED, true)
        send(FocusBackslashStep.VALUE, steps.toDouble())
        // If steps = 0, disable compensation
        if (steps == 0) send(FocusBackslashToggle.DISABLED, true)
    }

    fun backslash() = number(FocusBackslashStep.VALUE)

    fun hasDeviation() = name == "Nikon DSLR Z6"
}