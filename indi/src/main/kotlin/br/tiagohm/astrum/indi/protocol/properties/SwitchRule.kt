package br.tiagohm.astrum.indi.protocol.properties

/**
 * Rules to be imposed on the behavior of the switches in a vector.
 */
enum class SwitchRule {
    ONE_OF_MANY,
    AT_MOST_ONE,
    ANY_OF_MANY
}