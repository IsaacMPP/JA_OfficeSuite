package org.ja.officesuite

import android.content.Intent
import android.provider.OpenableColumns
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF172C40)
private val Teal = Color(0xFF007E91)
private val Paper = Color.White
private val Canvas = Color(0xFFF2F6F8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OfficeScreen() }
    }

    @Composable
    private fun OfficeScreen() {
        var document by rememberSaveable { mutableStateOf("") }
        var filename by rememberSaveable { mutableStateOf("Sin título.txt") }
        var fontSize by rememberSaveable { mutableFloatStateOf(17f) }
        var showInfo by remember { mutableStateOf(false) }
        var importedDocx by rememberSaveable { mutableStateOf(false) }
        val open = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) try {
                val displayedName = contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) cursor.getString(0) else null
                } ?: "Documento"
                val isDocx = displayedName.endsWith(".docx", ignoreCase = true) ||
                    contentResolver.getType(uri) == "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                val loaded = contentResolver.openInputStream(uri)?.use { stream ->
                    if (isDocx) DocxTextExtractor.extract(stream) else stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                } ?: error("No se pudo abrir el archivo")
                document = loaded
                filename = displayedName
                importedDocx = isDocx
                if (isDocx) Toast.makeText(this, "DOCX importado como texto. El formato original no se conserva.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al abrir: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
        val save = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
            if (uri != null) try {
                contentResolver.openOutputStream(uri, "wt")?.bufferedWriter(Charsets.UTF_8)?.use {
                    it.write(document)
                } ?: error("No se pudo guardar el archivo")
                Toast.makeText(this, "Documento guardado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al guardar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
        MaterialTheme(colorScheme = lightColorScheme(primary = Teal, background = Canvas, surface = Paper)) {
            BoxWithConstraints(Modifier.fillMaxSize().background(Canvas)) {
                val wide = maxWidth >= 700.dp
                Column(Modifier.fillMaxSize()) {
                    Surface(shadowElevation = 3.dp, color = Ink) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("JA OfficeSuite", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 21.sp)
                                Text(filename, color = Color(0xFFBFDBE2), fontSize = 12.sp, maxLines = 1)
                            }
                            TextButton(onClick = { showInfo = true }) {
                                Text("Acerca de", color = Color.White)
                            }
                        }
                    }
                    Surface(color = Paper, shadowElevation = 1.dp) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { open.launch(arrayOf("text/plain", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")) }) { Text("Abrir") }
                            Button(onClick = { save.launch(if (!importedDocx && filename.endsWith(".txt", true)) filename else filename.substringBeforeLast(".") + ".txt") }) {
                                Text("Guardar TXT")
                            }
                            Spacer(Modifier.weight(1f))
                            OutlinedButton(onClick = { fontSize = (fontSize - 1f).coerceAtLeast(12f) }, contentPadding = PaddingValues(horizontal = 10.dp)) {
                                Text("A−")
                            }
                            Text("${fontSize.toInt()}", fontSize = 13.sp, color = Ink)
                            OutlinedButton(onClick = { fontSize = (fontSize + 1f).coerceAtMost(30f) }, contentPadding = PaddingValues(horizontal = 10.dp)) {
                                Text("A+")
                            }
                        }
                    }
                    Box(Modifier.fillMaxSize().padding(if (wide) 24.dp else 12.dp), contentAlignment = Alignment.TopCenter) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(if (wide) 0.82f else 1f).fillMaxHeight(),
                            color = Paper,
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 2.dp
                        ) {
                            Column(Modifier.fillMaxSize().padding(if (wide) 36.dp else 20.dp)) {
                                if (document.isEmpty()) {
                                    Text("Empieza a escribir o importa TXT / DOCX", color = Color.Gray, fontSize = 13.sp)
                                    Spacer(Modifier.height(8.dp))
                                }
                                BasicTextField(
                                    value = document,
                                    onValueChange = { document = it },
                                    textStyle = TextStyle(color = Ink, fontSize = fontSize.sp, lineHeight = (fontSize * 1.5f).sp),
                                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Teal)
                                )
                            }
                        }
                    }
                }
                if (showInfo) {
                    AlertDialog(
                        onDismissRequest = { showInfo = false },
                        confirmButton = { TextButton(onClick = { showInfo = false }) { Text("Cerrar") } },
                        title = { Text("JA OfficeSuite · 0.3.0") },
                        text = { Text("Abre TXT e importa el texto principal de DOCX. DOCX no conserva estilos, tablas ni imágenes; guardar siempre genera TXT. El motor WordCraft todavía no está integrado y A−/A+ solo cambia la vista.") }
                    )
                }
            }
        }
    }
}
