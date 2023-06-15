package com.castelao.DatosFirebase

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*

class SixthActivity : AppCompatActivity() {

    lateinit var codigoProveedor: EditText
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var buscarProveedor: Button
    lateinit var eliminarProveedor: Button
    lateinit var modificarProveedor: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    lateinit var firebaseDatabase: FirebaseDatabase

    //Creamos variable para referenciar nuestra base de datos
    lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sixth)

        codigoProveedor = findViewById<EditText>(R.id.edtBuscarCodigoProveedor)
        nombreProveedor = findViewById<EditText>(R.id.edtBuscarNombreProveedor)
        telefonoProveedor = findViewById<EditText>(R.id.edtBuscarTelefonoProveedor)
        direccionProveedor = findViewById<EditText>(R.id.edtBuscarDireccionProveedor)

        buscarProveedor = findViewById<Button>(R.id.btnBuscarProveedor)
        eliminarProveedor = findViewById<Button>(R.id.btnEliminarProveedor)
        modificarProveedor = findViewById<Button>(R.id.btnModificarProveedor)
        atras = findViewById<Button>(R.id.btnAtrásSixth)
        limpiar = findViewById<Button>(R.id.btnCleanSixth)

        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        buscarProveedor.setOnClickListener{
            var codigo: String = codigoProveedor.text.toString()
            if (TextUtils.isEmpty(codigo)){
                Toast.makeText(this@SixthActivity, "Por favor, introduzca un código de proveedor.", Toast.LENGTH_SHORT).show()
            }
            else{
                buscarProveedor()
            }
        }

        eliminarProveedor.setOnClickListener{
            var codigo: String = codigoProveedor.text.toString()
            if (TextUtils.isEmpty(codigo)){
                Toast.makeText(this@SixthActivity, "Por favor, introduzca un código de cliente.", Toast.LENGTH_SHORT).show()
            }else{
                showDefaultDialog(this)
            }
        }

        modificarProveedor.setOnClickListener{
            var codigo: String = codigoProveedor.text.toString()
            var nombre: String = nombreProveedor.text.toString()
            var direccion: String = direccionProveedor.text.toString()
            var telefono: String = telefonoProveedor.text.toString()

            if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
                Toast.makeText(this@SixthActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            }else{
                showDefaultDialogModify(this)
            }
        }

        atras.setOnClickListener{
            val intentFourth = Intent(this, FourthActivity::class.java)
            startActivity(intentFourth)
        }

        limpiar.setOnClickListener{
            limpiarTodosLosCampos()
        }
    }


    private fun buscarProveedor(){
        databaseReference.child("Proveedores").child(codigoProveedor.text.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Si el código está en la base de datos, la aplicación lo buscará y mostrará en la interfaz el resto de campos asociados a dicho código
                if (snapshot.exists()) {
                    var nombre = snapshot.child("nombre").getValue(String::class.java)
                    var direccion = snapshot.child("direccion").getValue(String::class.java)
                    var telefono = snapshot.child("telefono").getValue(String::class.java)

                    nombreProveedor.setText(nombre)
                    direccionProveedor.setText(direccion)
                    telefonoProveedor.setText(telefono)
                }else {
                    Toast.makeText(this@SixthActivity, "El código introducido no existe en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido guardar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@SixthActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun showDefaultDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea eliminar este cliente?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                databaseReference.child("Proveedores").child(codigoProveedor.text.toString()).removeValue()
                limpiarTodosLosCampos()
                Toast.makeText(this@SixthActivity, "El proveedor ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }


    private fun showDefaultDialogModify(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea modificar este cliente?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                val reference = databaseReference.child("Proveedores").child(codigoProveedor.text.toString())

                val update = HashMap<String, Any>()
                update["direccion"] = direccionProveedor.text.toString()
                update["nombre"] = nombreProveedor.text.toString()
                update["telefono"] = telefonoProveedor.text.toString()

                reference.updateChildren(update)
                    .addOnSuccessListener {
                        // La actualización se realizó exitosamente
                        Toast.makeText(this@SixthActivity, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Ocurrió un error al actualizar el registro
                        Toast.makeText(this@SixthActivity, "Error al actualizar el registro.", Toast.LENGTH_SHORT).show()
                    }
                limpiarTodosLosCampos()
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
}