package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class SeventhActivity : AppCompatActivity() {

    lateinit var codigoCliente: EditText
    lateinit var nombreCliente: EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText
    lateinit var buscarCliente: Button
    lateinit var eliminarCliente: Button
    lateinit var modificarCliente: Button
    lateinit var ok: Button
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
        ok = findViewById<Button>(R.id.btnOkSeventh)
        atras = findViewById<Button>(R.id.btnAtrásSeventh)


        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        buscarCliente.setOnClickListener{
            limpiarCampos();
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

        eliminarCliente.setOnClickListener{
            databaseReference.child("Clientes").child(codigoCliente.text.toString()).removeValue()
        }

        modificarCliente.setOnClickListener{

        }

        ok.setOnClickListener{

        }

        atras.setOnClickListener{
            val intentFifth = Intent(this, FifthActivity::class.java)
            startActivity(intentFifth)
        }
    }

    fun limpiarCampos(){
        nombreCliente.setText("");
        direccionCliente.setText("");
        telefonoCliente.setText("");
    }
}