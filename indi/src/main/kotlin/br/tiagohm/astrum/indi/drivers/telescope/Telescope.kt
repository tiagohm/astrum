package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.PropertyAttribute
import br.tiagohm.astrum.indi.client.PropertyListener
import br.tiagohm.astrum.indi.drivers.Driver
import java.util.*
import kotlin.collections.ArrayList

open class Telescope(
    override val name: String,
    override val executable: String,
) : Driver {

    private val propertyListeners = ArrayList<PropertyListener>(1)

    override var client: Client? = null
        protected set

    /**
     * Determines if telescope is tracking.
     */
    open val isTracking: Boolean
        get() = switch(TrackState.ON)

    /**
     * Determines if telescope is parked.
     */
    open val isParked: Boolean
        get() = switch(Park.PARK)

    private val propertyListener = object : PropertyListener {
        override fun onProperty(device: String, propName: String, elementName: String, attr: PropertyAttribute, value: Any) {
            if (device == name) {
                propertyListeners.forEach { it.onProperty(device, propName, elementName, attr, value) }
            }
        }
    }

    override fun registerPropertyListener(listener: PropertyListener) {
        if (!propertyListeners.contains(listener)) propertyListeners.add(listener)
    }

    override fun unregisterPropertyListener(listener: PropertyListener) {
        propertyListeners.remove(listener)
    }

    override fun attach(client: Client) = also {
        this.client = client
        client.registerPropertyListener(propertyListener)
    }

    override fun detach() = also {
        client?.unregisterPropertyListener(propertyListener)
        client = null
    }

    /**
     * Sends the coordinates to telescope.
     *
     * @param raAz RA (JNow) in hours or azimuth in degrees
     * @param decAlt DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun coordinate(raAz: Double, decAlt: Double) {
        if (mountType() == MountType.ALTAZ) {
            send(arrayOf(Coordinate.AZ, Coordinate.ALT), arrayOf(raAz, decAlt))
        } else {
            send(arrayOf(Coordinate.RA, Coordinate.DEC), arrayOf(raAz, decAlt))
        }
    }

    /**
     * Gets the current coordinates.
     *
     * @return The coordinates in RA/DEC (JNow) or Altitude/Azimuth in degrees.
     */
    open fun coordinate() = when (mountType()) {
        MountType.ALTAZ -> number(Coordinate.AZ) to number(Coordinate.ALT)
        else -> number(Coordinate.RA) to number(Coordinate.DEC)
    }

    /**
     * Moves the telescope to the coordinates and keep tracking.
     *
     * @param raAz RA (JNow) in hours or azimuth in degrees
     * @param decAlt DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun goTo(raAz: Double, decAlt: Double) {
        send(OnCoordSet.TRACK, true)
        coordinate(raAz, decAlt)
    }

    /**
     * Slews the telescope to the coordinates and stop.
     *
     * @param raAz RA (JNow) in hours or azimuth in degrees
     * @param decAlt DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun slewTo(raAz: Double, decAlt: Double) {
        send(OnCoordSet.SLEW, true)
        coordinate(raAz, decAlt)
    }

    /**
     * Syncs the coordinates to telescope.
     *
     * @param raAz RA (JNow) in hours or azimuth in degrees
     * @param decAlt DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun sync(raAz: Double, decAlt: Double) {
        send(OnCoordSet.SYNC, true)
        coordinate(raAz, decAlt)
    }

    /**
     * Gets the telescope's mount type.
     */
    open fun mountType() = when {
        switch(MountType.ALTAZ) -> MountType.ALTAZ
        switch(MountType.EQ_FORK) -> MountType.EQ_FORK
        else -> MountType.EQ_GEM
    }

    /**
     * Gets the current track mode.
     */
    open fun trackMode() = when {
        switch(TrackMode.SIDEREAL) -> TrackMode.SIDEREAL
        switch(TrackMode.LUNAR) -> TrackMode.LUNAR
        switch(TrackMode.SOLAR) -> TrackMode.SOLAR
        else -> TrackMode.CUSTOM
    }

    /**
     * Sets the current telescope's track [mode].
     */
    open fun trackMode(mode: TrackMode) = send(mode, true)

    /**
     * Gets the supported slew rates.
     *
     * @return The supported slew rates or empty if Slew Rate is not supported.
     */
    open fun slewRates() = client?.elementNames(name, "TELESCOPE_SLEW_RATE")?.map { SlewRate(it) } ?: emptyList()

    /**
     * Gets the current slew rate.
     *
     * @return Thr current slew rate or [SlewRate.NONE] if Slew Rate is not supported.
     */
    open fun slewRate() = slewRates().firstOrNull { switch(it) } ?: SlewRate.NONE

    /**
     * Sets the current slew rate.
     * First, check if the slew [rate] is supported via [slewRates].
     */
    open fun slewRate(rate: SlewRate) = send(rate, true)

    open fun pierSide() = if (switch(PierSide.WEST)) PierSide.WEST else PierSide.EAST

    open fun pierSide(side: PierSide) = send(side, true)

    /**
     * Parks the telescope to HOME position.
     */
    open fun park() = send(Park.PARK, true)

    /**
     * Unparks the telescope.
     */
    open fun unpark() = send(Park.UNPARK, true)

    /**
     * Stops the telescope rapidly, but gracefully.
     */
    open fun abort() = send(AbortMotion.ABORT, true)

    /**
     * Gets the current park position.
     *
     * @return The coordinates in RA/DEC (JNow) or Altitude/Azimuth in degrees.
     */
    open fun parkPosition() = when (mountType()) {
        MountType.ALTAZ -> number(ParkPosition.AZ) to number(ParkPosition.ALT)
        else -> number(ParkPosition.HA) to number(ParkPosition.DEC)
    }

    /**
     * Sets the HOME park position (RA/DEC or AZ/ALT) in degrees.
     *
     * @param raAz RA/AZ park coordinates in degrees.
     * @param decAlt DEC/ALT park coordinates in degrees.
     */
    open fun parkPosition(raAz: Double, decAlt: Double) {
        if (mountType() == MountType.ALTAZ) {
            send(ParkPosition.AZ, raAz)
            send(ParkPosition.ALT, decAlt)
        } else {
            send(ParkPosition.HA, raAz)
            send(ParkPosition.DEC, decAlt)
        }
    }

    /**
     * Uses current coordinates as HOME park position.
     */
    fun parkToCurrentCoordinate() = send(ParkOption.CURRENT, true)

    /**
     * Uses driver's default park position.
     */
    fun parkToDefaultCoordinate() = send(ParkOption.DEFAULT, true)
}