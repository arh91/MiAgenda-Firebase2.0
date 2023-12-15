package com.castelao.miAgenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListSuppliersActivity : AppCompatActivity(), SuppliersAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_suppliers)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("tu_tabla")

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
                // Manejar el error si es necesario
            }
        })
    }

    override fun onItemClick(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }
}