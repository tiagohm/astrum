package br.tiagohm.astrum.indi.client

interface Command<T> {

    /**
     * Gets the XML command to send to driver.
     */
    fun toXML(device: String, value: T): String
}