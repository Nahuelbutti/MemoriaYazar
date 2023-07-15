package app.ejemplo.memoriaYazar
import android.app.Activity
import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class AdministradorMejoresJugadas(context: Context) {

    fun obtenerMejoresJugadas(instancia:Activity): MutableList<Jugada> {
        val sharedPreferences = instancia.getSharedPreferences("Ranking",Context.MODE_PRIVATE)
        val editor = sharedPreferences.getString("Ranking", "vacio")
        val rankings: MutableList<Jugada> = mutableListOf()
        var cumplio=false
        Log.d("recuperacion", editor.toString())
        if (editor != "vacio") {
            val guardado = JSONObject(editor)
            val rankingsArray = guardado.getJSONArray("Ranking")

            for (i in 0 until rankingsArray.length()) {
                val jugada = rankingsArray.getJSONObject(i)
                val puntaje = jugada.getInt("Mov")
                val nombre = jugada.getString("nombre")
                val jugadaObj = Jugada(nombre, puntaje)
                rankings.add(jugadaObj)
            }
        }
        return rankings;
    }

    fun evaluarMejorJugada(jugada: Jugada, instancia:Activity){
        val sharedPreferences = instancia.getSharedPreferences("Ranking",Context.MODE_PRIVATE)
        val editor = sharedPreferences.getString("Ranking", "vacio")
        val rankings: MutableList<Jugada> = mutableListOf()
        var cumplio=false

        if (editor != "vacio") {
            val guardado = JSONObject(editor)
            val rankingsArray = guardado.getJSONArray("Ranking")

            for (i in 0 until rankingsArray.length()) {
                val jugada = rankingsArray.getJSONObject(i)
                val puntaje = jugada.getInt("Mov")
                val nombre = jugada.getString("nombre")
                val jugadaObj = Jugada(nombre, puntaje)
                rankings.add(jugadaObj)
            }
            if (rankings.size<5){ // si es menor a 5 agrego el jugador actual, o sea tenemos una lista aun no completa
                cumplio=true
                rankings.add(jugada)
                rankings.sortBy { it.Mov }
            }else{  // aca si estaria completa
                if (jugada.Mov < rankings[4].Mov){
                    cumplio=true
                    rankings[4]=jugada;
                    rankings.sortBy { it.Mov }
                }
            }
        }
        else {
            cumplio=true
            rankings.add(jugada)
            Log.d("LISTA PRUEBA",rankings.toString())
        }
        if (cumplio){
            val generador = JSONArray()

            for (i in 0 until rankings.size) {
                val jugada = JSONObject()
                jugada.put("nombre", rankings[i].nombre)
                jugada.put("Mov", rankings[i].Mov)
                generador.put(i, jugada)
            }

            val jsonObject = JSONObject()
            jsonObject.put("Ranking", generador)

            // Guardar los datos en SharedPreferences
            val editor = sharedPreferences.edit()
            Log.d("r", jsonObject.toString())
            editor.putString("Ranking", jsonObject.toString())
            editor.apply()
        }
    }

    // obtengo la mejor jugada para mostrarla como record
    fun obtenerMejorJugada(instancia: Activity): String {
        val rankings = obtenerMejoresJugadas(instancia)
        val mejorJugada = rankings.minByOrNull { it.Mov }
        return mejorJugada?.Mov.toString() ?: "No hay mejores jugadas registradas."
    }
}