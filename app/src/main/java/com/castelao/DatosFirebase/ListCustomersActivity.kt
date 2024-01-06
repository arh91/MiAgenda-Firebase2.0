package com.castelao.DatosFirebase

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
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

        buscarCliPorNombre = findViewById(R.id.findCustomerByName)
        buscarCliPorNombre.setOnClickListener(){
            recyclerView.visibility = View.GONE
            atras.visibility = View.GONE
            buscarCliPorNombre.visibility = View.GONE
            textViewNombre.visibility = View.VISIBLE
            nombreCliente.visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
            volverListaCli.visibility = View.VISIBLE
        }

        ok.setOnClickListener(){
            val nombre = nombreCliente.text.toString()

            if(TextUtils.isEmpty(nombre)){
                mostrarPanelPersonalizado("Por favor, introduzca un nombre.")
            }else{
                listarCliente(nombre)

                obtenerNumeroClientesPorNombre(nombre) { numeroRegistros ->
                    if (numeroRegistros == 0) {
                        mostrarPanelPersonalizado("No existe ningún cliente con ese nombre en la base de datos")
                    }
                }

                textViewNombre.visibility = View.GONE
                nombreCliente.visibility = View.GONE
                ok.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        volverListaCli.setOnClickListener(){
            listarRegistrosClientes()
            obtenerNumeroClientes { numeroRegistros ->
                if (numeroRegistros == 0) {
                    mostrarPanelPersonalizado("No hay clientes en la base de datos.")
                }
            }
            textViewNombre.visibility = View.GONE
            nombreCliente.visibility = View.GONE
            ok.visibility = View.GONE
            volverListaCli.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            atras.visibility = View.VISIBLE
            buscarCliPorNombre.visibility = View.VISIBLE
        }

        firebaseDatabase = FirebaseDatabase.getInstance()

        // Inicializar la referencia a la base de datos de Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Clientes")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        listarRegistrosClientes()

        obtenerNumeroClientes { numeroRegistros ->
            if (numeroRegistros == 0) {
                mostrarPanelPersonalizado("No hay clientes en la base de datos.")
            }
        }
    }

    private fun listarRegistrosClientes() {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede acceder a los datos porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }
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
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede acceder a los datos porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }
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

    fun obtenerNumeroClientes(callback: (Int) -> Unit) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Obtiene la cantidad de registros
                val numeroRegistros: Int = dataSnapshot.childrenCount.toInt()
                callback(numeroRegistros)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores, si es necesario
                callback(-1) // Puedes cambiar esto según tus necesidades
            }
        })
    }

    fun obtenerNumeroClientesPorNombre(nombre: String, callback: (Int) -> Unit) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var contadorRegistros = 0

                for (snapshot in dataSnapshot.children) {
                    // Verifica si el campo específico tiene el valor deseado
                    val nombreRegistro = snapshot.child("nombre").getValue(String::class.java)

                    if (nombreRegistro != null && nombreRegistro == nombre) {
                        contadorRegistros++
                    }
                }
                callback(contadorRegistros)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores, si es necesario
                callback(-1) // Puedes cambiar esto según tus necesidades
            }
        })
    }

    private fun mostrarPanelPersonalizado(mensaje: String) {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.custom_toast_root))

        // Crea un objeto Toast personalizado con la vista personalizada
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        layout.findViewById<TextView>(R.id.custom_toast_text).text = mensaje

        // Muestra el Toast personalizado
        toast.show()
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}