package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.ISO_8601
import br.tiagohm.astrum.indi.drivers.Driver
import br.tiagohm.astrum.indi.drivers.Time
import br.tiagohm.astrum.indi.drivers.guider.Guider
import br.tiagohm.astrum.indi.parseISO8601
import java.time.ZonedDateTime
import java.util.*

// Some references:
// * https://ascom-standards.org/Help/Platform/html/T_ASCOM_DeviceInterface_ITelescopeV3.htm

// TODO: Mount Info

// TODO: AltAz mount has HORIZONTAL_COORD or EQUATORIAL_EOD_COORD?
// TODO: Alignment feature!
// TODO: Satellite tracking LX200

// https://github.com/indilib/indi/blob/master/drivers/telescope/ioptronv3.cpp
// TODO: GPS STATUS, Time Source
// TODO: HEMISPHERE
// TODO: FindHome/GoToHome?
// TODO: PEC
// TODO: Daylight (enviar junto com datetime?)
// TODO: Counter Weight State

interface Telescope : Driver {

    val isTracking: Boolean
        get() = switch(TrackState.ON)

    val isParked: Boolean
        get() = switch(Park.PARK)

    val isSlewing: Boolean
        get() = (has(EquatorialCoordinate.RA) && element(EquatorialCoordinate.RA)!!.isBusy) ||
                (has(HorizontalCoordinate.AZ) && element(HorizontalCoordinate.AZ)!!.isBusy)

    val isParking: Boolean
        get() = has(Park.PARK) && element(Park.PARK)!!.isBusy

    val hasJ2000: Boolean
        get() = has(EquatorialCoordinateJ2000.RA)

    override fun initialize() {
        register(EquatorialCoordinate::class)
        register(ParkOption::class)
        register(PierSide::class)
        register(TrackState::class)
        register(TrackRate::class)
        register(OnCoordSet::class) // TODO: Some scopes can support some this attrs
        register(TrackRate::class)
        // TODO: Track Satellite
        register(Park::class)
        register(TelescopeAbortMotion::class)
        register(MotionDirection::class)
        // TODO: Telescope Info

        register(Location::class)
        register(Time::class)

        // TODO: Snoop Device
    }

    fun position(ra: Double, dec: Double) = when (mountType()) {
        MountType.ALTAZ -> send(arrayOf(HorizontalCoordinate.AZ, HorizontalCoordinate.ALT), arrayOf(ra, dec))
        else -> send(arrayOf(EquatorialCoordinate.RA, EquatorialCoordinate.DEC), arrayOf(ra, dec))
    }

    fun position() = when (mountType()) {
        MountType.ALTAZ -> number(HorizontalCoordinate.AZ) to number(HorizontalCoordinate.ALT)
        else -> number(EquatorialCoordinate.RA) to number(EquatorialCoordinate.DEC)
    }

    fun goto(ra: Double, dec: Double) {
        send(OnCoordSet.TRACK, true)
        position(ra, dec)
    }

    fun slew(ra: Double, dec: Double) {
        send(OnCoordSet.SLEW, true)
        position(ra, dec)
    }

    fun sync(ra: Double, dec: Double) {
        send(OnCoordSet.SYNC, true)
        position(ra, dec)
    }

    fun move(direction: MotionDirection) = send(direction, true)

    fun stop(direction: MotionDirection) = send(direction, false)

    fun mountType() = when {
        switch(MountType.ALTAZ) -> MountType.ALTAZ
        switch(MountType.EQ_FORK) -> MountType.EQ_FORK
        else -> MountType.EQ_GEM
    }

    fun trackModes() = client.elements(name, "TELESCOPE_TRACK_MODE").map { it.element as TrackMode }

    fun trackMode() = trackModes().firstOrNull { switch(it) } ?: TrackMode.NONE

    fun trackMode(mode: TrackMode) = send(mode, true)

    fun tracking(enabled: Boolean) = send(if (enabled) TrackState.ON else TrackState.OFF, true)

    fun trackRate(ra: Double, dec: Double) = send(arrayOf(TrackRate.RA, TrackRate.DE), arrayOf(ra, dec))

    fun trackRate() = number(TrackRate.RA) to number(TrackRate.DE)

    fun slewRates() = client.elements(name, "TELESCOPE_SLEW_RATE").map { it.element as SlewRate }

