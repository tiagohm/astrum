package br.tiagohm.astrum.cli

import picocli.CommandLine

fun print(
    text: String,
    color: String = "green",
) = println(CommandLine.Help.Ansi.ON.string("@|$color,bold $text|@"))

fun error(text: String) = print(text, "red")

fun info(text: String) = print(text, "blue")

fun success(text: String) = print(text, "green")

fun warning(text: String) = print(text, "fg(202)")