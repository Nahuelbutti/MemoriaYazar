package app.ejemplo.memoriaYazar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MenuPrincipal : AppCompatActivity() {
    private lateinit var salir: Button
    private lateinit var cambioName: Button
    private lateinit var gameNameTextView: TextView
    private lateinit var rankingButton: Button
    private lateinit var explanationButton: Button
    private lateinit var playButton: Button
    private lateinit var pauseMusic: ImageButton
    private lateinit var mediaPlayerFondo: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private var isMusicPlaying = true
    private var inicioUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        supportActionBar?.hide() // Ocultar la barra de acción

        // Obtener referencias a los elementos de la interfaz
        rankingButton = findViewById(R.id.botonRankin)
        gameNameTextView = findViewById(R.id.MenuTexto)
        explanationButton = findViewById(R.id.botonAyuda)
        playButton = findViewById(R.id.botonJugar)
        pauseMusic = findViewById(R.id.botonPausaMusica)
        cambioName = findViewById(R.id.nombreUser)
        salir = findViewById(R.id.botonSalir)

        // Establecer el nombre del juego
        gameNameTextView.text = getString(R.string.titulo_menu)

        // Inicializar el reproductor de música
        mediaPlayerFondo = MediaPlayer.create(this, R.raw.musicabackground)
        mediaPlayerFondo.isLooping = true

        // Inicializar SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Restaurar el estado de reproducción de la música
        isMusicPlaying = sharedPreferences.getBoolean("isMusicPlaying", true)

        // Establecer el estado del botón de pausa/reproducir música
        updatePauseButtonState()

        // Manejar el clic en el botón de pausa/reproducir música
        pauseMusic.setOnClickListener {
            isMusicPlaying = !isMusicPlaying

            if (isMusicPlaying) {
                mediaPlayerFondo.start()
            } else {
                mediaPlayerFondo.pause()
            }

            // Guardar el estado de reproducción de la música en SharedPreferences
            sharedPreferences.edit()
                .putBoolean("isMusicPlaying", isMusicPlaying)
                .apply()

            // Actualizar el estado del botón de pausa/reproducir música
            updatePauseButtonState()
        }

        // Botón de salir
        salir.setOnClickListener {
            onBackPressed()
        }

        // Botón de cambiar nombre
        cambioName.setOnClickListener {
            NombreUsuario()
        }

        // Manejar el clic en el botón "boton ayuda"
        explanationButton.setOnClickListener {
            val intent = Intent(this, Ayuda::class.java)
            startActivity(intent)
        }

        // Manejar el clic en el botón "Jugar"
        playButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón de ranking
        rankingButton.setOnClickListener {
            val intent = Intent(this, Ranking::class.java)
            startActivity(intent)
        }

        // Iniciar el nombre del usuario
        if (inicioUser) {
            val storedName = sharedPreferences.getString("nombre", "")
            if (!storedName.isNullOrEmpty()) {
                setNombreUsuario(storedName)
            } else {
                NombreUsuario()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isMusicPlaying && !mediaPlayerFondo.isPlaying) {
            mediaPlayerFondo.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayerFondo.isPlaying) {
            mediaPlayerFondo.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerFondo.release()
    }

    private fun updatePauseButtonState() {
        if (isMusicPlaying) {
            pauseMusic.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
        } else {
            pauseMusic.setImageResource(android.R.drawable.ic_lock_silent_mode)
        }
    }

    override fun onBackPressed() {
        mostrarAlertaSalir()
    }

    private fun mostrarAlertaSalir() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.texto_salir))
        builder.setMessage(getString(R.string.texto_salirDialogo))
        builder.setPositiveButton(getString(R.string.texto_salir)) { _, _ ->
            finish()
        }
        builder.setNegativeButton(getString(R.string.texto_cancel), null)
        builder.show()
    }

    private fun NombreUsuario() {
        inicioUser = false // asegura que el código solo se ejecuta cuando se abre la app por primera vez
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.texto_saludo))
        val input = EditText(this)
        input.filters = arrayOf(InputFilter.LengthFilter(10)) // Limitar a 10 caracteres
        builder.setView(input)

        val dialog = builder.create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.isEnabled = false // Deshabilitar el botón "Aceptar" inicialmente

            input.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val nombre = s?.toString() ?: ""
                    button.isEnabled = nombre.length >= 1 // Habilitar o deshabilitar el botón "Aceptar" según la longitud del nombre
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No se requiere implementación
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No se requiere implementación
                }
            })
        }

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.texto_Aceptar)) { _, _ ->
            val nombre = input.text.toString()
            setNombreUsuario(nombre)
            dialog.dismiss()
        }

        dialog.setCancelable(false) // Evita que el diálogo se cierre al presionar fuera de él
        dialog.show()
    }

    private fun setNombreUsuario(nombre: String) {
        playButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nombre", nombre) // Pasar el nombre como dato extra en el intent
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.nombreUser)
        textView.text = nombre

        // Guardar el nombre en SharedPreferences
        sharedPreferences.edit()
            .putString("nombre", nombre)
            .apply()

        Toast.makeText(this, getString(R.string.texto_Bienvenido) + ", $nombre!", Toast.LENGTH_SHORT).show()
    }
}
