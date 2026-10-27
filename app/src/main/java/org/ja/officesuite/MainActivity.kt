package org.ja.officesuite

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri

class MainActivity : Activity() {
    private lateinit var editor: EditText
    private val openDocument = 101
    private val saveDocument = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(249, 250, 252))
        }
        layout.addView(TextView(this).apply {
            text = "JA OfficeSuite"
            textSize = 26f
            setTextColor(Color.rgb(30, 68, 115))
            setPadding(0, 0, 0, 16)
        })
        val actions = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        actions.addView(Button(this).apply {
            text = "Abrir TXT"
            setOnClickListener {
                startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/plain"
                }, openDocument)
            }
        })
        actions.addView(Button(this).apply {
            text = "Guardar TXT"
            setOnClickListener {
                startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TITLE, "documento.txt")
                }, saveDocument)
            }
        })
        layout.addView(actions)
        editor = EditText(this).apply {
            hint = "Escribe aquí tu documento..."
            textSize = 17f
            gravity = Gravity.TOP or Gravity.START
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.WHITE)
            setPadding(20, 20, 20, 20)
            setText("Bienvenido a JA OfficeSuite\n\nEsta es la primera versión Android de prueba.")
        }
        layout.addView(editor, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(layout)
    }

    @Deprecated("Legacy activity result API, used only for initial prototype")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri = data?.data ?: return
        if (resultCode != RESULT_OK) return
        try {
            when (requestCode) {
                openDocument -> contentResolver.openInputStream(uri)?.bufferedReader()?.use { editor.setText(it.readText()) }
                saveDocument -> contentResolver.openOutputStream(uri, "wt")?.bufferedWriter()?.use { it.write(editor.text.toString()) }
            }
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "No se pudo procesar el archivo: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}
