package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.enumerator.Danificado;
import br.com.velp.vluminum.enumerator.Direcao;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.enumerator.TipoPonto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.MunicipioClienteService;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.PontoService;
import br.com.velp.vluminum.service.PosteService;
import br.com.velp.vluminum.serviceimpl.MunicipioClienteServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PosteServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.GeocodificacaoUtil;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.SpinnerUtil;

@EActivity(R.layout.cadastro_ponto_activity)
public class CadastroPontoActivity extends ActivityBase {

    private static final String SALVAR = "Salvar";
    private static final String EDITAR = "Editar";
    private static final String ATUALIZAR = "Atualizar";

    @ViewById
    Spinner tipoPontoSpinner;

    @ViewById
    Spinner posteSpinner;

    @ViewById
    Spinner danificadoSpinner;

    @ViewById
    Spinner direcaoSpinner;

    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText numeroLogradouroEditText;

    @ViewById
    EditText bairroEditText;

    @ViewById
    Spinner estadoSpinner;

    @ViewById
    Spinner municipioSpinner;

    @ViewById
    EditText cepEditText;

    @ViewById
    EditText pontoReferenciaEditText;

    @ViewById
    EditText numPlaquetaTransformadorEditText;

    @ViewById
    Button salvarPontoBtn;

    @ViewById
    Button cadastrarMaterialBtn;

    @ViewById
    Button cadastrarFotoBtn;

    @ViewById
    Button questionarioBtn;

    @ViewById
    Button tracarRotaBtn;

    @ViewById
    Button ordensServicoBtn;

    @ViewById
    Button pesquisarEnderecoBtn;

    @ViewById
    LinearLayout containerPrincipal;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    private PosteService posteService = new PosteServiceImpl(this);

    private PontoService pontoService = new PontoServiceImpl(this);

    private MunicipioClienteService municipioClienteService = new MunicipioClienteServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private Location location;

    private Ponto ponto;

    private OrdemServico ordemServico;

    private Boolean cadastroAcessadoAPartirDoMapa, cadastroMapaPonto;

    private boolean loading = false;

    private boolean newer = false;

    @AfterViews
    void inicializar() {
        cadastroMapaPonto = false;
        carregarSpinners();
        inicializarPonto();
    }

    @Override
    public void onBackPressed() {
        finish();

        if (!cadastroMapaPonto) {
            if (cadastroAcessadoAPartirDoMapa != null && cadastroAcessadoAPartirDoMapa) {
                Intent intent = new Intent(this, MapaActivity_.class);
                startActivity(intent);
            } else {
                if (ordemServico == null) {
                    Intent intent = new Intent(this, PesquisaPontoActivity_.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ListaPontoOsActivity_.class);
                    intent.putExtra(OrdemServico.class.getName(), ordemServico);
                    startActivity(intent);
                }
            }
        }


    }

    private void inicializarPonto() {
        if (!newer) {
            recuperarParametros();
        }

        if (ponto == null || ponto.getId().isEmpty()) {
            ponto = new Ponto();
            habilitarBotoes(false);
        } else {
            habilitarModoEdicao(true);
            habilitarBotoes(true);
        }
    }

    private void habilitarModoEdicao(boolean opcao) {
        if (opcao) {
            loading = true;
            carregarDadosPonto();
            habilitarComponentes(false);
            pesquisarEnderecoBtn.setEnabled(false);
            salvarPontoBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
            salvarPontoBtn.setText(EDITAR);

        } else {
            loading = false;
            habilitarComponentes(true);
            pesquisarEnderecoBtn.setEnabled(true);
            salvarPontoBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
            salvarPontoBtn.setText(ATUALIZAR);
        }
    }

