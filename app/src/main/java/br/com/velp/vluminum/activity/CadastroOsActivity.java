package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.enumerator.MotivoAbertura;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.enumerator.TipoVeiculo;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.MotivoTrocaService;
import br.com.velp.vluminum.service.MunicipioClienteService;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.serviceimpl.MotivoTrocaServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioClienteServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.DateTimeUtils;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.GeocodificacaoUtil;
import br.com.velp.vluminum.util.InternetUtil;

import br.com.velp.vluminum.util.OrdemServicoUtil;
import br.com.velp.vluminum.util.SpinnerUtil;
import br.com.velp.vluminum.util.VLuminumUtil;

@EActivity(R.layout.cadastro_os_activity)
public class CadastroOsActivity extends ActivityBase {

    private static final String SALVAR = "Salvar";
    private static final String EDITAR = "Editar";
    private static final String ATUALIZAR = "Atualizar";

    @ViewById
    EditText osEditText;

    @ViewById
    Spinner motivoAberturaSpinner;

    @ViewById
    Button pesquisarEnderecoBtn;

    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText numLogradouroEditText;

    @ViewById
    EditText bairroEditText;

    @ViewById
    Spinner estadoSpinner;

    @ViewById
    Spinner municipioSpinner;

    @ViewById
    EditText cepEditText;

    @ViewById
    Spinner prioridadeSpinner;

    @ViewById
    EditText prazoEditText;

    @ViewById
    EditText obsEditText;

    @ViewById
    Spinner tipoVeiculoSpinner;

    @ViewById
    EditText obsTecnicaEditText;

    @ViewById
    Button cadastrarFotoBtn;

    @ViewById
    Button pontosBtn;

    @ViewById
    Button salvarOSBtn;

    @ViewById
    Button finalizarOSBtn;

    @ViewById
    LinearLayout containerPrincipal;

    @ViewById
    TextView labelObs;

    @ViewById
    TextView labelObsTecnica;

    @ViewById
    EditText dataCriacaoEditText;

    @ViewById
    RelativeLayout dadosChamado;

    @ViewById
    TextView labelProtocolo;

    @ViewById
    EditText protocoloEditText;

    @ViewById
    TextView labelNome;

    @ViewById
    EditText nomeEditText;

    @ViewById
    TextView labelTelefone;

    @ViewById
    EditText telefoneEditText;

    @ViewById
    TextView labelQuantidade;

    @ViewById
    EditText quantidadeEditText;


    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    private MunicipioClienteService municipioClienteService = new MunicipioClienteServiceImpl(this);

    private OrdemServicoService osService = new OrdemServicoServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private MotivoTrocaService motivoTrocaService = new MotivoTrocaServiceImpl(this);

    private Location location;

    private OrdemServico ordemServico;

    private AlertDialog alerta;

    private MotivoTroca motivoEncerramentoSelecionado;

    private boolean newer;

    private Boolean cadastroAcessadoAPartirDoMapa;

    private Ponto ponto;

    private boolean loading = false;

    private boolean semPonto = false;

    private int mYear, mMonth, mDay, mHour, mMin, mSeg;;

    static final int DATE_DIALOG_ID = 1;

    @ViewById
    ImageView btnData;

    DatePickerDialog.OnDateSetListener listenerInicial;


    @AfterViews
    void inicializar() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMin = c.get(Calendar.MINUTE);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mSeg = c.get(Calendar.SECOND);


        btnData = (ImageView) findViewById(R.id.btnData);

