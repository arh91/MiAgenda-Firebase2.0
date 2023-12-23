package com.castelao.DatosFirebase

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class SuppliersDetail : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    lateinit var codigoProveedor: EditText
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var eliminarProveedor: Button
    lateinit var modificarProveedor: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    lateinit var nombreAntiguo: String
    lateinit var telefonoAntiguo: String
    lateinit var direccionAntigua: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Proveedores")

        codigoProveedor = findViewById(R.id.editText_codigo_prov)
        nombreProveedor = findViewById(R.id.editText_nombre_prov)
        direccionProveedor = findViewById(R.id.editText_direccion_prov)
        telefonoProveedor = findViewById(R.id.editText_telefono_prov)

        eliminarProveedor = findViewById(R.id.btn_Eliminar_Prov)
        modificarProveedor = findViewById(R.id.btn_Modificar_Prov)
        atras = findViewById(R.id.btn_Atras_Lista_Prov)

        eliminarProveedor.setOnClickListener(){
            eliminarProveedor(this)
        }

        modificarProveedor.setOnClickListener(){
            modificarProveedor(this)
        }

        atras.setOnClickListener(){
            val intentListSuppliers = Intent(this, ListSuppliersActivity::class.java)
            startActivity(intentListSuppliers)
        }
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código
        listarDatosProveedor(code)
    }

    private fun listarDatosProveedor(code: String?) {
        code?.let {
            databaseReference.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Obtener los campos adicionales desde Firebase
                    val nombre = snapshot.child("nombre").getValue(String::class.java)
                    val direccion = snapshot.child("direccion").getValue(String::class.java)
                    val telefono = snapshot.child("telefono").getValue(String::class.java)

                    codigoProveedor.setText(code)
                    nombreProveedor.setText(nombre)
                    direccionProveedor.setText(direccion)
                    telefonoProveedor.setText(telefono)

                    codigoProveedor.isEnabled = false

                    nombreAntiguo = nombreProveedor.text.toString()
                    direccionAntigua = direccionProveedor.text.toString()
                    telefonoAntiguo = telefonoProveedor.text.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                    Toast.makeText(this@SuppliersDetail, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }


    private fun eliminarProveedor(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        databaseReference = firebaseDatabase!!.getReference("MyDatabase")
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoProveedor.text.toString()

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea eliminar el proveedor "+codigo+"?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                databaseReference.child("Proveedores").child(codigoProveedor.text.toString()).removeValue()
                limpiarTodosLosCampos()
                volverAListaProveedores()
                Toast.makeText(this@SuppliersDetail, "El proveedor ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }


    private fun modificarProveedor(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        databaseReference = firebaseDatabase!!.getReference("MyDatabase")

        val alertDialog = AlertDialog.Builder(context)

        val codigo = codigoProveedor.text.toString()
        val nombre = nombreProveedor.text.toString()
        val direccion = direccionProveedor.text.toString()
        val telefono = telefonoProveedor.text.toString()

        if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(this@SuppliersDetail, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
        }
        else if(nombre==nombreAntiguo && direccion==direccionAntigua && telefono==telefonoAntiguo){
            Toast.makeText(this@SuppliersDetail, "No se ha modificado ningún campo.", Toast.LENGTH_SHORT).show()
        }
        else {
            alertDialog.apply {
                setTitle("Advertencia")
                setMessage("¿Está seguro que desea modificar el proveedor " + codigo + "?")
                setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                    val reference = databaseReference.child("Proveedores")
                        .child(codigoProveedor.text.toString())

                    val update = HashMap<String, Any>()
                    update["direccion"] = direccionProveedor.text.toString()
                    update["nombre"] = nombreProveedor.text.toString()
                    update["telefono"] = telefonoProveedor.text.toString()

                    reference.updateChildren(update)
                        .addOnSuccessListener {
                            // La actualización se realizó exitosamente
                            Toast.makeText(
                                this@SuppliersDetail,
                                "Registro actualizado correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            limpiarTodosLosCampos()
                            volverAListaProveedores()
                        }
                        .addOnFailureListener {
                            // Ocurrió un error al actualizar el registro
                            Toast.makeText(
                                this@SuppliersDetail,
                                "Error al actualizar el registro.",
                                Toast.LENGTH_SHORT
                            ).show()
                            limpiarTodosLosCampos()
                            volverAListaProveedores()
                        }
                }
                setNegativeButton("Cancelar") { _, _ ->
                    Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                }
            }.create().show()
        }
    }


    private fun limpiarTodosLosCampos(){
        codigoProveedor.setText("")
        nombreProveedor.setText("");
        direccionProveedor.setText("");
        telefonoProveedor.setText("");
    }

    private fun volverAListaProveedores(){
        val intentListSuppliers = Intent(this, ListSuppliersActivity::class.java)
        startActivity(intentListSuppliers)
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}