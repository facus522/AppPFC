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
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 12/8/2018.
 */


public class CrearInfoActivity extends AppCompatActivity implements HttpAsyncTaskInterface{

    private Toolbar toolbar;
    private CardView boton_guardar;
    private CardView boton_volver;
    private TextInputLayout titulo;
    private TextInputLayout descripcion;
    private TextInputLayout url;
    private UsuarioDto usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_info_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        toolbar = findViewById(R.id.add_info_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> {
            CrearInfoActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        boton_guardar = findViewById(R.id.add_info_guardar_button);
        boton_volver = findViewById(R.id.add_info_volver_button);
        titulo = findViewById(R.id.add_info_titulo);
        descripcion = findViewById(R.id.add_info_descripcion);
        url = findViewById(R.id.add_info_url);

        boton_volver.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CrearInfoActivity.this);
            alertDialog.setTitle("Atención");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
            alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
                dialog.cancel();
                CrearInfoActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
            alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });

        boton_guardar.setOnClickListener(view -> {
            titulo.setError(null);
            descripcion.setError(null);
            url.setError(null);
            if (camposValidos()){
                ProgressDialog progressDialog = new ProgressDialog(CrearInfoActivity.this);
                progressDialog.setTitle("Creando Información o Noticia");
                progressDialog.setMessage("Espere por favor...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(true);
                progressDialog.show();

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodType(HttpCall.GET);
                String urlWS = getResources().getString(R.string.urlWS) + "/infoNoticias/addInfo";
                httpCall.setUrl(urlWS);
                HashMap<String,String> params = new HashMap<>();
                params.put("titulo", titulo.getEditText().getText().toString());
                params.put("descripcion", descripcion.getEditText().getText().toString());
                params.put("url", url.getEditText().getText().toString());
                params.put("idUsuario", usuarioLogueado.getId().toString());
                httpCall.setParams(params);
                new HttpRequest(WebServiceEnum.CREAR_INFO.getCodigo(), CrearInfoActivity.this){
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        progressDialog.dismiss();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }
                }.execute(httpCall);
            }
        });
        if (savedInstanceState != null){
            String title = savedInstanceState.getString("titulo");
            String description = savedInstanceState.getString("descripcion");
            String pagina = savedInstanceState.getString("pagina");

            if (title != null){
                titulo.getEditText().setText(title);
            }
            if (description != null){
                descripcion.getEditText().setText(description);
            }
            if (pagina != null){
                url.getEditText().setText(pagina);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("titulo", titulo.getEditText().getText().toString());
        outState.putString("descripcion", descripcion.getEditText().getText().toString());
        outState.putString("pagina", url.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    private boolean camposValidos(){
        if (titulo.getEditText().getText().toString().isEmpty() || url.getEditText().getText().toString().isEmpty()){
            titulo.setError(titulo.getEditText().getText().toString().isEmpty() ? "El título no puede estar vacío" : null);
            url.setError(url.getEditText().getText().toString().isEmpty() ? "La URL no puede estar vacía" : null);
            return false;
        } else if (titulo.getEditText().getText().length() > titulo.getCounterMaxLength()){
            titulo.setError("Se superó la cantidad máxima permitida de caracteres.");
            return false;
        } else if (descripcion.getEditText().getText().length() > descripcion.getCounterMaxLength()){
            descripcion.setError("Se superó la cantidad máxima permitida de caracteres.");
            return false;
        } else if (url.getEditText().getText().length() < 4 || !url.getEditText().getText().toString().substring(0,4).equals("http")){
            url.setError("La URL debe comenzar con 'http://'");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CrearInfoActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
            dialog.cancel();
            CrearInfoActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

}
