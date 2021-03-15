package br.tiagohm.astrum.cli

import br.tiagohm.astrum.cli.commands.Telescope
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

fun main() {
    val homeDir = FileSystemView.getFileSystemView().defaultDirectory.path
    val os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH)

    val appDir = when {
        os.contains("mac") || os.contains("darwin") -> Paths.get(homeDir, "Documents", "Astrum")
        os.contains("win") -> Paths.get(homeDir, "Documents", "Astrum")
        os.contains("nux") -> Paths.get(homeDir, ".config", "astrum")
        else -> throw IllegalArgumentException("Unsupported operating system")
    }.toFile().also { it.mkdir() }

    val terminal = TerminalBuilder.builder().build()
    val lineReader = LineReaderBuilder.builder()
        .terminal(terminal)
        .appName("Astrum")
        .variable(LineReader.HISTORY_FILE, Paths.get(appDir.path, ".history").toFile())
        .build()

    val astrum = Astrum()
    val telescope = Telescope(astrum)

    val cli = CommandLine(astrum).apply {
        addSubcommand(telescope)
        executionStrategy = RunAll()
        executionExceptionHandler = ExecutionExceptionHandler
    }

    while (true) {
        try {
            val line = lineReader.readLine()

            try {
                val args = CommandLineParser.parse(line)

                if (args.isNotEmpty()) {
                    cli.execute(*args)
                }
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        } catch (e: UserInterruptException) {
            exitProcess(0)
        } catch (e: EndOfFileException) {
            return
        }
    }
}

