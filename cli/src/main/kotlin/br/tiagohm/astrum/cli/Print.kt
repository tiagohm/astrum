package br.tiagohm.astrum.cli

import picocli.CommandLine

private fun show(text: String, color: String) = println(CommandLine.Help.Ansi.ON.string("@|$color,bold $text|@"))

fun blue(text: String) = show(text, "blue")

fun green(text: String) = show(text, "green")

fun red(text: String) = show(text, "red")

fun orange(text: String) = show(text, "fg(202)")

fun yellow(text: String) = show(text, "fg(11)")

fun grey(text: String) = show(text, "fg(8)")