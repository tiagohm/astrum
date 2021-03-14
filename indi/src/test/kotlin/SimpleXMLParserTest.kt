import br.tiagohm.astrum.indi.client.parser.SimpleXMLParser
import br.tiagohm.astrum.indi.client.parser.XMLTag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SimpleXMLParserTest {

    @Test
    fun parse() {
        val parser = SimpleXMLParser()

        parser.parse("<a />").also {
            assertEquals("a", it.name)
        }

        parser.parse("""<a p="n" />""").also {
            assertEquals("a", it.name)
            assertEquals(1, it.attributes.size)
            assertEquals("n", it.attributes["p"])
        }

        parser.parse("""<a p="n" q="m" />""").also {
            assertEquals("a", it.name)
            assertEquals(2, it.attributes.size)
            assertEquals("n", it.attributes["p"])
            assertEquals("m", it.attributes["q"])
        }

        parser.parse("<a>t</a>").also {
            assertEquals("a", it.name)
            assertEquals("t", it.text)
            assertEquals(0, it.attributes.size)
        }

        parser.parse("""< a b = "c" >t</ a >""").also {
            assertEquals("a", it.name)
            assertEquals("t", it.text)
            assertEquals(1, it.attributes.size)
            assertEquals("c", it.attributes["b"])
        }
    }

    @Test
    fun parseINDI() {
        val parser = SimpleXMLParser()

        parser.parse("""<delProperty name="Telescope Simulator"/>""").also {
            assertEquals("delProperty", it.name)
            assertEquals("", it.text)
            assertEquals(0, it.children.size)
            assertEquals(1, it.attributes.size)
            assertEquals("Telescope Simulator", it.attributes["name"])
        }

        parser.parse(
            """
        <defSwitchVector device="Rotator Simulator" name="CONFIG_PROCESS" label="Configuration"
         group="Options" state="Alert" perm="rw" rule="AtMostOne" timeout="0"
         timestamp="2021-03-11T03:32:18">
          <defSwitch name="CONFIG_LOAD" label="Load">Off</defSwitch>
          <defSwitch name="CONFIG_SAVE" label="Save">Off</defSwitch>
          <defSwitch name="CONFIG_DEFAULT" label="Default">Off</defSwitch>
          <defSwitch name="CONFIG_PURGE" label="Purge">Off</defSwitch>
          </defSwitchVector>
         <defSwitchVector device="Telescope Simulator" name="CONFIG_PROCESS" label="Configuration"
         group="Options" state="Alert" perm="rw" rule="AtMostOne" timeout="0"
         timestamp="2021-03-11T03:32:18">
          <defSwitch name="CONFIG_LOAD" label="Load">Off</defSwitch>
          <defSwitch name="CONFIG_SAVE" label="Save">Off</defSwitch>
          <defSwitch name="CONFIG_DEFAULT" label="Default">Off</defSwitch>
          <defSwitch name="CONFIG_PURGE" label="Purge">Off</defSwitch>
          </defSwitchVector>
        """.trimIndent()
        ).also {
            assertEquals("defSwitchVector", it.name)
            assertEquals("", it.text)
            assertEquals(4, it.children.size)
            assertEquals(9, it.attributes.size)
            assertEquals("Telescope Simulator", it.attributes["device"])
            assertEquals("CONFIG_PROCESS", it.attributes["name"])
            assertEquals("Configuration", it.attributes["label"])
            assertEquals("Options", it.attributes["group"])
            assertEquals("Alert", it.attributes["state"])
            assertEquals("rw", it.attributes["perm"])
            assertEquals("AtMostOne", it.attributes["rule"])
            assertEquals("0", it.attributes["timeout"])
            assertEquals("2021-03-11T03:32:18", it.attributes["timestamp"])

            assertEquals("defSwitch", it.children[0].name)
            assertEquals(2, it.children[0].attributes.size)
            assertEquals("CONFIG_LOAD", it.children[0].attributes["name"])
            assertEquals("Load", it.children[0].attributes["label"])
            assertEquals("Off", it.children[0].text)

            assertEquals("defSwitch", it.children[1].name)
            assertEquals(2, it.children[1].attributes.size)
            assertEquals("CONFIG_SAVE", it.children[1].attributes["name"])
            assertEquals("Save", it.children[1].attributes["label"])
            assertEquals("Off", it.children[1].text)

            assertEquals("defSwitch", it.children[2].name)
            assertEquals(2, it.children[2].attributes.size)
            assertEquals("CONFIG_DEFAULT", it.children[2].attributes["name"])
            assertEquals("Default", it.children[2].attributes["label"])
            assertEquals("Off", it.children[2].text)

            assertEquals("defSwitch", it.children[3].name)
            assertEquals(2, it.children[3].attributes.size)
            assertEquals("CONFIG_PURGE", it.children[3].attributes["name"])
            assertEquals("Purge", it.children[3].attributes["label"])
            assertEquals("Off", it.children[3].text)
        }

        parser.parse("""
            <defSwitchVector device="Rotator Simulator" name="CONNECTION" label="Connection" group="Main Control" state="Idle" perm="rw" rule="OneOfMany" timeout="60" timestamp="2021-03-13T22:51:10">
                <defSwitch name="CONNECT" label="Connect">
            Off
                </defSwitch>
                <defSwitch name="DISCONNECT" label="Disconnect">
            On
                </defSwitch>
            </defSwitchVector>
        """.trimIndent())
    }

    companion object {

        private fun SimpleXMLParser.parse(xml: String): XMLTag {
            var a: XMLTag? = null
            xml.forEach { parse(it)?.also { tag -> a = tag } }
            return a!!
        }
    }
}