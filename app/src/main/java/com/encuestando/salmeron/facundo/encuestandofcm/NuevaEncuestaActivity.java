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

/**
 * Created by Facundo Salmerón on 12/9/2018.
 */

public class
NuevaEncuestaActivity extends AppCompatActivity implements HttpAsyncTaskInterface, Serializable, PreguntaNuevaRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private FloatingActionButton multipleChoiceButton;
    private FloatingActionButton unicaButton;
    private FloatingActionButton numericaButton;
    private FloatingActionButton textualButton;
    private FloatingActionButton escalaButton;
    private FloatingActionMenu actionMenu;
    private ArrayList<PreguntaDto> preguntas;
    private RecyclerView recyclerView;
    private PreguntaNuevaRecyclerViewAdapter adapter;
    private CardView crear;
    private CardView volver;
    private TextInputLayout titulo;
    private TextInputLayout descripcion;
    private String tituloGuardar;
    private String descripcionGuardar;
    private Integer idEncuestaAsignado;
    private Integer idPreguntaAsignado;
    private Integer idRespuestaAsignado;
    private TextView errores;
    private boolean isAlertOpen = false;
    private CheckBox respuestasGeolocalizadas;
    private CheckBox restringir;
    private CheckBox sexo_restringir;
    private CheckBox edad_restringir;
    private TextInputLayout mayoresDe;
    private RadioGroup sexo_radioGroup;
    private RadioButton sexo_radioButton;
    private Integer isSexoRestriccion;
    private Integer isEdadRestriccion;
    private Boolean geolocalizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_encuesta_activity);
        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        tituloGuardar = (String) getIntent().getSerializableExtra("tituloGuardar");
        descripcionGuardar = (String) getIntent().getSerializableExtra("descripcionGuardar");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        errores = findViewById(R.id.erroresRestricciones);
        errores.setVisibility(View.GONE);
        toolbar = findViewById(R.id.titulo_nueva_encuesta);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> {
            NuevaEncuestaActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        crear = findViewById(R.id.crear_nueva_encuesta_button);
        volver = findViewById(R.id.volver_nueva_encuesta_button);
        titulo = findViewById(R.id.titulo_encuesta_nueva);
        descripcion = findViewById(R.id.descripcion_encuesta_nueva);
        geolocalizada = (Boolean) getIntent().getSerializableExtra("geolocalizada");
        isSexoRestriccion = (Integer) getIntent().getSerializableExtra("isSexoRestriccion");
        isEdadRestriccion = (Integer) getIntent().getSerializableExtra("isEdadRestriccion");

        respuestasGeolocalizadas = findViewById(R.id.checkbox_geolocalizada);
        respuestasGeolocalizadas.setChecked(geolocalizada);

        restringir = findViewById(R.id.checkbox_restricciones);
        sexo_restringir = findViewById(R.id.checkbox_restriccion_sexo);
        edad_restringir = findViewById(R.id.checkbox_restriccion_edad);
        mayoresDe = findViewById(R.id.restriccion_mayores_de);
        sexo_radioGroup = findViewById(R.id.restriccion_sexo);


        if (isSexoRestriccion != 0 || isEdadRestriccion != 0) {
            restringir.setChecked(true);
            if (isSexoRestriccion != 0) {
                sexo_restringir.setChecked(true);
                if (isSexoRestriccion.equals(1)) {
                    sexo_radioButton = findViewById(R.id.sexo_masculino_encuestado_button);
                } else {
                    sexo_radioButton = findViewById(R.id.sexo_femenino_encuestado_button);
                }
                sexo_radioButton.setChecked(true);

            }

            if (isEdadRestriccion != 0) {
                edad_restringir.setChecked(true);
                mayoresDe.getEditText().setText(isEdadRestriccion.toString());
            }
        }

        verificarRestricciones();

        sexo_radioGroup.setOnCheckedChangeListener((radioGroup, i) -> sexo_radioButton = findViewById(i));

        restringir.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (restringir.isChecked()) {
                sexo_restringir.setVisibility(View.VISIBLE);
                edad_restringir.setVisibility(View.VISIBLE);
                if (edad_restringir.isChecked()) {
                    mayoresDe.setVisibility(View.VISIBLE);
                } else {
                    mayoresDe.setVisibility(View.GONE);
                }

                if (sexo_restringir.isChecked()) {
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
            if (sexo_restringir.isChecked()) {
                sexo_radioGroup.setVisibility(View.VISIBLE);
            } else {
                sexo_radioGroup.setVisibility(View.GONE);
            }
        });

        edad_restringir.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (edad_restringir.isChecked()) {
                mayoresDe.setVisibility(View.VISIBLE);
            } else {
                mayoresDe.setVisibility(View.GONE);
            }
        });

        titulo.getEditText().setText(tituloGuardar);
        descripcion.getEditText().setText(descripcionGuardar);
        volver.setOnClickListener(view -> posibleSalida());
        crear.setOnClickListener(view -> onClickCrearEncuesta());
        recyclerView = findViewById(R.id.recycler_encuesta_nueva);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntaNuevaRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        actionMenu = findViewById(R.id.boton_agregar_pregunta);
        multipleChoiceButton = findViewById(R.id.multiple_choice_button);
        multipleChoiceButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_multiple_choice = new Intent(NuevaEncuestaActivity.this, PreguntaMultipleChoiceActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_multiple_choice, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        unicaButton = findViewById(R.id.unica_button);
        unicaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_unica_opcion = new Intent(NuevaEncuestaActivity.this, PreguntaUnicaOpcionActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_unica_opcion, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        numericaButton = findViewById(R.id.numerica_button);
        numericaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_numerica = new Intent(NuevaEncuestaActivity.this, PreguntaNumericaActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_numerica, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        textualButton = findViewById(R.id.textual_button);
        textualButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_textual = new Intent(NuevaEncuestaActivity.this, PreguntaTextualActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_textual, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        escalaButton = findViewById(R.id.escala_button);
        escalaButton.setOnClickListener(view -> {
            actionMenu.close(true);
            Intent agregar_escala = new Intent(NuevaEncuestaActivity.this, PreguntaEscalaActivity.class).putExtra("modificando", false);
            startActivityForResult(agregar_escala, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        if (savedInstanceState != null) {
            String tituloNuevo = savedInstanceState.getString("tituloNuevo");
            String descripcionNuevo = savedInstanceState.getString("descripcionNuevo");
            String edadRestriccion = savedInstanceState.getString("edadRestriccion");
            boolean estaAbierto = savedInstanceState.getBoolean("estaAbierto");
            if (tituloNuevo != null) {
                titulo.getEditText().setText(tituloNuevo);
            }
            if (descripcionNuevo != null) {
                descripcion.getEditText().setText(descripcionNuevo);
            }
            if (edadRestriccion != null) {
                mayoresDe.getEditText().setText(edadRestriccion);
            }
            if (estaAbierto) {
                showAlertDialog();
            }
        }
    }

    private void verificarRestricciones() {
        if (restringir.isChecked()) {
            sexo_restringir.setVisibility(View.VISIBLE);
            edad_restringir.setVisibility(View.VISIBLE);
            if (edad_restringir.isChecked()) {
                mayoresDe.setVisibility(View.VISIBLE);
            } else {
                mayoresDe.setVisibility(View.GONE);
            }

            if (sexo_restringir.isChecked()) {
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

    private void onClickCrearEncuesta() {
        titulo.setError(null);
        descripcion.setError(null);
        errores.setVisibility(View.GONE);
        if (titulo.getEditText().getText().toString().isEmpty()) {
            titulo.setError("El título no puede estar vacío!");
            return;
        } else if (titulo.getEditText().getText().length() > titulo.getCounterMaxLength()) {
            titulo.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (descripcion.getEditText().getText().length() > descripcion.getCounterMaxLength()) {
            descripcion.setError("Se superó la cantidad máxima permitida de caracteres.");
            return;
        }

        if (preguntas.size() < 1) {
            descripcion.setError("Debe agregarse al menos una pregunta.");
            return;
        }

        if (restringir.isChecked() && (!edad_restringir.isChecked() && !sexo_restringir.isChecked())) {
            errores.setText("Si escogió restringir, debe tildar al menos una de las opciones");
            errores.setVisibility(View.VISIBLE);
            return;
        }

        if (restringir.isChecked() && edad_restringir.isChecked() && mayoresDe.getEditText().getText().toString().isEmpty()) {
            errores.setText("Debe ingresar la edad de restricción");
            errores.setVisibility(View.VISIBLE);
            return;
        }

        if (restringir.isChecked() && sexo_restringir.isChecked() && sexo_radioButton == null) {
            errores.setText("Debe ingresar el sexo de restricción");
            errores.setVisibility(View.VISIBLE);
            return;
        }

        new Thread(() -> runOnUiThread(() -> {
            String geo = respuestasGeolocalizadas.isChecked() ? "true" : "false";
            String isEdad = (restringir.isChecked() && edad_restringir.isChecked()) ? mayoresDe.getEditText().getText().toString() : "";
            String isSexo = (restringir.isChecked() && sexo_restringir.isChecked()) ? (sexo_radioButton.getHint().toString().equals("Masculino") ? "1" : "2") : "";

            ProgressDialog progressDialog = new ProgressDialog(NuevaEncuestaActivity.this);
            progressDialog.setTitle("Creando Encuesta");
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
            String url = getResources().getString(R.string.urlWS) + "/encuestas/almacenarEncuesta";
            httpCall.setUrl(url);
            HashMap<String, String> params = new HashMap<>();
            httpCall.setParams(params);
            httpCall.setBody(body);
            new HttpRequest(WebServiceEnum.CREAR_ENCUESTA.getCodigo(), NuevaEncuestaActivity.this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    progressDialog.dismiss();
                    if (idEncuestaAsignado == null) {
                        progressDialog.dismiss();
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
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


            /*HttpCall httpCall = new HttpCall();
            httpCall.setMethodType(HttpCall.GET);
            String url = getResources().getString(R.string.urlWS) + "/encuestas/saveEncuesta";
            httpCall.setUrl(url);
            HashMap<String, String> params = new HashMap<>();
            params.put("titulo", titulo.getEditText().getText().toString());
            params.put("descripcion", descripcion.getEditText().getText().toString());
            params.put("isGeolocalizada", geo);
            params.put("isSexo", isSexo);
            params.put("isEdad", isEdad);
            params.put("idUsuario", usuarioLogueado.getId().toString());
            httpCall.setParams(params);
            new HttpRequest(WebServiceEnum.CREAR_ENCUESTA.getCodigo(), NuevaEncuestaActivity.this) {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    if (idEncuestaAsignado == null) {
                        progressDialog.dismiss();
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
                        alertDialog.setTitle("Error de Conexión");
                        alertDialog.setMessage("Verifique su conexión a Internet! \n\nSi el problema persiste se trata de un error interno en la base de datos.");
                        alertDialog.setIcon(R.drawable.ic_action_error);
                        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                        alertDialog.show();
                    } else {
                        for (PreguntaDto pregunta : preguntas) {
                            HttpCall httpCallPregunta = new HttpCall();
                            httpCallPregunta.setMethodType(HttpCall.GET);
                            String urlPregunta = getResources().getString(R.string.urlWS) + "/preguntas/savePregunta";
                            httpCallPregunta.setUrl(urlPregunta);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("descripcion", pregunta.getDescripcion());
                            params.put("idEncuesta", idEncuestaAsignado.toString());
                            params.put("numeroEscala", pregunta.getMaximaEscala() != null ? pregunta.getMaximaEscala().toString() : "");
                            params.put("idTipoRespuesta", pregunta.getTipoPregunta().getCodigo().toString());
                            params.put("idUsuario", usuarioLogueado.getId().toString());
                            httpCallPregunta.setParams(params);
                            try {
                                String receiverPregunta = new HttpRequest(WebServiceEnum.CREAR_PREGUNTA.getCodigo(), NuevaEncuestaActivity.this) {
                                    @Override
                                    public void onResponse(String response) {
                                        super.onResponse(response);
                                        if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty() && idPreguntaAsignado != null) {
                                            for (RespuestaDto rta : pregunta.getRespuestas()) {
                                                HttpCall httpCallRespuesta = new HttpCall();
                                                httpCallRespuesta.setMethodType(HttpCall.GET);
                                                String urlRespuesta = getResources().getString(R.string.urlWS) + "/respuestas/saveRespuesta";
                                                httpCallRespuesta.setUrl(urlRespuesta);
                                                HashMap<String, String> params = new HashMap<>();
                                                params.put("descripcion", rta.getDescripcion());
                                                params.put("idPregunta", idPreguntaAsignado.toString());
                                                params.put("idTipoRespuesta", pregunta.getTipoPregunta().getCodigo().toString());
                                                params.put("idUsuario", usuarioLogueado.getId().toString());
                                                httpCallRespuesta.setParams(params);
                                                try {
                                                    String receivedRespuesta1 = new HttpRequest(WebServiceEnum.CREAR_RESPUESTA.getCodigo(), NuevaEncuestaActivity.this) {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            super.onResponse(response);
                                                            idRespuestaAsignado = null;
                                                        }
                                                    }.execute(httpCallRespuesta).get();
                                                } catch (ExecutionException | InterruptedException ei) {
                                                    ei.printStackTrace();
                                                }
                                            }
                                        } else {
                                            HttpCall httpCallRta = new HttpCall();
                                            httpCallRta.setMethodType(HttpCall.GET);
                                            String urlRta = getResources().getString(R.string.urlWS) + "/respuestas/saveRespuesta";
                                            httpCallRta.setUrl(urlRta);
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("descripcion", "");
                                            params.put("idPregunta", idPreguntaAsignado.toString());
                                            params.put("idTipoRespuesta", pregunta.getTipoPregunta().getCodigo().toString());
                                            params.put("idUsuario", usuarioLogueado.getId().toString());
                                            httpCallRta.setParams(params);
                                            try {
                                                String receiverRespuesta2 = new HttpRequest(WebServiceEnum.CREAR_RESPUESTA.getCodigo(), NuevaEncuestaActivity.this) {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        super.onResponse(response);
                                                        idRespuestaAsignado = null;
                                                    }
                                                }.execute(httpCallRta).get();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }.execute(httpCallPregunta).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            idPreguntaAsignado = null;
                        }
                        progressDialog.dismiss();
                        showAlertDialog();
                    }
                }
            }.execute(httpCall);*/
        })).start();


    }

    private EncuestaJson getEncuestaPersistir(String geo, String isSexo, String isEdad) {
        EncuestaJson encuesta = new EncuestaJson();
        encuesta.setTitulo(titulo.getEditText().getText().toString());
        encuesta.setDescripcion(descripcion.getEditText().getText().toString());
        encuesta.setGeolicalizada(geo.equals("true") ? Boolean.TRUE : Boolean.FALSE);
        encuesta.setIsSexoRestriction(isSexo.isEmpty() ? null : Integer.valueOf(isSexo));
        encuesta.setIsEdadRestriction(isEdad.isEmpty() ? null : Integer.valueOf(isEdad));
        encuesta.setIdUsuarioAlta(usuarioLogueado.getId());
        for (PreguntaDto pregunta : preguntas) {
            PreguntaJson p = new PreguntaJson();
            p.setDescripcion(pregunta.getDescripcion());
            p.setNumeroEscala(pregunta.getMaximaEscala());
            p.setTipoRespuesta(new TipoRespuestaJson(pregunta.getTipoPregunta().getDescripcion(), pregunta.getTipoPregunta().getCodigo()));
            if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty()) {
                for (RespuestaDto rta : pregunta.getRespuestas()) {
                    RespuestaJson r = new RespuestaJson();
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

    @Override
    public void crearEncuesta(String result) {
        String createEncuestaJSON = result;
        if (createEncuestaJSON != null && !createEncuestaJSON.isEmpty()) {
            idEncuestaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void crearPregunta(String result) {
        String createPreguntaJSON = result;
        if (createPreguntaJSON != null && !createPreguntaJSON.isEmpty()) {
            idPreguntaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void crearRespuesta(String result) {
        String createPreguntaJSON = result;
        if (createPreguntaJSON != null && !createPreguntaJSON.isEmpty()) {
            idRespuestaAsignado = JSONConverterUtils.JSONCreateEncuestaConverter(result);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tituloNuevo", titulo.getEditText().getText().toString());
        outState.putString("descripcionNuevo", descripcion.getEditText().getText().toString());
        outState.putString("edadRestriccion", mayoresDe.getEditText().getText().toString());
        outState.putBoolean("estaAbierto", isAlertOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PreguntaDto dto = (PreguntaDto) data.getSerializableExtra("pregunta");
            boolean isModificacion = false;
            if (dto.getId() != null) {
                reemplazarPreguntaModificada(dto);
                isModificacion = true;
            } else {
                dto.setId(new Long(preguntas.size() + 1));
                preguntas.add(dto);
            }
            finish();
            getIntent().putExtra("preguntas", preguntas);
            getIntent().putExtra("tituloGuardar", titulo.getEditText().getText().toString());
            getIntent().putExtra("descripcionGuardar", descripcion.getEditText().getText().toString());

            getIntent().putExtra("geolocalizada", respuestasGeolocalizadas.isChecked());
            getIntent().putExtra("isSexoRestriccion", sexo_restringir.isChecked() ? (sexo_radioButton != null && sexo_radioButton.getHint().toString().equals("Masculino") ? 1 : 2) : 0);
            getIntent().putExtra("isEdadRestriccion", edad_restringir.isChecked() ? (!mayoresDe.getEditText().getText().toString().isEmpty() ? Integer.valueOf(mayoresDe.getEditText().getText().toString()) : 0) : 0);

            startActivity(getIntent());
            if (isModificacion) {
                Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido modificada correctamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido añadida correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reemplazarPreguntaModificada(PreguntaDto dto) {
        for (PreguntaDto p : preguntas) {
            if (p.getId().equals(dto.getId())) {
                p.setDescripcion(dto.getDescripcion());
                p.setMaximaEscala(dto.getMaximaEscala());
                p.setRespuestas(dto.getRespuestas());
            }
        }
    }

    private void posibleSalida() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
            dialog.cancel();
            NuevaEncuestaActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (actionMenu.isOpened()) {
            actionMenu.close(true);
        } else {
            posibleSalida();
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Qué acción desea realizar?");
        alertDialog.setNegativeButton("Eliminar", (dialogInterface, i) -> deletePregunta(adapter.getItem(position)));
        alertDialog.setNeutralButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
        alertDialog.setPositiveButton("Modificar", (dialogInterface, i) -> openModificacionPregunta(adapter.getItem(position)));
        alertDialog.show();
    }

    private void deletePregunta(PreguntaDto dto) {
        for (PreguntaDto p : preguntas) {
            if (p.getId().equals(dto.getId())) {
                p.setActivo(Boolean.FALSE);
            } else if (p.getId() > dto.getId()) {
                p.setId(p.getId() - 1);
            }
        }
        for (PreguntaDto p : preguntas) {
            if (!p.getActivo()) {
                preguntas.remove(p);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(NuevaEncuestaActivity.this, "La pregunta ha sido eliminada correctamente!", Toast.LENGTH_LONG).show();
    }

    private void openModificacionPregunta(PreguntaDto dto) {
        switch (dto.getTipoPregunta().getCodigo()) {
            case 1:
                actionMenu.close(true);
                Intent agregar_multiple_choice = new Intent(NuevaEncuestaActivity.this, PreguntaMultipleChoiceActivity.class);
                agregar_multiple_choice.putExtra("modificando", true);
                agregar_multiple_choice.putExtra("preguntaChoice", dto.getDescripcion());
                agregar_multiple_choice.putExtra("idPreguntaModificar", dto.getId());
                agregar_multiple_choice.putExtra("respuestasChoice", dto.getRespuestas());
                startActivityForResult(agregar_multiple_choice, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 2:
                actionMenu.close(true);
                Intent agregar_unica_opcion = new Intent(NuevaEncuestaActivity.this, PreguntaUnicaOpcionActivity.class);
                agregar_unica_opcion.putExtra("modificando", true);
                agregar_unica_opcion.putExtra("preguntaUnica", dto.getDescripcion());
                agregar_unica_opcion.putExtra("idPreguntaModificar", dto.getId());
                agregar_unica_opcion.putExtra("respuestasUnica", dto.getRespuestas());
                startActivityForResult(agregar_unica_opcion, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 3:
                actionMenu.close(true);
                Intent agregar_numerica = new Intent(NuevaEncuestaActivity.this, PreguntaNumericaActivity.class);
                agregar_numerica.putExtra("modificando", true);
                agregar_numerica.putExtra("preguntaNumerica", dto.getDescripcion());
                agregar_numerica.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_numerica, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 4:
                actionMenu.close(true);
                Intent agregar_textual = new Intent(NuevaEncuestaActivity.this, PreguntaTextualActivity.class);
                agregar_textual.putExtra("modificando", true);
                agregar_textual.putExtra("preguntaTextual", dto.getDescripcion());
                agregar_textual.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_textual, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 5:
                actionMenu.close(true);
                Intent agregar_escala = new Intent(NuevaEncuestaActivity.this, PreguntaEscalaActivity.class);
                agregar_escala.putExtra("modificando", true);
                agregar_escala.putExtra("preguntaEscala", dto.getDescripcion());
                agregar_escala.putExtra("maximaEscala", dto.getMaximaEscala());
                agregar_escala.putExtra("idPreguntaModificar", dto.getId());
                startActivityForResult(agregar_escala, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NuevaEncuestaActivity.this, AlertDialog.THEME_HOLO_DARK);
        alertDialog.setMessage("¡La encuesta ha sido creada correctamente!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", (dialog, which) -> {
            dialog.cancel();
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        alertDialog.show();
        isAlertOpen = true;
    }

}
