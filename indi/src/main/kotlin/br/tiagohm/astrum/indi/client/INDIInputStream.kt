package br.tiagohm.astrum.indi.client

import java.io.InputStream

internal class INDIInputStream(val inputStream: InputStream) : InputStream() {

    private var pos = 0

    override fun read() = if (pos < FAKE_ROOT_TAG.length) FAKE_ROOT_TAG[pos++].toInt() else inputStream.read()

    override fun close() = inputStream.close()

    override fun available() = inputStream.available()

    override fun mark(readlimit: Int) = inputStream.mark(readlimit)

    override fun markSupported() = inputStream.markSupported()

    override fun reset() = inputStream.reset()

    companion object {

        private const val FAKE_ROOT_TAG = "<fakeRoot>"
    }
}