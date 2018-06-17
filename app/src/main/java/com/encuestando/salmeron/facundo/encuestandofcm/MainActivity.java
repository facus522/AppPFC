package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.file.attribute.UserPrincipal;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MainActivity extends AppCompatActivity {

    private CardView boton_registrar;
    private CardView boton_login;
    private EditText campo_usuario;
    private EditText campo_password;
    private UsuarioDto usuarioDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        campo_usuario = (EditText) findViewById(R.id.usuario_texto);
        campo_password = (EditText) findViewById(R.id.password_texto);
        boton_registrar = (CardView) findViewById(R.id.register_button);
        boton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register_intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(register_intent);
            }
        });

        boton_login = (CardView) findViewById(R.id.login_button);
        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campo_usuario.getText().toString().equals("") || campo_password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "ERROR: Existen campos incompletos",Toast.LENGTH_LONG).show();
                } else{
                    HttpAsyncTask request = new HttpAsyncTask(1, usuarioDto);
                    request.execute("http://192.168.0.105:8080/EncuestasFCM/usuarios/loginUser?nombre=" + campo_usuario.getText().toString() + "&password=" + campo_password.getText().toString());
                    Intent userNormal_intent = new Intent(MainActivity.this, MenuNormalActivity.class);
                    MainActivity.this.startActivity(userNormal_intent);
                }
            }
        });
    }
}
