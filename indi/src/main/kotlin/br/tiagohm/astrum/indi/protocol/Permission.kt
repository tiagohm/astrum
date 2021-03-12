package br.tiagohm.astrum.indi.protocol

enum class Permission {
    READ,
    WRITE,
    READ_WRITE;

    val isRead: Boolean
        get() = this == READ || this == READ_WRITE

    val isWrite: Boolean
        get() = this == WRITE || this == READ_WRITE
}