package com.castelao.DatosFirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.castelao.DatosFirebase.R

class CustomersDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        val tvDetails: TextView = findViewById(R.id.tvDetails)
        tvDetails.text = "Detalles para el código: $code"
    }
}