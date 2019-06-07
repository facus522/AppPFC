package com.encuestando.salmeron.facundo.encuestandofcm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Facundo Salmerón on 15/10/2018.
 */

public class ResponderEncuestaEspecialActivity extends AppCompatActivity implements HttpAsyncTaskInterface, EncuestaEspecialRecyclerViewAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UsuarioDto usuarioLogueado;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EncuestaEspecialRecyclerViewAdapter adapter;
    private ArrayList<ListaEncuestaDto> encuestas = new ArrayList<>();
    private ArrayList<PreguntaDto> preguntasAbrir = new ArrayList<>();
    private ArrayList<ResultadoDto> resultados = new ArrayList<>();
    private HttpAsyncTask httpAsyncTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean scrollEnabled;
    private ImageButton ayuda;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responder_encuesta_especial_activity);
        toolbar = findViewById(R.id.title_resolver_especial);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponderEncuestaEspecialActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ayuda = findViewById(R.id.ayuda_menu);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this, AlertDialog.THEME_HOLO_DARK);
                alertDialog.setTitle("Información");
                alertDialog.setMessage("Las encuestas que contienen la etiqueta de 'Geolocalizadas' guardarán en base de datos la ubicación del encuestado.\nSi escoje 'Exportar Resultados', deberá buscar luego en su directorio de descargas el archivo Excel de la encuesta.\nLa generación del archivo puede tomar unos segundos, por lo que puede no encontrar inmediatamente el mismo.\nSi rechazó los permisos activarlos desde Configuración del dispositivo -> Aplicaciones -> Encuestando FCM -> Permisos -> Almacenamiento.");
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
        usuarioLogueado = (UsuarioDto) getIntent().getSerializableExtra("usuario");

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_responder_especial);
        swipeRefreshLayout.setOnRefreshListener(this);

        String url = getResources().getString(R.string.urlWS) + "/encuestas/getAllHabilitadas";
        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.CARGAR_ENCUESTAS_HABILITADAS.getCodigo());
        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
        try {
            String receivedData = httpAsyncTask.execute(url).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        if (encuestas.size() < 1) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this);
            alertDialog.setTitle("Sin Datos");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Actualmente no se encuentran habilitadas Encuestas para responder.");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ResponderEncuestaEspecialActivity.this.finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            alertDialog.show();
        }

        recyclerView = findViewById(R.id.recycler_resolver_especial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EncuestaEspecialRecyclerViewAdapter(this, encuestas);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        verifyStoragePermissions(ResponderEncuestaEspecialActivity.this);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ?
                                0 : recyclerView.getChildAt(0).getTop();

                boolean newScrollEnabled =
                        (dx == 0 && topRowVerticalPosition >= 0) ?
                                true : false;

                if (null != ResponderEncuestaEspecialActivity.this.swipeRefreshLayout && scrollEnabled != newScrollEnabled) {
                    // Start refreshing....
                    ResponderEncuestaEspecialActivity.this.swipeRefreshLayout.setEnabled(newScrollEnabled);
                    scrollEnabled = newScrollEnabled;
                }
            }
        });

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        Toast.makeText(ResponderEncuestaEspecialActivity.this, "Actualizado!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("¿Qué acción desea realizar?");
        if (Integer.parseInt(encuestas.get(position).getResoluciones()) > 0) {
            alertDialog.setNegativeButton("Ver Estadísticas", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i2) {
                    dialogInterface.cancel();

                    String urlAbrirGraficos = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + encuestas.get(position).getId();
                    httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                    httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                    try {
                        String receivedData = httpAsyncTask.execute(urlAbrirGraficos).get();
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                    String urlTraer = getResources().getString(R.string.urlWS) + "/resultados/getResultados?idEncuesta=" + encuestas.get(position).getId();
                    httpAsyncTask = new HttpAsyncTask(WebServiceEnum.RESULTADOS_ENCUESTA.getCodigo());
                    httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                    try {
                        String receivedData = httpAsyncTask.execute(urlTraer).get();
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }

                    Intent graphics_intent = new Intent(ResponderEncuestaEspecialActivity.this, GraphicsActivity.class).putExtra("usuario", usuarioLogueado);
                    graphics_intent.putExtra("idEncuestaPersistida", encuestas.get(position).getId());
                    graphics_intent.putExtra("preguntas", preguntasAbrir);
                    graphics_intent.putExtra("resultados", resultados);
                    graphics_intent.putExtra("tituloEncuesta", encuestas.get(position).getTitulo());
                    graphics_intent.putExtra("descripcionEncuesta", encuestas.get(position).getDescripcion());
                    graphics_intent.putExtra("isGeolocalizada", encuestas.get(position).getGeolocalizada());
                    startActivityForResult(graphics_intent, 1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            alertDialog.setNeutralButton("Exportar Resultados", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();

                    if ((ActivityCompat.checkSelfPermission(ResponderEncuestaEspecialActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                            && (ActivityCompat.checkSelfPermission(ResponderEncuestaEspecialActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResponderEncuestaEspecialActivity.this, AlertDialog.THEME_HOLO_DARK);
                        alertDialog.setTitle("Error al Exportar");
                        alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_action_error));
                        alertDialog.setMessage("No se han podido exportar los resultados ya que ha denegado los permisos para utilizar el almacenamientola en su dispositivo móvil.\n Por favor actívelos.\n(Ver ayuda arriba a la derecha)");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                return;
                            }
                        });
                        alertDialog.show();
                    } else {
                        String urlAbrirGraficos = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + encuestas.get(position).getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(urlAbrirGraficos).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }

                        String urlTraer = getResources().getString(R.string.urlWS) + "/resultados/getResultados?idEncuesta=" + encuestas.get(position).getId();
                        httpAsyncTask = new HttpAsyncTask(WebServiceEnum.RESULTADOS_ENCUESTA.getCodigo());
                        httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                        try {
                            String receivedData = httpAsyncTask.execute(urlTraer).get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }

                        ExportResultsCSVTask task = new ExportResultsCSVTask();
                        Date fecha = new Date();
                        String newstring = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(fecha);
                        task.setNombreArchivo("encuesta_" + encuestas.get(position).getId() + "_" + newstring + "_FCM.csv");
                        task.setTituloEncuesta(encuestas.get(position).getTitulo());
                        task.setPreguntas(preguntasAbrir);
                        task.setResultados(resultados);
                        try {
                            String receivedData = task.execute().get();
                        } catch (ExecutionException | InterruptedException ei) {
                            ei.printStackTrace();
                        }
                    }


                }
            });
        } else {
            alertDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


        alertDialog.setPositiveButton("Responder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                String urlAbrir = getResources().getString(R.string.urlWS) + "/encuestas/openEncuesta?idEncuesta=" + encuestas.get(position).getId();
                httpAsyncTask = new HttpAsyncTask(WebServiceEnum.OPEN_ENCUESTA.getCodigo());
                httpAsyncTask.setHttpAsyncTaskInterface(ResponderEncuestaEspecialActivity.this);
                try {
                    String receivedData = httpAsyncTask.execute(urlAbrir).get();
                } catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

                Intent responder_intent = new Intent(ResponderEncuestaEspecialActivity.this, ResolviendoEncuestaEspecialActivity.class).putExtra("usuario", usuarioLogueado);
                responder_intent.putExtra("idEncuestaPersistida", encuestas.get(position).getId());
                responder_intent.putExtra("preguntas", preguntasAbrir);
                responder_intent.putExtra("tituloEncuesta", encuestas.get(position).getTitulo());
                responder_intent.putExtra("descripcionEncuesta", encuestas.get(position).getDescripcion());
                responder_intent.putExtra("isGeolocalizada", encuestas.get(position).getGeolocalizada());
                startActivityForResult(responder_intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        alertDialog.show();
    }

    public class ExportResultsCSVTask extends AsyncTask<String, String, String> {
        private final ProgressDialog dialog = new ProgressDialog(ResponderEncuestaEspecialActivity.this);
        private String nombreArchivo;
        private ArrayList<PreguntaDto> preguntas;
        private ArrayList<ResultadoDto> resultados;
        private String tituloEncuesta;

        public String getTituloEncuesta() {
            return tituloEncuesta;
        }

        public void setTituloEncuesta(String tituloEncuesta) {
            this.tituloEncuesta = tituloEncuesta;
        }

        public ArrayList<ResultadoDto> getResultados() {
            return resultados;
        }

        public void setResultados(ArrayList<ResultadoDto> resultados) {
            this.resultados = resultados;
        }

        public String getNombreArchivo() {
            return nombreArchivo;
        }

        public void setNombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
        }

        public ArrayList<PreguntaDto> getPreguntas() {
            return preguntas;
        }

        public void setPreguntas(ArrayList<PreguntaDto> preguntas) {
            this.preguntas = preguntas;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exportando resultados...");
            this.dialog.show();
        }

        protected String doInBackground(final String... args) {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, nombreArchivo);
            try {

                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                csvWrite.writeNext("Titulo Encuesta: ", tituloEncuesta);
                for (PreguntaDto preg : preguntas) {
                    csvWrite.writeNext("Pregunta: ", preg.getDescripcion());
                    csvWrite.writeNext("Edad Encuestado", "Sexo Encuestado", "Respuesta", "Latitud", "Longitud");
                    for (RespuestaDto rta : preg.getRespuestas()) {
                        for (ResultadoDto rdo : resultados) {
                            if (rta.getIdPersistido().equals(rdo.getIdRespuesta())) {
                                csvWrite.writeNext(rdo.getEdad().toString(),
                                        rdo.getSexo().equals(SexoEnum.MASCULINO.getCodigo()) ? "Masculino" : "Femenino",
                                        rdo.getDescripcion(),
                                        rdo.getLatitud(),
                                        rdo.getLongitud());
                            }
                        }
                    }
                }

                csvWrite.close();
                return "";
            } catch (IOException e) {
                Log.e("MainActivity", e.getMessage(), e);
                return "";
            }
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(final String success) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success.isEmpty()) {
                Toast.makeText(ResponderEncuestaEspecialActivity.this, "Guardando como " + nombreArchivo, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ResponderEncuestaEspecialActivity.this, "Exportación fallida!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            finish();
            startActivity(getIntent());
            Toast.makeText(ResponderEncuestaEspecialActivity.this, "La encuesta ha sido respondida correctamente!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cargarEncuestas(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            encuestas.addAll(JSONConverterUtils.JSONEncuestasConverter(result));
        }
    }

    @Override
    public void abrirEncuesta(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            preguntasAbrir = JSONConverterUtils.JSONAbrirEncuestasConverter(result);
        }
    }

    @Override
    public void traerResultadosEncuesta(String result) {
        String encuestasJSON = result;
        if (encuestasJSON != null && !encuestasJSON.isEmpty()) {
            resultados = JSONConverterUtils.JSONTraerResultadosEncuestaConverter(result);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
