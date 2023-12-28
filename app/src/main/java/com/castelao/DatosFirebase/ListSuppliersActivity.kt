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

class ListSuppliersActivity : AppCompatActivity(), SuppliersAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var atras: Button
    private lateinit var textViewNombre: TextView
    private lateinit var nombreProveedor: EditText
    private lateinit var ok: Button
    private lateinit var buscarProvPorNombre: Button
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_suppliers)

        textViewNombre = findViewById(R.id.textView_nombreProv)
        nombreProveedor = findViewById(R.id.editTextNombreProv)
        ok = findViewById(R.id.btnOkProv)

        textViewNombre.visibility = View.GONE
        nombreProveedor.visibility = View.GONE
        ok.visibility = View.GONE

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        atras = findViewById(R.id.btnAtrasRegistroProveedores)
        atras.setOnClickListener(){
            val toFourth = Intent(this, FourthActivity::class.java)
            startActivity(toFourth)
        }

        buscarProvPorNombre = findViewById(R.id.findSupplierByName)
        buscarProvPorNombre.setOnClickListener(){
            recyclerView.visibility = View.GONE
            atras.visibility = View.GONE
            buscarProvPorNombre.visibility = View.GONE
            textViewNombre.visibility = View.VISIBLE
            nombreProveedor.visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
        }

        ok.setOnClickListener(){
            val nombre = nombreProveedor.text.toString()

            textViewNombre.visibility = View.GONE
            nombreProveedor.visibility = View.GONE
            ok.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            atras.visibility = View.VISIBLE
            buscarProvPorNombre.visibility = View.VISIBLE
        }

        firebaseDatabase = FirebaseDatabase.getInstance()

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Proveedores")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        listarRegistrosProveedores()
    }

    private fun listarRegistrosProveedores() {
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

    private fun listarProveedor(nombre: String){
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
                val adapter = SuppliersAdapter(dataList, this@ListSuppliersActivity)
                recyclerView.adapter = adapter
                //providersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListSuppliersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun accederDatosProveedor(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, SuppliersDetail::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }
}