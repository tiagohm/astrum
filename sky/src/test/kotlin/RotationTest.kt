import br.tiagohm.astrum.common.units.angle.Degrees
import br.tiagohm.astrum.sky.core.rotation.Rotation
import org.junit.jupiter.api.Test

class RotationTest {

    @Test
    fun obliquityAndAscendingNode() {
        System.err.println(Rotation.compute(Degrees(286.13), Degrees(63.87))) // Sun
        System.err.println(Rotation.compute(Degrees(281.0103), Degrees(61.4155))) // Mercury
        System.err.println(Rotation.compute(Degrees(272.76), Degrees(67.16))) // Venus
        System.err.println(Rotation.compute(Degrees(0.0), Degrees(90.0))) // Earth
        System.err.println(Rotation.compute(Degrees(317.269202), Degrees(54.432516))) // Mars
        System.err.println(Rotation.compute(Degrees(268.056595), Degrees(64.495303))) // Jupiter
        System.err.println(Rotation.compute(Degrees(40.589), Degrees(83.537))) // Saturn
        System.err.println(Rotation.compute(Degrees(257.311), Degrees(-15.175))) // Uranus
        System.err.println(Rotation.compute(Degrees(299.36), Degrees(43.46))) // Neptune
        System.err.println(Rotation.compute(Degrees(132.993), Degrees(-6.163))) // Pluto
        System.err.println(Rotation.compute(Degrees(269.9949), Degrees(66.5392))) // Moon
        System.err.println(Rotation.compute(Degrees(317.67071657), Degrees(52.88627266))) // Phobos
        System.err.println(Rotation.compute(Degrees(316.65705808), Degrees(53.50992033))) // Deimos
        System.err.println(Rotation.compute(Degrees(268.05), Degrees(64.50))) // Io
        System.err.println(Rotation.compute(Degrees(268.08), Degrees(64.51))) // Europa
        System.err.println(Rotation.compute(Degrees(268.20), Degrees(64.57))) // Ganymede
        System.err.println(Rotation.compute(Degrees(268.72), Degrees(64.83))) // Callisto
    }
}