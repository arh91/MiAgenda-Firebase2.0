package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class SixthActivity : AppCompatActivity() {

    lateinit var codigoProveedor: EditText
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var buscarProveedor: Button
    lateinit var eliminarProveedor: Button
    lateinit var modificarProveedor: Button
    lateinit var ok: Button
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
        ok = findViewById<Button>(R.id.btnOkSeventh)
        atras = findViewById<Button>(R.id.btnAtrásSixth)

        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        buscarProveedor.setOnClickListener{
            limpiarCampos()
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

        eliminarProveedor.setOnClickListener{

        }

        modificarProveedor.setOnClickListener{

        }

        ok.setOnClickListener{

        }

        atras.setOnClickListener{
            val intentFourth = Intent(this, FourthActivity::class.java)
            startActivity(intentFourth)
        }
    }

    fun limpiarCampos(){
        nombreProveedor.setText("");
        direccionProveedor.setText("");
        telefonoProveedor.setText("");
    }
}