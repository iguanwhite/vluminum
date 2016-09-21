package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Alimentador;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.MunicipioCliente;
import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.entity.Pergunta;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.entity.Sincronismo;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.entity.Subestacao;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.entity.TipoPoste;
import br.com.velp.vluminum.entity.UsuarioCliente;
import br.com.velp.vluminum.enumerator.Danificado;
import br.com.velp.vluminum.enumerator.Direcao;
import br.com.velp.vluminum.enumerator.MotivoAbertura;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.enumerator.TipoSincronismo;
import br.com.velp.vluminum.enumerator.TipoVeiculo;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;
import br.com.velp.vluminum.service.AlimentadorService;
import br.com.velp.vluminum.service.FotoService;
import br.com.velp.vluminum.service.GrupoService;
import br.com.velp.vluminum.service.GrupoServicoService;
import br.com.velp.vluminum.service.MaterialIdentificadoService;
import br.com.velp.vluminum.service.MaterialService;
import br.com.velp.vluminum.service.MotivoTrocaService;
import br.com.velp.vluminum.service.MunicipioClienteService;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.service.OpcaoPerguntaService;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.service.OrdemServicoServicoService;
import br.com.velp.vluminum.service.PerguntaService;
import br.com.velp.vluminum.service.PontoService;
import br.com.velp.vluminum.service.PosteService;
import br.com.velp.vluminum.service.RespostaService;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.service.SincronismoService;
import br.com.velp.vluminum.service.SituacaoService;
import br.com.velp.vluminum.service.TipoPosteService;
import br.com.velp.vluminum.service.TipoService;
import br.com.velp.vluminum.service.UsuarioClienteService;
import br.com.velp.vluminum.serviceimpl.AlimentadorServiceImpl;
import br.com.velp.vluminum.serviceimpl.FotoServiceImpl;
import br.com.velp.vluminum.serviceimpl.GrupoServiceImpl;
import br.com.velp.vluminum.serviceimpl.GrupoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialIdentificadoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialServiceImpl;
import br.com.velp.vluminum.serviceimpl.MotivoTrocaServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioClienteServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.serviceimpl.OpcaoPerguntaServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PerguntaServiceImpl;
import br.com.velp.vluminum.serviceimpl.PontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PosteServiceImpl;
import br.com.velp.vluminum.serviceimpl.RespostaServiceImpl;
import br.com.velp.vluminum.serviceimpl.ServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.SincronismoServiceImpl;
import br.com.velp.vluminum.serviceimpl.SituacaoServiceImpl;
import br.com.velp.vluminum.serviceimpl.TipoPosteServiceImpl;
import br.com.velp.vluminum.serviceimpl.TipoServiceImpl;
import br.com.velp.vluminum.serviceimpl.UsuarioClienteServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.Util;

@EActivity(R.layout.sincronismo_activity)
public class SincronismoActivity extends Activity {

    public static final int ERRO_SEM_CONEXAO_INTERNET = 3;

    public static final int ERRO_USUARIO_SEM_CLIENTE = 2;

    public static final int SINCRONISMO_CONCLUIDO = 1;

    public static final int ERRO = 0;

    public final File CAMINHO_FOTO = new File("/sdcard/vluminum/fotos/");

    public final String STATUS_ATRIBUIDA = "8";

    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private StringBuilder msgErro = new StringBuilder();

    public final String RETORNO_TRUE = "true";
    public final String RETORNO_FALSE = "false";
    public final String RETORNO_OS_ATUALIZADA = "os_atualizada";

    @ViewById
    TextView texto;

    @ViewById
    TextView qtde;

    @ViewById
    ProgressBar progress;

    @ViewById
    TextView tabela;

    @ViewById
    TextView textView;

    @ViewById
    Button buttonErro;

    @ViewById
    Button buttonOk;

    @ViewById
    Button buttonCancelar;

    private HttpAsyncTaskInicial assyncTask;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    private MunicipioClienteService municipioClienteService = new MunicipioClienteServiceImpl(this);

    private TipoPosteService tipoPosteService = new TipoPosteServiceImpl(this);

    private PosteService posteService = new PosteServiceImpl(this);

    private TipoService tipoService = new TipoServiceImpl(this);

    private GrupoService grupoService = new GrupoServiceImpl(this);

    private GrupoServicoService grupoServicoService = new GrupoServicoServiceImpl(this);

    private MaterialService materialService = new MaterialServiceImpl(this);

    private MaterialIdentificadoService materialIdentificadoService = new MaterialIdentificadoServiceImpl(this);

    private SituacaoService situacaoService = new SituacaoServiceImpl(this);

    private OrdemServicoService osService = new OrdemServicoServiceImpl(this);

    private PerguntaService perguntaService = new PerguntaServiceImpl(this);

    private PontoService pontoService = new PontoServiceImpl(this);

    private SincronismoService sincronismoService = new SincronismoServiceImpl(this);

    private ServicoService servicoService = new ServicoServiceImpl(this);

    private AlimentadorService alimentadorService = new AlimentadorServiceImpl(this);

    private OpcaoPerguntaService opcaoPerguntaService = new OpcaoPerguntaServiceImpl(this);

    private MotivoTrocaService motivoTrocaService = new MotivoTrocaServiceImpl(this);

    private RespostaService respostaService = new RespostaServiceImpl(this);

    private FotoService fotoService = new FotoServiceImpl(this);

    private UsuarioClienteService usuarioClienteService = new UsuarioClienteServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private OrdemServicoServicoService ordemServicoServicoService = new OrdemServicoServicoServiceImpl(this);

    private ConfiguracaoWebService confWs;

    private final String ARQUIVO_LOG = "/sdcard/vluminum/arquivo_log.txt";

    private InternetUtil internetUtil;

