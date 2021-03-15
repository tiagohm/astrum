package br.tiagohm.astrum.cli

import java.io.IOException
import java.util.*

object CommandLineParser {

    fun parse(line: String): Array<String> {
        if (line.isEmpty()) return emptyArray()

        // Parse with a simple finite state machine

        var state = NORMAL
        val tokenizer = StringTokenizer(line, "\"\' ", true)
        val list = ArrayList<String>()
        val current = StringBuilder()
        var lastTokenHasBeenQuoted = false

        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken()

            when (state) {
                IN_QUOTE -> if (token == "'") {
                    lastTokenHasBeenQuoted = true
                    state = NORMAL
                } else {
                    current.append(token)
                }
                IN_DOUBLE_QUOTE -> if (token == "\"") {
                    lastTokenHasBeenQuoted = true
                    state = NORMAL
                } else {
                    current.append(token)
                }
                else -> {
                    when (token) {
                        "'" -> state = IN_QUOTE
                        "\"" -> state = IN_DOUBLE_QUOTE
                        " " -> {
                            if (lastTokenHasBeenQuoted || current.isNotEmpty()) {
                                list.add("$current")
                                current.clear()
                            }
                        }
                        else -> current.append(token)
                    }

                    lastTokenHasBeenQuoted = false
                }
            }
        }

        if (lastTokenHasBeenQuoted || current.isNotEmpty()) {
            list.add("$current")
        }

        if (state == IN_QUOTE || state == IN_DOUBLE_QUOTE) {
            throw IOException("Unbalanced quotes in $line")
        }

        return list.toTypedArray()
    }

    private const val NORMAL = 0
    private const val IN_QUOTE = 1
    private const val IN_DOUBLE_QUOTE = 2
}