package br.tiagohm.astrum.core.sky.atmosphere

enum class ApparentMagnitudeAlgorithm {
    MUELLER_1893,                // G. Mueller, based on visual observations 1877-91. [Explanatory Supplement to the Astronomical Almanac, 1961]
    ASTRONOMICAL_ALMANAC_1984,   // Astronomical Almanac 1984 and later. These give V (instrumental) magnitudes (allegedly from D.L. Harris, but this is wrong!)
    EXPLANATORY_SUPPLEMENT_1992, // Algorithm provided by Pere Planesas (Observatorio Astronomico Nacional) (Was called "Planesas")
    EXPLANATORY_SUPPLEMENT_2013, // Explanatory Supplement to the Astronomical Almanac, 3rd edition 2013
    GENERIC                      // Visual magnitude based on phase angle and albedo. The formula source for this is totally unknown!
}