import br.tiagohm.astrum.tools.catalog.StellariumNebulaCatalogue
import org.junit.jupiter.api.Test
import java.io.File

class CatalogueTest {

    @Test
    fun nebula() {
        val data = StellariumNebulaCatalogue.load(File(System.getProperty("user.home") + "/catalog.dat"))
        val ngc4565 = data.find { it.ngc == 4565 }!!
        System.err.println(ngc4565)
    }
}