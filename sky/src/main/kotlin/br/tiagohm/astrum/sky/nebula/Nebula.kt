package br.tiagohm.astrum.sky.nebula

import br.tiagohm.astrum.sky.CelestialObject
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.coordinates.EquatorialCoord
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.LightYear
import kotlin.math.min

data class Nebula(
    override val id: String = "",
    val m: Int = 0, // Messier Catalog number
    val ngc: Int = 0, // New General Catalog number
    val ic: Int = 0, // Index Catalog number
    val c: Int = 0, // Caldwell Catalog number
    val b: Int = 0, // Barnard Catalog number (Dark Nebulae)
    val sh2: Int = 0, // Sharpless Catalog number (Catalogue of HII Regions (Sharpless, 1959))
    val vdb: Int = 0, // van den Bergh Catalog number (Catalogue of Reflection Nebulae (van den Bergh, 1966))
    val rcw: Int = 0, // RCW Catalog number (H-α emission regions in Southern Milky Way (Rodgers+, 1960))
    val ldn: Int = 0, // LDN Catalog number (Lynds' Catalogue of Dark Nebulae (Lynds, 1962))
    val lbn: Int = 0, // LBN Catalog number (Lynds' Catalogue of Bright Nebulae (Lynds, 1965))
    val cr: Int = 0, // Collinder Catalog number
    val mel: Int = 0, // Melotte Catalog number
    val pgc: Int = 0, // PGC number (Catalog of galaxies)
    val ugc: Int = 0, // UGC number (The Uppsala General Catalogue of Galaxies)
    val arp: Int = 0, // Arp number (Atlas of Peculiar Galaxies (Arp, 1966))
    val vv: Int = 0, // VV number (The Catalogue of Interacting Galaxies (Vorontsov-Velyaminov+, 2001))
    val dwb: Int = 0, // DWB number (Catalogue and distances of optically visible H II regions (Dickel+, 1969))
    val tr: Int = 0, // Tr number (Trumpler Catalogue)
    val st: Int = 0, // St number (Stock Catalogue)
    val ru: Int = 0, // Ru number (Ruprecht Catalogue)
    val vdbha: Int = 0, // vdB-Ha number (van den Bergh-Hagen Catalogue)
    val ced: String = "", // Ced number (Cederblad Catalog of bright diffuse Galactic nebulae)
    val pk: String = "", // PK number (Catalogue of Galactic Planetary Nebulae)
    val png: String = "", // PN G number (Strasbourg-ESO Catalogue of Galactic Planetary Nebulae (Acker+, 1992))
    val snrg: String = "", // SNR G number (A catalogue of Galactic supernova remnants (Green, 2014))
    val aco: String = "", // ACO number (Rich Clusters of Galaxies (Abell+, 1989))
    val hcg: String = "", // HCG number (Hickson Compact Group (Hickson, 1989))
    val eso: String = "", // ESO number (ESO/Uppsala Survey of the ESO(B) Atlas (Lauberts, 1982))
    val vdbh: String = "", // VdBH number (Southern Stars embedded in nebulosity (van den Bergh+, 1975))
    val mType: String = "", // Morphological type of object (as string)
    val bMag: Double = 99.0, // B magnitude
    val vMag: Double = 99.0, // V magnitude.
    val majorAxisSize: Angle = Degrees.ZERO, // Major axis size in degrees
    val minorAxisSize: Angle = Degrees.ZERO, // Minor axis size in degrees
    val orientation: Angle = Degrees.ZERO, // Orientation angle in degrees
    val distance: Distance = LightYear.ZERO, // distance
    val distanceError: Double = 0.0, // Error of distance
    val redshift: Double = 0.0,
    val redshiftError: Double = 0.0,
    val parallax: Double = 0.0, // Parallax in milliarcseconds (mas)
    val parallaxError: Double = 0.0,
    val surfaceBrightness: Double = 0.0, // mag/arcsec²
    val posEquJ2000: EquatorialCoord = EquatorialCoord.ZERO,
    val nebulaType: NebulaType = NebulaType.UNKNOWN,
    val h400: Boolean = false,
    val bennett: Boolean = false,
    val dunlop: Boolean = false,
    val names: List<String> = emptyList(),
) : CelestialObject {

    private val xyz by lazy { Algorithms.sphericalToRectangularCoordinates(posEquJ2000.ra, posEquJ2000.dec) }

    override val type = PlanetType.DSO

    override fun distance(o: Observer) = distance

    override fun constellation(o: Observer) = super.constellation(o)

    override fun visualMagnitude(o: Observer, extra: Any?) = min(vMag, bMag)

    override fun angularSize(o: Observer) = (majorAxisSize + minorAxisSize) * 0.5

    override fun computeJ2000EquatorialPosition(o: Observer) = xyz
}