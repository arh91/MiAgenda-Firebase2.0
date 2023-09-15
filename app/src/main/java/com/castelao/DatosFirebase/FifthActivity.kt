package com.castelao.DatosFirebase

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class FifthActivity : AppCompatActivity() {

    // Creamos variables para editText y Buttons
    lateinit var codigoCliente:EditText
    lateinit var nombreCliente:EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText

    lateinit var insertarDatos: Button
    lateinit var atras: Button
    lateinit var masOpciones: Button

    lateinit var firebaseDatabase: FirebaseDatabase

    //Creamos variable para referenciar nuestra base de datos
    lateinit var databaseReference: DatabaseReference


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        //Inicializamos variables para identificar los editText y Buttons del layout
        codigoCliente = findViewById<EditText>(R.id.edtCodigoCliente)
        nombreCliente = findViewById<EditText>(R.id.edtNombreCliente)
        telefonoCliente = findViewById<EditText>(R.id.edtTelefonoCliente)
        direccionCliente = findViewById<EditText>(R.id.edtDireccionCliente)

        insertarDatos = findViewById<Button>(R.id.btnEnviarCliente)
        atras = findViewById<Button>(R.id.btnAtrasFifth)
        masOpciones = findViewById<Button>(R.id.btnOpcionesCliente)


        firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        //Añadimos evento al botón insertarDatos

        insertarDatos.setOnClickListener {
            // Capturamos cadenas introducidas por usuario y las almacenamos en variables
            var codigoCliente: String = codigoCliente.text.toString()
            var nombreCliente: String = nombreCliente.text.toString()
            var telefonoCliente: String = telefonoCliente.text.toString()
            var direccionCliente: String = direccionCliente.text.toString()

            // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
            if (TextUtils.isEmpty(codigoCliente) || TextUtils.isEmpty(nombreCliente) || TextUtils.isEmpty(telefonoCliente) || TextUtils.isEmpty(direccionCliente)) {
                Toast.makeText(this@FifthActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                //En caso contrario, llamamos al método que añadirá los datos introducidos a Firebase, y posteriormente dejamos en blanco otra vez todos los campos
                insertarDatos(codigoCliente, nombreCliente, direccionCliente, telefonoCliente)
                limpiarTodosLosCampos()
            }
        }


        //Añadimos evento al boton masOpciones

        masOpciones.setOnClickListener(){
            val toSeventh = Intent(this, SeventhActivity::class.java)
            startActivity(toSeventh)
        }


        //Añadimos evento al botón atras

        atras.setOnClickListener{
            val toThird = Intent(this, ThirdActivity::class.java)
            startActivity(toThird)
        }
    }


    private fun insertarDatos(codigo: String, nombre: String, direccion: String, telefono: String) {

        // Si no hay conexión a Internet, informar de ello al usuario
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No se puede realizar ésta acción porque no hay conexión a Internet", Toast.LENGTH_LONG).show()
            return
        }

        //Creamos un objeto de nuestra clase Cliente al que le pasamos las 4 cadenas introducidas por el usuario en los editText
        //val cliente = Cliente(code, name, address, phone)


        // Ahora comprobamos si el código introducido por el usuario ya está registrado en nuestra base de datos o no

        databaseReference.child("Clientes").orderByChild("codigo").equalTo(codigo).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //Si el código ya está registrado, lanzamos un aviso al usuario para que pruebe con otro distinto
                if(snapshot.exists()){
                    Toast.makeText(this@FifthActivity, "El código introducido ya existe.", Toast.LENGTH_SHORT).show()
                }else {
                    //En caso contrario, la aplicación registrará el nuevo objeto cliente en la tabla Clientes de nuestra base de datos
                    //databaseReference.child("Clientes").child(code).setValue(cliente)
                    databaseReference.child("Clientes").child(codigo).child("codigo").setValue(codigo)
                    databaseReference.child("Clientes").child(codigo).child("direccion").setValue(direccion)
                    databaseReference.child("Clientes").child(codigo).child("nombre").setValue(nombre)
                    databaseReference.child("Clientes").child(codigo).child("telefono").setValue(telefono)

                    // Avisamos al usuario que los datos se han guardado correctamente
                    Toast.makeText(this@FifthActivity, "Datos guardados.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido guardar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@FifthActivity, "No se pudieron guardar los datos. $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Método que vuelve a dejar en blanco todos los campos del layout
    fun limpiarTodosLosCampos(){
       codigoCliente.setText("")
       nombreCliente.setText("")
       direccionCliente.setText("")
       telefonoCliente.setText("")
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}