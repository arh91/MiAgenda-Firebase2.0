package com.castelao.DatosFirebase

class Cliente {

    var codigo: String? = null
        get() {
            //Llamada al getter de codigo
            return field
        }
        set(nuevoCodigo) {
            //Llamada al setter de codigo
            field = nuevoCodigo
        }

    var nombre: String? = null
        get() {
            //Llamada al getter de nombre
            return field
        }
        set(nuevoNombre) {
            //Llamada al setter de nombre
            field = nuevoNombre
        }


    var direccion: String? = null

        get() {
            //Llamada al getter de direccion
            return field
        }
        set(nuevaDireccion) {
            //Llamada al setter de direccion
            field = nuevaDireccion
        }


    var telefono: String? = null

        get() {
            //Llamada al getter de telefono
            return field
        }
        set(nuevoTelefono) {
            //Llamada al setter de telefono
            field = nuevoTelefono
        }


    //Constructores

    constructor(codigo: String?, nombre: String?, direccion: String?, telefono: String?) {
        this.codigo = codigo
        this.nombre = nombre
        this.direccion = direccion
        this.telefono = telefono
    }

    constructor() {}

}