    @AfterViews
    void inicializar() {
        internetUtil = new InternetUtil(this);

        try {
            Intent intent = getIntent();
            confWs = (ConfiguracaoWebService) intent.getSerializableExtra(ConfiguracaoWebService.class.getName());

            // TODO: CRIAR OBJETO 'ConfiguracaoWebService' ao invês de criar várias strings
            Util.endereco = confWs.getUrl();
            Util.porta = confWs.getPorta();
            Util.nomeWS = confWs.getNomeWS();
            Util.login = confWs.getUsuario();
            Util.senha = confWs.getSenha();

            // Cria arquivo de LOG
            try {
                File arquivo_log = new File(ARQUIVO_LOG);
                if (!arquivo_log.exists()) {
                    OutputStream fo = new FileOutputStream(arquivo_log);
                    fo.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (confWs.getTipoSincronismo() == TipoSincronismo.INICIAL) {
                assyncTask = new HttpAsyncTaskInicial(this, progress, texto, textView, tabela, qtde, true);
            } else if (confWs.getTipoSincronismo() == TipoSincronismo.PARCIAL) {
                assyncTask = new HttpAsyncTaskInicial(this, progress, texto, textView, tabela, qtde, false);
            }
            assyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gravarMSGErro(String tabela, String id) {
        msgErro.append("Erro ao gravar ").append(tabela).append(" ID: ").append(id).append("\n");
    }

    public void gravarMSGErroUpload(String tabela, String id) {
        msgErro.append("Erro ao realizar o upload ").append(tabela).append(" ID: ").append(id).append("\n");
    }

    public void gravarErro(Exception erro) {
        try {
            File file = new File(ARQUIVO_LOG);
            long tamanhoBytes = file.length();
            long tamanhoMegaBytes = tamanhoBytes / (1024 * 1024);
            if (tamanhoMegaBytes < 2) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ARQUIVO_LOG, true)));
                out.println();
                erro.printStackTrace(out);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.buttonErro)
    public void abrirTelaLogUsuario() {
        Intent intent = new Intent(this, LogUsuarioActivity_.class);
        Bundle b = new Bundle();
        b.putString("log", msgErro.toString());
        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

    }

    @Click(R.id.buttonCancelar)
    public void cancelar() {
        if (assyncTask != null) {
            assyncTask.cancel(true);
        }

        finish();
    }

    @Click(R.id.buttonOk)
    public void ok() {
        finish();
    }


    public class HttpAsyncTaskInicial extends AsyncTask<Object, Object, Integer> {

        ProgressBar progressBar;
        int contador = 1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> arrayList = new ArrayList<String>();
        private Context context;
        private TextView texto;
        private TextView textoPrincipal;
        private TextView tabela;
        private TextView qtde;
        private int total = 0;
        private int PROGRESSO;
        private boolean isInicial;

        public HttpAsyncTaskInicial(Context context, ProgressBar progressBar, TextView texto, TextView textoPrincipal, TextView tabela, TextView qtde, boolean isInicial) {

            this.context = context;
            this.tabela = tabela;
            this.progressBar = progressBar;
            this.texto = texto;
            this.qtde = qtde;
            this.textoPrincipal = textoPrincipal;
            this.isInicial = isInicial;
        }

        public HttpAsyncTaskInicial(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            texto.setText("0%");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            total += PROGRESSO;
            progressBar.incrementProgressBy(PROGRESSO);
            texto.setText(total + "%");
            super.onProgressUpdate(values);
        }

        public void atualizaTextoTabela(final String texto) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tabela.setText(texto);
                    qtde.setText("0/0");

                }
            });
        }

        public void setBotaoErroVisile() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonErro.setVisibility(View.VISIBLE);
                }
            });
        }

        public void atualizaTextoQtde(final String texto) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    qtde.setText(texto);

                }
            });
        }

        public void atualizaTextopPrincipal(final String texto) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textoPrincipal.setText(texto);
                }
            });
        }

        public void enableButton() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonOk.setEnabled(true);
                    buttonCancelar.setEnabled(false);

                }
            });
        }

        public void atualizaBarraProgresso() {

            try {
                Thread.sleep(1000);
                publishProgress();
                Thread.sleep(1000);
                contador = 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void apagarTabelaSincronismo(String tabela) {
            sincronismoService.deletarTabelaSinc(tabela);
        }

        public void inseriDataHoraSinc(String tabela) {
            try {
                InputStream inputStream = Util.realizaRequisicao("getDataHora", null, null, null, null);
                String result = Util.convertInputStreamToString(inputStream);
                if (result != null && !result.isEmpty()) {
                    Sincronismo sincronismo = new Sincronismo();
                    sincronismo.setTabela(tabela);
                    sincronismo.setDataSincronismo(new java.util.Date(format.parse(result).getTime()));
                    sincronismoService.salvarOuAtualizar(sincronismo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }

        }

        public void atualizaDataHoraSinc(String tabela) {
            try {
                InputStream inputStream = Util.realizaRequisicao("getDataHora", null, null, null, null);
                String result = Util.convertInputStreamToString(inputStream);
                if (result != null && !result.isEmpty()) {
                    sincronismoService.atualizarDataSincronismo(tabela, result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }

        }

        //INICIAL
        @Override
        protected Integer doInBackground(Object... param) {
            try {
                String idsCliente = "";
                Util.setarUsuarioLogado(confWs.getUsuario());
                if (internetUtil.isInternetAtiva()) {
                    if (upload()) {
                        return null;
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                atualizaDataHoraSinc("tb_sincronismo");

                atualizaTextopPrincipal("Realizando o Download de: ");
                InputStream inputStream = null;
                String result = null;

                //USUARIO_CLIENTE
                atualizaTextoTabela("USUARIO X CLIENTE");
                //Busca data e hora

                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        //Deleta tabela sincronismo
                        usuarioClienteService.deletarTudo();
                        inseriDataHoraSinc("tb_usuario_cliente");
                        inputStream = Util.realizaRequisicao("getUsuarioCliente", null, confWs.getIdUsuario(), null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_usuario_cliente");
                        inputStream = Util.realizaRequisicao("getUsuarioCliente", data, confWs.getIdUsuario(), null, null);
                    }

                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty() && !result.equals("[]")) {

                        JSONObject jsonUsuarioCliente = null;


                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonUsuarioCliente = (JSONObject) jsonArray.get(i);
                                    UsuarioCliente usuarioCliente = new UsuarioCliente();
                                    usuarioCliente.setId(jsonUsuarioCliente.getInt("id_usuario_cliente"));
                                    usuarioCliente.setIdUsuario(jsonUsuarioCliente.getInt("id_usuario"));
                                    usuarioCliente.setIdCliente(jsonUsuarioCliente.getInt("id_cliente"));

                                    usuarioClienteService.salvarOuAtualizar(usuarioCliente);
                                    ++contador;
                                    idsCliente += usuarioCliente.getIdCliente() + ",";
                                } catch (Exception e) {
                                    gravarMSGErro("USUARIO_CLIENTE", String.valueOf(jsonUsuarioCliente.getInt("id_usuario_cliente")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_usuario_cliente");
                                return null;
                            }
                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_usuario_cliente");
                        }

                    } else {
                        //Busca usuario x cliente
                        List<UsuarioCliente> lista = usuarioClienteService.listar("id_cliente", true);
                        if (lista == null || lista.isEmpty()) {
                            return ERRO_USUARIO_SEM_CLIENTE;
                        } else {
                            for (UsuarioCliente item : lista) {
                                idsCliente += item.getIdCliente() + ",";
                            }
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                if (Util.stringBrancaOuNull(idsCliente)) {
                    return ERRO_USUARIO_SEM_CLIENTE;
                }

                if (idsCliente.length() > 0 && idsCliente.charAt(idsCliente.length() - 1) == ',') {
                    idsCliente = idsCliente.substring(0, idsCliente.length() - 1);
                }

                PROGRESSO = 2;
                atualizaBarraProgresso();
                /**/
                //Sincronismo MUNICIPIO
                atualizaTextoTabela("MUNICÍPIO");
                //Busca data e hora
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        municipioService.deletarTudo();
                        inseriDataHoraSinc("tb_municipio");
                        inputStream = Util.realizaRequisicao("buscaMunicipios", null, null, idsCliente, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_municipio");
                        inputStream = Util.realizaRequisicao("buscaMunicipios", data, null, idsCliente, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {
                        JSONObject jsonMunicipio = null;
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonMunicipio = (JSONObject) jsonArray.get(i);
                                    Municipio mun = new Municipio(jsonMunicipio.getInt("id_municipio"), jsonMunicipio.getString("nome"), jsonMunicipio.getString("uf"));
                                    municipioService.salvarOuAtualizar(mun);
                                    ++contador;

                                } catch (Exception e) {
                                    gravarMSGErro("MUNICÍPIO", String.valueOf(jsonMunicipio.getInt("id_municipio")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_municipio");
                                return null;
                            }
                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_municipio");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                PROGRESSO = 3;
                atualizaBarraProgresso();
                /**/
                //Sincronismo MUNICIPIO_CLIENTE
                atualizaTextoTabela("MUNICÍPIO CLIENTE");
                //Busca data e hora
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        municipioClienteService.deletarTudo();
                        inseriDataHoraSinc("tb_municipio_cliente");
                        inputStream = Util.realizaRequisicao("getMunicipioCliente", null, null, idsCliente, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_municipio_cliente");
                        inputStream = Util.realizaRequisicao("getMunicipioCliente", data, null, idsCliente, null);
                    }
                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {
                        JSONObject jsonMunicipioCliente = null;
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonMunicipioCliente = (JSONObject) jsonArray.get(i);
                                    MunicipioCliente mun = new MunicipioCliente();
                                    mun.setId(jsonMunicipioCliente.getInt("id_municipio_cliente"));
                                    mun.setIdCliente(jsonMunicipioCliente.getInt("id_cliente"));
                                    mun.setUsuarioVinculacao(jsonMunicipioCliente.getInt("usuario_vinculacao"));
                                    mun.setIdMunicipio(jsonMunicipioCliente.getInt("id_municipio"));
                                    mun.setDataCadastro(jsonMunicipioCliente.has("data_vinculacao") ? new java.sql.Date(format.parse(jsonMunicipioCliente.getString("data_vinculacao")).getTime()) : null);

                                    municipioClienteService.salvarOuAtualizar(mun);
                                    ++contador;

                                } catch (Exception e) {
                                    gravarMSGErro("MUNICIPIO_CLIENTE", String.valueOf(jsonMunicipioCliente.getInt("id_municipio_cliente")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_municipio_cliente");
                                return null;
                            }
                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_municipio_cliente");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                PROGRESSO = 5;
                atualizaBarraProgresso();

                //Sincronismo POSTE
                atualizaTextoTabela("POSTE");

                //Busca data e hora
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        posteService.deletarTudo();
                        inseriDataHoraSinc("tb_poste");
                        inputStream = Util.realizaRequisicao("buscaPoste", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_poste");
                        inputStream = Util.realizaRequisicao("buscaPoste", data, null, null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {

                        JSONObject jsonPoste = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonPoste = (JSONObject) jsonArray.get(i);
                                    Poste poste = new Poste();
                                    poste.setId(jsonPoste.getString("id_poste"));
                                    poste.setTipoPoste(br.com.velp.vluminum.enumerator.TipoPoste.getTipoPoste(jsonPoste.getInt("tipo_poste")));
                                    poste.setSigla(jsonPoste.getString("sigla"));
                                    poste.setDescricao(jsonPoste.getString("descricao"));
                                    poste.setBarramento((jsonPoste.has("barramento") && jsonPoste.getString("barramento") != null && !jsonPoste.getString("barramento").isEmpty() ? jsonPoste.getString("barramento") : null));
                                    poste.setInformacoesTecnicas(jsonPoste.has("informacao_tec") ? jsonPoste.getString("informacao_tec") : null);
                                    poste.setAltura(jsonPoste.has("altura") ? new BigDecimal(jsonPoste.getString("altura")) : null);
                                    poste.setUsuarioCadastro(1);
                                    poste.setDataCadastro(new Date());
                                    posteService.salvarOuAtualizar(poste);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("POSTE", String.valueOf(jsonPoste.getString("id_poste")));
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_poste");
                                return null;
                            }
                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_poste");
                        }
                    }
                    PROGRESSO = 5;
                    atualizaBarraProgresso();

                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                //TODO: código comentado, esperando a prefeitura fornecer os dados
                /*
                //Sincronismo SUBESTACAO
                atualizaTextoTabela("SUBESTAÇÃO");

                if (internetUtil.isInternetAtiva()) {

                    if (isInicial) {
                        inseriDataHoraSinc("tb_subestacao");
                        inputStream = Util.realizaRequisicao("buscaSubestacao", null, null,null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_subestacao");
                        inputStream = Util.realizaRequisicao("buscaSubestacao", data, idUsuario,null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {
                        SubestacaoService subestacaoService = new SubestacaoServiceImpl(context);
                        subestacaoService.deletarTudo();
                        JSONObject jsonSub = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length() - 1));
                                try {
                                    jsonSub = (JSONObject) jsonArray.get(i);
                                    Subestacao subestacao = new Subestacao();
                                    subestacao.setId(jsonSub.getInt("id_subestacao"));
                                    subestacao.setDescricao(jsonSub.getString("descricao"));

                                    subestacaoService.salvarOuAtualizar(subestacao);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo();
                                return null;
                            }

                        }
                        if(!isInicial){
                            atualizaDataHoraSinc("tb_subestacao");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }
                */
                PROGRESSO = 5;
                atualizaBarraProgresso();

                //Sincronismo PONTO
                atualizaTextoTabela("PONTO");
                /**/
                //Busca lotes de 10 mil pontos.
                Integer ultimoId = 0;
                Integer tamanhoArray = 0;
                Boolean exit = false;
                result = null;
                if (isInicial) {
                    pontoService.deletarRegistrosNaoSinc();
                }
                do {
                    if (internetUtil.isInternetAtiva() && exit == false) {

                        if (isInicial) {
                            inseriDataHoraSinc("tb_ponto");
                            inputStream = Util.realizaRequisicaoPonto("buscaPonto", null, null, idsCliente, null,5000,ultimoId);
                        } else {
                            String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_ponto");
                            inputStream = Util.realizaRequisicao("buscaPonto", data, null, idsCliente, null);
                            exit = true;
                        }


                        result = Util.convertInputStreamToString(inputStream);

                        if (result != null && !result.isEmpty() && !result.equals("[]") ) {


                            JSONObject jsonPonto = null;


                            JSONArray jsonArray = new JSONArray(result);

                            InputStream inputStreamOS = Util.realizaRequisicao("getDataHora", null, null, null, null);
                            String resultData = Util.convertInputStreamToString(inputStreamOS);
                            Date dataHoraInsert = new java.util.Date(format.parse(resultData).getTime());


                            tamanhoArray += jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!this.isCancelled()) {
                                    atualizaTextoQtde(contador + "/" + (tamanhoArray));
                                    try {
                                        jsonPonto = (JSONObject) jsonArray.get(i);
                                        Ponto ponto = new Ponto();


                                        ponto.setId(jsonPonto.getString("id_ponto"));

                                        ponto.setDirecao(jsonPonto.has("direcao") ? Direcao.getDirecao(jsonPonto.getInt("direcao")) : null);
                                        ponto.setNumPlaquetaTransf(jsonPonto.has("num_plaqueta") ? jsonPonto.getString("num_plaqueta") : null);
                                        ponto.setLogradouro(jsonPonto.has("logradouro") ? jsonPonto.getString("logradouro") : null);
                                        ponto.setNumLogradouro(jsonPonto.has("num_logradouro") ? jsonPonto.getString("num_logradouro") : null);
                                        ponto.setBairro(jsonPonto.has("bairro") ? jsonPonto.getString("bairro") : null);
                                        ponto.setDanificado(jsonPonto.has("danificado") ? Danificado.getDanificado(jsonPonto.getBoolean("danificado")) : null);
                                        ponto.setLatitude(jsonPonto.has("latitude") ? new Double(jsonPonto.getString("latitude")) : null);
                                        ponto.setLongitude(jsonPonto.has("longitude") ? new Double(jsonPonto.getString("longitude")) : null);
                                        ponto.setPontoReferencia(jsonPonto.has("ponto_referencia") ? jsonPonto.getString("ponto_referencia") : null);
                                        ponto.setCep(jsonPonto.has("cep") ? jsonPonto.getString("cep") : null);
                                        ponto.setNumTrafo(jsonPonto.has("num_trafo") ? jsonPonto.getInt("num_trafo") : null);
                                        //Municipio
                                        if (jsonPonto.has("id_municipio")) {
                                            ponto.setMunicipio(new Municipio(jsonPonto.getInt("id_municipio")));

                                        }

                                        //Poste
                                        if (jsonPonto.has("id_poste")) {
                                            ponto.setPoste(new Poste(jsonPonto.getString("id_poste")));
                                        }

                                        ponto.setDataSincronismo(dataHoraInsert);

                                        ponto.setIdCliente(jsonPonto.has("id_cliente") ? jsonPonto.getInt("id_cliente") : null);

                                        pontoService.salvarOuAtualizar(ponto);
                                        ultimoId = jsonPonto.getInt("id_sinc");
                                        ++contador;
                                    } catch (Exception e) {
                                        gravarMSGErro("PONTO", String.valueOf(jsonPonto.getString("id_ponto")));
                                        gravarErro(e);
                                    }
                                } else {
                                    apagarTabelaSincronismo("tb_ponto");
                                    return null;
                                }
                            }

                            if (!isInicial) {
                                atualizaDataHoraSinc("tb_ponto");
                            }
                        }
                    } else {
                        return ERRO_SEM_CONEXAO_INTERNET;
                    }
                }while (result != null && !result.isEmpty() && !result.equals("[]") && exit == false );
                result = null;
                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo GRUPO
                atualizaTextoTabela("GRUPO");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        grupoService.deletarTudo();
                        inseriDataHoraSinc("tb_grupo");
                        inputStream = Util.realizaRequisicao("buscaGrupo", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_grupo");
                        inputStream = Util.realizaRequisicao("buscaGrupo", data, null, null, null);
                    }

                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {
                        JSONObject jsonGrupo = null;
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {

                                    //java.sql.Date data = new java.sql.Date(format.parse(jsonTipo.getString("data_alteracao")).getTime());
                                    jsonGrupo = (JSONObject) jsonArray.get(i);
                                    Grupo grupo = new Grupo();
                                    grupo.setId(jsonGrupo.getInt("id_grupo"));
                                    grupo.setDescricao(jsonGrupo.getString("descricao"));
                                    grupo.setUsuarioAlteracao(jsonGrupo.has("usuario_alteracao") ? jsonGrupo.getInt("usuario_alteracao") : null);
                                    grupo.setUsuarioCadastro(jsonGrupo.getInt("usuario_cadastro"));
                                    grupo.setDataAlteracao(jsonGrupo.has("data_alteracao") ? new java.util.Date(format.parse(jsonGrupo.getString("data_alteracao")).getTime()) : null);
                                    grupo.setDataCadastro(jsonGrupo.has("data_cadastro") ? new java.sql.Date(format.parse(jsonGrupo.getString("data_cadastro")).getTime()) : null);

                                    grupoService.salvarOuAtualizar(grupo);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("GRUPO", String.valueOf(jsonGrupo.getInt("id_grupo")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_grupo");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_grupo");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }
                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo TIPO
                atualizaTextoTabela("TIPO");
                //Busca data e hora
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        tipoService.deletarTudo();
                        inseriDataHoraSinc("tb_tipo");
                        inputStream = Util.realizaRequisicao("buscaTipo", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_tipo");
                        inputStream = Util.realizaRequisicao("buscaTipo", data, null, null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);
                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonTipo = null;
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    //java.sql.Date data = new java.sql.Date(format.parse(jsonTipo.getString("data_alteracao")).getTime());
                                    jsonTipo = (JSONObject) jsonArray.get(i);
                                    Tipo tipo = new Tipo();
                                    tipo.setId(jsonTipo.getInt("id_tipo"));
                                    tipo.setDescricao(jsonTipo.getString("descricao"));
                                    tipo.setUsuarioAlteracao(jsonTipo.has("usuario_alteracao") ? jsonTipo.getInt("usuario_alteracao") : null);
                                    tipo.setUsuarioCadastro(jsonTipo.has("usuario_cadastro") ? jsonTipo.getInt("usuario_cadastro") : null);
                                    tipo.setDataAlteracao(jsonTipo.has("data_alteracao") ? new java.util.Date(format.parse(jsonTipo.getString("data_alteracao")).getTime()) : null);
                                    tipo.setDataCadastro(jsonTipo.has("data_cadastro") ? new java.sql.Date(format.parse(jsonTipo.getString("data_cadastro")).getTime()) : null);

                                    //Busca grupo
                                    if (jsonTipo.has("id_grupo")) {
                                        tipo.setGrupo(new Grupo(jsonTipo.getInt("id_grupo")));
                                    }


                                    tipoService.salvarOuAtualizar(tipo);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("TIPO", String.valueOf(jsonTipo.getInt("id_tipo")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_tipo");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_tipo");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }
                PROGRESSO = 5;
                atualizaBarraProgresso();

                //Sincronismo MATERIAL
                atualizaTextoTabela("MATERIAL");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        materialService.deletarTudo();
                        inseriDataHoraSinc("tb_material");
                        inputStream = Util.realizaRequisicao("buscaMaterial", null, null, idsCliente, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_material");
                        inputStream = Util.realizaRequisicao("buscaMaterial", data, null, idsCliente, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonMaterial = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    //java.sql.Date data = new java.sql.Date(format.parse(jsonTipo.getString("data_alteracao")).getTime());
                                    jsonMaterial = (JSONObject) jsonArray.get(i);
                                    Material material = new Material();
                                    material.setIdMaterial(jsonMaterial.getInt("id_material"));
                                    material.setPotencia(jsonMaterial.has("potencia") ? jsonMaterial.getInt("potencia") : null);
                                    material.setInformacaoTec(jsonMaterial.has("informacao_tec") ? jsonMaterial.getString("informacao_tec") : null);
                                    material.setDescricao(jsonMaterial.has("descricao") ? jsonMaterial.getString("descricao") : null);
                                    material.setMarca(jsonMaterial.has("marca") ? jsonMaterial.getString("marca") : null);
                                    if (jsonMaterial.has("id_tipo")) {
                                        material.setTipo(new Tipo(jsonMaterial.getInt("id_tipo")));
                                    }
                                    material.setIdCliente(jsonMaterial.has("id_cliente") ? jsonMaterial.getInt("id_cliente") : null);
                                    material.setIdServicoInstalacao(jsonMaterial.has("id_servico_instalacao") ? jsonMaterial.getLong("id_servico_instalacao") : null);
                                    material.setIdServicoRemocao(jsonMaterial.has("id_servico_remoto") ? jsonMaterial.getLong("id_servico_remoto") : null);
                                    material.setCusto(jsonMaterial.has("custo") ? new Double(jsonMaterial.getString("custo")) : null);
                                    material.setUsuarioAlteracao(jsonMaterial.has("usuario_alteracao") ? jsonMaterial.getInt("usuario_alteracao") : null);
                                    material.setUsuarioCadastro(jsonMaterial.has("usuario_cadastro") ? jsonMaterial.getInt("usuario_cadastro") : null);
                                    material.setDataAlteracao(jsonMaterial.has("data_alteracao") ? new java.util.Date(format.parse(jsonMaterial.getString("data_alteracao")).getTime()) : null);
                                    material.setDataCadastro(jsonMaterial.has("data_cadastro") ? new java.sql.Date(format.parse(jsonMaterial.getString("data_cadastro")).getTime()) : null);
                                    materialService.salvarOuAtualizar(material);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("MATERIAL", String.valueOf(jsonMaterial.getInt("id_material")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_material");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_material");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }


                PROGRESSO = 5;
                atualizaBarraProgresso();
                /**/
                //Sincronismo MATERIAL IDENTIFICADO
                atualizaTextoTabela("MATERIAL IDENTIFICADO");

                //Busca lotes de 10 mil materiais identificados.
                Integer ultimoIdMaterial = 0;
                Integer tamanhoArrayMaterial = 0;
                Boolean exitMaterial = false;
                result = null;
                if (isInicial) {
                    materialIdentificadoService.deletarRegistrosNaoSinc();
                }
                do {
                    if (internetUtil.isInternetAtiva() && exitMaterial == false) {

                        if (isInicial) {
                            inseriDataHoraSinc("tb_material_identificado");
                            inputStream = Util.realizaRequisicaoMaterial("buscaMaterialIdentificado", null, null, idsCliente, null,5000,ultimoIdMaterial);
                        } else {
                            String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_material_identificado");
                            inputStream = Util.realizaRequisicao("buscaMaterialIdentificado", data, confWs.getIdUsuario(), idsCliente, null);
                            exitMaterial = true;
                        }

                        result = Util.convertInputStreamToString(inputStream);

                        if (result != null && !result.isEmpty() && !result.equals("[]")){

                            JSONObject jsonMaterialIdentificado = null;

                            JSONArray jsonArray = new JSONArray(result);

                            InputStream inputStreamOS = Util.realizaRequisicao("getDataHora", null, null, null, null);
                            String resultData = Util.convertInputStreamToString(inputStreamOS);
                            Date dataHoraInsert = new java.util.Date(format.parse(resultData).getTime());

                            tamanhoArrayMaterial += jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!this.isCancelled()) {
                                    atualizaTextoQtde(contador + "/" + tamanhoArrayMaterial);
                                    try {
                                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                        //java.sql.Date data = new java.sql.Date(format.parse(jsonTipo.getString("data_alteracao")).getTime());
                                        jsonMaterialIdentificado = (JSONObject) jsonArray.get(i);
                                        MaterialIdentificado materialIdentificado = new MaterialIdentificado();
                                        materialIdentificado.setIdMaterialIdentificado(jsonMaterialIdentificado.getString("id_material_identificado"));
                                        materialIdentificado.setIdPonto(jsonMaterialIdentificado.has("id_ponto") ? jsonMaterialIdentificado.getString("id_ponto") : null);
                                        materialIdentificado.setIdOs(jsonMaterialIdentificado.has("id_os") ? jsonMaterialIdentificado.getString("id_os") : null);

                                        if (jsonMaterialIdentificado.has("id_material")) {
                                            materialIdentificado.setMaterial(new Material(jsonMaterialIdentificado.getInt("id_material")));
                                        }
                                        materialIdentificado.setNumSerie(jsonMaterialIdentificado.has("num_serie") ? jsonMaterialIdentificado.getString("num_serie") : null);
                                        materialIdentificado.setCusto(jsonMaterialIdentificado.has("custo") ? jsonMaterialIdentificado.getDouble("custo") : null);
                                        materialIdentificado.setPosicaoInstalacao(jsonMaterialIdentificado.has("posicao_instalacao") ? jsonMaterialIdentificado.getInt("posicao_instalacao") : null);

                                        materialIdentificado.setDataAlteracao(jsonMaterialIdentificado.has("data_retirada") ? new java.util.Date(format.parse(jsonMaterialIdentificado.getString("data_retirada")).getTime()) : null);
                                        materialIdentificado.setUsuarioAlteracao(jsonMaterialIdentificado.has("usuario_alteracao") ? jsonMaterialIdentificado.getInt("usuario_alteracao") : null);
                                        materialIdentificado.setUsuarioCadastro(jsonMaterialIdentificado.has("usuario_cadastro") ? jsonMaterialIdentificado.getInt("usuario_cadastro") : null);
                                        materialIdentificado.setDataAlteracao(jsonMaterialIdentificado.has("data_alteracao") ? new java.util.Date(format.parse(jsonMaterialIdentificado.getString("data_alteracao")).getTime()) : null);
                                        materialIdentificado.setDataCadastro(jsonMaterialIdentificado.has("data_cadastro") ? new java.sql.Date(format.parse(jsonMaterialIdentificado.getString("data_cadastro")).getTime()) : null);
                                        materialIdentificado.setIdCliente(jsonMaterialIdentificado.has("id_cliente") ? jsonMaterialIdentificado.getInt("id_material") : null);
                                        materialIdentificado.setClassificacao(1); // 0 - Do ponto; 1 - Removido; 2 - Inserido;
                                        materialIdentificado.setCriadoWeb(true);


                                        materialIdentificado.setDataSincronizacao(dataHoraInsert);
                                        materialIdentificadoService.salvarOuAtualizar(materialIdentificado, false);
                                        ultimoIdMaterial = jsonMaterialIdentificado.getInt("id_sinc");
                                        ++contador;
                                    } catch (Exception e) {
                                        gravarMSGErro("MATERIAL_IDENTIFICADO", String.valueOf(jsonMaterialIdentificado.getString("id_material_identificado")));
                                        e.printStackTrace();
                                        gravarErro(e);
                                    }
                                } else {
                                    apagarTabelaSincronismo("tb_material_identificado");
                                    return null;
                                }
                            }
                            if (!isInicial) {
                                atualizaDataHoraSinc("tb_material_identificado");
                            }
                        }
                    } else {
                        return ERRO_SEM_CONEXAO_INTERNET;
                    }
                }while (result != null && !result.isEmpty() && !result.equals("[]") && exitMaterial == false );
                result = null;

                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo TIPO POSTE
                atualizaTextoTabela("TIPO POSTE");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        tipoPosteService.deletarTudo();
                        inseriDataHoraSinc("tb_tipo_poste");
                        inputStream = Util.realizaRequisicao("buscaTipoPoste", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_tipo_poste");
                        inputStream = Util.realizaRequisicao("buscaTipoPoste", data, confWs.getIdUsuario(), null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonTipoPoste = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonTipoPoste = (JSONObject) jsonArray.get(i);
                                    TipoPoste tipoPoste = new TipoPoste();
                                    tipoPoste.setId(jsonTipoPoste.getInt("id_tipo_poste"));
                                    tipoPoste.setDescricao(jsonTipoPoste.getString("descricao"));
                                    tipoPoste.setUsuarioAlteracao(jsonTipoPoste.has("usuario_alteracao") ? jsonTipoPoste.getInt("usuario_alteracao") : null);
                                    tipoPoste.setUsuarioCadastro(jsonTipoPoste.has("usuario_cadastro") ? jsonTipoPoste.getInt("usuario_cadastro") : null);
                                    tipoPoste.setDataAlteracao(jsonTipoPoste.has("data_alteracao") ? new java.util.Date(format.parse(jsonTipoPoste.getString("data_alteracao")).getTime()) : null);
                                    tipoPoste.setDataCadastro(jsonTipoPoste.has("data_cadastro") ? new java.sql.Date(format.parse(jsonTipoPoste.getString("data_cadastro")).getTime()) : null);

                                    tipoPosteService.salvarOuAtualizar(tipoPoste);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("TIPO_POSTE", String.valueOf(jsonTipoPoste.getInt("id_tipo_poste")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_tipo_poste");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_tipo_poste");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                PROGRESSO = 5;
                atualizaBarraProgresso();

                //Sincronismo SITUAÇÃO
                atualizaTextoTabela("SITUAÇÃO");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        situacaoService.deletarTudo();
                        inseriDataHoraSinc("tb_situacao");
                        inputStream = Util.realizaRequisicao("buscaSituacao", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_situacao");
                        inputStream = Util.realizaRequisicao("buscaSituacao", data, confWs.getIdUsuario(), null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonSituacao = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonSituacao = (JSONObject) jsonArray.get(i);
                                    Situacao situacao = new Situacao();
                                    situacao.setId(jsonSituacao.getString("id_situacao"));
                                    situacao.setDescricao(jsonSituacao.getString("descricao"));
                                    situacao.setTipo(jsonSituacao.has("tipo") ? jsonSituacao.getInt("tipo") : null);

                                    if (jsonSituacao.has("flg_ativo")) {
                                        if (jsonSituacao.getBoolean("flg_ativo") == false) {
                                            situacao.setFlgAtivo(0);
                                        } else {
                                            situacao.setFlgAtivo(1);
                                        }
                                    }

                                    // situacao.setFlgAtivo(jsonSituacao.getInt("flg_ativo"));
                                    situacao.setUsuarioAlteracao(jsonSituacao.has("usuario_alteracao") ? jsonSituacao.getInt("usuario_alteracao") : null);
                                    situacao.setUsuarioCadastro(jsonSituacao.has("usuario_cadastro") ? jsonSituacao.getInt("usuario_cadastro") : null);
                                    situacao.setDataAlteracao(jsonSituacao.has("data_alteracao") ? new java.util.Date(format.parse(jsonSituacao.getString("data_alteracao")).getTime()) : null);
                                    situacao.setDataCadastro(jsonSituacao.has("data_cadastro") ? new java.sql.Date(format.parse(jsonSituacao.getString("data_cadastro")).getTime()) : null);
                                    situacao.setFlgExigeMotivo(jsonSituacao.has("flg_exige_motivo") ? jsonSituacao.getBoolean("flg_exige_motivo") : null);
                                    situacao.setFluxoStatus(jsonSituacao.has("fluxo_status") ? jsonSituacao.getString("fluxo_status") : null);
                                    situacaoService.salvarOuAtualizar(situacao);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("SITUAÇÃO", String.valueOf(jsonSituacao.getString("id_situacao")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_situacao");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_situacao");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                PROGRESSO = 5;
                atualizaBarraProgresso();
                String listaIdOsUpdateEmCampo = "";
                String listaIdOs = "";
                //Sincronismo OS
                atualizaTextoTabela("ORDEM SERVIÇO");
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        osService.deletarRegistrosNaoSinc();
                        inseriDataHoraSinc("tb_ordem_servico");
                        inputStream = Util.realizaRequisicao("buscaOS", null, confWs.getIdUsuario(),null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_ordem_servico");
                        inputStream = Util.realizaRequisicao("buscaOS", data, confWs.getIdUsuario(),null , null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {

                        JSONObject jsonOS = null;
                        JSONArray jsonArray = new JSONArray(result);

                        InputStream inputStreamOS = Util.realizaRequisicao("getDataHora", null, null, null, null);
                        String resultData = Util.convertInputStreamToString(inputStreamOS);
                        Date dataHoraInsert = new java.util.Date(format.parse(resultData).getTime());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonOS = (JSONObject) jsonArray.get(i);
                                    OrdemServico os = new OrdemServico();

                                    os.setId(jsonOS.getString("id_os"));
                                    os.setObs(jsonOS.has("obs") ? jsonOS.getString("obs") : null);
                                    os.setTipoCesto(jsonOS.has("tp_cesto") ? TipoVeiculo.getTipoVeiculo(jsonOS.getInt("tp_cesto")) : null);
                                    os.setPrazoAtendimento(jsonOS.has("prazo_atendimento") ? jsonOS.getInt("prazo_atendimento") : null);
                                    os.setLogradouro(jsonOS.has("logradouro") ? jsonOS.getString("logradouro") : null);
                                    os.setPrioridade(jsonOS.has("prioridade") ? Prioridade.getPrioridade(jsonOS.getInt("prioridade")) : null);
                                    os.setObsCampo(jsonOS.has("obs_campo") ? jsonOS.getString("obs_campo") : null);
                                    os.setNumLogradouro(jsonOS.has("num_logradouro") ? jsonOS.getString("num_logradouro") : null);
                                    os.setBairro(jsonOS.has("bairro") ? jsonOS.getString("bairro") : null);
                                    os.setDataRetorno(jsonOS.has("data_retorno") ? new java.util.Date(format.parse(jsonOS.getString("data_retorno")).getTime()) : null);
                                    os.setDataCadastro(jsonOS.has("data_cadastro") ? new java.util.Date(format.parse(jsonOS.getString("data_cadastro")).getTime()) : null);
                                    os.setDataAlteracao(jsonOS.has("data_alteracao") ? new java.util.Date(format.parse(jsonOS.getString("data_alteracao")).getTime()) : null);
                                    os.setUsuarioCadastro(jsonOS.has("usuario_cadastro") ? jsonOS.getInt("usuario_cadastro") : null);
                                    os.setUsuarioAlteracao(jsonOS.has("usuario_alteracao") ? jsonOS.getInt("usuario_alteracao") : null);
                                    //os.setDataSincronismo(jsonOS.has("data_sincronismo") ? new java.sql.Date(format.parse(jsonOS.getString("data_sincronismo")).getTime()) : null);
                                    os.setPontoReferencia(jsonOS.has("ponto_referencia") ? jsonOS.getString("ponto_referencia") : null);
                                    os.setDataDespacho(jsonOS.has("data_despacho") ? new java.sql.Date(format.parse(jsonOS.getString("data_despacho")).getTime()) : null);
                                    os.setNumOs(jsonOS.has("numero_os") ? jsonOS.getInt("numero_os") : null);
                                    os.setDataAtribuicao(jsonOS.has("data_atribuicao") ? new java.sql.Date(format.parse(jsonOS.getString("data_atribuicao")).getTime()) : null);
                                    os.setUsuarioAtribuicao(jsonOS.has("usuario_atribuicao") ? jsonOS.getInt("usuario_atribuicao") : null);
                                    os.setMotivoAbertura(jsonOS.has("id_motivo") ? MotivoAbertura.getPrioridade(jsonOS.getInt("id_motivo")) : null);
                                    os.setUsuarioExecucao(jsonOS.has("usuario_execucao") ? jsonOS.getInt("usuario_execucao") : null);
                                    os.setIdChamado(jsonOS.has("id_chamado") ? jsonOS.getInt("id_chamado") : null);


                                    os.setNomeChamado(jsonOS.has("nome_chamado") ? jsonOS.getString("nome_chamado") : null);
                                    os.setTelChamado(jsonOS.has("tel_chamado") ? jsonOS.getString("tel_chamado") : null);
                                    os.setQtdChamado(jsonOS.has("qtd_chamado") ? jsonOS.getString("qtd_chamado") : null);
                                    os.setProtocoloChamado(jsonOS.has("protocolo_chamado") ? jsonOS.getString("protocolo_chamado") : null);


                                    os.setDataSincronismo(dataHoraInsert);

                                    if (jsonOS.has("id_situacao")) {
                                        os.setSituacao(new Situacao(jsonOS.getString("id_situacao")));
                                        if (jsonOS.getString("id_situacao").equals(STATUS_ATRIBUIDA)) {
                                            listaIdOsUpdateEmCampo += "'" + os.getId() + "',";
                                        }
                                    }

                                    if (jsonOS.has("id_municipio")) {
                                        os.setMunicipio(new Municipio(jsonOS.getInt("id_municipio")));
                                    }
                                    os.setIdCliente(jsonOS.has("id_cliente") ? jsonOS.getInt("id_cliente") : null);
                                    listaIdOs += "'" + os.getId() + "',";
                                    osService.salvarOuAtualizar(os);


                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("ORDEM_SERVIÇO", String.valueOf(jsonOS.getString("id_os")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_ordem_servico");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_ordem_servico");
                        }
                    }

                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                //ATUALIZA A O.S COMO EM CAMPO NA TABELA TROCA_STATUS
                //TODAS AS OS BAIXADAS E INSERIDAS E COM O STATUS ATRIBUIDA SERÃO ATUALIZAS COM O ID_SITUACAO EM_CAMPO.
                if (internetUtil.isInternetAtiva() && listaIdOsUpdateEmCampo.length() > 0 && listaIdOsUpdateEmCampo.charAt(listaIdOsUpdateEmCampo.length() - 1) == ',') {
                    listaIdOsUpdateEmCampo = listaIdOsUpdateEmCampo.substring(0, listaIdOsUpdateEmCampo.length() - 1);
                    Util.realizaRequisicao("updateSituacaoOSEmCampo", null, confWs.getIdUsuario(), null, listaIdOsUpdateEmCampo);
                }


                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo Opcao Pergunta
                atualizaTextoTabela("OPÇÃO PERGUNTA");
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        opcaoPerguntaService.deletarTudo();
                        inseriDataHoraSinc("tb_opcao_pergunta");
                        inputStream = Util.realizaRequisicao("getOpcaoPergunta", null, confWs.getIdUsuario(), null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_opcao_pergunta");
                        inputStream = Util.realizaRequisicao("getOpcaoPergunta", data, confWs.getIdUsuario(), null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonOpcao = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonOpcao = (JSONObject) jsonArray.get(i);
                                    OpcaoPergunta opcao = new OpcaoPergunta();
                                    opcao.setId(jsonOpcao.getInt("id"));
                                    opcao.setDescricao(jsonOpcao.getString("descricao"));
                                    opcao.setPergunta(new Pergunta(jsonOpcao.getInt("id_pergunta")));
                                    opcaoPerguntaService.salvarOuAtualizar(opcao);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("OPÇÃO_PERGUNTA", String.valueOf(jsonOpcao.getInt("id")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_opcao_pergunta");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_opcao_pergunta");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }


                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo Perguntas
                atualizaTextoTabela("PERGUNTAS");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        perguntaService.deletarTudo();
                        inseriDataHoraSinc("tb_pergunta");
                        inputStream = Util.realizaRequisicao("buscaPerguntas", null, null, idsCliente, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_pergunta");
                        inputStream = Util.realizaRequisicao("buscaPerguntas", data, null, idsCliente, null);
                    }

                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {

                        JSONObject jsonPergunta = null;
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {

                                    jsonPergunta = (JSONObject) jsonArray.get(i);
                                    Pergunta pergunta = new Pergunta();
                                    pergunta.setId(jsonPergunta.getInt("id_pergunta"));
                                    pergunta.setDescricao(jsonPergunta.getString("descricao"));
                                    pergunta.setTipoPergunta(jsonPergunta.getInt("tipo_pergunta"));
                                    pergunta.setOrdem(jsonPergunta.has("ordem") ? jsonPergunta.getInt("ordem") : null);
                                    pergunta.setAtiva(true);
                                    pergunta.setDataCadastro(jsonPergunta.has("data_cadastro") ? new java.util.Date(format.parse(jsonPergunta.getString("data_cadastro")).getTime()) : null);
                                    pergunta.setDataAlteracao(jsonPergunta.has("data_alteracao") ? new java.util.Date(format.parse(jsonPergunta.getString("data_alteracao")).getTime()) : null);
                                    pergunta.setUsuarioCadastro(jsonPergunta.getInt("usuario_cadastro"));
                                    pergunta.setUsuarioAlteracao(jsonPergunta.has("usuario_alteracao") ? jsonPergunta.getInt("usuario_alteracao") : null);

                                    perguntaService.salvarOuAtualizar(pergunta);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("PERGUNTA", String.valueOf(jsonPergunta.getInt("id_pergunta")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_pergunta");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_pergunta");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }


                //90
                PROGRESSO = 2;
                atualizaBarraProgresso();
                //Sincronismo GRUPO SERVICO
                atualizaTextoTabela("GRUPO SERVIÇO");
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        grupoServicoService.deletarTudo();
                        inseriDataHoraSinc("tb_grupo_servico");
                        inputStream = Util.realizaRequisicao("getGrupoServico", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_grupo_servico");
                        inputStream = Util.realizaRequisicao("getGrupoServico", data, confWs.getIdUsuario(), null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonGrupoServico = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonGrupoServico = (JSONObject) jsonArray.get(i);
                                    GrupoServico grupoServico = new GrupoServico();
                                    grupoServico.setId(jsonGrupoServico.getInt("id_grupo_servico"));
                                    grupoServico.setDescricao(jsonGrupoServico.getString("descricao"));
                                    grupoServico.setUsuarioAlteracao(jsonGrupoServico.has("usuario_alteracao") ? jsonGrupoServico.getInt("usuario_alteracao") : null);
                                    grupoServico.setUsuarioCadastro(jsonGrupoServico.has("usuario_cadastro") ? jsonGrupoServico.getInt("usuario_cadastro") : null);
                                    grupoServico.setDataAlteracao(jsonGrupoServico.has("data_alteracao") ? new java.util.Date(format.parse(jsonGrupoServico.getString("data_alteracao")).getTime()) : null);
                                    grupoServico.setDataCadastro(jsonGrupoServico.has("data_cadastro") ? new java.sql.Date(format.parse(jsonGrupoServico.getString("data_cadastro")).getTime()) : null);

                                    grupoServicoService.salvarOuAtualizar(grupoServico);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("GRUPO_SERVIÇO", String.valueOf(jsonGrupoServico.getInt("id_grupo_servico")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_grupo_servico");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_grupo_servico");
                        }
                    }

                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }
                //Busca data e hora


                PROGRESSO = 2;
                atualizaBarraProgresso();
                /**/
                //Sincronismo SERVICO
                atualizaTextoTabela("SERVIÇO");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        servicoService.deletarRegistrosNaoSinc();
                        inseriDataHoraSinc("tb_servico");
                        inputStream = Util.realizaRequisicao("getServico", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_servico");
                        inputStream = Util.realizaRequisicao("getServico", data, null, null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {


                        JSONObject jsonServico = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonServico = (JSONObject) jsonArray.get(i);
                                    Servico servico = new Servico();
                                    servico.setId(jsonServico.getInt("id_servico"));
                                    servico.setDescricao(jsonServico.getString("descricao"));
                                    servico.setGrupo(new GrupoServico(jsonServico.getInt("id_grupo_servico")));
                                    servico.setDataAlteracao(jsonServico.has("data_alteracao") ? new java.util.Date(format.parse(jsonServico.getString("data_alteracao")).getTime()) : new Date());
                                    servico.setDataCadastro(jsonServico.has("data_cadastro") ? new java.sql.Date(format.parse(jsonServico.getString("data_cadastro")).getTime()) : new Date());
                                    servico.setUsuarioCadastro(1);
                                    servico.setDataCadastro(new Date());
                                    servicoService.salvarOuAtualizar(servico);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("SERVIÇO", String.valueOf(jsonServico.getInt("id_servico")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_servico");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_servico");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }


                //Busca OS_PONTO E OS_SERVICO
                if (listaIdOs.length() > 0 && listaIdOs.charAt(listaIdOs.length() - 1) == ',') {
                    listaIdOs = listaIdOs.substring(0, listaIdOs.length() - 1);

                    //Busca OS_PONTO
                    contador = 1;
                    atualizaTextoTabela("OS PONTO");
                    if (internetUtil.isInternetAtiva()) {
                        if (isInicial) {
                            ordemServicoPontoService.deletarRegistrosNaoSinc();
                            inseriDataHoraSinc("tb_ordem_ponto");
                            inputStream = Util.realizaRequisicao("getOsPonto", null, confWs.getIdUsuario(), null, listaIdOs);
                        } else {
                            String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_ordem_ponto");
                            inputStream = Util.realizaRequisicao("getOsPonto", data, confWs.getIdUsuario(), null, listaIdOs);
                        }


                        result = Util.convertInputStreamToString(inputStream);

                        if (result != null && !result.isEmpty()) {
                            JSONObject jsonOsPonto = null;

                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!this.isCancelled()) {
                                    atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                    try {
                                        jsonOsPonto = (JSONObject) jsonArray.get(i);
                                        OrdemServicoPonto osPonto = new OrdemServicoPonto();
                                        osPonto.setId(jsonOsPonto.getString("id_os_ponto"));
                                        osPonto.setOs(new OrdemServico(jsonOsPonto.getString("id_os")));
                                        osPonto.setPonto(new Ponto(jsonOsPonto.getString("id_ponto")));
                                        osPonto.setSituacao(jsonOsPonto.has("situacao") ? jsonOsPonto.getInt("situacao") : 0);

                                        ordemServicoPontoService.salvarOuAtualizar(osPonto);
                                        ++contador;
                                    } catch (Exception e) {
                                        gravarMSGErro("OS_PONTO", String.valueOf(jsonOsPonto.getString("id_os_ponto")));
                                        e.printStackTrace();
                                        gravarErro(e);
                                    }
                                } else {
                                    apagarTabelaSincronismo("tb_ordem_ponto");
                                    return null;
                                }

                            }

                            if (!isInicial) {
                                atualizaDataHoraSinc("tb_ordem_ponto");
                            }
                        }
                    } else {
                        return ERRO_SEM_CONEXAO_INTERNET;
                    }

                    contador = 1;
                    //Busca OS_SERVICO

                    atualizaTextoTabela("OS SERVICO");
                    if (internetUtil.isInternetAtiva()) {
                        if (isInicial) {
                            ordemServicoServicoService.deletarRegistrosNaoSinc();
                            inseriDataHoraSinc("tb_os_servico");
                            inputStream = Util.realizaRequisicao("getOsServico", null, confWs.getIdUsuario(), null, listaIdOs);
                        } else {
                            String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_os_servico");
                            inputStream = Util.realizaRequisicao("getOsServico", data, confWs.getIdUsuario(), null, listaIdOs);
                        }


                        result = Util.convertInputStreamToString(inputStream);

                        if (result != null && !result.isEmpty()) {


                            JSONObject jsonOsServico = null;


                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (!this.isCancelled()) {
                                    atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                    try {
                                        jsonOsServico = (JSONObject) jsonArray.get(i);
                                        OrdemServicoServico osServico = new OrdemServicoServico();
                                        osServico.setId(jsonOsServico.getString("id_os_servico"));
                                        osServico.setOs(new OrdemServico(jsonOsServico.getString("id_os")));
                                        osServico.setPonto(jsonOsServico.has("id_ponto") ? new Ponto(jsonOsServico.getString("id_ponto")) : null);
                                        osServico.setServico(new Servico(jsonOsServico.getInt("id_servico")));
                                        osServico.setMaterialIdentificado(jsonOsServico.has("id_material_identificado") ? new MaterialIdentificado(jsonOsServico.getString("id_material_identificado")) : null);

                                        ordemServicoServicoService.salvarOuAtualizar(osServico);
                                        ++contador;
                                    } catch (Exception e) {
                                        gravarMSGErro("OS_SERVIÇO", String.valueOf(jsonOsServico.getString("id_os_servico")));
                                        e.printStackTrace();
                                        gravarErro(e);
                                    }
                                } else {
                                    apagarTabelaSincronismo("tb_os_servico");
                                    return null;
                                }

                            }

                            if (!isInicial) {
                                atualizaDataHoraSinc("tb_os_servico");
                            }
                        }
                    } else {
                        return ERRO_SEM_CONEXAO_INTERNET;
                    }
                }


                PROGRESSO = 5;
                atualizaBarraProgresso();
                //Sincronismo ALIMENTADOR
                atualizaTextoTabela("ALIMENTADOR");
                if (internetUtil.isInternetAtiva()) {


                    if (isInicial) {
                        alimentadorService.deletarTudo();
                        inseriDataHoraSinc("tb_alimentador");
                        inputStream = Util.realizaRequisicao("getAlimentador", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_alimentador");
                        inputStream = Util.realizaRequisicao("getAlimentador", data, confWs.getIdUsuario(), null, null);
                    }

                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {

                        JSONObject jsonAlimentador = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonAlimentador = (JSONObject) jsonArray.get(i);
                                    Alimentador alimentador = new Alimentador();
                                    alimentador.setId(jsonAlimentador.getInt("id_alimentador"));
                                    alimentador.setDescricao(jsonAlimentador.getString("descricao"));
                                    alimentador.setDataAlteracao(jsonAlimentador.has("data_alteracao") ? new java.util.Date(format.parse(jsonAlimentador.getString("data_alteracao")).getTime()) : new Date());
                                    alimentador.setDataCadastro(jsonAlimentador.has("data_cadastro") ? new java.sql.Date(format.parse(jsonAlimentador.getString("data_cadastro")).getTime()) : new Date());
                                    alimentador.setUsuarioCadastro(1);
                                    alimentador.setDataCadastro(new Date());
                                    alimentadorService.salvarOuAtualizar(alimentador);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("ALIMENTADOR", String.valueOf(jsonAlimentador.getInt("id_alimentador")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_alimentador");
                                return null;
                            }

                        }

                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_alimentador");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }

                atualizaTextoTabela("MOTIVO TROCA");
                contador = 1;
                if (internetUtil.isInternetAtiva()) {
                    if (isInicial) {
                        motivoTrocaService.deletarTudo();
                        inseriDataHoraSinc("tb_motivo_troca");
                        inputStream = Util.realizaRequisicao("getMotivoTroca", null, null, null, null);
                    } else {
                        String data = sincronismoService.buscarDataFormatadaUltimoSincronismo("tb_motivo_troca");
                        inputStream = Util.realizaRequisicao("getMotivoTroca", data, confWs.getIdUsuario(), null, null);
                    }


                    result = Util.convertInputStreamToString(inputStream);

                    if (result != null && !result.isEmpty()) {

                        JSONObject jsonMotivoTroca = null;


                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (!this.isCancelled()) {
                                atualizaTextoQtde(contador + "/" + (jsonArray.length()));
                                try {
                                    jsonMotivoTroca = (JSONObject) jsonArray.get(i);
                                    MotivoTroca motivoTroca = new MotivoTroca();
                                    motivoTroca.setId(jsonMotivoTroca.getInt("id_motivo_troca"));
                                    motivoTroca.setDescricao(jsonMotivoTroca.getString("descricao"));
                                    motivoTroca.setDataCadastro(jsonMotivoTroca.has("data_cadastro") ? new java.sql.Date(format.parse(jsonMotivoTroca.getString("data_cadastro")).getTime()) : new Date());
                                    motivoTroca.setUsuarioCadastro(jsonMotivoTroca.getInt("usuario_cadastro"));
                                    motivoTrocaService.salvarOuAtualizar(motivoTroca);
                                    ++contador;
                                } catch (Exception e) {
                                    gravarMSGErro("MOTIVO_TROCA", String.valueOf(jsonMotivoTroca.getInt("id_motivo_troca")));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                apagarTabelaSincronismo("tb_motivo_troca");
                                return null;
                            }

                        }
                        if (!isInicial) {
                            atualizaDataHoraSinc("tb_motivo_troca");
                        }
                    }
                } else {
                    return ERRO_SEM_CONEXAO_INTERNET;
                }


                PROGRESSO = 1;
                atualizaBarraProgresso();
                enableButton();
                atualizaTextoQtde("0");
                atualizaTextoTabela("");

                return SINCRONISMO_CONCLUIDO;
            } catch (HttpHostConnectException e) {
                e.printStackTrace();
                return ERRO_SEM_CONEXAO_INTERNET;
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
                return ERRO;
            } finally {
                //Enviar log
                try {
                    File file_arquivo = new File(ARQUIVO_LOG);
                    if (file_arquivo != null && file_arquivo.length() != 0) {
                        byte[] bytesFile = new byte[(int) file_arquivo.length()];
                        FileInputStream fileInputStream = new FileInputStream(file_arquivo);
                        fileInputStream.read(bytesFile);
                        fileInputStream.close();

                        String tipo = Base64.encodeToString(bytesFile, Base64.DEFAULT);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("usuario", confWs.getUsuario());
                        jsonObject.accumulate("senha", confWs.getSenha());
                        jsonObject.accumulate("bytes_arquivo", tipo);

                        InputStream inputStream = Util.realizaRequisicaoSet("setLog", jsonObject);
                        String result = Util.convertInputStreamToString(inputStream);
                        if (result != null && !result.equals("") && result.equals("OK")) {
                            File file = new File(ARQUIVO_LOG);
                            file.delete();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Botão de erro
                if (msgErro != null && !msgErro.toString().isEmpty()) {
                    setBotaoErroVisile();
                    Intent intent = new Intent(this.context, LogUsuarioActivity_.class);
                    Bundle b = new Bundle();
                    b.putString("log", msgErro.toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            //post
            if (result == null) {
                //botao cancelar
            } else if (result == ERRO) {
                AlertaUtil.mensagemErro(context, "Erro no sincronismo! Contate o suporte");
            } else if (result == SINCRONISMO_CONCLUIDO) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (msgErro != null && !msgErro.toString().isEmpty()) {
                    texto.setText("Sincronismo concluído com erro(s)!");
                } else {
                    texto.setText("Sincronismo concluído!");
                }
                texto.setGravity(Gravity.CENTER_HORIZONTAL);
            } else if (result == ERRO_USUARIO_SEM_CLIENTE) {
                AlertaUtil.mensagemErro(context, "Erro! O usuário não possui cliente(s) vinculado(s). Contate o suporte!");
            } else if (result == ERRO_SEM_CONEXAO_INTERNET) {
                //Sem conexao
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Erro! ");
                builder.setMessage("Sem conexão com a Internet");
                builder.setNegativeButton("Cancelar Sincronismo.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelar();
                    }
                });
                builder.show();

            }

            super.onPostExecute(result);
        }

        public boolean upload() {
            boolean cancel = false;
            InputStream inputStream = null;
            String result = null;
            try {
                atualizaTextopPrincipal("Realizando o Upload de: ");
                atualizaTextoTabela("PONTO");
                //ENVIAR PONTO.Busca ponto sincronismo  = 0.
                List<Ponto> listaPontosNaoSincronizados = pontoService.buscarPorParametroConsulta(new PontoParametroConsulta(0));
                if (listaPontosNaoSincronizados != null && !listaPontosNaoSincronizados.isEmpty()) {

                    for (Ponto item : listaPontosNaoSincronizados) {
                        if (!this.isCancelled()) {
                            if (internetUtil.isInternetAtiva()) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.accumulate("usuario", Util.login);
                                    jsonObject.accumulate("senha", Util.senha);
                                    jsonObject.accumulate("id_ponto", item.getId());
                                    jsonObject.accumulate("id_poste", item.getPoste().getId());
                                    jsonObject.accumulate("id_municipio", item.getMunicipio().getId());
                                    jsonObject.accumulate("bairro", item.getBairro());
                                    jsonObject.accumulate("cep", item.getCep());
                                    jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());

                                    if (item.getDataCadastro() != null) {
                                        Date data = item.getDataCadastro();
                                        String dataString = format2.format(data);
                                        jsonObject.accumulate("data_cadastro", dataString);

                                    }

                                    if (item.getDataSincronismo() != null) {
                                        Date data = item.getDataSincronismo();
                                        String dataString = format2.format(data);
                                        jsonObject.accumulate("data_sincronismo", dataString);

                                    }

                                    if (item.getDataAlteracao() != null) {
                                        Date data = item.getDataAlteracao();
                                        String dataString = format2.format(data);

                                        jsonObject.accumulate("data_alteracao", dataString);
                                    }

                                    if (item.getUsuarioAlteracao() != null) {
                                        jsonObject.accumulate("usuario_alteracao", item.getUsuarioAlteracao());
                                    }

                                    if (item.getLogradouro() != null) {
                                        jsonObject.accumulate("logradouro", item.getLogradouro());
                                    }
                                    if (item.getNumLogradouro() != null) {
                                        jsonObject.accumulate("num_logradouro", item.getNumLogradouro());
                                    }

                                    if (item.getDirecao() != null && item.getDirecao().getCodigo() != null) {
                                        jsonObject.accumulate("direcao", item.getDirecao().getCodigo());
                                    }

                                    if (item.getDanificado() != null && item.getDanificado().getCodigo() != null) {
                                        jsonObject.accumulate("danificado", item.getDanificado().getCodigo());
                                    }

                                    if (item.getNumPlaquetaTransf() != null) {
                                        jsonObject.accumulate("num_plaqueta_transf", item.getNumPlaquetaTransf());
                                    }

                                    if (item.getPontoReferencia() != null) {
                                        jsonObject.accumulate("ponto_referencia", item.getPontoReferencia());
                                    }

                                    if (item.getLatitude() != null) {
                                        jsonObject.accumulate("latitude", item.getLatitude());
                                    }

                                    if (item.getLongitude() != null) {
                                        jsonObject.accumulate("longitude", item.getLongitude());
                                    }

                                    if (item.getNumTrafo() != null) {
                                        jsonObject.accumulate("num_trafo", item.getNumTrafo());
                                    }

                                    if (item.getIdCliente() != null) {
                                        jsonObject.accumulate("id_cliente", item.getIdCliente());
                                    }

                                    inputStream = Util.realizaRequisicaoSet("inseriPonto", jsonObject);
                                    result = Util.convertInputStreamToString(inputStream);
                                    if (result != null && !result.equals("") && result.equals("true")) {
                                        item.setSincronizado(1);
                                        pontoService.salvarOuAtualizar(item);
                                        atualizaTextoQtde(++contador + "/" + (listaPontosNaoSincronizados.size()));
                                    } else {
                                        System.out.print("Ponto nao atualizado");
                                    }
                                } catch (Exception e) {
                                    gravarMSGErroUpload("PONTO", String.valueOf(item.getId()));
                                    e.printStackTrace();
                                    gravarErro(e);
                                }
                            } else {
                                cancel = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }

            PROGRESSO = 5;
            atualizaBarraProgresso();

            //Sincronismo Material Identificado
            /* */
            try {
                atualizaTextoTabela("MATERIAL IDENTIFICADO");
                //ENVIAR material identificado sincronismo  = 0.
                List<MaterialIdentificado> listaMaterialNaoSincronizados = materialIdentificadoService.buscarPorParametroConsulta(new PontoParametroConsulta(0));
                if (listaMaterialNaoSincronizados != null && !listaMaterialNaoSincronizados.isEmpty()) {

                    for (MaterialIdentificado item : listaMaterialNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);

                                jsonObject.accumulate("id_material_identificado", item.getIdMaterialIdentificado());
                                jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());


                                if (item.getUsuarioAlteracao() != null) {
                                    jsonObject.accumulate("usuario_alteracao", item.getUsuarioAlteracao());
                                }

                                if (item.getMaterial() != null && item.getMaterial().getIdMaterial() != null) {
                                    jsonObject.accumulate("id_material", item.getMaterial().getIdMaterial());
                                }

                                if (item.getIdPonto() != null) {
                                    jsonObject.accumulate("id_ponto", item.getIdPonto());
                                }

                                if (item.getIdOs() != null) {
                                    jsonObject.accumulate("id_os", item.getIdOs());
                                }

                                if (item.getNumSerie() != null) {
                                    jsonObject.accumulate("num_serie", item.getNumSerie());
                                }

                                if (item.getCusto() != null) {
                                    jsonObject.accumulate("custo", item.getCusto());
                                }

                                if (item.getDataCadastro() != null) {
                                    Date data = item.getDataCadastro();
                                    String dataString = format2.format(data);
                                    jsonObject.accumulate("data_cadastro", dataString);

                                }

                                if (item.getDataAlteracao() != null) {
                                    Date data = item.getDataAlteracao();
                                    String dataString = format2.format(data);

                                    jsonObject.accumulate("data_alteracao", dataString);
                                }


                                if (item.getDataRetirada() != null) {
                                    Date data = item.getDataRetirada();
                                    String dataString = format2.format(data);
                                    jsonObject.accumulate("data_retirada", dataString);
                                }

                                if (item.getDataInstalacao() != null) {
                                    Date data = item.getDataInstalacao();
                                    String dataString = format2.format(data);
                                    jsonObject.accumulate("data_instalacao", dataString);
                                }


                                if (item.getPosicaoInstalacao() != null) {
                                    jsonObject.accumulate("posicao_instalacao", item.getPosicaoInstalacao());
                                }

                                if (item.getSituacao() != null) {
                                    jsonObject.accumulate("situacao", item.getSituacao());
                                }

                                if (item.getDataSincronizacao() != null) {
                                    Date data = item.getDataSincronizacao();
                                    String dataString = format2.format(data);
                                    jsonObject.accumulate("data_sincronismo", dataString);
                                }

                                if (item.getIdCliente() != null) {
                                    jsonObject.accumulate("id_cliente", item.getIdCliente());
                                }

                                inputStream = Util.realizaRequisicaoSet("setMaterialIdentificado", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                    item.setSincronizado(1);
                                    materialIdentificadoService.salvarOuAtualizar(item, false);
                                    atualizaTextoQtde(++contador + "/" + (listaMaterialNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("MATERIAL IDENTIFICADO", String.valueOf(item.getIdMaterialIdentificado()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }
            PROGRESSO = 5;
            atualizaBarraProgresso();
            //Sincronismo OS

            try {
                //ENVIAR Ordem de Serviço sincronismo  = 0.
                atualizaTextoTabela("ORDEM SERVIÇO");
                List<OrdemServico> listaOSNaoSincronizados = osService.buscarPorParametroConsulta(new OsParametroConsulta(0));
                if (listaOSNaoSincronizados != null && !listaOSNaoSincronizados.isEmpty()) {

                    for (OrdemServico item : listaOSNaoSincronizados) {
                        if (!this.isCancelled()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("usuario", Util.login);
                            jsonObject.accumulate("senha", Util.senha);
                            jsonObject.accumulate("id", item.getId());
                            jsonObject.accumulate("id_usuario", confWs.getIdUsuario());
                            if (item.getSituacao() != null && item.getSituacao() != null) {
                                jsonObject.accumulate("id_situacao", item.getSituacao().getId());
                            }

                            jsonObject.accumulate("id_municipio", item.getMunicipio().getId());

                            if (item.getObs() != null) {
                                jsonObject.accumulate("obs", item.getObs());
                            }


                            if (item.getTipoCesto() != null) {
                                jsonObject.accumulate("tp_cesto", item.getTipoCesto().getCodigo());
                            }

                            if (item.getPrazoAtendimento() != null) {
                                jsonObject.accumulate("prazo_atendimento", item.getPrazoAtendimento());
                            }

                            if (item.getLogradouro() != null) {
                                jsonObject.accumulate("logradouro", item.getLogradouro());
                            }

                            if (item.getPrioridade() != null && item.getPrioridade().getCodigo() != null) {
                                jsonObject.accumulate("prioridade", item.getPrioridade().getCodigo());
                            }

                            if (item.getObsCampo() != null) {
                                jsonObject.accumulate("obs_campo", item.getObsCampo());
                            }

                            if (item.getNumLogradouro() != null) {
                                jsonObject.accumulate("num_logradouro", item.getNumLogradouro());
                            }


                            if (item.getDataCadastro() != null) {
                                Date data = item.getDataCadastro();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_cadastro", dataString);

                            }

                            if (item.getDataAlteracao() != null) {
                                Date data = item.getDataAlteracao();
                                String dataString = format2.format(data);

                                jsonObject.accumulate("data_alteracao", dataString);
                            }


                            if (item.getBairro() != null) {
                                jsonObject.accumulate("bairro", item.getBairro());
                            }
                            jsonObject.accumulate("usuario_atribuicao", item.getUsuarioAtribuicao());


                            InputStream inputStreamOS = Util.realizaRequisicao("getDataHora", null, null, null, null);
                            String resultData = Util.convertInputStreamToString(inputStreamOS);

                            jsonObject.accumulate("data_retorno", resultData);

                            if (item.getDataAtribuicao() != null) {
                                Date data = item.getDataAtribuicao();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_atribuicao", dataString);
                            }

                            if (item.getDataSincronismo() != null) {
                                Date data = item.getDataSincronismo();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_sincronismo", dataString);
                            }

                            if (item.getPontoReferencia() != null) {
                                jsonObject.accumulate("ponto_referencia", item.getPontoReferencia());
                            }

                            if (item.getDataDespacho() != null) {
                                Date data = item.getDataDespacho();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_despacho", dataString);
                            }

                            if (item.getNumOs() != null) {
                                jsonObject.accumulate("num_os", item.getNumOs());
                            }
                            if (item.getCep() != null) {
                                jsonObject.accumulate("cep", item.getCep());
                            }

                            if (item.getMotivoAbertura() != null && item.getMotivoAbertura().getCodigo() != null) {
                                jsonObject.accumulate("id_motivo", item.getMotivoAbertura().getCodigo());
                            }

                            if (item.getMotivoEncerramento() != null && item.getMotivoEncerramento().getId() != null) {
                                jsonObject.accumulate("id_motivo_encerramento", item.getMotivoEncerramento().getId());
                            }

                            if (item.getUsuarioExecucao() != null) {
                                jsonObject.accumulate("usuario_execucao", item.getUsuarioExecucao());
                            }

                            if (item.getIdCliente() != null) {
                                jsonObject.accumulate("id_cliente", item.getIdCliente());
                            }

                            if (item.getUsuarioAlteracao() != null) {
                                jsonObject.accumulate("usuario_alteracao", item.getUsuarioAlteracao());
                            }

                            if(item.getIdChamado() != null){
                                jsonObject.accumulate("id_chamado",item.getIdChamado());
                            }

                            jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());

                            try {
                                inputStream = Util.realizaRequisicaoSet("inseriOrdemServico", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals(RETORNO_TRUE)) {
                                    item.setSincronizado(1);
                                    osService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaOSNaoSincronizados.size()));
                                } else if (result != null && !result.equals("") && result.equals(RETORNO_OS_ATUALIZADA)){
                                    msgErro.append("Atenção! A Ordem de Serviço: ").append(item.getNumOs()).append(" já foi alterada no módulo WEB!").append("\n");
                                    item.setSincronizado(1);
                                    osService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaOSNaoSincronizados.size()));
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("ORDEM SERVIÇO", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }


            //Sincronismo SERVICO
            try {
                //ENVIAR SERVICO.
                atualizaTextoTabela("SERVICO");
                List<Servico> listaServicoNaoSincronizados = servicoService.buscarPorParametroConsulta(new ServicoParametroConsulta(0));
                if (listaServicoNaoSincronizados != null && !listaServicoNaoSincronizados.isEmpty()) {

                    for (Servico item : listaServicoNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);


                                jsonObject.accumulate("id_servico", item.getId());
                                jsonObject.accumulate("descricao", item.getDescricao());
                                jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());
                                jsonObject.accumulate("grupo", item.getGrupo().getId());
                                Date data = item.getDataCadastro();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_cadastro", dataString);
                                inputStream = Util.realizaRequisicaoSet("setServico", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                    item.setSincronizado(1);
                                    servicoService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaServicoNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("SERVIÇO", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }


            PROGRESSO = 2;
            atualizaBarraProgresso();

            //Sincronismo OS_PONTO
            try {
                //ENVIAR OS_PONTO.
                atualizaTextoTabela("OS PONTO");
                List<OrdemServicoPonto> listaOsPontoNaoSincronizados = ordemServicoPontoService.listarNaoSincronizados();
                if (listaOsPontoNaoSincronizados != null && !listaOsPontoNaoSincronizados.isEmpty()) {

                    for (OrdemServicoPonto item : listaOsPontoNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);

                                jsonObject.accumulate("id_osPonto", item.getId());
                                jsonObject.accumulate("id_os", item.getOs().getId());
                                jsonObject.accumulate("id_ponto", item.getPonto().getId());
                                jsonObject.accumulate("situacao", item.getSituacao());

                                inputStream = Util.realizaRequisicaoSet("setOsPonto", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                   if(item.getOs() != null && item.getOs().getId() != null){
                                       item.setSincronizado(1);
                                       ordemServicoPontoService.salvarOuAtualizar(item);
                                   }else{
                                       ordemServicoPontoService.deletar(item);
                                   }

                                    atualizaTextoQtde(++contador + "/" + (listaOsPontoNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("OS PONTO", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }


            contador = 0;

            //Sincronismo OS_SERVICO
            try {
                //ENVIAR OS_SERVICO.
                atualizaTextoTabela("OS SERVICO");
                List<OrdemServicoServico> listaOsServicoNaoSincronizados = ordemServicoServicoService.listarNaoSincronizados();
                if (listaOsServicoNaoSincronizados != null && !listaOsServicoNaoSincronizados.isEmpty()) {

                    for (OrdemServicoServico item : listaOsServicoNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);

                                jsonObject.accumulate("id_osServico", item.getId());

                                if (item.getOs() != null && item.getOs().getId() != null) {
                                    jsonObject.accumulate("id_os", item.getOs().getId());
                                }


                                if (item.getServico() != null && item.getServico().getId() != null) {
                                    jsonObject.accumulate("id_servico", item.getServico().getId());
                                }


                                if (item.getMaterialIdentificado() != null && item.getMaterialIdentificado().getIdMaterialIdentificado() != null) {
                                    jsonObject.accumulate("id_material_identificado", item.getMaterialIdentificado().getIdMaterialIdentificado());
                                }


                                if (item.getPonto() != null && item.getPonto().getId() != null) {
                                    jsonObject.accumulate("id_ponto", item.getPonto().getId());
                                }


                                inputStream = Util.realizaRequisicaoSet("setOsServico", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                    item.setSincronizado(1);
                                    ordemServicoServicoService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaOsServicoNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("OS SERVIÇO", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }


            PROGRESSO = 3;
            atualizaBarraProgresso();

            //Sincronismo respostas
            try {
                //ENVIAR Respostas.
                atualizaTextoTabela("RESPOSTA");
                List<Resposta> listaRespostaNaoSincronizados = respostaService.buscarPorParametroConsulta(new PontoParametroConsulta(0));
                if (listaRespostaNaoSincronizados != null && !listaRespostaNaoSincronizados.isEmpty()) {

                    for (Resposta item : listaRespostaNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);

                                jsonObject.accumulate("id_resposta", item.getId());
                                jsonObject.accumulate("id_pergunta", item.getPergunta().getId());

                                if (item.getOpcaoPergunta() != null && item.getOpcaoPergunta().getId() != null) {
                                    jsonObject.accumulate("id_opcao_pergunta", item.getOpcaoPergunta().getId());
                                }

                                if (item.getPonto() != null && item.getPonto().getId() != null) {
                                    jsonObject.accumulate("id_ponto", item.getPonto().getId());
                                }


                                if (item.getResposta() != null) {
                                    jsonObject.accumulate("resposta", item.getResposta());
                                }

                                if (item.getObservacao() != null) {
                                    jsonObject.accumulate("observacao", item.getObservacao());
                                }
                                jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());

                                Date data = item.getDataCadastro();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_cadastro", dataString);


                                inputStream = Util.realizaRequisicaoSet("inseriResposta", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                    item.setSincronizado(1);
                                    respostaService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaRespostaNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("RESPOSTA", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }
            PROGRESSO = 5;
            atualizaBarraProgresso();
            //Sincronismo Foto
            try {

                //ENVIAR Fotos.Busca foto sincronismo  = 0.
                atualizaTextoTabela("FOTO");
                List<Foto> listaFotosNaoSincronizados = fotoService.buscarPorParametroConsulta(new PontoParametroConsulta(0));
                if (listaFotosNaoSincronizados != null && !listaFotosNaoSincronizados.isEmpty()) {

                    for (Foto item : listaFotosNaoSincronizados) {
                        if (!this.isCancelled()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.accumulate("usuario", Util.login);
                                jsonObject.accumulate("senha", Util.senha);
                                jsonObject.accumulate("id_foto", item.getId());
                                jsonObject.accumulate("legenda", item.getLegenda());
                                if (item.getLatitude() != null) {
                                    jsonObject.accumulate("latitude", item.getLatitude());
                                }
                                if (item.getLongitude() != null) {
                                    jsonObject.accumulate("longitude", item.getLongitude());
                                }

                                if (item.getOrdemServico() != null && item.getOrdemServico().getId() != null) {
                                    jsonObject.accumulate("id_os", item.getOrdemServico().getId());
                                }

                                if (item.getPonto() != null && item.getPonto().getId() != null) {
                                    jsonObject.accumulate("id_ponto", item.getPonto().getId());
                                }

                                jsonObject.accumulate("usuario_cadastro", item.getUsuarioCadastro());

                                Date data = item.getDataCadastro();
                                String dataString = format2.format(data);
                                jsonObject.accumulate("data_cadastro", dataString);

                                //Busca Byte da foto
                                File foto = new File(CAMINHO_FOTO.getAbsolutePath() + "/" + item.getId() + ".jpg");

                                byte[] bytesFoto = new byte[(int) foto.length()];

                                try {

                                    FileInputStream fileInputStream = new FileInputStream(foto);
                                    fileInputStream.read(bytesFoto);
                                    fileInputStream.close();

                                    //Base64 bs = new Base64();

                                    String tipo = Base64.encodeToString(bytesFoto, Base64.DEFAULT);

                                    //String stringByte = Base64.encodeBase64String(bytesFoto);


                                    jsonObject.accumulate("bytes_foto", tipo);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    gravarErro(e);
                                }

                                inputStream = Util.realizaRequisicaoSet("inseriFoto", jsonObject);
                                result = Util.convertInputStreamToString(inputStream);
                                if (result != null && !result.equals("") && result.equals("true")) {
                                    item.setSincronizado(1);
                                    fotoService.salvarOuAtualizar(item);
                                    atualizaTextoQtde(++contador + "/" + (listaFotosNaoSincronizados.size()));
                                } else {
                                    System.out.print("Nao atualizado");
                                }
                            } catch (Exception e) {
                                gravarMSGErroUpload("FOTO", String.valueOf(item.getId()));
                                e.printStackTrace();
                                gravarErro(e);
                            }
                        } else {
                            cancel = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                gravarErro(e);
            }
            PROGRESSO = 5;
            atualizaBarraProgresso();
            atualizaTextoQtde("0");

            return cancel;
        }

    }

}
