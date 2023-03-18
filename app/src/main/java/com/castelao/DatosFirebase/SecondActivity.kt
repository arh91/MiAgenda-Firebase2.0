package com.castelao.DatosFirebase

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Recoge el Intent que ha iniciado la actividad
        val intent = getIntent()

        val proveedor = findViewById<TextView>(R.id.nombreProveedor)
        val cliente = findViewById<TextView>(R.id.nombreCliente)

        val b: Bundle? = intent.getExtras()

        if (b != null) {
            val prov = b.get("proveedor") as String
            val cl = b.get("cliente") as String
            proveedor.setText(prov)
            cliente.setText(cl)
        }


        intent.putExtra("pregunta", "How are you?");
        Log.d("MENSAJES", "actualizado intent")

        intent.putExtra("hecho", "Done!");
        Log.d("MENSAJES", "actualizado intent")

        setResult(Activity.RESULT_OK, intent);
        Log.d("MENSAJES", "actualizado resultado")

        val btnGoFirst = findViewById<Button>(R.id.btnGoFirst)

        btnGoFirst.setOnClickListener{
            finish()
        }


    }
}