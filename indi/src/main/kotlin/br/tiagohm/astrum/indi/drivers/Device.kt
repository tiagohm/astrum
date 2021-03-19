package br.tiagohm.astrum.indi.drivers

data class Device(
    val name: String,
    val driver: String,
) {

    val isCCD = CCD.contains(this)

    val isWeather = WEATHER.contains(this)

    val isAgent = AGENT.contains(this)

    val isFocuser = FOCUSER.contains(this)

    val isDome = DOME.contains(this)

    val isSpectrograph = SPECTROGRAPH.contains(this)

    val isAuxiliary = AUXILIARY.contains(this)

    val isDetector = DETECTOR.contains(this)

    val isTelescope = TELESCOPE.contains(this)

    val isFilterWheel = FILTER_WHEEL.contains(this)

    val isAdaptiveOptic = ADAPTIVE_OPTIC.contains(this)

    companion object {

        val CCD = listOf(
            Device("CCD Simulator", "indi_simulator_ccd"),
            Device("Guide Simulator", "indi_simulator_guide"),
            Device("DMK CCD", "indi_v4l2_ccd"),
            Device("iOptron iPolar", "indi_v4l2_ccd"),
            Device("iOptron iGuider", "indi_v4l2_ccd"),
            Device("V4L2 CCD", "indi_v4l2_ccd"),
            Device("Apogee CCD", "indi_apogee_ccd"),
            Device("ZWO CCD", "indi_asi_ccd"),
            Device("Atik CCD", "indi_atik_ccd"),
            Device("Meade Deep Sky Imager", "indi_dsi_ccd"),
            Device("FireFly MV", "indi_ffmv_ccd"),
            Device("Atik GP", "indi_ffmv_ccd"),
            Device("Starfish CCD", "indi_fishcamp_ccd"),
            Device("FLI CCD", "indi_fli_ccd"),
            Device("Canon DSLR", "indi_canon_ccd"),
            Device("Nikon DSLR", "indi_nikon_ccd"),
            Device("Pentax DSLR Legacy", "indi_pentax_ccd"),
            Device("Sony DSLR", "indi_sony_ccd"),
            Device("Fuji DSLR", "indi_fuji_ccd"),
            Device("GPhoto CCD", "indi_gphoto_ccd"),
            Device("iNova PLX", "indi_inovaplx_ccd"),
            Device("MI CCD (USB)", "indi_mi_ccd_usb"),
            Device("MI CCD (ETH)", "indi_mi_ccd_eth"),
            Device("Nightscape 8300 CCD", "indi_nightscape_ccd"),
            Device("Pentax DSLR Native", "indi_pentax"),
            Device("QHY CCD", "indi_qhy_ccd"),
            Device("Orion SSAG", "indi_qhy_ccd"),
            Device("QSI CCD", "indi_qsi_ccd"),
            Device("RPI Camera", "indi_rpicam"),
            Device("SBIG CCD", "indi_sbig_ccd"),
            Device("SBIG ST-I", "indi_sbig_ccd"),
            Device("SVBONY SV305", "indi_sv305_ccd"),
            Device("SX CCD", "indi_sx_ccd"),
            Device("ToupCam", "indi_toupcam_ccd"),
            Device("Omegon Pro", "indi_toupcam_ccd"),
            Device("Altair", "indi_altair_ccd"),
            Device("StartshootG", "indi_starshootg_ccd"),
            Device("Levenhuk", "indi_nncam_ccd"),
            Device("Mallincam", "indi_mallincam_ccd"),
            Device("INDI Webcam", "indi_webcam_ccd"),
        )
        val WEATHER = listOf(
            Device("Weather Simulator", "indi_simulator_weather"),
            Device("Astromechanics LPM", "indi_astromech_lpm"),
            Device("SQM", "indi_sqm_weather"),
            Device("Weather Meta", "indi_meta_weather"),
            Device("OpenWeatherMap", "indi_openweathermap_weather"),
            Device("MBox", "indi_mbox_weather"),
            Device("Vantage", "indi_vantage_weather"),
            Device("Weather Watcher", "indi_watcher_weather"),
            Device("Weather Safety Proxy", "indi_weather_safety_proxy"),
            Device("AAG Cloud Watcher NG", "indi_aagcloudwatcher_ng"),
            Device("AAG Cloud Watcher", "indi_aagcloudwatcher"),
            Device("Arduino MeteoStation", "indi_duino"),
            Device("Arduino MeteoStation SQM", "indi_duino"),
            Device("Weather Radio", "indi_weatherradio"),
        )
        val AGENT = listOf(
            Device("Imager Agent", "indi_imager_agent"),
        )
        val FOCUSER = listOf(
            Device("Focuser Simulator", "indi_simulator_focus"),
            Device("RoboFocus", "indi_robo_focus"),
            Device("Shoestring Astronomy FCUSB", "indi_fcusb_focus"),
            Device("Celestron SCT", "indi_celestron_sct_focus"),
            Device("AAF2", "indi_aaf2_focus"),
            Device("PlaneWave EFA", "indi_efa_focus"),
            Device("Rainbow Astro RSF", "indi_rainbowrsf_focus"),
            Device("Rigelsys NFocus", "indi_nfocus"),
            Device("Rigelsys NStep", "indi_nstep_focus"),
            Device("RBFocus", "indi_rbfocus_focus"),
            Device("MoonLite", "indi_moonlite_focus"),
            Device("MoonLite DRO", "indi_moonlitedro_focus"),
            Device("MyFocuserPro2", "indi_myfocuserpro2_focus"),
            Device("DeepSkyDad AF1", "indi_deepskydad_af1_focus"),
            Device("DeepSkyDad AF2", "indi_deepskydad_af2_focus"),
            Device("DeepSkyDad AF3", "indi_deepskydad_af3_focus"),
            Device("OnFocus", "indi_onfocus_focus"),
            Device("NightCrawler", "indi_nightcrawler_focus"),
            Device("Gemini Focusing Rotator", "indi_gemini_focus"),
            Device("USBFocusV3", "indi_usbfocusv3_focus"),
            Device("Pegasus DMFC", "indi_dmfc_focus"),
            Device("Pegasus OAG", "indi_dmfc_focus"),
            Device("Pyxis", "indi_pyxis_rotator"),
            Device("Pegasus Falcon", "indi_falcon_rotator"),
            Device("Rotator Simulator", "indi_simulator_rotator"),
            Device("Microtouch", "indi_microtouch_focus"),
            Device("Baader SteelDrive", "indi_steeldrive_focus"),
            Device("Baader SteelDriveII", "indi_steeldrive2_focus"),
            Device("HitecAstro DC", "indi_hitecastrodc_focus"),
            Device("SmartFocus", "indi_smartfocus_focus"),
            Device("Optec TCF-S", "indi_tcfs_focus"),
            Device("Optec TCF-S3", "indi_tcfs3_focus"),
            Device("Sesto Senso", "indi_sestosenso_focus"),
            Device("Sesto Senso 2", "indi_sestosenso2_focus"),
            Device("Esatto", "indi_sestosenso2_focus"),
            Device("Lakeside", "indi_lakeside_focus"),
            Device("FocusLynx", "indi_lynx_focus"),
            Device("PerfectStar", "indi_perfectstar_focus"),
            Device("Starlight EFS", "indi_siefs_focus"),
            Device("Lacerta MFOC", "indi_lacerta_mfoc_focus"),
            Device("ActiveFocuser", "indi_activefocuser_focus"),
            Device("ASI EAF", "indi_asi_focuser"),
            Device("Astromechanics FOC", "indi_astromechfoc"),
            Device("Bee Focuser", "indi_beefocus"),
            Device("DreamFocuser", "indi_dreamfocuser_focus"),
            Device("FLI PDF", "indi_fli_focus"),
            Device("Armadillo focuser", "indi_armadillo_focus"),
            Device("Platypus focuser", "indi_platypus_focus"),
        )
        val DOME = listOf(
            Device("ScopeDome Dome", "indi_scopedome_dome"),
            Device("DDW Dome", "indi_ddw_dome"),
            Device("Baader Dome", "indi_baader_dome"),
            Device("Pulsar Dome", "indi_rigel_dome"),
            Device("Dome Simulator", "indi_simulator_dome"),
            Device("RollOff Simulator", "indi_rolloff_dome"),
            Device("Dome Scripting Gateway", "indi_script_dome"),
            Device("DomePro2", "indi_domepro2_dome"),
            Device("DragonFly Dome", "indi_dragonfly_dome"),
            Device("MaxDome II", "indi_maxdomeii"),
            Device("NexDome", "indi_nexdome"),
            Device("Talon6", "indi_talon6"),
        )
        val SPECTROGRAPH = listOf(
            Device("Spectrograph Simulator", "indi_simulator_spectrograph"),
            Device("LIME-SDR Spectrograph", "indi_limesdr_detector"),
            Device("RTL-SDR", "indi_rtlsdr"),
            Device("Shelyak eShel", "indi_shelyakeshel_spectrograph"),
            Device("Shelyak SPOX", "indi_shelyakspox_spectrograph"),
            Device("SpectraCyber", "indi_spectracyber"),
        )
        val AUXILIARY = listOf(
            Device("Astrometry", "indi_astrometry"),
            Device("SkySafari", "indi_skysafari"),
            Device("Pegasus PPB", "indi_pegasus_ppb"),
            Device("Pegasus PPBA", "indi_pegasus_ppba"),
            Device("Pegasus PPBM", "indi_pegasus_ppba"),
            Device("Pegasus UPB", "indi_pegasus_upb"),
            Device("PlaneWave Delta-T", "indi_planewave_deltat"),
            Device("WatchDog", "indi_watchdog"),
            Device("Joystick", "indi_joystick"),
            Device("Alnitak Remote Dust Cover", "indi_flipflat"),
            Device("Flip Flat", "indi_flipflat"),
            Device("Flip Man", "indi_flipflat"),
            Device("SnapCap", "indi_snapcap"),
            Device("Arduino ST4", "indi_arduinost4"),
            Device("GPUSB", "indi_gpusb"),
            Device("STAR2000", "indi_star2000"),
            Device("GPS Simulator", "indi_simulator_gps"),
            Device("USB_Dewpoint", "indi_usbdewpoint"),
            Device("ASI Power", "indi_asi_power"),
            Device("ASI ST4", "indi_asi_st4"),
            Device("AstroLink 4", "indi_astrolink4"),
            Device("Arduino Simple Switcher", "indi_duino"),
            Device("Arduino Switcher", "indi_duino"),
            Device("Arduino Digital Inputs", "indi_duino"),
            Device("Arduino Demo", "indi_duino"),
            Device("Arduino Stepper", "indi_duino"),
            Device("Arduino Focuser", "indi_duino"),
            Device("Arduino Cosmos", "indi_duino"),
            Device("Arduino Servo", "indi_duino"),
            Device("Arduino Roof", "indi_duino"),
            Device("GPSD", "indi_gpsd"),
            Device("GPS Dongle", "indi_gpsd"),
            Device("GPS NMEA", "indi_gpsnmea"),
            Device("Seletek Rotator", "indi_seletek_rotator"),
            Device("Lacerta MGen Autoguider", "indi_mgenautoguider"),
            Device("RTKLIB Precise Positioning", "indi_rtklib"),
        )
        val DETECTOR = listOf(
            Device("AHP XC Correlator", "indi_ahp_correlator"),
        )
        val TELESCOPE = listOf(
            Device("LX200 Basic", "indi_lx200basic"),
            Device("LX200 GPS", "indi_lx200gps"),
            Device("LX200 Autostar", "indi_lx200autostar"),
            Device("LX200 Classic", "indi_lx200classic"),
            Device("LX200 OnStep", "indi_lx200_OnStep"),
            Device("LX200 TeenAstro", "indi_lx200_TeenAstro"),
            Device("LX200 16", "indi_lx200_16"),
            Device("LX90", "indi_lx200gps"),
            Device("ETX125", "indi_lx200gps"),
            Device("ETX105", "indi_lx200gps"),
            Device("ETX90", "indi_lx200gps"),
            Device("EQ500X", "indi_eq500x_telescope"),
            Device("AstroPhysics Experimental", "indi_lx200ap_experimental"),
            Device("AstroPhysics GTOCP2", "indi_lx200ap_gtocp2"),
            Device("AstroPhysics", "indi_lx200ap"),
            Device("Celestron GPS", "indi_celestron_gps"),
            Device("Celestron CPC", "indi_celestron_gps"),
            Device("Celestron NexStar", "indi_celestron_gps"),
            Device("Celestron AVX", "indi_celestron_gps"),
            Device("Celestron CGEM", "indi_celestron_gps"),
            Device("Celestron CGX", "indi_celestron_gps"),
            Device("Astro-Electronic FS-2", "indi_lx200fs2"),
            Device("Avalon Legacy", "indi_lx200basic"),
            Device("Paramount", "indi_paramount_telescope"),
            Device("Rainbow RST-135", "indi_rainbow_telescope"),
            Device("Rainbow RST-300", "indi_rainbow_telescope"),
            Device("CRUX TitanTCS", "indi_crux_mount"),
            Device("Sky Scan", "indi_synscan_telescope"),
            Device("Losmandy Gemini", "indi_lx200gemini"),
            Device("Mel Bartels", "indi_lx200basic"),
            Device("Temma Takahashi", "indi_temma_telescope"),
            Device("Argo Navis", "indi_lx200basic"),
            Device("SynScan", "indi_synscan_telescope"),
            Device("SynScan Legacy", "indi_synscanlegacy_telescope"),
            Device("SkySensor2000PC", "indi_lx200ss2000pc"),
            Device("iOptron iEQ Legacy", "indi_ieqlegacy_telescope"),
            Device("iOptron SkyGuider Pro", "indi_simulator_telescope"),
            Device("iOptron iEQ30 Pro", "indi_ieq_telescope"),
            Device("iOptron CEM26", "indi_ioptronv3_telescope"),
            Device("iOptron GEM28", "indi_ioptronv3_telescope"),
            Device("iOptron CEM40", "indi_ioptronv3_telescope"),
            Device("iOptron GEM45", "indi_ioptronv3_telescope"),
            Device("iOptron iEQ45 Pro", "indi_ieq_telescope"),
            Device("iOptron CEM60", "indi_ieq_telescope"),
            Device("iOptron CEM25", "indi_ieq_telescope"),
            Device("iOptron SmartEQ", "indi_ieq_telescope"),
            Device("iOptron ZEQ25", "indi_lx200zeq25"),
            Device("iOptron iEQ30", "indi_lx200zeq25"),
            Device("iOptron iEQ45", "indi_lx200zeq25"),
            Device("GotoNova 8400 Kit", "indi_lx200gotonova"),
            Device("iOptron HC8406", "indi_ioptronHC8406"),
            Device("iOptron v3", "indi_ioptronv3_telescope"),
            Device("iOptron CEM120", "indi_ioptronv3_telescope"),
            Device("iOptron CEM70", "indi_ioptronv3_telescope"),
            Device("Pulsar2", "indi_lx200pulsar2"),
            Device("SkyCommander", "indi_skycommander_telescope"),
            Device("LX200 10micron", "indi_lx200_10micron"),
            Device("Telescope Simulator", "indi_simulator_telescope"),
            Device("Skywatcher Alt-Az", "indi_skywatcherAltAzMount"),
            Device("Skywatcher Star Adventurer", "indi_skywatcherAltAzMount"),
            Device("AZ-GTi Alt-Az", "indi_skywatcherAltAzMount"),
            Device("Skywatcher Alt-Az Wedge", "indi_skywatcherAltAzSimple"),
            Device("Telescope Scripting Gateway", "indi_script_telescope"),
            Device("Digital Setting Circle", "indi_dsc_telescope"),
            Device("ES G-11 PMC-Eight", "indi_pmc8_telescope"),
            Device("ES EXOS2-GT PMC-Eight", "indi_pmc8_telescope"),
            Device("ES iEXOS100 PMC-Eight", "indi_pmc8_telescope"),
            Device("AOK Skywalker", "indi_lx200aok"),
            Device("Avalon StarGo", "indi_lx200stargo"),
            Device("Celestron AUX", "indi_celestron_aux"),
            Device("Celestron WiFi", "indi_celestron_aux"),
            Device("Nexstar Evolution", "indi_celestron_aux"),
            Device("EQMod Mount", "indi_eqmod_telescope"),
            Device("AZ-GTi", "indi_azgti_telescope"),
            Device("NexStar Evolution", "indi_nexstarevo_telescope"),
            Device("Starbook", "indi_starbook_telescope"),
        )
        val FILTER_WHEEL = listOf(
            Device("XAGYL Wheel", "indi_xagyl_wheel"),
            Device("Filter Simulator", "indi_simulator_wheel"),
            Device("Optec IFW", "indi_optec_wheel"),
            Device("Quantum Wheel", "indi_quantum_wheel"),
            Device("TruTech Wheel", "indi_trutech_wheel"),
            Device("QHYCFW1", "indi_qhycfw1_wheel"),
            Device("QHYCFW2", "indi_qhycfw2_wheel"),
            Device("QHYCFW3", "indi_qhycfw3_wheel"),
            Device("Manual Filter", "indi_manual_wheel"),
            Device("Apogee CFW", "indi_apogee_wheel"),
            Device("ASI EFW", "indi_asi_wheel"),
            Device("Atik EFW", "indi_atik_wheel"),
            Device("FLI CFW", "indi_fli_wheel"),
            Device("SX Wheel", "indi_sx_wheel"),
        )
        val ADAPTIVE_OPTIC = listOf(
            Device("SX AO", "indi_sx_ao"),
        )
    }
}