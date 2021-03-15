package br.tiagohm.astrum.cli

import picocli.CommandLine

internal object ExecutionExceptionHandler : CommandLine.IExecutionExceptionHandler {

    override fun handleExecutionException(
        e: Exception,
        commandLine: CommandLine,
        parseResult: CommandLine.ParseResult,
    ): Int {
        e.message?.let { error(it) }
        return 0
    }
}