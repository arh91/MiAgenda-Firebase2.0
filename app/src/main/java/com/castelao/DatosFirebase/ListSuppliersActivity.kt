package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListSuppliersActivity : AppCompatActivity(), SuppliersAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var atras: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_suppliers)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        atras = findViewById(R.id.btnAtrasRegistroProveedores)
        atras.setOnClickListener(){
            val toFourth = Intent(this, FourthActivity::class.java)
            startActivity(toFourth)
        }

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("Proveedores")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        fetchDataAndUpdateUI()
    }

    private fun fetchDataAndUpdateUI() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val code = dataSnapshot.child("codigo").getValue(String::class.java)
                    code?.let { dataList.add(it) }
                }

                val adapter = SuppliersAdapter(dataList, this@ListSuppliersActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListSuppliersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onItemClick(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, SuppliersDetail::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }
}