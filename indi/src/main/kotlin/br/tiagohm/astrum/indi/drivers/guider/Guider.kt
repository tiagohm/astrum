package br.tiagohm.astrum.indi.drivers.guider

import br.tiagohm.astrum.indi.drivers.Driver

// TODO: isPulseGuiding, guideRates
interface Guider : Driver {

    override fun initialize() {
        register(GuideDirection::class)
    }

    fun canGuide() = has(GuideDirection.NORTH) &&
            has(GuideDirection.SOUTH) &&
            has(GuideDirection.WEST) &&
            has(GuideDirection.EAST)

    fun pulse(
        ra: GuideDirection,
        raMs: Double,
        dec: GuideDirection,
        decMs: Double,
    ) {
        if ((ra == GuideDirection.WEST || ra == GuideDirection.EAST) &&
            dec == GuideDirection.NORTH || dec == GuideDirection.SOUTH
        ) {
            send(ra, raMs)
            send(dec, decMs)
        } else {
            error("Invalid guide direction: $ra/$dec")
        }
    }
}