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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModificarEncuestaActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable, PreguntaNuevaRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private FloatingActionButton multipleChoiceButton;
    private FloatingActionButton unicaButton;
    private FloatingActionButton numericaButton;
    private FloatingActionButton textualButton;
    private FloatingActionButton escalaButton;
    private FloatingActionMenu actionMenu;
    private ArrayList<PreguntaDto> preguntas;
    private ArrayList<PreguntaDto> preguntasEliminar;
    private ArrayList<RespuestaDto> respuestasEliminar;
    private RecyclerView recyclerView;
    private PreguntaNuevaRecyclerViewAdapter adapter;
    private CardView modificar;
    private CardView volver;
    private TextInputLayout titulo;
    private TextInputLayout descripcion;
    private String tituloGuardar;
    private String descripcionGuardar;
    private HttpAsyncTask httpAsyncTaskEncuesta;
    private HttpAsyncTask httpAsyncTaskPregunta;
    private HttpAsyncTask httpAsyncTaskRespuesta;
    private Integer idEncuestaAsignado;
    private Integer idPreguntaAsignado;
    private boolean isAlertOpen = false;
    private CheckBox respuestasGeolocalizadas;
    private Boolean geolocalizada;
    private CheckBox restringir;
    private CheckBox sexo_restringir;
    private CheckBox edad_restringir;
    private TextInputLayout mayoresDe;
    private RadioGroup sexo_radioGroup;
    private RadioButton sexo_radioButton;
    private Integer isSexoRestriccion;
    private Integer isEdadRestriccion;
    private TextView errores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_encuesta_activity);
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        idEncuestaAsignado = (Integer) getIntent().getSerializableExtra("idEncuestaPersistida");
        preguntasEliminar = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntasEliminar");
        respuestasEliminar = (ArrayList<RespuestaDto>) getIntent().getSerializableExtra("respuestasEliminar");
        tituloGuardar = (String) getIntent().getSerializableExtra("tituloGuardar");
        descripcionGuardar = (String) getIntent().getSerializableExtra("descripcionGuardar");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        geolocalizada = (Boolean) getIntent().getSerializableExtra("geolocalizada");
        isSexoRestriccion = (Integer) getIntent().getSerializableExtra("isSexoRestriccion");
        isEdadRestriccion = (Integer) getIntent().getSerializableExtra("isEdadRestriccion");
        errores = findViewById(R.id.erroresRestricciones);
        errores.setVisibility(View.GONE);
        respuestasGeolocalizadas = findViewById(R.id.checkbox_geolocalizada_modificar);
        respuestasGeolocalizadas.setChecked(geolocalizada);


        restringir = findViewById(R.id.checkbox_restricciones);
        sexo_restringir = findViewById(R.id.checkbox_restriccion_sexo);
        edad_restringir = findViewById(R.id.checkbox_restriccion_edad);
        mayoresDe = findViewById(R.id.restriccion_mayores_de);
        sexo_radioGroup = findViewById(R.id.restriccion_sexo);

        if (isSexoRestriccion != 0 || isEdadRestriccion != 0){
            restringir.setChecked(true);
            if (isSexoRestriccion != 0) {
                sexo_restringir.setChecked(true);
                if (isSexoRestriccion.equals(1)){
                    sexo_radioButton = findViewById(R.id.sexo_masculino_encuestado_button);
                } else {
                    sexo_radioButton = findViewById(R.id.sexo_femenino_encuestado_button);
                }
                sexo_radioButton.setChecked(true);

            }

            if (isEdadRestriccion != 0){
                edad_restringir.setChecked(true);
                mayoresDe.getEditText().setText(isEdadRestriccion.toString());
            }
        }


        verificarRestricciones();

        sexo_radioGroup.setOnCheckedChangeListener((radioGroup, i) -> sexo_radioButton = findViewById(i));

        restringir.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (restringir.isChecked()){
                sexo_restringir.setVisibility(View.VISIBLE);
                edad_restringir.setVisibility(View.VISIBLE);
                if (edad_restringir.isChecked()){
                    mayoresDe.setVisibility(View.VISIBLE);
                } else {
                    mayoresDe.setVisibility(View.GONE);
                }

                if (sexo_restringir.isChecked()){
                    sexo_radioGroup.setVisibility(View.VISIBLE);
                } else {
                    sexo_radioGroup.setVisibility(View.GONE);
                }
            } else {
                sexo_restringir.setVisibility(View.GONE);
                sexo_restringir.setChecked(Boolean.FALSE);
                edad_restringir.setVisibility(View.GONE);
                edad_restringir.setChecked(Boolean.FALSE);
                mayoresDe.setVisibility(View.GONE);
                mayoresDe.getEditText().setText("");
                sexo_radioGroup.setVisibility(View.GONE);
                sexo_radioGroup.clearCheck();
            }
        });

        sexo_restringir.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (sexo_restringir.isChecked()){
                sexo_radioGroup.setVisibility(View.VISIBLE);
            } else {
                sexo_radioGroup.setVisibility(View.GONE);
            }
        });

        edad_restringir.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (edad_restringir.isChecked()){
                mayoresDe.setVisibility(View.VISIBLE);
            } else {
                mayoresDe.setVisibility(View.GONE);
            }
        });

        toolbar = findViewById(R.id.titulo_modificar_encuesta);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_FIRST_USER);
            ModificarEncuestaActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        modificar = findViewById(R.id.modificar_encuesta_button);
        volver = findViewById(R.id.volver_modificar_encuesta_button);
        titulo = findViewById(R.id.titulo_encuesta_modificar);
        descripcion = findViewById(R.id.descripcion_encuesta_modificar);
        titulo.getEditText().setText(tituloGuardar);
        descripcion.getEditText().setText(descripcionGuardar);
        volver.setOnClickListener(view -> posibleSalida());
        modificar.setOnClickListener(view -> onClickModificarEncuesta());
        recyclerView = findViewById(R.id.recycler_encuesta_modificar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntaNuevaRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        actionMenu = findViewById(R.id.boton_agregar_pregunta);
        multipleChoiceButton = findViewById(R.id.multiple_choice_button);
        multipleChoiceButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_multiple_choice = new Intent(ModificarEncuestaActivity.this, PreguntaMultipleChoiceActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_multiple_choice, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_unica_opcion = new Intent(ModificarEncuestaActivity.this, PreguntaUnicaOpcionActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_unica_opcion, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        numericaButton = findViewById(R.id.numerica_button);
        numericaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_numerica = new Intent(ModificarEncuestaActivity.this, PreguntaNumericaActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_numerica, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        textualButton = findViewById(R.id.textual_button);
        textualButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_textual = new Intent(ModificarEncuestaActivity.this, PreguntaTextualActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_textual, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        escalaButton = findViewById(R.id.escala_button);
        escalaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_escala = new Intent(ModificarEncuestaActivity.this, PreguntaEscalaActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_escala, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        if (savedInstanceState != null){
            String tituloNuevo = savedInstanceState.getString("tituloNuevo");
            String descripcionNuevo = savedInstanceState.getString("descripcionNuevo");
            String edadRestriccion = savedInstanceState.getString("edadRestriccion");
            boolean estaAbierto = savedInstanceState.getBoolean("estaAbierto");
            if (tituloNuevo!= null){
                titulo.getEditText().setText(tituloNuevo);
            }
            if (descripcionNuevo!=null){
                descripcion.getEditText().setText(descripcionNuevo);
            }
            if (edadRestriccion != null){
                mayoresDe.getEditText().setText(edadRestriccion);
            }
            if (estaAbierto){
                showAlertDialog();
            }
        }
    }

    private void verificarRestricciones(){
        if (restringir.isChecked()){
            sexo_restringir.setVisibility(View.VISIBLE);
            edad_restringir.setVisibility(View.VISIBLE);
            if (edad_restringir.isChecked()){
                mayoresDe.setVisibility(View.VISIBLE);
            } else {
                mayoresDe.setVisibility(View.GONE);
            }

            if (sexo_restringir.isChecked()){
                sexo_radioGroup.setVisibility(View.VISIBLE);
            } else {
                sexo_radioGroup.setVisibility(View.GONE);
            }
        } else {
            sexo_restringir.setVisibility(View.GONE);
            edad_restringir.setVisibility(View.GONE);
            mayoresDe.setVisibility(View.GONE);
            sexo_radioGroup.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            ArrayList<RespuestaDto> rtasDto = (ArrayList) data.getSerializableExtra("rtasEliminar");
            if (rtasDto != null) respuestasEliminar.addAll(rtasDto);
            PreguntaDto dto = (PreguntaDto) data.getSerializableExtra("pregunta");
            if (dto.getId() != null){
                reemplazarPreguntaModificada(dto);
            } else {
                dto.setId(new Long(preguntas.size() + 1));
                preguntas.add(dto);
            }
            finish();
            getIntent().putExtra("respuestasEliminar", respuestasEliminar);
            getIntent().putExtra("preguntas", preguntas);
            getIntent().putExtra("tituloGuardar", titulo.getEditText().getText().toString());
            getIntent().putExtra("descripcionGuardar", descripcion.getEditText().getText().toString());

            getIntent().putExtra("geolocalizada", respuestasGeolocalizadas.isChecked());
            getIntent().putExtra("isSexoRestriccion", sexo_restringir.isChecked() ? (sexo_radioButton != null && sexo_radioButton.getHint().toString().equals("Masculino") ? 1 : 2) : 0);
            getIntent().putExtra("isEdadRestriccion", edad_restringir.isChecked() ? (!mayoresDe.getEditText().getText().toString().isEmpty() ? Integer.valueOf(mayoresDe.getEditText().getText().toString()) : 0) : 0);

            startActivity(getIntent());
            if (dto.getPreguntaModificada()){
                Toast.makeText(ModificarEncuestaActivity.this, "La pregunta ha sido modificada correctamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ModificarEncuestaActivity.this, "La pregunta ha sido añadida correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onClickModificarEncuesta(){
        titulo.setError(null);
        descripcion.setError(null);
        if (titulo.getEditText().getText().toString().isEmpty()){
            titulo.setError("El título no puede estar vacío!");
            return;
        } else if (titulo.getEditText().getText().length() > titulo.getCounterMaxLength()){
            titulo.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (descripcion.getEditText().getText().length() > descripcion.getCounterMaxLength()){
            descripcion.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (preguntas.size() < 1){
            descripcion.setError("Debe agregarse al menos una pregunta.");
            return;
        }

        if (restringir.isChecked() && (!edad_restringir.isChecked() && !sexo_restringir.isChecked())){
            errores.setText("Si escogió restringir, debe tildar al menos una de las opciones");
            errores.setVisibility(View.VISIBLE);
            return;
        }

        if (restringir.isChecked() && edad_restringir.isChecked() && mayoresDe.getEditText().getText().toString().isEmpty()){
            errores.setText("Debe ingresar la edad de restricción");
            errores.setVisibility(View.VISIBLE);
            return;
        }

        if (restringir.isChecked() && sexo_restringir.isChecked() && sexo_radioButton == null){
            errores.setText("Debe ingresar el sexo de restricción");
            errores.setVisibility(View.VISIBLE);
            return;
        }


        new Thread(() -> runOnUiThread(() -> {
            String geo = respuestasGeolocalizadas.isChecked() ? "true" : "false";
            String isEdad = (restringir.isChecked() && edad_restringir.isChecked()) ? mayoresDe.getEditText().getText().toString() : "0";
            String isSexo = (restringir.isChecked() && sexo_restringir.isChecked()) ? (sexo_radioButton.getHint().toString().equals("Masculino") ? "1" : "2") : "0";

            ProgressDialog progressDialog = new ProgressDialog(ModificarEncuestaActivity.this);
            progressDialog.setTitle("Modificar Encuesta");
            progressDialog.setMessage("Espere por favor...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();

            EncuestaJson encuesta = getEncuestaPersistir(geo, isSexo, isEdad);
            Gson gson = new Gson();
            String body = gson.toJson(encuesta);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodType(HttpCall.POST);
            String url = getResources().getString(R.string.urlWS) + "/encuestas/modificarEncuesta";
            httpCall.setUrl(url);
            HashMap<String, String> params = new HashMap<>();
            httpCall.setParams(params);
            httpCall.setBody(body);
            new HttpRequest(WebServiceEnum.MODIFICAR_ENCUESTA.getCodigo(), ModificarEncuestaActivity.this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    progressDialog.dismiss();
                    if (idEncuestaAsignado == null) {
                        progressDialog.dismiss();
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModificarEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
                        alertDialog.setTitle("Error de Conexión");
                        alertDialog.setMessage("Verifique su conexión a Internet! \n\nSi el problema persiste se trata de un error interno en la base de datos.");
                        alertDialog.setIcon(R.drawable.ic_action_error);
                        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                        alertDialog.show();
                    } else {
                        showAlertDialog();
                    }
                }
            }.execute(httpCall);




            /*String urlEncuesta = getResources().getString(R.string.urlWS) + "/encuestas/updateEncuesta?" + "idEncuesta=" + idEncuestaAsignado
                    + "&titulo=" + reemplazarEspacios(titulo.getEditText().getText().toString())
                    + "&descripcion=" + reemplazarEspacios(descripcion.getEditText().getText().toString())
                    + "&isGeolocalizada=" + geo
                    + "&isSexo=" + isSexo
                    + "&isEdad=" + isEdad
                    + "&idUsuario=" + usuarioLogueado.getId();
            httpAsyncTaskEncuesta = new HttpAsyncTask(WebServiceEnum.MODIFICAR_ENCUESTA.getCodigo());
            httpAsyncTaskEncuesta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
            try{
                String receivedDataEncuesta = httpAsyncTaskEncuesta.execute(urlEncuesta).get();
            }
            catch (ExecutionException | InterruptedException ei){
                ei.printStackTrace();
            }

            for (PreguntaDto preg : preguntas){
                if (preg.getIdPersistido() != null){
                    updatePregunta(preg);
                } else {
                    savePregunta(preg);
                }
            }
            for (RespuestaDto rta : respuestasEliminar){
                removeRespuesta(rta);
            }

            for (PreguntaDto preg : preguntasEliminar){
                removePregunta(preg);
            }

            showAlertDialog();
            cargandoModificar.setVisibility(View.GONE);*/
        })).start();


    }

    private EncuestaJson getEncuestaPersistir(String geo, String isSexo, String isEdad) {
        EncuestaJson encuesta = new EncuestaJson();
        encuesta.setId(idEncuestaAsignado);
        encuesta.setTitulo(titulo.getEditText().getText().toString());
        encuesta.setDescripcion(descripcion.getEditText().getText().toString());
        encuesta.setGeolicalizada(geo.equals("true") ? Boolean.TRUE : Boolean.FALSE);
        encuesta.setIsSexoRestriction(isSexo.isEmpty() ? null : Integer.valueOf(isSexo));
        encuesta.setIsEdadRestriction(isEdad.isEmpty() ? null : Integer.valueOf(isEdad));
        encuesta.setIdUsuarioModificacion(usuarioLogueado.getId());
        for (PreguntaDto pregunta : preguntas) {
            PreguntaJson p = new PreguntaJson();
            p.setId(pregunta.getIdPersistido());
            p.setDescripcion(pregunta.getDescripcion());
            p.setNumeroEscala(pregunta.getMaximaEscala());
            p.setTipoRespuesta(new TipoRespuestaJson(pregunta.getTipoPregunta().getDescripcion(), pregunta.getTipoPregunta().getCodigo()));
            if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty()) {
                for (RespuestaDto rta : pregunta.getRespuestas()) {
                    RespuestaJson r = new RespuestaJson();
                    r.setId(rta.getIdPersistido());
                    r.setDescripcion(rta.getDescripcion());
                    r.setTipoRespuesta(new TipoRespuestaJson(pregunta.getTipoPregunta().getDescripcion(), pregunta.getTipoPregunta().getCodigo()));
                    p.getRespuestas().add(r);
                }
            } else {
                RespuestaJson r = new RespuestaJson();
                r.setDescripcion("");
                r.setTipoRespuesta(new TipoRespuestaJson(pregunta.getTipoPregunta().getDescripcion(), pregunta.getTipoPregunta().getCodigo()));
                p.getRespuestas().add(r);
            }
            encuesta.getPreguntas().add(p);

        }
        return encuesta;
    }

    private PreguntaJson getRespuestasRemover() {
        PreguntaJson pregunta = new PreguntaJson();
        for (RespuestaDto rta : respuestasEliminar){
            RespuestaJson respuestaJson = new RespuestaJson();
            respuestaJson.setId(rta.getIdPersistido());
            pregunta.getRespuestas().add(respuestaJson);
        }
        return pregunta;
    }

    private EncuestaJson getPreguntasRemover() {
        EncuestaJson encuesta = new EncuestaJson();
        for (PreguntaDto pta : preguntasEliminar){
            PreguntaJson preguntaJson = new PreguntaJson();
            preguntaJson.setId(pta.getIdPersistido());
            for (RespuestaDto rta : pta.getRespuestas()){
                RespuestaJson respuestaJson = new RespuestaJson();
                respuestaJson.setId(rta.getIdPersistido());
                preguntaJson.getRespuestas().add(respuestaJson);
            }
            encuesta.getPreguntas().add(preguntaJson);
        }
        return encuesta;
    }

    private void showAlertDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModificarEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
        alertDialog.setMessage("¡La encuesta ha sido modificada correctamente!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", (dialog, which) -> {
            dialog.cancel();

            ProgressDialog progressDialog = new ProgressDialog(ModificarEncuestaActivity.this);
            progressDialog.setTitle("Regresando");
            progressDialog.setMessage("Espere por favor...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();

            PreguntaJson preguntaJson = getRespuestasRemover();
            Gson gson = new Gson();
            String body = gson.toJson(preguntaJson);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodType(HttpCall.POST);
            String url = getResources().getString(R.string.urlWS) + "/preguntas/removeRespuestas";
            httpCall.setUrl(url);
            HashMap<String, String> params = new HashMap<>();
            httpCall.setParams(params);
            httpCall.setBody(body);
            new HttpRequest(WebServiceEnum.ELIMINAR_RESPUESTA.getCodigo(), ModificarEncuestaActivity.this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    EncuestaJson encuestaJson = getPreguntasRemover();
                    Gson gson2 = new Gson();
                    String body2 = gson2.toJson(encuestaJson);

                    HttpCall httpCall2 = new HttpCall();
                    httpCall2.setMethodType(HttpCall.POST);
                    String url2 = getResources().getString(R.string.urlWS) + "/encuestas/removePreguntas";
                    httpCall2.setUrl(url2);
                    HashMap<String, String> params2 = new HashMap<>();
                    httpCall2.setParams(params2);
                    httpCall2.setBody(body2);
                    new HttpRequest(WebServiceEnum.ELIMINAR_PREGUNTA.getCodigo(), ModificarEncuestaActivity.this) {
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            progressDialog.dismiss();
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                        }
                    }.execute(httpCall2);

                }
            }.execute(httpCall);
        });
        alertDialog.show();
        isAlertOpen = true;
    }

    private void updatePregunta(PreguntaDto pregunta){
        if (pregunta.getPreguntaModificada()){
            String urlPregunta = getResources().getString(R.string.urlWS) + "/preguntas/updatePregunta?idPregunta=" + pregunta.getIdPersistido()
                    + "&descripcion=" + reemplazarEspacios(pregunta.getDescripcion())
                    + "&numeroEscala=" + (pregunta.getMaximaEscala() != null ? pregunta.getMaximaEscala() : "")
                    + "&idUsuario=" + usuarioLogueado.getId();
            httpAsyncTaskPregunta = new HttpAsyncTask(WebServiceEnum.MODIFICAR_PREGUNTA.getCodigo());
            httpAsyncTaskPregunta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
            try{
                String receivedDataPregunta = httpAsyncTaskPregunta.execute(urlPregunta).get();
            }
            catch (ExecutionException | InterruptedException ei){
                ei.printStackTrace();
            }
            if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty() && (pregunta.getTipoPregunta().equals(TipoPreguntaEnum.MULTIPLE_CHOICE) || pregunta.getTipoPregunta().equals(TipoPreguntaEnum.RESPUESTA_UNICA))){
                for (RespuestaDto rta : pregunta.getRespuestas()){
                    if (rta.getIdPersistido() != null){
                        updateRespuesta(rta);
                    } else {
                        saveRespuesta(rta, pregunta.getIdPersistido(), pregunta.getTipoPregunta().getCodigo());
                    }
                }
            }
        }
    }

    private void updateRespuesta(RespuestaDto respuesta){
        if (respuesta.getRespuestaModificada()){
            String urlRespuesta = getResources().getString(R.string.urlWS) + "/respuestas/updateRespuesta?idRespuesta=" + respuesta.getIdPersistido()
                    + "&descripcion=" + reemplazarEspacios(respuesta.getDescripcion())
                    + "&idUsuario=" + usuarioLogueado.getId();
            httpAsyncTaskRespuesta = new HttpAsyncTask(WebServiceEnum.MODIFICAR_RESPUESTA.getCodigo());
            httpAsyncTaskRespuesta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
            try{
                String receivedDataRta = httpAsyncTaskRespuesta.execute(urlRespuesta).get();
            }
            catch (ExecutionException | InterruptedException ei){
                ei.printStackTrace();
            }
        }
    }

    private void saveRespuesta(RespuestaDto respuesta, Integer idPreg, Integer idTipoRespuesta){
        String desc = respuesta != null ? respuesta.getDescripcion() : "";
        String urlRespuesta = getResources().getString(R.string.urlWS) + "/respuestas/saveRespuesta?descripcion=" + reemplazarEspacios(desc)
                + "&idPregunta=" + idPreg + "&idTipoRespuesta=" + idTipoRespuesta
                + "&idUsuario=" + usuarioLogueado.getId();
        httpAsyncTaskRespuesta = new HttpAsyncTask(WebServiceEnum.CREAR_RESPUESTA.getCodigo());
        httpAsyncTaskRespuesta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
        try{
            String receivedDataRta = httpAsyncTaskRespuesta.execute(urlRespuesta).get();
        }
        catch (ExecutionException | InterruptedException ei){
            ei.printStackTrace();
        }
    }

    private void savePregunta(PreguntaDto pregunta){
        String urlPregunta = getResources().getString(R.string.urlWS) + "/preguntas/savePregunta?descripcion=" + reemplazarEspacios(pregunta.getDescripcion())
                + "&idEncuesta=" + idEncuestaAsignado + "&numeroEscala=" + (pregunta.getMaximaEscala() != null ? pregunta.getMaximaEscala() : "")
                + "&idTipoRespuesta=" + pregunta.getTipoPregunta().getCodigo() + "&idUsuario=" + usuarioLogueado.getId();
        httpAsyncTaskPregunta = new HttpAsyncTask(WebServiceEnum.CREAR_PREGUNTA.getCodigo());
        httpAsyncTaskPregunta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
        try{
            String receivedDataPregunta = httpAsyncTaskPregunta.execute(urlPregunta).get();
        }
        catch (ExecutionException | InterruptedException ei){
            ei.printStackTrace();
        }

        if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty() && idPreguntaAsignado != null){
            for (RespuestaDto rta : pregunta.getRespuestas()){
                saveRespuesta(rta, idPreguntaAsignado, pregunta.getTipoPregunta().getCodigo());
            }
        } else {
            saveRespuesta(null, idPreguntaAsignado, pregunta.getTipoPregunta().getCodigo());
        }

        idPreguntaAsignado = null;
    }

    private void removeRespuesta(RespuestaDto respuesta){
        String urlRespuesta = getResources().getString(R.string.urlWS) + "/respuestas/deleteRespuesta?idRespuesta=" + respuesta.getIdPersistido()
                + "&idUsuario=" + usuarioLogueado.getId();
        httpAsyncTaskRespuesta = new HttpAsyncTask(WebServiceEnum.ELIMINAR_RESPUESTA.getCodigo());
        httpAsyncTaskRespuesta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
        try{
            String receivedDataRta = httpAsyncTaskRespuesta.execute(urlRespuesta).get();
        }
        catch (ExecutionException | InterruptedException ei){
            ei.printStackTrace();
        }
    }

    private void removePregunta(PreguntaDto pregunta){
        String urlPregunta = getResources().getString(R.string.urlWS) + "/preguntas/deletePregunta?idPregunta=" + pregunta.getIdPersistido()
                + "&idUsuario=" + usuarioLogueado.getId();
        httpAsyncTaskPregunta = new HttpAsyncTask(WebServiceEnum.ELIMINAR_PREGUNTA.getCodigo());
        httpAsyncTaskPregunta.setHttpAsyncTaskInterface(ModificarEncuestaActivity.this);
        try{
            String receivedDataPregunta = httpAsyncTaskPregunta.execute(urlPregunta).get();
        }
        catch (ExecutionException | InterruptedException ei){
            ei.printStackTrace();
        }
    }

    @Override
    public void crearPregunta(String result) {
        String createPreguntaJSON = result;
        if (createPreguntaJSON != null && !createPreguntaJSON.isEmpty()) {
            idPreguntaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    private void reemplazarPreguntaModificada(PreguntaDto dto){
        for (PreguntaDto p : preguntas){
            if (p.getId().equals(dto.getId())){
                p.setDescripcion(dto.getDescripcion());
                p.setMaximaEscala(dto.getMaximaEscala());
                p.setRespuestas(dto.getRespuestas());
                p.setPreguntaModificada(dto.getPreguntaModificada());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tituloNuevo", titulo.getEditText().getText().toString());
        outState.putString("descripcionNuevo", descripcion.getEditText().getText().toString());
        outState.putBoolean("estaAbierto", isAlertOpen);
        outState.putString("edadRestriccion", mayoresDe.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    private String reemplazarEspacios(String valor){
        String aux = valor.replace(" ", "%20");
        aux = aux.replace("\n", "%20");
        return aux;
    }

    @Override
    public void onBackPressed() {
        if (actionMenu.isOpened()){
            actionMenu.close(true);
        }else{
            posibleSalida();
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModificarEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Qué acción desea realizar?");
        alertDialog.setNegativeButton("Eliminar", (dialogInterface, i) -> deletePregunta(adapter.getItem(position)));
        alertDialog.setNeutralButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
        alertDialog.setPositiveButton("Modificar", (dialogInterface, i) -> openModificacionPregunta(adapter.getItem(position)));
        alertDialog.show();
    }

    private void deletePregunta(PreguntaDto dto){
        for (PreguntaDto p : preguntas){
            if (p.getId().equals(dto.getId())){
                p.setActivo(Boolean.FALSE);
            }else if (p.getId() > dto.getId()){
                p.setId(p.getId()-1);
            }
        }
        for (PreguntaDto p : preguntas){
            if (!p.getActivo()){
                if (p.getPreguntaPersistida()){
                    preguntasEliminar.add(p);
                }
                preguntas.remove(p);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(ModificarEncuestaActivity.this, "La pregunta ha sido eliminada correctamente!", Toast.LENGTH_LONG).show();
    }

    private void openModificacionPregunta(PreguntaDto dto) {
        switch (dto.getTipoPregunta().getCodigo()){
            case 1:
                actionMenu.close(true);
                Intent agregar_multiple_choice = new Intent(ModificarEncuestaActivity.this, PreguntaMultipleChoiceActivity.class);
                agregar_multiple_choice.putExtra("modificando", true);
                agregar_multiple_choice.putExtra("preguntaChoice", dto.getDescripcion());
                agregar_multiple_choice.putExtra("idPreguntaModificar", dto.getId());
                agregar_multiple_choice.putExtra("respuestasChoice", dto.getRespuestas());
                agregar_multiple_choice.putExtra("idPreguntaPersistida", dto.getIdPersistido());
                agregar_multiple_choice.putExtra("respuestasEliminar", new ArrayList<RespuestaDto>());
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 2:
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(ModificarEncuestaActivity.this, PreguntaUnicaOpcionActivity.class);
                agregar_unica_opcion.putExtra("modificando", true);
                agregar_unica_opcion.putExtra("preguntaUnica", dto.getDescripcion());
                agregar_unica_opcion.putExtra("idPreguntaModificar", dto.getId());
                agregar_unica_opcion.putExtra("respuestasUnica", dto.getRespuestas());
                agregar_unica_opcion.putExtra("idPreguntaPersistida", dto.getIdPersistido());
                agregar_unica_opcion.putExtra("respuestasEliminar", new ArrayList<RespuestaDto>());
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 3:
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(ModificarEncuestaActivity.this, PreguntaNumericaActivity.class);
                agregar_numerica.putExtra("modificando", true);
                agregar_numerica.putExtra("preguntaNumerica", dto.getDescripcion());
                agregar_numerica.putExtra("idPreguntaModificar", dto.getId());
                agregar_numerica.putExtra("idPreguntaPersistida", dto.getIdPersistido());
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 4:
                actionMenu.close(true);
                Intent agregar_textual = new Intent(ModificarEncuestaActivity.this, PreguntaTextualActivity.class);
                agregar_textual.putExtra("modificando", true);
                agregar_textual.putExtra("preguntaTextual", dto.getDescripcion());
                agregar_textual.putExtra("idPreguntaModificar", dto.getId());
                agregar_textual.putExtra("idPreguntaPersistida", dto.getIdPersistido());
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 5:
                actionMenu.close(true);
                Intent agregar_escala = new Intent(ModificarEncuestaActivity.this, PreguntaEscalaActivity.class);
                agregar_escala.putExtra("modificando", true);
                agregar_escala.putExtra("preguntaEscala", dto.getDescripcion());
                agregar_escala.putExtra("maximaEscala", dto.getMaximaEscala());
                agregar_escala.putExtra("idPreguntaModificar", dto.getId());
                agregar_escala.putExtra("idPreguntaPersistida", dto.getIdPersistido());
                startActivityForResult(agregar_escala, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }

    private void posibleSalida(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModificarEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos modificados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
            dialog.cancel();
            setResult(RESULT_FIRST_USER);
            ModificarEncuestaActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }
}
