package br.tiagohm.astrum.sky.constellations

import java.util.*
import kotlin.collections.ArrayList

data class ConstellationLine(
    val raLow: Double,
    val raHigh: Double,
    val decLow: Double,
    val constellation: Constellation,
) {

    companion object {

        val lines by lazy { load() }

        private fun parseHMS(hms: String, declination: Boolean = false): Double {
            return hms.split(":").let {
                if (!declination) it[0].toInt() + it[1].toInt() / 60.0 + it[2].toInt() / 3600.0
                else it[0].toInt().let { h -> (it[1].toInt() / 60.0).let { m -> if (h < 0) h - m else h + m } }
            }
        }

        private fun load(): List<ConstellationLine> {
            val res = ArrayList<ConstellationLine>(357)

            for (span in SPANS) {
                val raLow = parseHMS(span[0] as String)
                val raHigh = parseHMS(span[1] as String)
                val decLow = parseHMS(span[2] as String, true)
                val constellation = span[3] as Constellation

                res.add(ConstellationLine(raLow, raHigh, decLow, constellation))
            }

            return Collections.unmodifiableList(res)
        }

        private val SPANS = arrayOf(
            arrayOf("0:00:00", "24:00:00", "88:00", Constellation.UMI),
            arrayOf("8:00:00", "14:30:00", "86:30", Constellation.UMI),
            arrayOf("21:00:00", "23:00:00", "86:10", Constellation.UMI),
            arrayOf("18:00:00", "21:00:00", "86:00", Constellation.UMI),
            arrayOf("0:00:00", "8:00:00", "85:00", Constellation.CEP),
            arrayOf("9:10:00", "10:40:00", "82:00", Constellation.CAM),
            arrayOf("0:00:00", "5:00:00", "80:00", Constellation.CEP),
            arrayOf("10:40:00", "14:30:00", "80:00", Constellation.CAM),
            arrayOf("17:30:00", "18:00:00", "80:00", Constellation.UMI),
            arrayOf("20:10:00", "21:00:00", "80:00", Constellation.DRA),
            arrayOf("0:00:00", "3:30:30", "77:00", Constellation.CEP),
            arrayOf("11:30:00", "13:35:00", "77:00", Constellation.CAM),
            arrayOf("16:32:00", "17:30:00", "75:00", Constellation.UMI),
            arrayOf("20:10:00", "20:40:00", "75:00", Constellation.CEP),
            arrayOf("7:58:00", "9:10:00", "73:30", Constellation.CAM),
            arrayOf("9:10:00", "11:20:00", "73:30", Constellation.DRA),
            arrayOf("13:00:00", "16:32:00", "70:00", Constellation.UMI),
            arrayOf("3:06:00", "3:25:00", "68:00", Constellation.CAS),
            arrayOf("20:25:00", "20:40:00", "67:00", Constellation.DRA),
            arrayOf("11:20:00", "12:00:00", "66:30", Constellation.DRA),
            arrayOf("0:00:00", "0:20:00", "66:00", Constellation.CEP),
            arrayOf("14:00:00", "15:40:00", "66:00", Constellation.UMI),
            arrayOf("23:35:00", "24:00:00", "66:00", Constellation.CEP),
            arrayOf("12:00:00", "13:30:00", "64:00", Constellation.DRA),
            arrayOf("13:30:00", "14:25:00", "63:00", Constellation.DRA),
            arrayOf("23:10:00", "23:35:00", "63:00", Constellation.CEP),
            arrayOf("6:06:00", "7:00:00", "62:00", Constellation.CAM),
            arrayOf("20:00:00", "20:25:00", "61:30", Constellation.DRA),
            arrayOf("20:32:12", "20:36:00", "60:55", Constellation.CEP),
            arrayOf("7:00:00", "7:58:00", "60:00", Constellation.CAM),
            arrayOf("7:58:00", "8:25:00", "60:00", Constellation.UMA),
            arrayOf("19:46:00", "20:00:00", "59:30", Constellation.DRA),
            arrayOf("20:00:00", "20:32:12", "59:30", Constellation.CEP),
            arrayOf("22:52:00", "23:10:00", "59:05", Constellation.CEP),
            arrayOf("0:00:00", "2:26:00", "58:30", Constellation.CAS),
            arrayOf("19:25:00", "19:46:00", "58:00", Constellation.DRA),
            arrayOf("1:42:00", "1:54:30", "57:30", Constellation.CAS),
            arrayOf("2:26:00", "3:06:00", "57:00", Constellation.CAS),
            arrayOf("3:06:00", "3:10:00", "57:00", Constellation.CAM),
            arrayOf("22:19:00", "22:52:00", "56:15", Constellation.CEP),
            arrayOf("5:00:00", "6:06:00", "56:00", Constellation.CAM),
            arrayOf("14:02:00", "14:25:00", "55:30", Constellation.UMA),
            arrayOf("14:25:00", "19:25:00", "55:30", Constellation.DRA),
            arrayOf("3:10:00", "3:20:00", "55:00", Constellation.CAM),
            arrayOf("22:08:00", "22:19:00", "55:00", Constellation.CEP),
            arrayOf("20:36:00", "21:58:00", "54:50", Constellation.CEP),
            arrayOf("0:00:00", "1:42:00", "54:00", Constellation.CAS),
            arrayOf("6:06:00", "6:30:00", "54:00", Constellation.LYN),
            arrayOf("12:05:00", "13:30:00", "53:00", Constellation.UMA),
            arrayOf("15:15:00", "15:45:00", "53:00", Constellation.DRA),
            arrayOf("21:58:00", "22:08:00", "52:45", Constellation.CEP),
            arrayOf("3:20:00", "5:00:00", "52:30", Constellation.CAM),
            arrayOf("22:52:00", "23:20:00", "52:30", Constellation.CAS),
            arrayOf("15:45:00", "17:00:00", "51:30", Constellation.DRA),
            arrayOf("2:02:30", "2:31:00", "50:30", Constellation.PER),
            arrayOf("17:00:00", "18:14:00", "50:30", Constellation.DRA),
            arrayOf("0:00:00", "1:22:00", "50:00", Constellation.CAS),
            arrayOf("1:22:00", "1:40:00", "50:00", Constellation.PER),
            arrayOf("6:30:00", "6:48:00", "50:00", Constellation.LYN),
            arrayOf("23:20:00", "24:00:00", "50:00", Constellation.CAS),
            arrayOf("13:30:00", "14:02:00", "48:30", Constellation.UMA),
            arrayOf("0:00:00", "1:07:00", "48:00", Constellation.CAS),
            arrayOf("23:35:00", "24:00:00", "48:00", Constellation.CAS),
            arrayOf("18:10:30", "18:14:00", "47:30", Constellation.HER),
            arrayOf("18:14:00", "19:05:00", "47:30", Constellation.DRA),
            arrayOf("19:05:00", "19:10:00", "47:30", Constellation.CYG),
            arrayOf("1:40:00", "2:02:30", "47:00", Constellation.PER),
            arrayOf("8:25:00", "9:10:00", "47:00", Constellation.UMA),
            arrayOf("0:10:00", "0:52:00", "46:00", Constellation.CAS),
            arrayOf("12:00:00", "12:05:00", "45:00", Constellation.UMA),
            arrayOf("6:48:00", "7:22:00", "44:30", Constellation.LYN),
            arrayOf("21:54:30", "21:58:00", "44:00", Constellation.CYG),
            arrayOf("21:52:30", "21:54:30", "43:45", Constellation.CYG),
            arrayOf("19:10:00", "19:24:00", "43:30", Constellation.CYG),
            arrayOf("9:10:00", "10:10:00", "42:00", Constellation.UMA),
            arrayOf("10:10:00", "10:47:00", "40:00", Constellation.UMA),
            arrayOf("15:26:00", "15:45:00", "40:00", Constellation.BOO),
            arrayOf("15:45:00", "16:20:00", "40:00", Constellation.HER),
            arrayOf("9:15:00", "9:35:00", "39:45", Constellation.LYN),
            arrayOf("0:00:00", "2:31:00", "36:45", Constellation.AND),
            arrayOf("2:31:00", "2:34:00", "36:45", Constellation.PER),
            arrayOf("19:21:30", "19:24:00", "36:30", Constellation.LYR),
            arrayOf("4:30:00", "4:41:30", "36:00", Constellation.PER),
            arrayOf("21:44:00", "21:52:30", "36:00", Constellation.CYG),
            arrayOf("21:52:30", "22:00:00", "36:00", Constellation.LAC),
            arrayOf("6:32:00", "7:22:00", "35:30", Constellation.AUR),
            arrayOf("7:22:00", "7:45:00", "35:30", Constellation.LYN),
            arrayOf("0:00:00", "2:00:00", "35:00", Constellation.AND),
            arrayOf("22:00:00", "22:49:00", "35:00", Constellation.LAC),
            arrayOf("22:49:00", "22:52:00", "34:30", Constellation.LAC),
            arrayOf("22:52:00", "23:30:00", "34:30", Constellation.AND),
            arrayOf("2:34:00", "2:43:00", "34:00", Constellation.PER),
            arrayOf("10:47:00", "11:00:00", "34:00", Constellation.UMA),
            arrayOf("12:00:00", "12:20:00", "34:00", Constellation.CVN),
            arrayOf("7:45:00", "9:15:00", "33:30", Constellation.LYN),
            arrayOf("9:15:00", "9:53:00", "33:30", Constellation.LMI),
            arrayOf("0:43:00", "1:24:30", "33:00", Constellation.AND),
            arrayOf("15:11:00", "15:26:00", "33:00", Constellation.BOO),
            arrayOf("23:30:00", "23:45:00", "32:05", Constellation.AND),
            arrayOf("12:20:00", "13:15:00", "32:00", Constellation.CVN),
            arrayOf("23:45:00", "24:00:00", "31:20", Constellation.AND),
            arrayOf("13:57:30", "14:02:00", "30:45", Constellation.CVN),
            arrayOf("2:25:00", "2:43:00", "30:40", Constellation.TRI),
            arrayOf("2:43:00", "4:30:00", "30:40", Constellation.PER),
            arrayOf("4:30:00", "4:45:00", "30:00", Constellation.AUR),
            arrayOf("18:10:30", "19:21:30", "30:00", Constellation.LYR),
            arrayOf("11:00:00", "12:00:00", "29:00", Constellation.UMA),
            arrayOf("19:40:00", "20:55:00", "29:00", Constellation.CYG),
            arrayOf("4:45:00", "5:53:00", "28:30", Constellation.AUR),
            arrayOf("9:53:00", "10:30:00", "28:30", Constellation.LMI),
            arrayOf("13:15:00", "13:57:30", "28:30", Constellation.CVN),
            arrayOf("0:00:00", "0:04:00", "28:00", Constellation.AND),
            arrayOf("1:24:30", "1:40:00", "28:00", Constellation.TRI),
            arrayOf("5:53:00", "6:32:00", "28:00", Constellation.AUR),
            arrayOf("7:53:00", "8:00:00", "28:00", Constellation.GEM),
            arrayOf("20:55:00", "21:44:00", "28:00", Constellation.CYG),
            arrayOf("19:15:30", "19:40:00", "27:30", Constellation.CYG),
            arrayOf("1:55:00", "2:25:00", "27:15", Constellation.TRI),
            arrayOf("16:10:00", "16:20:00", "27:00", Constellation.CRB),
            arrayOf("15:05:00", "15:11:00", "26:00", Constellation.BOO),
            arrayOf("15:11:00", "16:10:00", "26:00", Constellation.CRB),
            arrayOf("18:22:00", "18:52:00", "26:00", Constellation.LYR),
            arrayOf("10:45:00", "11:00:00", "25:30", Constellation.LMI),
            arrayOf("18:52:00", "19:15:30", "25:30", Constellation.LYR),
            arrayOf("1:40:00", "1:55:00", "25:00", Constellation.TRI),
            arrayOf("0:43:00", "0:51:00", "23:45", Constellation.PSC),
            arrayOf("10:30:00", "10:45:00", "23:30", Constellation.LMI),
            arrayOf("21:15:00", "21:25:00", "23:30", Constellation.VUL),
            arrayOf("5:42:00", "5:53:00", "22:50", Constellation.TAU),
            arrayOf("0:04:00", "0:08:30", "22:00", Constellation.AND),
            arrayOf("15:55:00", "16:02:00", "22:00", Constellation.SER),
            arrayOf("5:53:00", "6:13:00", "21:30", Constellation.GEM),
            arrayOf("19:50:00", "20:15:00", "21:15", Constellation.VUL),
            arrayOf("18:52:00", "19:15:00", "21:05", Constellation.VUL),
            arrayOf("0:08:30", "0:51:00", "21:00", Constellation.AND),
            arrayOf("20:15:00", "20:34:00", "20:30", Constellation.VUL),
            arrayOf("7:48:30", "7:53:00", "20:00", Constellation.GEM),
            arrayOf("20:34:00", "21:15:00", "19:30", Constellation.VUL),
            arrayOf("19:15:00", "19:50:00", "19:10", Constellation.VUL),
            arrayOf("3:17:00", "3:22:00", "19:00", Constellation.ARI),
            arrayOf("18:52:00", "19:00:00", "18:30", Constellation.SGE),
            arrayOf("5:42:00", "5:46:00", "18:00", Constellation.ORI),
            arrayOf("6:13:00", "6:18:30", "17:30", Constellation.GEM),
            arrayOf("19:00:00", "19:50:00", "16:10", Constellation.SGE),
            arrayOf("4:58:00", "5:20:00", "16:00", Constellation.TAU),
            arrayOf("15:55:00", "16:05:00", "16:00", Constellation.HER),
            arrayOf("19:50:00", "20:15:00", "15:45", Constellation.SGE),
            arrayOf("4:37:00", "4:58:00", "15:30", Constellation.TAU),
            arrayOf("5:20:00", "5:36:00", "15:30", Constellation.TAU),
            arrayOf("12:50:00", "13:30:00", "15:00", Constellation.COM),
            arrayOf("17:15:00", "18:15:00", "14:20", Constellation.HER),
            arrayOf("11:52:00", "12:50:00", "14:00", Constellation.COM),
            arrayOf("7:30:00", "7:48:30", "13:30", Constellation.GEM),
            arrayOf("16:45:00", "17:15:00", "12:50", Constellation.HER),
            arrayOf("0:00:00", "0:08:30", "12:30", Constellation.PEG),
            arrayOf("5:36:00", "5:46:00", "12:30", Constellation.TAU),
            arrayOf("7:00:00", "7:30:00", "12:30", Constellation.GEM),
            arrayOf("21:07:00", "21:20:00", "12:30", Constellation.PEG),
            arrayOf("6:18:30", "6:56:00", "12:00", Constellation.GEM),
            arrayOf("18:15:00", "18:52:00", "12:00", Constellation.HER),
            arrayOf("20:52:30", "21:03:00", "11:50", Constellation.DEL),
            arrayOf("21:03:00", "21:07:00", "11:50", Constellation.PEG),
            arrayOf("11:31:00", "11:52:00", "11:00", Constellation.LEO),
            arrayOf("6:14:30", "6:18:30", "10:00", Constellation.ORI),
            arrayOf("6:56:00", "7:00:00", "10:00", Constellation.GEM),
            arrayOf("7:48:30", "7:55:30", "10:00", Constellation.CNC),
            arrayOf("23:50:00", "24:00:00", "10:00", Constellation.PEG),
            arrayOf("1:40:00", "3:17:00", "9:55", Constellation.ARI),
            arrayOf("20:08:30", "20:18:00", "8:30", Constellation.DEL),
            arrayOf("13:30:00", "15:05:00", "8:00", Constellation.BOO),
            arrayOf("22:45:00", "23:50:00", "7:30", Constellation.PEG),
            arrayOf("7:55:30", "9:15:00", "7:00", Constellation.CNC),
            arrayOf("9:15:00", "10:45:00", "7:00", Constellation.LEO),
            arrayOf("18:15:00", "18:39:44", "6:15", Constellation.OPH),
            arrayOf("18:39:44", "18:52:00", "6:15", Constellation.AQL),
            arrayOf("20:50:00", "20:52:30", "6:00", Constellation.DEL),
            arrayOf("7:00:00", "7:01:00", "5:30", Constellation.CMI),
            arrayOf("18:15:00", "18:25:30", "4:30", Constellation.SER),
            arrayOf("16:05:00", "16:45:00", "4:00", Constellation.HER),
            arrayOf("18:15:00", "18:25:30", "3:00", Constellation.OPH),
            arrayOf("21:28:00", "21:40:00", "2:45", Constellation.PEG),
            arrayOf("0:00:00", "2:00:00", "2:00", Constellation.PSC),
            arrayOf("18:35:00", "18:52:00", "2:00", Constellation.SER),
            arrayOf("20:18:00", "20:50:00", "2:00", Constellation.DEL),
            arrayOf("20:50:00", "21:20:00", "2:00", Constellation.EQU),
            arrayOf("21:20:00", "21:28:00", "2:00", Constellation.PEG),
            arrayOf("22:00:00", "22:45:00", "2:00", Constellation.PEG),
            arrayOf("21:40:00", "22:00:00", "1:45", Constellation.PEG),
            arrayOf("7:01:00", "7:12:00", "1:30", Constellation.CMI),
            arrayOf("3:35:00", "4:37:00", "0:00", Constellation.TAU),
            arrayOf("4:37:00", "4:40:00", "0:00", Constellation.ORI),
            arrayOf("7:12:00", "8:05:00", "0:00", Constellation.CMI),
            arrayOf("14:40:00", "15:05:00", "0:00", Constellation.VIR),
            arrayOf("17:50:00", "18:15:00", "0:00", Constellation.OPH),
            arrayOf("2:39:00", "3:17:00", "-1:45", Constellation.CET),
            arrayOf("3:17:00", "3:35:00", "-1:45", Constellation.TAU),
            arrayOf("15:05:00", "16:16:00", "-3:15", Constellation.SER),
            arrayOf("4:40:00", "5:05:00", "-4:00", Constellation.ORI),
            arrayOf("5:50:00", "6:14:30", "-4:00", Constellation.ORI),
            arrayOf("17:50:00", "17:58:00", "-4:00", Constellation.SER),
            arrayOf("18:15:00", "18:35:00", "-4:00", Constellation.SER),
            arrayOf("18:35:00", "18:52:00", "-4:00", Constellation.AQL),
            arrayOf("22:45:00", "23:50:00", "-4:00", Constellation.PSC),
            arrayOf("10:45:00", "11:31:00", "-6:00", Constellation.LEO),
            arrayOf("11:31:00", "11:50:00", "-6:00", Constellation.VIR),
            arrayOf("0:00:00", "0:20:00", "-7:00", Constellation.PSC),
            arrayOf("23:50:00", "24:00:00", "-7:00", Constellation.PSC),
            arrayOf("14:15:00", "14:40:00", "-8:00", Constellation.VIR),
            arrayOf("15:55:00", "16:16:00", "-8:00", Constellation.OPH),
            arrayOf("20:00:00", "20:32:00", "-9:00", Constellation.AQL),
            arrayOf("21:20:00", "21:52:00", "-9:00", Constellation.AQR),
            arrayOf("17:10:00", "17:58:00", "-10:00", Constellation.OPH),
            arrayOf("5:50:00", "8:05:00", "-11:00", Constellation.MON),
            arrayOf("4:55:00", "5:05:00", "-11:00", Constellation.ERI),
            arrayOf("5:05:00", "5:50:00", "-11:00", Constellation.ORI),
            arrayOf("8:05:00", "8:22:00", "-11:00", Constellation.HYA),
            arrayOf("9:35:00", "10:45:00", "-11:00", Constellation.SEX),
            arrayOf("11:50:00", "12:50:00", "-11:00", Constellation.VIR),
            arrayOf("17:35:00", "17:40:00", "-11:40", Constellation.OPH),
            arrayOf("18:52:00", "20:00:00", "-12:02", Constellation.AQL),
            arrayOf("4:50:00", "4:55:00", "-14:30", Constellation.ERI),
            arrayOf("20:32:00", "21:20:00", "-15:00", Constellation.AQR),
            arrayOf("17:10:00", "18:15:00", "-16:00", Constellation.SER),
            arrayOf("18:15:00", "18:52:00", "-16:00", Constellation.SCT),
            arrayOf("8:22:00", "8:35:00", "-17:00", Constellation.HYA),
            arrayOf("16:16:00", "16:22:30", "-18:15", Constellation.OPH),
            arrayOf("8:35:00", "9:05:00", "-19:00", Constellation.HYA),
            arrayOf("10:45:00", "10:50:00", "-19:00", Constellation.CRT),
            arrayOf("16:16:00", "16:22:30", "-19:15", Constellation.SCO),
            arrayOf("15:40:00", "15:55:00", "-20:00", Constellation.LIB),
            arrayOf("12:35:00", "12:50:00", "-22:00", Constellation.CRV),
            arrayOf("12:50:00", "14:15:00", "-22:00", Constellation.VIR),
            arrayOf("9:05:00", "9:45:00", "-24:00", Constellation.HYA),
            arrayOf("1:40:00", "2:39:00", "-24:23", Constellation.CET),
            arrayOf("2:39:00", "3:45:00", "-24:23", Constellation.ERI),
            arrayOf("10:50:00", "11:50:00", "-24:30", Constellation.CRT),
            arrayOf("11:50:00", "12:35:00", "-24:30", Constellation.CRV),
            arrayOf("14:15:00", "14:55:00", "-24:30", Constellation.LIB),
            arrayOf("16:16:00", "16:45:00", "-24:35", Constellation.OPH),
            arrayOf("0:00:00", "1:40:00", "-25:30", Constellation.CET),
            arrayOf("21:20:00", "21:52:00", "-25:30", Constellation.CAP),
            arrayOf("21:52:00", "23:50:00", "-25:30", Constellation.AQR),
            arrayOf("23:50:00", "24:00:00", "-25:30", Constellation.CET),
            arrayOf("9:45:00", "10:15:00", "-26:30", Constellation.HYA),
            arrayOf("4:42:00", "4:50:00", "-27:15", Constellation.ERI),
            arrayOf("4:50:00", "6:07:00", "-27:15", Constellation.LEP),
            arrayOf("20:00:00", "21:20:00", "-28:00", Constellation.CAP),
            arrayOf("10:15:00", "10:35:00", "-29:10", Constellation.HYA),
            arrayOf("12:35:00", "14:55:00", "-29:30", Constellation.HYA),
            arrayOf("14:55:00", "15:40:00", "-29:30", Constellation.LIB),
            arrayOf("15:40:00", "16:00:00", "-29:30", Constellation.SCO),
            arrayOf("4:35:00", "4:42:00", "-30:00", Constellation.ERI),
            arrayOf("16:45:00", "17:36:00", "-30:00", Constellation.OPH),
            arrayOf("17:36:00", "17:50:00", "-30:00", Constellation.SGR),
            arrayOf("10:35:00", "10:50:00", "-31:10", Constellation.HYA),
            arrayOf("6:07:00", "7:22:00", "-33:00", Constellation.CMA),
            arrayOf("12:15:00", "12:35:00", "-33:00", Constellation.HYA),
            arrayOf("10:50:00", "12:15:00", "-35:00", Constellation.HYA),
            arrayOf("3:30:00", "3:45:00", "-36:00", Constellation.FOR),
            arrayOf("8:22:00", "9:22:00", "-36:45", Constellation.PYX),
            arrayOf("4:16:00", "4:35:00", "-37:00", Constellation.ERI),
            arrayOf("17:50:00", "19:10:00", "-37:00", Constellation.SGR),
            arrayOf("21:20:00", "23:00:00", "-37:00", Constellation.PSA),
            arrayOf("23:00:00", "23:20:00", "-37:00", Constellation.SCL),
            arrayOf("3:00:00", "3:30:00", "-39:35", Constellation.FOR),
            arrayOf("9:22:00", "11:00:00", "-39:45", Constellation.ANT),
            arrayOf("0:00:00", "1:40:00", "-40:00", Constellation.SCL),
            arrayOf("1:40:00", "3:00:00", "-40:00", Constellation.FOR),
            arrayOf("3:52:00", "4:16:00", "-40:00", Constellation.ERI),
            arrayOf("23:20:00", "24:00:00", "-40:00", Constellation.SCL),
            arrayOf("14:10:00", "14:55:00", "-42:00", Constellation.CEN),
            arrayOf("15:40:00", "16:00:00", "-42:00", Constellation.LUP),
            arrayOf("16:00:00", "16:25:15", "-42:00", Constellation.SCO),
            arrayOf("4:50:00", "5:00:00", "-43:00", Constellation.CAE),
            arrayOf("5:00:00", "6:35:00", "-43:00", Constellation.COL),
            arrayOf("8:00:00", "8:22:00", "-43:00", Constellation.PUP),
            arrayOf("3:25:00", "3:52:00", "-44:00", Constellation.ERI),
            arrayOf("16:25:15", "17:50:00", "-45:30", Constellation.SCO),
            arrayOf("17:50:00", "19:10:00", "-45:30", Constellation.CRA),
            arrayOf("19:10:00", "20:20:00", "-45:30", Constellation.SGR),
            arrayOf("20:20:00", "21:20:00", "-45:30", Constellation.MIC),
            arrayOf("3:00:00", "3:25:00", "-46:00", Constellation.ERI),
            arrayOf("4:30:00", "4:50:00", "-46:30", Constellation.CAE),
            arrayOf("15:20:00", "15:40:00", "-48:00", Constellation.LUP),
            arrayOf("0:00:00", "2:20:00", "-48:10", Constellation.PHE),
            arrayOf("2:40:00", "3:00:00", "-49:00", Constellation.ERI),
            arrayOf("4:05:00", "4:16:00", "-49:00", Constellation.HOR),
            arrayOf("4:16:00", "4:30:00", "-49:00", Constellation.CAE),
            arrayOf("21:20:00", "22:00:00", "-50:00", Constellation.GRU),
            arrayOf("6:00:00", "8:00:00", "-50:45", Constellation.PUP),
            arrayOf("8:00:00", "8:10:00", "-50:45", Constellation.VEL),
            arrayOf("2:25:00", "2:40:00", "-51:00", Constellation.ERI),
            arrayOf("3:50:00", "4:05:00", "-51:00", Constellation.HOR),
            arrayOf("0:00:00", "1:50:00", "-51:30", Constellation.PHE),
            arrayOf("6:00:00", "6:10:00", "-52:30", Constellation.CAR),
            arrayOf("8:10:00", "8:27:00", "-53:00", Constellation.VEL),
            arrayOf("3:30:00", "3:50:00", "-53:10", Constellation.HOR),
            arrayOf("3:50:00", "4:00:00", "-53:10", Constellation.DOR),
            arrayOf("0:00:00", "1:35:00", "-53:30", Constellation.PHE),
            arrayOf("2:10:00", "2:25:00", "-54:00", Constellation.ERI),
            arrayOf("4:30:00", "5:00:00", "-54:00", Constellation.PIC),
            arrayOf("15:03:00", "15:20:00", "-54:00", Constellation.LUP),
            arrayOf("8:27:00", "8:50:00", "-54:30", Constellation.VEL),
            arrayOf("6:10:00", "6:30:00", "-55:00", Constellation.CAR),
            arrayOf("11:50:00", "12:50:00", "-55:00", Constellation.CEN),
            arrayOf("14:10:00", "15:03:00", "-55:00", Constellation.LUP),
            arrayOf("15:03:00", "15:20:00", "-55:00", Constellation.NOR),
            arrayOf("4:00:00", "4:20:00", "-56:30", Constellation.DOR),
            arrayOf("8:50:00", "11:00:00", "-56:30", Constellation.VEL),
            arrayOf("11:00:00", "11:15:00", "-56:30", Constellation.CEN),
            arrayOf("17:30:00", "18:00:00", "-57:00", Constellation.ARA),
            arrayOf("18:00:00", "20:20:00", "-57:00", Constellation.TEL),
            arrayOf("22:00:00", "23:20:00", "-57:00", Constellation.GRU),
            arrayOf("3:12:00", "3:30:00", "-57:30", Constellation.HOR),
            arrayOf("5:00:00", "5:30:00", "-57:30", Constellation.PIC),
            arrayOf("6:30:00", "6:50:00", "-58:00", Constellation.CAR),
            arrayOf("0:00:00", "1:20:00", "-58:30", Constellation.PHE),
            arrayOf("1:20:00", "2:10:00", "-58:30", Constellation.ERI),
            arrayOf("23:20:00", "24:00:00", "-58:30", Constellation.PHE),
            arrayOf("4:20:00", "4:35:00", "-59:00", Constellation.DOR),
            arrayOf("15:20:00", "16:25:15", "-60:00", Constellation.NOR),
            arrayOf("20:20:00", "21:20:00", "-60:00", Constellation.IND),
            arrayOf("5:30:00", "6:00:00", "-61:00", Constellation.PIC),
            arrayOf("15:10:00", "15:20:00", "-61:00", Constellation.CIR),
            arrayOf("16:25:15", "16:35:00", "-61:00", Constellation.ARA),
            arrayOf("14:55:00", "15:10:00", "-63:35", Constellation.CIR),
            arrayOf("16:35:00", "16:45:00", "-63:35", Constellation.ARA),
            arrayOf("6:00:00", "6:50:00", "-64:00", Constellation.PIC),
            arrayOf("6:50:00", "9:02:00", "-64:00", Constellation.CAR),
            arrayOf("11:15:00", "11:50:00", "-64:00", Constellation.CEN),
            arrayOf("11:50:00", "12:50:00", "-64:00", Constellation.CRU),
            arrayOf("12:50:00", "14:32:00", "-64:00", Constellation.CEN),
            arrayOf("13:30:00", "13:40:00", "-65:00", Constellation.CIR),
            arrayOf("16:45:00", "16:50:00", "-65:00", Constellation.ARA),
            arrayOf("2:10:00", "3:12:00", "-67:30", Constellation.HOR),
            arrayOf("3:12:00", "4:35:00", "-67:30", Constellation.RET),
            arrayOf("14:45:00", "14:55:00", "-67:30", Constellation.CIR),
            arrayOf("16:50:00", "17:30:00", "-67:30", Constellation.ARA),
            arrayOf("17:30:00", "18:00:00", "-67:30", Constellation.PAV),
            arrayOf("22:00:00", "23:20:00", "-67:30", Constellation.TUC),
            arrayOf("4:35:00", "6:35:00", "-70:00", Constellation.DOR),
            arrayOf("13:40:00", "14:45:00", "-70:00", Constellation.CIR),
            arrayOf("14:45:00", "17:00:00", "-70:00", Constellation.TRA),
            arrayOf("0:00:00", "1:20:00", "-75:00", Constellation.TUC),
            arrayOf("3:30:00", "4:35:00", "-75:00", Constellation.HYI),
            arrayOf("6:35:00", "9:02:00", "-75:00", Constellation.VOL),
            arrayOf("9:02:00", "11:15:00", "-75:00", Constellation.CAR),
            arrayOf("11:15:00", "13:40:00", "-75:00", Constellation.MUS),
            arrayOf("18:00:00", "21:20:00", "-75:00", Constellation.PAV),
            arrayOf("21:20:00", "23:20:00", "-75:00", Constellation.IND),
            arrayOf("23:20:00", "24:00:00", "-75:00", Constellation.TUC),
            arrayOf("0:45:00", "1:20:00", "-76:00", Constellation.TUC),
            arrayOf("0:00:00", "3:30:00", "-82:30", Constellation.HYI),
            arrayOf("7:40:00", "13:40:00", "-82:30", Constellation.CHA),
            arrayOf("13:40:00", "18:00:00", "-82:30", Constellation.APS),
            arrayOf("3:30:00", "7:40:00", "-85:00", Constellation.MEN),
            arrayOf("0:00:00", "24:00:00", "-90:00", Constellation.OCT),
        )
    }
}
