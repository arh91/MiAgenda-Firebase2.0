package com.castelao.DatosFirebase

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*

class CustomersDetail : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    lateinit var codigoCliente: EditText
    lateinit var nombreCliente: EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText
    lateinit var eliminarCliente: Button
    lateinit var modificarCliente: Button
    lateinit var limpiar: Button
    lateinit var atras: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Clientes")

        codigoCliente = findViewById(R.id.editText_codigo_cli)
        nombreCliente = findViewById(R.id.editText_nombre_cli)
        direccionCliente = findViewById(R.id.editText_direccion_cli)
        telefonoCliente = findViewById(R.id.editText_telefono_cli)

        eliminarCliente = findViewById(R.id.btn_Eliminar_cli)
        modificarCliente = findViewById(R.id.btn_Modificar_cli)
        atras = findViewById(R.id.btn_Atras_Lista_Cli)

        eliminarCliente.setOnClickListener(){
            eliminarCliente(this)
        }

        modificarCliente.setOnClickListener(){
            modificarCliente(this)
        }

        atras.setOnClickListener(){
            val intentListCustomers = Intent(this, ListCustomersActivity::class.java)
            startActivity(intentListCustomers)
        }
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código
        listarDatosCliente(code)
    }


    private fun listarDatosCliente(code: String?) {
        code?.let {
            databaseReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Obtener los campos adicionales desde Firebase
                    val nombre = snapshot.child("nombre").getValue(String::class.java)
                    val direccion = snapshot.child("direccion").getValue(String::class.java)
                    val telefono = snapshot.child("telefono").getValue(String::class.java)

                    codigoCliente.setText(code)
                    nombreCliente.setText(nombre)
                    direccionCliente.setText(direccion)
                    telefonoCliente.setText(telefono)

                    codigoCliente.isEnabled = false
                }

                override fun onCancelled(error: DatabaseError) {
                    // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                    Toast.makeText(this@CustomersDetail, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }


    private fun eliminarCliente(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        databaseReference = firebaseDatabase!!.getReference("MyDatabase")
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoCliente.text.toString()

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea eliminar el cliente "+codigo+"?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                databaseReference.child("Clientes").child(codigoCliente.text.toString()).removeValue()
                limpiarTodosLosCampos()
                volverAListaClientes()
                Toast.makeText(this@CustomersDetail, "El cliente ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }


    private fun modificarCliente(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        databaseReference = firebaseDatabase!!.getReference("MyDatabase")
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoCliente.text.toString()
        val nombre = nombreCliente.text.toString()
        val direccion = direccionCliente.text.toString()
        val telefono = telefonoCliente.text.toString()

        if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(this@CustomersDetail, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
        }
        else{
            alertDialog.apply {
                setTitle("Advertencia")
                setMessage("¿Está seguro que desea modificar el cliente "+codigo+"?")
                setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                    val reference = databaseReference.child("Clientes").child(codigoCliente.text.toString())

                    val update = HashMap<String, Any>()
                    update["direccion"] = direccionCliente.text.toString()
                    update["nombre"] = nombreCliente.text.toString()
                    update["telefono"] = telefonoCliente.text.toString()

                    reference.updateChildren(update)
                        .addOnSuccessListener {
                            // La actualización se realizó exitosamente
                            Toast.makeText(this@CustomersDetail, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                            limpiarTodosLosCampos()
                            volverAListaClientes()
                        }
                        .addOnFailureListener {
                            // Ocurrió un error al actualizar el registro
                            Toast.makeText(this@CustomersDetail, "Error al actualizar el registro.", Toast.LENGTH_SHORT).show()
                            limpiarTodosLosCampos()
                            volverAListaClientes()
                        }
                }
                setNegativeButton("Cancelar") { _, _ ->
                    Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                }
            }.create().show()
        }
    }


    private fun limpiarTodosLosCampos(){
        codigoCliente.setText("")
        nombreCliente.setText("");
        direccionCliente.setText("");
        telefonoCliente.setText("");
    }

    private fun volverAListaClientes(){
        val intentListCustomers = Intent(this, ListCustomersActivity::class.java)
        startActivity(intentListCustomers)
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}