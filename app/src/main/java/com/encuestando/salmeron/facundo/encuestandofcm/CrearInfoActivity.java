package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

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
    private HttpAsyncTask httpAsyncTask;
    private UsuarioDto usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_info_activity);
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        toolbar = (Toolbar) findViewById(R.id.add_info_toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrearInfoActivity.this.finish();
            }
        });

        boton_guardar = (CardView) findViewById(R.id.add_info_guardar_button);
        boton_volver = (CardView) findViewById(R.id.add_info_volver_button);
        titulo = (TextInputLayout) findViewById(R.id.add_info_titulo);
        descripcion = (TextInputLayout) findViewById(R.id.add_info_descripcion);
        url = (TextInputLayout) findViewById(R.id.add_info_url);

        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CrearInfoActivity.this);
                alertDialog.setTitle("Atención");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
                alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        CrearInfoActivity.this.finish();
                    }
                });
                alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo.setError(null);
                descripcion.setError(null);
                url.setError(null);
                if (camposValidos()){
                    String urlWS = "http://192.168.0.107:8080/EncuestasFCM/infoNoticias/addInfo?titulo="+reemplazarEspacios(titulo.getEditText().getText().toString())+
                            "&descripcion="+reemplazarEspacios(descripcion.getEditText().getText().toString())+"&url="+url.getEditText().getText().toString()+
                            "&idUsuario="+usuarioLogueado.getId();
                    httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CREAR_INFO.getCodigo());
                    httpAsyncTask.setHttpAsyncTaskInterface(CrearInfoActivity.this);
                    try {
                        String receivedData = httpAsyncTask.execute(urlWS).get();
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    private String reemplazarEspacios(String valor){

        String aux = valor.replace(" ", "%20");
        aux = aux.replace("\n", "%20");
        return aux;
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
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                CrearInfoActivity.this.finish();
            }
        });
        alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void loginUsuario(String result) {

    }

    @Override
    public void registerUsuario(String result) {

    }

    @Override
    public void cargarInfoNoticias(String result) {

    }

    @Override
    public void eliminarInfoNoticia(String result) {

    }

    @Override
    public void crearInfoNoticica(String result) {

    }
}
