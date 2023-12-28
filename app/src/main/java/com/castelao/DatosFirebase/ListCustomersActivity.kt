package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ListCustomersActivity : AppCompatActivity(), CustomersAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var atras: Button
    private lateinit var textViewNombre: TextView
    private lateinit var nombreCliente: EditText
    private lateinit var ok: Button
    private lateinit var buscarCliPorNombre: Button
    private lateinit var volverListaCli: Button
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_customers)

        textViewNombre = findViewById(R.id.textView_nombreCli)
        nombreCliente = findViewById(R.id.editTextNombreCli)
        ok = findViewById(R.id.btnOkCli)
        volverListaCli = findViewById(R.id.btnVolverListaCli)

        textViewNombre.visibility = View.GONE
        nombreCliente.visibility = View.GONE
        ok.visibility = View.GONE
        volverListaCli.visibility = View.GONE

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        atras = findViewById(R.id.btnAtrasRegistroClientes)
        atras.setOnClickListener(){
            val toFifth = Intent(this, FifthActivity::class.java)
            startActivity(toFifth)
        }

        buscarCliPorNombre = findViewById(R.id.findSupplierByName)
        buscarCliPorNombre.setOnClickListener(){
            recyclerView.visibility = View.GONE
            atras.visibility = View.GONE
            buscarCliPorNombre.visibility = View.GONE
            textViewNombre.visibility = View.VISIBLE
            nombreCliente.visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
        }

        ok.setOnClickListener(){
            val nombre = nombreCliente.text.toString()
            listarCliente(nombre)

            textViewNombre.visibility = View.GONE
            nombreCliente.visibility = View.GONE
            ok.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            volverListaCli.visibility = View.VISIBLE
        }

        volverListaCli.setOnClickListener(){
            listarRegistrosClientes()
            volverListaCli.visibility = View.GONE
            atras.visibility = View.VISIBLE
            buscarCliPorNombre.visibility = View.VISIBLE
        }

        firebaseDatabase = FirebaseDatabase.getInstance()

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Clientes")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        listarRegistrosClientes()
    }

    private fun listarRegistrosClientes() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val code = dataSnapshot.child("codigo").getValue(String::class.java)
                    code?.let { dataList.add(it) }
                }

                val adapter = CustomersAdapter(dataList, this@ListCustomersActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListCustomersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun listarCliente(nombre: String){
        val query = databaseReference.orderByChild("nombre").equalTo(nombre)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val provider = dataSnapshot.child("codigo").getValue(String::class.java)
                    provider?.let {
                        dataList.add(it)
                    }
                }
                val adapter = CustomersAdapter(dataList, this@ListCustomersActivity)
                recyclerView.adapter = adapter
                //providersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListCustomersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun accederDatosCliente(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, CustomersDetail::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }
}