        // add a click listener to the button
        btnData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        listenerInicial = new DatePickerDialog.OnDateSetListener(){

            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                mYear = arg1;
                mMonth = arg2;
                mDay = arg3;
                updateDisplay();
            }
        };

        bloquearObservacoes();

        if (!newer) {
            recuperarParametros();
        }else{
            visibilidadeChamado(false);
        }

        carregarSpinners();

        if (cadastroAcessadoAPartirDoMapa) {
            coletarEnderecoPonto();
        }

        inicializarOS();

        verificarPermissaoFinalizarOs();

        alterarBotao();
    }

    private void visibilidadeChamado(boolean visivel){

        if(visivel){
            dadosChamado.setVisibility(View.VISIBLE);
            labelProtocolo.setVisibility(View.VISIBLE);
            protocoloEditText.setVisibility(View.VISIBLE);
            labelNome.setVisibility(View.VISIBLE);
            nomeEditText.setVisibility(View.VISIBLE);
            labelTelefone.setVisibility(View.VISIBLE);
            telefoneEditText.setVisibility(View.VISIBLE);
            labelQuantidade.setVisibility(View.VISIBLE);
            quantidadeEditText.setVisibility(View.VISIBLE);

        }else{
            dadosChamado.setVisibility(View.GONE);
            labelProtocolo.setVisibility(View.GONE);
            protocoloEditText.setVisibility(View.GONE);
            labelNome.setVisibility(View.GONE);
            nomeEditText.setVisibility(View.GONE);
            labelTelefone.setVisibility(View.GONE);
            telefoneEditText.setVisibility(View.GONE);
            labelQuantidade.setVisibility(View.GONE);
            quantidadeEditText.setVisibility(View.GONE);

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, listenerInicial, mYear, mMonth, mDay);

        }
        return null;
    }


    /**
     * Método para atualizar os valores no campo data.
     *
     * @author Henrique Frederico
     */
    private void updateDisplay() {
        dataCriacaoEditText.setText(new StringBuilder().append(pad(mDay)).append("/")
                // Month is 0 based so add 1
                .append(pad(mMonth + 1)).append("/").append(mYear).append(" ")
                .append(pad(mHour)).append(":").append(pad(mMin))// .append(":")
                // .append(pad(mSeg)).append(" ")


        );
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    /**
     * Método para atualizar os valores no campo data.
     *
     * @param c
     *            - valor do campo data
     * @author Henrique Frederico
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }





    private void verificarPermissaoFinalizarOs() {
        if (ordemServico != null && ordemServico.getId() != null) {
            Long qtdeTotalPontosDaOS = ordemServicoPontoService.quantidadePontosDaOs(ordemServico.getId());
            Long qtdeTotalPontosDaOsNaoFinalizados = ordemServicoPontoService.quantidadePontosDaOsNaoFinalizados(ordemServico.getId());
            // Quando houver mais de um ponto, o usuario só vê o botao finalizar OS se finalizou os pontos na ligação
            if (qtdeTotalPontosDaOS != null && qtdeTotalPontosDaOS > 1
                    && qtdeTotalPontosDaOsNaoFinalizados != null && qtdeTotalPontosDaOsNaoFinalizados > 0) {
                finalizarOSBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void coletarEnderecoPonto() {
        final double latitude = ponto.getLatitude();
        final double longitude = ponto.getLongitude();

        // Obtêm o endereço
        GeocodificacaoUtil geocodificacaoUtil = new GeocodificacaoUtil(this);
        Address address = null;
        try {
            address = geocodificacaoUtil.getEndereco(latitude, longitude);
        } catch (IOException ex) {
            Log.e(CadastroPontoActivity.class.getName(), "Não foi possível obter o Endereco do Ponto", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
        }

        if (address == null) {
            exibirMsgNaoFoiPossivelObterEndereco();
        } else {
            logradouroEditText.setText(address.getThoroughfare() == null ? null : address.getThoroughfare().toUpperCase());
            numLogradouroEditText.setText(address.getSubThoroughfare() == null ? null : address.getSubThoroughfare().toUpperCase());
            bairroEditText.setText(address.getSubLocality() == null ? null : address.getSubLocality().toUpperCase());

            String nomeEstado = address.getAdminArea();
            Estado estado = Estado.getEstado(nomeEstado); // Retorna a sigla da UF baseado no nome da UF.
            if (estado != null) {
                estadoSpinner.setSelection(SpinnerUtil.getIndice(estadoSpinner, estado.getSigla()));
            }

            String municipioNome = address.getLocality();
            if (municipioNome != null) {
                municipioSpinner.setSelection(SpinnerUtil.getIndice(municipioSpinner, municipioNome));
            }
            cepEditText.setText(address.getPostalCode().replaceAll("\\D", ""));
        }
    }

    @Override
    public void onBackPressed() {
        finish();

        if (!cadastroAcessadoAPartirDoMapa) {
            Intent intent = new Intent(this, ListaOsActivity_.class);
            if (ponto != null) {
                intent.putExtra(Ponto.class.getName(), ponto);
            }
            startActivity(intent);

        }
    }

    private void bloquearObservacoes() {
        obsTecnicaEditText.setSingleLine(false);
        obsEditText.setSingleLine(false);

        if (OrdemServicoUtil.getInstance().isNovaOS()) {
            labelObs.setVisibility(View.INVISIBLE);
            obsEditText.setVisibility(View.INVISIBLE);
            labelObsTecnica.setVisibility(View.INVISIBLE);
            obsTecnicaEditText.setVisibility(View.INVISIBLE);
            OrdemServicoUtil.getInstance().setNovaOS(false);
            visibilidadeChamado(false);
        }
    }

    private void inicializarOS() {
        if (ordemServico == null) {
            ordemServico = new OrdemServico();
            ordemServico.setNumOs(Math.abs(new Random().nextInt())); // TODO: POSSIVELMENTE IRÁ MUDAR. DEVERÁ SER GERADO EM UM PADRÃO!
            osEditText.setText(ordemServico.getNumOs().toString());
            cadastrarFotoBtn.setEnabled(false);
            pontosBtn.setEnabled(false);
            finalizarOSBtn.setVisibility(View.INVISIBLE);
        } else {
            habilitarModoEdicao(true);
            cadastrarFotoBtn.setEnabled(true);
            pontosBtn.setEnabled(true);
            finalizarOSBtn.setVisibility(View.VISIBLE);
        }
    }

    private void habilitarModoEdicao(boolean opcao) {
        if (opcao) {
            loading = true;
            carregarDadosOS();
            habilitarComponentes(false);
            salvarOSBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
            salvarOSBtn.setText(EDITAR);
        } else {
            loading = false;
            habilitarComponentes(true);
            salvarOSBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
            salvarOSBtn.setText(ATUALIZAR);
        }
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());

        cadastroAcessadoAPartirDoMapa = (Boolean) intent.getSerializableExtra("cadastroAcessadoAPartirDoMapa");
        if (cadastroAcessadoAPartirDoMapa == null) {
            cadastroAcessadoAPartirDoMapa = false;
        }




    }

    private void exibirMsgNaoFoiPossivelObterEndereco() {
        AlertaUtil.mensagemInformacao(this, "Não foi possível obter o Endereço.");
    }

    void carregarSpinners() {
        motivoAberturaSpinner.setAdapter(new ArrayAdapter<MotivoAbertura>(this, android.R.layout.simple_spinner_item, MotivoAbertura.values()));


        motivoAberturaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                alterarBotao();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        prioridadeSpinner.setAdapter(new ArrayAdapter<Prioridade>(this, android.R.layout.simple_spinner_item, Prioridade.values()));

        List<String> estados = municipioService.listarEstados();
        estadoSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados));

        tipoVeiculoSpinner.setAdapter(new ArrayAdapter<TipoVeiculo>(this, android.R.layout.simple_spinner_item, TipoVeiculo.values()));
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

    @Click(R.id.pontosBtn)
    void abrirListaPontos() {
        if (ordemServico != null) {
            Intent intent;


            if (semPonto) {
                intent = new Intent(this, TabCadastroMaterialActivity_.class);
                intent.putExtra(OrdemServico.class.getName(), ordemServico);

            } else {
                intent = new Intent(this, ListaPontoOsActivity_.class);
                intent.putExtra(OrdemServico.class.getName(), ordemServico);
            }


            startActivity(intent);
            finish();
        }
    }


    private void alterarBotao() {
        semPonto = false;

        MotivoAbertura motivoAbertura = (MotivoAbertura) motivoAberturaSpinner.getSelectedItem();
        if (motivoAbertura == MotivoAbertura.PODA_DE_ARVORE) {
            semPonto = true;
        }
        if (semPonto) {
            pontosBtn.setText("Serviços");
            pontosBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.servico, 0, 0, 0);
        } else {
            pontosBtn.setText("Pontos");
            pontosBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ponto, 0, 0, 0);

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

            // Obtêm o endereço
            GeocodificacaoUtil geocodificacaoUtil = new GeocodificacaoUtil(this);
            Address address = null;
            try {
                address = geocodificacaoUtil.getEndereco(latitude, longitude);
            } catch (IOException ex) {
                Log.e(CadastroPontoActivity.class.getName(), "Não foi possível obter o Endereco", ex);
                AlertaUtil.mensagemErro(this, ex.getMessage());
            }

            if (address == null) {
                exibirMsgNaoFoiPossivelObterEndereco();
            } else {
                logradouroEditText.setText(address.getThoroughfare() == null ? null : address.getThoroughfare().toUpperCase());
                numLogradouroEditText.setText(address.getSubThoroughfare() == null ? null : address.getSubThoroughfare().toUpperCase());
                bairroEditText.setText(address.getSubLocality() == null ? null : address.getSubLocality().toUpperCase());

                String nomeEstado = address.getAdminArea();
                Estado estado = Estado.getEstado(nomeEstado);
                if (estado != null) {
                    estadoSpinner.setSelection(SpinnerUtil.getIndice(estadoSpinner, estado.getSigla()));
                }

                String municipioNome = address.getLocality();
                if (municipioNome != null) {
                    municipioSpinner.setSelection(SpinnerUtil.getIndice(municipioSpinner, municipioNome));
                }
                cepEditText.setText(address.getPostalCode());
            }

        }
    }

    @Click(R.id.salvarOSBtn)
    void salvar() {
        if (salvarOSBtn.getText().toString().equals(EDITAR)) {
            habilitarModoEdicao(false);
            return;
        }

        boolean result = false;
        boolean finalizarOs = false;
        prepararOsParaInsercao(finalizarOs);

        try {
            result = osService.salvarOuAtualizar(ordemServico);
        } catch (CampoObrigatorioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemAlerta(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroPosteActivity.class.getName(), "Erro ao salvar OS", ex);
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
            return;
        }

        if (result) {
            String msg = (salvarOSBtn.getText().toString().equals(SALVAR)) ? "OS cadastrada com sucesso!" : "OS atualizada com sucesso!";
            AlertaUtil.mostrarMensagem(this, msg);

            newer = true;

            if (cadastroAcessadoAPartirDoMapa && ponto != null) {
                associarPontoAOrdemServico();
            }

            inicializar();
        }
    }

    void finalizar() {
        if (motivoEncerramentoSelecionado != null) {
            ordemServico.setMotivoEncerramento(motivoEncerramentoSelecionado);

            boolean result = false;
            boolean finalizarOs = true;
            prepararOsParaInsercao(finalizarOs);

            // Muda a situação da OS

            ordemServico.setSituacao(new Situacao(Situacao.SITUACAO_FINALIZADA.toString()));

            try {
                result = osService.salvarOuAtualizar(ordemServico);
            } catch (RegraNegocioException ex) {
                AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
            } catch (Exception ex) {
                Log.e(CadastroPosteActivity.class.getName(), "Erro ao finalizar OS", ex);
                AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
                return;
            }

            if (result) {
                String msg = "OS finalizada com sucesso!";
                AlertaUtil.mostrarMensagem(this, msg);

                startActivity(new Intent(this, ListaOsActivity_.class));
                finish();
            }
        }
    }

    @Click(R.id.finalizarOSBtn)
    void mostrarDialogSelecaoMotivoEncerramento() {
        final List<MotivoTroca> motivos = motivoTrocaService.listar("descricao", true);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rowlayout_lista_motivo_troca, motivos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o Motivo de Encerramento:");

        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int indiceItemSelecionado) {
                motivoEncerramentoSelecionado = motivos.get(indiceItemSelecionado);

                finalizar();

                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    @Click(R.id.cadastrarFotoBtn)
    void abrirTelaCadastroFotos() {
        if (ordemServico != null) {
            Intent intent = new Intent(this, ListaFotoActivity_.class);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
            startActivity(intent);
        }
    }

    private void habilitarComponentes(boolean opcao) {
        for (int i = 0; i < containerPrincipal.getChildCount(); i++) {
            View componenteFilho = containerPrincipal.getChildAt(i);
            if (componenteFilho instanceof EditText || componenteFilho instanceof Spinner || componenteFilho instanceof ImageView) {
                componenteFilho.setEnabled(opcao);
            }
        }

        pesquisarEnderecoBtn.setEnabled(opcao);
    }

    private void carregarDadosOS() {
        ordemServico.setId(ordemServico.getId());

        if (ordemServico.getNumOs() != null) {
            osEditText.setText(ordemServico.getNumOs().toString());
        }

        if (ordemServico.getMotivoAbertura() != null) {
            motivoAberturaSpinner.setSelection(SpinnerUtil.getIndice(motivoAberturaSpinner, ordemServico.getMotivoAbertura().getDescricao()));
        }

        if (ordemServico.getLogradouro() != null) {
            logradouroEditText.setText(ordemServico.getLogradouro());
        }

        if (ordemServico.getNumLogradouro() != null) {
            numLogradouroEditText.setText(ordemServico.getNumLogradouro());
        }

        if (ordemServico.getDataCadastro() != null) {
            dataCriacaoEditText.setText(DateTimeUtils.converterDataParaStringFormatoPadrao(ordemServico.getDataCadastro()));
        }

        if (ordemServico.getNomeChamado() != null) {
            visibilidadeChamado(true);
            nomeEditText.setText(ordemServico.getNomeChamado());
        }else{
            visibilidadeChamado(false);
        }


        if (ordemServico.getProtocoloChamado() != null) {
            protocoloEditText.setText(ordemServico.getProtocoloChamado());
        }



        if (ordemServico.getTelChamado() != null) {
            telefoneEditText.setText(ordemServico.getTelChamado());
        }

        if (ordemServico.getQtdChamado() != null) {
            quantidadeEditText.setText(ordemServico.getQtdChamado());
        }









        if (ordemServico.getBairro() != null) {
            bairroEditText.setText(ordemServico.getBairro());
        }

        if (ordemServico.getMunicipio() != null) {
            Municipio municipio = municipioService.buscarPorId(ordemServico.getMunicipio().getId());
            estadoSpinner.setSelection(SpinnerUtil.getIndice(estadoSpinner, municipio.getUf()));

            List<Municipio> municipios = municipioService.listarPorEstado(municipio.getUf());
            municipioSpinner.setAdapter(new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipios));

            int indice = SpinnerUtil.getIndice(municipioSpinner, municipio.getNome());
            municipioSpinner.setSelection(indice);
        }

        if (ordemServico.getCep() != null) {
            cepEditText.setText(ordemServico.getCep());
        }

        if (ordemServico.getPrioridade() != null) {
            prioridadeSpinner.setSelection(SpinnerUtil.getIndice(prioridadeSpinner, ordemServico.getPrioridade().getDescricao()));
        }

        if (ordemServico.getPrazoAtendimento() != null) {
            prazoEditText.setText(ordemServico.getPrazoAtendimento().toString());
        }

        if (ordemServico.getObs() != null) {
            obsEditText.setText(ordemServico.getObs().toString());
        }

        if (ordemServico.getTipoCesto() != null) {
            tipoVeiculoSpinner.setSelection(SpinnerUtil.getIndice(tipoVeiculoSpinner, ordemServico.getTipoCesto().getDescricao()));
        }

        if (ordemServico.getObsCampo() != null) {
            obsTecnicaEditText.setText(ordemServico.getObsCampo().toString());
        }
    }

    private void prepararOsParaInsercao(boolean finalizarOs) {
        try {
            if (finalizarOs) {
                ordemServico.setSituacao(new Situacao(Situacao.SITUACAO_FINALIZADA.toString()));
            } else {
                ordemServico.setSituacao(new Situacao(Situacao.SITUACAO_EM_CAMPO.toString()));
            }

            MotivoAbertura motivoAbertura = (MotivoAbertura) motivoAberturaSpinner.getSelectedItem();
            ordemServico.setMotivoAbertura(motivoAbertura);

            Prioridade prioridade = (Prioridade) prioridadeSpinner.getSelectedItem();
            ordemServico.setPrioridade(prioridade);

            try {
                String dataCadastro = (dataCriacaoEditText.getText().toString() != null) ? dataCriacaoEditText.getText().toString() : null;
                if (dataCadastro != null) {
                    ordemServico.setDataCadastro(DateTimeUtils.converteStringParaDateFormatoPadrao3(dataCadastro));
                } else {
                    ordemServico.setDataCadastro(new Date());
                }
            } catch (Exception e) {
                ordemServico.setDataCadastro(new Date());
            }


            String prazo = (prazoEditText.getText() != null && prazoEditText.getText().length() > 0) ? prazoEditText.getText().toString() : null;
            if (prazo != null) {
                ordemServico.setPrazoAtendimento(Integer.valueOf(prazo));
            }

            String obs = (obsEditText.getText() != null) ? obsEditText.getText().toString() : null;
            ordemServico.setObs(obs);

            String logradouro = (logradouroEditText.getText() != null) ? logradouroEditText.getText().toString() : null;
            ordemServico.setLogradouro(logradouro);

            String numLogradouro = (numLogradouroEditText.getText() != null) ? numLogradouroEditText.getText().toString() : null;
            ordemServico.setNumLogradouro(numLogradouro);

            String bairro = (bairroEditText.getText() != null) ? bairroEditText.getText().toString() : null;
            ordemServico.setBairro(bairro);
            if (municipioSpinner.getSelectedItem() != null) {
                Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
                if (municipio != null && !municipio.getNome().contains("SELECIONE")) {
                    ordemServico.setMunicipio(municipio);
                }
            }
            String cep = (cepEditText.getText() != null) ? cepEditText.getText().toString() : null;
            ordemServico.setCep(cep);

            TipoVeiculo tipoCesto = (TipoVeiculo) tipoVeiculoSpinner.getSelectedItem();
            ordemServico.setTipoCesto(tipoCesto);

            String obsTecnica = (obsTecnicaEditText.getText() != null) ? obsTecnicaEditText.getText().toString() : null;
            ordemServico.setObsCampo(obsTecnica);

            ordemServico.setUsuarioExecucao(VLuminumUtil.getUsuarioLogado().getId());
            ordemServico.setUsuarioAtribuicao(VLuminumUtil.getUsuarioLogado().getId());
            ordemServico.setIdCliente(municipioClienteService.buscaClientePorIdMunicipio(ordemServico.getMunicipio().getId()));
            ordemServico.setSincronizado(0);
        } catch (Exception e) {
            e.printStackTrace();
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

}