package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val proveedores = findViewById<Button>(R.id.btnProveedores)
        val clientes = findViewById<Button>(R.id.btnClientes)
        val atras = findViewById<Button>(R.id.btnAtrasThird)

        proveedores.setOnClickListener(){
            val intentFourth = Intent(this, FourthActivity::class.java)
            startActivity(intentFourth)
        }

        clientes.setOnClickListener(){
            val intentFifth = Intent(this, FifthActivity::class.java)
            startActivity(intentFifth)
        }

        atras.setOnClickListener{
            val toMain = Intent(this, MainActivity::class.java)
            startActivity(toMain)
        }
    }
}