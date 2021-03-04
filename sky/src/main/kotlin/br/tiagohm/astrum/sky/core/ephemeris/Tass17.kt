package br.tiagohm.astrum.sky.core.ephemeris

import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.time.JulianDay
import kotlin.math.cos
import kotlin.math.sin

/**
 * The TASS 1.7 theory of the Saturnian satellites including HYPERION by Alain VIENNE and Luc DURIEZ.
 * ftp://ftp.imcce.fr/pub/ephem/satel/tass17.
 */
object Tass17 {

    fun computeCoordinates(jd: JulianDay, body: Int): DoubleArray {
        return computeOsculatingCoordinates(jd, jd, body)
    }

    fun computeOsculatingCoordinates(jd0: JulianDay, jd: JulianDay, body: Int): DoubleArray {
        val t = jd0.value - 2444240.0

        val ts = doubleArrayOf(t0, t1, t2)
        val es = arrayOf(elem0, elem1, elem2)

        Algorithms.computeInterpolatedElements(t, elem, DIM, ::computeAllTass17Elem, DELTA_T, ts, es)

        t0 = ts[0]
        t1 = ts[1]
        t2 = ts[2]

        val be = elem.sliceArray(body * 6 until body * 6 + 6)
        val x = Algorithms.ellipticToRectangularN(SATS[body].mu, be, jd.value - jd0.value)

        return doubleArrayOf(
            TASS17_TO_VSOP87[0] * x[0] + TASS17_TO_VSOP87[1] * x[1] + TASS17_TO_VSOP87[2] * x[2],
            TASS17_TO_VSOP87[3] * x[0] + TASS17_TO_VSOP87[4] * x[1] + TASS17_TO_VSOP87[5] * x[2],
            TASS17_TO_VSOP87[6] * x[0] + TASS17_TO_VSOP87[7] * x[1] + TASS17_TO_VSOP87[8] * x[2],
            TASS17_TO_VSOP87[0] * x[3] + TASS17_TO_VSOP87[1] * x[4] + TASS17_TO_VSOP87[2] * x[5],
            TASS17_TO_VSOP87[3] * x[3] + TASS17_TO_VSOP87[4] * x[4] + TASS17_TO_VSOP87[5] * x[5],
            TASS17_TO_VSOP87[6] * x[3] + TASS17_TO_VSOP87[7] * x[4] + TASS17_TO_VSOP87[8] * x[5],
        )
    }

    private fun computeTass17Elem(t: Double, lon: DoubleArray, body: Int): DoubleArray {
        val elem = DoubleArray(6)

        for (i in 0..5) elem[i] = SATS[body].s0[i]

        for (tmt in SATS[body].series[0]) {
            var arg = 0.0

            for (i in 0..6) arg += tmt.i[i] * lon[i]
            for (tt in tmt.terms) elem[0] += tt[0] * cos(tt[1] + tt[2] * t + arg)
        }

        elem[0] = SATS[body].aam * (1.0 + elem[0])

        val start = if (body == 7) 0 else {
            // First multiterm already calculated.
            elem[1] += lon[body]
            1
        }

        for (k in start until SATS[body].series[1].size) {
            val tmt = SATS[body].series[1][k]
            var arg = 0.0

            for (i in 0..6) arg += tmt.i[i] * lon[i]
            for (tt in tmt.terms) elem[1] += tt[0] * sin(tt[1] + tt[2] * t + arg)
        }

        elem[1] += SATS[body].aam * t

        for (tmt in SATS[body].series[2]) {
            var arg = 0.0

            for (i in 0..6) arg += tmt.i[i] * lon[i]

            for (tt in tmt.terms) {
                val x = tt[1] + tt[2] * t + arg
                elem[2] += tt[0] * cos(x)
                elem[3] += tt[0] * sin(x)
            }
        }

        for (tmt in SATS[body].series[3]) {
            var arg = 0.0

            for (i in 0..6) arg += tmt.i[i] * lon[i]

            for (tt in tmt.terms) {
                val x = tt[1] + tt[2] * t + arg
                elem[4] += tt[0] * cos(x)
                elem[5] += tt[0] * sin(x)
            }
        }

        return elem
    }

    private fun computeAllTass17Elem(t: Double, elem: DoubleArray) {
        val lon = DoubleArray(7)
        computeLon(t, lon)

        for (body in 0..7) {
            computeTass17Elem(t, lon, body).copyInto(elem, body * 6)
        }
    }

    private fun computeLon(t: Double, lon: DoubleArray) {
        for (i in 0..6) {
            lon[i] = 0.0

            val tmt = SATS[i].series[1][0]

            for (tt in tmt.terms) {
                lon[i] += tt[0] * sin(tt[1] + tt[2] * t)
            }
        }
    }

    private class Term(
        val i: IntArray,
        val terms: Array<DoubleArray>,
    )

    private class Sat(
        val mu: Double,
        val aam: Double,
        val s0: DoubleArray,
        val series: Array<Array<Term>>,
    )

    private const val DIM = 8 * 6
    private const val DELTA_T = 1.0 // 1 day

    private var t0 = -1E100
    private var t1 = -1E100
    private var t2 = -1E100

    private val elem = DoubleArray(DIM)
    private val elem0 = DoubleArray(DIM)
    private val elem1 = DoubleArray(DIM)
    private val elem2 = DoubleArray(DIM)

    private val TASS17_TO_VSOP87 = doubleArrayOf(
        -9.833473364922412278E-01, -1.603871593615649693E-01, 8.546329577978411975E-02,
        1.817361158757799129E-01, -8.678312794665074866E-01, 4.624292968291581735E-01,
        0.000000000000000000E+00, 4.702603847778936010E-01, 8.825277165667645230E-01,
    )

    // Mimas

    private val MIMAS_0_0 = arrayOf(
        doubleArrayOf(2.760763800054540E-05, 6.863463000141887E-01, 2.437929552050393E-04),
        doubleArrayOf(9.347313993693880E-06, 2.209688858348459E+00, 2.791965241219151E-02),
    )

    private val MIMAS_0_1 = arrayOf(
        doubleArrayOf(1.247132122206555E-04, 3.384553386830940E+00, 6.649590915041870E+00),
        doubleArrayOf(5.773002078925660E-05, 9.293070332553311E-01, 6.649834707997076E+00),
        doubleArrayOf(5.767710349652186E-05, 2.698207086816744E+00, 6.649347122086664E+00),
        doubleArrayOf(1.211438560239529E-05, 2.011860786802310E+00, 6.649103329131461E+00),
        doubleArrayOf(1.201719293977645E-05, 4.757245986859100E+00, 6.650078500952279E+00),
    )

