package com.encuestando.salmeron.facundo.encuestandofcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InfoNoticiasNormalActivity extends AppCompatActivity {

    private ListView listViewInfoNoticias;
    private List<InfoNoticiaDto> infoNoticiasDto = new ArrayList<InfoNoticiaDto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_noticias_normal_activity);
        listViewInfoNoticias = (ListView) findViewById(R.id.listaInfoNoticias);

        HashMap<String, String>  mapaInfoNoticias = new HashMap<>();
        mapaInfoNoticias.put("Primer Noticia", "Primera descripcion de noticia");
        mapaInfoNoticias.put("Segunda Noticia", "Descripcion segunda noticia, descripcion segunda noticia, descripcion segunda noticia");

        List<HashMap<String, String>> listaMapas = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listaMapas, R.layout.info_noticias_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.titulo_info_normal, R.id.subtitulo_info_normal});

        Iterator it = mapaInfoNoticias.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listaMapas.add(resultMap);
        }

        listViewInfoNoticias.setAdapter(adapter);
    }
}
