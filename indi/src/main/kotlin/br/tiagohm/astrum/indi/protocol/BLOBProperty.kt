package br.tiagohm.astrum.indi.protocol

interface BLOBProperty : Property<ByteArray> {

    override val type: String
        get() = "BLOB"

    override fun valueToText(value: ByteArray): String {
        TODO("Not yet implemented")
    }
}