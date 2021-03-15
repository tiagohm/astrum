package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.drivers.BaseDriver
import br.tiagohm.astrum.indi.protocol.State

// Some references:
// * https://ascom-standards.org/Help/Platform/html/T_ASCOM_DeviceInterface_ITelescopeV3.htm

// TODO: doPulse
// TODO: AltAz mount has HORIZONTAL_COORD or EQUATORIAL_EOD_COORD?
// TODO: Alignment feature!
// TODO: Whats clearParking does?
open class TelescopeDriver(
    override val name: String,
    override val executable: String,
) : BaseDriver() {

    /**
     * Determines if the telescope is tracking.
     */
    open val isTracking: Boolean
        get() = switch(TrackState.ON)

    /**
     * Determines if the telescope is parked.
     */
    open val isParked: Boolean
        get() = switch(Park.PARK)

    /**
     * Determines if the telescope is slewing.
     */
    open val isSlewing: Boolean
        get() = (has(Coordinate.RA) && element(Coordinate.RA)!!.state == State.BUSY) ||
                (has(Coordinate.AZ) && element(Coordinate.AZ)!!.state == State.BUSY)

    /**
     * Sends the coordinates to telescope.
     *
     * @param ra RA (JNow) in hours or azimuth in degrees
     * @param dec DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun coordinates(ra: Double, dec: Double) {
        if (mountType() == MountType.ALTAZ) {
            send(arrayOf(Coordinate.AZ, Coordinate.ALT), arrayOf(ra, dec))
        } else {
            send(arrayOf(Coordinate.RA, Coordinate.DEC), arrayOf(ra, dec))
        }
    }

    /**
     * Gets the current coordinates.
     *
     * @return The coordinates in RA/DEC (JNow) or Altitude/Azimuth in degrees.
     */
    open fun coordinates() = when (mountType()) {
        MountType.ALTAZ -> number(Coordinate.AZ) to number(Coordinate.ALT)
        else -> number(Coordinate.RA) to number(Coordinate.DEC)
    }

    /**
     * Moves the telescope to the coordinates and keep tracking.
     *
     * @param ra RA (JNow) in hours or azimuth in degrees
     * @param dec DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun goTo(ra: Double, dec: Double) {
        send(OnCoordSet.TRACK, true)
        coordinates(ra, dec)
    }

    /**
     * Slews the telescope to the coordinates and stop.
     *
     * @param ra RA (JNow) in hours or azimuth in degrees
     * @param dec DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun slewTo(ra: Double, dec: Double) {
        send(OnCoordSet.SLEW, true)
        coordinates(ra, dec)
    }

    /**
     * Syncs the coordinates to telescope.
     *
     * @param ra RA (JNow) in hours or azimuth in degrees
     * @param dec DEC (JNow) in degrees or altitude in degrees above horizon
     */
    open fun sync(ra: Double, dec: Double) {
        send(OnCoordSet.SYNC, true)
        coordinates(ra, dec)
    }

    /**
     * Moves the telescope towards [direction].
     */
    open fun move(direction: MotionDirection) = send(direction, true)

    /**
     * Stops moving the telescope towards [direction].
     */
    open fun stop(direction: MotionDirection) = send(direction, false)

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
     * Enables or disables the tracking.
     */
    open fun tracking(enabled: Boolean) = send(TrackState.ON, enabled)

    /**
     * Sends the custom track rate.
     */
    open fun trackRate(ra: Double, dec: Double) = send(arrayOf(TrackRate.RA, TrackRate.DE), arrayOf(ra, dec))

    /**
     * Gets the current track rate.
     *
     * @return The track rate of RA axis and DEC axis, respectively.
     */
    open fun trackRate() = number(TrackRate.RA) to number(TrackRate.DE)

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
     * Gets the rates at which the telescope may be moved about the specified [axis].
     */
    open fun axisRates(axis: MountAxis) = number(axis)

    open fun canGuide() = has(GuideDirection.NORTH) &&
            has(GuideDirection.SOUTH) &&
            has(GuideDirection.WEST) &&
            has(GuideDirection.EAST)

    open fun canPark() = has(Park.PARK)

    /**
     * Moves the telescope to its park position, stop all motion (or restrict to a small safe range).
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
     * @param ra RA/AZ park coordinates in degrees.
     * @param dec DEC/ALT park coordinates in degrees.
     */
    open fun parkPosition(ra: Double, dec: Double) {
        if (mountType() == MountType.ALTAZ) {
            send(ParkPosition.AZ, ra)
            send(ParkPosition.ALT, dec)
        } else {
            send(ParkPosition.HA, ra)
            send(ParkPosition.DEC, dec)
        }
    }

    /**
     * Sets the telescope's park position to be its current position.
     */
    open fun parkToCurrentPosition() = send(ParkOption.CURRENT, true)

    /**
     * Sets the telescope's park position to be the driver's default park position.
     */
    open fun parkToDefaultPosition() = send(ParkOption.DEFAULT, true)
}