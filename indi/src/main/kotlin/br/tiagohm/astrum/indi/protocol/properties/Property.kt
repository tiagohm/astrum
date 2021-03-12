package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.protocol.vectors.PropertyVector

/**
 * One member of a vector.
 */
interface Property<T> {

    /**
     * The Vector that holds this property.
     */
    val vector: PropertyVector<T>

    /**
     * A name for identification purposes.
     */
    val name: String

    /**
     * A label for presentation purposes.
     */
    val label: String

    /**
     * Property value.
     */
    val value: T
}