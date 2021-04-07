package com.example.registrousuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContra;
    private ListView listaUsuarios;
    private TextView tvPath;
    int documento;
    String usuario;
    String nombres;
    String apellidos;
    String contra;
    String path;
    private GestionBD gestionBD;
    SQLiteDatabase baseDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.casteo();
        String pathDatabase = getDatabasePath("dbusuarios").getAbsolutePath();
        path = pathDatabase;
        tvPath.setText(path);
    }

    //1. Casteo o Inicializador de objetos
    private void casteo(){
        etDocumento = (EditText) findViewById(R.id.etDocumento);
        etNombres = (EditText) findViewById(R.id.etNombres);
        etApellidos= (EditText) findViewById(R.id.etApellidos);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContra = (EditText) findViewById(R.id.etContra);
        listaUsuarios = (ListView) findViewById(R.id.lvLista);
        tvPath = (TextView) findViewById(R.id.tvPath);
    }

    //2. Validar campos
    private boolean validar_campos(){
        boolean respuesta;
        documento = Integer.parseInt(etDocumento.getText().toString());

        nombres = etNombres.getText().toString();
        apellidos = etApellidos.getText().toString();
        usuario = etUsuario.getText().toString();
        contra = etContra.getText().toString();
        if(nombres.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || contra.isEmpty()){
            Snackbar.make(findViewById(R.id.parent), "Campos vacios", Snackbar.LENGTH_SHORT);
            respuesta = false;
        } respuesta = true;
        return respuesta;
    }

    //3. Borrar campos
    public void borrarCampos(){
        etDocumento.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etUsuario.setText("");
        etContra.setText("");
    }

    //4.
    public void registrar(View view) {
        if (validar_campos()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(this, view);
            Usuario usuario = new Usuario();
            usuario.nombres = this.nombres;

            usuario.apellidos = this.apellidos;
            try {
                contra = encriptar(contra);
            } catch (Exception e) {
                e.printStackTrace();
            }
            usuario.contra = this.contra;

            usuario.documento = this.documento;
            ArrayList<String> lista = new ArrayList<>();
            gestionBD = new GestionBD(this);
            SQLiteDatabase db = gestionBD.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from usuarios where USU_DOCUMENTO =" + documento, null);
            if (fila.moveToFirst()) {
                do {
                    lista.add(fila.getString(0) + " | " + fila.getString(1));
                } while (fila.moveToNext());
                db.close();
                if (lista.isEmpty()) {
                    Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Usuario agregado", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            usuario.usuario = this.usuario;
            usuarioDAO.insert(usuario);
            this.ListarUsuario();
            borrarCampos();
        }
    }
    public void ListarUsuario(){ //Orientado a objetos
        UsuarioDAO usuarioDao = new UsuarioDAO(this, findViewById(R.id.lvLista));
        ArrayList<Usuario> usuarioList = usuarioDao.getUsuarioList();
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, usuarioList);
        listaUsuarios.setAdapter(adapter);
    }
    public void ListarUsuario2(View view){ //Orientado a objetos
        UsuarioDAO usuarioDao = new UsuarioDAO(this, findViewById(R.id.lvLista));
        ArrayList<Usuario> usuarioList = usuarioDao.getUsuarioList();
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, usuarioList);
        listaUsuarios.setAdapter(adapter);
    }
    public void consultarUsuario(View view){ //No orientado a objetos
        ArrayList<String> lista = new ArrayList<>();
        gestionBD = new GestionBD(this);
        SQLiteDatabase db = gestionBD.getWritableDatabase();
        Cursor fila = db.rawQuery("select * from usuarios", null);
        if(fila.moveToFirst()){
            do{
                lista.add(fila.getString(0)+" | "+fila.getString(1));
            }while(fila.moveToNext());
            db.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            listaUsuarios.setAdapter(adapter);
        }
    }
    public String encriptar(String password) throws Exception{
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes= cipher.doFinal(password.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }
    public SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}