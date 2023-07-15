package app.ejemplo.memoriaYazar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Ayuda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)

        supportActionBar?.hide() // borra la barra de arriba

        val AyudaBoton = findViewById<Button>(R.id.botonAtras)
        AyudaBoton.setOnClickListener {
            onBackPressed()}

    }
}