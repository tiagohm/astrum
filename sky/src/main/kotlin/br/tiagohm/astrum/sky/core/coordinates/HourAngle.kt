package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Degrees
import br.tiagohm.astrum.sky.core.units.Radians

class HourAngle(val ha: Degrees, val dec: Radians) : Spherical(ha.radians, dec)