    private val MIMAS_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), MIMAS_0_0),
        Term(intArrayOf(2, 0, -2, 0, 0, 0, 0, 0), MIMAS_0_1),
    )

    private val MIMAS_1_0 = arrayOf(
        doubleArrayOf(9.283150504689616E-06, 1.804019257682701E+00, 1.990850081500073E-05),
        doubleArrayOf(9.230818991264319E-05, 2.435908416203692E+00, 8.594659879963006E-05),
        doubleArrayOf(7.345436389839680E-05, 4.239927673886392E+00, 1.058550996146308E-04),
        doubleArrayOf(5.060827138159436E-04, 4.533623190990083E+00, 1.578463564054092E-04),
        doubleArrayOf(2.628008938901560E-04, 5.445714149319756E-02, 1.777548572204099E-04),
        doubleArrayOf(1.096551493377600E-05, 2.023919695921281E+00, 2.238844543900385E-04),
        doubleArrayOf(7.574073228630605E-01, 6.863463000141887E-01, 2.437929552050393E-04),
        doubleArrayOf(1.317556115144799E-05, 2.490365557696890E+00, 2.637014560200400E-04),
        doubleArrayOf(1.969761970496516E-04, 4.459828112124973E+00, 3.098310531896687E-04),
        doubleArrayOf(2.459408712169538E-04, 3.122254716217880E+00, 3.297395540046694E-04),
        doubleArrayOf(2.799611810201398E-05, 5.219969491004272E+00, 4.016393116104485E-04),
        doubleArrayOf(7.266042399548720E-04, 1.372692600028377E+00, 4.875859104100786E-04),
        doubleArrayOf(9.274943256038984E-06, 6.670083626422765E-01, 5.735325092097087E-04),
        doubleArrayOf(1.284412109690001E-05, 2.764723137428667E+00, 6.454322668154877E-04),
        doubleArrayOf(1.243299165183161E-02, 2.059038900042566E+00, 7.313788656151179E-04),
        doubleArrayOf(8.715614709370008E-06, 4.494947316246257E+00, 8.173254644147480E-04),
        doubleArrayOf(3.590283311520520E-04, 3.431731500070944E+00, 1.218964776025196E-03),
        doubleArrayOf(3.847028724666396E-06, 6.152515926191327E+00, 1.366310570108340E-02),
        doubleArrayOf(1.276023939118606E-04, 5.446831688805226E+00, 1.374905229988303E-02),
        doubleArrayOf(9.505947727365781E-06, 3.697269572615722E+00, 1.390689865628844E-02),
        doubleArrayOf(5.198697062661200E-06, 4.383615872629911E+00, 1.415069161149348E-02),
        doubleArrayOf(1.163953693117700E-04, 3.677931635243811E+00, 1.423663821029311E-02),
        doubleArrayOf(4.870003920011340E-05, 3.292242611895686E+00, 2.718827354657639E-02),
        doubleArrayOf(2.237171274873740E-04, 8.369962583200818E-01, 2.743206650178144E-02),
        doubleArrayOf(1.059900648786554E-03, 4.664935211924063E+00, 2.767585945698648E-02),
        doubleArrayOf(2.266397525680950E-03, 2.209688858348459E+00, 2.791965241219151E-02),
        doubleArrayOf(1.022783782146454E-03, 2.896035158362648E+00, 2.816344536739655E-02),
        doubleArrayOf(2.097481794022580E-04, 3.582381458376836E+00, 2.840723832260159E-02),
        doubleArrayOf(4.473029935270700E-05, 4.268727758391025E+00, 2.865103127780663E-02),
    )

    private val MIMAS_1_1 = arrayOf(
        doubleArrayOf(8.436544361747722E-06, 5.237122972947232E+00, 4.050159832983069E-01),
    )

    private val MIMAS_1_2 = arrayOf(
        doubleArrayOf(1.456357118609923E-04, 2.429607332411430E-01, 6.649590915041870E+00),
        doubleArrayOf(6.713447572711532E-05, 4.070899686845125E+00, 6.649834707997076E+00),
        doubleArrayOf(6.681066992467156E-05, 5.839799740406537E+00, 6.649347122086664E+00),
        doubleArrayOf(1.422191153382851E-05, 5.153453440392031E+00, 6.649103329131461E+00),
        doubleArrayOf(1.410806846233703E-05, 1.615653333269246E+00, 6.650078500952279E+00),
    )

    private val MIMAS_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), MIMAS_1_0),
        Term(intArrayOf(-1, 3, -2, 0, 0, 0, 0, 0), MIMAS_1_1),
        Term(intArrayOf(2, 0, -2, 0, 0, 0, 0, 0), MIMAS_1_2),
    )

    private val MIMAS_2_0 = arrayOf(
        doubleArrayOf(1.607455685492625E-04, 4.012828055947553E+00, -1.044883867247364E-02),
        doubleArrayOf(1.578422930662670E-05, 5.932334220063532E+00, 1.625184896369267E-02),
        doubleArrayOf(7.291096595471061E-05, 3.477087866487927E+00, 1.649564191889771E-02),
        doubleArrayOf(6.526439278535280E-06, 4.869118403888217E+00, 1.665348827530312E-02),
        doubleArrayOf(3.336219324045869E-04, 1.021841512912324E+00, 1.673943487410275E-02),
        doubleArrayOf(8.915309088647409E-06, 3.161572755262240E-01, 1.682538147290238E-02),
        doubleArrayOf(1.733062364950220E-05, 2.413872050312614E+00, 1.689728123050816E-02),
        doubleArrayOf(1.511549643267883E-03, 4.849780466516306E+00, 1.698322782930779E-02),
        doubleArrayOf(2.716644193630320E-05, 4.144096229130206E+00, 1.706917442810742E-02),
        doubleArrayOf(3.214169596116360E-05, 6.241811003916594E+00, 1.714107418571320E-02),
        doubleArrayOf(7.314708595483528E-03, 2.394534112940702E+00, 1.722702078451283E-02),
        doubleArrayOf(4.732749947219130E-05, 1.688849875554602E+00, 1.731296738331245E-02),
        doubleArrayOf(2.051066737888520E-05, 6.449719967511965E-01, 1.738486714091824E-02),
        doubleArrayOf(1.598170384010833E-02, 6.222473066544683E+00, 1.747081373971786E-02),
        doubleArrayOf(3.701056581056820E-05, 2.375196175568790E+00, 1.755676033851749E-02),
        doubleArrayOf(7.741070178691200E-05, 1.331318296765385E+00, 1.762866009612328E-02),
        doubleArrayOf(7.111400179101439E-03, 6.256340593792847E-01, 1.771460669492290E-02),
        doubleArrayOf(6.027636670891800E-05, 3.061542475582978E+00, 1.780055329372254E-02),
        doubleArrayOf(2.628589083368250E-05, 2.017664596779575E+00, 1.787245305132832E-02),
        doubleArrayOf(1.462159663847020E-03, 1.311980359393475E+00, 1.795839965012795E-02),
        doubleArrayOf(2.018714468805330E-05, 3.747888775597167E+00, 1.804434624892758E-02),
        doubleArrayOf(7.427257818452120E-06, 2.704010896793763E+00, 1.811624600653336E-02),
        doubleArrayOf(3.306740201503266E-04, 1.998326659407663E+00, 1.820219260533298E-02),
        doubleArrayOf(6.793672394573960E-06, 4.434235075611354E+00, 1.828813920413261E-02),
        doubleArrayOf(7.462286917233880E-05, 2.684672959421853E+00, 1.844598556053802E-02),
        doubleArrayOf(1.636165431356800E-05, 3.371019259436040E+00, 1.868977851574306E-02),
        doubleArrayOf(7.716155921462711E-06, 7.762840176851795E-01, 4.490288024149930E-02),
        doubleArrayOf(1.650028552141730E-05, 4.604222971289160E+00, 4.514667319670434E-02),
        doubleArrayOf(1.155548887648690E-05, 2.148976617713557E+00, 4.539046615190938E-02),
        doubleArrayOf(1.620277455112710E-05, 2.835322917727745E+00, 4.563425910711441E-02),
        doubleArrayOf(7.443474433517800E-06, 3.521669217741933E+00, 4.587805206231945E-02),
    )

    private val MIMAS_2_1 = arrayOf(
        doubleArrayOf(2.602700419616530E-03, 1.822484926062486E-01, 6.667061728781588E+00),
    )

    private val MIMAS_2_2 = arrayOf(
        doubleArrayOf(5.322831922763783E-06, 5.176410732312329E+00, 4.224867970380249E-01),
    )

    private val MIMAS_2_3 = arrayOf(
        doubleArrayOf(6.248133126576452E-05, 4.252092258474006E-01, 1.331665264382346E+01),
        doubleArrayOf(2.892274500639771E-05, 4.253148179451381E+00, 1.331689643677866E+01),
        doubleArrayOf(2.889623530439334E-05, 6.022048233012799E+00, 1.331640885086825E+01),
        doubleArrayOf(6.069308702586342E-06, 5.335701932998608E+00, 1.331616505791305E+01),
        doubleArrayOf(6.020614391923135E-06, 1.797901825875778E+00, 1.331714022973387E+01),
    )

    private val MIMAS_2 = arrayOf(
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), MIMAS_2_0),
        Term(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0), MIMAS_2_1),
        Term(intArrayOf(-2, 3, 0, 0, 0, 0, 0, 0), MIMAS_2_2),
        Term(intArrayOf(3, 0, -2, 0, 0, 0, 0, 0), MIMAS_2_3),
    )

    private val MIMAS_3_0 = arrayOf(
        doubleArrayOf(5.339008498818640E-06, 4.924784125696391E+00, 9.986805201763754E-03),
        doubleArrayOf(1.140613650769890E-05, 2.469537772120787E+00, 1.023059815696879E-02),
        doubleArrayOf(8.188197469152401E-06, 1.429141854518208E-02, 1.047439111217383E-02),
        doubleArrayOf(1.141748624638240E-05, 7.006377185593701E-01, 1.071818406737887E-02),
        doubleArrayOf(5.211706783516070E-06, 1.386984018573560E+00, 1.096197702258391E-02),
        doubleArrayOf(1.139717898986310E-05, 1.236334060267667E+00, -1.622629652399248E-02),
        doubleArrayOf(5.206791451508880E-05, 5.499877602534791E-01, -1.647008947919752E-02),
        doubleArrayOf(2.328430197381575E-04, 6.146826767418875E+00, -1.671388243440256E-02),
        doubleArrayOf(5.186530053942100E-06, 5.693256976253909E-01, -1.679982903320219E-02),
        doubleArrayOf(1.412387702084000E-05, 1.613203576428794E+00, -1.687172879080797E-02),
        doubleArrayOf(1.074089419341231E-03, 5.460480467404687E+00, -1.695767538960760E-02),
        doubleArrayOf(1.838941226078010E-05, 6.166164704790789E+00, -1.704362198840723E-02),
        doubleArrayOf(4.248311992240600E-05, 9.268572764146046E-01, -1.711552174601300E-02),
        doubleArrayOf(5.301694964646620E-03, 4.774134167390498E+00, -1.720146834481264E-02),
        doubleArrayOf(5.463249591300440E-05, 5.479818404776600E+00, -1.728741494361227E-02),
        doubleArrayOf(2.612362501347000E-05, 2.405109764004165E-01, -1.735931470121805E-02),
        doubleArrayOf(1.188963618162444E-02, 4.087787867376310E+00, -1.744526130001768E-02),
        doubleArrayOf(1.417196313981720E-05, 4.793472104762412E+00, -1.753120789881731E-02),
        doubleArrayOf(3.275262890701040E-05, 5.837349983565814E+00, -1.760310765642309E-02),
        doubleArrayOf(5.317666807877856E-03, 2.598489137723283E-01, -1.768905425522272E-02),
        doubleArrayOf(2.254194030722800E-05, 4.107125804748224E+00, -1.777500085402235E-02),
        doubleArrayOf(1.876210282323130E-05, 2.009411029961832E+00, -1.784690061162813E-02),
        doubleArrayOf(1.092196251660480E-03, 2.715095267347932E+00, -1.793284721042776E-02),
        doubleArrayOf(1.200955094991380E-05, 2.791868511442401E-01, -1.801879380922739E-02),
        doubleArrayOf(6.138904676788960E-06, 4.464657383537437E+00, -1.809069356683316E-02),
        doubleArrayOf(2.224213393203463E-04, 5.170341620923537E+00, -1.817664016563279E-02),
        doubleArrayOf(5.011145194424460E-05, 1.342402667319555E+00, -1.842043312083783E-02),
        doubleArrayOf(1.083732897435760E-05, 3.797649020895159E+00, -1.866422607604287E-02),
        doubleArrayOf(1.978445332406060E-05, 5.019691662617644E+00, -4.536491371220917E-02),
    )

    private val MIMAS_3_1 = arrayOf(
        doubleArrayOf(1.477125534949241E-05, 2.559894425015774E+00, 1.335156871886320E+01),
        doubleArrayOf(6.813829579894155E-06, 1.873548125001586E+00, 1.335132492590799E+01),
        doubleArrayOf(6.707536779238787E-06, 1.046480714401697E-01, 1.335181251181840E+01),
    )

    private val MIMAS_3 = arrayOf(
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), MIMAS_3_0),
        Term(intArrayOf(3, 0, -2, 0, 0, 0, 0, 0), MIMAS_3_1),
    )

    private val ENCELADUS_0_0 = arrayOf(
        doubleArrayOf(2.265054397578757E-05, 7.519405621081341E-01, 4.579638210202045E+00),
    )

    private val ENCELADUS_0_1 = arrayOf(
        doubleArrayOf(7.116593169805980E-06, 5.461033287440088E-01, 2.514460612958353E+00),
    )

    private val ENCELADUS_0 = arrayOf(
        Term(intArrayOf(0, 2, 0, -2, 0, 0, 0, 0), ENCELADUS_0_0),
        Term(intArrayOf(0, 2, -2, 0, 0, 0, 0, 0), ENCELADUS_0_1),
    )

    private val ENCELADUS_1_0 = arrayOf(
        doubleArrayOf(4.496393702552367E-03, 2.342959364982154E+00, 1.549375812779411E-03),
        doubleArrayOf(3.354575501528797E-03, 4.597833882505114E+00, 4.427150026633873E-03),
        doubleArrayOf(3.106536996299520E-05, 2.912482457830643E+00, 8.854300053267745E-03),
    )

    private val ENCELADUS_1_1 = arrayOf(
        doubleArrayOf(4.253124471669380E-06, 1.946766624913765E+00, 2.289819105101023E+00),
    )

    private val ENCELADUS_1_2 = arrayOf(
        doubleArrayOf(2.407327778886120E-05, 4.985440644756694E+00, 1.257230306479177E+00),
    )

    private val ENCELADUS_1_3 = arrayOf(
        doubleArrayOf(5.359800347726300E-06, 1.150736720732397E+00, 8.382988370771400E+00),
    )

    private val ENCELADUS_1_4 = arrayOf(
        doubleArrayOf(2.157848301674358E-05, 3.893533207807963E+00, 4.579638210202045E+00),
    )

    private val ENCELADUS_1_5 = arrayOf(
        doubleArrayOf(2.107499273982886E-05, 5.461033287440088E-01, 2.514460612958353E+00),
    )

    private val ENCELADUS_1_6 = arrayOf(
        doubleArrayOf(1.204571746494518E-05, 3.481858748969675E+00, 4.492830157146606E-01),
    )

    private val ENCELADUS_1_7 = arrayOf(
        doubleArrayOf(1.082902927586888E-05, 2.389951319910909E+00, 3.771690919437530E+00),
    )

    private val ENCELADUS_1_8 = arrayOf(
        doubleArrayOf(6.457229782189520E-06, 4.233799311077810E+00, 5.028921225916706E+00),
    )

    private val ENCELADUS_1_9 = arrayOf(
        doubleArrayOf(4.159628279141040E-06, 6.077647302244714E+00, 6.286151532395882E+00),
    )

    private val ENCELADUS_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), ENCELADUS_1_0),
        Term(intArrayOf(0, 1, 0, -1, 0, 0, 0, 0), ENCELADUS_1_1),
        Term(intArrayOf(0, 1, -1, 0, 0, 0, 0, 0), ENCELADUS_1_2),
        Term(intArrayOf(0, 2, 0, 0, 0, -2, 0, 0), ENCELADUS_1_3),
        Term(intArrayOf(0, 2, 0, -2, 0, 0, 0, 0), ENCELADUS_1_4),
        Term(intArrayOf(0, 2, -2, 0, 0, 0, 0, 0), ENCELADUS_1_5),
        Term(intArrayOf(0, 2, -4, 2, 0, 0, 0, 0), ENCELADUS_1_6),
        Term(intArrayOf(0, 3, -3, 0, 0, 0, 0, 0), ENCELADUS_1_7),
        Term(intArrayOf(0, 4, -4, 0, 0, 0, 0, 0), ENCELADUS_1_8),
        Term(intArrayOf(0, 5, -5, 0, 0, 0, 0, 0), ENCELADUS_1_9),
    )

    private val ENCELADUS_2_0 = arrayOf(
        doubleArrayOf(6.526400221336371E-06, 2.681979744993624E+00, 2.445958739557666E-05),
    )

    private val ENCELADUS_2_1 = arrayOf(
        doubleArrayOf(4.864126392950970E-06, 2.790627620835740E+00, -3.797451619237667E+00),
    )

    private val ENCELADUS_2_2 = arrayOf(
        doubleArrayOf(5.778112672027477E-06, 1.733182516415079E+00, 1.471391305053998E-03),
        doubleArrayOf(7.443719437241270E-06, 3.988057033938039E+00, 4.349165518908460E-03),
        doubleArrayOf(4.803805197845248E-03, 3.189423405738944E+00, 5.898541331687871E-03),
        doubleArrayOf(1.097719996101334E-04, 5.532383110312554E+00, 7.447917144467280E-03),
        doubleArrayOf(6.715628349206410E-06, 4.645664974245721E+00, 1.032569135832174E-02),
    )

    private val ENCELADUS_2_3 = arrayOf(
        doubleArrayOf(5.311492417496450E-06, 2.536683592343353E-01, 2.071076138575381E+00),
    )

    private val ENCELADUS_2_4 = arrayOf(
        doubleArrayOf(1.576763094428542E-03, 7.997716879783442E-01, 4.585536751533733E+00),
    )

    private val ENCELADUS_2_5 = arrayOf(
        doubleArrayOf(1.305558563149728E-05, 4.693005675247022E+00, 8.138458320962040E-01),
    )

    private val ENCELADUS_2_6 = arrayOf(
        doubleArrayOf(2.328380239517312E-05, 5.990750337669914E+00, -4.433844743829719E-01),
    )

    private val ENCELADUS_2_7 = arrayOf(
        doubleArrayOf(1.134789737634680E-05, 4.693304903676271E+00, 9.165174961735778E+00),
    )

    private val ENCELADUS_2_8 = arrayOf(
        doubleArrayOf(5.297748739729408E-06, 4.146902346503011E+00, -1.700614780862149E+00),
    )

    private val ENCELADUS_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), ENCELADUS_2_0),
        Term(intArrayOf(0, -1, 0, 0, 0, 2, 0, 0), ENCELADUS_2_1),
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), ENCELADUS_2_2),
        Term(intArrayOf(0, -1, 2, 0, 0, 0, 0, 0), ENCELADUS_2_3),
        Term(intArrayOf(0, 1, 0, 0, 0, 0, 0, 0), ENCELADUS_2_4),
        Term(intArrayOf(0, -2, 3, 0, 0, 0, 0, 0), ENCELADUS_2_5),
        Term(intArrayOf(0, -3, 4, 0, 0, 0, 0, 0), ENCELADUS_2_6),
        Term(intArrayOf(0, 3, 0, -2, 0, 0, 0, 0), ENCELADUS_2_7),
        Term(intArrayOf(0, -4, 5, 0, 0, 0, 0, 0), ENCELADUS_2_8),
    )

    private val ENCELADUS_3_0 = arrayOf(
        doubleArrayOf(1.280938903343441E-04, 1.983149608096680E+00, -7.280483472742215E-03),
    )

    private val ENCELADUS_3_1 = arrayOf(
        doubleArrayOf(3.085667117081219E-05, 3.937780549016139E+00, -3.452416044929610E-03),
        doubleArrayOf(1.421727517964700E-05, 4.624126849030327E+00, -3.208623089724569E-03),
        doubleArrayOf(1.420735399816070E-05, 1.098415954121576E-01, -3.696209000134648E-03),
    )

    private val ENCELADUS_3 = arrayOf(
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), ENCELADUS_3_0),
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), ENCELADUS_3_1),
    )

    private val TETHYS_0_0 = arrayOf(
        doubleArrayOf(8.692361446792260E-06, 3.746226045578181E+00, 5.868527757813049E+00),
    )

    private val TETHYS_0_1 = arrayOf(
        doubleArrayOf(9.773647178903700E-06, 3.347429921083522E+00, 2.065177597243693E+00),
    )

    private val TETHYS_0_2 = arrayOf(
        doubleArrayOf(6.341490354446320E-06, 3.087559012405942E-01, 3.097766395865539E+00),
    )

    private val TETHYS_0 = arrayOf(
        Term(intArrayOf(0, 0, 2, 0, 0, -2, 0, 0), TETHYS_0_0),
        Term(intArrayOf(0, 0, 2, -2, 0, 0, 0, 0), TETHYS_0_1),
        Term(intArrayOf(0, 0, 3, -3, 0, 0, 0, 0), TETHYS_0_2),
    )

    private val TETHYS_1_0 = arrayOf(
        doubleArrayOf(3.094782117290198E-06, 1.804019257682701E+00, 1.990850081500073E-05),
        doubleArrayOf(1.001557835312868E-05, 6.318891585209909E-01, 6.603809798462935E-05),
        doubleArrayOf(1.080021159829085E-05, 5.577501069793485E+00, 8.594659879963006E-05),
        doubleArrayOf(4.021385382444620E-06, 4.239927673886392E+00, 1.058550996146308E-04),
        doubleArrayOf(2.555821179926381E-05, 1.392030537400290E+00, 1.578463564054092E-04),
        doubleArrayOf(1.267528991884128E-05, 3.196049795082991E+00, 1.777548572204099E-04),
        doubleArrayOf(1.157195347749026E-05, 5.165512349511074E+00, 2.238844543900385E-04),
        doubleArrayOf(3.597193003482037E-02, 3.827938953603982E+00, 2.437929552050393E-04),
        doubleArrayOf(9.845954273310880E-06, 5.631958211286683E+00, 2.637014560200400E-04),
        doubleArrayOf(9.194949884833879E-06, 1.318235458535180E+00, 3.098310531896687E-04),
        doubleArrayOf(9.815613284418477E-06, 6.263847369807674E+00, 3.297395540046694E-04),
        doubleArrayOf(1.717434823483564E-05, 2.078376837414479E+00, 4.016393116104485E-04),
        doubleArrayOf(3.432162123697680E-05, 4.514285253618171E+00, 4.875859104100786E-04),
        doubleArrayOf(7.218512993756520E-06, 3.808601016232069E+00, 5.735325092097087E-04),
        doubleArrayOf(1.266151790256666E-05, 5.906315791018461E+00, 6.454322668154877E-04),
        doubleArrayOf(5.891806577851199E-04, 5.200631553632359E+00, 7.313788656151179E-04),
        doubleArrayOf(1.323271931613666E-05, 1.353354662656465E+00, 8.173254644147480E-04),
        doubleArrayOf(1.697093421514454E-05, 2.901388464811505E-01, 1.218964776025196E-03),
        doubleArrayOf(3.767845663771360E-06, 3.010923272601534E+00, 1.366310570108340E-02),
        doubleArrayOf(6.001489632871980E-06, 2.305239035215433E+00, 1.374905229988303E-02),
        doubleArrayOf(8.037140287447099E-06, 5.556769190259295E-01, 1.390689865628844E-02),
        doubleArrayOf(3.600130988680400E-06, 1.242023219040118E+00, 1.415069161149348E-02),
        doubleArrayOf(5.489567383712060E-06, 5.363389816540173E-01, 1.423663821029311E-02),
        doubleArrayOf(1.052724821924284E-05, 3.978588911909874E+00, 2.743206650178144E-02),
        doubleArrayOf(4.944669618681220E-05, 1.523342558334271E+00, 2.767585945698648E-02),
        doubleArrayOf(1.049859753280688E-04, 5.351281511938252E+00, 2.791965241219151E-02),
        doubleArrayOf(4.772345678736420E-05, 6.037627811952441E+00, 2.816344536739655E-02),
        doubleArrayOf(9.867772453097199E-06, 4.407888047870434E-01, 2.840723832260159E-02),
    )

    private val TETHYS_1_1 = arrayOf(
        doubleArrayOf(3.381153874922860E-06, 3.443909349583986E+00, 2.934263878906524E+00),
    )

    private val TETHYS_1_2 = arrayOf(
        doubleArrayOf(4.252829449661740E-06, 2.159362918305770E+00, 1.937452727948935E+00),
    )

    private val TETHYS_1_3 = arrayOf(
        doubleArrayOf(2.857487995253398E-05, 1.029186337468635E-01, 1.032588798621846E+00),
    )

    private val TETHYS_1_4 = arrayOf(
        doubleArrayOf(1.086312362376316E-05, 3.746226045578181E+00, 5.868527757813049E+00),
    )

    private val TETHYS_1_5 = arrayOf(
        doubleArrayOf(5.706936513245540E-06, 1.177133183021748E+00, 3.874905455897870E+00),
    )

    private val TETHYS_1_6 = arrayOf(
        doubleArrayOf(2.612226809076254E-05, 3.347429921083522E+00, 2.065177597243693E+00),
    )

    private val TETHYS_1_7 = arrayOf(
        doubleArrayOf(1.309044788609482E-05, 3.087559012405942E-01, 3.097766395865539E+00),
    )

    private val TETHYS_1_8 = arrayOf(
        doubleArrayOf(7.589604761197260E-06, 3.553267188577251E+00, 4.130355194487387E+00),
    )

    private val TETHYS_1_9 = arrayOf(
        doubleArrayOf(4.749981493034660E-06, 5.145931687343214E-01, 5.162943993109232E+00),
    )

    private val TETHYS_1_10 = arrayOf(
        doubleArrayOf(4.209916715651780E-06, 1.843847991166901E+00, 1.257230306479177E+00),
    )

    private val TETHYS_1_11 = arrayOf(
        doubleArrayOf(9.634177151388563E-06, 3.197170586335357E+00, 2.290686578839635E-01),
    )

    private val TETHYS_1_12 = arrayOf(
        doubleArrayOf(3.367826063379420E-06, 3.687695982333802E+00, 2.514460612958353E+00),
    )

    private val TETHYS_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TETHYS_1_0),
        Term(intArrayOf(0, 0, 1, 0, 0, -1, 0, 0), TETHYS_1_1),
        Term(intArrayOf(0, 0, 1, 0, -1, 0, 0, 0), TETHYS_1_2),
        Term(intArrayOf(0, 0, 1, -1, 0, 0, 0, 0), TETHYS_1_3),
        Term(intArrayOf(0, 0, 2, 0, 0, -2, 0, 0), TETHYS_1_4),
        Term(intArrayOf(0, 0, 2, 0, -2, 0, 0, 0), TETHYS_1_5),
        Term(intArrayOf(0, 0, 2, -2, 0, 0, 0, 0), TETHYS_1_6),
        Term(intArrayOf(0, 0, 3, -3, 0, 0, 0, 0), TETHYS_1_7),
        Term(intArrayOf(0, 0, 4, -4, 0, 0, 0, 0), TETHYS_1_8),
        Term(intArrayOf(0, 0, 5, -5, 0, 0, 0, 0), TETHYS_1_9),
        Term(intArrayOf(0, 1, -1, 0, 0, 0, 0, 0), TETHYS_1_10),
        Term(intArrayOf(0, 1, -2, 1, 0, 0, 0, 0), TETHYS_1_11),
        Term(intArrayOf(0, 2, -2, 0, 0, 0, 0, 0), TETHYS_1_12),
    )

    private val TETHYS_2_0 = arrayOf(
        doubleArrayOf(2.463670320328344E-05, 2.681979744993623E+00, 2.445958739557666E-05),
    )

    private val TETHYS_2_1 = arrayOf(
        doubleArrayOf(6.736048525042149E-06, 1.795199654407042E+00, 3.940425661480334E-01),
    )

    private val TETHYS_2_2 = arrayOf(
        doubleArrayOf(1.012648120942390E-05, 4.634475612002642E+00, -2.540221312758490E+00),
    )

    private val TETHYS_2_3 = arrayOf(
        doubleArrayOf(1.316230344933185E-05, 9.203831673794880E-01, -5.465990108433128E-01),
    )

    private val TETHYS_2_4 = arrayOf(
        doubleArrayOf(9.537957898915770E-06, 1.891679082907508E+00, 1.263128847810865E+00),
    )

    private val TETHYS_2_5 = arrayOf(
        doubleArrayOf(1.026432488829525E-03, 5.239109003991030E+00, 3.328306445054557E+00),
    )

    private val TETHYS_2_6 = arrayOf(
        doubleArrayOf(4.858676635900159E-05, 4.930353102750437E+00, 2.305400491890175E-01),
    )

    private val TETHYS_2_7 = arrayOf(
        doubleArrayOf(1.274174154891934E-05, 4.827434469003571E+00, -8.020487494328282E-01),
    )

    private val TETHYS_2_8 = arrayOf(
        doubleArrayOf(4.893120486700722E-06, 1.582923181666916E+00, -1.834637548054674E+00),
    )

    private val TETHYS_2_9 = arrayOf(
        doubleArrayOf(1.379536465955919E-05, 4.874775170004872E+00, 1.471391305053998E-03),
    )

    private val TETHYS_2_10 = arrayOf(
        doubleArrayOf(4.496122761411780E-06, 2.849157684080121E+00, -4.433844743829719E-01),
    )

    private val TETHYS_2_11 = arrayOf(
        doubleArrayOf(1.564767415994558E-04, 4.568461127222154E+00, 3.458059983814795E-03),
        doubleArrayOf(8.681007704489939E-05, 7.405221736181721E-01, 3.214267028609755E-03),
        doubleArrayOf(8.168879255357250E-05, 5.941153727250531E+00, 3.945645894224873E-03),
        doubleArrayOf(8.101165266469901E-05, 3.195768527193775E+00, 2.970474073404717E-03),
        doubleArrayOf(7.081435508752854E-05, 5.254807427236342E+00, 3.701852939019836E-03),
        doubleArrayOf(1.795872905935810E-05, 3.443147200851330E-01, 4.189438849429914E-03),
        doubleArrayOf(1.765915793094640E-05, 5.651014880769380E+00, 2.726681118199677E-03),
        doubleArrayOf(6.448693043220393E-06, 8.713108941844011E-01, -1.044883867247364E-02),
        doubleArrayOf(4.134187193316490E-06, 3.483793623207228E-02, 3.300213627409385E-03),
        doubleArrayOf(4.064250964938940E-06, 5.960491664622441E+00, 3.615906340220205E-03),
        doubleArrayOf(3.921367852619880E-06, 1.030661020099323E+00, 4.433231804634951E-03),
        doubleArrayOf(3.847793440490170E-06, 1.823075927165398E+00, 2.482888162994638E-03),
    )

    private val TETHYS_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TETHYS_2_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), TETHYS_2_1),
        Term(intArrayOf(0, 0, -1, 0, 0, 2, 0, 0), TETHYS_2_2),
        Term(intArrayOf(0, 0, -1, 0, 2, 0, 0, 0), TETHYS_2_3),
        Term(intArrayOf(0, 0, -1, 2, 0, 0, 0, 0), TETHYS_2_4),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, 0, 0), TETHYS_2_5),
        Term(intArrayOf(0, 0, -2, 3, 0, 0, 0, 0), TETHYS_2_6),
        Term(intArrayOf(0, 0, -3, 4, 0, 0, 0, 0), TETHYS_2_7),
        Term(intArrayOf(0, 0, -4, 5, 0, 0, 0, 0), TETHYS_2_8),
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), TETHYS_2_9),
        Term(intArrayOf(0, -3, 4, 0, 0, 0, 0, 0), TETHYS_2_10),
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), TETHYS_2_11),
    )

    private val TETHYS_3_0 = arrayOf(
        doubleArrayOf(6.111685892210941E-06, 6.135349392018898E+00, -2.445240135601595E-05),
        doubleArrayOf(4.341737854415172E-06, 2.696600432337302E+00, -4.804041710792879E-04),
    )

    private val TETHYS_3_1 = arrayOf(
        doubleArrayOf(8.097194747293030E-06, 1.086326741907496E+00, -2.233451268904413E-03),
        doubleArrayOf(3.696660017446820E-05, 3.999804418933084E-01, -2.477244224109452E-03),
        doubleArrayOf(3.369179308695820E-06, 2.149542558082810E+00, -2.635090580514859E-03),
        doubleArrayOf(1.634061837956512E-04, 5.996819449058704E+00, -2.721037179314491E-03),
        doubleArrayOf(3.688282109438380E-06, 4.193183792652183E-01, -2.806983778114122E-03),
        doubleArrayOf(1.001531421510340E-05, 1.463196258068622E+00, -2.878883535719899E-03),
        doubleArrayOf(7.269088792971872E-04, 5.310473149044516E+00, -2.964830134519532E-03),
        doubleArrayOf(1.307143245256740E-05, 6.016157386430616E+00, -3.050776733319159E-03),
        doubleArrayOf(3.003084312634590E-05, 7.768499580544321E-01, -3.122676490924936E-03),
        doubleArrayOf(3.578626570900766E-03, 4.624126849030327E+00, -3.208623089724569E-03),
        doubleArrayOf(3.863423686243950E-05, 5.329811086416426E+00, -3.294569688524200E-03),
        doubleArrayOf(1.853853482238710E-05, 9.050365804024403E-02, -3.366469446129977E-03),
        doubleArrayOf(7.978986672131195E-03, 3.937780549016139E+00, -3.452416044929610E-03),
        doubleArrayOf(1.026902134395760E-05, 4.643464786402239E+00, -3.538362643729240E-03),
        doubleArrayOf(2.358139037174070E-05, 5.687342665205642E+00, -3.610262401335017E-03),
        doubleArrayOf(3.586811056529552E-03, 1.098415954121576E-01, -3.696209000134648E-03),
        doubleArrayOf(1.598764340805630E-05, 3.957118486388051E+00, -3.782155598934278E-03),
        doubleArrayOf(1.348141163501630E-05, 1.859403711601660E+00, -3.854055356540055E-03),
        doubleArrayOf(7.456079964789274E-04, 2.565087948987761E+00, -3.940001955339688E-03),
        doubleArrayOf(8.589176280796499E-06, 1.291795327840677E-01, -4.025948554139318E-03),
        doubleArrayOf(4.417994658707790E-06, 4.314650065177265E+00, -4.097848311745095E-03),
        doubleArrayOf(1.629278673196500E-04, 5.020334302563366E+00, -4.183794910544728E-03),
        doubleArrayOf(3.605229442958720E-05, 1.192395348959384E+00, -4.427587865749766E-03),
        doubleArrayOf(7.804425391545350E-06, 3.647641702534988E+00, -4.671380820954806E-03),
        doubleArrayOf(3.824550961047250E-06, 4.774776807336220E+00, 2.397965045685182E-02),
        doubleArrayOf(8.194661069081041E-06, 2.319530453760617E+00, 2.422344341205686E-02),
        doubleArrayOf(5.770004540143910E-06, 6.147469407364597E+00, 2.446723636726190E-02),
        doubleArrayOf(8.055438816212310E-06, 5.506304001991995E-01, 2.471102932246693E-02),
        doubleArrayOf(3.690275690503300E-06, 1.236976700213389E+00, 2.495482227767198E-02),
        doubleArrayOf(1.403252488381090E-05, 4.869684344257474E+00, -3.137206845712112E-02),
    )

    private val TETHYS_3_2 = arrayOf(
        doubleArrayOf(4.066369173620947E-06, 2.572521517863344E-01, 6.660065306154045E+00),
    )

    private val TETHYS_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TETHYS_3_0),
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), TETHYS_3_1),
        Term(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0), TETHYS_3_2),
    )

    private val DIONE_0_0 = arrayOf(
        doubleArrayOf(5.044474622853420E-06, 5.198036938148699E+00, 9.048639293270888E-01),
    )

    private val DIONE_0_1 = arrayOf(
        doubleArrayOf(1.975589414013096E-05, 3.540388778084452E+00, 3.803350160569355E+00),
    )

    private val DIONE_0_2 = arrayOf(
        doubleArrayOf(1.231028575289072E-05, 9.712959155280194E-01, 1.809727858654177E+00),
    )

    private val DIONE_0_3 = arrayOf(
        doubleArrayOf(5.098846661514180E-06, 5.981941867419887E-01, 5.705025240854031E+00),
    )

    private val DIONE_0_4 = arrayOf(
        doubleArrayOf(7.467512730405980E-06, 3.027740200086925E+00, 2.714591787981265E+00),
    )

    private val DIONE_0_5 = arrayOf(
        doubleArrayOf(4.727673963003260E-06, 5.084184484645832E+00, 3.619455717308356E+00),
    )

    private val DIONE_0_6 = arrayOf(
        doubleArrayOf(3.991422799327240E-06, 1.029186337468635E-01, 1.032588798621846E+00),
    )

    private val DIONE_0_7 = arrayOf(
        doubleArrayOf(4.809588755287700E-06, 2.058372674937290E-01, 2.065177597243693E+00),
    )

    private val DIONE_0_8 = arrayOf(
        doubleArrayOf(4.044589508141894E-06, 2.614152002392929E-01, 2.294246255127657E+00),
    )

    private val DIONE_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 1, -1, 0, 0, 0), DIONE_0_0),
        Term(intArrayOf(0, 0, 0, 2, 0, -2, 0, 0), DIONE_0_1),
        Term(intArrayOf(0, 0, 0, 2, -2, 0, 0, 0), DIONE_0_2),
        Term(intArrayOf(0, 0, 0, 3, 0, -3, 0, 0), DIONE_0_3),
        Term(intArrayOf(0, 0, 0, 3, -3, 0, 0, 0), DIONE_0_4),
        Term(intArrayOf(0, 0, 0, 4, -4, 0, 0, 0), DIONE_0_5),
        Term(intArrayOf(0, 0, 1, -1, 0, 0, 0, 0), DIONE_0_6),
        Term(intArrayOf(0, 0, 2, -2, 0, 0, 0, 0), DIONE_0_7),
        Term(intArrayOf(0, 1, 0, -1, 0, 0, 0, 0), DIONE_0_8),
    )

    private val DIONE_1_0 = arrayOf(
        doubleArrayOf(1.253214092917414E-04, 5.484552018571947E+00, 1.549375812779411E-03),
        doubleArrayOf(9.470863297623297E-05, 1.456241228915321E+00, 4.427150026633873E-03),
        doubleArrayOf(2.711677399803780E-06, 6.054075111420435E+00, 8.854300053267745E-03),
    )

    private val DIONE_1_1 = arrayOf(
        doubleArrayOf(4.269998249018742E-06, 5.396405225690409E+00, 3.940181065606379E-01),
    )

    private val DIONE_1_2 = arrayOf(
        doubleArrayOf(1.025994897473446E-05, 1.993980622473295E-01, 1.901675080284677E+00),
    )

    private val DIONE_1_3 = arrayOf(
        doubleArrayOf(2.682461086510494E-05, 5.198036938148699E+00, 9.048639293270888E-01),
    )

    private val DIONE_1_4 = arrayOf(
        doubleArrayOf(2.565388997294820E-05, 3.540388778084452E+00, 3.803350160569355E+00),
    )

    private val DIONE_1_5 = arrayOf(
        doubleArrayOf(3.125192040346257E-06, 1.285576205981393E+00, 3.409332054008717E+00),
    )

    private val DIONE_1_6 = arrayOf(
        doubleArrayOf(2.717321774931640E-05, 9.712959155280194E-01, 1.809727858654177E+00),
    )

    private val DIONE_1_7 = arrayOf(
        doubleArrayOf(5.552232090659460E-06, 5.981941867419887E-01, 5.705025240854031E+00),
    )

    private val DIONE_1_8 = arrayOf(
        doubleArrayOf(1.270721017173096E-05, 3.027740200086925E+00, 2.714591787981265E+00),
    )

    private val DIONE_1_9 = arrayOf(
        doubleArrayOf(6.847120194215620E-06, 5.084184484645832E+00, 3.619455717308356E+00),
    )

    private val DIONE_1_10 = arrayOf(
        doubleArrayOf(3.974354213288560E-06, 8.574434620251531E-01, 4.524319646635444E+00),
    )

    private val DIONE_1_11 = arrayOf(
        doubleArrayOf(1.499025754719972E-05, 3.244511287336658E+00, 1.032588798621846E+00),
    )

    private val DIONE_1_12 = arrayOf(
        doubleArrayOf(1.201329949530382E-05, 2.058372674937290E-01, 2.065177597243693E+00),
    )

    private val DIONE_1_13 = arrayOf(
        doubleArrayOf(6.090479694942740E-06, 3.450348554830387E+00, 3.097766395865539E+00),
    )

    private val DIONE_1_14 = arrayOf(
        doubleArrayOf(3.552297626424840E-06, 4.116745349874580E-01, 4.130355194487387E+00),
    )

    private val DIONE_1_15 = arrayOf(
        doubleArrayOf(4.723715451200404E-06, 3.403007853829086E+00, 2.294246255127657E+00),
    )

    private val DIONE_1_16 = arrayOf(
        doubleArrayOf(4.770404124767720E-06, 5.557793274556413E-02, 2.290686578839635E-01),
    )

    private val DIONE_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), DIONE_1_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), DIONE_1_1),
        Term(intArrayOf(0, 0, 0, 1, 0, -1, 0, 0), DIONE_1_2),
        Term(intArrayOf(0, 0, 0, 1, -1, 0, 0, 0), DIONE_1_3),
        Term(intArrayOf(0, 0, 0, 2, 0, -2, 0, 0), DIONE_1_4),
        Term(intArrayOf(0, 0, 0, 2, 0, -3, 0, 0), DIONE_1_5),
        Term(intArrayOf(0, 0, 0, 2, -2, 0, 0, 0), DIONE_1_6),
        Term(intArrayOf(0, 0, 0, 3, 0, -3, 0, 0), DIONE_1_7),
        Term(intArrayOf(0, 0, 0, 3, -3, 0, 0, 0), DIONE_1_8),
        Term(intArrayOf(0, 0, 0, 4, -4, 0, 0, 0), DIONE_1_9),
        Term(intArrayOf(0, 0, 0, 5, -5, 0, 0, 0), DIONE_1_10),
        Term(intArrayOf(0, 0, 1, -1, 0, 0, 0, 0), DIONE_1_11),
        Term(intArrayOf(0, 0, 2, -2, 0, 0, 0, 0), DIONE_1_12),
        Term(intArrayOf(0, 0, 3, -3, 0, 0, 0, 0), DIONE_1_13),
        Term(intArrayOf(0, 0, 4, -4, 0, 0, 0, 0), DIONE_1_14),
        Term(intArrayOf(0, 1, 0, -1, 0, 0, 0, 0), DIONE_1_15),
        Term(intArrayOf(0, 1, -2, 1, 0, 0, 0, 0), DIONE_1_16),
    )

    private val DIONE_2_0 = arrayOf(
        doubleArrayOf(1.172252747692422E-04, 2.681979734714067E+00, 2.445958739557666E-05),
    )

    private val DIONE_2_1 = arrayOf(
        doubleArrayOf(1.288509519128533E-05, 1.795199654407042E+00, 3.940425661480334E-01),
    )

    private val DIONE_2_2 = arrayOf(
        doubleArrayOf(3.368317726971210E-06, 3.079746085685258E+00, 1.390853717105622E+00),
    )

    private val DIONE_2_3 = arrayOf(
        doubleArrayOf(2.527837703075836E-05, 1.595801592159713E+00, -1.507632514136644E+00),
    )

    private val DIONE_2_4 = arrayOf(
        doubleArrayOf(3.539705714890319E-06, 3.850614164262772E+00, -1.113614407576006E+00),
    )

    private val DIONE_2_5 = arrayOf(
        doubleArrayOf(2.594271531157734E-05, 1.023301801126352E+00, 4.859897877785333E-01),
    )

    private val DIONE_2_6 = arrayOf(
        doubleArrayOf(6.245540270289302E-04, 1.994597716654372E+00, 2.295717646432711E+00),
    )

    private val DIONE_2_7 = arrayOf(
        doubleArrayOf(4.309530620212000E-06, 4.537996183502177E+00, -3.409307594421322E+00),
    )

    private val DIONE_2_8 = arrayOf(
        doubleArrayOf(2.669607656360495E-05, 2.108450170157240E+00, -4.188741415485552E-01),
    )

    private val DIONE_2_9 = arrayOf(
        doubleArrayOf(6.968783491136024E-06, 5.200588559833291E-02, -1.323738070875643E+00),
    )

    private val DIONE_2_10 = arrayOf(
        doubleArrayOf(3.304505798253070E-06, 4.278746908219012E+00, -2.228602000202733E+00),
    )

    private val DIONE_2_11 = arrayOf(
        doubleArrayOf(3.787380702785700E-06, 5.033271736497301E+00, 1.263128847810865E+00),
    )

    private val DIONE_2_12 = arrayOf(
        doubleArrayOf(2.859852996917539E-05, 1.788760449160644E+00, 2.305400491890175E-01),
    )

    private val DIONE_2_13 = arrayOf(
        doubleArrayOf(7.799304255363336E-06, 1.685841815413778E+00, -8.020487494328282E-01),
    )

    private val DIONE_2_14 = arrayOf(
        doubleArrayOf(2.969852804959130E-06, 4.724515835256708E+00, -1.834637548054674E+00),
    )

    private val DIONE_2_15 = arrayOf(
        doubleArrayOf(5.709737153001810E-06, 5.673408458612511E+00, -7.798450772541216E-05),
        doubleArrayOf(2.203368656279073E-03, 4.874775170004872E+00, 1.471391305053998E-03),
        doubleArrayOf(4.177951671700660E-06, 3.418533941089551E+00, -2.955758721579874E-03),
        doubleArrayOf(5.709763882914900E-06, 9.345492278074399E-01, 3.020767117833410E-03),
        doubleArrayOf(1.886080065709498E-05, 3.189421946098056E+00, 5.898541331687871E-03),
    )

    private val DIONE_2_16 = arrayOf(
        doubleArrayOf(2.618379490210864E-06, 1.426868473632360E+00, 3.458059983814795E-03),
    )

    private val DIONE_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), DIONE_2_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), DIONE_2_1),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 0, 0), DIONE_2_2),
        Term(intArrayOf(0, 0, 0, -1, 0, 2, 0, 0), DIONE_2_3),
        Term(intArrayOf(0, 0, 0, -1, 0, 3, 0, 0), DIONE_2_4),
        Term(intArrayOf(0, 0, 0, -1, 2, 0, 0, 0), DIONE_2_5),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 0, 0), DIONE_2_6),
        Term(intArrayOf(0, 0, 0, -2, 0, 3, 0, 0), DIONE_2_7),
        Term(intArrayOf(0, 0, 0, -2, 3, 0, 0, 0), DIONE_2_8),
        Term(intArrayOf(0, 0, 0, -3, 4, 0, 0, 0), DIONE_2_9),
        Term(intArrayOf(0, 0, 0, -4, 5, 0, 0, 0), DIONE_2_10),
        Term(intArrayOf(0, 0, -1, 2, 0, 0, 0, 0), DIONE_2_11),
        Term(intArrayOf(0, 0, -2, 3, 0, 0, 0, 0), DIONE_2_12),
        Term(intArrayOf(0, 0, -3, 4, 0, 0, 0, 0), DIONE_2_13),
        Term(intArrayOf(0, 0, -4, 5, 0, 0, 0, 0), DIONE_2_14),
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), DIONE_2_15),
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), DIONE_2_16),
    )

    private val DIONE_3_0 = arrayOf(
        doubleArrayOf(2.282739732511724E-05, 6.135349401370048E+00, -2.445240135601595E-05),
        doubleArrayOf(3.529922124449878E-05, 2.632866665182736E+00, -4.804041710792879E-04),
    )

    private val DIONE_3_1 = arrayOf(
        doubleArrayOf(1.655363859165119E-04, 1.556643393089241E+00, -1.471954902036816E-03),
    )

    private val DIONE_3_2 = arrayOf(
        doubleArrayOf(2.754688236937855E-05, 7.961878954263457E-01, -3.452416044929610E-03),
        doubleArrayOf(1.269228313121738E-05, 1.482534195440534E+00, -3.208623089724569E-03),
        doubleArrayOf(1.268342612853373E-05, 3.251434249001951E+00, -3.696209000134648E-03),
        doubleArrayOf(2.655183882413472E-06, 2.168880495454724E+00, -2.964830134519532E-03),
        doubleArrayOf(2.629764340505144E-06, 5.706680602577554E+00, -3.940001955339688E-03),
    )

    private val DIONE_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), DIONE_3_0),
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), DIONE_3_1),
        Term(intArrayOf(-1, 0, 2, 0, 0, 0, 0, 0), DIONE_3_2),
    )

    private val RHEA_0_0 = arrayOf(
        doubleArrayOf(1.460129617898124E-05, 4.426139084868009E+00, 9.968111509575888E-01),
    )

    private val RHEA_0_1 = arrayOf(
        doubleArrayOf(6.497476875291102E-05, 5.710685516146226E+00, 1.993622301915178E+00),
    )

    private val RHEA_0_2 = arrayOf(
        doubleArrayOf(8.448119776705509E-06, 3.455872944043167E+00, 1.599604195354539E+00),
    )

    private val RHEA_0_3 = arrayOf(
        doubleArrayOf(2.344587498942920E-05, 7.120466402448569E-01, 2.990433452872767E+00),
    )

    private val RHEA_0_4 = arrayOf(
        doubleArrayOf(4.000365153622274E-06, 4.740419375321384E+00, 2.596415346312128E+00),
    )

    private val RHEA_0_5 = arrayOf(
        doubleArrayOf(8.875319141540999E-06, 1.996593071523072E+00, 3.987244603830355E+00),
    )

    private val RHEA_0_6 = arrayOf(
        doubleArrayOf(3.452681160707960E-06, 3.281139502801291E+00, 4.984055754787943E+00),
    )

    private val RHEA_0_7 = arrayOf(
        doubleArrayOf(9.207121596242679E-06, 5.198036938148699E+00, 9.048639293270888E-01),
    )

    private val RHEA_0_8 = arrayOf(
        doubleArrayOf(4.621278522602440E-06, 4.112888569117812E+00, 1.809727858654177E+00),
    )

    private val RHEA_0_9 = arrayOf(
        doubleArrayOf(5.977877370825760E-06, 2.159362918305770E+00, 1.937452727948935E+00),
    )

    private val RHEA_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 1, -1, 0, 0), RHEA_0_0),
        Term(intArrayOf(0, 0, 0, 0, 2, -2, 0, 0), RHEA_0_1),
        Term(intArrayOf(0, 0, 0, 0, 2, -3, 0, 0), RHEA_0_2),
        Term(intArrayOf(0, 0, 0, 0, 3, -3, 0, 0), RHEA_0_3),
        Term(intArrayOf(0, 0, 0, 0, 3, -4, 0, 0), RHEA_0_4),
        Term(intArrayOf(0, 0, 0, 0, 4, -4, 0, 0), RHEA_0_5),
        Term(intArrayOf(0, 0, 0, 0, 5, -5, 0, 0), RHEA_0_6),
        Term(intArrayOf(0, 0, 0, 1, -1, 0, 0, 0), RHEA_0_7),
        Term(intArrayOf(0, 0, 0, 2, -2, 0, 0, 0), RHEA_0_8),
        Term(intArrayOf(0, 0, 1, 0, -1, 0, 0, 0), RHEA_0_9),
    )

    private val RHEA_1_0 = arrayOf(
        doubleArrayOf(4.982624562626507E-04, 4.765136030335644E+00, 5.271851040460129E-06),
        doubleArrayOf(3.475126795324123E-06, 5.905021737251245E+00, 1.054370208092026E-05),
        doubleArrayOf(4.462723299012653E-06, 4.222312533161678E+00, 1.898637155826853E-05),
        doubleArrayOf(3.255047679977468E-04, 3.303959081334276E+00, 2.445240135601595E-05),
        doubleArrayOf(1.772508422160692E-05, 5.196471838140832E+00, 4.891917479115332E-05),
        doubleArrayOf(9.471405209345812E-06, 7.450246191142529E-01, 1.035769927204739E-04),
        doubleArrayOf(1.830753059305353E-05, 2.440869213255669E-01, 4.559517697232720E-04),
        doubleArrayOf(6.147405445926997E-05, 3.666743633441163E+00, 4.804041710792879E-04),
        doubleArrayOf(4.693769190005650E-05, 4.409577074506501E+00, 5.839811637997618E-04),
        doubleArrayOf(6.390530214147782E-05, 2.864957498258305E+00, 1.167962327599524E-03),
        doubleArrayOf(8.699581713814392E-06, 4.133277437711403E+00, 1.751943491399285E-03),
    )

    private val RHEA_1_1 = arrayOf(
        doubleArrayOf(8.614402961042187E-06, 5.396405225692853E+00, 3.940181065606379E-01),
    )

    private val RHEA_1_2 = arrayOf(
        doubleArrayOf(5.229341727885530E-05, 4.426139084868009E+00, 9.968111509575888E-01),
    )

    private val RHEA_1_3 = arrayOf(
        doubleArrayOf(1.003942988473349E-05, 2.171326512764949E+00, 6.027930443969509E-01),
    )

    private val RHEA_1_4 = arrayOf(
        doubleArrayOf(3.825560625917272E-06, 6.199699253192279E+00, 2.087749378363131E-01),
    )

    private val RHEA_1_5 = arrayOf(
        doubleArrayOf(9.265555925994737E-05, 5.710685516146226E+00, 1.993622301915178E+00),
    )

    private val RHEA_1_6 = arrayOf(
        doubleArrayOf(1.367398797935688E-05, 3.455872944043167E+00, 1.599604195354539E+00),
    )

    private val RHEA_1_7 = arrayOf(
        doubleArrayOf(2.755180293512720E-05, 7.120466402448569E-01, 2.990433452872767E+00),
    )

    private val RHEA_1_8 = arrayOf(
        doubleArrayOf(5.024449380692616E-06, 4.740419375321384E+00, 2.596415346312128E+00),
    )

    private val RHEA_1_9 = arrayOf(
        doubleArrayOf(9.310491515462421E-06, 1.996593071523072E+00, 3.987244603830355E+00),
    )

    private val RHEA_1_10 = arrayOf(
        doubleArrayOf(2.065629848458831E-06, 6.024965806599599E+00, 3.593226497269717E+00),
    )

    private val RHEA_1_11 = arrayOf(
        doubleArrayOf(3.359851109236900E-06, 3.281139502801291E+00, 4.984055754787943E+00),
    )

    private val RHEA_1_12 = arrayOf(
        doubleArrayOf(1.266911496422784E-05, 2.056444284558907E+00, 9.048639293270888E-01),
    )

    private val RHEA_1_13 = arrayOf(
        doubleArrayOf(9.413368380851320E-06, 4.112888569117812E+00, 1.809727858654177E+00),
    )

    private val RHEA_1_14 = arrayOf(
        doubleArrayOf(4.460853124280260E-06, 6.169332853676718E+00, 2.714591787981265E+00),
    )

    private val RHEA_1_15 = arrayOf(
        doubleArrayOf(2.423482201792380E-06, 1.942591831056039E+00, 3.619455717308356E+00),
    )

    private val RHEA_1_16 = arrayOf(
        doubleArrayOf(3.028875527833520E-06, 5.300955571895564E+00, 1.937452727948935E+00),
    )

    private val RHEA_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), RHEA_1_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), RHEA_1_1),
        Term(intArrayOf(0, 0, 0, 0, 1, -1, 0, 0), RHEA_1_2),
        Term(intArrayOf(0, 0, 0, 0, 1, -2, 0, 0), RHEA_1_3),
        Term(intArrayOf(0, 0, 0, 0, 1, -3, 0, 0), RHEA_1_4),
        Term(intArrayOf(0, 0, 0, 0, 2, -2, 0, 0), RHEA_1_5),
        Term(intArrayOf(0, 0, 0, 0, 2, -3, 0, 0), RHEA_1_6),
        Term(intArrayOf(0, 0, 0, 0, 3, -3, 0, 0), RHEA_1_7),
        Term(intArrayOf(0, 0, 0, 0, 3, -4, 0, 0), RHEA_1_8),
        Term(intArrayOf(0, 0, 0, 0, 4, -4, 0, 0), RHEA_1_9),
        Term(intArrayOf(0, 0, 0, 0, 4, -5, 0, 0), RHEA_1_10),
        Term(intArrayOf(0, 0, 0, 0, 5, -5, 0, 0), RHEA_1_11),
        Term(intArrayOf(0, 0, 0, 1, -1, 0, 0, 0), RHEA_1_12),
        Term(intArrayOf(0, 0, 0, 2, -2, 0, 0, 0), RHEA_1_13),
        Term(intArrayOf(0, 0, 0, 3, -3, 0, 0, 0), RHEA_1_14),
        Term(intArrayOf(0, 0, 0, 4, -4, 0, 0, 0), RHEA_1_15),
        Term(intArrayOf(0, 0, 1, 0, -1, 0, 0, 0), RHEA_1_16),
    )

    private val RHEA_2_0 = arrayOf(
        doubleArrayOf(6.496268041362011E-06, 6.195159743999218E-01, -2.445958739557666E-05),
        doubleArrayOf(9.712611923476502E-04, 2.687921917028628E+00, 2.445958739557666E-05),
        doubleArrayOf(1.671852669066537E-04, 5.358703148577743E-02, 4.806275624808988E-04),
        doubleArrayOf(2.256764766375221E-06, 3.428726874313563E-01, 1.143502740203947E-03),
    )

    private val RHEA_2_1 = arrayOf(
        doubleArrayOf(3.248040932920730E-05, 1.795199654407042E+00, 3.940425661480334E-01),
    )

    private val RHEA_2_2 = arrayOf(
        doubleArrayOf(1.108384911297719E-04, 3.652245876718619E+00, -6.027685848095554E-01),
    )

    private val RHEA_2_3 = arrayOf(
        doubleArrayOf(3.382899984196108E-05, 5.907058448821680E+00, -2.087504782489175E-01),
    )

    private val RHEA_2_4 = arrayOf(
        doubleArrayOf(2.778461053446649E-06, 5.020278361984143E+00, 1.852676283117203E-01),
    )

    private val RHEA_2_5 = arrayOf(
        doubleArrayOf(3.116043009304567E-04, 6.221338739275052E+00, 1.390853717105622E+00),
    )

    private val RHEA_2_6 = arrayOf(
        doubleArrayOf(2.241664081352878E-05, 2.367699445440403E+00, -1.599579735767144E+00),
    )

    private val RHEA_2_7 = arrayOf(
        doubleArrayOf(4.424102595490495E-06, 4.622512017543462E+00, -1.205561629206506E+00),
    )

    private val RHEA_2_8 = arrayOf(
        doubleArrayOf(6.929884985440310E-06, 1.083153014162185E+00, -2.596390886724733E+00),
    )

    private val RHEA_2_9 = arrayOf(
        doubleArrayOf(5.779542899092500E-06, 2.507246294651899E+00, 3.384476019020799E+00),
    )

    private val RHEA_2_10 = arrayOf(
        doubleArrayOf(2.427569664020370E-06, 6.081791890063556E+00, -3.593202037682322E+00),
    )

    private val RHEA_2_11 = arrayOf(
        doubleArrayOf(2.492530233817800E-06, 3.791792725930115E+00, 4.381287169978387E+00),
    )

    private val RHEA_2_12 = arrayOf(
        doubleArrayOf(6.766246868422730E-06, 4.164894454716146E+00, 4.859897877785333E-01),
    )

    private val RHEA_2_13 = arrayOf(
        doubleArrayOf(3.851717115480820E-06, 1.994597716654372E+00, 2.295717646432711E+00),
    )

    private val RHEA_2_14 = arrayOf(
        doubleArrayOf(1.308501254327346E-05, 5.250042823747033E+00, -4.188741415485552E-01),
    )

    private val RHEA_2_15 = arrayOf(
        doubleArrayOf(3.467317924478190E-06, 3.193598539188126E+00, -1.323738070875643E+00),
    )

    private val RHEA_2_16 = arrayOf(
        doubleArrayOf(2.224849328232030E-06, 5.239109003991030E+00, 3.328306445054557E+00),
    )

    private val RHEA_2_17 = arrayOf(
        doubleArrayOf(7.641483476943351E-06, 1.733182516415079E+00, 1.471391305053998E-03),
    )

    private val RHEA_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), RHEA_2_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), RHEA_2_1),
        Term(intArrayOf(0, 0, 0, 0, -1, 2, 0, 0), RHEA_2_2),
        Term(intArrayOf(0, 0, 0, 0, -1, 3, 0, 0), RHEA_2_3),
        Term(intArrayOf(0, 0, 0, 0, -1, 4, 0, 0), RHEA_2_4),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 0, 0), RHEA_2_5),
        Term(intArrayOf(0, 0, 0, 0, -2, 3, 0, 0), RHEA_2_6),
        Term(intArrayOf(0, 0, 0, 0, -2, 4, 0, 0), RHEA_2_7),
        Term(intArrayOf(0, 0, 0, 0, -3, 4, 0, 0), RHEA_2_8),
        Term(intArrayOf(0, 0, 0, 0, 3, -2, 0, 0), RHEA_2_9),
        Term(intArrayOf(0, 0, 0, 0, -4, 5, 0, 0), RHEA_2_10),
        Term(intArrayOf(0, 0, 0, 0, 4, -3, 0, 0), RHEA_2_11),
        Term(intArrayOf(0, 0, 0, -1, 2, 0, 0, 0), RHEA_2_12),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 0, 0), RHEA_2_13),
        Term(intArrayOf(0, 0, 0, -2, 3, 0, 0, 0), RHEA_2_14),
        Term(intArrayOf(0, 0, 0, -3, 4, 0, 0, 0), RHEA_2_15),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, 0, 0), RHEA_2_16),
        Term(intArrayOf(0, -1, 0, 2, 0, 0, 0, 0), RHEA_2_17),
    )

    private val RHEA_3_0 = arrayOf(
        doubleArrayOf(2.970532433280590E-03, 2.626878648804450E+00, -4.804041710792879E-04),
        doubleArrayOf(1.787509410723081E-04, 6.204551285275548E+00, -2.445240135601595E-05),
        doubleArrayOf(2.723114438070935E-05, 6.085981723978948E+00, 1.167962327599524E-03),
        doubleArrayOf(2.414440346500943E-05, 5.096853638619743E+00, -5.839811637997618E-04),
        doubleArrayOf(9.525093659923903E-06, 5.044236237492881E+00, -5.271851040460129E-06),
        doubleArrayOf(3.807917651740949E-06, 1.070489829428804E+00, 1.751943491399285E-03),
        doubleArrayOf(3.291355004303486E-06, 4.446713203128226E+00, 5.839811637997618E-04),
        doubleArrayOf(2.944165615658908E-06, 3.500661322372421E-01, -1.167962327599524E-03),
    )

    private val RHEA_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), RHEA_3_0),
    )

    private val TITAN_0_0 = arrayOf(
        doubleArrayOf(5.193372353628770E-06, 5.396405225692853E+00, 3.940181065606379E-01),
    )

    private val TITAN_0_1 = arrayOf(
        doubleArrayOf(4.923588320704660E-06, 5.786945864275197E-01, 7.869171700036950E-01),
    )

    private val TITAN_0_2 = arrayOf(
        doubleArrayOf(2.514051816697911E-05, 4.426139084868009E+00, 9.968111509575888E-01),
    )

    private val TITAN_0_3 = arrayOf(
        doubleArrayOf(1.234070873751902E-05, 1.993980622473295E-01, 1.901675080284677E+00),
    )

    private val TITAN_0_4 = arrayOf(
        doubleArrayOf(7.428005944912294E-06, 3.443909349583986E+00, 2.934263878906524E+00),
    )

    private val TITAN_0_5 = arrayOf(
        doubleArrayOf(1.514267591217036E-06, 5.287757340750888E+00, 4.191494185385700E+00),
    )

    private val TITAN_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), TITAN_0_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), TITAN_0_1),
        Term(intArrayOf(0, 0, 0, 0, 1, -1, 0, 0), TITAN_0_2),
        Term(intArrayOf(0, 0, 0, 1, 0, -1, 0, 0), TITAN_0_3),
        Term(intArrayOf(0, 0, 1, 0, 0, -1, 0, 0), TITAN_0_4),
        Term(intArrayOf(0, 1, 0, 0, 0, -1, 0, 0), TITAN_0_5),
    )

    private val TITAN_1_0 = arrayOf(
        doubleArrayOf(1.489184840960493E-03, 4.482905304402765E+00, 5.271851040460129E-06),
        doubleArrayOf(9.861735003619475E-06, 5.820876772324451E+00, 1.054370208092026E-05),
        doubleArrayOf(3.205222547434483E-05, 2.421050292728566E+00, 1.880354218803814E-05),
        doubleArrayOf(6.277976937099409E-04, 1.641703069309713E-01, 2.445240135601595E-05),
        doubleArrayOf(2.782832942434251E-05, 2.085876834041031E+00, 4.891917479115332E-05),
        doubleArrayOf(3.898032222540717E-06, 2.691928895904259E+00, 2.825103078658793E-04),
        doubleArrayOf(3.675185983863103E-06, 3.132180625569890E+00, 3.017286310072802E-04),
        doubleArrayOf(6.328208168488662E-06, 1.099595805258099E+00, 5.652783908051565E-04),
        doubleArrayOf(1.839936992682087E-04, 4.432539236156053E+00, 5.839811637997618E-04),
        doubleArrayOf(1.012886232301891E-06, 3.824011892951753E+00, 6.026839367943671E-04),
        doubleArrayOf(1.807246533364200E-06, 5.897892147256107E+00, 8.662336965922434E-04),
        doubleArrayOf(3.340295455803047E-06, 3.949193776134189E+00, 1.119043152808370E-03),
        doubleArrayOf(2.671699222038788E-06, 1.780723999304769E-02, 1.149259554604918E-03),
        doubleArrayOf(2.064532509871716E-04, 2.875895084979086E+00, 1.167962327599524E-03),
        doubleArrayOf(2.919923956899900E-06, 2.618006049618430E+00, 1.186665100594129E-03),
        doubleArrayOf(3.918483314907732E-06, 3.102869921028134E+00, 1.192414728955539E-03),
        doubleArrayOf(2.910646188137447E-05, 4.133511734209461E+00, 1.751943491399285E-03),
        doubleArrayOf(3.080473554041300E-06, 5.390964128071579E+00, 2.335924655199047E-03),
    )

    private val TITAN_1_1 = arrayOf(
        doubleArrayOf(2.177885623014171E-06, 5.338524981350306E+00, 1.937456015378634E-03),
        doubleArrayOf(2.229788591597716E-06, 3.516065572948062E+00, 1.942727866419093E-03),
        doubleArrayOf(3.240885104844179E-06, 1.456814599107305E+00, 1.956509193942217E-03),
        doubleArrayOf(3.317890554439667E-06, 5.917531821903471E+00, 1.961781044982677E-03),
        doubleArrayOf(8.515242483441068E-07, 4.094907555655027E+00, 1.967052896023137E-03),
        doubleArrayOf(1.583596726276704E-06, 1.857323310303026E+00, 1.967322011606203E-03),
        doubleArrayOf(3.183322410669546E-06, 3.483641352342939E-02, 1.972593862646663E-03),
        doubleArrayOf(2.439949469786914E-06, 4.495498981484839E+00, 1.977865713687124E-03),
        doubleArrayOf(8.331051796244125E-07, 2.672845480380433E+00, 1.983137564727584E-03),
    )

    private val TITAN_1_2 = arrayOf(
        doubleArrayOf(9.325252754097003E-07, 1.465474667914254E+00, 3.928990634430571E-01),
        doubleArrayOf(6.059629840959338E-06, 2.254812572103060E+00, 3.940181065606379E-01),
    )

    private val TITAN_1_3 = arrayOf(
        doubleArrayOf(1.119995323971813E-06, 5.646772130696905E+00, 7.863331888575091E-01),
        doubleArrayOf(5.747511089543420E-06, 5.786945864275197E-01, 7.869171700036950E-01),
    )

    private val TITAN_1_4 = arrayOf(
        doubleArrayOf(1.208825245395646E-05, 1.284546431278216E+00, 9.968111509575888E-01),
    )

    private val TITAN_1_5 = arrayOf(
        doubleArrayOf(9.999688627568780E-07, 2.569092862556433E+00, 1.993622301915178E+00),
    )

    private val TITAN_1_6 = arrayOf(
        doubleArrayOf(6.461479160208660E-06, 3.340990715837123E+00, 1.901675080284677E+00),
    )

    private val TITAN_1_7 = arrayOf(
        doubleArrayOf(4.165211007064940E-06, 3.023166959941940E-01, 2.934263878906524E+00),
    )

    private val TITAN_1_8 = arrayOf(
        doubleArrayOf(8.903963476562940E-07, 2.146164687161095E+00, 4.191494185385700E+00),
    )

    private val TITAN_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TITAN_1_0),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 5, 0), TITAN_1_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), TITAN_1_2),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), TITAN_1_3),
        Term(intArrayOf(0, 0, 0, 0, 1, -1, 0, 0), TITAN_1_4),
        Term(intArrayOf(0, 0, 0, 0, 2, -2, 0, 0), TITAN_1_5),
        Term(intArrayOf(0, 0, 0, 1, 0, -1, 0, 0), TITAN_1_6),
        Term(intArrayOf(0, 0, 1, 0, 0, -1, 0, 0), TITAN_1_7),
        Term(intArrayOf(0, 1, 0, 0, 0, -1, 0, 0), TITAN_1_8),
    )

    private val TITAN_2_0 = arrayOf(
        doubleArrayOf(2.030439241690526E-06, 3.465392760946876E+00, -5.406408831993331E-06),
        doubleArrayOf(1.720731211496869E-05, 3.423177098527374E+00, 5.406408831993331E-06),
        doubleArrayOf(2.297673200703965E-06, 4.851969044359338E+00, -1.067825987245346E-05),
        doubleArrayOf(5.578526024329263E-06, 4.763368404218852E+00, 1.067825987245346E-05),
        doubleArrayOf(2.429402127836536E-05, 4.497027349870592E+00, 1.918773635511653E-05),
        doubleArrayOf(1.921261556124706E-04, 6.049785198226078E-01, -2.445958739557666E-05),
        doubleArrayOf(2.892653650392732E-02, 2.687601928257754E+00, 2.445958739557666E-05),
        doubleArrayOf(5.704537193893060E-06, 2.441938711199521E+00, -2.973143843603680E-05),
        doubleArrayOf(2.391668026442592E-05, 4.004671318127588E+00, 2.973143843603680E-05),
        doubleArrayOf(1.273579323673662E-06, 4.271696076744320E+00, -3.500328947649692E-05),
        doubleArrayOf(2.219325143437113E-06, 3.927500699309107E-01, -4.891198875159261E-05),
        doubleArrayOf(1.391334470388962E-06, 3.792208194559931E+00, 2.777039784926735E-04),
        doubleArrayOf(1.840440310193102E-06, 4.554834665120421E+00, -5.595215764041851E-04),
        doubleArrayOf(6.349090434417589E-06, 5.278059950506263E+00, 5.595215764041851E-04),
        doubleArrayOf(1.359141741251292E-06, 6.222686370138073E+00, 5.839811637997618E-04),
        doubleArrayOf(1.313668933428861E-06, 5.635555579481489E+00, -6.084407511953385E-04),
        doubleArrayOf(1.818913752278572E-06, 3.943058897661665E+00, 6.084407511953385E-04),
        doubleArrayOf(1.054968278903017E-06, 6.106129826283189E-01, 1.124788312095915E-03),
        doubleArrayOf(2.599144808715373E-06, 6.100137782540312E+00, -1.143502740203947E-03),
        doubleArrayOf(7.446564554937326E-05, 3.484557927409254E+00, 1.143502740203947E-03),
        doubleArrayOf(1.030286270548740E-06, 3.243359475417337E+00, 1.162179719401369E-03),
        doubleArrayOf(2.100942202805606E-06, 2.406466000093305E+00, 1.192421914995100E-03),
        doubleArrayOf(9.603481577558545E-06, 4.751548744302498E+00, 1.727483904003709E-03),
        doubleArrayOf(1.071090644258225E-06, 6.019617650063695E+00, 2.311465067803471E-03),
    )

    private val TITAN_2_1 = arrayOf(
        doubleArrayOf(9.612769517722884E-07, 2.431612830889724E+00, -3.922906227094757E-01),
        doubleArrayOf(4.930907396569310E-06, 1.216505067979523E+00, -3.928746038556616E-01),
    )

    private val TITAN_2_2 = arrayOf(
        doubleArrayOf(6.687871156591785E-05, 4.936792307996836E+00, 3.940425661480334E-01),
    )

    private val TITAN_2_3 = arrayOf(
        doubleArrayOf(2.596686176814385E-06, 9.084195729203095E-01, 7.880606727086713E-01),
    )

    private val TITAN_2_4 = arrayOf(
        doubleArrayOf(4.938696934118670E-06, 3.652245876718619E+00, -6.027685848095554E-01),
    )

    private val TITAN_2_5 = arrayOf(
        doubleArrayOf(1.010084616284730E-05, 6.221338739275052E+00, 1.390853717105622E+00),
    )

    private val TITAN_2_6 = arrayOf(
        doubleArrayOf(2.338446911029510E-06, 1.595801592159713E+00, -1.507632514136644E+00),
    )

    private val TITAN_2_7 = arrayOf(
        doubleArrayOf(5.269296403563900E-06, 1.994597716654372E+00, 2.295717646432711E+00),
    )

    private val TITAN_2_8 = arrayOf(
        doubleArrayOf(1.363323740924180E-06, 4.634475612002642E+00, -2.540221312758490E+00),
    )

    private val TITAN_2_9 = arrayOf(
        doubleArrayOf(3.315192749055180E-06, 5.239109003991030E+00, 3.328306445054557E+00),
    )

    private val TITAN_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TITAN_2_0),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 0, 0), TITAN_2_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), TITAN_2_2),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), TITAN_2_3),
        Term(intArrayOf(0, 0, 0, 0, -1, 2, 0, 0), TITAN_2_4),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 0, 0), TITAN_2_5),
        Term(intArrayOf(0, 0, 0, -1, 0, 2, 0, 0), TITAN_2_6),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 0, 0), TITAN_2_7),
        Term(intArrayOf(0, 0, -1, 0, 0, 2, 0, 0), TITAN_2_8),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, 0, 0), TITAN_2_9),
    )

    private val TITAN_3_0 = arrayOf(
        doubleArrayOf(2.789942947721349E-03, 6.204694901726296E+00, -2.445240135601595E-05),
        doubleArrayOf(1.312363309291625E-04, 5.044269214427242E+00, -5.271851040460129E-06),
        doubleArrayOf(1.125670790406430E-04, 6.084205141557698E+00, 1.167962327599524E-03),
        doubleArrayOf(1.916668518784865E-05, 5.094974746907165E+00, -5.839811637997618E-04),
        doubleArrayOf(1.497943297409488E-05, 1.070503341744811E+00, 1.751943491399285E-03),
        doubleArrayOf(1.144622908335464E-05, 4.298020835227772E+00, 5.839811637997618E-04),
        doubleArrayOf(1.105889373330841E-05, 3.817225181469991E+00, -1.870277299460525E-05),
        doubleArrayOf(9.469793088277916E-06, 3.725645429506557E+00, -1.054370208092026E-05),
        doubleArrayOf(6.878606841089768E-06, 4.544829611068419E+00, 5.271851040460129E-06),
        doubleArrayOf(6.072279735075281E-06, 3.088021134790829E-01, -1.167962327599524E-03),
        doubleArrayOf(3.672628251015072E-06, 5.291779482141999E+00, 4.891917479115332E-05),
        doubleArrayOf(3.001306766151942E-06, 2.631624338898295E-01, 2.445240135601595E-05),
        doubleArrayOf(2.508038681269874E-06, 5.838193085927095E+00, -4.804041710792879E-04),
        doubleArrayOf(2.453965972048404E-06, 4.844097342411929E+00, -2.972425239647608E-05),
        doubleArrayOf(1.777443612458054E-06, 1.373143790052677E-01, 3.017286310072802E-04),
        doubleArrayOf(1.638627359387858E-06, 2.338023546843110E+00, 2.335924655199047E-03),
        doubleArrayOf(1.619813827555800E-06, 3.201683403167966E+00, 1.149259554604918E-03),
        doubleArrayOf(1.585471208377748E-06, 5.833093965190200E+00, 1.186665100594129E-03),
        doubleArrayOf(1.518975368269692E-06, 3.329608496168708E+00, 1.870277299460525E-05),
        doubleArrayOf(1.151714556320695E-06, 2.625783233864042E-02, 1.192414728955539E-03),
        doubleArrayOf(8.830894877995539E-07, 5.372467623946739E+00, -1.751943491399285E-03),
    )

    private val TITAN_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), TITAN_3_0),
    )

    private val IAPETUS_0_0 = arrayOf(
        doubleArrayOf(2.055029475492061E-06, 2.502670659577745E+00, 7.745544060220222E-02),
        doubleArrayOf(1.017421707635384E-05, -2.565419768024297E+00, 7.803942174838812E-02),
        doubleArrayOf(6.585278743116177E-07, 1.895225923201359E+00, 7.804469359942858E-02),
        doubleArrayOf(1.178034971556374E-05, 3.767316688774966E+00, 7.917751804453235E-02),
        doubleArrayOf(2.109797808204992E-05, 6.169137529926802E+00, 7.919657122309593E-02),
        doubleArrayOf(5.616037270279334E-07, -2.864068502197524E+00, 7.920738404075992E-02),
        doubleArrayOf(1.210381264018071E-06, -1.536414322676488E+00, 7.921265589180036E-02),
    )

    private val IAPETUS_0_1 = arrayOf(
        doubleArrayOf(3.248586724047530E-06, 1.173515119422923E+00, 1.560680306791122E-01),
        doubleArrayOf(2.327804697388999E-05, 2.388610005279730E+00, 1.566520118252981E-01),
        doubleArrayOf(1.506081447204865E-06, 5.660579539606869E-01, 1.566572836763386E-01),
        doubleArrayOf(2.219668063709623E-06, 2.284631982621601E+00, 1.572307211204436E-01),
        doubleArrayOf(1.177813487687222E-04, -2.679467329715666E+00, 1.572359929714840E-01),
        doubleArrayOf(5.121363233915475E-06, 1.781122928927975E+00, 1.572412648225245E-01),
        doubleArrayOf(3.258205784517132E-06, 1.657988726564947E+00, 1.578199741176699E-01),
        doubleArrayOf(6.406895152069824E-07, 3.418104295086557E+00, 1.578252459687104E-01),
        doubleArrayOf(7.233685566797959E-07, 5.117268855773154E-01, 1.583740892676283E-01),
        doubleArrayOf(6.553814036784504E-07, 2.913580261178442E+00, 1.583931424461918E-01),
        doubleArrayOf(5.886085615859691E-06, -2.969003559963930E+00, 1.584039552638558E-01),
        doubleArrayOf(3.846693748791777E-07, 1.489841855036775E+00, 1.584092271148963E-01),
        doubleArrayOf(1.849113934605446E-06, 5.952077755114058E+00, 1.584144989659368E-01),
        doubleArrayOf(1.523920218717224E-07, 1.386868830832936E+00, 1.589879364100418E-01),
        doubleArrayOf(6.359828738920669E-07, 5.848319820906959E+00, 1.589932082610822E-01),
        doubleArrayOf(1.105572785393772E-07, -1.164606940957312E-01, 1.595719175562276E-01),
        doubleArrayOf(6.957733951844473E-07, 1.201970451738299E+00, 1.595771894072681E-01),
    )

    private val IAPETUS_0_2 = arrayOf(
        doubleArrayOf(6.748302380368017E-07, 5.416167758678300E+00, 2.358485830483941E-01),
        doubleArrayOf(3.458158840283551E-06, 3.480902144089144E-01, 2.364325641945799E-01),
    )

    private val IAPETUS_0_3 = arrayOf(
        doubleArrayOf(4.946872838108632E-07, 6.077953687111643E+00, 1.918402836815050E-03),
        doubleArrayOf(5.065090050950890E-07, 4.255501070028474E+00, 1.923674687855510E-03),
        doubleArrayOf(5.354701955778437E-07, 1.796193334454873E+00, 1.926643197714647E-03),
        doubleArrayOf(2.368323695654563E-06, 2.196618138085234E+00, 1.937456015378634E-03),
        doubleArrayOf(2.424687512275475E-06, 3.741587517563335E-01, 1.942727866419093E-03),
        doubleArrayOf(6.223980017737441E-07, 4.834746214664873E+00, 1.947999717459554E-03),
        doubleArrayOf(3.515734994495499E-06, 4.598407354081068E+00, 1.956509193942217E-03),
        doubleArrayOf(3.599154632304914E-06, 2.775939218332986E+00, 1.961781044982677E-03),
        doubleArrayOf(9.236794241568811E-07, 9.533149020652341E-01, 1.967052896023137E-03),
        doubleArrayOf(1.730312424825915E-06, 4.998916170228774E+00, 1.967322011606203E-03),
        doubleArrayOf(3.478141777876746E-06, 3.176429222382267E+00, 1.972593862646663E-03),
        doubleArrayOf(2.665836454376912E-06, 1.353906429920962E+00, 1.977865713687124E-03),
        doubleArrayOf(9.101734358180643E-07, 5.814438133970226E+00, 1.983137564727584E-03),
    )

    private val IAPETUS_0_4 = arrayOf(
        doubleArrayOf(1.173808537373678E-05, 3.099779317070623E-01, 3.148353166650650E-01),
        doubleArrayOf(1.422582505896866E-03, 1.629074651441286E+00, 3.148405885161054E-01),
        doubleArrayOf(1.282760621734726E-05, 2.948174134620363E+00, 3.148458603671461E-01),
        doubleArrayOf(4.431013807987148E-07, 5.376468530544894E-01, 3.148597690664211E-01),
        doubleArrayOf(8.613770793559659E-07, 4.998444131476766E+00, 3.148650409174615E-01),
    )

    private val IAPETUS_0_5 = arrayOf(
        doubleArrayOf(1.529823976752019E-05, 4.943058008729855E+00, 4.732445437799614E-01),
        doubleArrayOf(1.638556362699022E-05, 3.120590560712127E+00, 4.732498156310018E-01),
        doubleArrayOf(4.393561481041129E-06, 1.297974022507647E+00, 4.732550874820422E-01),
        doubleArrayOf(5.965719593399348E-07, 5.170692394242208E+00, 4.732689961813175E-01),
    )

    private val IAPETUS_0_6 = arrayOf(
        doubleArrayOf(1.205192931267955E-06, 2.446685865249762E-02, 2.356279326243051E-01),
        doubleArrayOf(1.539010606612464E-06, 1.349751065508437E+00, 2.356332044753456E-01),
        doubleArrayOf(5.125234375859194E-07, 6.347454751688458E-01, 2.356386108841776E-01),
        doubleArrayOf(4.815890873838023E-05, 4.857380043863779E+00, 2.356440172930096E-01),
        doubleArrayOf(1.049405056557275E-06, 6.205868207753926E+00, 2.356492891440501E-01),
        doubleArrayOf(2.261000099791683E-06, 4.144773749632801E+00, 2.356630704715732E-01),
        doubleArrayOf(6.346855400965112E-07, 4.208242392372787E+00, 2.368011668029451E-01),
    )

    private val IAPETUS_0_7 = arrayOf(
        doubleArrayOf(4.677155865229010E-07, 1.960182176247418E+00, 1.564260895501687E-01),
        doubleArrayOf(1.822472901105999E-06, 1.375578599796670E-01, 1.564313614012092E-01),
        doubleArrayOf(1.780232418309521E-06, 4.598275031411171E+00, 1.564366332522496E-01),
        doubleArrayOf(1.648047287035982E-06, 1.857191416947010E+00, 1.564474460699136E-01),
        doubleArrayOf(7.820099241529981E-07, 1.117332070337260E+00, 1.564664992484772E-01),
    )

    private val IAPETUS_0_8 = arrayOf(
        doubleArrayOf(7.283955691107278E-07, 3.393198290814080E+00, 7.723479017811326E-02),
        doubleArrayOf(7.115131339933769E-07, 1.570730155065997E+00, 7.724006202915373E-02),
    )

    private val IAPETUS_0_9 = arrayOf(
        doubleArrayOf(3.791140650423246E-05, 3.877892946988424E+00, 7.088586950767433E-01),
    )

    private val IAPETUS_0_10 = arrayOf(
        doubleArrayOf(5.570980365205931E-07, 9.146867890556700E-01, 8.672626503405993E-01),
        doubleArrayOf(5.703165858434648E-07, 5.375403960487173E+00, 8.672679221916397E-01),
    )

    private val IAPETUS_0_11 = arrayOf(
        doubleArrayOf(1.199902334914149E-06, 8.563423202419649E-01, 6.296621238536474E-01),
        doubleArrayOf(1.557003369506093E-05, 3.258149248363778E+00, 6.296811770322111E-01),
    )

    private val IAPETUS_0_12 = arrayOf(
        doubleArrayOf(2.861462370993798E-06, 2.306043720186055E-01, 5.504846058091149E-01),
        doubleArrayOf(1.182264841666031E-06, 2.632429282009304E+00, 5.505036589876786E-01),
    )

    private val IAPETUS_0_13 = arrayOf(
        doubleArrayOf(1.228324287730070E-06, 6.138699763037207E+00, 1.102876801637381E+00),
    )

    private val IAPETUS_0_14 = arrayOf(
        doubleArrayOf(4.329731043745186E-06, 1.745631218955876E+00, 9.445217655483165E-01),
    )

    private val IAPETUS_0_15 = arrayOf(
        doubleArrayOf(9.898839492003675E-07, 5.001271649790288E+00, 8.653251943252205E-01),
    )

    private val IAPETUS_0_16 = arrayOf(
        doubleArrayOf(1.430384079869284E-06, 2.331131895479707E-01, 1.259362354064422E+00),
    )

    private val IAPETUS_0_17 = arrayOf(
        doubleArrayOf(4.421327735883800E-07, 5.003780467319657E+00, 1.574202942580528E+00),
    )

    private val IAPETUS_0_18 = arrayOf(
        doubleArrayOf(3.479701758905445E-05, 2.913621055460106E+00, 1.311651739473694E+00),
    )

    private val IAPETUS_0_19 = arrayOf(
        doubleArrayOf(5.667000670514980E-07, 6.227605955410412E+00, 1.470055694737550E+00),
        doubleArrayOf(5.801464486513693E-07, 4.405137819662329E+00, 1.470060966588590E+00),
    )

    private val IAPETUS_0_20 = arrayOf(
        doubleArrayOf(1.080732052994442E-06, 6.169261486294519E+00, 1.232455168250598E+00),
    )

    private val IAPETUS_0_21 = arrayOf(
        doubleArrayOf(1.870537893668915E-05, 4.970065340019012E+00, 2.216515668800783E+00),
    )

    private val IAPETUS_0_22 = arrayOf(
        doubleArrayOf(5.433130776641635E-07, 1.942520463673839E+00, 2.137319097577687E+00),
    )

    private val IAPETUS_0_23 = arrayOf(
        doubleArrayOf(1.129835111020686E-05, 1.931391320176082E+00, 3.249104467422628E+00),
    )

    private val IAPETUS_0_24 = arrayOf(
        doubleArrayOf(8.163925621658264E-07, 3.775239311342985E+00, 4.506334773901807E+00),
    )

    private val IAPETUS_0_25 = arrayOf(
        doubleArrayOf(8.563952066234920E-07, 3.157716115970889E+00, 6.587859751149659E+00),
    )

    private val IAPETUS_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 1, 0), IAPETUS_0_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 2, 0), IAPETUS_0_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 3, 0), IAPETUS_0_2),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 5, 0), IAPETUS_0_3),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -1, 0), IAPETUS_0_4),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 1, 0), IAPETUS_0_5),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -2, 0), IAPETUS_0_6),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -3, 0), IAPETUS_0_7),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -4, 0), IAPETUS_0_8),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -1, 0), IAPETUS_0_9),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 1, 0), IAPETUS_0_10),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -2, 0), IAPETUS_0_11),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -3, 0), IAPETUS_0_12),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -1, 0), IAPETUS_0_13),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -3, 0), IAPETUS_0_14),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -4, 0), IAPETUS_0_15),
        Term(intArrayOf(0, 0, 0, 0, 0, 4, -4, 0), IAPETUS_0_16),
        Term(intArrayOf(0, 0, 0, 0, 0, 5, -5, 0), IAPETUS_0_17),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, -1, 0), IAPETUS_0_18),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 1, 0), IAPETUS_0_19),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, -2, 0), IAPETUS_0_20),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, -1, 0), IAPETUS_0_21),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, -2, 0), IAPETUS_0_22),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, -1, 0), IAPETUS_0_23),
        Term(intArrayOf(0, 1, 0, 0, 0, 0, -1, 0), IAPETUS_0_24),
        Term(intArrayOf(1, 0, 0, 0, 0, 0, -1, 0), IAPETUS_0_25),
    )

    private val IAPETUS_1_0 = arrayOf(
        doubleArrayOf(1.928386916598716E-01, 1.316743285171985E+00, 5.271851040460129E-06),
        doubleArrayOf(1.197700127979115E-03, 2.905816296579413E+00, 1.054370208092026E-05),
        doubleArrayOf(1.785444267015284E-04, 3.897159948941672E+00, 1.894532180866185E-05),
        doubleArrayOf(7.466410913376219E-04, 3.411600296951643E+00, 2.445240135601595E-05),
        doubleArrayOf(4.076652529480639E-05, 3.776409831511328E+00, 2.966566038404764E-05),
        doubleArrayOf(3.620254646473473E-05, 6.177614480595389E-01, 3.510481004211992E-05),
        doubleArrayOf(2.023635272434820E-05, 5.342652755830533E+00, 4.891917479115332E-05),
        doubleArrayOf(2.148685811756090E-05, 3.324109814821024E+00, 5.483417106242491E-05),
        doubleArrayOf(2.377227624569038E-05, 3.239254695447169E+00, 2.825103078658793E-04),
        doubleArrayOf(4.028991254502335E-05, 1.095989272623461E+00, 5.652783908051565E-04),
        doubleArrayOf(5.283085924212965E-05, 6.218635238921890E+00, 5.787093127593016E-04),
        doubleArrayOf(1.125807495942191E-03, 4.414124516924016E+00, 5.839811637997618E-04),
        doubleArrayOf(3.586013723690129E-05, 2.436062381089369E+00, 5.892530148402220E-04),
        doubleArrayOf(7.459887995646048E-06, 4.087393199562610E+00, 6.026839367943671E-04),
        doubleArrayOf(1.314054216207817E-05, 5.873663787672587E+00, 8.662336965922434E-04),
        doubleArrayOf(1.603086203623188E-05, 2.485710326866144E+00, 1.157149509935537E-03),
        doubleArrayOf(2.400461005136443E-04, 2.926349976058209E+00, 1.167962327599524E-03),
        doubleArrayOf(3.004295676379663E-04, 4.182760326808072E+00, 1.173234178639984E-03),
        doubleArrayOf(7.393695567545098E-05, 5.503253064658139E+00, 1.178506029680444E-03),
        doubleArrayOf(3.490441837523331E-05, 4.159104921593594E+00, 1.751943491399285E-03),
        doubleArrayOf(3.811816549252109E-05, 5.449835843337831E+00, 1.757215342439746E-03),
        doubleArrayOf(9.878270828624305E-06, 4.692950541308903E-01, 1.762487193480206E-03),
    )

    private val IAPETUS_1_1 = arrayOf(
        doubleArrayOf(6.143353968708967E-07, 1.287562896667545E+00, 7.687145945601632E-02),
        doubleArrayOf(4.523919915777730E-06, 2.502659168377380E+00, 7.745544060220222E-02),
        doubleArrayOf(3.714286018866130E-07, -1.141689927113436E+00, 7.802333707968367E-02),
        doubleArrayOf(5.608496749491884E-07, 1.852697585028197E-01, 7.802860893072413E-02),
        doubleArrayOf(3.504383346317222E-07, 2.398679759866219E+00, 7.803414989734765E-02),
        doubleArrayOf(2.295038701669439E-05, -2.593118006894055E+00, 7.803942174838812E-02),
        doubleArrayOf(6.404009664458889E-07, 1.893523997409873E+00, 7.804469359942858E-02),
        doubleArrayOf(8.091467686328637E-07, 6.119791853597793E+00, 7.805847492695170E-02),
        doubleArrayOf(4.190958483159967E-07, 1.812437113434972E+00, 7.861259007691003E-02),
        doubleArrayOf(6.286284677985618E-07, 1.791293531808356E+00, 7.862340289457402E-02),
        doubleArrayOf(3.164931584142412E-07, 5.589825798207451E+00, 7.917224619349188E-02),
        doubleArrayOf(4.353564226390001E-06, 6.257381704689877E-01, 7.917751804453235E-02),
        doubleArrayOf(3.164896582547253E-07, 1.944834709464376E+00, 7.918278989557279E-02),
        doubleArrayOf(1.404022491486175E-07, -1.433272645194262E+00, 7.919129937205547E-02),
        doubleArrayOf(3.794706462471952E-06, 3.027544876345173E+00, 7.919657122309593E-02),
        doubleArrayOf(3.059127278557355E-07, 3.041311214209734E+00, 7.919657125832364E-02),
        doubleArrayOf(1.407650236154023E-07, 1.204832936646125E+00, 7.920184307413637E-02),
        doubleArrayOf(8.733847339265118E-07, -2.854848402830646E+00, 7.920738404075992E-02),
        doubleArrayOf(6.190372619937466E-07, -1.535786959595905E+00, 7.921265589180036E-02),
        doubleArrayOf(3.845413058184980E-07, 5.829842020175415E+00, 7.922643721932350E-02),
        doubleArrayOf(3.936684188721603E-07, 4.007371902635274E+00, 7.923170907036394E-02),
        doubleArrayOf(4.101604037784085E-07, 4.242652639255375E+00, 7.978055236928183E-02),
        doubleArrayOf(4.086365824538882E-08, -4.056548501884276E-01, 8.036453351546773E-02),
        doubleArrayOf(4.231066732027226E-07, 4.057026289098603E+00, 8.036980536650817E-02),
    )

    private val IAPETUS_1_2 = arrayOf(
        doubleArrayOf(4.416345379733718E-07, 6.241592663692312E+00, 1.554840495329263E-01),
        doubleArrayOf(3.837882772948628E-06, 1.173503571344250E+00, 1.560680306791122E-01),
        doubleArrayOf(4.360169268782757E-07, 1.069524219711398E+00, 1.566467399742577E-01),
        doubleArrayOf(2.721985372704609E-05, 2.388610893433953E+00, 1.566520118252981E-01),
        doubleArrayOf(1.203630670831745E-06, 5.660322567848149E-01, 1.566572836763386E-01),
        doubleArrayOf(2.227388462533576E-06, 2.284631982621601E+00, 1.572307211204436E-01),
        doubleArrayOf(1.378064515172981E-04, -2.679466414745420E+00, 1.572359929714840E-01),
        doubleArrayOf(5.768343947193972E-06, 1.781127439794527E+00, 1.572412648225245E-01),
        doubleArrayOf(4.253374565827929E-06, 1.704388717616716E+00, 1.578199741176699E-01),
        doubleArrayOf(3.291231434148502E-07, -2.864966874148501E+00, 1.578252459687104E-01),
        doubleArrayOf(4.141402947439892E-07, -2.965677063355761E+00, 1.584039552638558E-01),
        doubleArrayOf(6.393990743929588E-06, -1.649960073803633E+00, 1.584092271148963E-01),
        doubleArrayOf(2.790880384447099E-07, -1.753655473493772E+00, 1.589879364100418E-01),
        doubleArrayOf(3.256983864383599E-07, -4.347514166563534E-01, 1.589932082610822E-01),
        doubleArrayOf(2.267895350910583E-07, 3.024427904422583E+00, 1.595719175562276E-01),
        doubleArrayOf(1.090721316940875E-07, -1.939541945846773E+00, 1.595771894072681E-01),
    )

    private val IAPETUS_1_3 = arrayOf(
        doubleArrayOf(4.889359735634015E-07, 5.416167758678300E+00, 2.358485830483941E-01),
        doubleArrayOf(2.502679246525132E-06, 3.480902144089144E-01, 2.364325641945799E-01),
    )

    private val IAPETUS_1_4 = arrayOf(
        doubleArrayOf(5.227495470767936E-07, 4.005663121141773E+00, 2.153785202069178E-04),
        doubleArrayOf(5.262843436434110E-07, 2.183142522975625E+00, 2.206503712473779E-04),
        doubleArrayOf(3.815771898913724E-07, 7.105474585412466E-02, 7.504405092155264E-04),
        doubleArrayOf(9.196878775751337E-07, 2.473094150998370E+00, 7.694936877791099E-04),
        doubleArrayOf(1.352657464923885E-06, 2.873044246870902E+00, 7.803065054430962E-04),
        doubleArrayOf(3.973724314685021E-07, 4.140593867861234E-01, 7.832750153022327E-04),
        doubleArrayOf(1.368009711983665E-06, 1.050560920047950E+00, 7.855783564835565E-04),
        doubleArrayOf(3.472239690061985E-07, 1.735637572758064E+00, 7.885468663426930E-04),
        doubleArrayOf(3.480378535316622E-07, 5.511199893486148E+00, 7.908502075240164E-04),
        doubleArrayOf(3.033297996664303E-07, 6.195403829536804E+00, 7.938187173831529E-04),
        doubleArrayOf(3.418187177414854E-07, 8.141915154020281E-01, 7.940878329662196E-04),
        doubleArrayOf(2.688244928253836E-06, 5.275014486169524E+00, 7.993596840066795E-04),
        doubleArrayOf(2.569260340418719E-06, 3.452633751557773E+00, 8.046315350471398E-04),
        doubleArrayOf(1.036863039010391E-06, 1.630081068326518E+00, 8.099033860875997E-04),
        doubleArrayOf(3.969393749935304E-07, 8.738771354003039E-01, 1.869483662023897E-03),
        doubleArrayOf(3.985256112150306E-06, 3.275634661668579E+00, 1.888536840587480E-03),
        doubleArrayOf(1.511339171430308E-06, 4.356355818907831E+00, 1.902318168110603E-03),
        doubleArrayOf(1.475074400837350E-05, 5.719796115875051E+00, 1.907590019151063E-03),
        doubleArrayOf(1.216707168547837E-06, 7.209469663619112E-01, 1.912861870191523E-03),
        doubleArrayOf(3.075011314359275E-07, 3.644290442329441E+00, 1.912996427983057E-03),
        doubleArrayOf(1.415742853315232E-06, 4.757124368944763E+00, 1.913130985774590E-03),
        doubleArrayOf(3.814316630052462E-07, 3.288005910176154E+00, 1.918275465063078E-03),
        doubleArrayOf(1.179423115340822E-05, 6.061718986149902E+00, 1.918402836815050E-03),
        doubleArrayOf(1.876817015958310E-06, 4.754449361520011E-01, 1.921371346674187E-03),
        doubleArrayOf(1.399238708843930E-05, 4.239759216446882E+00, 1.923674687855510E-03),
        doubleArrayOf(2.186693735333124E-05, 1.995274806149824E+00, 1.926643197714647E-03),
        doubleArrayOf(2.330447358044871E-06, 2.432875200820899E+00, 1.928946538895970E-03),
        doubleArrayOf(8.755936386396388E-07, 3.115210303371935E+00, 1.931915048755107E-03),
        doubleArrayOf(2.254199615438521E-06, 6.141816327520342E+00, 1.932049606546640E-03),
        doubleArrayOf(5.971965491672709E-06, 8.753490785501354E-01, 1.932184164338173E-03),
        doubleArrayOf(2.941694888718857E-07, 3.751626473924576E+00, 1.934218389936430E-03),
        doubleArrayOf(4.077887373315127E-07, 5.689822989499520E+00, 1.937328643626661E-03),
        doubleArrayOf(7.393168775867132E-05, 2.280321547311384E+00, 1.937456015378634E-03),
        doubleArrayOf(8.171736433893184E-05, 4.510335354461255E-01, 1.942727866419093E-03),
        doubleArrayOf(6.144966414607280E-07, 2.242399219818416E-02, 1.942855238171066E-03),
        doubleArrayOf(3.223198173228014E-06, 2.430574184290058E-01, 1.942862424210627E-03),
        doubleArrayOf(8.603158836863158E-07, 4.197919449469832E+00, 1.945696376278230E-03),
        doubleArrayOf(1.704559011257133E-05, -1.417339665945397E+00, 1.947999717459554E-03),
        doubleArrayOf(3.140486507800807E-07, 4.482050013324328E+00, 1.948127089211526E-03),
        doubleArrayOf(3.299741975330665E-06, 4.703710238329596E+00, 1.948134275251087E-03),
        doubleArrayOf(4.531105084562628E-07, 6.090353248906123E+00, 1.948141461290647E-03),
        doubleArrayOf(2.574587837723788E-06, 5.732598943257583E+00, 1.948268833042620E-03),
        doubleArrayOf(5.336777443382800E-06, 3.277820916335360E+00, 1.951237342901756E-03),
        doubleArrayOf(1.915584168637660E-06, 6.159110785889812E+00, 1.953271568500014E-03),
        doubleArrayOf(4.614651554236785E-07, 4.267748340354923E+00, 1.953413312331107E-03),
        doubleArrayOf(1.540440893862750E-06, -2.380019622661880E+00, 1.953540684083080E-03),
        doubleArrayOf(1.101769834711451E-04, -1.556727729554004E+00, 1.956509193942217E-03),
        doubleArrayOf(3.070012836628351E-07, 5.229794370811791E+00, 1.958812535123540E-03),
        doubleArrayOf(1.189228383255110E-04, 2.899805052106753E+00, 1.961781044982677E-03),
        doubleArrayOf(2.613243661620941E-06, 2.424238476169638E+00, 1.961908416734649E-03),
        doubleArrayOf(8.507522187621048E-06, 3.014198965814164E+00, 1.961915602774210E-03),
        doubleArrayOf(5.610129338334513E-07, 3.684122942793509E+00, 1.962050160565743E-03),
        doubleArrayOf(3.066646802040954E-05, 1.120589496342319E+00, 1.967052896023137E-03),
        doubleArrayOf(9.616747267908099E-07, 6.000060288823683E-01, 1.967180267775110E-03),
        doubleArrayOf(8.812132213666628E-06, 1.185788445220376E+00, 1.967187453814670E-03),
        doubleArrayOf(3.612477825551320E-05, -1.345764111856863E+00, 1.967322011606203E-03),
        doubleArrayOf(1.191488030171526E-06, 2.272066757023249E+00, 1.972324747063597E-03),
        doubleArrayOf(2.549155479604930E-06, 5.291014601965657E+00, 1.972459304855130E-03),
        doubleArrayOf(9.027685330072385E-05, -3.132006983447723E+00, 1.972593862646663E-03),
        doubleArrayOf(5.273506284484312E-07, 7.170338045570530E-01, 1.975562372505800E-03),
        doubleArrayOf(7.318838264333341E-05, 1.353835293153985E+00, 1.977865713687124E-03),
        doubleArrayOf(5.398713831902744E-07, 5.177746978176391E+00, 1.980834223546260E-03),
        doubleArrayOf(3.608057298728251E-06, 4.863591915141639E+00, 1.980961595298232E-03),
        doubleArrayOf(2.328751291389269E-05, 5.814424528754509E+00, 1.983137564727584E-03),
        doubleArrayOf(1.576239906932032E-06, 3.002295562826074E+00, 1.986233446338693E-03),
        doubleArrayOf(3.799033641308746E-07, 4.259130445062984E+00, 1.986375190169786E-03),
        doubleArrayOf(3.242171175776111E-07, 3.907553066682691E+00, 1.986502561921759E-03),
        doubleArrayOf(1.499811815760443E-06, 3.988002288677764E+00, 1.988409415768044E-03),
        doubleArrayOf(5.627708927914463E-07, 5.539620791805293E+00, 1.991647041210247E-03),
        doubleArrayOf(2.042389544816678E-06, 5.291730688852117E+00, 1.991774412962219E-03),
        doubleArrayOf(1.460797854077857E-06, 3.748973474201665E+00, 1.996918892250707E-03),
        doubleArrayOf(6.197295348292014E-06, 3.290476399266866E+00, 1.997046264002680E-03),
        doubleArrayOf(1.095526621882867E-06, 1.933069997683063E+00, 2.002190743291167E-03),
        doubleArrayOf(2.089500807780357E-06, 1.580270469414437E+00, 2.002318115043139E-03),
        doubleArrayOf(3.187588107072781E-07, 6.035967805962264E+00, 2.007589966083600E-03),
    )

    private val IAPETUS_1_5 = arrayOf(
        doubleArrayOf(3.414474608395144E-07, 5.333092134829300E+00, 3.928800102292660E-01),
        doubleArrayOf(3.741206194503089E-07, 3.334420556287444E+00, 3.940318878881610E-01),
        doubleArrayOf(2.637183491724404E-05, 4.683474110615003E+00, 3.940371597392014E-01),
        doubleArrayOf(8.279548593975801E-07, 1.908398224503419E+00, 3.940479725568654E-01),
        doubleArrayOf(6.484210285032441E-07, 3.233671623826318E+00, 3.940532444079058E-01),
    )

    private val IAPETUS_1_6 = arrayOf(
        doubleArrayOf(2.940137697333849E-06, 3.451571632901731E+00, 3.148353166650650E-01),
        doubleArrayOf(7.282549735894645E-04, 4.770667297372782E+00, 3.148405885161054E-01),
        doubleArrayOf(3.358151723062283E-06, 6.089767182911175E+00, 3.148458603671461E-01),
        doubleArrayOf(3.257749851873783E-07, 1.856841516027055E+00, 3.148650409174615E-01),
    )

    private val IAPETUS_1_7 = arrayOf(
        doubleArrayOf(9.488209354297983E-06, 4.943057563632108E+00, 4.732445437799614E-01),
        doubleArrayOf(1.029453478899256E-05, 3.120590331336322E+00, 4.732498156310018E-01),
        doubleArrayOf(2.792746040615356E-06, 1.297967072116361E+00, 4.732550874820422E-01),
        doubleArrayOf(4.374967901448417E-07, 5.170827648758129E+00, 4.732689961813175E-01),
    )

    private val IAPETUS_1_8 = arrayOf(
        doubleArrayOf(6.013024933189873E-06, 1.743122401426509E+00, 2.356440172930096E-01),
        doubleArrayOf(4.958075120563441E-06, 4.144929329850513E+00, 2.356630704715732E-01),
    )

    private val IAPETUS_1_9 = arrayOf(
        doubleArrayOf(7.533074112851135E-07, 1.960182176247418E+00, 1.564260895501687E-01),
        doubleArrayOf(2.517101053299125E-06, 1.375551717917236E-01, 1.564313614012092E-01),
        doubleArrayOf(2.480836668098538E-06, 4.598277210434070E+00, 1.564366332522496E-01),
        doubleArrayOf(1.143486543010702E-06, 1.117389755399236E+00, 1.564664992484772E-01),
    )

    private val IAPETUS_1_10 = arrayOf(
        doubleArrayOf(3.574500970589592E-07, 5.215822607081832E+00, 7.722951832707283E-02),
        doubleArrayOf(1.392818914226552E-06, 3.393198290814080E+00, 7.723479017811326E-02),
        doubleArrayOf(1.360536764325014E-06, 1.570730155065997E+00, 7.724006202915373E-02),
        doubleArrayOf(4.891864753374598E-07, 5.795005219238083E+00, 7.725384335667685E-02),
        doubleArrayOf(4.778483243651071E-07, 3.972537083490000E+00, 7.725911520771732E-02),
    )

    private val IAPETUS_1_11 = arrayOf(
        doubleArrayOf(7.489157248963648E-07, 6.282589894747257E-01, 7.880552662998393E-01),
        doubleArrayOf(7.443531196416904E-07, 3.430541494724291E+00, 7.880851322960668E-01),
        doubleArrayOf(7.620147658522374E-07, 1.608073358976210E+00, 7.880904041471072E-01),
    )

    private val IAPETUS_1_12 = arrayOf(
        doubleArrayOf(2.222879678616121E-05, 7.364334077752746E-01, 7.088586950767433E-01),
    )

    private val IAPETUS_1_13 = arrayOf(
        doubleArrayOf(6.546227444825897E-07, 1.939050585742052E+00, 6.296759051811704E-01),
        doubleArrayOf(1.769563612146425E-05, 3.258149248363778E+00, 6.296811770322111E-01),
        doubleArrayOf(6.546227444825897E-07, 4.577247910985506E+00, 6.296864488832515E-01),
    )

    private val IAPETUS_1_14 = arrayOf(
        doubleArrayOf(2.569819239848606E-06, 2.306043720186055E-01, 5.504846058091149E-01),
        doubleArrayOf(1.150895741388962E-06, 2.632411300442608E+00, 5.505036589876786E-01),
    )

    private val IAPETUS_1_15 = arrayOf(
        doubleArrayOf(3.716648744882634E-07, 4.908225137751350E+00, 4.712719499173147E-01),
        doubleArrayOf(3.630505879727552E-07, 3.085757002003267E+00, 4.712772217683551E-01),
    )

    private val IAPETUS_1_16 = arrayOf(
        doubleArrayOf(7.410458807790901E-07, 2.997108078146744E+00, 1.102876801637381E+00),
        doubleArrayOf(2.917099551383937E-07, 9.555532956830555E-02, 1.102930992663213E+00),
    )

    private val IAPETUS_1_17 = arrayOf(
        doubleArrayOf(3.001367588499240E-07, 5.512961820466838E+00, 1.023699283592849E+00),
    )

    private val IAPETUS_1_18 = arrayOf(
        doubleArrayOf(4.216532478702710E-06, 1.745631218955876E+00, 9.445217655483165E-01),
    )

    private val IAPETUS_1_19 = arrayOf(
        doubleArrayOf(8.149891095115382E-07, 5.001271649790290E+00, 8.653251943252205E-01),
        doubleArrayOf(3.764251887678508E-07, 1.119909356269040E+00, 8.653442475037843E-01),
    )

    private val IAPETUS_1_20 = arrayOf(
        doubleArrayOf(1.310246341606266E-06, 2.331131895479707E-01, 1.259362354064422E+00),
    )

    private val IAPETUS_1_21 = arrayOf(
        doubleArrayOf(3.830879278957400E-07, 5.003780467319657E+00, 1.574202942580528E+00),
    )

    private val IAPETUS_1_22 = arrayOf(
        doubleArrayOf(7.277204919188508E-07, 5.941165931805280E+00, 1.390848310696790E+00),
    )

    private val IAPETUS_1_23 = arrayOf(
        doubleArrayOf(2.181721459549961E-05, 6.055213709049899E+00, 1.311651739473694E+00),
    )

    private val IAPETUS_1_24 = arrayOf(
        doubleArrayOf(3.811901873161304E-07, 1.714424909184600E+00, 2.295712240023879E+00),
    )

    private val IAPETUS_1_25 = arrayOf(
        doubleArrayOf(1.184715424009358E-05, 1.828472686429220E+00, 2.216515668800783E+00),
    )

    private val IAPETUS_1_26 = arrayOf(
        doubleArrayOf(7.269853312811282E-06, 5.072983973765877E+00, 3.249104467422628E+00),
    )

    private val IAPETUS_1_27 = arrayOf(
        doubleArrayOf(5.287253161661598E-07, 6.336466577531914E-01, 4.506334773901807E+00),
    )

    private val IAPETUS_1_28 = arrayOf(
        doubleArrayOf(5.608837306250120E-07, 1.612346238109583E-02, 6.587859751149659E+00),
    )

    private val IAPETUS_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), IAPETUS_1_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 1, 0), IAPETUS_1_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 2, 0), IAPETUS_1_2),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 3, 0), IAPETUS_1_3),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 5, 0), IAPETUS_1_4),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), IAPETUS_1_5),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -1, 0), IAPETUS_1_6),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 1, 0), IAPETUS_1_7),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -2, 0), IAPETUS_1_8),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -3, 0), IAPETUS_1_9),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -4, 0), IAPETUS_1_10),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), IAPETUS_1_11),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -1, 0), IAPETUS_1_12),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -2, 0), IAPETUS_1_13),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -3, 0), IAPETUS_1_14),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -4, 0), IAPETUS_1_15),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -1, 0), IAPETUS_1_16),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -2, 0), IAPETUS_1_17),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -3, 0), IAPETUS_1_18),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -4, 0), IAPETUS_1_19),
        Term(intArrayOf(0, 0, 0, 0, 0, 4, -4, 0), IAPETUS_1_20),
        Term(intArrayOf(0, 0, 0, 0, 0, 5, -5, 0), IAPETUS_1_21),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 0, 0), IAPETUS_1_22),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, -1, 0), IAPETUS_1_23),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 0, 0), IAPETUS_1_24),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, -1, 0), IAPETUS_1_25),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, -1, 0), IAPETUS_1_26),
        Term(intArrayOf(0, 1, 0, 0, 0, 0, -1, 0), IAPETUS_1_27),
        Term(intArrayOf(1, 0, 0, 0, 0, 0, -1, 0), IAPETUS_1_28),
    )

    private val IAPETUS_2_0 = arrayOf(
        doubleArrayOf(7.357194732163894E-04, 6.035829207568037E+00, -5.406408831993331E-06),
        doubleArrayOf(2.935648107313708E-02, 3.358649794029711E+00, 5.406408831993331E-06),
        doubleArrayOf(6.699050371985383E-04, 4.753014210656826E+00, -1.067825987245346E-05),
        doubleArrayOf(4.151869133102979E-04, 4.743964405299185E+00, 1.067825987245346E-05),
        doubleArrayOf(1.899854046059933E-04, 3.517567007545812E+00, -1.595011091291359E-05),
        doubleArrayOf(1.991669418726220E-04, 4.485476749277378E+00, 1.918773635511653E-05),
        doubleArrayOf(1.011804925675502E-04, 3.745348713034283E+00, -2.445958739557666E-05),
        doubleArrayOf(9.953584143544754E-04, 5.831603333509536E+00, 2.445958739557666E-05),
        doubleArrayOf(6.928098928797385E-05, 5.576141930376416E+00, -2.973143843603680E-05),
        doubleArrayOf(1.234894258008271E-04, 8.652949986394700E-01, 2.973143843603680E-05),
        doubleArrayOf(2.082728796332944E-05, 1.173870037237461E+00, -3.500328947649692E-05),
        doubleArrayOf(3.040421176914689E-07, 2.136467296916470E+00, 5.595215764041851E-04),
        doubleArrayOf(1.287853944279400E-05, 5.287782933428181E+00, -5.785747549677684E-04),
        doubleArrayOf(2.039385297517835E-05, 4.585155128610154E+00, 5.785747549677684E-04),
        doubleArrayOf(1.595251648310000E-05, 6.234432051555658E+00, 5.839811637997618E-04),
        doubleArrayOf(1.343153395169566E-05, 4.649306306086897E+00, 5.893875726317552E-04),
        doubleArrayOf(1.799640976047236E-05, 2.341543068636097E-01, 1.143502740203947E-03),
        doubleArrayOf(1.130469268372695E-05, 4.574612374891347E+00, 1.157149509935537E-03),
        doubleArrayOf(3.789273029962630E-04, 2.745292826757329E+00, 1.162555918767530E-03),
        doubleArrayOf(1.065311292483293E-05, 3.513635458424760E-03, 1.173234178639984E-03),
        doubleArrayOf(6.594412507279831E-06, 1.160392236116490E+00, 1.178640587471977E-03),
        doubleArrayOf(4.598867989379571E-07, 1.609956090712706E+00, 1.727483904003709E-03),
        doubleArrayOf(4.889687732101663E-05, 4.014982830991590E+00, 1.746537082567292E-03),
        doubleArrayOf(5.041791242559473E-06, 5.286783389563643E+00, 2.330518246367054E-03),
    )

    private val IAPETUS_2_1 = arrayOf(
        doubleArrayOf(3.827903648104360E-07, 3.349310327302221E+00, -7.628207190099841E-02),
        doubleArrayOf(3.347616463036283E-06, 2.134214888660969E+00, -7.686605304718431E-02),
        doubleArrayOf(4.386017871369636E-07, 2.238193464103548E+00, -7.744476234232975E-02),
        doubleArrayOf(2.330306527936928E-05, 9.191067654693918E-01, -7.745003419337021E-02),
        doubleArrayOf(9.621927111300821E-07, 2.741692065655550E+00, -7.745530604441068E-02),
        doubleArrayOf(2.236294716863211E-06, 1.023085701193345E+00, -7.802874348851565E-02),
        doubleArrayOf(1.188902959997339E-04, -2.960003180568126E-01, -7.803401533955611E-02),
        doubleArrayOf(5.159571361902864E-06, 1.526594756217454E+00, -7.803928719059658E-02),
        doubleArrayOf(3.648905338577408E-06, 1.607558302204549E+00, -7.861799648574201E-02),
        doubleArrayOf(2.763121546924847E-07, -1.105228500517151E-01, -7.862326833678248E-02),
        doubleArrayOf(2.374053234088113E-06, -6.755268368608213E-03, -7.920197763192791E-02),
        doubleArrayOf(2.809879517690395E-06, -1.325635285498895E+00, -7.920724948296838E-02),
        doubleArrayOf(7.601575967574296E-07, 3.638825235880475E+00, -7.921252133400884E-02),
        doubleArrayOf(2.011807388354464E-07, -1.221851342565206E+00, -7.978595877811381E-02),
        doubleArrayOf(2.722673162134422E-07, -2.540738375872117E+00, -7.979123062915428E-02),
        doubleArrayOf(2.341140383006404E-07, 2.833112279003631E-01, -8.036993992429971E-02),
        doubleArrayOf(1.079744827006783E-07, -1.035863524313422E+00, -8.037521177534018E-02),
    )

    private val IAPETUS_2_2 = arrayOf(
        doubleArrayOf(5.077127357111450E-07, 1.063128508151314E+00, 7.744476234232975E-02),
        doubleArrayOf(1.216563739390478E-07, -7.586058089542492E-01, 7.745003419337021E-02),
        doubleArrayOf(3.788966811408025E-07, 9.593705739442162E-01, 7.802347163747521E-02),
        doubleArrayOf(8.559499914533230E-07, 2.278052169500489E+00, 7.802874348851565E-02),
        doubleArrayOf(9.792124873256762E-07, -2.686078783286550E+00, 7.803401533955611E-02),
        doubleArrayOf(2.075828194269492E-06, 2.092609920904744E+00, 7.861799648574201E-02),
        doubleArrayOf(3.015291948812891E-06, -1.152929578102659E+00, 7.919670578088745E-02),
        doubleArrayOf(2.453612858406647E-04, 1.661250302251527E-01, 7.920197763192791E-02),
        doubleArrayOf(3.267848713606678E-06, 1.485175738282932E+00, 7.920724948296838E-02),
        doubleArrayOf(2.045440860185655E-06, -1.760359860454440E+00, 7.978595877811381E-02),
        doubleArrayOf(9.507520895249895E-07, 3.018328843736854E+00, 8.036993992429971E-02),
        doubleArrayOf(8.310721660889963E-07, -1.945802109050186E+00, 8.037521177534018E-02),
        doubleArrayOf(3.678842089652509E-07, 5.656064793685675E+00, 8.038048362638064E-02),
        doubleArrayOf(1.163907976076402E-07, 1.090855869404554E+00, 8.095392107048561E-02),
        doubleArrayOf(4.857377246389406E-07, 5.552306859478579E+00, 8.095919292152608E-02),
    )

    private val IAPETUS_2_3 = arrayOf(
        doubleArrayOf(1.155571530124003E-06, 2.959627469406032E+00, -1.572305865626520E-01),
    )

    private val IAPETUS_2_4 = arrayOf(
        doubleArrayOf(3.386727736182448E-07, 5.810388343392691E+00, 1.566574182341301E-01),
        doubleArrayOf(1.733359910757746E-06, 7.423107991233067E-01, 1.572413993803160E-01),
        doubleArrayOf(4.262641945379126E-06, 7.918495104173626E-01, 1.583794956764603E-01),
        doubleArrayOf(8.024442052834091E-06, 3.193669906570326E+00, 1.583985488550238E-01),
    )

    private val IAPETUS_2_5 = arrayOf(
        doubleArrayOf(3.591607352372491E-07, 4.481232803237869E+00, 2.352700083110402E-01),
        doubleArrayOf(2.976455752240285E-06, -5.868558890738737E-01, 2.358539894572260E-01),
        doubleArrayOf(1.320515754572473E-05, 6.282501451704030E-01, 2.364379706034119E-01),
        doubleArrayOf(8.543461484774715E-07, 5.088883400685836E+00, 2.364432424544524E-01),
        doubleArrayOf(3.770599260080091E-07, 4.984963438378681E+00, 2.370219517495979E-01),
        doubleArrayOf(1.426804754726213E-06, 3.386073918171003E-01, 2.376059328957838E-01),
        doubleArrayOf(1.112281761662677E-06, -1.484029309805413E+00, 2.376112047468242E-01),
        doubleArrayOf(3.039469315779643E-07, 2.976610131749417E+00, 2.376164765978647E-01),
    )

    private val IAPETUS_2_6 = arrayOf(
        doubleArrayOf(5.756485350981315E-07, 3.655807898223860E+00, 3.156345418265079E-01),
    )

    private val IAPETUS_2_7 = arrayOf(
        doubleArrayOf(9.807167381341471E-06, 4.647844982264678E+00, -3.940425661480334E-01),
        doubleArrayOf(1.050421274404971E-05, 1.871271231028191E-01, -3.940478379990738E-01),
        doubleArrayOf(2.816558865567409E-06, 2.009743661307299E+00, -3.940531098501144E-01),
        doubleArrayOf(3.824414539954584E-07, 4.420210596752325E+00, -3.940670185493895E-01),
    )

    private val IAPETUS_2_8 = arrayOf(
        doubleArrayOf(3.660318350154349E-07, 4.194707709203449E+00, -3.148161289287099E-01),
        doubleArrayOf(1.034350804286399E-05, 1.792690836877676E+00, -3.148351821072734E-01),
        doubleArrayOf(3.362081661163648E-07, 4.540912112901320E+00, -3.148459949249374E-01),
    )

    private val IAPETUS_2_9 = arrayOf(
        doubleArrayOf(1.891655464023067E-06, 2.997740360570593E+00, -2.356333390331371E-01),
        doubleArrayOf(2.738557431501198E-04, 1.678643034250733E+00, -2.356386108841776E-01),
        doubleArrayOf(2.085092385794607E-06, 3.595434709071512E-01, -2.356438827352181E-01),
    )

    private val IAPETUS_2_10 = arrayOf(
        doubleArrayOf(3.297148610970001E-07, 3.283240059277484E+00, -1.564259549923772E-01),
        doubleArrayOf(4.210058117501252E-07, 1.957966659954585E+00, -1.564312268434177E-01),
        doubleArrayOf(1.295232370537576E-05, 4.706187935978230E+00, -1.564420396610816E-01),
        doubleArrayOf(3.074615033071044E-06, 5.446004958756377E+00, -1.564610928396452E-01),
    )

    private val IAPETUS_2_11 = arrayOf(
        doubleArrayOf(5.503284015527491E-07, 1.347535507567527E+00, -7.722411191824082E-02),
        doubleArrayOf(2.144377111729538E-06, 3.170159823835280E+00, -7.722938376928129E-02),
        doubleArrayOf(2.094675673402415E-06, 4.992627959583361E+00, -7.723465562032175E-02),
        doubleArrayOf(4.509478629698950E-07, 1.450547484355915E+00, -7.724546843798574E-02),
        doubleArrayOf(9.711472430656398E-07, 2.190338602286141E+00, -7.726452161654932E-02),
    )

    private val IAPETUS_2_12 = arrayOf(
        doubleArrayOf(1.580459593848266E-06, 5.957697356265878E+00, 1.912996427983057E-03),
        doubleArrayOf(3.059086093929264E-06, 2.076274581959904E+00, 1.932049606546640E-03),
        doubleArrayOf(4.097571772366432E-06, 2.476739673478372E+00, 1.942862424210627E-03),
        doubleArrayOf(4.622849445770262E-06, 6.542846935894391E-01, 1.948134275251087E-03),
        doubleArrayOf(4.098021786747177E-07, 4.478092318805028E+00, 1.951102785110223E-03),
        doubleArrayOf(9.865359954273804E-07, 5.114846914000389E+00, 1.953406126291547E-03),
        doubleArrayOf(6.577318919877076E-07, 3.559551940631996E+00, 1.956643751733750E-03),
        doubleArrayOf(1.141609439284819E-05, 4.878643124436238E+00, 1.961915602774210E-03),
        doubleArrayOf(1.246701893241003E-05, 3.056131321602242E+00, 1.967187453814670E-03),
        doubleArrayOf(2.869127018959028E-06, 1.233485437260538E+00, 1.972459304855130E-03),
        doubleArrayOf(5.346245981429982E-07, 2.130300142857571E+00, 1.972728420438197E-03),
        doubleArrayOf(2.905389353328124E-07, 1.636237329753522E+00, 1.983272122519117E-03),
        doubleArrayOf(5.913037768933752E-07, 5.106224293368807E+00, 1.986368004130226E-03),
        doubleArrayOf(3.023788452701212E-07, 3.282662154041165E+00, 1.991639855170686E-03),
    )

    private val IAPETUS_2_13 = arrayOf(
        doubleArrayOf(5.121289657996665E-06, 3.617695571228794E+00, 3.940372942969930E-01),
        doubleArrayOf(5.937514602713236E-04, 4.936792335851361E+00, 3.940425661480334E-01),
        doubleArrayOf(5.590721162724015E-06, 6.255890970618563E+00, 3.940478379990738E-01),
        doubleArrayOf(3.508789971395948E-07, 2.022944114109337E+00, 3.940670185493895E-01),
    )

    private val IAPETUS_2_14 = arrayOf(
        doubleArrayOf(3.500731595844181E-06, 1.909247431651662E+00, 3.148459949249374E-01),
    )

    private val IAPETUS_2_15 = arrayOf(
        doubleArrayOf(2.223396437049981E-06, 4.822744530752217E+00, 4.732391373711294E-01),
    )

    private val IAPETUS_2_16 = arrayOf(
        doubleArrayOf(1.903491726480552E-06, 1.967591900767555E+00, 5.524465214118891E-01),
        doubleArrayOf(1.948656845764241E-06, 1.451237650194714E-01, 5.524517932629298E-01),
        doubleArrayOf(5.000991669041441E-07, 4.605684755931306E+00, 5.524570651139702E-01),
    )

    private val IAPETUS_2_17 = arrayOf(
        doubleArrayOf(3.067385455485728E-07, 2.393030894759276E+00, -7.880606727086713E-01),
        doubleArrayOf(3.140166875052082E-07, 4.215499030507360E+00, -7.880659445597117E-01),
    )

    private val IAPETUS_2_18 = arrayOf(
        doubleArrayOf(7.066767025578607E-06, 5.707015564669242E+00, -6.296567174448153E-01),
    )

    private val IAPETUS_2_19 = arrayOf(
        doubleArrayOf(3.279448099865014E-07, 2.451375363875169E+00, -5.504601462217194E-01),
        doubleArrayOf(3.126490699275733E-07, 1.368667098072893E+00, -5.504739275492424E-01),
        doubleArrayOf(1.048627591564010E-05, 4.956843545116611E-02, -5.504791994002831E-01),
        doubleArrayOf(3.126490699275733E-07, 5.013655080009026E+00, -5.504844712513235E-01),
    )

    private val IAPETUS_2_20 = arrayOf(
        doubleArrayOf(1.831410067152233E-06, 3.077113311796339E+00, -4.712826281771871E-01),
        doubleArrayOf(7.115752344230280E-07, 6.753063833723361E-01, -4.713016813557509E-01),
    )

    private val IAPETUS_2_21 = arrayOf(
        doubleArrayOf(1.721342888159264E-05, 9.024276128068640E-01, 7.880606727086713E-01),
    )

    private val IAPETUS_2_22 = arrayOf(
        doubleArrayOf(1.093705824191350E-06, 2.826816249991383E-01, 7.088831546641388E-01),
    )

    private val IAPETUS_2_23 = arrayOf(
        doubleArrayOf(2.918130882538270E-06, 1.562086464859070E+00, -8.653197879163885E-01),
    )

    private val IAPETUS_2_24 = arrayOf(
        doubleArrayOf(5.831454312634436E-07, 4.589631341204242E+00, -7.861232166932925E-01),
    )

    private val IAPETUS_2_25 = arrayOf(
        doubleArrayOf(5.754083403713949E-07, 3.163232139672567E+00, 1.182078779269309E+00),
    )

    private val IAPETUS_2_26 = arrayOf(
        doubleArrayOf(4.595462189000190E-07, 5.053348902770820E+00, 1.023723743180244E+00),
    )

    private val IAPETUS_2_27 = arrayOf(
        doubleArrayOf(8.343508926599710E-07, 3.074604494266973E+00, -1.180160376432494E+00),
    )

    private val IAPETUS_2_28 = arrayOf(
        doubleArrayOf(3.005442215364578E-07, 3.363297035584122E+00, -1.390853717105622E+00),
        doubleArrayOf(3.076753876071480E-07, 5.185765171332203E+00, -1.390858988956663E+00),
    )

    private val IAPETUS_2_29 = arrayOf(
        doubleArrayOf(6.251473936893123E-06, 3.940966283548398E-01, -1.232449761841766E+00),
    )

    private val IAPETUS_2_30 = arrayOf(
        doubleArrayOf(1.645103872537621E-05, 6.221338739275052E+00, 1.390853717105622E+00),
    )

    private val IAPETUS_2_31 = arrayOf(
        doubleArrayOf(3.207481705138808E-06, 4.620837650975520E+00, -2.137313691168855E+00),
    )

    private val IAPETUS_2_32 = arrayOf(
        doubleArrayOf(9.029410063641440E-06, 1.994597716654372E+00, 2.295717646432711E+00),
    )

    private val IAPETUS_2_33 = arrayOf(
        doubleArrayOf(1.980399556792357E-06, 1.376326363638862E+00, -3.169902489790702E+00),
    )

    private val IAPETUS_2_34 = arrayOf(
        doubleArrayOf(5.659369633167750E-06, 5.239109003991030E+00, 3.328306445054557E+00),
    )

    private val IAPETUS_2_35 = arrayOf(
        doubleArrayOf(4.105962555389770E-07, 5.815663679651547E+00, -4.427132796269878E+00),
    )

    private val IAPETUS_2_36 = arrayOf(
        doubleArrayOf(3.999910250873396E-07, 7.997716879783442E-01, 4.585536751533733E+00),
    )

    private val IAPETUS_2_37 = arrayOf(
        doubleArrayOf(4.231711644426220E-07, 1.822484926062486E-01, 6.667061728781588E+00),
    )

    private val IAPETUS_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), IAPETUS_2_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, -1, 0), IAPETUS_2_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 1, 0), IAPETUS_2_2),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, -2, 0), IAPETUS_2_3),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 2, 0), IAPETUS_2_4),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 3, 0), IAPETUS_2_5),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 4, 0), IAPETUS_2_6),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 0, 0), IAPETUS_2_7),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 1, 0), IAPETUS_2_8),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 2, 0), IAPETUS_2_9),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 3, 0), IAPETUS_2_10),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 4, 0), IAPETUS_2_11),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 5, 0), IAPETUS_2_12),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), IAPETUS_2_13),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -1, 0), IAPETUS_2_14),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 1, 0), IAPETUS_2_15),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 2, 0), IAPETUS_2_16),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 0, 0), IAPETUS_2_17),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 2, 0), IAPETUS_2_18),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 3, 0), IAPETUS_2_19),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 4, 0), IAPETUS_2_20),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), IAPETUS_2_21),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, -1, 0), IAPETUS_2_22),
        Term(intArrayOf(0, 0, 0, 0, 0, -3, 4, 0), IAPETUS_2_23),
        Term(intArrayOf(0, 0, 0, 0, 0, -3, 5, 0), IAPETUS_2_24),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, 0, 0), IAPETUS_2_25),
        Term(intArrayOf(0, 0, 0, 0, 0, 3, -2, 0), IAPETUS_2_26),
        Term(intArrayOf(0, 0, 0, 0, 0, -4, 5, 0), IAPETUS_2_27),
        Term(intArrayOf(0, 0, 0, 0, -1, 0, 0, 0), IAPETUS_2_28),
        Term(intArrayOf(0, 0, 0, 0, -1, 0, 2, 0), IAPETUS_2_29),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 0, 0), IAPETUS_2_30),
        Term(intArrayOf(0, 0, 0, -1, 0, 0, 2, 0), IAPETUS_2_31),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 0, 0), IAPETUS_2_32),
        Term(intArrayOf(0, 0, -1, 0, 0, 0, 2, 0), IAPETUS_2_33),
        Term(intArrayOf(0, 0, 1, 0, 0, 0, 0, 0), IAPETUS_2_34),
        Term(intArrayOf(0, -1, 0, 0, 0, 0, 2, 0), IAPETUS_2_35),
        Term(intArrayOf(0, 1, 0, 0, 0, 0, 0, 0), IAPETUS_2_36),
        Term(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0), IAPETUS_2_37),
    )

    private val IAPETUS_3_0 = arrayOf(
        doubleArrayOf(6.794549145709775E-02, 5.047886356662284E+00, -5.271851040460129E-06),
        doubleArrayOf(6.892434301339112E-04, 1.398038368144667E+00, 5.271851040460129E-06),
        doubleArrayOf(4.570393856724005E-05, 3.663028182224427E+00, -1.054370208092026E-05),
        doubleArrayOf(2.873148969672351E-05, 1.130417841604620E+00, 1.067825987245346E-05),
        doubleArrayOf(1.915644482319336E-05, 1.666983826523547E+00, 1.608466870444679E-05),
        doubleArrayOf(5.927925628073686E-06, 2.236765758751786E+00, -1.621922649597999E-05),
        doubleArrayOf(1.388791091408471E-05, 6.199545784892948E+00, -1.870277299460525E-05),
        doubleArrayOf(2.730634121085477E-04, 3.074026406224565E+00, -2.445240135601595E-05),
        doubleArrayOf(9.851993483313069E-06, 3.568018055505390E+00, 2.445240135601595E-05),
        doubleArrayOf(1.363049113672050E-05, 1.573081241951383E+00, -2.972425239647608E-05),
        doubleArrayOf(6.564159971602750E-06, 5.899199575040269E+00, 2.986599622756999E-05),
        doubleArrayOf(1.115402869499410E-05, 2.124370914205684E+00, 4.891917479115332E-05),
        doubleArrayOf(5.075828846612914E-06, 3.331278759394790E-01, 5.419102583161344E-05),
        doubleArrayOf(2.829811667283325E-05, 3.179813769687029E+00, 5.787093127593016E-04),
        doubleArrayOf(4.494417792607357E-05, 5.092981879591376E+00, -5.839811637997618E-04),
        doubleArrayOf(3.010291742672222E-05, 4.323389769715201E+00, 5.839811637997618E-04),
        doubleArrayOf(2.832841674512957E-05, 3.775418852980096E+00, -5.892530148402220E-04),
        doubleArrayOf(9.901282214730337E-06, 3.022565890567675E+00, 5.892530148402220E-04),
        doubleArrayOf(3.855202341799354E-06, 3.260409788853447E+00, 1.149259554604918E-03),
        doubleArrayOf(6.779699256950095E-07, 3.450394767068876E+00, -1.167962327599524E-03),
        doubleArrayOf(2.641112196820165E-04, 6.085250961969193E+00, 1.167962327599524E-03),
        doubleArrayOf(5.003929435494984E-06, 5.249025520673378E+00, -1.173234178639984E-03),
        doubleArrayOf(1.816919041955310E-04, 1.123018507358733E+00, 1.173234178639984E-03),
        doubleArrayOf(4.700210983954140E-06, 3.997089887831509E+00, -1.178506029680444E-03),
        doubleArrayOf(3.932389676076612E-06, 5.902900444967474E+00, 1.186665100594129E-03),
        doubleArrayOf(3.374620641006819E-05, 1.072212121537805E+00, 1.751943491399285E-03),
        doubleArrayOf(2.348785638862567E-05, 2.392069283444719E+00, 1.757215342439746E-03),
        doubleArrayOf(3.494901083668216E-06, 2.384257843599439E+00, 2.335924655199047E-03),
    )

    private val IAPETUS_3_1 = arrayOf(
        doubleArrayOf(3.472415791165123E-07, -2.485713646106026E+00, 7.803942174838812E-02),
        doubleArrayOf(3.695428746936740E-07, 7.056695618928304E-01, 7.917751804453235E-02),
        doubleArrayOf(3.421209496536026E-07, 3.666045991093121E-01, 7.920738404075992E-02),
        doubleArrayOf(3.695428746936740E-07, 2.768173152147268E+00, 7.922643721932350E-02),
    )

    private val IAPETUS_3_2 = arrayOf(
        doubleArrayOf(3.970411713643165E-07, 2.468316120918740E+00, 1.566520118252981E-01),
        doubleArrayOf(1.388077368193791E-06, 2.364532110656320E+00, 1.572307211204436E-01),
        doubleArrayOf(1.564612965731580E-06, -2.599830403698914E+00, 1.572359929714840E-01),
        doubleArrayOf(3.930946817313776E-07, 5.320498414089342E+00, 1.578199741176699E-01),
        doubleArrayOf(4.741596443169412E-07, -2.890040852186059E+00, 1.584039552638558E-01),
        doubleArrayOf(1.832371513844348E-06, 1.571448595037313E+00, 1.584092271148963E-01),
        doubleArrayOf(3.902069246730890E-07, 1.467528632730159E+00, 1.589879364100418E-01),
        doubleArrayOf(4.268928712637706E-07, 3.104364787604514E+00, 1.595719175562276E-01),
    )

    private val IAPETUS_3_3 = arrayOf(
        doubleArrayOf(1.141957796039554E-06, 1.706497420624183E+00, -3.940371597392014E-01),
        doubleArrayOf(5.876712089500301E-07, 3.528959925655309E+00, -3.940424315902420E-01),
    )

    private val IAPETUS_3_4 = arrayOf(
        doubleArrayOf(2.987819907657320E-05, 1.592450553501527E+00, -3.148405885161054E-01),
        doubleArrayOf(1.598538661690393E-05, 3.414911760531445E+00, -3.148458603671461E-01),
        doubleArrayOf(5.196347763201437E-07, 1.364682192599308E+00, -3.148650409174615E-01),
    )

    private val IAPETUS_3_5 = arrayOf(
        doubleArrayOf(4.754237067161859E-07, 4.619994519724736E+00, -2.356440172930096E-01),
    )

    private val IAPETUS_3_6 = arrayOf(
        doubleArrayOf(3.836235546728265E-07, 6.225559278035010E+00, -1.564313614012092E-01),
        doubleArrayOf(7.454540946216246E-07, 1.764836475886549E+00, -1.564366332522496E-01),
    )

    private val IAPETUS_3_7 = arrayOf(
        doubleArrayOf(5.950685735865521E-07, 2.276553135246288E+00, 1.937456015378634E-03),
        doubleArrayOf(3.062968607914330E-07, 4.541169170095360E-01, 1.942727866419093E-03),
        doubleArrayOf(8.833068155471169E-07, 4.678333554199201E+00, 1.956509193942217E-03),
        doubleArrayOf(4.545649462411443E-07, 2.855871049168076E+00, 1.961781044982677E-03),
        doubleArrayOf(8.604417421849733E-07, 5.078832057446901E+00, 1.967322011606203E-03),
        doubleArrayOf(1.310815780246894E-06, 3.256355709659855E+00, 1.972593862646663E-03),
        doubleArrayOf(6.718820321393613E-07, 1.433809611894344E+00, 1.977865713687124E-03),
    )

    private val IAPETUS_3_8 = arrayOf(
        doubleArrayOf(1.141957796039554E-06, 5.137033501494948E+00, 3.940479725568654E-01),
        doubleArrayOf(5.876712089500302E-07, 3.314570996463822E+00, 3.940532444079058E-01),
    )

    private val IAPETUS_3_9 = arrayOf(
        doubleArrayOf(3.730666308347880E-07, 3.531492325926554E+00, 3.148353166650650E-01),
    )

    private val IAPETUS_3_10 = arrayOf(
        doubleArrayOf(2.011086466288782E-05, 5.022984906438330E+00, 4.732445437799614E-01),
        doubleArrayOf(1.081178008232983E-05, 3.200523219219202E+00, 4.732498156310018E-01),
        doubleArrayOf(3.781314342005297E-07, 5.250621987578875E+00, 4.732689961813175E-01),
    )

    private val IAPETUS_3_11 = arrayOf(
        doubleArrayOf(8.562203475402176E-07, 5.620822378456090E+00, -7.088586950767433E-01),
        doubleArrayOf(4.406257818911077E-07, 1.160099576307630E+00, -7.088639669277837E-01),
    )

    private val IAPETUS_3_12 = arrayOf(
        doubleArrayOf(4.385541576256689E-07, 6.246560326377261E+00, -6.296811770322111E-01),
    )

    private val IAPETUS_3_13 = arrayOf(
        doubleArrayOf(3.504051883832708E-07, 3.688750412526320E-01, 7.880851322960668E-01),
    )

    private val IAPETUS_3_14 = arrayOf(
        doubleArrayOf(6.998372934208747E-07, 9.946129891738023E-01, 8.672626503405993E-01),
        doubleArrayOf(3.601483607531824E-07, 5.455335791322262E+00, 8.672679221916397E-01),
    )

    private val IAPETUS_3_15 = arrayOf(
        doubleArrayOf(7.978736105198133E-07, 3.079032121013479E-01, -1.311651739473694E+00),
        doubleArrayOf(4.105995430914010E-07, 2.130365717132474E+00, -1.311657011324735E+00),
    )

    private val IAPETUS_3_16 = arrayOf(
        doubleArrayOf(7.118995492853203E-07, 2.434684834895837E-02, 1.470055694737550E+00),
        doubleArrayOf(3.663558059942478E-07, 4.485069650497419E+00, 1.470060966588590E+00),
    )

    private val IAPETUS_3_17 = arrayOf(
        doubleArrayOf(4.090821761277136E-07, 4.534644234722027E+00, -2.216515668800783E+00),
    )

    private val IAPETUS_3_18 = arrayOf(
        doubleArrayOf(3.817968724121190E-07, 2.080791132907865E+00, 2.374919624064639E+00),
    )

    private val IAPETUS_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), IAPETUS_3_0),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 1, 0), IAPETUS_3_1),
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 2, 0), IAPETUS_3_2),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 0, 0), IAPETUS_3_3),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 1, 0), IAPETUS_3_4),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 2, 0), IAPETUS_3_5),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 3, 0), IAPETUS_3_6),
        Term(intArrayOf(0, 0, 0, 0, 0, -1, 5, 0), IAPETUS_3_7),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 0, 0), IAPETUS_3_8),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, -1, 0), IAPETUS_3_9),
        Term(intArrayOf(0, 0, 0, 0, 0, 1, 1, 0), IAPETUS_3_10),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 1, 0), IAPETUS_3_11),
        Term(intArrayOf(0, 0, 0, 0, 0, -2, 2, 0), IAPETUS_3_12),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 0, 0), IAPETUS_3_13),
        Term(intArrayOf(0, 0, 0, 0, 0, 2, 1, 0), IAPETUS_3_14),
        Term(intArrayOf(0, 0, 0, 0, -1, 0, 1, 0), IAPETUS_3_15),
        Term(intArrayOf(0, 0, 0, 0, 1, 0, 1, 0), IAPETUS_3_16),
        Term(intArrayOf(0, 0, 0, -1, 0, 0, 1, 0), IAPETUS_3_17),
        Term(intArrayOf(0, 0, 0, 1, 0, 0, 1, 0), IAPETUS_3_18),
    )

    private val HYPERION_0_0 = arrayOf(
        doubleArrayOf(5.269198501828300E-03, 5.535906566693734E+00, 9.810539955099672E-03),
        doubleArrayOf(9.845757121969750E-04, 5.390367216813617E+00, 1.974675300542636E-01),
        doubleArrayOf(-9.447929974549504E-04, 2.695183608406955E+00, 9.873376502713178E-02),
        doubleArrayOf(-6.016015548174626E-04, 3.442462348892786E+00, 8.892322507203211E-02),
        doubleArrayOf(5.722146672387600E-04, 1.802365518040839E+00, 2.962012950813954E-01),
        doubleArrayOf(5.148150044366294E-04, 1.947904867920752E+00, 1.085443049822315E-01),
        doubleArrayOf(3.064232824160758E-04, 4.497549126447647E+00, 3.949350601085272E-01),
        doubleArrayOf(2.328679324996273E-04, 9.095474276756056E-01, 4.936688251356589E-01),
        doubleArrayOf(1.830222466848013E-04, 3.604731036082414E+00, 5.924025901627907E-01),
        doubleArrayOf(1.428121183641368E-04, 1.672933731036699E-02, 6.911363551899224E-01),
        doubleArrayOf(-1.309800881858535E-04, 4.597328162679121E+00, 1.072766731000612E-02),
        doubleArrayOf(1.186076675279319E-04, 1.912996635287834E-01, 8.893412600193221E-03),
        doubleArrayOf(1.030589558021878E-04, 2.711912945716443E+00, 7.898701202170543E-01),
        doubleArrayOf(9.162128151198170E-05, 2.549644258526895E+00, 2.863907551262957E-01),
        doubleArrayOf(8.420360344777332E-05, 5.244827866933704E+00, 3.851245201534275E-01),
        doubleArrayOf(-7.587557241443920E-05, 4.189741089378624E+00, 7.911268511693244E-02),
        doubleArrayOf(7.477636779983881E-05, 5.407096554123251E+00, 8.886038852441861E-01),
        doubleArrayOf(6.245968777723361E-05, 1.656826168161656E+00, 4.838582851805592E-01),
        doubleArrayOf(-5.925277975412419E-05, 1.055086777554782E+00, 3.060118350364951E-01),
        doubleArrayOf(-5.866195836719566E-05, 4.576031364902324E-02, 1.965504026993571E-01),
        doubleArrayOf(-5.838551971068579E-05, 3.750270385962327E+00, 4.047456000636268E-01),
        doubleArrayOf(5.517120904542451E-05, 1.819094855351211E+00, 9.873376502713178E-01),
        doubleArrayOf(5.291804011662172E-05, 4.352009776567740E+00, 5.825920502076911E-01),
        doubleArrayOf(4.785477163890776E-05, 7.640080777956939E-01, 6.813258152348228E-01),
        doubleArrayOf(4.648293960962418E-05, 6.137645957299674E+00, 1.876569900991639E-01),
        doubleArrayOf(-4.509683939781330E-05, 1.622686871902795E-01, 5.034793650907585E-01),
        doubleArrayOf(-4.394073687298339E-05, 1.200626127435425E+00, 1.183548449373311E-01),
        doubleArrayOf(4.393229670558661E-05, 3.459191686201771E+00, 7.800595802619547E-01),
        doubleArrayOf(4.272829406941570E-05, 1.848125831689869E+00, 4.927516977807525E-01),
        doubleArrayOf(4.154122350033837E-05, 4.514278463755098E+00, 1.086071415298450E+00),
        doubleArrayOf(-4.056750796467554E-05, 2.857452295597089E+00, 6.022131301178903E-01),
        doubleArrayOf(-3.857616137051121E-05, 5.552635904003895E+00, 7.009468951450221E-01),
        doubleArrayOf(3.815083219130052E-05, 6.154375294608579E+00, 8.787933452890865E-01),
        doubleArrayOf(-3.619913197295652E-05, 1.964634205231123E+00, 7.996806601721539E-01),
        doubleArrayOf(-3.615550506243641E-05, 6.017393906061373E-01, 1.778464501440642E-01),
        doubleArrayOf(3.211045925583688E-05, 2.566373595836530E+00, 9.775271103162182E-01),
        doubleArrayOf(-3.191646069822739E-05, 4.659817813637930E+00, 8.984144251992857E-01),
        doubleArrayOf(3.106998351274690E-05, 9.262767649808578E-01, 1.184805180325582E+00),
        doubleArrayOf(-2.986485373026291E-05, 4.041349085722073E+00, 2.943161986529901E-02),
        doubleArrayOf(-2.797299420355516E-05, 3.895809735842240E+00, 2.170886099644629E-01),
        doubleArrayOf(-2.722632461441940E-05, 1.071816114865886E+00, 9.971481902264174E-01),
        doubleArrayOf(-2.676900656082160E-05, 3.971018944640962E+00, 1.095544904247980E+00),
        doubleArrayOf(2.670876942507434E-05, 5.261557204243347E+00, 1.076260875343350E+00),
        doubleArrayOf(2.422130320267833E-05, 4.788627826207911E+00, 1.962107991019934E-02),
        doubleArrayOf(2.299675108056304E-05, 3.621460373393509E+00, 1.283538945352713E+00),
        doubleArrayOf(-2.297557612018265E-05, 3.766999723274154E+00, 1.095881955253549E+00),
        doubleArrayOf(2.232369387111974E-05, 1.673555505469107E+00, 1.174994640370482E+00),
        doubleArrayOf(-1.940722128323030E-05, 1.789980244999149E-01, 1.194615720280681E+00),
        doubleArrayOf(1.861668359762009E-05, 4.368739113874453E+00, 1.273728405397614E+00),
        doubleArrayOf(1.721156885182891E-05, 4.381040752907420E+00, 8.800609771712566E-02),
        doubleArrayOf(1.683393793859745E-05, 3.345867461927939E-02, 1.382272710379845E+00),
        doubleArrayOf(-1.635856648806699E-05, 2.874181632905266E+00, 1.293349485307813E+00),
        doubleArrayOf(1.543735278843180E-05, 7.807374151075219E-01, 1.372462170424745E+00),
        doubleArrayOf(-1.367379954537092E-05, 5.569365241310616E+00, 1.392083250334945E+00),
        doubleArrayOf(-1.338883184199104E-05, 2.503883944878085E+00, 8.984035242693857E-02),
        doubleArrayOf(-1.336950395903937E-05, 3.078080370694616E-01, 3.158223749915947E-01),
        doubleArrayOf(-1.320526200861202E-05, 3.296922999012952E+00, 2.765802151711960E-01),
        doubleArrayOf(-1.308495447734650E-05, 6.027455951817885E+00, 2.000408834733950E+00),
        doubleArrayOf(-1.302898520468604E-05, 1.009326463906489E+00, 1.094614323371379E-01),
        doubleArrayOf(1.289973076525889E-05, 4.062988177672174E+00, 9.924156005528160E-03),
        doubleArrayOf(-1.276260623147002E-05, 7.256396485357081E-01, 9.696923904671184E-03),
        doubleArrayOf(1.271138964892950E-05, 3.475921023512875E+00, 1.471195935451877E+00),
        doubleArrayOf(-1.249786948042500E-05, 4.914148009431628E+00, 2.962012605910000E-01),
        doubleArrayOf(1.223977806847720E-05, 2.728642283024626E+00, 1.481006475406977E+00),
        doubleArrayOf(-1.206158887296372E-05, 3.633762012421656E+00, 9.781663767222532E-02),
        doubleArrayOf(-1.131018256569493E-05, 1.981363542543681E+00, 1.490817015362076E+00),
        doubleArrayOf(-1.089636645498038E-05, 2.740943922055832E+00, 2.952841677264889E-01),
        doubleArrayOf(1.039179172259797E-05, 6.171104631918221E+00, 1.569929700479009E+00),
        doubleArrayOf(1.012836012521588E-05, 2.886483271935745E+00, 1.076271776273250E-01),
        doubleArrayOf(-9.589777592386616E-06, 5.344606903164943E+00, 9.171273549064512E-04),
        doubleArrayOf(-9.298418091702524E-06, 4.676547150949028E+00, 1.589550780389208E+00),
        doubleArrayOf(8.847111426033778E-06, 5.423825891429972E+00, 1.579740240434109E+00),
        doubleArrayOf(-8.783581973124320E-06, 4.617972413769575E+00, 5.894496611094870E-01),
        doubleArrayOf(8.461716341891088E-06, 2.583102933143981E+00, 1.668663465506141E+00),
        doubleArrayOf(-7.901292288839958E-06, 4.643088476327559E+00, 2.072780700093633E-01),
        doubleArrayOf(7.719706043802130E-06, 4.937019829864389E+00, 6.930214516183278E-02),
        doubleArrayOf(-7.609973567682646E-06, 1.088545452174794E+00, 1.688284545416340E+00),
        doubleArrayOf(7.430427565196998E-06, 3.313652336323319E+00, 9.677165703611184E-01),
        doubleArrayOf(7.356833812577206E-06, 6.184687279150488E-01, 8.689828053339868E-01),
        doubleArrayOf(7.165256187015007E-06, 6.008835944731589E+00, 1.066450335388250E+00),
        doubleArrayOf(6.955885415513037E-06, 4.206470426688558E+00, 7.702490403068549E-01),
        doubleArrayOf(6.885291075415051E-06, 5.278286541549328E+00, 1.767397230533273E+00),
        doubleArrayOf(6.614895521474710E-06, 1.511286818281744E+00, 6.715152752797231E-01),
        doubleArrayOf(6.331562819397564E-06, 1.835824192663037E+00, 1.678474005461240E+00),
        doubleArrayOf(-6.179612339504240E-06, 3.783729060580140E+00, 1.787018310443472E+00),
        doubleArrayOf(6.131629134251356E-06, 5.116017854362701E+00, 1.263917865442514E+00),
        doubleArrayOf(5.583256693630428E-06, 1.528016155588465E+00, 1.362651630469646E+00),
        doubleArrayOf(5.538856606051041E-06, 1.690284842782397E+00, 1.866130995560404E+00),
        doubleArrayOf(-5.031578459421872E-06, 4.533473869493745E-01, 1.281653848924308E-01),
        doubleArrayOf(-4.727461773298300E-06, 1.403512188080000E+00, 1.136827010127225E-02),
        doubleArrayOf(-4.490169397561938E-06, 8.800270869150743E-01, 9.899519638809918E-03),
        doubleArrayOf(4.445857119863126E-06, 3.908600739292801E+00, 9.721560271389426E-03),
        doubleArrayOf(3.929644136581539E-06, 3.385115638127897E+00, 8.252809808927092E-03),
        doubleArrayOf(3.847298333647876E-06, 5.128319493393325E+00, 7.819555776202598E-02),
        doubleArrayOf(-3.502084178980730E-06, 3.251162685363996E+00, 8.002981247183889E-02),
        doubleArrayOf(-2.961456426220440E-06, 5.853277499591428E+00, 2.952841654680000E-01),
        doubleArrayOf(2.811429184555117E-06, 3.658749758664493E+00, 1.164479466491257E-02),
        doubleArrayOf(2.625536042038141E-06, 1.756605204392253E+00, 9.965089238203824E-02),
        doubleArrayOf(2.190398167707681E-06, 2.620477234204319E-01, 1.192719722922376E-01),
        doubleArrayOf(-2.173672397129618E-06, 4.979927489736701E+00, 2.851449251039256E-02),
        doubleArrayOf(2.165574780684495E-06, 3.102770681707372E+00, 3.034874722020547E-02),
        doubleArrayOf(-2.086152830538867E-06, 2.139204531449687E+00, 1.174377175824247E-01),
        doubleArrayOf(1.758820758802031E-06, 2.435682350271240E+00, 9.835176321817914E-03),
        doubleArrayOf(-1.729303956187257E-06, 2.352945475936649E+00, 9.785903588381428E-03),
        doubleArrayOf(1.628729863936739E-06, 5.684298570350299E+00, 5.949160520673310E-02),
        doubleArrayOf(-1.424540056085036E-06, 4.915380737914367E+00, 8.880960902160362E-02),
        doubleArrayOf(1.386736928659767E-06, 1.969543959871211E+00, 8.903684112246060E-02),
        doubleArrayOf(1.373772890937228E-06, 4.749864788991031E-01, 1.086579210326600E-01),
        doubleArrayOf(-1.342309573473890E-06, 3.420823256942400E+00, 1.084306889318030E-01),
    )

    private val HYPERION_0 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), HYPERION_0_0),
    )

    private val HYPERION_1_0 = arrayOf(
        doubleArrayOf(1.591300460227652E-01, 5.535906566693734E+00, 9.810539955099672E-03),
        doubleArrayOf(4.042489669732959E-03, 1.912996635287834E-01, 8.893412600193221E-03),
        doubleArrayOf(-3.674456394728999E-03, 4.597328162679121E+00, 1.072766731000612E-02),
        doubleArrayOf(2.477704948047725E-03, 5.390367216813617E+00, 1.974675300542636E-01),
        doubleArrayOf(1.876329764520020E-03, 2.695183608406955E+00, 9.873376502713178E-02),
        doubleArrayOf(-1.559041896665946E-03, 3.442462348892786E+00, 8.892322507203211E-02),
        doubleArrayOf(1.534084173919484E-03, 5.344606903164943E+00, 9.171273549064512E-04),
        doubleArrayOf(1.177430869304958E-03, 1.802365518040839E+00, 2.962012950813954E-01),
        doubleArrayOf(1.132234522571428E-03, 1.947904867920752E+00, 1.085443049822315E-01),
        doubleArrayOf(7.097516189849207E-04, 4.497549126447647E+00, 3.949350601085272E-01),
        doubleArrayOf(4.276809299095375E-04, 9.095474276756056E-01, 4.936688251356589E-01),
        doubleArrayOf(-3.898971540977004E-04, 7.256396485357081E-01, 9.696923904671184E-03),
        doubleArrayOf(3.851351224149008E-04, 4.062988177672174E+00, 9.924156005528160E-03),
        doubleArrayOf(-3.602762810400590E-04, 4.810266918158017E+00, 1.136160504284885E-04),
        doubleArrayOf(-3.108861291415183E-04, 4.189741089378624E+00, 7.911268511693244E-02),
        doubleArrayOf(-3.036800061325884E-04, 4.041349085722073E+00, 2.943161986529901E-02),
        doubleArrayOf(2.883086646474427E-04, 3.604731036082414E+00, 5.924025901627907E-01),
        doubleArrayOf(2.741386484621230E-04, 2.880428915709079E+00, 1.167962290513110E-03),
        doubleArrayOf(2.445211406069563E-04, 6.137645957299674E+00, 1.876569900991639E-01),
        doubleArrayOf(2.397418179235776E-04, 4.432376484564475E+00, 5.839811452565560E-04),
        doubleArrayOf(2.079619720142985E-04, 2.549644258526895E+00, 2.863907551262957E-01),
        doubleArrayOf(1.997665368454137E-04, 1.672933731036699E-02, 6.911363551899224E-01),
        doubleArrayOf(-1.967051700749114E-04, 3.182961090757090E+00, 2.463636671824243E-05),
        doubleArrayOf(1.584700380869004E-04, 4.788627826207911E+00, 1.962107991019934E-02),
        doubleArrayOf(1.532039999508838E-04, 5.244827866933704E+00, 3.851245201534275E-01),
        doubleArrayOf(1.443751571550919E-04, 3.385115638127897E+00, 8.252809808927092E-03),
        doubleArrayOf(1.391797303858132E-04, 2.711912945716443E+00, 7.898701202170543E-01),
        doubleArrayOf(1.354323755526581E-04, 3.908600739292801E+00, 9.721560271389426E-03),
        doubleArrayOf(-1.347079373955693E-04, 1.055086777554782E+00, 3.060118350364951E-01),
        doubleArrayOf(-1.344254972843197E-04, 8.800270869150743E-01, 9.899519638809918E-03),
        doubleArrayOf(-1.336552626452528E-04, 1.200626127435425E+00, 1.183548449373311E-01),
        doubleArrayOf(-1.248289604761352E-04, 1.403512188080000E+00, 1.136827010127225E-02),
        doubleArrayOf(-1.168617638452060E-04, 4.643088476327559E+00, 2.072780700093633E-01),
        doubleArrayOf(1.158407408725238E-04, 1.656826168161656E+00, 4.838582851805592E-01),
        doubleArrayOf(-1.110204610592649E-04, 3.750270385962327E+00, 4.047456000636268E-01),
        doubleArrayOf(9.876297087120832E-05, 5.407096554123251E+00, 8.886038852441861E-01),
        doubleArrayOf(9.360835142429720E-05, 4.352009776567740E+00, 5.825920502076911E-01),
        doubleArrayOf(-9.343384687595748E-05, 2.150790928565845E+00, 1.557730146172579E-03),
        doubleArrayOf(-8.787340150688395E-05, 1.622686871902795E-01, 5.034793650907585E-01),
        doubleArrayOf(8.558910102815169E-05, 1.627305827400927E+00, 8.897968371024604E-05),
        doubleArrayOf(7.626783708559587E-05, 7.640080777956939E-01, 6.813258152348228E-01),
        doubleArrayOf(-7.423412446912493E-05, 2.857452295597089E+00, 6.022131301178903E-01),
        doubleArrayOf(7.224202068094286E-05, 3.658749758664493E+00, 1.164479466491257E-02),
        doubleArrayOf(7.068861066099871E-05, 1.819094855351211E+00, 9.873376502713178E-01),
        doubleArrayOf(-6.947176438271949E-05, 4.576031364902324E-02, 1.965504026993571E-01),
        doubleArrayOf(-6.514978275214879E-05, 6.017393906061373E-01, 1.778464501440642E-01),
        doubleArrayOf(-6.245521353308536E-05, 5.552635904003895E+00, 7.009468951450221E-01),
        doubleArrayOf(6.236351497568400E-05, 3.459191686201771E+00, 7.800595802619547E-01),
        doubleArrayOf(5.296941292250232E-05, 2.435682350271240E+00, 9.835176321817914E-03),
        doubleArrayOf(-5.247540827620734E-05, 2.352945475936649E+00, 9.785903588381428E-03),
        doubleArrayOf(-5.211914123734037E-05, 1.964634205231123E+00, 7.996806601721539E-01),
        doubleArrayOf(5.087526477014214E-05, 4.514278463755098E+00, 1.086071415298450E+00),
        doubleArrayOf(5.058590687048317E-05, 6.154375294608579E+00, 8.787933452890865E-01),
        doubleArrayOf(-4.813389965573155E-05, 3.895809735842240E+00, 2.170886099644629E-01),
        doubleArrayOf(-4.301007833478336E-05, 4.659817813637930E+00, 8.984144251992857E-01),
        doubleArrayOf(4.189085863844754E-05, 1.756605204392253E+00, 9.965089238203824E-02),
        doubleArrayOf(4.104292740965665E-05, 2.566373595836530E+00, 9.775271103162182E-01),
        doubleArrayOf(4.098739653872672E-05, 4.381040752907420E+00, 8.800609771712566E-02),
        doubleArrayOf(-4.092616660610000E-05, 9.897290504821536E-01, 1.751943435769670E-03),
        doubleArrayOf(3.764144636960646E-05, 4.406028499150309E+00, 1.834254709812902E-03),
        doubleArrayOf(3.665390355309391E-05, 9.262767649808578E-01, 1.184805180325582E+00),
        doubleArrayOf(-3.538628961664771E-05, 1.071816114865886E+00, 9.971481902264174E-01),
        doubleArrayOf(-3.531748699358457E-05, 1.009326463906489E+00, 1.094614323371379E-01),
        doubleArrayOf(3.326628366799721E-05, 5.261557204243347E+00, 1.076260875343350E+00),
        doubleArrayOf(-2.947898798731622E-05, 4.858034027149458E+00, 8.139193758498604E-03),
        doubleArrayOf(-2.902403206479552E-05, 3.766999723274154E+00, 1.095881955253549E+00),
        doubleArrayOf(2.841090381393687E-05, 2.886483271935745E+00, 1.076271776273250E-01),
        doubleArrayOf(2.693554901487583E-05, 1.673555505469107E+00, 1.174994640370482E+00),
        doubleArrayOf(2.676761780821079E-05, 4.937019829864389E+00, 6.930214516183278E-02),
        doubleArrayOf(2.669007886238697E-05, 5.436127530462639E+00, 3.940179327536207E-01),
        doubleArrayOf(2.640617243698899E-05, 3.621460373393509E+00, 1.283538945352713E+00),
        doubleArrayOf(2.465038854019734E-05, 6.213779106238012E+00, 1.148188615170074E-02),
        doubleArrayOf(2.383157781622512E-05, 6.778725395442773E-01, 1.671346196601068E-03),
        doubleArrayOf(-2.373722745643357E-05, 1.789980244999149E-01, 1.194615720280681E+00),
        doubleArrayOf(-2.358765186013457E-05, 2.503883944878085E+00, 8.984035242693857E-02),
        doubleArrayOf(-2.328181620162680E-05, 5.210323605092877E+00, 2.952860072513110E-03),
        doubleArrayOf(-2.308684467527070E-05, 4.979927489736701E+00, 2.851449251039256E-02),
        doubleArrayOf(2.176062809432465E-05, 4.368739113874453E+00, 1.273728405397614E+00),
        doubleArrayOf(2.173788459895790E-05, 3.102770681707372E+00, 3.034874722020547E-02),
        doubleArrayOf(-1.934646504415605E-05, 2.874181632905266E+00, 1.293349485307813E+00),
        doubleArrayOf(1.897373895483440E-05, 3.345867461927939E-02, 1.382272710379845E+00),
        doubleArrayOf(1.754329413716687E-05, 7.807374151075219E-01, 1.372462170424745E+00),
        doubleArrayOf(1.642710507186450E-05, 1.129878067543419E+00, 7.976285245286770E-03),
        doubleArrayOf(-1.600410709697660E-05, 6.149453095252681E+00, 2.035764949513110E-03),
        doubleArrayOf(1.572430747504168E-05, 4.451788812799354E+00, 1.983846574091700E-01),
        doubleArrayOf(-1.571827863857085E-05, 5.569365241310616E+00, 1.392083250334945E+00),
        doubleArrayOf(1.553609418389716E-05, 5.128319493393325E+00, 7.819555776202598E-02),
        doubleArrayOf(-1.539945531353985E-05, 3.078080370694616E-01, 3.158223749915947E-01),
        doubleArrayOf(1.460196299297440E-05, 4.914148009431628E+00, 2.962012605910000E-01),
        doubleArrayOf(1.410585893877412E-05, 3.475921023512875E+00, 1.471195935451877E+00),
        doubleArrayOf(1.357189445488529E-05, 2.728642283024626E+00, 1.481006475406977E+00),
        doubleArrayOf(1.339823501057820E-05, 3.971018944640962E+00, 1.095544904247980E+00),
        doubleArrayOf(-1.337650472587841E-05, 3.251162685363996E+00, 8.002981247183889E-02),
        doubleArrayOf(1.288836483864210E-05, 4.542301028988324E+00, 1.281578356513110E-03),
        doubleArrayOf(-1.273330553828309E-05, 1.981363542543681E+00, 1.490817015362076E+00),
        doubleArrayOf(1.163556995976533E-05, 1.511286818281744E+00, 6.715152752797231E-01),
        doubleArrayOf(1.139577901854967E-05, 5.099288517053791E+00, 5.727815102525914E-01),
        doubleArrayOf(1.139366110710306E-05, 4.206470426688558E+00, 7.702490403068549E-01),
        doubleArrayOf(1.135306132914049E-05, 2.740943922055832E+00, 2.952841677264889E-01),
        doubleArrayOf(-1.132855895484880E-05, 3.124409773657545E+00, 1.084128336043461E-02),
        doubleArrayOf(1.131587409293219E-05, 6.171104631918221E+00, 1.569929700479009E+00),
        doubleArrayOf(1.088679752730659E-05, 6.070246551700696E+00, 1.061405125957763E-02),
        doubleArrayOf(1.085425461534599E-05, 6.184687279150488E-01, 8.689828053339868E-01),
        doubleArrayOf(-1.056787968358008E-05, 3.296922999012952E+00, 2.765802151711960E-01),
        doubleArrayOf(1.037117124427068E-05, 2.404104908646983E+00, 4.740477452254596E-01),
        doubleArrayOf(-1.027660284731680E-05, 4.676547150949028E+00, 1.589550780389208E+00),
        doubleArrayOf(1.027273355964679E-05, 5.199067553284680E+00, 1.885741174540704E-01),
        doubleArrayOf(-1.024878235180840E-05, 4.617972413769575E+00, 5.894496611094870E-01),
        doubleArrayOf(1.006767559393374E-05, 3.313652336323319E+00, 9.677165703611184E-01),
        doubleArrayOf(-1.000802392783223E-05, 4.533473869493745E-01, 1.281653848924308E-01),
        doubleArrayOf(9.633989740567134E-06, 5.423825891429972E+00, 1.579740240434109E+00),
        doubleArrayOf(9.178075378506732E-06, 6.008835944731589E+00, 1.066450335388250E+00),
        doubleArrayOf(9.065206170156284E-06, 2.583102933143981E+00, 1.668663465506141E+00),
        doubleArrayOf(8.851132878153258E-06, 8.637871140265755E-01, 2.971184224363018E-01),
        doubleArrayOf(8.798422861268053E-06, 5.684298570350299E+00, 5.949160520673310E-02),
        doubleArrayOf(-8.257707350863237E-06, 1.088545452174794E+00, 1.688284545416340E+00),
        doubleArrayOf(8.253549910000110E-06, 2.420834245957355E+00, 1.165184100415382E+00),
        doubleArrayOf(8.204317973862647E-06, 1.757809810726964E+00, 8.163830125216846E-03),
        doubleArrayOf(7.905521775023910E-06, 1.611065854511902E+00, 2.873078824812022E-01),
        doubleArrayOf(-7.853735166683912E-06, 5.581666880342553E+00, 2.063609426544568E-01),
        doubleArrayOf(7.724851326708509E-06, 5.001566581686802E+00, 9.007028650621710E-03),
        doubleArrayOf(7.341396041786271E-06, 5.116017854362701E+00, 1.263917865442514E+00),
        doubleArrayOf(7.254032016403240E-06, 6.027455951817885E+00, 2.000408834733950E+00),
        doubleArrayOf(7.228880718926971E-06, 5.278286541549328E+00, 1.767397230533273E+00),
        doubleArrayOf(-7.215481275780168E-06, 1.664218052550344E+00, 8.779796549764733E-03),
        doubleArrayOf(-6.984482201975051E-06, 2.342090592094629E+00, 1.045114274636580E-02),
        doubleArrayOf(-6.882066002549454E-06, 3.030818015480898E+00, 1.145724978498250E-02),
        doubleArrayOf(6.764635964795921E-06, 1.835824192663037E+00, 1.678474005461240E+00),
        doubleArrayOf(6.650438793970224E-06, 2.620477234204319E-01, 1.192719722922376E-01),
        doubleArrayOf(-6.615254376434763E-06, 3.783729060580140E+00, 1.787018310443472E+00),
        doubleArrayOf(6.543758079294575E-06, 3.912539073151877E+00, 9.082249651543854E-01),
        doubleArrayOf(6.469301087553129E-06, 1.528016155588465E+00, 1.362651630469646E+00),
        doubleArrayOf(6.431124552019571E-06, 3.245373743805582E-01, 1.006958730181517E+00),
        doubleArrayOf(-6.344479273710200E-06, 3.488222662541158E+00, 2.854736277713893E-01),
        doubleArrayOf(6.326716714869053E-06, 1.217355464745067E+00, 8.094912001272536E-01),
        doubleArrayOf(-6.296309881986443E-06, 2.139204531449687E+00, 1.174377175824247E-01),
        doubleArrayOf(6.126061421202252E-06, 3.019720982785904E+00, 1.105692495208649E+00),
        doubleArrayOf(6.110229923833445E-06, 3.633762012421656E+00, 9.781663767222532E-02),
        doubleArrayOf(6.015743140846809E-06, 3.089369332580482E+00, 6.406027912661278E-04),
        doubleArrayOf(5.753474696726435E-06, 4.306249462919441E+00, 3.860416475083339E-01),
        doubleArrayOf(5.748353093941175E-06, 1.690284842782397E+00, 1.866130995560404E+00),
        doubleArrayOf(5.708428891129938E-06, 5.714904591191261E+00, 1.204426260235781E+00),
        doubleArrayOf(5.649105509135010E-06, 4.223199763993809E+00, 1.461385395496778E+00),
        doubleArrayOf(5.648928770703093E-06, 4.805357163517846E+00, 7.107574351001218E-01),
        doubleArrayOf(-5.558326355997969E-06, 3.778096755966772E+00, 1.646709829882825E-03),
        doubleArrayOf(5.443666713560605E-06, 4.649337840653702E-01, 1.228539745617870E-02),
        doubleArrayOf(5.432340482357107E-06, 5.992106607419760E+00, 3.753139801983278E-01),
        doubleArrayOf(5.352373966360052E-06, 3.558970722433383E+00, 3.958521874634336E-01),
        doubleArrayOf(-5.276359293355092E-06, 1.957273618059048E-01, 1.885752075470604E+00),
        doubleArrayOf(5.250152718378343E-06, 1.848125831689869E+00, 4.927516977807525E-01),
        doubleArrayOf(5.229887464078713E-06, 2.126902892417018E+00, 1.303160025262913E+00),
        doubleArrayOf(4.890118052517932E-06, 6.351980652268782E-01, 1.560119160523909E+00),
        doubleArrayOf(4.723632226316384E-06, 4.822086500829672E+00, 1.401893790290044E+00),
        doubleArrayOf(4.693369092482988E-06, 4.531007801068383E+00, 1.777207770488372E+00),
        doubleArrayOf(4.561946954009516E-06, 4.385468451187749E+00, 1.964864760587536E+00),
        doubleArrayOf(-4.420710568728507E-06, 4.044201739498272E+00, 2.667696752160964E-01),
        doubleArrayOf(-4.380976601809811E-06, 1.349018131091464E+00, 1.680359101889646E-01),
        doubleArrayOf(4.337634584565960E-06, 2.110173555110300E+00, 6.120236700729901E-01),
        doubleArrayOf(4.289012976116846E-06, 7.182477641466621E-01, 4.847754125354657E-01),
        doubleArrayOf(4.260384871171156E-06, 3.330381673632227E+00, 1.658852925551041E+00),
        doubleArrayOf(4.220548662286636E-06, 1.234084802055436E+00, 1.500627555317176E+00),
        doubleArrayOf(-4.199363160735397E-06, 2.890910970218556E+00, 1.984485840497735E+00),
        doubleArrayOf(-3.958927857591537E-06, 2.637836897642000E+00, 1.806334976402677E-02),
        doubleArrayOf(3.941866401660965E-06, 6.224633990080018E+00, 1.081664699371637E-02),
        doubleArrayOf(-3.779778844595939E-06, 2.970022335278144E+00, 1.063868762629588E-02),
        doubleArrayOf(3.732023312137390E-06, 3.929268410460785E+00, 1.599361320344308E+00),
        doubleArrayOf(3.704937010630530E-06, 2.931053342080318E+00, 1.192598652513110E-03),
        doubleArrayOf(3.635875031439811E-06, 7.974667524135091E-01, 2.063598525614668E+00),
        doubleArrayOf(-3.613554557427136E-06, 4.915380737914367E+00, 8.880960902160362E-02),
        doubleArrayOf(3.510914428131760E-06, 1.969543959871211E+00, 8.903684112246060E-02),
        doubleArrayOf(-3.500526370624341E-06, 6.183406270948697E+00, 3.842073927985210E-01),
        doubleArrayOf(3.460035905216260E-06, 5.853277499591428E+00, 2.952841654680000E-01),
        doubleArrayOf(3.455891490801950E-06, 3.413431372552740E+00, 5.835091775625976E-01),
        doubleArrayOf(-3.450615143245680E-06, 8.053972782328979E-01, 1.118669826513110E-03),
        doubleArrayOf(3.406532781873487E-06, 6.254154330840199E+00, 4.945859524905654E-01),
        doubleArrayOf(2.958781539525876E-06, 4.749864788991031E-01, 1.086579210326600E-01),
        doubleArrayOf(-2.877066321058344E-06, 3.420823256942400E+00, 1.084306889318030E-01),
        doubleArrayOf(-2.684534114394913E-06, 1.818605490929709E+00, 8.982392283903467E-03),
        doubleArrayOf(2.527885180883367E-06, 4.847179143307436E+00, 8.804432916482976E-03),
        doubleArrayOf(-2.228949169524294E-06, 2.568430696700499E+00, 2.954523591572750E-02),
        doubleArrayOf(2.203418048212828E-06, 5.514267474743575E+00, 2.931800381487053E-02),
        doubleArrayOf(2.084495541017897E-06, 2.546791604750330E+00, 4.905269977549836E-02),
        doubleArrayOf(-2.078328653497296E-06, 5.875598233879090E+00, 6.838501780692632E-02),
        doubleArrayOf(2.053262782839373E-06, 1.212212524551202E+00, 2.474857501079031E-03),
        doubleArrayOf(1.926895597615764E-06, 3.998441425849760E+00, 7.021927251673923E-02),
        doubleArrayOf(1.866581488955838E-06, 3.825993270575803E-01, 1.778682520038644E-02),
        doubleArrayOf(1.793161742446505E-06, 1.291671420326874E+00, 8.736549492585954E-02),
        doubleArrayOf(1.728658611291661E-06, 5.989253953642903E+00, 1.379759248475305E-01),
        doubleArrayOf(1.676857763913550E-06, 6.562334475941619E-01, 2.117881005637192E-02),
        doubleArrayOf(1.535772080970505E-06, 4.776710899143922E-02, 8.025577708070115E-03),
        doubleArrayOf(-1.524956909904084E-06, 1.497103946256642E+00, 1.075230367672436E-02),
        doubleArrayOf(-1.522980567370601E-06, 5.662659478400199E+00, 7.899906906650395E-02),
        doubleArrayOf(-1.495199523539624E-06, 5.488139457702296E+00, 1.784962247029556E-03),
        doubleArrayOf(1.487447331838316E-06, 1.414367071922007E+00, 1.070303094328788E-02),
        doubleArrayOf(1.476058477371991E-06, 2.716822700357048E+00, 7.922630116736093E-02),
        doubleArrayOf(1.450002439077977E-06, 8.691722030730533E-01, 1.056475879679429E-02),
        doubleArrayOf(-1.242111402697583E-06, 4.740860717216437E+00, 1.159550220212923E-02),
        doubleArrayOf(1.240886283959093E-06, 1.815156521491816E+00, 8.883424538832187E-02),
        doubleArrayOf(-1.224261037265257E-06, 2.720171354649790E+00, 1.256192201981903E-02),
        doubleArrayOf(-1.221915897644608E-06, 5.069768176293690E+00, 8.901220475574236E-02),
        doubleArrayOf(-1.201215374450881E-06, 4.098695796487174E+00, 1.101020351284040E-01),
        doubleArrayOf(-1.186953182299199E-06, 2.911471018178655E+00, 2.145533462001224E-02),
        doubleArrayOf(-1.129771140532139E-06, 5.319619156922048E+00, 8.708897036221921E-02),
        doubleArrayOf(-1.097793441792323E-06, 5.275200702223381E+00, 1.239901350660719E-02),
        doubleArrayOf(-1.056701003062998E-06, 1.483920036565450E-01, 4.968106525163343E-02),
        doubleArrayOf(1.028753109593422E-06, 3.374260754285869E+00, 8.918048966911464E-03),
        doubleArrayOf(-1.020891709037016E-06, 3.575210695322014E+00, 1.086332846659417E-01),
        doubleArrayOf(-1.012670463985482E-06, 3.291523879951270E+00, 8.868776233474979E-03),
        doubleArrayOf(1.000328987513614E-06, 3.205990405202193E-01, 1.084553252985212E-01),
        doubleArrayOf(9.414415383367402E-07, 4.110755286663576E+00, 1.794973371359828E-02),
        doubleArrayOf(-9.256226899654040E-07, 2.164192277692745E+00, 3.126587457511192E-02),
        doubleArrayOf(-8.873327693660700E-07, 3.396916671853406E-01, 5.857447785182665E-02),
        doubleArrayOf(-8.599515368071076E-07, 3.230728199748524E+00, 8.050214074788358E-03),
        doubleArrayOf(-8.578646315557892E-07, 1.890558157156235E+00, 2.787388971912643E-02),
        doubleArrayOf(8.458120789255400E-07, 3.315709437186336E+00, 1.973469596062783E-02),
        doubleArrayOf(-8.217941676205146E-07, 5.593253277458703E+00, 9.048095521820468E-02),
        doubleArrayOf(7.998618448176001E-07, 2.446537234113254E+00, 9.169937163833544E-03),
        doubleArrayOf(7.948809078699551E-07, 6.192140014287844E+00, 3.098935001147160E-02),
        doubleArrayOf(7.924651953382082E-07, 4.745720166335665E+00, 6.040873256163955E-02),
        doubleArrayOf(-7.833211544506204E-07, 1.391925790964367E+00, 1.272482575375243E-01),
        doubleArrayOf(7.730107483777507E-07, 5.668654913122977E+00, 2.952059954900926E-02),
        doubleArrayOf(-7.622738839419112E-07, 2.414043258321102E+00, 2.934264018158877E-02),
        doubleArrayOf(7.622712310485859E-07, 7.074805989149507E-02, 1.103785596920444E-01),
        doubleArrayOf(7.575143203880983E-07, 6.080299246534646E+00, 1.069865748360589E-01),
        doubleArrayOf(-7.315354488166669E-07, 6.261546215229414E+00, 1.950746385977086E-02),
        doubleArrayOf(7.005448425844999E-07, 1.616450943558919E+00, 7.542188416946162E-04),
        doubleArrayOf(-6.870417571431780E-07, 6.010893045593364E+00, 1.184684609877596E-01),
    )

    private val HYPERION_1 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), HYPERION_1_0),
    )

    private val HYPERION_2_0 = arrayOf(
        doubleArrayOf(1.030661479148230E-01, 3.619080362792600E+00, -8.924811235147779E-04),
        doubleArrayOf(2.448184191185018E-02, 2.680501958777963E+00, 2.464623139167320E-05),
        doubleArrayOf(-2.500610695618523E-03, 2.871801622306748E+00, 8.918058831584894E-03),
        doubleArrayOf(-1.653120911968409E-03, 4.366359103278453E+00, -1.070302107861445E-02),
        doubleArrayOf(-1.121964769453605E-03, 3.107866401998293E-02, 9.784128390361700E-02),
        doubleArrayOf(7.518101576911162E-04, 9.238967543856604E-01, -9.962624615064656E-02),
        doubleArrayOf(3.778282825702326E-04, 4.511898453158731E+00, -1.983600111777784E-01),
        doubleArrayOf(-3.775434250722031E-04, 2.726262272426791E+00, 1.965750489307488E-01),
        doubleArrayOf(-3.597821116452316E-04, 1.833444182060827E+00, 3.940425789850124E-01),
        doubleArrayOf(-2.927952161795262E-04, 5.421445880833606E+00, 2.953088139578806E-01),
        doubleArrayOf(2.580134073493171E-04, 1.766180138998234E-01, -8.981570619554689E-02),
        doubleArrayOf(2.216814079711899E-04, 1.816714844751185E+00, -2.970937762049101E-01),
        doubleArrayOf(1.927636081642950E-04, 2.546497551037094E+00, 2.060411181513110E-03),
        doubleArrayOf(-1.702244907149874E-04, 1.671175494871279E+00, -1.094367861057462E-01),
        doubleArrayOf(-1.630491456609473E-04, 5.566985230713519E+00, 1.076518238587167E-01),
        doubleArrayOf(1.502092233208532E-04, 7.783574045058210E-01, 8.803074394851733E-02),
        doubleArrayOf(1.403880753848180E-04, 5.404716543523964E+00, -3.958275412320419E-01),
        doubleArrayOf(1.282680047161120E-04, 3.764619712672674E+00, -1.885494712226787E-01),
        doubleArrayOf(1.080722389283692E-04, 1.933223218292120E+00, 9.835186186491344E-03),
        doubleArrayOf(-9.843461962138636E-05, 5.259177193644051E+00, -2.081705511328780E-01),
        doubleArrayOf(9.302812855413870E-05, 2.709532935117156E+00, -4.945613062591737E-01),
        doubleArrayOf(8.608901766955960E-05, 1.069436104265866E+00, -2.872832362498105E-01),
        doubleArrayOf(8.563197359093362E-05, 4.557658766807236E+00, -1.809608478421229E-03),
        doubleArrayOf(8.231557260635240E-05, 3.485627041196898E+00, 1.143316058513110E-03),
        doubleArrayOf(-7.641879996400636E-05, 3.810380026321384E+00, 8.000931476678443E-03),
        doubleArrayOf(-6.976234551437248E-05, 2.563993585237243E+00, -3.069043161600098E-01),
        doubleArrayOf(6.410167941575658E-05, 5.091998751814161E+00, -1.006097173943266E-03),
        doubleArrayOf(6.394074317345045E-05, 4.657437803037907E+00, -3.860170012769422E-01),
        doubleArrayOf(6.387046194265387E-05, 1.434932671034752E-02, -5.932950712863055E-01),
        doubleArrayOf(-6.379700394998453E-05, 2.146161973771036E+00, -7.788650730862894E-04),
        doubleArrayOf(-5.338815029861825E-05, 6.151995284010021E+00, -4.056380811871416E-01),
        doubleArrayOf(5.173543661985320E-05, 3.342022422925534E+00, 2.755133995131110E-04),
        doubleArrayOf(4.908475624063901E-05, 1.962254194631099E+00, -4.847507663040740E-01),
        doubleArrayOf(4.811302148563021E-05, 4.528627790467636E+00, 4.927763440121442E-01),
        doubleArrayOf(4.486679433374308E-05, 3.602351025483124E+00, -6.920288363134373E-01),
        doubleArrayOf(-4.178177074434045E-05, 3.456811675602482E+00, -5.043718462142733E-01),
        doubleArrayOf(4.042027917523730E-05, 2.250321955375842E+00, 2.953088117000000E-01),
        doubleArrayOf(-3.880672848400252E-05, 2.580722922546885E+00, 3.842320390299127E-01),
        doubleArrayOf(3.819239194924841E-05, 5.550255893404608E+00, -5.834845313312059E-01),
        doubleArrayOf(-3.738690787954837E-05, 2.124522881820873E+00, 1.872859878668457E-02),
        doubleArrayOf(-3.553990230264197E-05, 6.168724621318925E+00, 2.854982740027810E-01),
        doubleArrayOf(3.415645857185234E-05, 9.406260916955893E-01, 5.915101090392759E-01),
        doubleArrayOf(3.339668674029588E-05, 1.086165441574771E+00, 4.038531189401121E-01),
        doubleArrayOf(-3.301162776329309E-05, 7.616280671963972E-01, -6.031056112414052E-01),
        doubleArrayOf(3.269372364602020E-05, 3.813563431717510E+00, 2.644392326769670E-03),
        doubleArrayOf(3.180628253542403E-05, 9.071674170763108E-01, -7.907626013405691E-01),
        doubleArrayOf(3.177451875567787E-05, 5.375685567184869E+00, 9.875841125852346E-02),
        doubleArrayOf(3.067833613233151E-05, 5.304937507293081E+00, -1.162014843352090E-02),
        doubleArrayOf(3.003432659990370E-05, 2.855072284997068E+00, -6.822182963583376E-01),
        doubleArrayOf(2.732958451060629E-05, 5.769871291358446E+00, 6.652490226578010E-04),
        doubleArrayOf(2.729633446248457E-05, 3.573320049143738E+00, -1.974428838228719E-01),
        doubleArrayOf(-2.629885713884026E-05, 4.349629765968445E+00, -7.018393762685369E-01),
        doubleArrayOf(2.559468055750869E-05, 5.712524580593572E+00, -8.000516624044722E-02),
        doubleArrayOf(2.473774782254921E-05, 4.674167140347548E+00, 3.051193539129803E-01),
        doubleArrayOf(-2.417336169407593E-05, 3.473541012912848E+00, 1.867645089756491E-01),
        doubleArrayOf(2.384156418867662E-05, 1.598886765909899E-01, -7.809520613854695E-01),
        doubleArrayOf(2.259673699575893E-05, 4.495169115849089E+00, -8.894963663677009E-01),
        doubleArrayOf(-2.109852115260203E-05, 1.654446157561637E+00, -8.005731412956687E-01),
        doubleArrayOf(2.024562165367701E-05, 1.525636144991658E+00, 7.822020399341766E-02),
        doubleArrayOf(-2.019380769983444E-05, 1.991774535391674E+00, -9.814608072250240E-04),
        doubleArrayOf(2.008621003222866E-05, 5.246386190193526E+00, -8.035014398045319E-04),
        doubleArrayOf(1.902408812662748E-05, 3.747890375363762E+00, -8.796858264126013E-01),
        doubleArrayOf(-1.811127409136190E-05, 5.113637843764284E+00, -2.051356103371412E-02),
        doubleArrayOf(1.706204149397460E-05, 3.017340972186618E+00, -1.787389312675790E-01),
        doubleArrayOf(-1.698064243423227E-05, 5.242447856334416E+00, -8.993069063228005E-01),
        doubleArrayOf(1.679462249970688E-05, 4.220819753398175E+00, 1.769539690205495E-01),
        doubleArrayOf(-1.632299947318510E-05, 4.886425133804248E+00, -3.084677457434440E-04),
        doubleArrayOf(1.603779600347512E-05, 1.799985507441551E+00, -9.882301313948326E-01),
        doubleArrayOf(1.519891681318009E-05, 1.052706766956224E+00, -9.784195914397330E-01),
        doubleArrayOf(1.379512640799549E-05, 3.221573637798089E-01, -2.774726962947108E-01),
        doubleArrayOf(-1.366561535006994E-05, 2.547264247926875E+00, -9.980406713499322E-01),
        doubleArrayOf(1.283396500574140E-05, 1.556768500554912E+00, 3.084677457434440E-04),
        doubleArrayOf(1.279090832954947E-05, 5.438175218143973E+00, 9.864451691478030E-01),
        doubleArrayOf(1.243806749353366E-05, 1.231704791455413E+00, 2.161961288409481E-01),
        doubleArrayOf(1.235958412008419E-05, 3.427780699263810E+00, -9.785893723707998E-03),
        doubleArrayOf(1.225443721836814E-05, 9.863864053438862E-01, 7.880605117386331E-01),
        doubleArrayOf(1.225216491402911E-05, 2.742991609736432E+00, 8.877114041206713E-01),
        doubleArrayOf(-1.221520345313342E-05, 5.275906530953691E+00, 4.829658040570445E-01),
        doubleArrayOf(1.213225148001182E-05, 4.640708465730461E+00, -1.077153356466865E+00),
        doubleArrayOf(1.198327722769453E-05, 3.910159062552588E+00, -3.762064613218426E-01),
        doubleArrayOf(1.186021515881310E-05, 4.752692921877314E+00, 1.727297203769670E-03),
        doubleArrayOf(1.166603449799361E-05, 1.377244141335036E+00, 2.853913874178424E-02),
        doubleArrayOf(1.135648805958188E-05, 5.387987206211405E+00, -1.086963896421964E+00),
        doubleArrayOf(1.108533869996140E-05, 6.221340900016817E+00, 1.390853715947980E+00),
        doubleArrayOf(-1.098010740280340E-05, 6.135265946699647E+00, -1.096774436377064E+00),
        doubleArrayOf(1.086995985899863E-05, 5.860916584250122E+00, -3.032410098881379E-02),
        doubleArrayOf(1.069580937462716E-05, 1.214975454145048E+00, -4.749402263489743E-01),
        doubleArrayOf(1.067386083025773E-05, 3.781349049983041E+00, 5.025868839672437E-01),
        doubleArrayOf(-1.043068210990957E-05, 5.521224917064781E+00, -8.889857884064044E-02),
        doubleArrayOf(9.693581297810074E-06, 1.850173519369733E+00, 1.085178934174935E+00),
        doubleArrayOf(9.678835539512692E-06, 1.945524857325114E+00, -1.175887121493997E+00),
        doubleArrayOf(9.562800946711422E-06, 4.802977152918551E+00, -5.736739913761062E-01),
        doubleArrayOf(9.468784690060410E-06, 4.780800132962380E-02, 7.889776390935395E-01),
        doubleArrayOf(8.801022040147010E-06, 3.915534848785861E+00, -2.941408494094870E-01),
        doubleArrayOf(-8.797753682670658E-06, 3.440082338294301E+00, -1.195508201404196E+00),
        doubleArrayOf(8.599214007703230E-06, 1.787683868411798E+00, 1.974921762856553E-01),
        doubleArrayOf(8.485093197565512E-06, 2.107793544511012E+00, -6.724077564032379E-01),
        doubleArrayOf(-8.365240462865090E-06, 1.279431670356606E+00, 1.476430036256560E-03),
        doubleArrayOf(8.240909734627314E-06, 4.361192720355115E-01, -9.171174902330204E-04),
        doubleArrayOf(-8.205019885615929E-06, 5.188561463701097E-01, -8.678447567965355E-04),
        doubleArrayOf(7.999837192197997E-06, 6.268503657550692E+00, -9.870911879574012E-02),
        doubleArrayOf(7.994800633732257E-06, 2.692803597806052E+00, -1.185697661449096E+00),
        doubleArrayOf(7.964077512935541E-06, 4.819706490227462E+00, 1.174623638138164E-01),
        doubleArrayOf(7.702743188457386E-06, 5.533526556092043E+00, -1.274620886521128E+00),
        doubleArrayOf(7.473079103968928E-06, 5.695795243283790E+00, -7.711415214303697E-01),
        doubleArrayOf(7.388692911769633E-06, 6.328180546253961E-01, 2.756877340476813E-01),
        doubleArrayOf(7.270826776415052E-06, 1.468289434226756E+00, -2.450211269687357E-03),
        doubleArrayOf(7.236960740797404E-06, 4.628406826699250E+00, 1.085689512136231E-01),
        doubleArrayOf(7.144251731442444E-06, 4.545357127775086E+00, 1.183912699202067E+00),
        doubleArrayOf(-7.043838984557596E-06, 7.448987298889554E-01, -1.294241966431328E+00),
        doubleArrayOf(-6.917942470086600E-06, 3.896696083322066E+00, -2.060411181513110E-03),
        doubleArrayOf(6.782083637837408E-06, 3.311272325723300E+00, -3.167148561151095E-01),
        doubleArrayOf(6.750671838792509E-06, 6.160887173164911E-01, -4.154486211422413E-01),
        doubleArrayOf(6.736625416531154E-06, 4.252643881934955E-01, -2.518783322486501E-04),
        doubleArrayOf(6.716695065274547E-06, 1.115196417914451E+00, -9.073283355045334E-02),
        doubleArrayOf(6.651880335792781E-06, 6.006455934130108E+00, -2.179810910879777E-01),
        doubleArrayOf(6.570135525528793E-06, 4.204090416089263E+00, -5.141823861693731E-01),
        doubleArrayOf(6.551076448609189E-06, 3.000611634877713E+00, -8.698752864575016E-01),
        doubleArrayOf(6.445693456193227E-06, 3.926888399861492E+00, 3.149298938680800E-01),
        doubleArrayOf(6.221700343295405E-06, 1.508906807682454E+00, -6.129161511965049E-01),
        doubleArrayOf(-6.210141308502555E-06, 5.467206194483361E+00, 4.918592166572376E-01),
        doubleArrayOf(6.128034935351225E-06, 2.838342947686692E+00, -1.373354651548260E+00),
        doubleArrayOf(5.752222076851271E-06, 5.096908506454503E+00, -7.116499162236366E-01),
        doubleArrayOf(5.720920731236437E-06, 3.054280264694361E-01, -9.686090514846332E-01),
        doubleArrayOf(5.708438487292833E-06, 4.383088440588454E+00, 6.804333341113080E-01),
        doubleArrayOf(-5.618758010321190E-06, 4.332900428655887E+00, -1.392975731458459E+00),
        doubleArrayOf(5.586433308293342E-06, 6.280805296580291E+00, -1.284431426476228E+00),
        doubleArrayOf(5.455429436394752E-06, 9.573554290008455E-01, 1.282646464229199E+00),
        doubleArrayOf(5.234563142340540E-06, 2.401724898047694E+00, -8.103836812507684E-01),
        doubleArrayOf(-5.207388474970705E-06, 4.296952902336882E+00, 7.788650730862894E-04),
        doubleArrayOf(5.204438871550596E-06, 2.418454235357329E+00, -1.192473260608459E-01),
        doubleArrayOf(5.046334690770730E-06, 2.893440714256877E+00, -1.058940502818596E-02),
        doubleArrayOf(-5.035619025711394E-06, 5.839277492300028E+00, -1.081663712904294E-02),
        doubleArrayOf(4.976902542853610E-06, 3.893429725242215E+00, -1.067342816511765E+00),
        doubleArrayOf(4.951970227904307E-06, 1.978983531940741E+00, 2.063855888858485E-01),
        doubleArrayOf(-4.950572722250481E-06, 4.320598789629788E+00, -2.072534237779716E-01),
        doubleArrayOf(4.864691847876214E-06, 1.431593392813457E-01, -1.472088416575392E+00),
        doubleArrayOf(-4.835154150013290E-06, 2.223783275489257E-01, 1.067346965038102E-01),
        doubleArrayOf(4.711996026340074E-06, 5.989726596820463E+00, -9.091174462779002E-01),
        doubleArrayOf(4.647700174472706E-06, 8.781364407369228E-01, -2.961766488500037E-01),
        doubleArrayOf(-4.611548111165785E-06, 1.398883233285187E+00, 9.031674882013382E-03),
        doubleArrayOf(-4.463536929701217E-06, 1.637716820250533E+00, -1.491709496485591E+00),
        doubleArrayOf(4.448389546383381E-06, 4.344720011328309E+00, 8.804442781156406E-03),
        doubleArrayOf(4.423835447429758E-06, 3.652539037413501E+00, 1.381380229256330E+00),
        doubleArrayOf(4.297419749912965E-06, 1.198246116836868E+00, -1.166076581538897E+00),
        doubleArrayOf(4.206030088019200E-06, 3.294542988413657E+00, -1.007851211305032E+00),
        doubleArrayOf(-4.153052701221075E-06, 7.210106937409027E-01, 7.360328685412315E-03),
        doubleArrayOf(-4.097668847161432E-06, 2.888530959617075E+00, 7.000544140215073E-01),
        doubleArrayOf(3.894866749084315E-06, 3.635809700103133E+00, 6.902438740664076E-01),
        doubleArrayOf(-3.876996622725754E-06, 2.272914885477417E+00, 6.840966403831800E-02),
        doubleArrayOf(3.843534248051829E-06, 3.585621688174942E+00, -1.383165191503360E+00),
        doubleArrayOf(3.842326107820616E-06, 3.731161038055586E+00, -1.570822181602524E+00),
        doubleArrayOf(3.724216491694377E-06, 5.993593800083099E-01, -1.106584976332164E+00),
        doubleArrayOf(3.699201962974299E-06, 4.786247815611109E+00, -1.264810346566029E+00),
        doubleArrayOf(3.608611557983843E-06, 2.609753898886272E+00, -1.103539134606527E-01),
        doubleArrayOf(-3.538397263248576E-06, 5.225718519024773E+00, -1.590443261512723E+00),
        doubleArrayOf(3.531502882337352E-06, 6.453733863926071E-02, 1.480113994283462E+00),
        doubleArrayOf(3.397352921611207E-06, 4.482867476818612E+00, 2.962259413127871E-01),
        doubleArrayOf(-3.113267226101596E-06, 4.965245840107813E+00, -7.019462628534756E-02),
        doubleArrayOf(-2.954159247722890E-06, 2.957566593162261E+00, -1.143316058513110E-03),
        doubleArrayOf(2.885109484385860E-06, 3.947295643644445E+00, 6.086273772565560E-04),
        doubleArrayOf(2.832059006428450E-06, 2.402892932765738E+00, 1.192608522513110E-03),
        doubleArrayOf(-2.683799905115401E-06, 1.716935808520376E+00, 8.711361659361089E-02),
        doubleArrayOf(2.540204369555767E-06, 4.072427749742135E+00, 1.272729037689160E-01),
        doubleArrayOf(-2.530442498805404E-06, 9.696570680345386E-01, 9.692415654871056E-02),
        doubleArrayOf(2.471798836623408E-06, 1.185944477806245E+00, 1.964572614159102E-02),
        doubleArrayOf(-2.393809826972641E-06, 3.063101285835501E+00, 1.781147143177812E-02),
        doubleArrayOf(2.322013871583706E-06, 3.165732975843386E+00, -1.290578660159456E-01),
        doubleArrayOf(-2.252919673323646E-06, 9.946448142775208E-01, 1.075231354139779E-02),
        doubleArrayOf(-2.040203000028772E-06, 4.773946176578943E+00, -7.908803888554077E-02),
        doubleArrayOf(1.978250600690922E-06, 5.022592550872615E+00, 1.047578897775747E-02),
        doubleArrayOf(-1.817067803131338E-06, 2.941207823248317E+00, -2.563827320115845E-03),
        doubleArrayOf(-1.760241959810971E-06, 5.496237170821872E+00, -2.726735833327680E-03),
        doubleArrayOf(1.708248552006820E-06, 2.739053275877476E+00, -1.079200076232469E-02),
        doubleArrayOf(-1.707373996050395E-06, 5.993664930679350E+00, -1.061404139490420E-02),
        doubleArrayOf(1.638819397704571E-06, 4.499107449707681E+00, 9.007038515295140E-03),
        doubleArrayOf(-1.594635824936384E-06, 1.244495794905822E+00, 8.829079147874648E-03),
        doubleArrayOf(-1.549153538011889E-06, 4.175059439749656E+00, -1.959643367880767E-02),
        doubleArrayOf(-1.541304650138550E-06, 2.495897990714715E+00, -6.086273772565560E-04),
        doubleArrayOf(1.506189225615625E-06, 1.113991811579792E+00, 7.542287063680470E-04),
        doubleArrayOf(1.387762233686380E-06, 4.153420347799528E+00, -8.896981903681528E-05),
        doubleArrayOf(-1.337929891212176E-06, 2.339647246647041E-01, -9.145290932441870E-03),
        doubleArrayOf(1.284796105929756E-06, 3.679176774285410E-01, -8.092229359535366E-02),
        doubleArrayOf(1.274183971113668E-06, 2.489202295249182E+00, -8.868766368801548E-03),
        doubleArrayOf(-1.083761451668346E-06, 2.464214549006287E+00, 7.730307663851121E-02),
        doubleArrayOf(-1.082213439029503E-06, 4.386657373204058E-01, 2.945626609669069E-02),
        doubleArrayOf(1.074660210279234E-06, 6.052216247778919E+00, -2.143068838862057E-02),
        doubleArrayOf(-1.060143973086585E-06, 5.235531306351518E+00, -1.382622818201617E-04),
        doubleArrayOf(-1.050457836219505E-06, 5.297110302121204E-01, -1.533083914780906E-03),
        doubleArrayOf(1.016870649805557E-06, 1.862475158400216E+00, -1.005433735055530E-01),
        doubleArrayOf(9.272030248231280E-07, 2.193929082762471E+00, 7.246712634983826E-03),
        doubleArrayOf(8.522968078631408E-07, 2.315822545349735E+00, 2.762201138687778E-02),
        doubleArrayOf(-7.513552848257232E-07, 5.163096810852366E-01, -3.124122834372025E-02),
        doubleArrayOf(-7.452690477984808E-07, 4.217967099621830E+00, -6.038408633024787E-02),
        doubleArrayOf(-6.901926058355343E-07, 6.122964307670780E+00, 8.894787130342378E-02),
    )

    private val HYPERION_2 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), HYPERION_2_0),
    )

    private val HYPERION_3_0 = arrayOf(
        doubleArrayOf(5.948511882002843E-03, 4.694475827075523E+00, -1.136160504284885E-04),
        doubleArrayOf(1.535891024624297E-03, 3.859634729686247E-02, -2.463636671824243E-05),
        doubleArrayOf(1.572522677808419E-04, 6.085851736842245E+00, 1.167962290513110E-03),
        doubleArrayOf(-1.497345442720214E-04, 2.543684898509682E+00, -1.671346196601068E-03),
        doubleArrayOf(-4.911846033201391E-05, 1.605106494495041E+00, -7.542188416946162E-04),
        doubleArrayOf(-2.465654101797190E-05, 1.954530936499135E+00, -5.839811452565560E-04),
        doubleArrayOf(-2.213337603547740E-05, 4.211325867661734E+00, 1.751943435769670E-03),
        doubleArrayOf(2.189031408512060E-05, 5.633054231090162E+00, -1.030743405334940E-03),
        doubleArrayOf(1.598165380887390E-05, 4.295054520089123E+00, 5.839811452565560E-04),
        doubleArrayOf(1.496627946379793E-05, 4.016603287531243E+00, -1.784962247029556E-03),
        doubleArrayOf(1.404188064697451E-05, 1.796406158023830E+00, 8.139193758498604E-03),
        doubleArrayOf(-1.314294835383983E-05, 3.755897423060887E+00, 8.035113044779626E-04),
        doubleArrayOf(-9.122789187630200E-06, 3.498933647377842E+00, -1.167962290513110E-03),
        doubleArrayOf(8.389523097875214E-06, 3.290963638995533E+00, -1.148188615170074E-02),
        doubleArrayOf(7.467530692297956E-06, 6.665280904804041E-01, 1.629085132118349E-04),
        doubleArrayOf(-4.916203322979170E-06, 1.106474128302829E+00, 9.862014897670330E-02),
        doubleArrayOf(4.509531977488889E-06, 3.078024883516604E+00, -8.678348921231047E-04),
        doubleArrayOf(-4.145588215282021E-06, 9.163790711087549E-01, -1.760325880311314E-03),
        doubleArrayOf(3.982965022125390E-06, 1.480712538988318E+00, 1.281578356513110E-03),
        doubleArrayOf(3.839542395242535E-06, 1.999292218668653E+00, -9.884738107756028E-02),
        doubleArrayOf(3.566024262476873E-06, 5.441754567561372E+00, -9.924156005528160E-03),
        doubleArrayOf(3.148398194156444E-06, 5.587293917441577E+00, -1.975811461046921E-01),
        doubleArrayOf(3.030592492776347E-06, 5.238868506916578E+00, 9.706241883053072E-02),
        doubleArrayOf(-2.682452719829683E-06, 4.160135842068596E+00, -9.171273549064512E-04),
        doubleArrayOf(-2.169784851229061E-06, 3.801657736709643E+00, 1.973539140038351E-01),
        doubleArrayOf(-2.004460730134680E-06, 6.131686597281963E+00, -1.004051112237328E-01),
        doubleArrayOf(1.834573960516513E-06, 1.650866808143945E+00, 1.957961838576625E-01),
        doubleArrayOf(1.780425037193046E-06, 2.892110309034762E+00, -2.963149111318239E-01),
        doubleArrayOf(-1.562250771899869E-06, 3.269324547045398E+00, 8.025577708070115E-03),
        doubleArrayOf(-1.398643272650284E-06, 2.139446479501969E+00, 4.929246278334639E-05),
        doubleArrayOf(-1.272802367749216E-06, 6.260985974273701E+00, -8.431985254048623E-04),
        doubleArrayOf(1.255563077270305E-06, 2.282979034039322E+00, 9.171273549064512E-04),
        doubleArrayOf(1.221711462471724E-06, 1.049127417537954E+00, 1.794973371359828E-02),
        doubleArrayOf(-1.189056338328650E-06, 5.384407856796491E+00, -9.059457126863318E-02),
        doubleArrayOf(1.144957852083430E-06, 6.152650159259904E+00, 1.192598652513110E-03),
        doubleArrayOf(-1.121833101144503E-06, 5.986147247402415E+00, 8.725187887543105E-02),
        doubleArrayOf(1.116583047991100E-06, 4.491589766430666E+00, 1.068729587856304E-01),
        doubleArrayOf(-8.876506818660424E-07, 4.763882028017109E+00, -1.159550220212923E-02),
        doubleArrayOf(8.308837128254335E-07, 2.352385234980906E+00, -1.056475879679429E-02),
        doubleArrayOf(8.288384247459950E-07, 5.957800305884270E-01, -1.102156511788325E-01),
        doubleArrayOf(7.816180007614254E-07, 6.167394216097088E+00, -2.272321008569770E-04),
        doubleArrayOf(-7.628434629767395E-07, 1.853752868788740E+00, 8.880960902160362E-02),
        doubleArrayOf(7.436509468905391E-07, 3.199918346103792E+00, 1.950746385977086E-02),
        doubleArrayOf(-7.276927363562636E-07, 6.189033308047209E+00, -1.973469596062783E-02),
        doubleArrayOf(7.144299265706248E-07, 9.771747513115055E-01, -9.417637216246936E-04),
        doubleArrayOf(6.925549696724206E-07, 8.578277540091867E-01, 9.056321113405056E-03),
        doubleArrayOf(6.584378044173228E-07, 3.482263302524318E+00, -2.588473551507519E-03),
        doubleArrayOf(-6.290271131270627E-07, 4.885775490604302E+00, 8.779796549764733E-03),
        doubleArrayOf(-5.558619276909469E-07, 4.038242379481365E+00, -2.129242610680041E-02),
        doubleArrayOf(5.420684261778156E-07, 5.916741046460914E+00, 9.873376502713178E-02),
        doubleArrayOf(-4.533375266393237E-07, 9.714766439641385E-02, -1.084128336043461E-02),
        doubleArrayOf(4.270467112203478E-07, 1.691003306229041E-01, 8.050214074788358E-03),
        doubleArrayOf(-4.241248416918695E-07, 5.263738296470040E-01, -9.873376502713178E-02),
        doubleArrayOf(4.217192739334928E-07, 4.300290102902023E+00, 9.797954618543716E-02),
        doubleArrayOf(-4.111285058745586E-07, 3.968836178539811E+00, -9.810539955099672E-03),
        doubleArrayOf(3.911603306295834E-07, 4.503176163546744E+00, -9.007028650621710E-03),
        doubleArrayOf(3.588448340600037E-07, 1.252013478182742E+00, -8.903684112246060E-02),
        doubleArrayOf(-3.517973623230076E-07, 5.383203250461813E+00, 8.924909881882087E-04),
    )

    private val HYPERION_3 = arrayOf(
        Term(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0), HYPERION_3_0),
    )

    private val SATS = arrayOf(
        // Mimas
        Sat(
            8.457558957423141E-08,
            6.667061728781588E+00,
            doubleArrayOf(
                -5.196910356411064E-03,
                1.822484926062486E-01,
                0.000000000000000E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
            ),
            arrayOf(MIMAS_0, MIMAS_1, MIMAS_2, MIMAS_3),
        ),
        // Enceladus
        Sat(
            8.457559689847700E-08,
            4.585536751533733E+00,
            doubleArrayOf(
                -3.147075653259473E-03,
                7.997716657090215E-01,
                0.000000000000000E+00,
                0.000000000000000E+00,
                -4.817894243161427E-06,
                -3.858288993746540E-07,
            ),
            arrayOf(ENCELADUS_0, ENCELADUS_1, ENCELADUS_2, ENCELADUS_3),
        ),
        // Tethys
        Sat(
            8.457567386225863E-08,
            3.328306445054557E+00,
            doubleArrayOf(
                -2.047958691903563E-03,
                5.239109365414447E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
                -1.603681402396474E-05,
                -1.284293647967145E-06,
            ),
            arrayOf(TETHYS_0, TETHYS_1, TETHYS_2, TETHYS_3),
        ),
        // Dione
        Sat(
            8.457575023401118E-08,
            2.295717646432711E+00,
            doubleArrayOf(
                -1.245046723085128E-03,
                1.994592585279060E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
                -5.892691888978987E-05,
                -4.719091057203836E-06,
            ),
            arrayOf(DIONE_0, DIONE_1, DIONE_2, DIONE_3),
        ),
        // Rhea
        Sat(
            8.457594957866317E-08,
            1.390853715957114E+00,
            doubleArrayOf(
                -6.263338154589970E-04,
                6.221340947932125E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
                -4.193141432895800E-04,
                -3.357667186781713E-05,
            ),
            arrayOf(RHEA_0, RHEA_1, RHEA_2, RHEA_3),
        ),
        // Titan
        Sat(
            8.459559800923616E-08,
            3.940425676910059E-01,
            doubleArrayOf(
                -1.348089930929860E-04,
                4.936792168079816E+00,
                -1.249553518183080E-06,
                2.792373338061224E-06,
                -5.584488311754492E-03,
                -4.471842487301890E-04,
            ),
            arrayOf(TITAN_0, TITAN_1, TITAN_2, TITAN_3),
        ),
        // Iapetus
        Sat(
            8.457584639645043E-08,
            7.920197763192791E-02,
            doubleArrayOf(
                -4.931880677088688E-04,
                1.661250302251527E-01,
                3.904890046320212E-04,
                -9.380651872794318E-04,
                -1.315950341063651E-01,
                -1.054097030879299E-02,
            ),
            arrayOf(IAPETUS_0, IAPETUS_1, IAPETUS_2, IAPETUS_3),
        ),
        // Hyperion
        Sat(
            8.457558674940690E-08,
            2.953088138695055E-01,
            doubleArrayOf(
                -1.574686065780747E-03,
                2.250358656361423E+00,
                0.000000000000000E+00,
                0.000000000000000E+00,
                -4.939409467982673E-03,
                -3.958228521883369E-04,
            ),
            arrayOf(HYPERION_0, HYPERION_1, HYPERION_2, HYPERION_3),
        ),
    )
}