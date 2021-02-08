package br.tiagohm.astrum.core.time

@Suppress("NOTHING_TO_INLINE")
enum class DeltaTAlgorithmType(
    val deltaTnDot: Double,
    val deltaTStart: Int,
    val deltaTFinish: Int,
    val algorithm: DeltaTAlgorithm,
    val deltaTdontUseMoon: Boolean = false,
) {
    // Without correction, DeltaT is Zero. Like Stellarium versions before 0.12.
    NONE(-26.0, Int.MIN_VALUE, Int.MAX_VALUE, DeltaTWithoutCorrection, true),

    // Meeus & Simons (2000) algorithm for DeltaT
    MEEUS_SIMONS(-25.7376, 1620, 2000, DeltaTByMeeusSimons),

    // Espenak & Meeus (2006) algorithm for DeltaT (Recommended)
    ESPEANAK_MEEUS(-25.858, -1999, 3000, DeltaTByEspenakMeeus),

    // This is a trying area. Something is wrong with DeltaT, maybe ndot is still not applied correctly
    // Espenak & Meeus (2006) algorithm for DeltaT
    ESPEANAK_MEEUS_ZERO_MOON_ACCEL(-25.858, -1999, 3000, DeltaTByEspenakMeeus, true),

    ;

    /*
    TODO:
    SCHOCH,                    // Schoch (1931) algorithm for DeltaT
    CLEMENCE,                    // Clemence (1948) algorithm for DeltaT
    IAU,                        // IAU (1952) algorithm for DeltaT (based on observations by Spencer Jones (1939))
    ASTRONOMICAL_EPHEMERIS,        // Astronomical Ephemeris (1960) algorithm for DeltaT
    TuckermanGoldstine,        // Tuckerman (1962, 1964) & Goldstine (1973) algorithm for DeltaT
    MullerStephenson,            // Muller & Stephenson (1975) algorithm for DeltaT
    Stephenson1978,                     // Stephenson (1978) algorithm for DeltaT
    SchmadelZech1979,            // Schmadel & Zech (1979) algorithm for DeltaT
    MorrisonStephenson1982,    // Morrison & Stephenson (1982) algorithm for DeltaT (used by RedShift)
    StephensonMorrison1984,    // Stephenson & Morrison (1984) algorithm for DeltaT
    StephensonHoulden,            // Stephenson & Houlden (1986) algorithm for DeltaT
    Espenak,                    // Espenak (1987, 1989) algorithm for DeltaT
    Borkowski,                // Borkowski (1988) algorithm for DeltaT
    SchmadelZech1988,            // Schmadel & Zech (1988) algorithm for DeltaT
    ChaprontTouze,            // Chapront-Touzé & Chapront (1991) algorithm for DeltaT
    StephensonMorrison1995,    // Stephenson & Morrison (1995) algorithm for DeltaT
    Stephenson1997,                     // Stephenson (1997) algorithm for DeltaT
    ChaprontMeeus,            // Chapront, Chapront-Touze & Francou (1997) & Meeus (1998) algorithm for DeltaT
    JPL_HORIZONS,                // JPL Horizons algorithm for DeltaT
    MontenbruckPfleger,                 // Montenbruck & Pfleger (2000) algorithm for DeltaT
    ReingoldDershowitz,                 // Reingold & Dershowitz (2002, 2007) algorithm for DeltaT
    MorrisonStephenson2004,    // Morrison & Stephenson (2004, 2005) algorithm for DeltaT
    REIJS,                    // Reijs (2006) algorithm for DeltaT
    Banjevic,                    // Banjevic (2006) algorithm for DeltaT
    IslamSadiqQureshi,            // Islam, Sadiq & Qureshi (2008 + revisited 2013) algorithm for DeltaT (6 polynomials)
    KhalidSultanaZaidi,            // M. Khalid, Mariam Sultana and Faheem Zaidi polynomial approximation of time period 1620-2013 (2014)
    StephensonMorrisonHohenkerk2016,    // Stephenson, Morrison, Hohenkerk (2016) RSPA paper provides spline fit to observations for -720..2016 and else parabolic fit.
    Henriksson2017,            // Henriksson (2017) algorithm for DeltaT (The solution for Schoch formula for DeltaT (1931), but with ndot=-30.128"/cy^2)
     */

    inline fun compute(jd: Double) = algorithm.compute(jd)
}