package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.service.ConfiguracaoWsService;
import br.com.velp.vluminum.service.UsuarioService;
import br.com.velp.vluminum.serviceimpl.ConfiguracaoWsServiceImpl;
import br.com.velp.vluminum.serviceimpl.UsuarioServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.Util;

@EActivity(R.layout.configuracao_ws_activity)
public class ConfiguracaoWsActivity extends Activity {

    @ViewById
    EditText urlEditText;

    @ViewById
    EditText portaEditText;

    @ViewById
    EditText nomeWSEditText;

    @ViewById
    EditText usuarioEditText;

    @ViewById
    EditText senhaEditText;

    @ViewById
    Button testarBtn;

    public static final String URL = "162.144.97.138";

    public static final String PORTA = "80";

    public static final String NOME_WS = "vluminumws";

    public static final String SERVICO_WS = "/service?method=testarWS";

    public static final int SEM_CONEXAO_INTERNET = 0;

    public static final int DADOS_VALIDADOS = 1;

    public static final int DADOS_INVALIDADOS = 2;

    public static Boolean teste = false;

    public ProgressDialog progressDialog;

    public Integer idUsuario;

    private ConfiguracaoWsService configService = new ConfiguracaoWsServiceImpl(this);

    private UsuarioService usuarioService = new UsuarioServiceImpl(this);

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @AfterViews
    void inicializar() {
        List<ConfiguracaoWebService> lista = configService.listar("nomeWS", true);
        if (lista != null && !lista.isEmpty()) {
            ConfiguracaoWebService config = lista.get(0);
            urlEditText.setText(config.getUrl());
            portaEditText.setText(config.getPorta());
            nomeWSEditText.setText(config.getNomeWS());
            usuarioEditText.setText(config.getUsuario());
            senhaEditText.setText(config.getSenha());
        } else {
            urlEditText.setText(URL);
            portaEditText.setText(PORTA);
            nomeWSEditText.setText(NOME_WS);
        }
    }

    @Click(R.id.testarBtn)
    public void testarWebService(View v) {
        InternetUtil internetUtil = new InternetUtil(this);
        if (!internetUtil.isInternetAtiva()) {
            internetUtil.mostrarMensagemSolicitandoAtivacaoInternet("Para realizar o teste da configuração é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?", this, false);
            return;
        }

        try {
            new Thread(new Runnable() {
                public void run() {
                    Looper.myLooper();
                    Looper.prepare();
                    progressDialog = ProgressDialog.show(ConfiguracaoWsActivity.this, null, getApplicationContext().getString(R.string.testarWS), true);

                    String url = urlEditText.getText().toString();
                    String porta = portaEditText.getText().toString();
                    String nomeWS = nomeWSEditText.getText().toString();
                    String usuario = usuarioEditText.getText().toString();
                    String senha = senhaEditText.getText().toString();

                    //Enviar JSON WS
                    HttpAsyncTaskTestarWS http = new HttpAsyncTaskTestarWS(ConfiguracaoWsActivity.this);
                    http.execute(usuario, senha, url, porta, nomeWS);

                    Looper.loop();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.salvarBtn)
    public void salvarConfiguracoes(View v) {
        try {
            if (teste) {
                Boolean flag = false;

                String msg = "Insira: ";
                if (urlEditText == null || urlEditText.getText() == null || urlEditText.getText().toString().equals("")) {
                    msg += "Url";
                    flag = true;
                }
                if (portaEditText == null || portaEditText.getText() == null || portaEditText.getText().toString().equals("")) {
                    msg += " Porta";
                    flag = true;
                }

                if (nomeWSEditText == null || nomeWSEditText.getText() == null || nomeWSEditText.getText().toString().equals("")) {
                    msg += " Nome WS";
                    flag = true;
                }

                if (usuarioEditText == null || usuarioEditText.getText() == null || usuarioEditText.getText().toString().equals("")) {
                    msg += " Usuário";
                    flag = true;
                }

                if (flag) {
                    AlertaUtil.mensagemCamposObrigatorios(this, msg);
                } else {
                    try {
                        ConfiguracaoWebService config = new ConfiguracaoWebService();
                        config.setIdUsuario(idUsuario);
                        config.setUrl(urlEditText.getText().toString());
                        config.setNomeWS(nomeWSEditText.getText().toString());
                        config.setPorta(portaEditText.getText().toString());
                        config.setUsuario(usuarioEditText.getText().toString());
                        config.setSenha(senhaEditText.getText().toString());
                        configService.deletarTudo();
                        if (configService.salvarOuAtualizar(config)) {
                            AlertaUtil.mostrarMensagem(this, "Configuração cadastrada com sucesso!");
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                AlertaUtil.mensagemInformacao(this, "Clique no botão Testar antes de Salvar!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class HttpAsyncTaskTestarWS extends AsyncTask<String, Object, Integer> {
        Context c;

        public HttpAsyncTaskTestarWS(Context context) {
            this.c = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                // TODO: CRIAR OBJETO 'ConfiguracaoWebService' ao invês de criar várias strings
                Util.endereco = params[2];
                Util.porta = params[3];
                Util.nomeWS = params[4];
                Util.login = params[0];
                Util.senha = params[1];
                InputStream inputStream = Util.realizaRequisicao("testarWS", null, null, null, null);
                if (inputStream != null) {
                    String result = Util.convertInputStreamToString(inputStream);
                    if (result != null && !result.equals("")) {
                        usuarioService.deletarTudo();
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            idUsuario = json.getInt("id_usuario");
                            Usuario usuario = new Usuario();
                            usuario.setId(json.getInt("id_usuario"));
                            usuario.setNome(json.getString("nome"));
                            usuario.setLogin(json.getString("login"));
                            usuario.setSenha(json.getString("senha"));
                            usuarioService.salvarOuAtualizar(usuario);
                            return DADOS_VALIDADOS;
                        }
                    } else {
                        return DADOS_INVALIDADOS;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return SEM_CONEXAO_INTERNET;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case DADOS_VALIDADOS:
                    progressDialog.dismiss();
                    AlertaUtil.mensagemInformacao(c, "Dados validados com sucesso!");
                    teste = true;
                    EditText myEditText = (EditText) findViewById(R.id.senhaEditText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
                    break;

                case DADOS_INVALIDADOS:
                    progressDialog.dismiss();
                    AlertaUtil.mensagemInformacao(c, "Dados inválidos. Verifique as informações fornecidas!");
                    teste = false;
                    break;

                case SEM_CONEXAO_INTERNET:
                    progressDialog.dismiss();
                    AlertaUtil.mensagemInformacao(c, "Sem conexão com o Web Service. Verifique a conexão com a Internet!");
                    teste = false;
                    break;
                default:
                    break;
            }


        }

        public InputStream realizaRequisicao(String usuario, String senha, String caminho, String porta, String nomeWS) throws Exception {

            Log.d("sincronismo", "testarWS");

            InputStream inputStream = null;
            String result = "";

            HttpClient httpclient = new DefaultHttpClient();
            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario", usuario);
            jsonObject.accumulate("senha", senha);

            json = "[";
            json += jsonObject.toString();
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + caminho + ":" + porta + "/" + nomeWS + SERVICO_WS;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);


            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return inputStream = httpResponse.getEntity().getContent();

        }
    }


}
