package com.encuestando.salmeron.facundo.encuestandofcm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Facundo Salmerón on 04/11/2018.
 */

public class ResolviendoEncuestaNormalActivity extends AppCompatActivity implements HttpAsyncTaskInterface, PreguntaResponderRecyclerViewAdapter.ItemClickListener {

    private Toolbar toolbar;
    private UsuarioDto usuarioLogueado;
    private String tituloEncuesta;
    private String descripcionEncuesta;
    private TextView titulo;
    private TextView descripcion;
    private TextView cargandoErrores;
    private RecyclerView recyclerView;
    private PreguntaResponderRecyclerViewAdapter adapter;
    private ArrayList<PreguntaDto> preguntas;
    private CardView enviar;
    private CardView volver;
    private HttpAsyncTask httpAsyncTask;
    private Integer idEncuesta;
    private double latitud;
    private double longitud;
    private final int PICKER_REQUEST_CODE = 1;
    private CheckBox residencia;
    private Boolean isGeolocalizada;
    private ImageButton ayuda;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolviendo_encuesta_normal_activity);
        toolbar = findViewById(R.id.title_resolviendo_normal);
        tituloEncuesta = (String) getIntent().getSerializableExtra("tituloEncuesta");
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");
        descripcionEncuesta = (String) getIntent().getSerializableExtra("descripcionEncuesta");
        idEncuesta = (Integer) getIntent().getSerializableExtra("idEncuestaPersistida");
        isGeolocalizada = (Boolean) getIntent().getSerializableExtra("isGeolocalizada");

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResolviendoEncuestaNormalActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ayuda = findViewById(R.id.ayuda_respondiendo);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResolviendoEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Información");
                alertDialog.setMessage("Si se encuentra en su residencia debe tener activado su ubicación. Si rechazó los permisos activarlos desde Configuración del dispositivo -> Aplicaciones -> Encuestando FCM -> Permisos -> Ubicación.\nSi no se encuentra en su residencia habitual se abrirá un mapa donde deberá escoger su domicilio para completar con la encuesta Geolocalizada.");
                alertDialog.setIcon(R.drawable.ic_action_error);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        titulo = findViewById(R.id.titulo_encuesta_resolver);
        descripcion = findViewById(R.id.descripcion_encuesta_resolver);
        cargandoErrores = findViewById(R.id.cargandoErroresResponderNormal);
        cargandoErrores.setVisibility(View.GONE);
        residencia = findViewById(R.id.checkbox_residencia);
        if (!isGeolocalizada){
            residencia.setVisibility(View.GONE);
            ayuda.setVisibility(View.GONE);
        }
        else requestForSpecificPermission();

        enviar = findViewById(R.id.enviar_resolviendo_button);
        volver = findViewById(R.id.volver_resolviendo_button);

        volver.setOnClickListener((View view) ->
                posibleSalida()
        );
        enviar.setOnClickListener((View v) ->
                onClickResponderEncuesta()
        );

        titulo.setText(tituloEncuesta);
        descripcion.setText(descripcionEncuesta);

        preguntas = (ArrayList<PreguntaDto>) getIntent().getSerializableExtra("preguntas");
        recyclerView = findViewById(R.id.recycler_resolviendo_normal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PreguntaResponderRecyclerViewAdapter(this, preguntas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void onClickResponderEncuesta() {
        cargandoErrores.setTextColor(getResources().getColor(R.color.colorAccent));
        if (!evaluarRespuestas()) {
            cargandoErrores.setText("Debe responder todas las preguntas!");
            cargandoErrores.setVisibility(View.VISIBLE);
        } else {
            cargandoErrores.setVisibility(View.VISIBLE);
            cargandoErrores.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            cargandoErrores.setText("Cargando...");

            new Thread(() -> {
                runOnUiThread(() -> {
                    if (isGeolocalizada && !residencia.isChecked()) {
                        try {
                            Intent intent = new PlacePicker.IntentBuilder().build(this);
                            startActivityForResult(intent, PICKER_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    } else if (isGeolocalizada && residencia.isChecked()) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResolviendoEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                            alertDialog.setTitle("Error al Responder");
                            alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_action_error));
                            alertDialog.setMessage("No se ha podido responder la encuesta ya que ha denegado los permisos para utilizar la ubicación de su dispositivo móvil.\n Por favor actívelos.\n(Ver ayuda arriba a la derecha)");
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    return;
                                }
                            });
                            alertDialog.show();
                            cargandoErrores.setVisibility(View.GONE);
                        } else {
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        latitud = location.getLatitude();
                                        longitud = location.getLongitude();

                                        persistirRespuestas(usuarioLogueado.getSexo().toString());
                                        cargandoErrores.setVisibility(View.GONE);
                                        incrementarResoluciones();
                                        Intent returnIntent = new Intent();
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                    } else {
                                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResolviendoEncuestaNormalActivity.this, AlertDialog.THEME_HOLO_DARK);
                                        alertDialog.setTitle("Error al Responder");
                                        alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_action_error));
                                        alertDialog.setMessage("No se ha podido responder la encuesta ya que no tiene activada la ubicación de su dispositivo móvil.\n Por favor actívela.");
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                return;
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                    cargandoErrores.setVisibility(View.GONE);
                                }
                            });
                        }

                    } else {
                        persistirRespuestas(usuarioLogueado.getSexo().toString());
                        cargandoErrores.setVisibility(View.GONE);
                        incrementarResoluciones();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        cargandoErrores.setVisibility(View.GONE);
                    }

                });
            }).start();
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                latitud = latLng.latitude;
                longitud = latLng.longitude;

                persistirRespuestas(usuarioLogueado.getSexo().toString());
                cargandoErrores.setVisibility(View.GONE);
                incrementarResoluciones();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            cargandoErrores.setVisibility(View.GONE);
        }
    }


    private void incrementarResoluciones() {
        String urlIncrementando = getResources().getString(R.string.urlWS) + "/encuestas/incrementarResolucion?idEncuesta=" + idEncuesta;
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.INCREMENTAR_RESULTADO.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResolviendoEncuestaNormalActivity.this);
        try {
            String receivedDataEncuesta = httpAsyncTask.execute(urlIncrementando).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }

    private void persistirRespuestas(String sexo) {
        for (RecyclerView.ViewHolder vh : adapter.getViewHolders()) {
            switch (vh.getItemViewType()) {
                case 1:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderChoicer = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    ArrayList<RespuestaTildadaDto> respuestasChecked = getRespuestasChecked(viewHolderChoicer.getListaCheckbox());
                    for (RespuestaTildadaDto i : respuestasChecked) {
                        saveRespuesta(i.getId(), i.getDescripcion(), sexo);
                    }
                    break;
                case 2:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderUnica = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    RespuestaTildadaDto respuestaTildada = getRespuestaTildada(viewHolderUnica.getListaRadios());
                    saveRespuesta(respuestaTildada.getId(), respuestaTildada.getDescripcion(), sexo);
                    break;
                case 5:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolderScale = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    String rtaEscala = viewHolderScale.getScaleEditText().getText().toString();
                    Integer idSc = viewHolderScale.getScaleEditText().getId();
                    saveRespuesta(idSc, rtaEscala, sexo);
                    break;
                default:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolder = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    String rtaTextNum = viewHolder.getEditText().getText().toString();
                    Integer idTN = viewHolder.getEditText().getId();
                    saveRespuesta(idTN, rtaTextNum, sexo);
                    break;
            }
        }
    }

    private void saveRespuesta(Integer id, String descripcion, String sexo) {
        String urlRespuesta = getResources().getString(R.string.urlWS) + "/resultados/saveResultado?latitud=" + String.valueOf(latitud)
                + "&longitud=" + String.valueOf(longitud)
                + "&edadEncuestado=" + usuarioLogueado.getEdad().toString()
                + "&sexoEncuestado=" + sexo
                + "&idUsuario=" + usuarioLogueado.getId()
                + "&idRespuesta=" + id
                + "&descripcion=" + reemplazarEspacios(descripcion)
                + "&idEncuesta=" + idEncuesta;
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CONTESTAR_PREGUNTA.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResolviendoEncuestaNormalActivity.this);
        try {
            String receivedDataEncuesta = httpAsyncTask.execute(urlRespuesta).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }

    private String reemplazarEspacios(String valor){

        String aux = valor.replace(" ", "%20");
        aux = aux.replace("\n", "%20");
        return aux;
    }

    private ArrayList<RespuestaTildadaDto> getRespuestasChecked(ArrayList<CheckBox> checkBoxes) {
        ArrayList<RespuestaTildadaDto> rtas = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                RespuestaTildadaDto rta = new RespuestaTildadaDto();
                rta.setId(cb.getId());
                rta.setDescripcion(cb.getText().toString());
                rtas.add(rta);
            }
        }
        return rtas;
    }

    private RespuestaTildadaDto getRespuestaTildada(ArrayList<RadioButton> radioButtons) {
        RespuestaTildadaDto rta = new RespuestaTildadaDto();
        for (RadioButton rb : radioButtons) {
            if (rb.isChecked()) {
                rta.setId(rb.getId());
                rta.setDescripcion(rb.getText().toString());
                break;
            }
        }
        return rta;
    }

    private boolean evaluarRespuestas() {
        for (RecyclerView.ViewHolder vh : adapter.getViewHolders()) {
            switch (vh.getItemViewType()) {
                case 1:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderChoicer = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    if (!evaluarRespuestasCheckbox(viewHolderChoicer.getListaCheckbox()))
                        return false;
                    break;
                case 2:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder2 viewHolderUnica = (PreguntaResponderRecyclerViewAdapter.ViewHolder2) vh;
                    if (!evaluarRespuestasRadios(viewHolderUnica.getListaRadios())) return false;
                    break;
                case 5:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolderScale = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    if (viewHolderScale.getScaleEditText().getText().toString().isEmpty())
                        return false;
                    break;
                default:
                    PreguntaResponderRecyclerViewAdapter.ViewHolder viewHolder = (PreguntaResponderRecyclerViewAdapter.ViewHolder) vh;
                    if (viewHolder.getEditText().getText().toString().isEmpty()) return false;
                    break;
            }
        }
        return true;
    }

    private boolean evaluarRespuestasCheckbox(ArrayList<CheckBox> checkBoxes) {
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluarRespuestasRadios(ArrayList<RadioButton> radioButtons) {
        for (RadioButton rb : radioButtons) {
            if (rb.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private void posibleSalida() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResolviendoEncuestaNormalActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todas las respuestas!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ResolviendoEncuestaNormalActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
    public void onBackPressed() {
        posibleSalida();
    }
}