    private void habilitarBotoes(boolean opcao) {
        cadastrarMaterialBtn.setEnabled(opcao);
        cadastrarFotoBtn.setEnabled(opcao);
        questionarioBtn.setEnabled(opcao);
        tracarRotaBtn.setEnabled(opcao);
        ordensServicoBtn.setEnabled(opcao);
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        cadastroAcessadoAPartirDoMapa = (Boolean) intent.getSerializableExtra("cadastroAcessadoAPartirDoMapa");

        cadastroMapaPonto = (Boolean) intent.getSerializableExtra("cadastroMapaPonto");
        if (cadastroMapaPonto == null) {
            cadastroMapaPonto = false;
        }
    }

    void carregarSpinners() {
        List<Poste> postes = posteService.listar("descricao", true);
        List<Poste> postesComOpcaoSelecione = new LinkedList<Poste>();
        postesComOpcaoSelecione.add(new Poste(null, "-- SELECIONE --"));
        postesComOpcaoSelecione.addAll(postes);
        posteSpinner.setAdapter(new ArrayAdapter<Poste>(this, android.R.layout.simple_spinner_item, postesComOpcaoSelecione));

        danificadoSpinner.setAdapter(new ArrayAdapter<Danificado>(this, android.R.layout.simple_spinner_item, Danificado.values()));

        direcaoSpinner.setAdapter(new ArrayAdapter<Direcao>(this, android.R.layout.simple_spinner_item, Direcao.values()));

        List<String> estados = municipioService.listarEstados();
        estadoSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados));

        tipoPontoSpinner.setAdapter(new ArrayAdapter<TipoPonto>(this, android.R.layout.simple_spinner_item, TipoPonto.values()));
    }

    @ItemSelect(R.id.estadoSpinner)
    void carregarSpinnerMunicipio(boolean selected, int position) {
        String estadoSelecionado = (String) estadoSpinner.getSelectedItem();
        if (!loading && estadoSelecionado != null) {
            List<Municipio> municipios = municipioService.listarPorEstado(estadoSelecionado);
            List<Municipio> municipiosComOpcaoSelecione = new LinkedList<Municipio>();
            municipiosComOpcaoSelecione.add(new Municipio(null, "-- SELECIONE --", null));
            municipiosComOpcaoSelecione.addAll(municipios);

            municipioSpinner.setAdapter(new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipiosComOpcaoSelecione));
        }
    }

    @Click(R.id.pesquisarEnderecoBtn)
    void pesquisarEndereco() {
        GPSUtil gpsUtil = new GPSUtil(this);
        if (!gpsUtil.isGPSAtivo()) {
            gpsUtil.mostrarMensagemSolicitandoAtivacaoGPS("Não foi possível obter as Coordenadas do Endereço."
                    + "É necessário que o GPS esteja habilitado.\n"
                    + "Deseja habilitar o GPS?", this, false);
            return;
        }

        InternetUtil internetUtil = new InternetUtil(this);
        if (!internetUtil.isInternetAtiva()) {
            internetUtil.mostrarMensagemSolicitandoAtivacaoInternet("Para pesquisar o Endereço é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?", this, false);
            return;
        }

        location = gpsUtil.capturarCoordenadasGPS();
        if (location == null) {
            exibirMsgNaoFoiPossivelObterEndereco();
        } else {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            // Seta as coordenadas no ponto.
            ponto.setLatitude(latitude);
            ponto.setLongitude(longitude);

            // Obtêm o endereço
            GeocodificacaoUtil geocodificacaoUtil = new GeocodificacaoUtil(this);
            Address address = null;
            try {
                address = geocodificacaoUtil.getEndereco(latitude, longitude);
            } catch (IOException ex) {
                Log.e(CadastroPontoActivity.class.getName(), "Não foi possível obter o Endereco", ex);
                AlertaUtil.mensagemErro(this, ex.getMessage());
            }

            // Seta o endereço recuperado nos campos
            if (address == null) {
                exibirMsgNaoFoiPossivelObterEndereco();
            } else {
                logradouroEditText.setText(address.getThoroughfare() == null ? null : address.getThoroughfare().toUpperCase());
                numeroLogradouroEditText.setText(address.getSubThoroughfare() == null ? null : address.getSubThoroughfare().toUpperCase());
                bairroEditText.setText(address.getSubLocality() == null ? null : address.getSubLocality().toUpperCase());

                String nomeEstado = address.getAdminArea();
                Estado estado = Estado.getEstado(nomeEstado); // Retorna a sigla da UF baseado no nome da UF.
                if (estado != null) {
                    estadoSpinner.setSelection(SpinnerUtil.getIndice(estadoSpinner, estado.getSigla()));
                }
                String municipioNome = address.getLocality();
                if (municipioNome == null) {
                    municipioNome = address.getSubAdminArea();
                }
                int indice = 0;
                if (municipioNome != null) {
                    indice = SpinnerUtil.getIndice(municipioSpinner, municipioNome);
                    municipioSpinner.setSelection(indice);
                }
                cepEditText.setText(address.getPostalCode().replaceAll("\\D", ""));
            }
        }
    }

    private void exibirMsgNaoFoiPossivelObterEndereco() {
        AlertaUtil.mensagemInformacao(this, "Não foi possível obter o Endereço.");
    }

    @Click(R.id.salvarPontoBtn)
    void salvar() {
        if (salvarPontoBtn.getText().toString().equals(EDITAR)) {
            habilitarModoEdicao(false);
            return;
        }

        boolean result = false;

        prepararPontoParaInsercao();

        try {
            result = pontoService.salvarOuAtualizar(ponto);
        } catch (CampoObrigatorioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemAlerta(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroPosteActivity.class.getName(), "Erro ao salvar Ponto", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        if (result) {
            String msg = (salvarPontoBtn.getText().toString().equals(SALVAR)) ? "Ponto cadastrado com sucesso!" : "Ponto atualizado com sucesso!";
            AlertaUtil.mostrarMensagem(this, msg);

            if (ordemServico != null && !salvarPontoBtn.getText().toString().equals(ATUALIZAR)) {
                associarPontoAOrdemServico();
            }

            newer = true;

            inicializar();
        }
    }

    private void associarPontoAOrdemServico() {
        OrdemServicoPonto ordemServicoPonto = new OrdemServicoPonto();
        ordemServicoPonto.setPonto(ponto);
        ordemServicoPonto.setOs(ordemServico);
        ordemServicoPonto.setSincronizado(0);
        try {
            ordemServicoPontoService.salvarOuAtualizar(ordemServicoPonto);
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(ListaPontoOsActivity.class.getName(), "Erro ao associar ponto à os", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        AlertaUtil.mostrarMensagem(this, "Ponto criado e associado à OS com sucesso!");
    }

    @Click(R.id.cadastrarFotoBtn)
    void abrirTelaCadastroFotos() {
        if (ponto != null) {
            Intent intent = new Intent(this, ListaFotoActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
    }

    @Click(R.id.cadastrarMaterialBtn)
    void abrirTelaCadastroMaterial() {
        if (ponto != null) {
            Intent intent = new Intent(this, ListaMaterialActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
    }

    @Click(R.id.questionarioBtn)
    void abrirTelaQuestionario() {
        if (ponto != null) {
            Intent intent = new Intent(this, QuestionarioActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
    }

    @Click(R.id.tracarRotaBtn)
    void tracarRota() {
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

    @Click(R.id.ordensServicoBtn)
    void abrirTelaListaOrdensServico() {
        if (ponto != null) {
            Intent intent = new Intent(this, ListaOsActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
    }

    private void habilitarComponentes(boolean opcao) {
        for (int i = 0; i < containerPrincipal.getChildCount(); i++) {
            View componenteFilho = containerPrincipal.getChildAt(i);
            if (componenteFilho instanceof EditText || componenteFilho instanceof Spinner) {
                componenteFilho.setEnabled(opcao);
            }
        }
    }

    private void carregarDadosPonto() {
        ponto.setId(ponto.getId());

        if (ponto.getTipoPonto() != null) {
            tipoPontoSpinner.setSelection(SpinnerUtil.getIndice(tipoPontoSpinner, ponto.getTipoPonto().getDescricao()));
        }

        if (ponto.getMunicipio() != null) {
            Municipio municipio = municipioService.buscarPorId(ponto.getMunicipio().getId());
            estadoSpinner.setSelection(SpinnerUtil.getIndice(estadoSpinner, municipio.getUf()));

            List<Municipio> municipios = municipioService.listarPorEstado(municipio.getUf());
            municipioSpinner.setAdapter(new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipios));

            int indice = SpinnerUtil.getIndice(municipioSpinner, municipio.getNome());
            municipioSpinner.setSelection(indice);
        }

        if (ponto.getPoste() != null) {
            posteSpinner.setSelection(SpinnerUtil.getIndice(posteSpinner, ponto.getPoste().getDescricao()));
        }

        if (ponto.getDanificado() != null) {
            danificadoSpinner.setSelection(SpinnerUtil.getIndice(danificadoSpinner, ponto.getDanificado().getDescricao()));
        }

        if (ponto.getDirecao() != null) {
            direcaoSpinner.setSelection(SpinnerUtil.getIndice(direcaoSpinner, ponto.getDirecao().getDescricao()));
        }

        if (ponto.getLogradouro() != null) {
            logradouroEditText.setText(ponto.getLogradouro());
        }

        if (ponto.getNumLogradouro() != null) {
            numeroLogradouroEditText.setText(ponto.getNumLogradouro());
        }

        if (ponto.getBairro() != null) {
            bairroEditText.setText(ponto.getBairro());
        }

        if (ponto.getCep() != null) {
            cepEditText.setText(ponto.getCep());
        }

        if (ponto.getPontoReferencia() != null) {
            pontoReferenciaEditText.setText(ponto.getPontoReferencia());
        }

        if (ponto.getNumPlaquetaTransf() != null) {
            numPlaquetaTransformadorEditText.setText(ponto.getNumPlaquetaTransf());
        }
    }

    private void prepararPontoParaInsercao() {
        TipoPonto tipoPonto = (TipoPonto) tipoPontoSpinner.getSelectedItem();
        ponto.setTipoPonto(tipoPonto);

        Poste poste = (Poste) posteSpinner.getSelectedItem();
        ponto.setPoste(poste);

        Danificado danificado = (Danificado) danificadoSpinner.getSelectedItem();
        ponto.setDanificado(danificado);

        Direcao direcao = (Direcao) direcaoSpinner.getSelectedItem();
        ponto.setDirecao(direcao);

        String logradouro = (logradouroEditText.getText() != null) ? logradouroEditText.getText().toString() : null;
        ponto.setLogradouro(logradouro);

        String numLogradouro = (numeroLogradouroEditText.getText() != null) ? numeroLogradouroEditText.getText().toString() : null;
        ponto.setNumLogradouro(numLogradouro);

        String bairro = (bairroEditText.getText() != null) ? bairroEditText.getText().toString() : null;
        ponto.setBairro(bairro);

        Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
        ponto.setMunicipio(municipio);

        String cep = (cepEditText.getText() != null) ? cepEditText.getText().toString() : null;
        ponto.setCep(cep);

        String pontoReferencia = (pontoReferenciaEditText.getText() != null) ? pontoReferenciaEditText.getText().toString() : null;
        ponto.setPontoReferencia(pontoReferencia);

        String numPlaqueta = (numPlaquetaTransformadorEditText.getText() != null) ? numPlaquetaTransformadorEditText.getText().toString() : null;
        ponto.setNumPlaquetaTransf(numPlaqueta);

        ponto.setDataAlteracao(new Date());

        ponto.setIdCliente(municipioClienteService.buscaClientePorIdMunicipio(ponto.getMunicipio().getId()));
        ponto.setSincronizado(0);
    }

}