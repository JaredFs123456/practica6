package com.example.practica6

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class SensorsActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensorProximidad: Sensor? = null
    private var sensorLuz: Sensor? = null

    private lateinit var txtProximidadValor: TextView
    private lateinit var txtLuzValor: TextView
    private lateinit var txtTituloSensores: TextView
    private lateinit var txtProximidadTitulo: TextView
    private lateinit var txtLuzTitulo: TextView
    private lateinit var swProximidad: Switch
    private lateinit var swLuz: Switch
    private lateinit var rootLayout: ConstraintLayout

    private var proximidadActiva = false
    private var luzActiva = false

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors)

        prefs = getSharedPreferences("temas", MODE_PRIVATE)

        // Referencias UI
        rootLayout = findViewById(R.id.rootSensors)
        txtTituloSensores = findViewById(R.id.txtTituloSensores)
        txtProximidadTitulo = findViewById(R.id.txtProximidadTitulo)
        txtLuzTitulo = findViewById(R.id.txtLuzTitulo)
        txtProximidadValor = findViewById(R.id.txtProximidadValor)
        txtLuzValor = findViewById(R.id.txtLuzValor)
        swProximidad = findViewById(R.id.swProximidad)
        swLuz = findViewById(R.id.swLuz)

        val temaGuardado = prefs.getString("tema_actual", "guinda") ?: "guinda"
        val modoOscuroOn = prefs.getBoolean("modo_oscuro", false)
        aplicarTemaSensores(temaGuardado, modoOscuroOn)

        // SensorManager y sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensorProximidad == null) {
            txtProximidadValor.text = "No disponible en este dispositivo"
            swProximidad.isEnabled = false
        }

        if (sensorLuz == null) {
            txtLuzValor.text = "No disponible en este dispositivo"
            swLuz.isEnabled = false
        }

        swProximidad.setOnCheckedChangeListener { _, isChecked ->
            proximidadActiva = isChecked
            if (isChecked) {
                registrarSensor(sensorProximidad)
            } else {
                desregistrarSensor(sensorProximidad)
                txtProximidadValor.text = "Valor: --"
            }
        }

        swLuz.setOnCheckedChangeListener { _, isChecked ->
            luzActiva = isChecked
            if (isChecked) {
                registrarSensor(sensorLuz)
            } else {
                desregistrarSensor(sensorLuz)
                txtLuzValor.text = "Valor: --"
            }
        }
    }

    private fun aplicarTemaSensores(tema: String, modoOscuro: Boolean) {
        val colorTextoClaro = ContextCompat.getColor(this, R.color.tema_texto_claro)
        val colorFondoOscuro = ContextCompat.getColor(this, R.color.fondo_oscuro)
        val colorTarjetaOscura = ContextCompat.getColor(this, R.color.tarjeta_oscura)
        val colorTextoRosa = ContextCompat.getColor(this, R.color.texto_rosa_ipn)
        val colorTextoAzul = ContextCompat.getColor(this, R.color.texto_azul_escom)

        when (tema) {
            "guinda" -> {
                val colorGuinda = ContextCompat.getColor(this, R.color.ipn_guinda)
                if (!modoOscuro) {
                    rootLayout.setBackgroundColor(colorGuinda)
                    // textos blancos
                    txtTituloSensores.setTextColor(colorTextoClaro)
                    txtProximidadTitulo.setTextColor(colorTextoClaro)
                    txtLuzTitulo.setTextColor(colorTextoClaro)
                    txtProximidadValor.setTextColor(colorTextoClaro)
                    txtLuzValor.setTextColor(colorTextoClaro)
                    swProximidad.setTextColor(colorTextoClaro)
                    swLuz.setTextColor(colorTextoClaro)
                } else {
                    rootLayout.setBackgroundColor(colorFondoOscuro)
                    // textos rositas
                    txtTituloSensores.setTextColor(colorTextoRosa)
                    txtProximidadTitulo.setTextColor(colorTextoRosa)
                    txtLuzTitulo.setTextColor(colorTextoRosa)
                    txtProximidadValor.setTextColor(colorTextoRosa)
                    txtLuzValor.setTextColor(colorTextoRosa)
                    swProximidad.setTextColor(colorTextoRosa)
                    swLuz.setTextColor(colorTextoRosa)
                }
            }

            "azul" -> {
                val colorAzul = ContextCompat.getColor(this, R.color.escom_azul)
                if (!modoOscuro) {
                    rootLayout.setBackgroundColor(colorAzul)
                    // textos blancos
                    txtTituloSensores.setTextColor(colorTextoClaro)
                    txtProximidadTitulo.setTextColor(colorTextoClaro)
                    txtLuzTitulo.setTextColor(colorTextoClaro)
                    txtProximidadValor.setTextColor(colorTextoClaro)
                    txtLuzValor.setTextColor(colorTextoClaro)
                    swProximidad.setTextColor(colorTextoClaro)
                    swLuz.setTextColor(colorTextoClaro)
                } else {
                    rootLayout.setBackgroundColor(colorFondoOscuro)
                    // textos azules
                    txtTituloSensores.setTextColor(colorTextoAzul)
                    txtProximidadTitulo.setTextColor(colorTextoAzul)
                    txtLuzTitulo.setTextColor(colorTextoAzul)
                    txtProximidadValor.setTextColor(colorTextoAzul)
                    txtLuzValor.setTextColor(colorTextoAzul)
                    swProximidad.setTextColor(colorTextoAzul)
                    swLuz.setTextColor(colorTextoAzul)
                }
            }
        }

        // Fondo de los switches igual en ambos modos
        swProximidad.setBackgroundColor(colorTarjetaOscura)
        swLuz.setBackgroundColor(colorTarjetaOscura)
    }


    private fun registrarSensor(sensor: Sensor?) {
        sensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun desregistrarSensor(sensor: Sensor?) {
        sensor?.let {
            sensorManager.unregisterListener(this, it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (proximidadActiva) registrarSensor(sensorProximidad)
        if (luzActiva) registrarSensor(sensorLuz)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {
            Sensor.TYPE_PROXIMITY -> {
                val valor = event.values[0]
                txtProximidadValor.text = "Valor: $valor cm"
            }
            Sensor.TYPE_LIGHT -> {
                val valor = event.values[0]
                txtLuzValor.text = "Valor: $valor lx"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario
    }
}
