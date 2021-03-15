package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.drivers.Driver

interface DriverListener {

    fun onDriverAdded(driver: Driver)

    fun onDriverRemoved(driver: Driver)
}