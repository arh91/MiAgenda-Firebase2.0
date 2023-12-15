package com.castelao.DatosFirebase

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.castelao.DatosFirebase.R
import com.google.firebase.database.*

class SuppliersDetail : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        databaseReference = FirebaseDatabase.getInstance().reference.child("Proveedores")

        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código
        fetchDataAndUpdateUI(code)
    }

    private fun fetchDataAndUpdateUI(code: String?) {
        code?.let {
            databaseReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Obtener los campos adicionales desde Firebase
                    val campo1 = snapshot.child("nombre").getValue(String::class.java)
                    val campo2 = snapshot.child("direccion").getValue(String::class.java)
                    val campo3 = snapshot.child("telefono").getValue(String::class.java)

                    // Actualizar el TextView con los detalles
                    val tvDetails: TextView = findViewById(R.id.tvDetails)
                    tvDetails.text = "Detalles para el código $code:\nCampo1: $campo1\nCampo2: $campo2\nCampo3: $campo3"
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error si es necesario
                }
            })
        }
    }
}