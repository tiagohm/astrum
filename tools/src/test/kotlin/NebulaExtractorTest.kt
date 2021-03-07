import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.nebula.NebulaType
import br.tiagohm.astrum.tools.extractor.stellarium.NebulaExtractor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class NebulaExtractorTest {

    @Test
    fun extract() {
        val file = File(System.getProperty("user.home") + "/catalog.standard.dat")
        val (_, edition, data) = NebulaExtractor.extract(file)

        if (edition == "standard") assertTrue(data.size >= 94671)

        val andromeda = data.find { it.m == 31 }!!

        assertEquals(224, andromeda.ngc)
        assertEquals(2557, andromeda.pgc)
        assertEquals(454, andromeda.ugc)
        assertEquals(NebulaType.GALAXY, andromeda.nebulaType)
        assertEquals(Degrees(45.0), andromeda.orientation)
        assertEquals(22.24, andromeda.surfaceBrightness, 0.01)
        assertEquals(3.44, andromeda.vMag, 0.01)
        assertTrue(andromeda.names.contains("Andromeda Galaxy"))
    }
}