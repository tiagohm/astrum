package br.tiagohm.astrum.indi.protocol

interface BLOBElement : Element<ByteArray> {

    override val type: ElementType
        get() = ElementType.BLOB

    override fun convert(value: ByteArray): String {
        TODO("Not yet implemented")
    }
}