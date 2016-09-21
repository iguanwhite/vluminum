package br.com.velp.vluminum.activity;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.entity.UsuarioCliente;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.service.PontoService;
import br.com.velp.vluminum.service.UsuarioClienteService;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.UsuarioClienteServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.VLuminumUtil;

@EActivity(R.layout.mapa_ponto_activity)
public class MapaPontoActivity extends ActivityBase implements GoogleMap.OnMarkerClickListener, SeekBar.OnSeekBarChangeListener {

    @ViewById
    TextView textView3;

    @ViewById
    SeekBar seekBar;

    private Integer value = 500;

    private List<Ponto> pontos;

    private Municipio municipio;

    private OrdemServico ordemServico;

    private Boolean associarPontoOs;

    private Location location;

    private OrdemServicoService ordemServicoService = new OrdemServicoServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private UsuarioClienteService usuarioClienteService = new UsuarioClienteServiceImpl(this);

    private PontoService pontoService = new PontoServiceImpl(this);

    private GoogleMap mapa;

    private Map<String, Ponto> markerIdPontoMap = new HashMap<String, Ponto>();

    @AfterViews
    void inicializar() {
        iniciarSeekBar();
        recuperarParametrosConsulta();
        coletarPontos();
        montarMapa();
    }

    private void iniciarSeekBar() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(value);
    }

    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();
        municipio = (Municipio) intent.getSerializableExtra(Municipio.class.getName());
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        associarPontoOs = (Boolean) intent.getSerializableExtra("associarPontoOs");
    }

    private void coletarPontos() {

        pontos = new ArrayList<Ponto>();

        PontoParametroConsulta pc = new PontoParametroConsulta();
        pc.setMunicipio(municipio);

        Usuario us = VLuminumUtil.getUsuarioLogado();
        List<UsuarioCliente> usuarioClientes = usuarioClienteService.listarPorUsuario(us.getId());
        List<Ponto> todosPontos = pontoService.buscarPorParametroConsulta(pc); // Todos os pontos do municipio escolhido;


        //Ve em todos os pontos apenas aqueles que possuem os clientes obtidos do Usuario.
        //OBS. Implementacao de teste. (Sabemos que não é a forma mais otimizada dessa operaçao.)
        for (Ponto p : todosPontos) {
            for (UsuarioCliente u : usuarioClientes) {
                if (p.getIdCliente() == u.getIdCliente()) {
                    pontos.add(p);
                }
            }
        }


    }


    //method for when the progress bar is changed
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        value = progress;
        textView3.setText("Raio (metros):" + value);

    }

    //method for when the progress bar is first touched
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //method for when the progress bar is released
    public void onStopTrackingTouch(SeekBar seekBar) {
        setarPontosNoMapa();
    }


    private void montarMapa() {
        // Captura as coordenadas para dar Zomm no mapa na localização capturada.
        GPSUtil gpsUtil = new GPSUtil(this);
        location = gpsUtil.capturarCoordenadasGPS();

        InternetUtil internetUtil = new InternetUtil(this);

        if (gpsUtil.isGPSAtivo() && internetUtil.isInternetAtiva()) {
            //     mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();

            switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
                case ConnectionResult.SUCCESS:
                    mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
                    // Gets to GoogleMap from the MapView and does initialization stuff
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
                setarPontosNoMapa();
            }
        } else {
            if (!gpsUtil.isGPSAtivo()) {
                gpsUtil.mostrarMensagemSolicitandoAtivacaoGPS(
                        "Para abrir os Pontos no Mapa é necessário que o GPS esteja habilitado.\nDeseja habilitar o GPS?",
                        this, true);
            }

            if (!internetUtil.isInternetAtiva()) {
                internetUtil.mostrarMensagemSolicitandoAtivacaoInternet(
                        "Para abrir os Pontos no Mapa é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?",
                        this, true);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Ponto ponto = markerIdPontoMap.get(marker.getId());
        if (ponto != null) {

            if (associarPontoOs) {


                associarPontoAOrdemServico(ponto);

            } else {


                //    btnVisualizar btnCriar btnRotas

                final Dialog dialog = new Dialog(MapaPontoActivity.this);
                dialog.setContentView(R.layout.customalert);
                dialog.setTitle("Ponto");

                Button btnVisualizar = (Button) dialog.findViewById(R.id.btnVisualizar);
                btnVisualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirPonto(ponto);
                        dialog.cancel();
                    }
                });

                Button btnCriar = (Button) dialog.findViewById(R.id.btnCriar);
                btnCriar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirListaOs(ponto);
                        dialog.cancel();
                    }
                });

                Button btnRotas = (Button) dialog.findViewById(R.id.btnRotas);
                btnRotas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tracarRota(ponto);
                        dialog.cancel();
                    }
                });

                dialog.show();

            }


        }
        return true;
    }


    private void associarPontoAOrdemServico(Ponto ponto) {
        OrdemServicoPonto ordemServicoPonto = new OrdemServicoPonto();
        ordemServicoPonto.setPonto(ponto);
        ordemServicoPonto.setOs(ordemServico);
        try {
            if (osJaAssociadaAoPonto(ordemServicoPonto.getOs().getId(), ponto.getId())) {
                AlertaUtil.mostrarMensagem(this, "Ponto selecionado já associado à OS!");
                return;
            } else {
                ordemServicoPonto.setSincronizado(0);
                ordemServicoPontoService.salvarOuAtualizar(ordemServicoPonto);
            }
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(ListaPontoOsActivity.class.getName(), "Erro ao associar ponto à os", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        AlertaUtil.mostrarMensagem(this, "Ponto associado à OS com sucesso!");

        Intent intent = new Intent(this, ListaPontoOsActivity_.class);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
    }


    private boolean osJaAssociadaAoPonto(String idOs, String idPonto) {
        List<OrdemServicoPonto> lista = ordemServicoPontoService.listarPorOs(idOs);
        List<Ponto> pontos = new ArrayList<Ponto>();
        for (OrdemServicoPonto ordemServicoPonto : lista) {
            pontos.add(ordemServicoPonto.getPonto());
        }
        boolean contem = false;
        for (Ponto p : pontos) {
            if (p.getId().equals(idPonto)) {
                contem = true;
                break;
            }
        }

        return contem;
    }


    @UiThread
    void abrirPonto(Ponto ponto) {

        StringBuilder msg = new StringBuilder("Abrindo o Ponto: ");
        msg.append("\nNº Plaqueta: ").append((ponto.getNumPlaquetaTransf() == null || ponto.getNumPlaquetaTransf().isEmpty()) ? "---" : ponto.getNumPlaquetaTransf());
        msg.append("\nEndereço: ").append((ponto.getEnderecoFormatado() == null || ponto.getEnderecoFormatado().isEmpty()) ? "---" : ponto.getEnderecoFormatado());
        AlertaUtil.mostrarMensagem(this, msg.toString());

        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        intent.putExtra("cadastroMapaPonto", true);
        startActivity(intent);
        finish();
    }

    @UiThread
    void abrirListaOs(Ponto ponto) {

        StringBuilder msg = new StringBuilder("Ponto a ser incluído em O.S: ");
        msg.append("\nNº Plaqueta: ").append((ponto.getNumPlaquetaTransf() == null || ponto.getNumPlaquetaTransf().isEmpty()) ? "---" : ponto.getNumPlaquetaTransf());
        msg.append("\nEndereço: ").append((ponto.getEnderecoFormatado() == null || ponto.getEnderecoFormatado().isEmpty()) ? "---" : ponto.getEnderecoFormatado());
        AlertaUtil.mostrarMensagem(this, msg.toString());

        Intent intent = new Intent(this, ListaOsPontoActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        startActivity(intent);
        finish();
    }

    private void adicionarMarkerPosicaoUsuario() {
        Marker marker = mapa.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.person));
    }

    void setarPontosNoMapa() {
        mapa.clear();
        adicionarMarkerPosicaoUsuario(); //TODO. Voltar codigo.


        if (pontos == null || pontos.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhum ponto encontrado!");
            voltarTelaAnterior();
        }

        for (Ponto ponto : pontos) {

            if (ponto != null && ponto.getId() != null && ponto.getLatitude() != null && ponto.getLongitude() != null) {

                if (validarRaioAtual(ponto.getLatitude(), ponto.getLongitude())) { //TODO. Voltar codigo.
                    LatLng coordenadas = new LatLng(ponto.getLatitude(), ponto.getLongitude());
                    Marker marker = mapa.addMarker(new MarkerOptions().position(coordenadas));
                    markerIdPontoMap.put(marker.getId(), ponto);


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
            } //TODO. Voltar codigo.
        }


        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
        mapa.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


    private void tracarRota(Ponto ponto) {
        GPSUtil gpsUtil = new GPSUtil(this);
        InternetUtil internetUtil = new InternetUtil(this);

        Location location = gpsUtil.capturarCoordenadasGPS();

        if (gpsUtil.isGPSAtivo() && internetUtil.isInternetAtiva()) {
            if (location == null) {
                AlertaUtil.mostrarMensagem(this, "GPS habilitado, mas não foi possível obter a localização.\n");
                finish();
            } else {
                AlertaUtil.mostrarMensagem(this, "Traçando rota...");
                StringBuilder url = new StringBuilder("http://maps.google.com/maps?saddr=");
                url.append(location.getLatitude()).append(",").append(location.getLongitude());
                url.append("&daddr=").append(ponto.getLatitude()).append(",").append(ponto.getLongitude());

                Intent navigation = new Intent(Intent.ACTION_VIEW);
                navigation.setData(Uri.parse(url.toString()));
                startActivity(navigation);
            }
        }
    }


    private boolean validarRaioAtual(Double lat, Double lon) {

        if (lat != null && lon != null) {
            Location ponto = new Location(location);
            Double radius = value.doubleValue();

            ponto.setLatitude(lat);
            ponto.setLongitude(lon);

            float distance = location.distanceTo(ponto);
            if (distance < radius) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }


    }


    private void voltarTelaAnterior() {
        finish();
    }

}