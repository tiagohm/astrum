package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.drivers.BaseDriver

open class Focuser(
    override val name: String,
    override val executable: String,
) : BaseDriver() {

    open fun focusIn() = send(FocusDirection.INWARD, true)

    open fun focusOut() = send(FocusDirection.OUTWARD, true)

    open fun abort() = send(FocusAbortMotion.ABORT, true)

    open val isIn: Boolean
        get() = switch(FocusDirection.INWARD)

    open val isOut: Boolean
        get() = switch(FocusDirection.OUTWARD)

    open fun move(ms: Double) = send(FocusTimer.VALUE, ms)

    open fun moveAbsolute(n: Double) = send(FocusPosition.ABSOLUTE, n)

    open fun canMoveAbsolute() = has(FocusPosition.ABSOLUTE)

    open fun moveRelative(n: Double) = send(FocusPosition.RELATIVE, n)

    open fun canMoveRelative() = has(FocusPosition.RELATIVE)

    open fun hasBacklash() = has(FocusBackslashStep.VALUE)

    open fun backslash(steps: Int) {
        // Make sure focus compensation is enabled.
        if (steps != 0) send(FocusBackslashToggle.ENABLED, true)
        send(FocusBackslashStep.VALUE, steps.toDouble())
        // If steps = 0, disable compensation
        if (steps == 0) send(FocusBackslashToggle.DISABLED, true)
    }

    open fun backslash() = number(FocusBackslashStep.VALUE)

    open fun hasDeviation() = name == "Nikon DSLR Z6"
}