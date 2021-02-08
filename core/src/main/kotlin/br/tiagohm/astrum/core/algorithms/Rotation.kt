package br.tiagohm.astrum.core.algorithms

import br.tiagohm.astrum.core.Consts

data class Rotation(
    val period: Double = 1.0,        // [deprecated] (sidereal) rotation period [earth days]
    val offset: Double = 0.0,        // [deprecated] rotation at epoch  [degrees]
    val epoch: Double = Consts.J2000,// JDE (JD TT) of epoch for these elements
    val obliquity: Double = 0.0,     // [deprecated] tilt of rotation axis w.r.t. ecliptic [radians]
    val ascendingNode: Double = 0.0, // [deprecated] long. of ascending node of equator on the ecliptic [radians]
    // These values are only in the modern algorithms. invalid if ra0=0.
    val w0: Double = 0.0,            // [deg] mean longitude of prime meridian along equator measured from intersection with ICRS plane at epoch.
    val w1: Double = 0.0,            // [deg/d] mean longitude motion. W=W0+d*W1.
)