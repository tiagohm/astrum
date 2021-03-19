package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.drivers.Driver

interface Focuser : Driver {

    val isFocusIn: Boolean
        get() = switch(FocusDirection.INWARD)

    val isFocusOut: Boolean
        get() = switch(FocusDirection.OUTWARD)

    override fun initialize() {
        register(FocusDirection::class)
        register(FocusAbortMotion::class)
        register(FocusTimer::class)
        register(FocusPosition::class)
        register(FocusBackslashStep::class)
        register(FocusBackslashToggle::class)
        register(FocusTemperature::class)
    }

    fun focusIn() = send(FocusDirection.INWARD, true)

    fun focusOut() = send(FocusDirection.OUTWARD, true)

    fun abortFocusing() = send(FocusAbortMotion.ABORT, true)

    fun moveFocusByTime(ms: Double) = send(FocusTimer.VALUE, ms)

    fun moveAbsoluteFocus(n: Double) = send(FocusPosition.ABSOLUTE, n)

    fun canMoveAbsolute() = has(FocusPosition.ABSOLUTE)

    fun moveRelativeFocus(n: Double) = send(FocusPosition.RELATIVE, n)

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

    fun temperature() = number(FocusTemperature.TEMPERATURE)

    fun hasDeviation() = name == "Nikon DSLR Z6"
}