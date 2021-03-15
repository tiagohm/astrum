package br.tiagohm.astrum.cli

import picocli.CommandLine

private fun show(text: String, color: String) {
    println(CommandLine.Help.Ansi.ON.string("@|$color,bold $text|@"))
}

fun showError(text: String) = show(text, "red")

fun showInfo(text: String) = show(text, "blue")

fun showSuccess(text: String) = show(text, "green")

fun showWarning(text: String) = show(text, "fg(202)")