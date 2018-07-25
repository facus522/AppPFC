package com.lsikh.unlmaps.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lsikh.unlmaps.MainActivity;
import com.lsikh.unlmaps.R;
import com.lsikh.unlmaps.database.SQLiteDataBase;
import com.lsikh.unlmaps.model.PuntoInteresDto;
import com.lsikh.unlmaps.model.TipoDependenciaDto;
import com.lsikh.unlmaps.model.UnidadAcademicaDto;
import com.lsikh.unlmaps.utils.HttpAsyncTask;
import com.lsikh.unlmaps.interfaces.HttpAsyncTaskInterface;
import com.lsikh.unlmaps.utils.JSONConverterUtils;
import com.lsikh.unlmaps.utils.UrlBuilder;

import org.apache.commons.lang3.ArrayUtils;


public class SearchFragment extends Fragment implements HttpAsyncTaskInterface {

    private static Integer[] TIPO_DEPENDENCIAS_SHOW_UNIDAD_ACADEMICA_SPINNER = {1, 4, 6, 7, 8, 9, 10};

    private static Integer[] TIPO_DEPENDENCIAS_SHOW_PUNTO_INTERES_SPINNER = {1, 7, 8, 9, 10, 12};

    private Spinner unidadAcademicaSpinner;

    private Spinner tipoDependenciaSpinner;

    private Spinner puntoInteresSpinner;

    private Button searchButton;

    private TextView unidadAcademicaLabel;

    private MainActivity mainActivity;

    private HttpAsyncTask httpAsyncTask;

    private SQLiteDataBase sqLiteDataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        sqLiteDataBase = new SQLiteDataBase(getActivity(), "DB", null, SQLiteDataBase.CURRENT_DATABASE_VERSION);

        httpAsyncTask = new HttpAsyncTask(0);
        httpAsyncTask.setHttpAsyncTaskInterface(this);
        httpAsyncTask.execute(UrlBuilder.getUrlTipoDepenenciaGetAll());

        tipoDependenciaSpinner = (Spinner) view.findViewById(R.id.tipoDependenciaSpinner);
        tipoDependenciaSpinner.setOnItemSelectedListener(tipoDependenciaOnItemClickListener());

        populateTipoDependenciaSpinner(null);

        unidadAcademicaLabel = (TextView) view.findViewById(R.id.unidadAcademicaLabel);
        unidadAcademicaSpinner = (Spinner) view.findViewById(R.id.unidadAcademicaSpinner);
        showUnidadAcademicaLabelSpinner(Boolean.FALSE);
        unidadAcademicaSpinner.setOnItemSelectedListener(unidadAcademicaOnItemClickListener());

        populateUndiadAcademicaSpinner(null);

        HttpAsyncTask auxiliarHttpAsyncTask = new HttpAsyncTask(1);
        auxiliarHttpAsyncTask.setHttpAsyncTaskInterface(this);
        auxiliarHttpAsyncTask.execute(UrlBuilder.getUrlUnidadAcademicaGetAll());

        puntoInteresSpinner = (Spinner) view.findViewById(R.id.puntoInteresSpinner);
        puntoInteresSpinner.setOnItemSelectedListener(puntoInteresOnItemClickListener());
        showPuntoInteresSpinner(Boolean.FALSE);

        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setVisibility(View.GONE);

        searchButton.setOnClickListener(searchButtonOnClickListener());

