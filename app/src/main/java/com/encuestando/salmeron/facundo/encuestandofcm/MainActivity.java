package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */

public class MainActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable{

    private CardView boton_registrar;
    private CardView boton_login;
    private TextInputLayout campo_usuario;
    private TextInputLayout campo_password;
    private UsuarioDto usuarioDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioDto = new UsuarioDto();
        setContentView(R.layout.login_activity);
        campo_usuario = findViewById(R.id.usuario_texto);
        campo_password = findViewById(R.id.password_texto);
        boton_registrar = findViewById(R.id.register_button);
        boton_registrar.setOnClickListener(view -> {
            Intent register_intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivityForResult(register_intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        boton_login = findViewById(R.id.login_button);
        boton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = campo_usuario.getEditText().getText().toString();
                String contrasenia = campo_password.getEditText().getText().toString();

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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    campo_usuario.setError(null);
                                    campo_password.setError(null);
                                    usuarioDto = null;

                                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                    progressDialog.setTitle("Verificando sus datos");
                                    progressDialog.setMessage("Espere por favor...");
                                    progressDialog.setIndeterminate(false);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(true);
                                    progressDialog.show();


                                    HttpCall httpCall = new HttpCall();
                                    httpCall.setMethodType(HttpCall.GET);
                                    String url = getResources().getString(R.string.urlWS) + "/usuarios/loginUser";
                                    httpCall.setUrl(url);
                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("nombre", campo_usuario.getEditText().getText().toString());
                                    params.put("password", campo_password.getEditText().getText().toString());
                                    httpCall.setParams(params);
                                    new HttpRequest(WebServiceEnum.LOGIN_USER.getCodigo(), MainActivity.this){
                                        @Override
                                        public void onResponse(String response) {
                                            super.onResponse(response);
                                            progressDialog.dismiss();

                                            if (usuarioDto != null && usuarioDto.getExito() != null){
                                                if (usuarioDto.getExito()){
                                                    if (usuarioDto.getTipoUsuario().equals(TipoUsuarioEnum.USUARIO_ESPECIAL.getCodigo())){
                                                        Intent userEspecial_intent = new Intent(MainActivity.this, MenuEspecialActivity.class).putExtra("usuario", usuarioDto);
                                                        startActivityForResult(userEspecial_intent, 1);
                                                    } else if(usuarioDto.getTipoUsuario().equals(TipoUsuarioEnum.USUARIO_NORMAL.getCodigo())){
                                                        Intent userNormal_intent = new Intent(MainActivity.this, MenuNormalActivity.class).putExtra("usuario", usuarioDto);
                                                        startActivityForResult(userNormal_intent, 1);
                                                    }

                                                } else{
                                                    if (usuarioDto.getNroError().equals(ErrorLoginEnum.ERROR_USUARIO.getCodigo())){
                                                        campo_usuario.setError(usuarioDto.getError());
                                                    } else if (usuarioDto.getNroError().equals(ErrorLoginEnum.ERROR_PASSWORD.getCodigo())){
                                                        campo_password.setError(usuarioDto.getError());
                                                    }
                                                }
                                            } else{
                                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_DARK);
                                                alertDialog.setTitle("Error de Conexión");
                                                alertDialog.setMessage("Verifique su conexión a Internet! \n\nSi el problema persiste se trata de un error interno en la base de datos.");
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
                                    }.execute(httpCall);
                                }
                            });

                        }
                    }).start();
                }
            }
        });

        if (savedInstanceState != null){
            String nombreUsuario = savedInstanceState.getString("nombreUsuario");
            String contraseniaUsuario = savedInstanceState.getString("contraseniaUsuario");

            if (nombreUsuario != null){
                campo_usuario.getEditText().setText(nombreUsuario);
            }
            if (contraseniaUsuario != null){
                campo_password.getEditText().setText(contraseniaUsuario);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("nombreUsuario", campo_usuario.getEditText().getText().toString());
        outState.putString("contraseniaUsuario", campo_password.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void loginUsuario(String result) {
        String loginUsuarioJSON = result;
        if (loginUsuarioJSON != null && !loginUsuarioJSON.isEmpty()) {
            usuarioDto = JSONConverterUtils.JSONUsuarioLoginConverter(result);

        }
    }
}