    fun slewRate() = slewRates().firstOrNull { switch(it) } ?: SlewRate.NONE

    fun slewRate(rate: SlewRate) = send(rate, true)

    fun pierSide() = if (switch(PierSide.WEST)) PierSide.WEST else PierSide.EAST

    fun pierSide(side: PierSide) = send(side, true)

    fun axisRates(axis: MountAxis) = number(axis)

    fun canTrackSatellite() = has(SatTracking.TRACK)

    fun canPark() = has(Park.PARK)

    fun canSync() = has(OnCoordSet.SYNC)

    fun canGoto() = has(OnCoordSet.TRACK)

    fun canSlew() = has(OnCoordSet.SLEW)

    fun park() = send(Park.PARK, true)

    fun unpark() = send(Park.UNPARK, true)

    fun abortMotion() = send(TelescopeAbortMotion.ABORT, true)

    fun parkPosition() = when (mountType()) {
        MountType.ALTAZ -> number(ParkPosition.AZ) to number(ParkPosition.ALT)
        else -> number(ParkPosition.HA) to number(ParkPosition.DEC)
    }

    fun parkPosition(ra: Double, dec: Double) {
        if (mountType() == MountType.ALTAZ) {
            send(ParkPosition.AZ, ra)
            send(ParkPosition.ALT, dec)
        } else {
            send(ParkPosition.HA, ra)
            send(ParkPosition.DEC, dec)
        }
    }

    fun parkToCurrentPosition() = send(ParkOption.CURRENT, true)

    fun parkToDefaultPosition() = send(ParkOption.DEFAULT, true)

    fun clearParking() = send(ParkOption.PURGE_DATA, true)

    fun hasLocation() = has(Location.LONG) && has(Location.LAT) && has(Location.ELEV)

    fun location(longitude: Double, latitude: Double, elevation: Double) {
        val a = arrayOf(Location.LONG, Location.LAT, Location.ELEV)
        val b = arrayOf(longitude, latitude, elevation)
        send(a, b)
    }

    fun location() = Triple(number(Location.LONG), number(Location.LAT), number(Location.ELEV))

    fun hasTime() = has(Time.UTC)

    fun time(): ZonedDateTime {
        return if (hasTime()) {
            parseISO8601(text(Time.UTC), text(Time.OFFSET))
        } else {
            ZonedDateTime.now()
        }
    }

    fun time(utc: Date, offset: Double) {
        send(arrayOf(Time.UTC, Time.OFFSET), arrayOf(ISO_8601.format(utc), "$offset"))
    }

    fun time(dateTime: ZonedDateTime) {
        val utc = Date.from(dateTime.toInstant())
        val offset = dateTime.offset.totalSeconds / 3600.0
        time(utc, offset)
    }

    override fun status(): Map<String, Any> {
        val status = LinkedHashMap<String, Any>()
        status["SLEWING"] = isSlewing
        status["PARKING"] = isParking
        status["PARKED"] = isParked
        status["TRACKING"] = isTracking
        status["TRACK MODE"] = trackMode()
        status["SLEW RATE"] = slewRate()
        val coordinates = position()
        status["RA"] = coordinates.first
        status["DEC"] = coordinates.second
        return status
    }

    override fun info(): Map<String, Any> {
        val info = LinkedHashMap<String, Any>()
        info["CAN SYNC"] = canSync()
        info["CAN GOTO"] = canSync()
        info["CAN TRACK SATELLITE"] = canTrackSatellite()
        info["CAN GUIDE"] = this is Guider && canGuide()
        info["CAN PARK"] = canPark()
        info["SLEW RATES"] = slewRates().joinToString()
        info["TRACK MODES"] = trackModes().joinToString()
        val (raRate, decRate) = trackRate()
        info["TRACK RATE"] = "RA: $raRate DEC: $decRate"
        val (raPark, decPark) = parkPosition()
        info["PARK POSITION"] = "RA: $raPark DEC: $decPark"
        info["DATE TIME"] = if (hasTime()) time() else "NOT SUPPORTED"
        info["LOCATION"] = if (hasLocation()) location().let { loc -> "LON: ${loc.first} LAT: ${loc.second} ELEV: ${loc.third}" }
        else "NOT SUPPORTED"
        return info
    }
}