package com.castelao.DatosFirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    // definir el requestCode
    val RESULTADO_UNO=1
    val RESULTADO_DOS=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gosecond = findViewById<Button>(R.id.askMeBtn)
        val registro = findViewById<Button>(R.id.btnRegistro)
        val salir = findViewById<Button>(R.id.btnSalir)

        gosecond.setOnClickListener{
            // Crea un Intent para iniciar la segunda actividad



// Añade datos adicionales al Intent
            intent.putExtra("proveedor", "Castelao")
            intent.putExtra("cliente", "Google")

// Inicia la segunda actividad
            startActivityForResult(intent, RESULTADO_UNO)

            startActivityForResult(intent, RESULTADO_DOS)
        }


        registro.setOnClickListener{
            val intentThird = Intent(this, ThirdActivity::class.java)
            startActivity(intentThird)
        }

        salir.setOnClickListener{
            finishAffinity()
        }





    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val saludo = findViewById<TextView>(R.id.originalTextView)
        val goSecond = findViewById<TextView>(R.id.textViewGo)
        if(resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            RESULTADO_UNO -> {
                if (data != null) {
                    saludo.text = data.getStringExtra("pregunta")
                }; }
            // Other result codes
            else -> {}
        }
        when(requestCode) {
            RESULTADO_DOS -> {
                if (data != null) {
                    goSecond.text = data.getStringExtra("hecho")
                }; }
            // Other result codes
            else -> {}
        }
    }
}