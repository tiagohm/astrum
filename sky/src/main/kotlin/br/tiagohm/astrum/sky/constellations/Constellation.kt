package br.tiagohm.astrum.sky.constellations

import br.tiagohm.astrum.sky.M_PI
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.math.Triad

enum class Constellation(
    val latinName: String,
    val iau: String,
    val genitive: String,
    val description: String, // https://starchild.gsfc.nasa.gov/docs/StarChild/questions/88constellations.html
) {
    /**
     * Andromeda.
     */
    AND("Andromeda", "And", "Andromedae", "Princess of Ethiopia"),

    /**
     * Antlia.
     */
    ANT("Antlia", "Ant", "Antliae", "Air pump"),

    /**
     * Apus.
     */
    APS("Apus", "Aps", "Apodis", "Bird of Paradise"),

    /**
     * Aquarius.
     */
    AQR("Aquarius", "Aqr", "Aquarii", "Water bearer"),

    /**
     * Aquila.
     */
    AQL("Aquila", "Aql", "Aquilae", "Eagle"),

    /**
     * Ara.
     */
    ARA("Ara", "Ara", "Arae", "Altar"),

    /**
     * Aries.
     */
    ARI("Aries", "Ari", "Arietis", "Ram"),

    /**
     * Auriga.
     */
    AUR("Auriga", "Aur", "Aurigae", "Charioteer"),

    /**
     * Boötes.
     */
    BOO("Boötes", "Boo", "Boötis", "Herdsman"),

    /**
     * Caelum.
     */
    CAE("Caelum", "Cae", "Caeli", "Graving tool"),

    /**
     * Camelopardalis.
     */
    CAM("Camelopardalis", "Cam", "Camelopardalis", "Giraffe"),

    /**
     * Cancer.
     */
    CNC("Cancer", "Cnc", "Cancri", "Crab"),

    /**
     * Canes Venatici.
     */
    CVN("Canes Venatici", "CVn", "Canum Venaticorum", "Hunting dogs"),

    /**
     * Canis Major.
     */
    CMA("Canis Major", "CMa", "Canis Majoris", "Big dog"),

    /**
     * Canis Minor.
     */
    CMI("Canis Minor", "CMi", "Canis Minoris", "Little dog"),

    /**
     * Capricornus.
     */
    CAP("Capricornus", "Cap", "Capricorni", "Sea goat"),

    /**
     * Carina.
     */
    CAR("Carina", "Car", "Carinae", "Keel of Argonauts' ship"),

    /**
     * Cassiopeia.
     */
    CAS("Cassiopeia", "Cas", "Cassiopeiae", "Queen of Ethiopia"),

    /**
     * Centaurus.
     */
    CEN("Centaurus", "Cen", "Centauri", "Centaur"),

    /**
     * Cepheus.
     */
    CEP("Cepheus", "Cep", "Cephei", "King of Ethiopia"),

    /**
     * Cetus.
     */
    CET("Cetus", "Cet", "Ceti", "Sea monster (whale)"),

    /**
     * Chamaeleon.
     */
    CHA("Chamaeleon", "Cha", "Chamaeleontis", "Chameleon"),

    /**
     * Circinus.
     */
    CIR("Circinus", "Cir", "Circini", "Compasses"),

    /**
     * Columba.
     */
    COL("Columba", "Col", "Columbae", "Dove"),

    /**
     * Coma Berenices.
     */
    COM("Coma Berenices", "Com", "Comae Berenices", "Berenice's hair"),

    /**
     * Corona Australis.
     */
    CRA("Corona Australis", "CrA", "Coronae Australis", "Southern crown"),

    /**
     * Corona Borealis.
     */
    CRB("Corona Borealis", "CrB", "Coronae Borealis", "Northern crown"),

    /**
     * Corvus.
     */
    CRV("Corvus", "Crv", "Corvi", "Crow"),

    /**
     * Crater.
     */
    CRT("Crater", "Crt", "Crateris", "Cup"),

    /**
     * Crux.
     */
    CRU("Crux", "Cru", "Crucis", "Cross"),

    /**
     * Cygnus.
     */
    CYG("Cygnus", "Cyg", "Cygni", "Swan"),

    /**
     * Delphinus.
     */
    DEL("Delphinus", "Del", "Delphini", "Porpoise"),

    /**
     * Dorado.
     */
    DOR("Dorado", "Dor", "Doradus", "Swordfish"),

    /**
     * Draco.
     */
    DRA("Draco", "Dra", "Draconis", "Dragon"),

    /**
     * Equuleus.
     */
    EQU("Equuleus", "Equ", "Equulei", "Little horse"),

    /**
     * Eridanus.
     */
    ERI("Eridanus", "Eri", "Eridani", "River"),

    /**
     * Fornax.
     */
    FOR("Fornax", "For", "Fornacis", "Furnace"),

    /**
     * Gemini.
     */
    GEM("Gemini", "Gem", "Geminorum", "Twins"),

    /**
     * Grus.
     */
    GRU("Grus", "Gru", "Gruis", "Crane"),

    /**
     * Hercules.
     */
    HER("Hercules", "Her", "Herculis", "Hercules, son of Zeus"),

    /**
     * Horologium.
     */
    HOR("Horologium", "Hor", "Horologii", "Clock"),

    /**
     * Hydra.
     */
    HYA("Hydra", "Hya", "Hydrae", "Sea serpent"),

    /**
     * Hydrus.
     */
    HYI("Hydrus", "Hyi", "Hydri", "Water snake"),

    /**
     * Indus.
     */
    IND("Indus", "Ind", "Indi", "Indian"),

    /**
     * Lacerta.
     */
    LAC("Lacerta", "Lac", "Lacertae", "Lizard"),

    /**
     * Leo Minor.
     */
    LMI("Leo Minor", "LMi", "Leonis Minoris", "Little lion"),

    /**
     * Leo.
     */
    LEO("Leo", "Leo", "Leonis", "Lion"),

    /**
     * Lepus.
     */
    LEP("Lepus", "Lep", "Leporis", "Hare"),

    /**
     * Libra.
     */
    LIB("Libra", "Lib", "Librae", "Balance"),

    /**
     * Lupus.
     */
    LUP("Lupus", "Lup", "Lupi", "Wolf"),

    /**
     * Lynx.
     */
    LYN("Lynx", "Lyn", "Lyncis", "Lynx"),

    /**
     * Lyra.
     */
    LYR("Lyra", "Lyr", "Lyrae", "Lyre"),

    /**
     * Mensa.
     */
    MEN("Mensa", "Men", "Mensae", "Table mountain"),

    /**
     * Microscopium.
     */
    MIC("Microscopium", "Mic", "Microscopii", "Microscope"),

    /**
     * Monoceros.
     */
    MON("Monoceros", "Mon", "Monocerotis", "Unicorn"),

    /**
     * Musca.
     */
    MUS("Musca", "Mus", "Muscae", "Fly"),

    /**
     * Norma.
     */
    NOR("Norma", "Nor", "Normae", "Carpenter's Level"),

    /**
     * Octans.
     */
    OCT("Octans", "Oct", "Octantis", "Octant"),

    /**
     * Ophiuchus.
     */
    OPH("Ophiuchus", "Oph", "Ophiuchi", "Holder of serpent"),

    /**
     * Orion.
     */
    ORI("Orion", "Ori", "Orionis", "Orion, the hunter"),

    /**
     * Orion.
     */
    PAV("Pavo", "Pav", "Pavonis", "Peacock"),

    /**
     * Pegasus.
     */
    PEG("Pegasus", "Peg", "Pegasi", "Pegasus, the winged horse"),

    /**
     * Perseus.
     */
    PER("Perseus", "Per", "Persei", "Perseus, hero who saved Andromeda"),

    /**
     * Phoenix.
     */
    PHE("Phoenix", "Phe", "Phoenicis", "Phoenix"),

    /**
     * Pictor.
     */
    PIC("Pictor", "Pic", "Pictoris", "Easel"),

    /**
     * Pisces.
     */
    PSC("Pisces", "Psc", "Piscium", "Fishes"),

    /**
     * Piscis Austrinus.
     */
    PSA("Piscis Austrinus", "PsA", "Piscis Austrini", "Southern fish"),

    /**
     * Puppis.
     */
    PUP("Puppis", "Pup", "Puppis", "Stern of the Argonauts' ship"),

    /**
     * Pyxis.
     */
    PYX("Pyxis", "Pyx", "Pyxidis", "Compass on the Argonauts' ship"),

    /**
     * Reticulum.
     */
    RET("Reticulum", "Ret", "Reticuli", "Net"),

    /**
     * Sagitta.
     */
    SGE("Sagitta", "Sge", "Sagittae", "Arrow"),

    /**
     * Sagittarius.
     */
    SGR("Sagittarius", "Sgr", "Sagittarii", "Archer"),

    /**
     * Scorpius.
     */
    SCO("Scorpius", "Sco", "Scorpii", "Scorpion"),

    /**
     * Sculptor.
     */
    SCL("Sculptor", "Scl", "Sculptoris", "Sculptor's tools"),

    /**
     * Scutum.
     */
    SCT("Scutum", "Sct", "Scuti", "Shield"),

    /**
     * Serpens.
     */
    SER("Serpens", "Ser", "Serpentis", "Serpent"),

    /**
     * Sextans.
     */
    SEX("Sextans", "Sex", "Sextantis", "Sextant"),

    /**
     * Taurus.
     */
    TAU("Taurus", "Tau", "Tauri", "Bull"),

    /**
     * Telescopium.
     */
    TEL("Telescopium", "Tel", "Telescopii", "Telescope"),

    /**
     * Triangulum Australe.
     */
    TRA("Triangulum Australe", "TrA", "Trianguli Australis", "Southern triangle"),

    /**
     * Triangulum.
     */
    TRI("Triangulum", "Tri", "Trianguli", "Triangle"),

    /**
     * Tucana.
     */
    TUC("Tucana", "Tuc", "Tucanae", "Toucan"),

    /**
     * Ursa Major.
     */
    UMA("Ursa Major", "UMa", "Ursae Majoris", "Big bear"),

    /**
     * Ursa Minor.
     */
    UMI("Ursa Minor", "UMi", "Ursae Minoris", "Little bear"),

    /**
     * Vela.
     */
    VEL("Vela", "Vel", "Velorum", "Sail of the Argonauts' ship"),

    /**
     * Virgo.
     */
    VIR("Virgo", "Vir", "Virginis", "Virgin"),

    /**
     * Volans.
     */
    VOL("Volans", "Vol", "Volantis", "Flying fish"),

    /**
     * Vulpecula.
     */
    VUL("Vulpecula", "Vul", "Vulpeculae", "Fox");

    companion object {

        fun find(o: Observer, posEquJNow: Triad): Constellation? {
            val pos1875 = Algorithms.j2000ToJ1875(o.equinoxEquatorialToJ2000(posEquJNow, false))
            val (ra1875, dec1875) = Algorithms.rectangularToSphericalCoordinates(pos1875)

            var ra1875InHours = ra1875.radians.value * 12.0 / M_PI
            if (ra1875InHours < 0.0) ra1875InHours += 24.0
            val dec1875InDeg = dec1875.degrees.value

            val lines = ConstellationLine.lines

            var entry = 0

            while (lines[entry].decLow > dec1875InDeg) entry++

            while (entry < lines.size) {
                while (lines[entry].raHigh <= ra1875InHours) entry++
                while (lines[entry].raLow >= ra1875InHours) entry++
                if (lines[entry].raHigh > ra1875InHours) return lines[entry].constellation else entry++
            }

            return null
        }
    }
}