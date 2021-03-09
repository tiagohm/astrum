package br.tiagohm.astrum.sky.core.units.distance

import br.tiagohm.astrum.sky.core.units.Unit

interface Distance : Unit, Comparable<Distance> {

    val meter: Meter

    val kilometer: Kilometer

    val au: AU

    val lightYear: LightYear

    val parsec: Parsec

    /**
     * Gets time in seconds for light to travel that distance.
     */
    val lightTime: Double
        get() = au.value * 0.005775183
}