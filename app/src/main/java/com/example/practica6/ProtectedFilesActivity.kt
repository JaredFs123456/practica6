package com.example.practica6

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProtectedFilesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val REQUEST_CODE_OPEN_DIRECTORY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ListView donde mostraremos los archivos reales
        listView = ListView(this)
        setContentView(listView)

        // Abrir selector de carpetas
        openDirectoryPicker()
    }

    private fun openDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val treeUri: Uri? = data?.data

            if (treeUri != null) {
                // Guardar permiso permanente
                contentResolver.takePersistableUriPermission(
                    treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                loadFilesFromUri(treeUri)
            }
        }
    }

    private fun loadFilesFromUri(uri: Uri) {
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        val fileList = mutableListOf<String>()

        val cursor = contentResolver.query(childrenUri, arrayOf(
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE
        ), null, null, null)

        cursor?.use {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                fileList.add(name)
            }
        }

        if (fileList.isEmpty()) {
            fileList.add("Carpeta vacía")
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            fileList
        )

        listView.adapter = adapter
    }
}
