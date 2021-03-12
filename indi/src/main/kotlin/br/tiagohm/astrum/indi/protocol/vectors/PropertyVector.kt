package br.tiagohm.astrum.indi.protocol.vectors

import br.tiagohm.astrum.indi.protocol.Permission
import br.tiagohm.astrum.indi.protocol.State

interface PropertyVector<T> {

    val device: String

    /**
     * A name for identification purposes.
     */
    val name: String

    /**
     * A label for presentation purposes.
     */
    val label: String

    /**
     * Suggests how Clients might organize the Vector's properties,
     * for presentation purposes for example.
     */
    val group: String

    val state: State

    /**
     * It serves only as a hint to a Client as to whether a Device is
     * potentially willing to allow a Property to be changed.
     */
    val permission: Permission

    /**
     * Specifies the worst-case time it might take to change the value to something else.
     */
    val timeout: Int
}