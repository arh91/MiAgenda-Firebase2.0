package com.castelao.DatosFirebase

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.castelao.DatosFirebase.R
import com.google.firebase.database.*

class SuppliersDetail : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    lateinit var codigoProveedor: EditText
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var eliminarProveedor: Button
    lateinit var modificarProveedor: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_detail)

        val code = intent.getStringExtra("code")
        // Aquí deberías cargar los detalles adicionales desde Firebase usando el código

        databaseReference = FirebaseDatabase.getInstance().reference.child("Proveedores")

        codigoProveedor = findViewById(R.id.editText_codigo_prov)
        nombreProveedor = findViewById(R.id.editText_nombre_prov)
        direccionProveedor = findViewById(R.id.editText_direccion_prov)
        telefonoProveedor = findViewById(R.id.editText_telefono_prov)

        eliminarProveedor = findViewById(R.id.btn_Eliminar_Prov)
        modificarProveedor = findViewById(R.id.btn_Modificar_Prov)
        atras = findViewById(R.id.btn_Atras_Lista_Prov)

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


    private fun eliminarProveedor(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoProveedor.text.toString()

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea eliminar el proveedor "+codigo+"?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                databaseReference.child("Proveedores").child(codigoProveedor.text.toString()).removeValue()
                limpiarTodosLosCampos()
                Toast.makeText(this@SuppliersDetail, "El proveedor ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                limpiarTodosLosCampos()
            }
        }.create().show()
    }


    private fun modificarProveedor(context: Context) {
        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoProveedor.text.toString()

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea modificar el proveedor "+codigo+"?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                val reference = databaseReference.child("Proveedores").child(codigoProveedor.text.toString())

                val update = HashMap<String, Any>()
                update["direccion"] = direccionProveedor.text.toString()
                update["nombre"] = nombreProveedor.text.toString()
                update["telefono"] = telefonoProveedor.text.toString()

                reference.updateChildren(update)
                    .addOnSuccessListener {
                        // La actualización se realizó exitosamente
                        Toast.makeText(this@SuppliersDetail, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                        limpiarTodosLosCampos()
                    }
                    .addOnFailureListener {
                        // Ocurrió un error al actualizar el registro
                        Toast.makeText(this@SuppliersDetail, "Error al actualizar el registro.", Toast.LENGTH_SHORT).show()
                        limpiarTodosLosCampos()
                    }
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                limpiarTodosLosCampos()
            }
        }.create().show()
    }


    private fun limpiarTodosLosCampos(){
        codigoProveedor.setText("")
        nombreProveedor.setText("");
        direccionProveedor.setText("");
        telefonoProveedor.setText("");
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}