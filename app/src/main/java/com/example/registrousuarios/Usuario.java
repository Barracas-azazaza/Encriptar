package com.example.registrousuarios;

import androidx.annotation.NonNull;

public class Usuario {

    public int documento;
    public String usuario;
    public String nombres;
    public String apellidos;
    public String contra;
    @NonNull
    @Override
    public String toString(){
        return "Documento: "+documento+" Nombre: "+nombres+" Contraseña: "+contra;
    }
}
