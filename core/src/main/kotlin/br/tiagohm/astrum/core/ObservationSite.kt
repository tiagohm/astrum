package br.tiagohm.astrum.core

data class ObservationSite(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val lightPollution: Int = 0,
) {

    companion object {

        // Some observation sites from Stellarium
        val THE_VALLEY = ObservationSite("The Valley - AI", 18.21704, -63.05783, 19.0, 0)
        val CHASSE_ROYALE = ObservationSite("Chasse Royale - BE", 50.42842, 3.95001, 42.0, 0)
        val GUSTAVIA = ObservationSite("Gustavia - BL", 17.89618, -62.84978, 6.0, 4)
        val HAMILTON = ObservationSite("Hamilton - BM", 32.2949, -64.78303, 22.0, 0)
        val KRALENDIJK = ObservationSite("Kralendijk - BQ", 12.15, -68.26667, 4.0, 0)
        val CAMBEBBA = ObservationSite("Cambebba - BR", -3.62092, -38.82913, 41.0, 0)
        val WEST_ISLAND = ObservationSite("West Island - CC", -12.15681, 96.82251, 12.0, 0)
        val FLYING_FISH_COVE = ObservationSite("Flying Fish Cove - CX", -10.42172, 105.67912, 135.0, 0)
        val HASELBACHTAL = ObservationSite("Haselbachtal - DE", 51.2357, 14.02576, 224.0, 0)
        val BURG_UNTER_FALKENSTEIN = ObservationSite("Burg Unter-Falkenstein - DE", 47.7044, 12.10466, 1256.0, 0)
        val STANLEY = ObservationSite("Stanley - FK", -51.7, -57.85, 43.0, 0)
        val PALIKIR_NATIONAL_GOVERNMENT_CENTER =
            ObservationSite("Palikir - National Government Center - FM", 6.92477, 158.16109, 92.0, 0)
        val SAINT_GEORGE_S = ObservationSite("Saint George's - GD", 12.05644, -61.74849, 33.0, 4)
        val GRYTVIKEN = ObservationSite("Grytviken - GS", -54.28111, -36.5092, 4.0, 0)
        val GUAM_GOVERNMENT_HOUSE = ObservationSite("Guam Government House - GU", 13.47191, 144.74978, 46.0, 0)
        val HAGATNA = ObservationSite("Hagatna - GU", 13.47567, 144.74886, 5.0, 0)
        val AMATO = ObservationSite("Amato - IT", 38.38333, 16.13333, 727.0, 0)
        val VADUZ = ObservationSite("Vaduz - LI", 47.14151, 9.52154, 465.0, 4)
        val MARIGOT = ObservationSite("Marigot - MF", 18.06819, -63.08302, 1.0, 4)
        val RMI_CAPITOL = ObservationSite("RMI Capitol - MH", 7.08931, 171.3805, 7.0, 0)
        val PLYMOUTH = ObservationSite("Plymouth - MS", 16.70555, -62.21292, 77.0, 0)
        val BRADES = ObservationSite("Brades - MS", 16.79183, -62.21058, 52.0, 0)
        val VALLETTA = ObservationSite("Valletta - MT", 35.89972, 14.51472, 44.0, 4)
        val KINGSTON = ObservationSite("Kingston - NF", -29.05459, 167.96628, 71.0, 0)
        val YAREN = ObservationSite("Yaren - NR", -0.55085, 166.9252, 6.0, 0)
        val ALOFI = ObservationSite("Alofi - NU", -19.05451, -169.91768, 51.0, 0)
        val SAINT_PIERRE = ObservationSite("Saint-Pierre - PM", 46.77914, -56.1773, 30.0, 4)
        val ADAMSTOWN = ObservationSite("Adamstown - PN", -25.06597, -130.10147, 67.0, 0)
        val MELEKEOK = ObservationSite("Melekeok - PW", 7.50043, 134.62355, 68.0, 0)
        val CAUCASIAN_MOUNTAIN_OBSERVATORY =
            ObservationSite("Caucasian Mountain Observatory - RU", 43.7361, 42.6675, 2112.0, 3)
        val JAMESTOWN = ObservationSite("Jamestown - SH", -15.93872, -5.71675, 426.0, 0)
        val LONGYEARBYEN = ObservationSite("Longyearbyen - SJ", 78.22334, 15.64689, 1.0, 0)
        val SAN_MARINO = ObservationSite("San Marino - SM", 43.93667, 12.44639, 683.0, 0)
        val PHILIPSBURG = ObservationSite("Philipsburg - SX", 18.026, -63.04582, 0.0, 0)
        val LOBAMBA = ObservationSite("Lobamba - SZ", -26.46667, 31.2, 694.0, 0)
        val COCKBURN_TOWN = ObservationSite("Cockburn Town - TC", 21.46122, -71.14188, 7.0, 0)
        val PORT_AUX_FRANCAIS = ObservationSite("Port-aux-Francais - TF", -49.35, 70.21667, 7.0, 0)
        val FUNAFUTI = ObservationSite("Funafuti - TV", -8.52425, 179.19417, 7.0, 0)
        val IRON_RIVER = ObservationSite("Iron River - US", 46.09273, -88.64235, 458.0, 0)
        val VATICAN_CITY = ObservationSite("Vatican City - VA", 41.90236, 12.45332, 62.0, 0)
        val ROAD_TOWN = ObservationSite("Road Town - VG", 18.42693, -64.62079, 9.0, 4)
        val MATA_UTU = ObservationSite("Mata-Utu - WF", -13.28163, -176.17453, 15.0, 0)
        val SAAO_SUTHERLAND = ObservationSite("SAAO Sutherland - ZA", -32.38722, 20.81167, 1798.0, 1)
        val RIVERLEA = ObservationSite("Riverlea - ZA", -26.2119, 27.97322, 1645.0, 0)
        val ALIPURDUAR = ObservationSite("Alipurduar - IN", 26.489, 89.527, 93.0, 4)
        val HAIFA = ObservationSite("Haifa - IL", 32.816667, 34.983333, 0.0, 4)
        val EILAT = ObservationSite("Eilat - IL", 29.55, 34.95, 0.0, 3)
        val UNIVERSITY_OF_TOKYO_ATACAMA_OBSERVATORY =
            ObservationSite("University of Tokyo Atacama Observatory - CL", -22.986667, -67.742222, 5640.0, 1)
        val ATACAMA_COSMOLOGY_TELESCOPE =
            ObservationSite("Atacama Cosmology Telescope - CL", -22.958611, -67.787778, 5190.0, 1)
        val LLANO_DE_CHAJNANTOR_OBSERVATORY_2 =
            ObservationSite("Llano de Chajnantor Observatory - CL", -23.022778, -67.754722, 5104.0, 1)
        val LLANO_DE_CHAJNANTOR_OBSERVATORY_1 =
            ObservationSite("Llano de Chajnantor Observatory - CL", -22.971389, -67.702778, 4800.0, 1)
        val LARGE_MILLIMETER_TELESCOPE =
            ObservationSite("Large Millimeter Telescope - MX", 18.985, -97.314722, 4580.0, 1)
        val INDIAN_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Indian Astronomical Observatory - IN", 32.779444, 78.964167, 4500.0, 1)
        val MEYER_WOMBLE_OBSERVATORY = ObservationSite("Meyer-Womble Observatory - US", 39.586667, -105.64, 4312.0, 1)
        val YANGBAJING_INTERNATIONAL_COSMIC_RAY_OBSERVATORY =
            ObservationSite("Yangbajing International Cosmic Ray Observatory - CN", 30.083333, 90.55, 4300.0, 1)
        val MAUNA_KEA_OBSERVATORY = ObservationSite("Mauna Kea Observatory - US", 19.824444, -155.473333, 4190.0, 1)
        val BARCROFT_OBSERVATORY = ObservationSite("Barcroft Observatory - US", 37.588611, -118.241944, 3890.0, 1)
        val VERY_LONG_BASELINE_ARRAY =
            ObservationSite("Very Long Baseline Array - US", 19.801381, -155.455503, 3730.0, 1)
        val LLANO_DEL_HATO_NATIONAL_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Llano del Hato National Astronomical Observatory - VE", 8.786389, -70.871944, 3600.0, 1)
        val SPHINX_OBSERVATORY = ObservationSite("Sphinx Observatory - CH", 46.5475, 7.985, 3571.0, 1)
        val MAUNA_LOA_OBSERVATORY = ObservationSite("Mauna Loa Observatory - US", 19.536111, -155.576111, 3394.0, 1)
        val MAGDALENA_RIDGE_OBSERVATORY =
            ObservationSite("Magdalena Ridge Observatory - US", 33.976667, -107.184722, 3230.0, 1)
        val MOUNT_GRAHAM_INTERNATIONAL_OBSERVATORY =
            ObservationSite("Mount Graham International Observatory - US", 32.701389, -109.891944, 3191.0, 1)
        val GORNERGRAT_OBSERVATORY = ObservationSite("Gornergrat Observatory - CH", 45.984444, 7.785833, 3135.0, 1)
        val EUROPEAN_EXTREMELY_LARGE_TELESCOPE =
            ObservationSite("European Extremely Large Telescope - CH", -24.588889, -70.192222, 3060.0, 1)
        val HALEAKALA_OBSERVATORY = ObservationSite("Haleakala Observatory - US", 20.708333, -156.2575, 3036.0, 1)
        val PIC_DU_MIDI_OBSERVATORY = ObservationSite("Pic du Midi Observatory - FR", 42.936389, 0.142778, 2877.0, 1)
        val AMUNDSEN_SCOTT_SOUTH_POLE_STATION =
            ObservationSite("Amundsen-Scott South Pole Station - AQ", -90.0, -139.266667, 2835.0, 1)
        val CERRO_ARMAZONES_OBSERVATORY =
            ObservationSite("Cerro Armazones Observatory - CH", -24.598333, -70.201111, 2817.0, 1)
        val NATIONAL_ASTRONOMICAL_OBSERVATORY_MX =
            ObservationSite("National Astronomical Observatory - MX", 31.044167, -115.463611, 2800.0, 1)
        val APACHE_POINT_OBSERVATORY =
            ObservationSite("Apache Point Observatory - US", 32.780278, -105.820278, 2788.0, 1)
        val CERRO_PACHON = ObservationSite("Cerro Pachón - CL", -30.240833, -70.736667, 2722.0, 1)
        val NATIONAL_ASTRONOMICAL_OBSERVATORY_CO =
            ObservationSite("National Astronomical Observatory - CO", 4.596111, -74.0775, 2640.0, 1)
        val PARANAL_OBSERVATORY = ObservationSite("Paranal Observatory - CL", -24.627222, -70.404167, 2635.0, 1)
        val FRED_LAWRENCE_WHIPPLE_OBSERVATORY =
            ObservationSite("Fred Lawrence Whipple Observatory - US", 31.681111, -110.878333, 2606.0, 1)
        val ROQUE_DE_LOS_MUCHACHOS_OBSERVATORY =
            ObservationSite("Roque de los Muchachos Observatory - ES", 28.766667, -17.883333, 2396.0, 1)
        val TEIDE_OBSERVATORY = ObservationSite("Teide Observatory - ES", 28.3, -16.509722, 2390.0, 1)
        val LA_SILLA_OBSERVATORY = ObservationSite("La Silla Observatory - CL", -29.254167, -70.739444, 2380.0, 1)
        val LAS_CAMPANAS_OBSERVATORY = ObservationSite("Las Campanas Observatory - CL", -29.015, -70.692222, 2380.0, 1)
        val LOWELL_OBSERVATORY = ObservationSite("Lowell Observatory - US", 35.202778, -111.664444, 2210.0, 1)
        val CERRO_TOLOLO_INTER_AMERICAN_OBSERVATORY =
            ObservationSite("Cerro Tololo Inter-American Observatory - CL", -30.169167, -70.805833, 2200.0, 1)
        val VERY_LARGE_ARRAY = ObservationSite("Very Large Array - US", 34.078611, -107.618333, 2124.0, 1)
        val KITT_PEAK_NATIONAL_OBSERVATORY =
            ObservationSite("Kitt Peak National Observatory - US", 31.958333, -111.596667, 2096.0, 1)
        val SPECIAL_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Special Astrophysical Observatory - RU", 43.646944, 41.440556, 2070.0, 1)
        val YUNNAN_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Yunnan Astronomical Observatory - CN", 25.033333, 102.783333, 2014.0, 1)
        val MOUNT_WILSON_OBSERVATORY =
            ObservationSite("Mount Wilson Observatory - US", 34.223889, -118.061667, 1742.0, 1)
        val PALOMAR_OBSERVATORY = ObservationSite("Palomar Observatory - US", 33.355833, -116.863889, 1712.0, 1)
        val AKER_OBSERVATORY = ObservationSite("Aker Observatory - US", 32.7993, -109.72822, 0.0, 1)
        val ALDERSHOT_OBSERVATORY = ObservationSite("Aldershot Observatory - GB", 51.25883, -0.76319, 0.0, 1)
        val ALGONQUIN_RADIO_OBSERVATORY =
            ObservationSite("Algonquin Radio Observatory - CA", 45.955503, -78.073042, 0.0, 1)
        val ALLEGHENY_OBSERVATORY = ObservationSite("Allegheny Observatory - US", 40.482525, -80.020829, 0.0, 1)
        val AMETLLA_DE_MAR_OBSERVATORY = ObservationSite("Ametlla de Mar Observatory - ES", 40.928933, 0.79125, 0.0, 1)
        val ARRAY_FOR_MICROWAVE_BACKGROUND_ANISOTROPY =
            ObservationSite("Array for Microwave Background Anisotropy - US", 19.536194, -155.575278, 3396.0, 1)
        val ANDERSON_MESA_STATION = ObservationSite("Anderson Mesa Station - US", 35.096944, -111.535833, 2163.0, 1)
        val ANGELL_HALL_OBSERVATORY = ObservationSite("Angell Hall Observatory - US", 42.27675, -83.739917, 0.0, 1)
        val APOLLO_OBSERVATORY = ObservationSite("Apollo Observatory - US", 39.788823, -84.201794, 0.0, 1)
        val ARCETRI_OBSERVATORY = ObservationSite("Arcetri Observatory - IT", 43.750556, 11.254444, 0.0, 1)
        val ARECIBO_OBSERVATORY = ObservationSite("Arecibo Observatory - US", 18.344167, -66.752778, 0.0, 1)
        val ARMAGH_OBSERVATORY = ObservationSite("Armagh Observatory - GB", 54.353152, -6.64997, 0.0, 1)
        val ARYABHATTA_RESEARCH_INSTITUTE_OF_OBSERVATIONAL_SCIENCES =
            ObservationSite("Aryabhatta Research Institute of Observational Sciences - IN", 29.022, 79.027, 1951.0, 1)
        val ASHTON_OBSERVATORY = ObservationSite("Ashton Observatory - US", 41.813359, -93.288211, 260.0, 1)
        val ASIAGO_OBSERVATORY = ObservationSite("Asiago Observatory - IT", 45.866389, 11.526389, 1045.0, 1)
        val ASTRONOMICAL_OBSERVATORY_OF_LISBON =
            ObservationSite("Astronomical Observatory of Lisbon - PT", 38.710539, -9.187506, 140.0, 1)
        val ASTROPHYSICAL_INSTITUTE_POTSDAM =
            ObservationSite("Astrophysical Institute Potsdam - DE", 52.405, 13.104167, 0.0, 1)
        val AUSTRALIA_TELESCOPE_COMPACT_ARRAY =
            ObservationSite("Australia Telescope Compact Array - AU", -30.312778, 149.550278, 0.0, 1)
        val AUSTRALIAN_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Australian Astronomical Observatory - AU", -31.27667, 149.06667, 1164.0, 1)
        val BADLANDS_OBSERVATORY = ObservationSite("Badlands Observatory - US", 43.990833, -102.130556, 0.0, 1)
        val BAKER_OBSERVATORY = ObservationSite("Baker Observatory - US", 37.398889, -93.041667, 0.0, 1)
        val BAKSAN_NEUTRINO_OBSERVATORY =
            ObservationSite("Baksan Neutrino Observatory - RU", 43.288889, 42.702778, 3000.0, 1)
        val BALL_STATE_UNIVERSITY_OBSERVATORY =
            ObservationSite("Ball State University Observatory - US", 40.1999, -85.4113, 0.0, 1)
        val BAREKET_OBSERVATORY = ObservationSite("Bareket observatory - IL", 31.888189, 35.031694, 0.0, 1)
        val BAYFORDBURY_OBSERVATORY = ObservationSite("Bayfordbury Observatory - GB", 51.776389, -0.095, 0.0, 1)
        val BEHLEN_OBSERVATORY = ObservationSite("Behlen Observatory - US", 41.170833, -96.446667, 0.0, 1)
        val BEIJING_ANCIENT_OBSERVATORY =
            ObservationSite("Beijing Ancient Observatory - CN", 39.906111, 116.428056, 0.0, 1)
        val BELGRADE_OBSERVATORY = ObservationSite("Belgrade Observatory - SR", 44.803611, 20.513333, 253.0, 3)
        val BELOGRADCHIK_OBSERVATORY = ObservationSite("Belogradchik Observatory - BG", 43.622778, 22.675, 650.0, 1)
        val BENMORE_PEAK_OBSERVATORY = ObservationSite("Benmore Peak Observatory - NZ", -44.4172, 170.094, 1932.0, 1)
        val BERGISCH_GLADBACH_OBSERVATORY =
            ObservationSite("Bergisch Gladbach Observatory - DE", 51.087611, 7.485778, 0.0, 3)
        val BERLIN_OBSERVATORY = ObservationSite("Berlin Observatory - DE", 52.503889, 13.394167, 0.0, 3)
        val BURRELL_MEMORIAL_OBSERVATORY =
            ObservationSite("Burrell Memorial Observatory - US", 41.3754, -81.8513, 0.0, 1)
        val BESANCON_OBSERVATORY = ObservationSite("Besancon Observatory - FR", 47.246944, 5.989722, 0.0, 1)
        val BIG_BEAR_SOLAR_OBSERVATORY =
            ObservationSite("Big Bear Solar Observatory - US", 34.258333, -116.921389, 2060.0, 1)
        val BISDEE_TIER_OPTICAL_ASTRONOMY_OBSERVATORY =
            ObservationSite("Bisdee Tier Optical Astronomy Observatory - AU", -42.425833, 147.288611, 646.0, 1)
        val BORDEAUX_OBSERVATORY = ObservationSite("Bordeaux Observatory - FR", 44.8349, -0.526, 0.0, 1)
        val BOSSCHA_OBSERVATORY = ObservationSite("Bosscha Observatory - ID", -6.824444, 107.615556, 1310.0, 1)
        val BOWMAN_OBSERVATORY = ObservationSite("Bowman Observatory - US", 41.032389, -73.621111, 0.0, 1)
        val BOYDEN_OBSERVATORY = ObservationSite("Boyden Observatory - ZA", -29.038831, 26.404722, 1372.0, 1)
        val BRADSTREET_OBSERVATORY = ObservationSite("Bradstreet Observatory - US", 40.050278, -75.368889, 0.0, 1)
        val BRADLEY_OBSERVATORY = ObservationSite("Bradley Observatory - US", 33.765233, -84.294161, 315.0, 1)
        val BRAESIDE_OBSERVATORY = ObservationSite("Braeside Observatory - US", 35.191667, -111.748333, 2274.0, 1)
        val BRERA_OBSERVATORY = ObservationSite("Brera Observatory - IT", 45.471389, 9.189444, 0.0, 1)
        val BROOKS_OBSERVATORY = ObservationSite("Brooks Observatory - US", 41.666667, -83.0, 0.0, 1)
        val BROOKS_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Brooks Astronomical Observatory - US", 43.5875, -84.774444, 257.0, 1)
        val BRORFELDE_OBSERVATORY = ObservationSite("Brorfelde Observatory - DK", 55.621944, 11.665278, 0.0, 1)
        val BUCKNELL_OBSERVATORY = ObservationSite("Bucknell Observatory - US", 40.951317, -76.883217, 171.0, 1)
        val BYURAKAN_OBSERVATORY = ObservationSite("Byurakan Observatory - AM", 40.330833, 44.268333, 1500.0, 1)
        val CAGIGAL_OBSERVATORY = ObservationSite("Cagigal Observatory - VE", 10.503611, -66.928889, 1037.0, 1)
        val CAGLIARI_OBSERVATORY = ObservationSite("Cagliari Observatory - IT", 39.136528, 8.972778, 0.0, 1)
        val CALAR_ALTO_OBSERVATORY = ObservationSite("Calar Alto Observatory - ES", 37.223611, -2.546111, 2168.0, 1)
        val CALTECH_SUBMILLIMETER_OBSERVATORY =
            ObservationSite("Caltech Submillimeter Observatory - US", 19.8225, -155.47585, 0.0, 1)
        val CAMPBELLTOWN_ROTARY_OBSERVATORY =
            ObservationSite("Campbelltown Rotary Observatory - AU", -34.07, 150.8, 100.0, 1)
        val CANOPUS_HILL_OBSERVATORY = ObservationSite("Canopus Hill Observatory - AU", -42.8475, 147.432778, 260.0, 1)
        val CAPILLA_PEAK_OBSERVATORY =
            ObservationSite("Capilla Peak Observatory - US", 34.706667, -106.408611, 2835.0, 1)
        val CAPODIMONTE_OBSERVATORY = ObservationSite("Capodimonte Observatory - IT", 40.862861, 14.255056, 150.0, 1)
        val CARTER_OBSERVATORY = ObservationSite("Carter Observatory - NZ", -41.28437, 174.76697, 0.0, 1)
        val CATANIA_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Catania Astrophysical Observatory - IT", 37.528808, 15.071494, 0.0, 1)
        val CELESTIAL_OBSERVATORY = ObservationSite("Celestial Observatory - US", 35.295278, -93.136389, 170.0, 1)
        val CHAMBERLIN_OBSERVATORY = ObservationSite("Chamberlin Observatory - US", 39.676111, -104.953056, 1651.0, 1)
        val CHICO_COMMUNITY_OBSERVATORY =
            ObservationSite("Chico Community Observatory - US", 39.771171, -121.782745, 0.0, 1)
        val CINCINNATI_OBSERVATORY = ObservationSite("Cincinnati Observatory - US", 39.138611, -84.422778, 0.0, 1)
        val CITY_OBSERVATORY_EDINBURGH =
            ObservationSite("City Observatory. Edinburgh - GB", 55.954722, -3.183333, 107.0, 1)
        val COATS_OBSERVATORY = ObservationSite("Coats Observatory - GB", 55.845615, -4.431166, 0.0, 0)
        val COIT_OBSERVATORY = ObservationSite("Coit Observatory - US", 42.35025, -71.105372, 32.0, 1)
        val COLLINS_OBSERVATORY = ObservationSite("Collins Observatory - US", 42.11803, -77.07768, 252.0, 1)
        val COLOMBO_UNIVERSITY_OBSERVATORY =
            ObservationSite("Colombo University Observatory - LK", 6.90143, 79.86139, 0.0, 1)
        val CONCORDIA_COLLEGE_OBSERVATORY =
            ObservationSite("Concordia College Observatory - US", 46.865278, -96.77, 0.0, 1)
        val CONSELL_OBSERVATORY = ObservationSite("Consell Observatory - ES", 39.653611, 2.8225, 130.0, 1)
        val COPENHAGEN_UNIVERSITY_OBSERVATORY =
            ObservationSite("Copenhagen University Observatory - DK", 55.686825, 12.575814, 10.0, 1)
        val CORDELL_LORENZ_OBSERVATORY = ObservationSite("Cordell-Lorenz Observatory - US", 35.204444, -85.92, 590.0, 1)
        val COTE_D_AZUR_OBSERVATORY = ObservationSite("Cote-d'Azur Observatory - FR", 43.72333, 7.30167, 0.0, 1)
        val CRANE_OBSERVATORY = ObservationSite("Crane Observatory - US", 39.035972, -95.698889, 0.0, 1)
        val CRAWFORD_OBSERVATORY = ObservationSite("Crawford Observatory - IE", 51.89253, -8.49229, 0.0, 1)
        val CRIMEAN_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Crimean Astrophysical Observatory - RU", 44.726667, 34.015861, 0.0, 1)
        val CRNI_VRH_OBSERVATORY = ObservationSite("Crni Vrh Observatory - SI", 45.946667, 14.073611, 0.0, 1)
        val CUPILLARI_OBSERVATORY = ObservationSite("Cupillari Observatory - US", 41.5965, -75.678, 0.0, 1)
        val CUSTER_OBSERVATORY = ObservationSite("Custer Observatory - US", 41.051875, -72.434514, 0.0, 1)
        val DARK_SKY_OBSERVATORY = ObservationSite("Dark Sky Observatory - US", 36.2514, -81.4121, 932.0, 1)
        val DEARBORN_OBSERVATORY = ObservationSite("Dearborn Observatory - US", 42.056667, -87.675, 195.0, 1)
        val DETROIT_OBSERVATORY = ObservationSite("Detroit Observatory - US", 42.281667, -83.731667, 0.0, 1)
        val DAVID_DUNLAP_OBSERVATORY = ObservationSite("David Dunlap Observatory - CA", 43.862954, -79.422687, 224.0, 1)
        val DOME_C = ObservationSite("Dome C - AQ", -75.1, 123.35, 3233.0, 1)
        val DOMINION_OBSERVATORY = ObservationSite("Dominion Observatory - CA", 45.393591, -75.714329, 0.0, 1)
        val DOMINION_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Dominion Astrophysical Observatory - CA", 48.520286, -123.418147, 0.0, 1)
        val DUNSINK_OBSERVATORY = ObservationSite("Dunsink Observatory - IE", 53.387056, -6.337547, 0.0, 1)
        val DYER_OBSERVATORY = ObservationSite("Dyer Observatory - US", 36.052222, -86.805, 345.0, 1)
        val EFFELSBERG_RADIO_TELESCOPE =
            ObservationSite("Effelsberg Radio Telescope - DE", 50.524722, 6.882778, 319.0, 1)
        val EGE_UNIVERSITY_OBSERVATORY =
            ObservationSite("Ege University Observatory - TR", 38.398333, 27.268333, 800.0, 1)
        val ELGINFIELD_OBSERVATORY = ObservationSite("Elginfield Observatory - CA", 43.1924, -81.3158, 325.0, 1)
        val ESKDALEMUIR_OBSERVATORY = ObservationSite("Eskdalemuir Observatory - GB", 55.312222, -3.206111, 0.0, 1)
        val FABRA_OBSERVATORY = ObservationSite("Fabra Observatory - ES", 41.418333, 2.124167, 415.0, 1)
        val FAN_MOUNTAIN_OBSERVATORY = ObservationSite("Fan Mountain Observatory - US", 37.878153, -78.693033, 566.0, 1)
        val FELIX_AGUILAR_OBSERVATORY = ObservationSite("Felix Aguilar Observatory - AR", -31.8023, -69.3265, 2420.0, 1)
        val FERNBANK_OBSERVATORY = ObservationSite("Fernbank Observatory - US", 33.778889, -84.318056, 323.0, 1)
        val FICK_OBSERVATORY = ObservationSite("Fick Observatory - US", 42.005611, -93.943944, 314.0, 1)
        val FIVE_COLLEGE_RADIO_ASTRONOMY_OBSERVATORY =
            ObservationSite("Five College Radio Astronomy Observatory - US", 42.39167, -72.345, 306.0, 1)
        val FLARESTAR_OBSERVATORY = ObservationSite("Flarestar Observatory - MT", 35.911111, 14.470833, 0.0, 1)
        val FOGGY_BOTTOM_OBSERVATORY = ObservationSite("Foggy Bottom Observatory - US", 42.81651, -75.532568, 343.0, 1)
        val FOOTHILL_OBSERVATORY = ObservationSite("Foothill Observatory - US", 37.363125, -122.131514, 0.0, 1)
        val FORD_OBSERVATORY_2 = ObservationSite("Ford Observatory - US", 42.417533, -76.494075, 348.0, 1)
        val FORD_OBSERVATORY_1 = ObservationSite("Ford Observatory - US", 34.381944, -117.681667, 0.0, 1)
        val FOX_OBSERVATORY = ObservationSite("Fox Observatory - US", 26.129306, -80.359917, 0.0, 1)
        val FOX_PARK_PUBLIC_OBSERVATORY =
            ObservationSite("Fox Park Public Observatory - US", 42.640136, -84.747447, 0.0, 1)
        val FRANCIS_MARION_UNIVERSITY_OBSERVATORY =
            ObservationSite("Francis Marion University Observatory - US", 34.185111, -79.651778, 0.0, 1)
        val FREMONT_PEAK_OBSERVATORY =
            ObservationSite("Fremont Peak Observatory - US", 36.760222, -121.498722, 838.0, 1)
        val FROSTY_DREW_OBSERVATORY = ObservationSite("Frosty Drew Observatory - US", 41.367222, -71.663611, 0.0, 1)
        val FUERTES_OBSERVATORY = ObservationSite("Fuertes Observatory - US", 42.452777, -76.474489, 274.0, 1)
        val GIANT_METREWAVE_RADIO_TELESCOPE =
            ObservationSite("Giant Metrewave Radio Telescope - IN", 19.096517, 74.049742, 0.0, 1)
        val GIFFORD_OBSERVATORY = ObservationSite("Gifford Observatory - NZ", -41.3051, 174.7831, 0.0, 1)
        val GIRAWALI_OBSERVATORY = ObservationSite("Girawali Observatory - IN", 19.083333, 73.666667, 1000.0, 1)
        val GLEN_D_RILEY_OBSERVATORY = ObservationSite("Glen D. Riley Observatory - US", 41.7007, -88.1588, 197.0, 1)
        val GODLEE_OBSERVATORY = ObservationSite("Godlee Observatory - GB", 53.476144, -2.232644, 77.0, 1)
        val GOODSELL_OBSERVATORY = ObservationSite("Goodsell Observatory - US", 44.461667, -93.152222, 290.0, 1)
        val GOETHE_LINK_OBSERVATORY = ObservationSite("Goethe Link Observatory - US", 39.55, -86.395, 293.0, 1)
        val GOLDENDALE_OBSERVATORY_STATE_PARK =
            ObservationSite("Goldendale Observatory State Park - US", 45.838889, -120.815278, 640.0, 1)
        val GRAN_TELESCOPIO_CANARIAS =
            ObservationSite("Gran Telescopio Canarias - ES", 28.756611, -17.892028, 2267.0, 1)
        val GRANT_O_GALE_OBSERVATORY = ObservationSite("Grant O. Gale Observatory - US", 41.756669, -92.72, 0.0, 1)
        val GREEN_BANK_TELESCOPE = ObservationSite("Green Bank Telescope - US", 38.433056, -79.839722, 0.0, 1)
        val GREEN_POINT_OBSERVATORY = ObservationSite("Green Point Observatory - AU", -34.00181, 151.07326, 0.0, 1)
        val GRIFFITH_OBSERVATORY = ObservationSite("Griffith Observatory - US", 34.11856, -118.30037, 0.0, 3)
        val GUILLERMO_HARO_OBSERVATORY =
            ObservationSite("Guillermo Haro Observatory - MX", 31.052778, -110.384722, 2480.0, 1)
        val HAMBURG_BERGEDORF_OBSERVATORY = ObservationSite("Hamburg-Bergedorf Observatory - DE", 53.48, 10.241, 0.0, 1)
        val HAT_CREEK_RADIO_OBSERVATORY =
            ObservationSite("Hat Creek Radio Observatory - US", 40.817778, -121.473333, 0.0, 1)
        val HARD_LABOR_CREEK_OBSERVATORY =
            ObservationSite("Hard Labor Creek Observatory - US", 33.671111, -83.593889, 219.0, 1)
        val HARTUNG_BOOTHROYD_OBSERVATORY =
            ObservationSite("Hartung–Boothroyd Observatory - US", 42.458225, -76.384608, 530.0, 1)
        val HARTEBEESTHOEK_RADIO_ASTRONOMY_OBSERVATORY =
            ObservationSite("Hartebeesthoek Radio Astronomy Observatory - ZA", -25.887222, 27.684722, 0.0, 1)
        val HARVARD_COLLEGE_OBSERVATORY =
            ObservationSite("Harvard College Observatory - US", 42.38146, -71.12837, 0.0, 3)
        val HARVARD_SMITHSONIAN_CENTER_FOR_ASTROPHYSICS =
            ObservationSite("Harvard–Smithsonian Center for Astrophysics - US", 42.38146, -71.12837, 3.0, 0)
        val HAUTE_PROVENCE_OBSERVATORY = ObservationSite("Haute-Provence Observatory - FR", 43.930833, 5.713333, 0.0, 1)
        val HAYSTACK_OBSERVATORY = ObservationSite("Haystack Observatory - US", 42.6233, -71.4882, 131.0, 1)
        val HECTOR_J_ROBINSON_OBSERVATORY =
            ObservationSite("Hector J. Robinson Observatory - US", 42.245833, -83.186944, 0.0, 1)
        val HELSINKI_UNIVERSITY_OBSERVATORY =
            ObservationSite("Helsinki University Observatory - FI", 60.161667, 24.955, 0.0, 1)
        val HERRETT_OBSERVATORY = ObservationSite("Herrett Observatory - US", 42.583889, -114.470278, 1120.0, 2)
        val HIDDEN_VALLEY_OBSERVATORY =
            ObservationSite("Hidden Valley Observatory - US", 44.108333, -103.2975, 100.0, 1)
        val HIRSCH_OBSERVATORY = ObservationSite("Hirsch Observatory - US", 42.72838, -73.68039, 0.0, 2)
        val HOBBS_OBSERVATORY = ObservationSite("Hobbs Observatory - US", 44.815833, -91.271667, 285.0, 1)
        val HOHER_LIST_OBSERVATORY = ObservationSite("Hoher List Observatory - DE", 50.166667, 6.85, 549.0, 2)
        val HOLCOMB_OBSERVATORY_AND_PLANETARIUM =
            ObservationSite("Holcomb Observatory and Planetarium - US", 39.841389, -86.171389, 10.0, 3)
        val HONG_KONG_OBSERVATORY = ObservationSite("Hong Kong Observatory - HK", 22.3025, 114.174167, 0.0, 4)
        val HOPKINS_OBSERVATORY = ObservationSite("Hopkins Observatory - US", 42.71167, -73.20167, 0.0, 4)
        val HOWELL_OBSERVATORY = ObservationSite("Howell Observatory - US", 33.464, -88.819, 1000.0, 2)
        val HRADEC_KRALOVE_OBSERVATORY =
            ObservationSite("Hradec Kralove Observatory - CZ", 50.177222, 15.839167, 0.0, 1)
        val HUAIROU_SOLAR_OBSERVING_STATION =
            ObservationSite("Huairou Solar Observing Station - CH", 40.315833, 116.594444, 0.0, 1)
        val HYDE_MEMORIAL_OBSERVATORY = ObservationSite("Hyde Memorial Observatory - US", 40.777816, -96.636225, 0.0, 3)
        val INNSBRUCK_OBSERVATORY = ObservationSite("Innsbruck Observatory - AT", 47.2642, 11.3425, 0.0, 3)
        val ISAAC_NEWTON_GROUP_OF_TELESCOPES =
            ObservationSite("Isaac Newton Group of Telescopes - ES", 28.762056, -17.877639, 0.0, 1)
        val JACK_C_DAVIS_OBSERVATORY = ObservationSite("Jack C. Davis Observatory - US", 39.1857, -119.7964, 1534.0, 3)
        val JAMES_WYLIE_SHEPHERD_OBSERVATORY =
            ObservationSite("James Wylie Shepherd Observatory - US", 33.10378, -86.86497, 0.0, 4)
        val JAKARTA_PLANETARIUM_AND_OBSERVATORY =
            ObservationSite("Jakarta Planetarium and Observatory - ID", -6.189742, 106.839431, 10.0, 6)
        val JANTAR_MANTAR_OBSERVATORY = ObservationSite("Jantar Mantar Observatory - IN", 26.92472, 75.82444, 100.0, 4)
        val JENA_OBSERVATORY = ObservationSite("Jena Observatory - DE", 50.925278, 11.583056, 0.0, 4)
        val JEWETT_OBSERVATORY = ObservationSite("Jewett Observatory - US", 46.728611, -117.153056, 790.0, 2)
        val JODRELL_BANK_OBSERVATORY = ObservationSite("Jodrell Bank Observatory - GB", 53.23625, -2.307139, 0.0, 3)
        val JONES_OBSERVATORY = ObservationSite("Jones Observatory - US", 35.0175, -85.2353, 0.0, 1)
        val JUDSON_B_COIT_OBSERVATORY =
            ObservationSite("Judson B. Coit Observatory - US", 42.35025, -71.105372, 32.0, 5)
        val KAMIOKA_OBSERVATORY = ObservationSite("Kamioka Observatory - JP", 36.426667, 137.311667, 0.0, 3)
        val KANZELHOEHE_SOLAR_OBSERVATORY =
            ObservationSite("Kanzelhoehe Solar Observatory - AT", 46.678333, 13.906667, 1526.0, 1)
        val KARL_SCHWARZSCHILD_OBSERVATORY =
            ObservationSite("Karl Schwarzschild Observatory - DE", 50.980111, 11.711167, 341.0, 1)
        val KECK_OBSERVATORY = ObservationSite("Keck Observatory - US", 19.82636, -155.47501, 4154.0, 1)
        val KEEBLE_OBSERVATORY = ObservationSite("Keeble Observatory - US", 37.762389, -77.475389, 0.0, 1)
        val KENNON_OBSERVATORY = ObservationSite("Kennon Observatory - US", 34.36425, -89.536444, 0.0, 1)
        val KEVOLA_OBSERVATORY = ObservationSite("Kevola Observatory - FI", 60.42, 22.765, 0.0, 1)
        val KIELDER_OBSERVATORY = ObservationSite("Kielder Observatory - GB", 55.231944, -2.61625, 370.0, 1)
        val KIRKWOOD_OBSERVATORY = ObservationSite("Kirkwood Observatory - US", 39.165833, -86.526111, 235.0, 4)
        val KITAMI_OBSERVATORY = ObservationSite("Kitami Observatory - JP", 43.817778, 143.903611, 0.0, 1)
        val KLET_OBSERVATORY = ObservationSite("Klet Observatory - CZ", 48.863611, 14.284444, 1070.0, 1)
        val KODAIKANAL_SOLAR_OBSERVATORY =
            ObservationSite("Kodaikanal Solar Observatory - IN", 10.232222, 77.464722, 2343.0, 1)
        val KONKOLY_OBSERVATORY = ObservationSite("Konkoly Observatory - HU", 47.5, 18.965, 0.0, 3)
        val KOELNER_OBSERVATORIUM_FUER_SUBMILLIMETER_ASTRONOMIE =
            ObservationSite("Koelner Observatorium fuer SubMillimeter Astronomie - CH", 45.984444, 7.785833, 3135.0, 1)
        val KUFFNER_OBSERVATORY = ObservationSite("Kuffner observatory - AT", 48.2125, 16.291111, 0.0, 6)
        val KVISTABERG_OBSERVATORY = ObservationSite("Kvistaberg Observatory - SE", 59.50167, 17.60667, 0.0, 2)
        val KYUNG_HEE_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Kyung Hee Astronomical Observatory - KR", 37.238611, 127.082222, 0.0, 5)
        val LA_CANADA_OBSERVATORY = ObservationSite("La Canada Observatory - ES", 40.603917, -4.49315, 1400.0, 1)
        val LADD_OBSERVATORY = ObservationSite("Ladd Observatory - US", 41.838889, -71.399167, 69.0, 1)
        val LAKE_AFTON_PUBLIC_OBSERVATORY =
            ObservationSite("Lake Afton Public Observatory - US", 37.6222, -97.6269, 0.0, 1)
        val LANDESSTERNWARTE_HEIDELBERG_KONIGSTUHL =
            ObservationSite("Landessternwarte Heidelberg-Königstuhl - DE", 49.3981, 8.725342, 0.0, 3)
        val LA_PLATA_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("La Plata Astronomical Observatory - AR", -34.908325, -57.93332, 0.0, 4)
        val LAS_BRISAS_OBSERVATORY = ObservationSite("Las Brisas Observatory - US", 38.8767, -105.2775, 2591.0, 1)
        val LAWS_OBSERVATORY = ObservationSite("Laws Observatory - US", 38.9438, -92.3228, 0.0, 1)
        val LEANDER_MCCORMICK_OBSERVATORY =
            ObservationSite("Leander McCormick Observatory - US", 38.032703, -78.522392, 264.0, 1)
        val LEE_OBSERVATORY = ObservationSite("Lee Observatory - LB", 33.900544, 35.479803, 38.0, 5)
        val LEIDEN_OBSERVATORY = ObservationSite("Leiden Observatory - NL", 52.155, 4.485, 0.0, 5)
        val LEONCITO_ASTRONOMICAL_COMPLEX =
            ObservationSite("Leoncito Astronomical Complex - AR", -31.7986, -69.2956, 2483.0, 1)
        val TYEE_EDUCATIONAL_COMPLEX = ObservationSite("Tyee Educational Complex - US", 47.435, -122.277, 0.0, 1)
        val LICK_OBSERVATORY = ObservationSite("Lick Observatory - US", 37.341389, -121.642778, 1283.0, 1)
        val LONG_WAVELENGTH_ARRAY = ObservationSite("Long Wavelength Array - US", 34.07, -107.63, 0.0, 1)
        val LULIN_OBSERVATORY = ObservationSite("Lulin Observatory - TW", 23.468611, 120.873611, 2863.0, 1)
        val LUND_OBSERVATORY = ObservationSite("Lund Observatory - SE", 55.69833, 13.18667, 0.0, 4)
        val LYON_OBSERVATORY = ObservationSite("Lyon Observatory - FR", 45.694722, 4.7825, 266.0, 3)
        val MACALESTER_COLLEGE_OBSERVATORY =
            ObservationSite("Macalester College Observatory - US", 44.936444, -93.169583, 0.0, 4)
        val MALAKOFF_TOWER = ObservationSite("Malakoff Tower - BR", -8.0608, -34.8708, 0.0, 4)
        val MANASTASH_RIDGE_OBSERVATORY =
            ObservationSite("Manastash Ridge Observatory - US", 46.9511, -120.7245, 1198.0, 1)
        val MARAGHEH_OBSERVATORY = ObservationSite("Maragheh observatory - IR", 37.396078, 46.209158, 0.0, 1)
        val MARGARET_M_JACOBY_OBSERVATORY =
            ObservationSite("Margaret M. Jacoby Observatory - US", 41.714444, -71.4825, 0.0, 5)
        val MARIA_MITCHELL_OBSERVATORY = ObservationSite("Maria Mitchell Observatory - US", 41.28, -70.105, 0.0, 3)
        val MARKREE_OBSERVATORY = ObservationSite("Markree Observatory - IE", 54.174286, -8.46147, 0.0, 1)
        val MARSEILLE_OBSERVATORY = ObservationSite("Marseille Observatory - FR", 43.308889, 5.385833, 0.0, 1)
        val MARTZ_OBSERVATORY = ObservationSite("Martz Observatory - US", 42.008611, -79.066667, 0.0, 1)
        val MAYNARD_F_JORDAN_OBSERVATORY =
            ObservationSite("Maynard F. Jordan Observatory - US", 44.898889, -68.6675, 0.0, 1)
        val MCDONALD_OBSERVATORY = ObservationSite("McDonald Observatory - US", 30.671389, -104.0225, 2070.0, 1)
        val MCKIM_OBSERVATORY = ObservationSite("McKim Observatory - US", 39.645556, -86.852222, 259.0, 1)
        val MCMATH_HULBERT_SOLAR_OBSERVATORY =
            ObservationSite("McMath–Hulbert Solar Observatory - US", 42.697648, -83.319175, 0.0, 3)
        val MDM_OBSERVATORY = ObservationSite("MDM Observatory - US", 31.951667, -111.615833, 0.0, 1)
        val MEAD_OBSERVATORY = ObservationSite("Mead Observatory - US", 32.459722, -84.995556, 73.0, 5)
        val C_E_K_MEES_OBSERVATORY = ObservationSite("C. E. K. Mees Observatory - US", 42.700278, -77.408767, 720.0, 1)
        val MEHALSO_OBSERVATORY = ObservationSite("Mehalso Observatory - US", 42.118417, -79.9875, 0.0, 1)
        val MELBOURNE_OBSERVATORY = ObservationSite("Melbourne Observatory - AU", -37.8297, 144.9755, 0.0, 1)
        val MELTON_MEMORIAL_OBSERVATORY =
            ObservationSite("Melton Memorial Observatory - US", 33.9975, -81.026389, 0.0, 5)
        val MENDENHALL_OBSERVATORY = ObservationSite("Mendenhall Observatory - US", 36.070472, -97.193833, 0.0, 1)
        val MENKE_OBSERVATORY = ObservationSite("Menke Observatory - US", 41.771944, -90.794306, 0.0, 1)
        val METSAHOVI_RADIO_OBSERVATORY =
            ObservationSite("Metsähovi Radio Observatory - FI", 60.218056, 24.393889, 0.0, 1)
        val PARIS_OBSERVATORY = ObservationSite("Paris Observatory - FR", 48.836439, 2.336506, 0.0, 5)
        val MICHIGAN_STATE_UNIVERSITY_OBSERVATORY =
            ObservationSite("Michigan State University Observatory - US", 42.706389, -84.482222, 264.0, 1)
        val TOULOUSE_OBSERVATORY = ObservationSite("Toulouse Observatory - FR", 43.61233, 1.46278, 1000.0, 1)
        val MILLER_OBSERVATORY = ObservationSite("Miller Observatory - US", 35.57577, -81.20614, 0.0, 1)
        val MILLS_OBSERVATORY = ObservationSite("Mills Observatory - GB", 56.464981, -3.012575, 0.0, 1)
        val MMT_OBSERVATORY = ObservationSite("MMT Observatory - US", 31.68833, -110.885, 0.0, 1)
        val ASTRONOMICKE_OBSERVATORIUM_MODRA =
            ObservationSite("Astronomické observatórium Modra - SK", 48.373273, 17.274021, 531.0, 1)
        val MOLETAI_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Moletai Astronomical Observatory - LT", 55.315972, 25.563333, 200.0, 1)
        val MOLONGLO_OBSERVATORY_SYNTHESIS_TELESCOPE =
            ObservationSite("Molonglo Observatory Synthesis Telescope - AU", -35.3707, 149.4241, 0.0, 1)
        val MONT_MEGANTIC_OBSERVATORY = ObservationSite("Mont Megantic Observatory - CA", 45.4558, -71.1525, 1111.0, 1)
        val MONTEREY_INSTITUTE_FOR_RESEARCH_IN_ASTRONOMY =
            ObservationSite("Monterey Institute for Research in Astronomy - US", 36.6609, -121.8087, 0.0, 1)
        val MOORE_OBSERVATORY = ObservationSite("Moore Observatory - US", 38.344444, -85.528889, 230.0, 1)
        val MORGAN_MONROE_OBSERVATORY =
            ObservationSite("Morgan–Monroe Observatory - US", 39.313611, -86.434444, 245.0, 1)
        val CHARLES_S_MORRIS_OBSERVATORY =
            ObservationSite("Charles S. Morris Observatory - US", 41.013167, -85.757806, 0.0, 1)
        val MORRISON_OBSERVATORY = ObservationSite("Morrison Observatory - US", 39.15167, -92.69667, 228.0, 1)
        val MOUNT_STONY_BROOK_OBSERVATORY =
            ObservationSite("Mount Stony Brook Observatory - US", 40.91476, -73.12568, 0.0, 1)
        val MOUNDS_OBSERVATORY = ObservationSite("Mounds Observatory - US", 35.831233, -96.146, 0.0, 3)
        val MOUNT_CUBA_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Mount Cuba Astronomical Observatory - US", 39.785, -75.63333, 1000.0, 1)
        val MOUNT_JOHN_UNIVERSITY_OBSERVATORY =
            ObservationSite("Mount John University Observatory - NZ", -43.986667, 170.465, 1029.0, 1)
        val MOUNT_LAGUNA_OBSERVATORY = ObservationSite("Mount Laguna Observatory - US", 32.8424, -116.428, 1859.0, 1)
        val MOUNT_LEMMON_OBSERVATORY = ObservationSite("Mount Lemmon Observatory - US", 32.442, -110.7893, 2791.0, 1)
        val MOUNT_PLEASANT_RADIO_OBSERVATORY =
            ObservationSite("Mount Pleasant Radio Observatory - AU", -42.805, 147.439167, 43.0, 1)
        val MOUNT_STROMLO_OBSERVATORY =
            ObservationSite("Mount Stromlo Observatory - AU", -35.320278, 149.006944, 0.0, 1)
        val OBSERWATORIUM_ASTRONOMICZNE_NA_SUHORZE =
            ObservationSite("Obserwatorium astronomiczne na Suhorze - PL", 49.569167, 20.0675, 1009.0, 1)
        val MOUNTAIN_SKIES_OBSERVATORY = ObservationSite("Mountain Skies Observatory - US", 41.328, -110.292, 2103.0, 2)
        val MULLARD_RADIO_ASTRONOMY_OBSERVATORY =
            ObservationSite("Mullard Radio Astronomy Observatory - GB", 52.1674, 0.03264, 0.0, 0)
        val NACHI_KATSUURA_OBSERVATORY = ObservationSite("Nachi-Katsuura Observatory - JP", 33.61, 135.924722, 0.0, 1)
        val NATIONAL_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("National Astronomical Observatory - MX", 31.0439, -115.4637, 2800.0, 1)
        val NATIONAL_OBSERVATORY = ObservationSite("National Observatory - BR", -22.8953, -43.2246, 10.0, 5)
        val NATIONAL_OBSERVATORY_OF_ATHENS =
            ObservationSite("National Observatory of Athens - GR", 37.97333, 23.72, 0.0, 1)
        val TUBITAK_ULUSAL_GOZLEMEVI = ObservationSite("TUBITAK Ulusal Gozlemevi - TR", 36.824167, 30.335556, 2450.0, 1)
        val NATIONAL_SOLAR_OBSERVATORY = ObservationSite("National Solar Observatory - US", 32.788, -105.82, 0.0, 1)
        val NAYLOR_OBSERVATORY = ObservationSite("Naylor Observatory - US", 40.1469, -76.8989, 170.0, 1)
        val NEUCHATEL_OBSERVATORY = ObservationSite("Neuchatel Observatory - CH", 47.0, 6.9529, 0.0, 1)
        val OBSERVATOIRE_DE_NICE = ObservationSite("Observatoire de Nice - FR", 43.72744, 7.29907, 372.0, 1)
        val NORMAN_LOCKYER_OBSERVATORY_AND_PLANETARIUM =
            ObservationSite("Norman Lockyer Observatory and Planetarium - GB", 50.685, -3.22, 0.0, 1)
        val NORDIC_OPTICAL_TELESCOPE = ObservationSite("Nordic Optical Telescope - ES", 28.757222, -17.885, 2382.0, 1)
        val NORTH_GEORGIA_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("North Georgia Astronomical Observatory - US", 34.520833, 84.053889, 0.0, 1)
        val NORTH_OTAGO_ASTRONOMICAL_SOCIETY_OBSERVATORY =
            ObservationSite("North Otago Astronomical Society Observatory - NZ", -45.091482, 170.95801, 0.0, 1)
        val NYROLAN_OBSERVATORIO = ObservationSite("Nyrölän observatorio - FI", 62.342222, 25.513056, 200.0, 1)
        val O_BRIEN_OBSERVATORY = ObservationSite("O'Brien Observatory - US", 45.1815, -92.7757, 308.0, 4)
        val OIL_REGION_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Oil Region Astronomical Observatory - US", 41.470278, -79.783611, 0.0, 0)
        val ONDOKUZ_MAYIS_UNIVERSITY_OBSERVATORY =
            ObservationSite("Ondokuz Mayıs University Observatory - TR", 41.368333, 36.200833, 165.0, 1)
        val ONDREJOV_OBSERVATORY = ObservationSite("Ondrejov Observatory - CZ", 49.915175, 14.780994, 500.0, 1)
        val ONSALA_SPACE_OBSERVATORY = ObservationSite("Onsala Space Observatory - SE", 57.393056, 11.917778, 20.0, 1)
        val OOTY_RADIO_TELESCOPE = ObservationSite("Ooty Radio Telescope - IN", 11.383404, 76.66616, 2240.0, 1)
        val ORCHARD_HILL_OBSERVATORY = ObservationSite("Orchard Hill Observatory - US", 42.394061, -72.521564, 80.0, 3)
        val ORIOLOROMANO_OBSERVATORY = ObservationSite("Orioloromano Observatory - IT", 42.169528, 12.138, 400.0, 1)
        val OTTER_CREEK_OBSERVATORY = ObservationSite("Otter Creek Observatory - US", 38.07307, -86.016362, 0.0, 2)
        val OWENS_VALLEY_RADIO_OBSERVATORY =
            ObservationSite("Owens Valley Radio Observatory - US", 37.233889, -118.282222, 1222.0, 1)
        val PACHMARHI_ARRAY_OF_CERENKOV_TELESCOPES =
            ObservationSite("Pachmarhi Array of Cerenkov Telescopes - IN", 22.466667, 76.433333, 1075.0, 1)
        val PALISADES_DOWS_OBSERVATORY =
            ObservationSite("Palisades-Dows Observatory - US", 41.889333, -91.500167, 0.0, 1)
        val PANZANO_OBSERVATORY = ObservationSite("Panzano Observatory - IT", 44.621122, 11.041724, 0.0, 4)
        val PARKES_OBSERVATORY = ObservationSite("Parkes Observatory - AU", -32.999944, 148.262306, 0.0, 1)
        val PAUL_ROBINSON_OBSERVATORY = ObservationSite("Paul Robinson Observatory - US", 40.681806, -74.898056, 0.0, 1)
        val PEACH_MOUNTAIN_OBSERVATORY = ObservationSite("Peach Mountain Observatory - US", 42.3988, -83.9355, 315.0, 1)
        val PERKINS_OBSERVATORY = ObservationSite("Perkins Observatory - US", 40.251036, -83.055825, 0.0, 2)
        val PERTH_OBSERVATORY = ObservationSite("Perth Observatory - AU", -32.007778, 116.135278, 0.0, 1)
        val PICO_DOS_DIAS_OBSERVATORY =
            ObservationSite("Pico dos Dias Observatory - BR", -22.534444, -45.5825, 1864.0, 1)
        val PIERA_OBSERVATORY = ObservationSite("Piera Observatory - ES", 41.5215, 1.7553, 0.0, 2)
        val PINE_BLUFF_OBSERVATORY = ObservationSite("Pine Bluff Observatory - US", 43.0777, -89.6717, 362.0, 1)
        val PINE_MOUNTAIN_OBSERVATORY = ObservationSite("Pine Mountain Observatory - US", 43.7915, -120.9408, 1980.0, 1)
        val OSSERVATORIO_ASTRONOMICO_DELLA_MONTAGNA_PISTOIESE =
            ObservationSite("Osservatorio Astronomico della Montagna Pistoiese - IT", 44.063056, 10.804167, 1000.0, 1)
        val POZNAN_OBSERVATORY = ObservationSite("Poznan Observatory - PL", 52.3944, 16.8745, 0.0, 3)
        val PULKOVO_OBSERVATORY = ObservationSite("Pulkovo Observatory - RU", 59.771667, 30.326111, 0.0, 3)
        val PURPLE_MOUNTAIN_OBSERVATORY =
            ObservationSite("Purple Mountain Observatory - CN", 32.066667, 118.816667, 267.0, 4)
        val QUITO_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Quito Astronomical Observatory - EC", -0.214886, -78.502555, 10.0, 5)
        val RADCLIFFE_OBSERVATORY = ObservationSite("Radcliffe Observatory - GB", 51.7608, -1.2639, 0.0, 1)
        val RAINWATER_OBSERVATORY_AND_PLANETARIUM =
            ObservationSite("Rainwater Observatory and Planetarium - US", 33.28725, -89.385139, 0.0, 1)
        val RALPH_A_WORLEY_OBSERVATORY =
            ObservationSite("Ralph A. Worley Observatory - US", 32.319753, -93.621508, 328.0, 1)
        val RATTLESNAKE_MOUNTAIN_OBSERVATORY =
            ObservationSite("Rattlesnake Mountain Observatory - US", 46.405556, -119.610556, 1075.0, 1)
        val RED_BARN_OBSERVATORY = ObservationSite("Red Barn Observatory - US", 31.387983, -83.651986, 107.0, 2)
        val RED_BUTTES_OBSERVATORY = ObservationSite("Red Buttes Observatory - US", 41.176389, -105.573889, 2246.0, 1)
        val ROCHESTER_INSTITUTE_OF_TECHNOLOGY_OBSERVATORY =
            ObservationSite("Rochester Institute of Technology Observatory - US", 43.0758, -77.6647, 168.0, 2)
        val RITTER_OBSERVATORY = ObservationSite("Ritter Observatory - US", 41.6624, -83.6123, 182.0, 5)
        val ROBERT_BROWNLEE_OBSERVATORY =
            ObservationSite("Robert Brownlee Observatory - US", 34.23083, -117.20972, 0.0, 1)
        val ROBINSON_OBSERVATORY = ObservationSite("Robinson Observatory - US", 28.59175, -81.19062, 0.0, 1)
        val ROBOTIC_LUNAR_OBSERVATORY = ObservationSite("Robotic Lunar Observatory - US", 35.2148, -111.6344, 2146.0, 5)
        val JOSEPH_H_ROGERS_OBSERVATORY =
            ObservationSite("Joseph H. Rogers Observatory - US", 44.706667, -85.775556, 257.0, 4)
        val ROLNICK_OBSERVATORY = ObservationSite("Rolnick Observatory - US", 41.171111, -73.3305, 0.0, 5)
        val ROME_OBSERVATORY = ObservationSite("Rome Observatory - IT", 41.92167, 12.45167, 0.0, 7)
        val ROTHNEY_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Rothney Astrophysical Observatory - CA", 50.8684, -114.291, 1269.0, 3)
        val ROYAL_OBSERVATORY_1 = ObservationSite("Royal Observatory - GB", 51.477811, -0.001475, 0.0, 7)
        val ROYAL_OBSERVATORY_2 = ObservationSite("Royal Observatory - GB", 55.923056, -3.187778, 146.0, 3)
        val OBSERVATORIO_DEL_ROQUE_DE_LOS_MUCHACHOS =
            ObservationSite("Observatorio del Roque de los Muchachos - ES", 28.766667, -17.883333, 2396.0, 1)
        val ROSEMARY_HILL_OBSERVATORY = ObservationSite("Rosemary Hill Observatory - US", 29.4001, -82.5862, 23.0, 4)
        val ROZHEN_OBSERVATORY = ObservationSite("Rozhen Observatory - BG", 41.6925, 24.738056, 1759.0, 2)
        val RUTHERFORD_OBSERVATORY = ObservationSite("Rutherford Observatory - US", 40.81, -73.9614, 0.0, 2)
        val SANKT_ANDREASBERG_OBSERVATORY =
            ObservationSite("Sankt Andreasberg Observatory - DE", 51.73184, 10.52605, 710.0, 2)
        val SFA_OBSERVATORY = ObservationSite("SFA Observatory - US", 31.7599, -94.661, 148.0, 2)
        val SAGAMORE_HILL_RADIO_OBSERVATORY =
            ObservationSite("Sagamore Hill Radio Observatory - US", 42.6323, -70.8201, 59.0, 4)
        val SHATTUCK_OBSERVATORY = ObservationSite("Shattuck Observatory - US", 43.705, -72.285278, 0.0, 1)
        val SEVEN_HILLS_OBSERVATORY = ObservationSite("Seven Hills Observatory - US", 40.719908, -99.155544, 0.0, 0)
        val SHAMAKHI_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Shamakhi Astrophysical Observatory - AZ", 40.772222, 48.584444, 0.0, 1)
        val SHANGHAI_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Shanghai Astronomical Observatory - CN", 31.190278, 121.429444, 100.0, 7)
        val SHERZER_OBSERVATORY = ObservationSite("Sherzer Observatory - US", 42.263892, -83.6246, 0.0, 4)
        val SIDING_SPRING_OBSERVATORY =
            ObservationSite("Siding Spring Observatory - AU", -31.273333, 149.064444, 1165.0, 1)
        val NORMAN_LOCKYER_OBSERVATORY =
            ObservationSite("Norman Lockyer Observatory - GB", 50.68803, -3.219835, 10.0, 4)
        val SIERRA_NEVADA_OBSERVATORY =
            ObservationSite("Sierra Nevada Observatory - ES", 37.064167, -3.384722, 2896.0, 1)
        val SILESIAN_PLANETARIUM_AND_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Silesian Planetarium and Astronomical Observatory - PL", 50.290469, 18.991469, 0.0, 4)
        val SIMEIZ_OBSERVATORY = ObservationSite("Simeiz Observatory - RU", 44.418039, 33.9974, 0.0, 1)
        val SKALNATE_PLESO_OBSERVATORY =
            ObservationSite("Skalnate Pleso Observatory - SK", 49.189381, 20.233819, 0.0, 3)
        val SMITHSONIAN_ASTROPHYSICAL_OBSERVATORY =
            ObservationSite("Smithsonian Astrophysical Observatory - US", 42.38146, -71.12837, 0.0, 6)
        val SOLA_FIDE_OBSERVATORY = ObservationSite("Sola Fide Observatory - US", 43.618959, -92.987294, 0.0, 3)
        val SOMMERS_BAUSCH_OBSERVATORY =
            ObservationSite("Sommers–Bausch Observatory - US", 40.003722, -105.2625, 1653.0, 1)
        val SONNENBORGH_OBSERVATORY = ObservationSite("Sonnenborgh Observatory - NL", 52.08667, 5.13, 0.0, 7)
        val SONOMA_STATE_OBSERVATORY = ObservationSite("Sonoma State Observatory - US", 38.336667, -122.6675, 53.0, 1)
        val SOUTH_AFRICAN_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("South African Astronomical Observatory - ZA", -33.9347, 18.4776, 0.0, 6)
        val SOUTHERN_ASTROPHYSICAL_RESEARCH_TELESCOPE =
            ObservationSite("Southern Astrophysical Research Telescope - CH", -30.238, -70.733722, 2738.0, 1)
        val SOUTH_POLE_TELESCOPE = ObservationSite("South Pole Telescope - AQ", -90.0, -139.266667, 2835.0, 1)
        val SPANISH_NATIONAL_OBSERVATORY = ObservationSite("Spanish National Observatory - ES", 40.411, -3.678, 10.0, 3)
        val WILLIAM_MILLER_SPERRY_OBSERVATORY =
            ObservationSite("William Miller Sperry Observatory - US", 40.666389, -74.323333, 23.0, 4)
        val SPROUL_OBSERVATORY = ObservationSite("Sproul Observatory - US", 39.903611, -75.355278, 60.0, 3)
        val STARDOME_OBSERVATORY = ObservationSite("Stardome Observatory - NZ", -36.905978, 174.776736, 0.0, 1)
        val STARKENBURG_OBSERVATORY = ObservationSite("Starkenburg Observatory - DE", 49.648056, 8.653056, 0.0, 3)
        val STEFANIK_S_OBSERVATORY = ObservationSite("Stefanik's Observatory - CZ", 50.081194, 14.398222, 0.0, 5)
        val STJERNEBORG = ObservationSite("Stjerneborg - DK", 55.9068, 12.6967, 0.0, 3)
        val STOCKHOLM_OBSERVATORY = ObservationSite("Stockholm Observatory - SE", 59.27167, 18.30833, 0.0, 3)
        val STOKESVILLE_OBSERVATORY = ObservationSite("Stokesville Observatory - US", 38.352361, -79.151944, 0.0, 1)
        val STONYHURST_OBSERVATORY = ObservationSite("Stonyhurst Observatory - GB", 53.8, -2.5, 115.0, 1)
        val OBSERVATORY_OF_STRASBOURG = ObservationSite("Observatory of Strasbourg - FR", 48.583333, 7.768056, 142.0, 1)
        val STULL_OBSERVATORY = ObservationSite("Stull Observatory - US", 42.25, -77.783333, 0.0, 1)
        val STUTTGART_OBSERVATORY = ObservationSite("Stuttgart Observatory - DE", 48.875, 9.59667, 0.0, 5)
        val SUDBURY_NEUTRINO_OBSERVATORY =
            ObservationSite("Sudbury Neutrino Observatory - CA", 46.466667, -81.172778, -2000.0, 1)
        val SUNRIVER_OBSERVATORY = ObservationSite("Sunriver Observatory - US", 43.885083, -121.447611, 1269.0, 1)
        val SYDNEY_OBSERVATORY = ObservationSite("Sydney Observatory - AU", -33.859574, 151.204576, 0.0, 4)
        val TABLE_MOUNTAIN_OBSERVATORY =
            ObservationSite("Table Mountain Observatory - US", 34.382, -117.6818, 2286.0, 1)
        val TAEDUK_RADIO_ASTRONOMY_OBSERVATORY =
            ObservationSite("Taeduk Radio Astronomy Observatory - KR", 36.397586, 127.375208, 110.0, 2)
        val TAMKE_ALLAN_OBSERVATORY = ObservationSite("Tamke-Allan Observatory - US", 35.8325, -84.617833, 336.0, 1)
        val TARTU_OBSERVATOORIUM = ObservationSite("Tartu Observatoorium - EE", 58.265833, 26.466111, 0.0, 4)
        val TEN_ACRE_OBSERVATORY = ObservationSite("Ten Acre Observatory - US", 35.0886, -97.1207, 0.0, 1)
        val THAI_NATIONAL_OBSERVATORY =
            ObservationSite("Thai National Observatory - TH", 18.590549, 98.486546, 2457.0, 1)
        val THE_HEIGHTS_OBSERVATORY = ObservationSite("The Heights Observatory - AU", -34.812439, 138.682356, 166.0, 4)
        val TEXAS_A_M_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Texas A&M Astronomical Observatory - US", 30.572717, -96.36665, 86.0, 3)
        val THEODOR_JACOBSEN_OBSERVATORY =
            ObservationSite("Theodor Jacobsen Observatory - US", 47.660432, -122.3092, 0.0, 6)
        val THOMPSON_OBSERVATORY = ObservationSite("Thompson Observatory - US", 42.505, -89.03167, 0.0, 1)
        val TUORLA_OBSERVATORY = ObservationSite("Tuorla Observatory - FI", 60.415833, 22.443333, 60.0, 1)
        val ASTRONOMICAL_OBSERVATORY_OF_TRIESTE =
            ObservationSite("Astronomical Observatory of Trieste - IT", 45.644836, 13.774047, 0.0, 4)
        val OBSERVATORY_OF_TURIN = ObservationSite("Observatory of Turin - IT", 45.040556, 7.763333, 0.0, 4)
        val UDAIPUR_SOLAR_OBSERVATORY = ObservationSite("Udaipur Solar Observatory - IN", 24.604589, 73.674189, 0.0, 1)
        val ULUGH_BEG_OBSERVATORY = ObservationSite("Ulugh Beg Observatory - UZ", 39.675, 67.005, 0.0, 3)
        val UNIVERSITY_OF_ALABAMA_OBSERVATORY =
            ObservationSite("University of Alabama Observatory - US", 33.209682, -87.543886, 0.0, 1)
        val UNIVERSITY_OF_NORTH_ALABAMA_OBSERVATORY =
            ObservationSite("University of North Alabama Observatory - US", 34.809278, -87.6825, 0.0, 1)
        val UNIVERSITY_OF_ILLINOIS_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("University of Illinois Astronomical Observatory - US", 40.105244, -88.226128, 0.0, 1)
        val UNIVERSITY_OF_LONDON_OBSERVATORY =
            ObservationSite("University of London Observatory - GB", 51.6134, -0.242, 0.0, 5)
        val UNIVERSITY_OF_MARYLAND_OBSERVATORY =
            ObservationSite("University of Maryland Observatory - US", 39.001667, -76.956667, 53.0, 3)
        val UNIVERSITY_OF_NEW_HAMPSHIRE_OBSERVATORY =
            ObservationSite("University of New Hampshire Observatory - US", 43.1465, -70.9438, 31.0, 2)
        val UNIVERSITY_OF_OKLAHOMA_OBSERVATORY =
            ObservationSite("University of Oklahoma Observatory - US", 35.2025, -97.444056, 372.0, 2)
        val UPPSALA_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Uppsala Astronomical Observatory - SE", 59.837417, 17.647833, 0.0, 3)
        val URANIA = ObservationSite("Urania - AT", 48.211667, 16.383611, 0.0, 6)
        val URANIA_STERNWARTE = ObservationSite("Urania Sternwarte - CH", 47.3744, 8.5395, 0.0, 5)
        val URANIBORG = ObservationSite("Uraniborg - DK", 55.907778, 12.696111, 0.0, 1)
        val XINJIANG_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Xinjiang Astronomical Observatory - CN", 43.47, 87.18, 100.0, 1)
        val VALONGO_OBSERVATORY = ObservationSite("Valongo Observatory - BR", -22.898333, -43.186667, 14.0, 7)
        val VAINU_BAPPU_OBSERVATORY = ObservationSite("Vainu Bappu Observatory - IN", 12.566667, 78.833333, 700.0, 1)
        val VAN_VLECK_OBSERVATORY = ObservationSite("Van Vleck Observatory - US", 41.555, -72.659167, 65.0, 4)
        val VANDERBILT_UNIVERSITY_OBSERVATORY =
            ObservationSite("Vanderbilt University Observatory - US", 36.0524, -86.8053, 0.0, 2)
        val VASSAR_COLLEGE_OBSERVATORY = ObservationSite("Vassar College Observatory - US", 41.6875, -73.893611, 0.0, 2)
        val VATICAN_OBSERVATORY = ObservationSite("Vatican Observatory - IT", 41.747222, 12.650556, 430.0, 6)
        val VEEN_OBSERVATORY = ObservationSite("Veen Observatory - US", 42.904444, -85.403611, 210.0, 3)
        val VEGA_BRAY_OBSERVATORY = ObservationSite("Vega-Bray Observatory - US", 31.940833, -110.2575, 1180.0, 2)
        val VENTSPILS_INTERNATIONAL_RADIO_ASTRONOMY_CENTRE =
            ObservationSite("Ventspils International Radio Astronomy Centre - LV", 57.553493, 21.854916, 1.0, 2)
        val VIENNA_OBSERVATORY = ObservationSite("Vienna Observatory - AT", 48.231883, 16.333747, 0.0, 6)
        val VILNIUS_UNIVERSITY_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Vilnius University Astronomical Observatory - LT", 54.683111, 25.2865, 101.0, 5)
        val VISNJAN_OBSERVATORY = ObservationSite("Visnjan Observatory - HR", 45.2775, 13.726111, 10.0, 3)
        val OBSERVATORY_VSETIN = ObservationSite("Observatory Vsetin - CZ", 49.34425, 17.996281, 10.0, 3)
        val WARNER_OBSERVATORY = ObservationSite("Warner Observatory - US", 43.15361, -77.59, 156.0, 1)
        val WARNER_AND_SWASEY_OBSERVATORY =
            ObservationSite("Warner and Swasey Observatory - US", 41.536111, -81.568472, 0.0, 1)
        val WARREN_RUPP_OBSERVATORY = ObservationSite("Warren Rupp Observatory - US", 40.637778, -82.436389, 0.0, 2)
        val WASHBURN_OBSERVATORY = ObservationSite("Washburn Observatory - US", 43.076467, -89.4089, 0.0, 2)
        val WEAVER_STUDENT_OBSERVATORY =
            ObservationSite("Weaver Student Observatory - US", 36.661667, -121.808333, 3.0, 1)
        val WEITKAMP_OBSERVATORY = ObservationSite("Weitkamp Observatory - US", 40.125746, -82.937266, 0.0, 1)
        val WEST_MOUNTAIN_OBSERVATORY =
            ObservationSite("West Mountain Observatory - US", 40.08741, -111.82604, 2120.0, 2)
        val WHITIN_OBSERVATORY = ObservationSite("Whitin Observatory - US", 42.295, -71.30333, 0.0, 4)
        val WIDENER_UNIVERSITY_OBSERVATORY =
            ObservationSite("Widener University Observatory - US", 39.862222, -75.358333, 0.0, 5)
        val WILDER_OBSERVATORY = ObservationSite("Wilder Observatory - US", 42.36575, -72.524167, 0.0, 3)
        val WILLARD_L_ECCLES_OBSERVATORY =
            ObservationSite("Willard L. Eccles Observatory - US", 38.5217, -113.2863, 2912.0, 1)
        val WILLIAM_BRYDONE_JACK_OBSERVATORY =
            ObservationSite("William Brydone Jack Observatory - CA", 45.948056, -66.640556, 1.0, 0)
        val WILLIAMS_OBSERVATORY = ObservationSite("Williams Observatory - US", 35.24415, -81.67095, 0.0, 1)
        val WINER_OBSERVATORY = ObservationSite("Winer Observatory - US", 31.665578, -110.601783, 0.0, 2)
        val WINFREE_OBSERVATORY = ObservationSite("Winfree Observatory - US", 37.439556, -79.173111, 0.0, 2)
        val WISE_OBSERVATORY = ObservationSite("Wise Observatory - IL", 30.595833, 34.763333, 875.0, 2)
        val WIYN_OBSERVATORY = ObservationSite("WIYN Observatory - US", 31.9575, -111.6008, 0.0, 1)
        val WYOMING_INFRARED_OBSERVATORY =
            ObservationSite("Wyoming Infrared Observatory - US", 41.1765, -105.574, 2943.0, 1)
        val YALE_STUDENT_OBSERVATORY = ObservationSite("Yale Student Observatory - US", 41.321, -72.922, 38.0, 1)
        val JANTAR_MANTAR = ObservationSite("Jantar Mantar - IN", 28.627108, 77.216478, 0.0, 7)
        val YERKES_OBSERVATORY = ObservationSite("Yerkes Observatory - US", 42.570278, -88.556111, 334.0, 3)
        val YORK_UNIVERSITY_OBSERVATORY =
            ObservationSite("York University Observatory - CA", 43.7739, -79.5073, 196.0, 6)
        val YUBA_CITY_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("Yuba City Astronomical Observatory - US", 39.11465, -121.634125, 0.0, 2)
        val ZIMMERWALD_OBSERVATORY = ObservationSite("Zimmerwald Observatory - CH", 46.87667, 7.465, 0.0, 2)
        val EPENDES_OBSERVATORY = ObservationSite("Ependes Observatory - CH", 46.76237, 7.13938, 680.0, 2)
        val UNIVERSITY_OF_COIMBRA_ASTRONOMICAL_OBSERVATORY =
            ObservationSite("University of Coimbra Astronomical Observatory - PT", 40.1992, -8.44418, 0.0, 2)
        val PISGAH_ASTRONOMICAL_RESEARCH_INSTITUTE =
            ObservationSite("Pisgah Astronomical Research Institute - US", 35.1996, -82.8724, 914.0, 0)
        val MOUNT_BURNETT_OBSERVATORY = ObservationSite("Mount Burnett Observatory - AU", -37.9727, 145.4954, 307.0, 3)
        val MOPRA_OBSERVATORY = ObservationSite("Mopra Observatory - AU", -31.2677, 149.0996, 866.0, 1)
        val MACQUARIE_UNIVERSITY_OBSERVATORY =
            ObservationSite("Macquarie University Observatory - AU", -33.77037, 151.111081, 0.0, 3)
        val GROVE_CREEK_OBSERVATORY = ObservationSite("Grove Creek Observatory - AU", -33.8296, 149.3666, 0.0, 1)
        val CANBERRA_DEEP_SPACE_COMMUNICATION_COMPLEX =
            ObservationSite("Canberra Deep Space Communication Complex - AU", -35.401389, 148.981667, 0.0, 1)
        val BALLARAT_MUNICIPAL_OBSERVATORY_AND_MUSEUM =
            ObservationSite("Ballarat Municipal Observatory and Museum - AU", -37.581088, 143.861182, 0.0, 5)
        val BATHURST_OBSERVATORY_RESEARCH_FACILITY =
            ObservationSite("Bathurst Observatory Research Facility - AU", -33.3865, 149.654517, 0.0, 2)
        val GUERNSEY_OBSERVATORY = ObservationSite("Guernsey Observatory - GB", 49.449276, 2.635795, 46.0, 2)
        val ENTRE_RIOS_DO_OESTE = ObservationSite("Entre Rios do Oeste - BR", -24.703889, -54.242222, 230.0, 4)
        val MARIPA = ObservationSite("Maripá - BR", -24.417778, -53.83, 402.0, 4)
        val MERCEDES = ObservationSite("Mercedes - BR", -24.453889, -54.161944, 415.0, 4)
        val PATO_BRAGADO = ObservationSite("Pato Bragado - BR", -24.625833, -54.225, 563.0, 4)
        val QUATRO_PONTES = ObservationSite("Quatro Pontes - BR", -24.575, -53.976944, 427.0, 4)
        val SAO_JOSE_DAS_PALMEIRAS = ObservationSite("São José das Palmeiras - BR", -24.837778, -54.063889, 563.0, 4)
        val NYKOBING_SJAELLAND = ObservationSite("Nykobing Sjaelland - DK", 55.925, 11.666667, 0.0, 4)
        val NYKOBING_MORS = ObservationSite("Nykobing Mors - DK", 56.795278, 8.859167, 0.0, 4)
        val BATTLESTEADS_OBSERVATORY = ObservationSite("Battlesteads Observatory - GB", 55.085817, -2.221499, 100.0, 2)
    }
}
