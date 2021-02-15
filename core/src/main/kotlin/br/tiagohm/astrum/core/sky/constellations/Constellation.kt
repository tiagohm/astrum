package br.tiagohm.astrum.core.sky.constellations

import br.tiagohm.astrum.core.M_180_PI
import br.tiagohm.astrum.core.M_PI
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.algorithms.Algorithms
import br.tiagohm.astrum.core.algorithms.math.Triad

enum class Constellation(
    val latinName: String,
    val iau: String,
    val genitive: String,
    val description: String, // https://starchild.gsfc.nasa.gov/docs/StarChild/questions/88constellations.html
) {
    AND("Andromeda", "And", "Andromedae", "Princess of Ethiopia"),
    ANT("Antlia", "Ant", "Antliae", "Air pump"),
    APS("Apus", "Aps", "Apodis", "Bird of Paradise"),
    AQR("Aquarius", "Aqr", "Aquarii", "Water bearer"),
    AQL("Aquila", "Aql", "Aquilae", "Eagle"),
    ARA("Ara", "Ara", "Arae", "Altar"),
    ARI("Aries", "Ari", "Arietis", "Ram"),
    AUR("Auriga", "Aur", "Aurigae", "Charioteer"),
    BOO("Boötes", "Boo", "Boötis", "Herdsman"),
    CAE("Caelum", "Cae", "Caeli", "Graving tool"),
    CAM("Camelopardalis", "Cam", "Camelopardalis", "Giraffe"),
    CNC("Cancer", "Cnc", "Cancri", "Crab"),
    CVN("Canes Venatici", "CVn", "Canum Venaticorum", "Hunting dogs"),
    CMA("Canis Major", "CMa", "Canis Majoris", "Big dog"),
    CMI("Canis Minor", "CMi", "Canis Minoris", "Little dog"),
    CAP("Capricornus", "Cap", "Capricorni", "Sea goat"),
    CAR("Carina", "Car", "Carinae", "Keel of Argonauts' ship"),
    CAS("Cassiopeia", "Cas", "Cassiopeiae", "Queen of Ethiopia"),
    CEN("Centaurus", "Cen", "Centauri", "Centaur"),
    CEP("Cepheus", "Cep", "Cephei", "King of Ethiopia"),
    CET("Cetus", "Cet", "Ceti", "Sea monster (whale)"),
    CHA("Chamaeleon", "Cha", "Chamaeleontis", "Chameleon"),
    CIR("Circinus", "Cir", "Circini", "Compasses"),
    COL("Columba", "Col", "Columbae", "Dove"),
    COM("Coma Berenices", "Com", "Comae Berenices", "Berenice's hair"),
    CRA("Corona Australis", "CrA", "Coronae Australis", "Southern crown"),
    CRB("Corona Borealis", "CrB", "Coronae Borealis", "Northern crown"),
    CRV("Corvus", "Crv", "Corvi", "Crow"),
    CRT("Crater", "Crt", "Crateris", "Cup"),
    CRU("Crux", "Cru", "Crucis", "Cross"),
    CYG("Cygnus", "Cyg", "Cygni", "Swan"),
    DEL("Delphinus", "Del", "Delphini", "Porpoise"),
    DOR("Dorado", "Dor", "Doradus", "Swordfish"),
    DRA("Draco", "Dra", "Draconis", "Dragon"),
    EQU("Equuleus", "Equ", "Equulei", "Little horse"),
    ERI("Eridanus", "Eri", "Eridani", "River"),
    FOR("Fornax", "For", "Fornacis", "Furnace"),
    GEM("Gemini", "Gem", "Geminorum", "Twins"),
    GRU("Grus", "Gru", "Gruis", "Crane"),
    HER("Hercules", "Her", "Herculis", "Hercules, son of Zeus"),
    HOR("Horologium", "Hor", "Horologii", "Clock"),
    HYA("Hydra", "Hya", "Hydrae", "Sea serpent"),
    HYI("Hydrus", "Hyi", "Hydri", "Water snake"),
    IND("Indus", "Ind", "Indi", "Indian"),
    LAC("Lacerta", "Lac", "Lacertae", "Lizard"),
    LMI("Leo Minor", "LMi", "Leonis Minoris", "Little lion"),
    LEO("Leo", "Leo", "Leonis", "Lion"),
    LEP("Lepus", "Lep", "Leporis", "Hare"),
    LIB("Libra", "Lib", "Librae", "Balance"),
    LUP("Lupus", "Lup", "Lupi", "Wolf"),
    LYN("Lynx", "Lyn", "Lyncis", "Lynx"),
    LYR("Lyra", "Lyr", "Lyrae", "Lyre"),
    MEN("Mensa", "Men", "Mensae", "Table mountain"),
    MIC("Microscopium", "Mic", "Microscopii", "Microscope"),
    MON("Monoceros", "Mon", "Monocerotis", "Unicorn"),
    MUS("Musca", "Mus", "Muscae", "Fly"),
    NOR("Norma", "Nor", "Normae", "Carpenter's Level"),
    OCT("Octans", "Oct", "Octantis", "Octant"),
    OPH("Ophiuchus", "Oph", "Ophiuchi", "Holder of serpent"),
    ORI("Orion", "Ori", "Orionis", "Orion, the hunter"),
    PAV("Pavo", "Pav", "Pavonis", "Peacock"),
    PEG("Pegasus", "Peg", "Pegasi", "Pegasus, the winged horse"),
    PER("Perseus", "Per", "Persei", "Perseus, hero who saved Andromeda"),
    PHE("Phoenix", "Phe", "Phoenicis", "Phoenix"),
    PIC("Pictor", "Pic", "Pictoris", "Easel"),
    PSC("Pisces", "Psc", "Piscium", "Fishes"),
    PSA("Piscis Austrinus", "PsA", "Piscis Austrini", "Southern fish"),
    PUP("Puppis", "Pup", "Puppis", "Stern of the Argonauts' ship"),
    PYX("Pyxis", "Pyx", "Pyxidis", "Compass on the Argonauts' ship"),
    RET("Reticulum", "Ret", "Reticuli", "Net"),
    SGE("Sagitta", "Sge", "Sagittae", "Arrow"),
    SGR("Sagittarius", "Sgr", "Sagittarii", "Archer"),
    SCO("Scorpius", "Sco", "Scorpii", "Scorpion"),
    SCL("Sculptor", "Scl", "Sculptoris", "Sculptor's tools"),
    SCT("Scutum", "Sct", "Scuti", "Shield"),
    SER("Serpens", "Ser", "Serpentis", "Serpent"),
    SEX("Sextans", "Sex", "Sextantis", "Sextant"),
    TAU("Taurus", "Tau", "Tauri", "Bull"),
    TEL("Telescopium", "Tel", "Telescopii", "Telescope"),
    TRA("Triangulum Australe", "TrA", "Trianguli Australis", "Southern triangle"),
    TRI("Triangulum", "Tri", "Trianguli", "Triangle"),
    TUC("Tucana", "Tuc", "Tucanae", "Toucan"),
    UMA("Ursa Major", "UMa", "Ursae Majoris", "Big bear"),
    UMI("Ursa Minor", "UMi", "Ursae Minoris", "Little bear"),
    VEL("Vela", "Vel", "Velorum", "Sail of the Argonauts' ship"),
    VIR("Virgo", "Vir", "Virginis", "Virgin"),
    VOL("Volans", "Vol", "Volantis", "Flying fish"),
    VUL("Vulpecula", "Vul", "Vulpeculae", "Fox");

    companion object {

        fun find(o: Observer, pos: Triad): Constellation? {
            val pos1875 = Algorithms.j2000ToJ1875(o.equinoxEquatorialToJ2000(pos, false))
            var (ra1875, dec1875) = Algorithms.rectangularToSphericalCoordinates(pos1875)

            ra1875 *= 12.0 / M_PI // hours
            if (ra1875 < 0.0) ra1875 += 24.0
            dec1875 *= M_180_PI // degrees

            val lines = ConstellationLine.lines

            var entry = 0

            while (lines[entry].decLow > dec1875) entry++

            while (entry < lines.size) {
                while (lines[entry].raHigh <= ra1875) entry++
                while (lines[entry].raLow >= ra1875) entry++
                if (lines[entry].raHigh > ra1875) return lines[entry].constellation else entry++
            }

            return null
        }
    }
}