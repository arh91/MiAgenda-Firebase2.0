package com.castelao.DatosFirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class FourthActivity : AppCompatActivity() {

    // Creamos variables para editText y Buttons
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var codigoProveedor: EditText

    lateinit var insertarDatos: Button
    lateinit var atras: Button
    lateinit var masOpciones: Button

    lateinit var firebaseDatabase: FirebaseDatabase

    //Creamos variable para referenciar nuestra base de datos
    lateinit var databaseReference: DatabaseReference


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        //Inicializamos variables para identificar los editText y Buttons del layout
        codigoProveedor = findViewById<EditText>(R.id.edtCodigoProveedor)
        nombreProveedor = findViewById<EditText>(R.id.edtNombreProveedor)
        telefonoProveedor = findViewById<EditText>(R.id.edtTelefonoProveedor)
        direccionProveedor = findViewById<EditText>(R.id.edtDireccionProveedor)

        insertarDatos = findViewById<Button>(R.id.btnEnviarProveedor)
        atras = findViewById<Button>(R.id.btnAtrasFourth)
        masOpciones = findViewById<Button>(R.id.btnOpcionesProveedor)


        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        //Añadimos evento al botón insertarDatos

        insertarDatos.setOnClickListener {
            // Capturamos cadenas introducidas por usuario y las almacenamos en variables
            var code: String = codigoProveedor.text.toString()
            var name: String = nombreProveedor.text.toString()
            var phone: String = telefonoProveedor.text.toString()
            var address: String = direccionProveedor.text.toString()

            // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                Toast.makeText(this@FourthActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                //En caso contrario, llamamos al método que añadirá los datos introducidos a Firebase, y posteriormente dejamos en blanco otra vez todos los campos
                addDatatoFirebase(code, name, address, phone)
                clearFields()
            }
        }


        //Añadimos evento al botón opcionesProveedor

        masOpciones.setOnClickListener{
            val toSixth = Intent(this, SixthActivity::class.java)
            startActivity(toSixth)
        }



        //Añadimos evento al botón atrás

        atras.setOnClickListener{
            val toThird = Intent(this, ThirdActivity::class.java)
            startActivity(toThird)
        }
    }


    private fun addDatatoFirebase(code: String, name: String, address: String, phone: String) {

        //Creamos un objeto de nuestra clase Proveedor al que le pasamos las 4 cadenas introducidas por el usuario en los editText
        val proveedor = Proveedor(code, name, address, phone)

        // Ahora comprobamos si el código introducido por el usuario ya está registrado en nuestra base de datos o no

        databaseReference.child("Proveedores").orderByChild("codigo").equalTo(code).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Si el código ya está registrado, lanzamos un aviso al usuario para que pruebe con otro distinto
                if(snapshot.exists()){
                    Toast.makeText(this@FourthActivity, "El código introducido ya existe.", Toast.LENGTH_SHORT).show()
                }else {
                    //En caso contrario, la aplicación registrará el nuevo objeto proveedor en la tabla Proveedores de nuestra base de datos
                    databaseReference.child("Proveedores").child(code).setValue(proveedor)

                    // Avisamos al usuario que los datos se han guardado correctamente
                    Toast.makeText(this@FourthActivity, "Datos guardados.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido guardar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@FourthActivity, "No se pudieron guardar los datos. $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Método que vuelve a dejar en blanco todos los campos del layout
    fun clearFields(){
        codigoProveedor.setText("")
        nombreProveedor.setText("")
        direccionProveedor.setText("")
        telefonoProveedor.setText("")
    }
}


