package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.PathJSONParser;


@EActivity(R.layout.activity_main2)
public class MapaActivity extends Activity implements GoogleMap.OnMarkerClickListener {

    private Location location;

    private OrdemServicoService ordemServicoService = new OrdemServicoServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    InternetUtil internetUtil = new InternetUtil(this);

    private GoogleMap mapa;

    ArrayList<LatLng> markerPoints, markerNear;

    @ViewById
    Button btn_draw;

    @ViewById
    Button btn_navegar;


    private Map<String, Ponto> markerIdPontoMap = new HashMap<String, Ponto>();

    @AfterViews
    void inicializar() {

        markerPoints = new ArrayList<LatLng>();
        markerNear = new ArrayList<LatLng>();

        montarMapa();

        if (mapa != null) {
            setarPontosNoMapa();
            // Enable MyLocation Button in the Map
            mapa.setMyLocationEnabled(true);

            btn_draw.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (internetUtil.isInternetAtiva()) {
                        gerarRotas();
                    } else {
                        toastVerificaInternet();
                    }

                }
            });

            btn_navegar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (internetUtil.isInternetAtiva()) {
                        navegar();
                    } else {
                        toastVerificaInternet2();
                    }
                }
            });

        }
    }


    private void gerarRotas() {
        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);
            btn_draw.setBackgroundColor(Color.BLUE);

            toastGerandoRota();

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        } else {
            toastMinimo();
        }


    }

    private void toastMinimo() {
        Toast.makeText(this, "É necessário ao menos 2 (dois) pontos no mapa para gerar uma rota.", Toast.LENGTH_LONG).show();
    }

    private void toastGerandoRota() {
        Toast.makeText(this, "Gerando Rota...", Toast.LENGTH_LONG).show();
    }

    private void toastVerificaInternet() {
        Toast.makeText(this, "É necessário conexão com a internet para gerar as rotas!", Toast.LENGTH_LONG).show();
    }

    private void toastVerificaInternet2() {
        Toast.makeText(this, "É necessário conexão com a internet para a navegação!", Toast.LENGTH_LONG).show();
    }

    private void toastConectividade(){
        Toast.makeText(this, "Rota não obtida. Verifique sua conectividade com a internet!", Toast.LENGTH_LONG).show();
        btn_draw.setBackgroundColor(Color.WHITE);
    }

    private void montarMapa() {
        // Captura as coordenadas para dar Zomm no mapa na localização capturada.
        GPSUtil gpsUtil = new GPSUtil(this);
        location = gpsUtil.capturarCoordenadasGPS();

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SUCCESS:
                mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa2)).getMap();
                if (mapa != null) {
                    mapa.setOnMarkerClickListener(this);
                    break;
                }

            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(this, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                return;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(this, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                return;
            default:
                Toast.makeText(this, GooglePlayServicesUtil.isGooglePlayServicesAvailable(this), Toast.LENGTH_SHORT).show();
        }

        if (location == null) {
            AlertaUtil.mostrarMensagem(this, "GPS habilitado, mas não foi possível obter a localização.\n");
            finish();
        } else {
            adicionarMarkerPosicaoUsuario();
        }

        if (!gpsUtil.isGPSAtivo()) {
            gpsUtil.mostrarMensagemSolicitandoAtivacaoGPS(
                    "Para abrir os Pontos no Mapa é necessário que o GPS esteja habilitado.\nDeseja habilitar o GPS?",
                    this, true);
        }

    }


    private void adicionarMarkerPosicaoUsuario() {
        Marker marker = mapa.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.person));
    }

    void setarPontosNoMapa() {
        mapa.clear();
        adicionarMarkerPosicaoUsuario();

        List<OrdemServicoPonto> pontosAssociados = ordemServicoPontoService.listar("id", true);

        if (pontosAssociados == null || pontosAssociados.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhum ponto encontrado!");
            voltarTelaAnterior();
        }
        int c = 0;

        for (OrdemServicoPonto pontoAssociado : pontosAssociados) {
            if (pontoAssociado != null && pontoAssociado.getPonto() != null) {

                Ponto ponto = pontoAssociado.getPonto();


                if (ponto != null && ponto.getId() != null && ponto.getLatitude() != null && ponto.getLongitude() != null) {

                    LatLng coordenadas = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                    Marker marker = mapa.addMarker(new MarkerOptions().position(coordenadas));
                    markerIdPontoMap.put(marker.getId(), ponto);
                    markerPoints.add(c, coordenadas);

                    List<OrdemServicoPonto> ordensServicoPonto = ordemServicoPontoService.buscarPorPonto(ponto.getId());
                    if (ordensServicoPonto == null || ordensServicoPonto.isEmpty()) { // Ponto não associado a nehuma ordem de servico.
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    } else {
                        boolean existeAlgumaOsDoPontoEmAtendimento = false;
                        for (OrdemServicoPonto ordemServicoPonto : ordensServicoPonto) {
                            if (ordemServicoPonto.getOs() != null) {
                                OrdemServico os = ordemServicoService.buscarPorId(ordemServicoPonto.getOs().getId());
                                if (os.getSituacao() == null || os.getSituacao().getId().equals(Situacao.SITUACAO_EM_CAMPO.toString())) {
                                    existeAlgumaOsDoPontoEmAtendimento = true;
                                    break;
                                }
                            }
                        }
                        if (existeAlgumaOsDoPontoEmAtendimento) { // Ponto não associado a nenhuma ordem de servico em atendimento (em campo).
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        } else { // Ponto não associado a ordens de servico apenas atribuídas.
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    }
                }
            }
            c++;
        }

        adicionarRotaProximos();
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
        mapa.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


    private void adicionarRotaProximos() {

        double lng = location.getLongitude();
        double lat = location.getLatitude();

        Map<Integer, Double> hDist = new HashMap<Integer, Double>();

        for (LatLng l : markerPoints) {

            if (l.latitude != 0) {

                double mlat = l.latitude;
                double mlng = l.longitude;
                double dLat = Math.toRadians(mlat - lat);
                double dLong = Math.toRadians(mlng - lng);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(lat)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double d = a * c;

                hDist.put(markerPoints.indexOf(l), d);

            }
        }
        Map<Integer, Double> sortedMap = sortByComparator(hDist);
        Set<Integer> chaves = sortedMap.keySet();
        int x = 0;
        for (Integer chave : chaves) {
            if (x < 9) {

                markerNear.add(x, markerPoints.get(chave));
                if (x == 0) {
                    Marker marker = mapa.addMarker(new MarkerOptions().position(markerPoints.get(chave)));
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    Marker marker = mapa.addMarker(new MarkerOptions().position(markerPoints.get(chave)));
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                x++;
            } else {
                break;
            }
        }
    }


    private static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap) {

        // Convert Map to List
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Double> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for (int i = 2; i < markerNear.size(); i++) {
            LatLng point = (LatLng) markerNear.get(i);
            if (i == 2) {
                waypoints = "waypoints=";
                waypoints += location.getLatitude() + "," + location.getLongitude() + "|";
            }

            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Ponto ponto = markerIdPontoMap.get(marker.getId());
        if (ponto != null) {
            StringBuilder msg = new StringBuilder("Abrindo o Ponto: ");
            msg.append("\nNº Plaqueta: ").append((ponto.getNumPlaquetaTransf() == null || ponto.getNumPlaquetaTransf().isEmpty()) ? "---" : ponto.getNumPlaquetaTransf());
            msg.append("\nEndereço: ").append((ponto.getEnderecoFormatado() == null || ponto.getEnderecoFormatado().isEmpty()) ? "---" : ponto.getEnderecoFormatado());
            AlertaUtil.mostrarMensagem(this, msg.toString());

            abrirPonto(ponto);
        }
        return true;
    }

    @UiThread
    void abrirPonto(Ponto ponto) {
        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        intent.putExtra("cadastroAcessadoAPartirDoMapa", true);
        startActivity(intent);
        finish();
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            if(result != null && result.length() > 4 ){
                parserTask.execute(result);
            }else{
                toastConectividade();
            }


        }
    }



    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            if (result != null && result.size() > 0) {
                // Traversing through all the routes
                for (int i = 0; i == 0; i++) {
               // for (int i = 0; i < result.size(); i++) {

                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.RED);
                }

                // Drawing polyline in the Google Map for the i-th route
                mapa.addPolyline(lineOptions);
                toastRotaGerada();

            }
        }
    }


    private void toastRotaGerada() {
        Toast.makeText(this, "Rota gerada com sucesso!", Toast.LENGTH_LONG).show();
        btn_draw.setBackgroundColor(Color.WHITE);
    }

    private void navegar() {

        GPSUtil gpsUtil = new GPSUtil(this);
        InternetUtil internetUtil = new InternetUtil(this);


        LatLng point = (LatLng) markerNear.get(0);
        Location location = gpsUtil.capturarCoordenadasGPS();

        if (gpsUtil.isGPSAtivo() && internetUtil.isInternetAtiva()) {
            if (location == null) {
                AlertaUtil.mostrarMensagem(this, "GPS habilitado, mas não foi possível obter a localização.\n");
                finish();
            } else {
                AlertaUtil.mostrarMensagem(this, "Traçando rota...");
                StringBuilder url = new StringBuilder("http://maps.google.com/maps?saddr=");
                url.append(location.getLatitude()).append(",").append(location.getLongitude());
                url.append("&daddr=").append(point.latitude).append(",").append(point.longitude);

                Intent navigation = new Intent(Intent.ACTION_VIEW);
                navigation.setData(Uri.parse(url.toString()));
                startActivity(navigation);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void voltarTelaAnterior() {
        finish();
    }

}