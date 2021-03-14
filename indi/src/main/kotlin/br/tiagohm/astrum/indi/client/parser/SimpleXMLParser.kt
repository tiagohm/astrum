package br.tiagohm.astrum.indi.client.parser

import java.io.IOException

/**
 * Simple XML Parser to parse INDI protocol.
 */
class SimpleXMLParser {

    private var state = TAG_START
    private var level = -1

    private var tagName = Array(MAX_LEVEL) { StringBuilder() }
    private var attrName = Array(MAX_LEVEL) { StringBuilder() }
    private var attrValue = Array(MAX_LEVEL) { StringBuilder() }
    private var text = Array(MAX_LEVEL) { StringBuilder() }
    private var hasSlash = Array(MAX_LEVEL) { false }

    private val attributes = Array(MAX_LEVEL) { ArrayList<String>(1) }
    private val children = ArrayList<XMLTag>(1)

    fun parse(i: Int) = if (i < 0) null else parse(i.toChar())

    fun parse(c: Char): XMLTag? {
        // Ignore
        if (c == '\r' || c == '\n' || c == '\t') {
            return null
        }
        // Start
        else if (state == TAG_START) {
            if (c == '<') {
                state = TAG_NAME
                level++
            } else if (c.isWhitespace()) {
                // ignore
            } else if (c == '/') {
                state = TAG_SLASH_END
            } else if (level >= 0 && c.isLetterOrDigit()) {
                level++
                state = TAG_NAME
                tagName[level].append(c)
            } else {
                throw IOException("Found invalid characted at TAG_START: $c")
            }
        }
        // Name
        else if (state == TAG_NAME) {
            if (c.isLetterOrDigit()) {
                tagName[level].append(c)
            } else if (c.isWhitespace()) {
                if (tagName[level].isNotEmpty()) {
                    state = WS
                }
            } else if (c == '/') {
                state = TAG_SLASH
            } else if (c == '>') {
                state = TAG_END
            } else {
                throw IOException("Found invalid characted at TAG_NAME: $c")
            }
        }
        // Slash
        else if (state == TAG_SLASH) {
            hasSlash[level] = true

            if (c == '>') {
                state = TAG_END
            } else {
                throw IOException("Found invalid characted at TAG_SLASH: $c")
            }
        }
        // Whitespace
        else if (state == WS) {
            if (c.isWhitespace()) {
                // ignore
            } else if (c.isLetterOrDigit()) {
                attrName[level].append(c)
                state = ATTR_NAME
            } else if (c == '/') {
                state = TAG_SLASH
            } else if (c == '>') {
                state = TAG_END
            }
        }
        // Attr Name
        else if (state == ATTR_NAME) {
            if (c.isLetterOrDigit()) {
                attrName[level].append(c)
            } else if (c == '=') {
                attributes[level].add(attrName[level].toString())
                attrName[level].clear()
                state = ATTR_EQUAL
            } else if (c == ' ') {
                // ignore
            } else {
                throw IOException("Found invalid characted at ATTR_NAME: $c")
            }
        }
        // Attr Equal
        else if (state == ATTR_EQUAL) {
            if (c == '"') {
                state = ATTR_START_QUOTE
            } else if (c == ' ') {
                // ignore
            } else {
                throw IOException("Found invalid characted at ATTR_EQUAL: $c")
            }
        }
        // Attr Start Quote
        else if (state == ATTR_START_QUOTE) {
            state = if (c == '"') {
                ATTR_END_QUOTE
            } else {
                attrValue[level].append(c)
                ATTR_VALUE
            }
        }
        // Attr Value
        else if (state == ATTR_VALUE) {
            if (c == '"') {
                state = ATTR_END_QUOTE
            } else {
                attrValue[level].append(c)
            }
        }
        // Attr End Quote
        else if (state == ATTR_END_QUOTE) {
            attributes[level].add(attrValue[level].toString())
            attrValue[level].clear()

            state = when {
                c.isWhitespace() -> WS
                c == '/' -> TAG_SLASH
                c == '>' -> TAG_END
                else -> throw IOException("Found invalid characted at ATTR_END_QUOTE: $c")
            }
        }
        // Text
        else if (state == TEXT) {
            if (c == '<') {
                state = TAG_START
            } else {
                text[level].append(c)
            }
        }
        // Tag Slash End
        else if (state == TAG_SLASH_END) {
            hasSlash[level] = true

            // TODO: Validation?
            if (c.isLetterOrDigit()) {
                state = TAG_NAME_END
            } else if (c.isWhitespace()) {
                // ignore
            } else {
                throw IOException("Found invalid characted at TAG_SLASH_END: $c")
            }
        }
        // Tag Name End
        else if (state == TAG_NAME_END) {
            if (c.isLetterOrDigit()) {
                // ignore
            } else if (c == '>') {
                state = TAG_END
            } else if (c.isWhitespace()) {
                // ignore
            } else {
                throw IOException("Found invalid characted at TAG_NAME_END: $c")
            }
        }

        // End
        if (state == TAG_END) {
            if (hasSlash[level]) {
                hasSlash[level] = false

                if (level == 0) {
                    return makeXMLTag(level--).also { reset() }
                } else {
                    children.add(makeXMLTag(level--))
                }
            } else if (c.isWhitespace()) {
                // ignore
            } else if (c == '<') {
                state = TAG_START
            } else {
                state = TEXT
            }
        }

        return null
    }

    private fun makeXMLTag(level: Int): XMLTag {
        val name = tagName[level].toString()
        val text = text[level].toString()
        val attr = if (attributes[level].isEmpty()) {
            emptyMap()
        } else {
            val size = attributes[level].size
            val res = LinkedHashMap<String, String>(size)

            for (i in 0 until size step 2) {
                res[attributes[level][i]] = attributes[level][i + 1]
            }

            res
        }

        val tag = XMLTag(name, attr, text.trim(), if (level == 0) children.toList() else emptyList())

        return tag.also {
            tagName[level].clear()
            this.text[level].clear()
            this.attributes[level].clear()
        }
    }

    fun reset() {
        state = TAG_START
        level = -1
        children.clear()
    }

    companion object {

        private const val MAX_LEVEL = 2
        private const val TAG_START = 0
        private const val TAG_NAME = 1
        private const val WS = 2
        private const val ATTR_NAME = 3
        private const val ATTR_EQUAL = 4
        private const val ATTR_START_QUOTE = 5
        private const val ATTR_VALUE = 6
        private const val ATTR_END_QUOTE = 7
        private const val TAG_SLASH = 8
        private const val TAG_END = 9
        private const val TEXT = 10
        private const val TAG_SLASH_END = 11
        private const val TAG_NAME_END = 12
    }
}