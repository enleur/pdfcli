package com.rozetkapay.pdfcli

import com.lowagie.text.pdf.BaseFont
import org.openpdf.pdf.ITextRenderer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText

fun main(args: Array<String>) {
    if (args.contains("-h") || args.contains("--help")) {
        printHelp()

        return
    }

    val opts = args.asSequence()
        .windowed(size = 2, step = 1)
        .filter { (key, value) -> key.startsWith("--") && !value.startsWith("--") }
        .associate { (key, value) -> key to value }

    val html = opts["--in"]?.takeIf { it != "-" }
        ?.let { Path.of(it).readText() }
        ?: System.`in`.readAllBytes().decodeToString()

    val output = opts["--out"]?.takeIf { it != "-" }
        ?.let { Files.newOutputStream(Path.of(it)) }
        ?: System.out

    output.use {
        ITextRenderer().apply {
            // Register fonts with Unicode support for Cyrillic characters
            fontResolver.addFont("fonts/ArialRegular.ttf", BaseFont.IDENTITY_H, true)
            setDocumentFromString(html, opts["--base"] ?: cwdAsUri())
            layout()
            createPDF(it)
        }
    }
}

private fun printHelp() {
    println(
        """
        HTML to PDF CLI (Native OpenPDF)
        
        Usage:
          pdfcli [--in <input.html|->] [--out <output.pdf|->] [--base <base-URI>]
        
        Notes:
          - If --in is omitted or '-', reads HTML from stdin.
          - If --out is omitted or '-', writes PDF to stdout.
          - --base sets the base URI for resolving relative links (defaults to current directory).
        """.trimIndent()
    )
}

private fun cwdAsUri(): String = Path.of(".").toAbsolutePath().normalize().toUri().toString()

