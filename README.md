# Astrum

Astrum is a Kotlin astronomy library and command-line tool to compute high-precision positions of the celestial
objects and Earth satellites.

Most of the algorithms were ported from [Stellarium](https://github.com/Stellarium/stellarium/).

Kotlin

```kotlin
val sun = Sun()
val earth = Earth(sun)
val site = ObservationSite("Observatório Pico dos Dias", -22.534444, -45.5825, 1864.0)
val o = Observer(
    earth,
    site,
    DateTime(2021, 2, 5, 12, 0, 0),
    DeltaTAlgorithmType.ESPEANAK_MEEUS,
    useTopocentricCoordinates = true,
    useNutation = true,
    useLightTravelTime = true,
)
val mercury = Mercury(sun)
val pos = mercury.computeEquinoxEquatorialPosition(o)
val radec = Algorithms.rectangularToSphericalCoordinates(pos)
```

## Features

## References and Inspirations

* [Stellarium](https://github.com/Stellarium/stellarium/)
* [Bill-Gray/lunar](https://github.com/Bill-Gray/lunar)
* [skyfielders/python-skyfield](https://github.com/skyfielders/python-skyfield)
