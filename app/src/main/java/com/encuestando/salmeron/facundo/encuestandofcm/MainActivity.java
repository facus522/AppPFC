package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.file.attribute.UserPrincipal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MainActivity extends AppCompatActivity {

    private CardView boton_registrar;
    private CardView boton_login;
    private TextInputLayout campo_usuario;
    private TextInputLayout campo_password;
    private UsuarioDto usuarioDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        campo_usuario = (TextInputLayout) findViewById(R.id.usuario_texto);
        campo_password = (TextInputLayout) findViewById(R.id.password_texto);
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
                String usuario = campo_usuario.getEditText().getText().toString().trim();
                String contrasenia = campo_password.getEditText().getText().toString().trim();

                 if (usuario.isEmpty()){
                     campo_usuario.setError("El campo usuario no puede estar vacío.");
                 } else{
                     campo_usuario.setError(null);
                 }
                 if (contrasenia.isEmpty()){
                     campo_password.setError("La contraseña no puede estar vacía.");
                 } else{
                     campo_password.setError(null);
                 }

                if (!(usuario.isEmpty() || contrasenia.isEmpty())){
                    campo_usuario.setError(null);
                    campo_password.setError(null);
                    String url = "http://192.168.0.105:8080/EncuestasFCM/usuarios/loginUser?nombre=" + campo_usuario.getEditText().getText().toString() + "&password=" + campo_password.getEditText().getText().toString();
                    new HttpAsyncTask(0, MainActivity.this).execute(url);
                    if (usuarioDto != null){
                        if (usuarioDto.isExito()){
                            Intent userNormal_intent = new Intent(MainActivity.this, MenuNormalActivity.class);
                            MainActivity.this.startActivity(userNormal_intent);
                        } else{
                            Toast.makeText(MainActivity.this, "ERROR EN DATOS",Toast.LENGTH_LONG).show();
                        }
                    } else{
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Error de Conexión");
                        alertDialog.setMessage("Verifique su conexión a Internet!");
                        alertDialog.setIcon(R.drawable.ic_action_error);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });
    }


    public UsuarioDto getUsuarioDto() {
        return usuarioDto;
    }

    public void setUsuarioDto(UsuarioDto usuarioDto) {
        this.usuarioDto = usuarioDto;
    }
}
