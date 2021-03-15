# Astrum

Astrum is a [Kotlin](https://kotlinlang.org/) astronomical library and command-line tool to compute high-precision positions of the celestial objects and control the astronomical
equipments (Mounts, Cameras, Focusers, etc) via [INDI](https://indilib.org/) Protocol.

## Sky

Set of astronomical algorithms to compute celestial object positions.

### Featues

* Compute high-precision positions for planets, moons, asteroids, comets, stars and DSOs
* Time correction
* Nutation and Precession
* Atmosphere Refraction and Extinction
* Lunar and Solar eclipses

Most of the algorithms were ported from [Stellarium](https://github.com/Stellarium/stellarium/).

### Example

```kotlin
    val sun = Sun()
val earth = Earth(sun)

val location = Location("Observatório Pico dos Dias", Degrees(-22.534444).radians, Degrees(-45.5825), Meter(1864.0))
val o = Observer(
    home = earth,
    location = location,
    jd = JulianDay(2021, 2, 5, 12, 0, 0),
    utcOffset = -3.0,
    timeCorrection = TimeCorrectionType.ESPEANAK_MEEUS,
    pressure = Millibar(1013.0),
    temperature = Celsius(15.0),
    extinctionCoefficient = 0.13,
    useTopocentricCoordinates = true,
    useNutation = true,
    useLightTravelTime = true,
    apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
)

val mars = Mars(sun)
val phobos = Phobos(mars)

println(phobos.visualMagnitude(o))
println(phobos.visualMagnitudeWithExtinction(o))
println(phobos.meanOppositionMagnitude)
println(phobos.equatorialJ2000(o))
println(phobos.equatorial(o))
println(phobos.hourAngle(o))
println(phobos.horizontal(o))
println(phobos.galactic(o))
println(phobos.supergalactic(o))
println(phobos.eclipticJ2000(o))
println(phobos.ecliptic(o))
println(phobos.parallacticAngle(o))
println(phobos.constellation(o))
println(phobos.elongation(o))
println(phobos.phaseAngle(o))
println(phobos.illumination(o))
println(phobos.distance(o))
println(phobos.distanceFromSun(o))
println(phobos.orbitalVelocity(o))
println(phobos.heliocentricVelocity(o))
println(phobos.angularSize(o) * 2)
```

### Installation

TODO

## INDI

Simplified INDI Client.

### Example

```kotlin
val client = Client("192.168.0.121", 7624)
client.registerMessageListener(this)
client.registerElementListener(this)
client.registerDriverListener(this)

client.connect()

val ra = client.element(Coordinate.RA)
client.send(TrackState.ON, true)

// Telescope

val telescope = client.telescopes().first()

val (ra, dec) = telescope.position()
telescope.tracking(true)
telescope.goto(ra, dec)

client.disconnect()
```

### Installation

TODO

## CLI

Command-line tool to compute celestial objects positions and control your astronomical equipments via INDI Protocol.

### Download

TODO

### Commands

* Run Astrum CLI and execute any command below:

`connect`

Connects to the server running on [host] using the port number [port].

Syntax: `connect [-h=<string>] [-p=<integer>]`

* `-h`, `--host`: INDI server host (default: localhost)
* `-p`, `--port`: INDI server port (default: 7624)

---

`disconnect`

Disconnects from the server.

---

`telescope`

Manages the telescopes and mounts.

Syntax: `telescope [COMMAND]`

Commands:

---

`telescope config datetime <arg0>`: Sets the UTC Date & Offset. Use NOW for get the current date.

* `arg0`(string): The UTC Date & Offset in ISO 8601 format

---

`telescope config location --lat=[<double|sexagesimal>] --lon=[<double|sexagesimal>] --e=[<double|sexagesimal>]`: Sets the Earth geodetic coordinate.

* `--lat`: Site latitude (-90 to +90), degrees +N
* `--lon`: Site longitude (0 to 360), degrees +E
* `-e`: Site elevation, meters

---

`telescope config slewrate <arg0>`: Sets the slew rate. Use `telescope info` command to see the available slew rates.

* `arg0`(string): The slew rate.

---

`telescope config trackmode <arg0>`: Sets the track mode.

* `arg0`:(string): The following track mode: SIDEREAL, SOLAR, LUNAR or CUSTOM

---

`telescope abort`:   Aborts the telescope.

---

`telescope goto --dec=<double|sexagesimal> --ra=<double|sexagesimal>`: Moves the telescope to the coordinates and keep tracking.

* `--dec`: DEC or altitude in degrees
* `--ra`: RA in hours or azimuth in degrees

---

`telescope info`: Shows the available telescopes' info.

---

`telescope list`: Lists the available telescopes.

---

`telescope off`: Disconnects the telescope.

---

`telescope on`: Connects the telescope.

---

`telescope park`: Moves the telescope to its park position.

---

`telescope pulse --dec=<double> --ra=<double>`: Pulses guiding for RA and DEC axes.

* `--dec`: DEC guiding pulse in milliseconds following by direction (N or S)
* `--ra`: RA guiding pulse in milliseconds following by direction (E or W)

Example: `pulse 1000.0N 2000.0W`

---

`telescope slew --dec=<double|sexagesimal> --ra=<double|sexagesimal>`: Slews the telescope to the coordinates and stop.

---

`telescope start`: Stars the tracking.

---

`telescope status`: Shows the telescope's status.

---

`telescope stop`: Stops the tracking.

---

`telescope sync --dec=<double|sexagesimal> --ra=<double|sexagesimal>`: Syncs the coordinates to the telescope.

---

`telescope unpark`: Unparks the telescope.
