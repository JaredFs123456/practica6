package com.example.practica6

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = getSharedPreferences("temas", MODE_PRIVATE)

        val btnCarpetas = findViewById<Button>(R.id.btnCarpetas)
        val btnSensores = findViewById<Button>(R.id.btnSensores)
        val btnTemaGuinda = findViewById<Button>(R.id.btnTemaGuinda)
        val btnTemaAzul = findViewById<Button>(R.id.btnTemaAzul)
        val swModoOscuro = findViewById<Switch>(R.id.swModoOscuro)

        // Leer preferencias guardadas
        val temaGuardado = prefs.getString("tema_actual", "guinda") ?: "guinda"
        val modoOscuroOn = prefs.getBoolean("modo_oscuro", false)

        // Configurar estado inicial del switch y emoji
        swModoOscuro.isChecked = modoOscuroOn
        swModoOscuro.text = if (modoOscuroOn) "🌙" else "☀️"

        // Aplicar tema con la combinación actual (tema + modo oscuro)
        aplicarTema(temaGuardado, modoOscuroOn)

        // Listener del switch ☀️ / 🌙
        swModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("modo_oscuro", isChecked).apply()
            swModoOscuro.text = if (isChecked) "🌙" else "☀️"

            val temaActual = prefs.getString("tema_actual", "guinda") ?: "guinda"
            aplicarTema(temaActual, isChecked)
        }

        // Botón carpetas protegidas (biométrico)
        btnCarpetas.setOnClickListener {
            mostrarAutenticacionBiometrica()
        }

        // Botón sensores
        btnSensores.setOnClickListener {
            startActivity(Intent(this, SensorsActivity::class.java))
        }

        // Botón Tema Guinda
        btnTemaGuinda.setOnClickListener {
            guardarTema("guinda")
            val modoOscuro = prefs.getBoolean("modo_oscuro", false)
            aplicarTema("guinda", modoOscuro)
        }

        // Botón Tema Azul
        btnTemaAzul.setOnClickListener {
            guardarTema("azul")
            val modoOscuro = prefs.getBoolean("modo_oscuro", false)
            aplicarTema("azul", modoOscuro)
        }
    }

    private fun guardarTema(tema: String) {
        prefs.edit().putString("tema_actual", tema).apply()
    }

    private fun aplicarTema(tema: String, modoOscuro: Boolean) {
        val root = findViewById<ConstraintLayout>(R.id.main)
        val btnCarpetas = findViewById<Button>(R.id.btnCarpetas)
        val btnSensores = findViewById<Button>(R.id.btnSensores)
        val btnTemaGuinda = findViewById<Button>(R.id.btnTemaGuinda)
        val btnTemaAzul = findViewById<Button>(R.id.btnTemaAzul)
        val txtTitulo = findViewById<TextView>(R.id.txtTitulo)
        val txtTemaTitulo = findViewById<TextView>(R.id.txtTemaTitulo)
        val swModoOscuro = findViewById<Switch>(R.id.swModoOscuro)

        val colorTextoClaro = ContextCompat.getColor(this, R.color.tema_texto_claro)
        val colorFondoOscuro = ContextCompat.getColor(this, R.color.fondo_oscuro)
        val colorTarjetaOscura = ContextCompat.getColor(this, R.color.tarjeta_oscura)
        val colorTextoRosa = ContextCompat.getColor(this, R.color.texto_rosa_ipn)
        val colorTextoAzul = ContextCompat.getColor(this, R.color.texto_azul_escom)

        when (tema) {
            "guinda" -> {
                val colorGuinda = ContextCompat.getColor(this, R.color.ipn_guinda)
                val colorGuindaOscuro = ContextCompat.getColor(this, R.color.ipn_guinda_oscuro)

                if (!modoOscuro) {
                    // Modo claro + tema guinda
                    root.setBackgroundColor(colorGuinda)
                    listOf(btnCarpetas, btnSensores, btnTemaGuinda, btnTemaAzul).forEach {
                        it.setBackgroundColor(colorGuindaOscuro)
                        it.setTextColor(colorTextoClaro)
                    }
                    txtTitulo.setTextColor(colorTextoClaro)
                    txtTemaTitulo.setTextColor(colorTextoClaro)
                    swModoOscuro.setTextColor(colorTextoClaro)
                } else {
                    // Modo oscuro + textos rositas
                    root.setBackgroundColor(colorFondoOscuro)
                    listOf(btnCarpetas, btnSensores, btnTemaGuinda, btnTemaAzul).forEach {
                        it.setBackgroundColor(colorTarjetaOscura)
                        it.setTextColor(colorTextoRosa)
                    }
                    txtTitulo.setTextColor(colorTextoRosa)
                    txtTemaTitulo.setTextColor(colorTextoRosa)
                    swModoOscuro.setTextColor(colorTextoRosa)
                }
            }

            "azul" -> {
                val colorAzul = ContextCompat.getColor(this, R.color.escom_azul)
                val colorAzulOscuro = ContextCompat.getColor(this, R.color.escom_azul_oscuro)

                if (!modoOscuro) {
                    // Modo claro + tema azul
                    root.setBackgroundColor(colorAzul)
                    listOf(btnCarpetas, btnSensores, btnTemaGuinda, btnTemaAzul).forEach {
                        it.setBackgroundColor(colorAzulOscuro)
                        it.setTextColor(colorTextoClaro)
                    }
                    txtTitulo.setTextColor(colorTextoClaro)
                    txtTemaTitulo.setTextColor(colorTextoClaro)
                    swModoOscuro.setTextColor(colorTextoClaro)
                } else {
                    // Modo oscuro + textos azules
                    root.setBackgroundColor(colorFondoOscuro)
                    listOf(btnCarpetas, btnSensores, btnTemaGuinda, btnTemaAzul).forEach {
                        it.setBackgroundColor(colorTarjetaOscura)
                        it.setTextColor(colorTextoAzul)
                    }
                    txtTitulo.setTextColor(colorTextoAzul)
                    txtTemaTitulo.setTextColor(colorTextoAzul)
                    swModoOscuro.setTextColor(colorTextoAzul)
                }
            }
        }
    }


    private fun mostrarAutenticacionBiometrica() {

        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@MainActivity, ProtectedFilesActivity::class.java))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            }
        )

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación requerida")
            .setSubtitle("Usa huella o patrón/PIN del dispositivo")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(info)
    }
}
