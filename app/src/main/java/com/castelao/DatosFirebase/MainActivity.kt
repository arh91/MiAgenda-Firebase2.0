package com.castelao.DatosFirebase

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class
MainActivity : AppCompatActivity() {

    // definir el requestCode
    val RESULTADO_UNO=1
    val RESULTADO_DOS=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registro = findViewById<Button>(R.id.btnRegistro)
        val salir = findViewById<Button>(R.id.btnSalir)

        /*val gosecond = findViewById<Button>(R.id.askMeBtn)
        gosecond.setOnClickListener{
            // Crea un Intent para iniciar la segunda actividad




        // Añade datos adicionales al Intent
                    intent.putExtra("proveedor", "Castelao")
                    intent.putExtra("cliente", "Google")

        // Inicia la segunda actividad
                    startActivityForResult(intent, RESULTADO_UNO)

                    startActivityForResult(intent, RESULTADO_DOS)
                }*/


        registro.setOnClickListener{
            val intentThird = Intent(this, ThirdActivity::class.java)
            startActivity(intentThird)
        }

        salir.setOnClickListener{
            finishAffinity()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //asignarDimensionesVistas()
            Toast.makeText(this@MainActivity, "LANDSCAPE!", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this@MainActivity, "PORTRAIT!", Toast.LENGTH_SHORT).show()
        }
    }

    /*fun asignarDimensionesVistas(){
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // Calcula las dimensiones basadas en porcentajes
        val widthPercentageTitle = 0.336 // Porcentaje del 33,6%
        val heightPercentageTitle = 0.145 // Porcentaja del 14,5%
        val widthPercentageButton = 0.19 // Porcentaje del 19%
        val heightPercentageButton = 0.145 // Porcentaje del 14,5%
        val marginLeftPercentageTitle = 0.345
        val marginTopPercentageTitle = 0.068
        val marginLeftPercentageBtnRegistro = 0.169
        val marginBottomPercentageBtnRegistro = 0.209
        val marginRightPercentageBtnSalir = 0.103
        val marginBottomPercentageBtnSalir = 0.209


        val widthTitle = (screenHeight * widthPercentageTitle).toInt()
        val heightTitle = (screenWidth * heightPercentageTitle).toInt()
        val widthButton = (screenHeight * widthPercentageButton).toInt()
        val heightButton = (screenWidth * heightPercentageButton).toInt()
        val marginLeftTitle = (screenHeight * marginLeftPercentageTitle).toInt()
        val marginTopTitle = (screenWidth * marginTopPercentageTitle).toInt()
        val marginLeftBtnRegistro = (screenHeight * marginLeftPercentageBtnRegistro).toInt()
        val marginBottomBtnRegistro = (screenWidth * marginBottomPercentageBtnRegistro).toInt()
        val marginRightBtnSalir = (screenHeight * marginRightPercentageBtnSalir).toInt()
        val marginBottomBtnSalir = (screenWidth * marginBottomPercentageBtnSalir).toInt()


        val title = findViewById<TextView>(R.id.tituloLand)
        val tituloParams = title.layoutParams
        tituloParams.width = widthTitle
        tituloParams.height = heightTitle
        title.layoutParams = tituloParams
        val titulooParams = title.layoutParams as ConstraintLayout.LayoutParams
        titulooParams.setMargins(marginLeftTitle, marginTopTitle, 0, 0)
        title.layoutParams = titulooParams

        val btnOne = findViewById<Button>(R.id.btnRegistro)
        val btnRegistroParams = btnOne.layoutParams
        btnRegistroParams.width = widthButton // Asigna el valor calculado a la anchura de btnRegistroLand en el layout
        btnRegistroParams.height = heightButton // Asigna el valor calculado a la altura de btnRegistroLand en el layout
        btnOne.layoutParams = btnRegistroParams
        val btnRegistrooParams = btnOne.layoutParams as ConstraintLayout.LayoutParams
        btnRegistrooParams.setMargins(marginLeftBtnRegistro, 0, 0, marginBottomBtnRegistro)
        btnOne.layoutParams = btnRegistrooParams

        val btnTwo = findViewById<Button>(R.id.btnSalir)
        val btnSalirParams = btnTwo.layoutParams
        btnSalirParams.width = widthButton
        btnSalirParams.height = heightButton
        btnTwo.layoutParams = btnSalirParams
        val btnSalirrParams = btnTwo.layoutParams as ConstraintLayout.LayoutParams
        btnSalirrParams.setMargins(0, 0, marginRightBtnSalir, marginBottomBtnSalir)
    }*/

    /*@Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val saludo = findViewById<TextView>(R.id.originalTextView)
        val goSecond = findViewById<TextView>(R.id.textViewGo)
        if(resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            RESULTADO_UNO -> {
                if (data != null) {
                    saludo.text = data.getStringExtra("pregunta")
                }; }
            // Other result codes
            else -> {}
        }
        when(requestCode) {
            RESULTADO_DOS -> {
                if (data != null) {
                    goSecond.text = data.getStringExtra("hecho")
                }; }
            // Other result codes
            else -> {}
        }
    }*/
}