        return view;
    }

    @Override
    public void populateUndiadAcademicaSpinner(String result) {
        /*String unidadAcademicaJSON = "{\"response\":[\n" +
                "      {\"id\":1,\"nombre\":\"FICH\"},\n" +
                "      {\"id\":2,\"nombre\":\"FCBC\"},\n" +
                "      {\"id\":3,\"nombre\":\"FCM\"},\n" +
                "      {\"id\":4,\"nombre\":\"FADU\"},\n" +
                "      {\"id\":5,\"nombre\":\"FHUC\"},\n" +
                "      {\"id\":6,\"nombre\":\"ESS\"},\n" +
                "      {\"id\":7,\"nombre\":\"Aulario\"}\n" +
                "  ]\n" +
                "}\n";*/
        String unidadAcademicaJSON = result;
        if (unidadAcademicaJSON != null && !unidadAcademicaJSON.isEmpty()) {
            ArrayAdapter unidadAcademicaSpinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_items_layout,
                    JSONConverterUtils.JSONUnidadAcademicaConverter(unidadAcademicaJSON));
            unidadAcademicaSpinner.setAdapter(unidadAcademicaSpinnerArrayAdapter);
        }
    }

    @Override
    public void populateTipoDependenciaSpinner(String result) {
        String tipoDependenciaJSON = result;
        if (tipoDependenciaJSON != null && !tipoDependenciaJSON.isEmpty()) {
            ArrayAdapter tipoDependenciaArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_items_layout,
                    JSONConverterUtils.JSONTipoDependenciaConverter(tipoDependenciaJSON));
            tipoDependenciaSpinner.setAdapter(tipoDependenciaArrayAdapter);
        }
    }

    @Override
    public void populatePuntoInteresSpinner(String result) {
        String puntoInteresJSON = result;
        if (puntoInteresJSON != null && !puntoInteresJSON.isEmpty()) {
            ArrayAdapter puntoInteresArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_items_layout,
                    JSONConverterUtils.JSONPuntoInteresConverter(puntoInteresJSON));
            puntoInteresSpinner.setAdapter(puntoInteresArrayAdapter);
        }
    }

    @Override
    public void getPath(String result) {
        mainActivity.showSearchResultOnMap(JSONConverterUtils.JSONPuntoConverter(result), Boolean.TRUE);
    }

    @Override
    public void getPoints(String result) {
        mainActivity.showSearchResultOnMap(JSONConverterUtils.JSONPuntoConverter(result), Boolean.FALSE);
    }

    private void showUnidadAcademicaLabelSpinner(Boolean visible) {
        if (visible) {
            unidadAcademicaLabel.setVisibility(View.VISIBLE);
            unidadAcademicaSpinner.setVisibility(View.VISIBLE);
        } else {
            unidadAcademicaLabel.setVisibility(View.GONE);
            unidadAcademicaSpinner.setVisibility(View.GONE);
        }
    }

    private void showPuntoInteresSpinner(Boolean visible) {
        if (visible) {
            puntoInteresSpinner.setVisibility(View.VISIBLE);
        } else {
            puntoInteresSpinner.setVisibility(View.GONE);
        }
    }

    private void showErrorToast() {
        Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.fragment_search_error_toast_message), Toast.LENGTH_LONG).show();
    }

    private AdapterView.OnItemSelectedListener tipoDependenciaOnItemClickListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetAll();
                if (!tipoDependenciaSpinner.getSelectedItem().toString().equals(JSONConverterUtils.SPINNER_PROMPT)) {
                    if (ArrayUtils.contains(TIPO_DEPENDENCIAS_SHOW_UNIDAD_ACADEMICA_SPINNER, ((TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem()).getId())) {
                        showUnidadAcademicaLabelSpinner(Boolean.TRUE);
                    } else if (((TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem()).getId().equals(12)) {
                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(2);
                        httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                        TipoDependenciaDto tipoDependenciaDto = (TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem();
                        httpAsyncTask.execute(UrlBuilder.getUrlDependenciaPorTipo(tipoDependenciaDto.getId()));
                        showPuntoInteresSpinner(Boolean.TRUE);
                    } else {
                        showUnidadAcademicaLabelSpinner(Boolean.FALSE);
                        showPuntoInteresSpinner(Boolean.FALSE);
                        searchButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    showUnidadAcademicaLabelSpinner(Boolean.FALSE);
                    showPuntoInteresSpinner(Boolean.FALSE);
                    searchButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener unidadAcademicaOnItemClickListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!unidadAcademicaSpinner.getSelectedItem().toString().equals(JSONConverterUtils.SPINNER_PROMPT)) {
                    if (ArrayUtils.contains(TIPO_DEPENDENCIAS_SHOW_PUNTO_INTERES_SPINNER, ((TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem()).getId())) {
                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(2);
                        httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                        TipoDependenciaDto tipoDependenciaDto = (TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem();
                        UnidadAcademicaDto unidadAcademicaDto = (UnidadAcademicaDto) unidadAcademicaSpinner.getSelectedItem();
                        httpAsyncTask.execute(UrlBuilder.getUrlDependenciaPorTipoUnidad(unidadAcademicaDto.getNombre(), tipoDependenciaDto.getDescripcion()));
                        showPuntoInteresSpinner(Boolean.TRUE);
                    } else {
                        searchButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    showPuntoInteresSpinner(Boolean.FALSE);
                    searchButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener puntoInteresOnItemClickListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!puntoInteresSpinner.getSelectedItem().toString().equals(JSONConverterUtils.SPINNER_PROMPT)) {
                    searchButton.setVisibility(View.VISIBLE);
                } else {
                    searchButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private View.OnClickListener searchButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeSearch(null, null, null, false, mainActivity);
               /* String insert = null;
                TipoDependenciaDto tipoDependenciaDto = (TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem();
                UnidadAcademicaDto unidadAcademicaDto = null;
                if(unidadAcademicaSpinner.getVisibility() == View.VISIBLE){
                    unidadAcademicaDto = (UnidadAcademicaDto) unidadAcademicaSpinner.getSelectedItem();
                }
                PuntoInteresDto puntoInteresDto = null;
                if(puntoInteresSpinner.getVisibility() == View.VISIBLE){
                    puntoInteresDto = (PuntoInteresDto) puntoInteresSpinner.getSelectedItem();
                }
                if (unidadAcademicaSpinner.getVisibility() == View.VISIBLE && puntoInteresSpinner.getVisibility() == View.VISIBLE) {
                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask(3);
                    httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                    httpAsyncTask.execute(UrlBuilder.getUrlGetCamino(mainActivity.getMapsFragment().getLatitude(), mainActivity.getMapsFragment().getLongitude(),
                            mainActivity.getMapsFragment().getActualFloor(), puntoInteresDto.getId()));
                    insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_unidad, c_unidad, d_punto, c_punto) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                            ", '" + unidadAcademicaDto.getNombre() + "', " + unidadAcademicaDto.getId() + ", '" + puntoInteresDto.getNombre() + "', " + puntoInteresDto.getId() + ")");
                } else if (unidadAcademicaSpinner.getVisibility() == View.VISIBLE && puntoInteresSpinner.getVisibility() == View.GONE) {
                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask(5);
                    httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                    httpAsyncTask.execute(UrlBuilder.getUrlGetEdificio(tipoDependenciaDto.getId(), unidadAcademicaDto.getId()));
                    insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_unidad, c_unidad) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                            ", '" + unidadAcademicaDto.getNombre() + "', " + unidadAcademicaDto.getId() +")");
                } else if (unidadAcademicaSpinner.getVisibility() == View.GONE && puntoInteresSpinner.getVisibility() == View.VISIBLE) {
                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask(3);
                    httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                    httpAsyncTask.execute(UrlBuilder.getUrlGetCamino(mainActivity.getMapsFragment().getLatitude(), mainActivity.getMapsFragment().getLongitude(),
                            mainActivity.getMapsFragment().getActualFloor(), puntoInteresDto.getId()));
                    insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_punto, c_punto) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                             ", '" + puntoInteresDto.getNombre() + "', " + puntoInteresDto.getId() + ")");
                } else if (unidadAcademicaSpinner.getVisibility() == View.GONE && puntoInteresSpinner.getVisibility() == View.GONE) {
                    HttpAsyncTask httpAsyncTask = new HttpAsyncTask(4);
                    httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
                    httpAsyncTask.execute(UrlBuilder.getUrlGetAllByTipoDependenia(tipoDependenciaDto.getId()));
                    insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() + ")");
                }
                SQLiteDatabase db = sqLiteDataBase.getWritableDatabase();
                db.execSQL(insert);
                db.close();*/
            }
        };
    }

    public void executeSearch(Integer idTipoDependencia, Integer idUnidadAcademica, Integer idPunto, Boolean isLastSearch, MainActivity mainActivity){
        String insert = null;
        TipoDependenciaDto tipoDependenciaDto = new TipoDependenciaDto();
        UnidadAcademicaDto unidadAcademicaDto = null;
        PuntoInteresDto puntoInteresDto = null;
        if(isLastSearch){
            this.mainActivity = mainActivity;
            tipoDependenciaDto.setId(idTipoDependencia);
            if(idUnidadAcademica != null){
                unidadAcademicaDto = new UnidadAcademicaDto();
                unidadAcademicaDto.setId(idUnidadAcademica);
            }
            if(idPunto != null){
                puntoInteresDto = new PuntoInteresDto();
                puntoInteresDto.setId(idPunto);
            }
        }
        else{
            tipoDependenciaDto = (TipoDependenciaDto) tipoDependenciaSpinner.getSelectedItem();
            if(unidadAcademicaSpinner.getVisibility() == View.VISIBLE){
                unidadAcademicaDto = (UnidadAcademicaDto) unidadAcademicaSpinner.getSelectedItem();
            }
            if(puntoInteresSpinner.getVisibility() == View.VISIBLE){
                puntoInteresDto = (PuntoInteresDto) puntoInteresSpinner.getSelectedItem();
            }
        }
        if (unidadAcademicaDto != null && puntoInteresDto != null) {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(3);
            httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
            httpAsyncTask.execute(UrlBuilder.getUrlGetCamino(mainActivity.getMapsFragment().getLatitude(), mainActivity.getMapsFragment().getLongitude(),
                    mainActivity.getMapsFragment().getActualFloor(), puntoInteresDto.getId()));
            insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_unidad, c_unidad, d_punto, c_punto) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                    ", '" + unidadAcademicaDto.getNombre() + "', " + unidadAcademicaDto.getId() + ", '" + puntoInteresDto.getNombre() + "', " + puntoInteresDto.getId() + ")");
        } else if (unidadAcademicaDto != null && puntoInteresDto == null) {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(5);
            httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
            httpAsyncTask.execute(UrlBuilder.getUrlGetEdificio(tipoDependenciaDto.getId(), unidadAcademicaDto.getId()));
            insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_unidad, c_unidad) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                    ", '" + unidadAcademicaDto.getNombre() + "', " + unidadAcademicaDto.getId() +")");
        } else if (unidadAcademicaDto == null && puntoInteresDto != null) {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(3);
            httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
            httpAsyncTask.execute(UrlBuilder.getUrlGetCamino(mainActivity.getMapsFragment().getLatitude(), mainActivity.getMapsFragment().getLongitude(),
                    mainActivity.getMapsFragment().getActualFloor(), puntoInteresDto.getId()));
            insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo, d_punto, c_punto) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() +
                    ", '" + puntoInteresDto.getNombre() + "', " + puntoInteresDto.getId() + ")");
        } else if (unidadAcademicaDto == null && puntoInteresDto == null) {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(4);
            httpAsyncTask.setHttpAsyncTaskInterface(SearchFragment.this);
            httpAsyncTask.execute(UrlBuilder.getUrlGetAllByTipoDependenia(tipoDependenciaDto.getId()));
            insert = new String("INSERT INTO Busquedas (d_tipo, c_tipo) VALUES ('" + tipoDependenciaDto.getDescripcion() + "', " + tipoDependenciaDto.getId() + ")");
        }
        if(!isLastSearch){
            SQLiteDatabase db = sqLiteDataBase.getWritableDatabase();
            db.execSQL(insert);
            db.close();
        }
    }

    private void resetAll() {
        unidadAcademicaSpinner.setSelection(0);
        showUnidadAcademicaLabelSpinner(Boolean.FALSE);
        showPuntoInteresSpinner(Boolean.FALSE);
        searchButton.setVisibility(View.GONE);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
