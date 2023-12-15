package com.castelao.miAgenda

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.castelao.DatosFirebase.R

class SuppliersDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        val tvDetails: TextView = findViewById(R.id.tvDetails)
        tvDetails.text = "Detalles para el código: $code"
    }
}