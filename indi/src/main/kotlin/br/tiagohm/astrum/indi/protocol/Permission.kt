package br.tiagohm.astrum.indi.protocol

enum class Permission {
    READ,
    WRITE,
    READ_WRITE;

    companion object {

        fun parse(text: String) = when (text) {
            "ro" -> READ
            "wo" -> WRITE
            else -> READ_WRITE
        }
    }
}