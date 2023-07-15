package app.ejemplo.memoriaYazar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class Ranking : AppCompatActivity() {

    private lateinit var mejoresJugadas: MutableList<Jugada>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rankmejoresjugadas)

        val administradorMejoresJugadas = AdministradorMejoresJugadas(this) // guardar jugadas

        mejoresJugadas =  administradorMejoresJugadas.obtenerMejoresJugadas(this)

        supportActionBar?.hide()

        val textViewMejoresJugadas = findViewById<TextView>(R.id.TxtRanking)
        val formattedJugadas = formatMejoresJugadas()
        textViewMejoresJugadas.text = formattedJugadas

        val botonAtras = findViewById<Button>(R.id.botonAtras)
        botonAtras.setOnClickListener {
            onBackPressed()
        }
    }

    private fun formatMejoresJugadas(): String {
        val stringBuilder = StringBuilder()

        for (i in 0 until mejoresJugadas.size) {
            val jugada = mejoresJugadas[i]
            stringBuilder.append("${jugada.nombre}. " + getString(R.string.text_Puntaje) + " ${jugada.Mov}\n")
        }

        val formattedJugadas = stringBuilder.toString()
        Log.d("Ranking", formattedJugadas) // Agregar impresión en la consola

        return stringBuilder.toString()
    }

}
