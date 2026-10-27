package org.ja.officesuite

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.util.zip.ZipInputStream

/** Extracts visible main-document text only. Never represents a round-trip DOCX editor. */
object DocxTextExtractor {
    private const val MAX_DOCUMENT_XML_BYTES = 8 * 1024 * 1024
    private const val MAX_ENTRIES = 5000
    private const val MAIN_DOCUMENT = "word/document.xml"
    private const val WORD_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"

    fun extract(input: InputStream): String {
        ZipInputStream(input.buffered()).use { zip ->
            var entries = 0
            while (true) {
                val entry = zip.nextEntry ?: break
                entries++
                require(entries <= MAX_ENTRIES) { "El archivo contiene demasiadas entradas" }
                if (entry.name == MAIN_DOCUMENT) {
                    val data = java.io.ByteArrayOutputStream()
                    val buffer = ByteArray(8192)
                    var total = 0
                    while (true) {
                        val read = zip.read(buffer)
                        if (read == -1) break
                        total += read
                        require(total <= MAX_DOCUMENT_XML_BYTES) { "El documento es demasiado grande" }
                        data.write(buffer, 0, read)
                    }
                    val factory = XmlPullParserFactory.newInstance().apply {
                        isNamespaceAware = true
                    }
                    val parser = factory.newPullParser()
                    parser.setInput(data.toByteArray().inputStream(), "UTF-8")
                    val out = StringBuilder()
                    var inText = false
                    var inParagraph = false
                    while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                        val inWordNamespace = parser.namespace == WORD_NS
                        when (parser.eventType) {
                            XmlPullParser.START_TAG -> if (inWordNamespace) when (parser.name) {
                                "p" -> { if (inParagraph) out.append('\n'); inParagraph = true }
                                "t" -> inText = true
                                "tab" -> out.append('\t')
                                "br", "cr" -> out.append('\n')
                            }
                            XmlPullParser.TEXT -> if (inText) out.append(parser.text)
                            XmlPullParser.END_TAG -> if (inWordNamespace) when (parser.name) {
                                "t" -> inText = false
                                "p" -> { out.append('\n'); inParagraph = false }
                            }
                        }
                        parser.next()
                    }
                    return out.toString().trimEnd()
                }
                zip.closeEntry()
            }
        }
        throw IllegalArgumentException("No se encontró word/document.xml en el DOCX")
    }
}
