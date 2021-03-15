package br.tiagohm.astrum.cli

import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine
import picocli.CommandLine.RunAll
import java.nio.file.Paths
import java.util.*
import javax.swing.filechooser.FileSystemView
import kotlin.system.exitProcess

fun main(vararg args: String) {
    // Operating System
    val os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH)
    // System HOME directory
    val homeDir = FileSystemView.getFileSystemView().defaultDirectory.path
    // Application directory
    val appDir = when {
        os.contains("mac") || os.contains("darwin") -> Paths.get(homeDir, "Documents", "Astrum")
        os.contains("win") -> Paths.get(homeDir, "Documents", "Astrum")
        os.contains("nux") -> Paths.get(homeDir, ".config", "astrum")
        else -> throw IllegalArgumentException("Unsupported operating system")
    }.toFile().also { it.mkdir() }
    // Terminal
    val terminal = TerminalBuilder.builder().build()
    val lineReader = LineReaderBuilder.builder()
        .terminal(terminal)
        .appName("Astrum")
        .variable(LineReader.HISTORY_FILE, Paths.get(appDir.path, ".history").toFile())
        .build()
    // Commands
    val astrum = Astrum()
    // CLI
    val cli = CommandLine(astrum).apply {
        executionStrategy = RunAll()
        executionExceptionHandler = ExecutionExceptionHandler

        execute(*args)
    }
    // Process user input
    while (true) {
        try {
            // Read command line
            val line = lineReader.readLine()

            try {
                // Parse and execute the command line
                CommandLineParser.parse(line).also { cli.execute(*it) }
            } catch (e: Exception) {
                e.message?.let { red("[ERROR] $it") }
            }
        } catch (e: UserInterruptException) {
            exitProcess(0)
        } catch (e: EndOfFileException) {
            return
        }
    }
}

