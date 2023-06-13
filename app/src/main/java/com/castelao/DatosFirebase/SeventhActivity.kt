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

class SeventhActivity : AppCompatActivity() {

    lateinit var codigoCliente: EditText
    lateinit var nombreCliente: EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText
    lateinit var buscarCliente: Button
    lateinit var eliminarCliente: Button
    lateinit var modificarCliente: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    lateinit var firebaseDatabase: FirebaseDatabase

    //Creamos variable para referenciar nuestra base de datos
    lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seventh)

        codigoCliente = findViewById<EditText>(R.id.edtBuscarCodigoCliente)
        nombreCliente = findViewById<EditText>(R.id.edtBuscarNombreCliente)
        telefonoCliente = findViewById<EditText>(R.id.edtBuscarTelefonoCliente)
        direccionCliente = findViewById<EditText>(R.id.edtBuscarDireccionCliente)

        buscarCliente = findViewById<Button>(R.id.btnBuscarCliente)
        eliminarCliente = findViewById<Button>(R.id.btnEliminarCliente)
        modificarCliente = findViewById<Button>(R.id.btnModificarCliente)
        atras = findViewById<Button>(R.id.btnAtrásSeventh)
        limpiar = findViewById<Button>(R.id.btnCleanSeventh)


        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        buscarCliente.setOnClickListener{
            limpiarCampos();
            var codigo: String = codigoCliente.text.toString()
            if (TextUtils.isEmpty(codigo)){
                Toast.makeText(this@SeventhActivity, "Por favor, introduzca un código de cliente.", Toast.LENGTH_SHORT).show()
            }
           else{
               buscarCliente()
            }
        }

        eliminarCliente.setOnClickListener{
            var codigo: String = codigoCliente.text.toString()
            if (TextUtils.isEmpty(codigo)){
                Toast.makeText(this@SeventhActivity, "Por favor, introduzca un código de cliente.", Toast.LENGTH_SHORT).show()
            }else{
                showDefaultDialog(this)
            }
        }

        modificarCliente.setOnClickListener{
            var codigo: String = codigoCliente.text.toString()
            var nombre: String = nombreCliente.text.toString()
            var direccion: String = direccionCliente.text.toString()
            var telefono: String = telefonoCliente.text.toString()

            if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
                Toast.makeText(this@SeventhActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            }else{
                showDefaultDialogModify(this)
            }
        }

        atras.setOnClickListener{
            val intentFifth = Intent(this, FifthActivity::class.java)
            startActivity(intentFifth)
        }

        limpiar.setOnClickListener{
            limpiarTodosLosCampos()
        }
    }

    private fun buscarCliente() {
        databaseReference.child("Clientes").child(codigoCliente.text.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Si el código está en la base de datos, la aplicación lo buscará y mostrará en la interfaz el resto de campos asociados a dicho código
                if (snapshot.exists()) {
                    var nombre = snapshot.child("nombre").getValue(String::class.java)
                    var direccion = snapshot.child("direccion").getValue(String::class.java)
                    var telefono = snapshot.child("telefono").getValue(String::class.java)

                    nombreCliente.setText(nombre)
                    direccionCliente.setText(direccion)
                    telefonoCliente.setText(telefono)
                }else {
                    Toast.makeText(this@SeventhActivity, "El código introducido no existe en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido guardar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@SeventhActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
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
                databaseReference.child("Clientes").child(codigoCliente.text.toString()).removeValue()
                limpiarTodosLosCampos()
                Toast.makeText(this@SeventhActivity, "El cliente ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }.create().show()
    }


    private fun showDefaultDialogModify(context: Context) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle("Advertencia")
            setMessage("¿Está seguro que desea modificar este cliente?")
            setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                val reference = databaseReference.child("Clientes").child(codigoCliente.text.toString())

                val update = HashMap<String, Any>()
                update["direccion"] = direccionCliente.text.toString()
                update["nombre"] = nombreCliente.text.toString()
                update["telefono"] = telefonoCliente.text.toString()

                reference.updateChildren(update)
                    .addOnSuccessListener {
                        // La actualización se realizó exitosamente
                        Toast.makeText(this@SeventhActivity, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Ocurrió un error al actualizar el registro
                        Toast.makeText(this@SeventhActivity, "Error al actualizar el registro.", Toast.LENGTH_SHORT).show()
                    }
                limpiarTodosLosCampos()
            }
            setNegativeButton("Cancelar") { _, _ ->
                Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                limpiarTodosLosCampos()
            }
        }.create().show()
    }


    private fun limpiarCampos(){
        nombreCliente.setText("")
        direccionCliente.setText("")
        telefonoCliente.setText("")
    }

    private fun limpiarTodosLosCampos(){
        codigoCliente.setText("")
        nombreCliente.setText("")
        direccionCliente.setText("")
        telefonoCliente.setText("")

    }
}