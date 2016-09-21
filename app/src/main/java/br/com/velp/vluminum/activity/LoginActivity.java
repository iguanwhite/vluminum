package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.dao.DatabaseManager;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.enumerator.TipoSincronismo;
import br.com.velp.vluminum.service.ConfiguracaoWsService;
import br.com.velp.vluminum.service.SincronismoService;
import br.com.velp.vluminum.service.UsuarioService;
import br.com.velp.vluminum.serviceimpl.ConfiguracaoWsServiceImpl;
import br.com.velp.vluminum.serviceimpl.SincronismoServiceImpl;
import br.com.velp.vluminum.serviceimpl.UsuarioServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.DateTimeUtils;
import br.com.velp.vluminum.util.InternetUtil;
import br.com.velp.vluminum.util.VLuminumUtil;

import static android.support.v4.app.ActivityCompat.startActivity;


@EActivity(R.layout.login_activity)
public class LoginActivity extends Activity implements PopupMenu.OnMenuItemClickListener, OnTaskCompleted {

    private static final String MSG_CONFIG_WEB_SERVICE = "Configuração de Web Service não encontrada.";

    @ViewById
    AutoCompleteTextView usuarioAutoComplete;

    @ViewById
    EditText senhaEditText;

    @ViewById
    Button entrarBtn;

    @ViewById
    TextView ultimaDataSincronizacaoTextView;

    @ViewById
    TextView existeVersaoNovaTextView;

    private ConfiguracaoWsService config = new ConfiguracaoWsServiceImpl(this);

    private UsuarioService usuarioService = new UsuarioServiceImpl(this);

    private SincronismoService sincronismoService = new SincronismoServiceImpl(this);

    private List<Usuario> usuarios;

    private AlertDialog alerta;

    private OnTaskCompleted o;

    String versionCode = "";
    String versionName = "";
    String URLVersion = "";
    String URLBase = "http://162.144.97.138/vluminumws/update_tablet/";
    String URLDownload = "";

    @AfterViews
    void inicializar() {
        // Iniciar o banco de dados.
        DatabaseManager.init(this);

        usuarios = usuarioService.listar("login", true);
        usuarioAutoComplete.setAdapter(new ArrayAdapter<Usuario>(this, android.R.layout.simple_dropdown_item_1line, usuarios));

        setarMsgUltimaDataSincronizacao();

        try{
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {

                RetrieveFeedTask a = new RetrieveFeedTask();
                a.execute();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        setarMsgUltimaDataSincronizacao();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        o = this;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Click(R.id.entrarBtn)
    public void efetuarLogin() {
        limparErrosDosCampos();

        boolean cancelar = false;
        View focusView = null;

        String usuarioInformado = usuarioAutoComplete.getText().toString();
        String senhaInformada = senhaEditText.getText().toString();

        // Verifica o usuário.
        if (TextUtils.isEmpty(usuarioInformado)) {
            usuarioAutoComplete.setError(getString(R.string.msg_erro_campo_obrigatorio));
            focusView = usuarioAutoComplete;
            cancelar = true;
        }

        // Verifica a senhaInformada.
        if (TextUtils.isEmpty(senhaInformada)) {
            senhaEditText.setError(getString(R.string.msg_erro_senha_invalida));
            focusView = senhaEditText;
            cancelar = true;
        }

        if (cancelar) {
            // Seta o foco no primeiro campo com erro.
            focusView.requestFocus();
        } else {
            autenticarUsuario(usuarioInformado, senhaInformada);
        }
    }

    private void limparErrosDosCampos() {
        usuarioAutoComplete.setError(null);
        senhaEditText.setError(null);
    }

    public void autenticarUsuario(String usuarioInformado, String senhaInformada) {
        Usuario usuario = usuarioService.validarUsuario(usuarioInformado, senhaInformada);
        if (usuario != null) {
            // Salva o usuário, para ser acessado posteriormente de outras partes da aplicação.
            VLuminumUtil.setUsuarioLogado(usuario);

            startActivity(new Intent(getApplicationContext(), DashboardActivity_.class));
            finish();
        } else {
            senhaEditText.setError(getString(R.string.msg_erro_login_invalido));
            senhaEditText.requestFocus();
        }
    }

    @Click(R.id.opcoesBtn)
    public void opcoes(View v) {
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, v);
        popupMenu.setOnMenuItemClickListener(LoginActivity.this);
        popupMenu.inflate(R.menu.menu_option);
        popupMenu.show();
    }

    @Click(R.id.sincronizarBtn)
    public void sincronismoParcial(View v) {
        InternetUtil internetUtil = new InternetUtil(this);
        if (!internetUtil.isInternetAtiva()) {
            internetUtil.mostrarMensagemSolicitandoAtivacaoInternet("Para realizar o Sincronismo é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?", this, false);
            return;
        }

        List<ConfiguracaoWebService> lista = config.listar("nomeWS", true);
        if (lista != null && !lista.isEmpty()) {
            ConfiguracaoWebService config = lista.get(0);
            config.setTipoSincronismo(TipoSincronismo.PARCIAL);

            Intent intent = new Intent(this, SincronismoActivity_.class);
            intent.putExtra(ConfiguracaoWebService.class.getName(), config);
            startActivity(intent);
        } else {
            AlertaUtil.mensagemAlerta(this, MSG_CONFIG_WEB_SERVICE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionConfig:
                startActivity(new Intent(getApplicationContext(), ConfiguracaoWsActivity_.class));
                return true;
            case R.id.actionSinc:
                InternetUtil internetUtil = new InternetUtil(this);
                if (!internetUtil.isInternetAtiva()) {
                    internetUtil.mostrarMensagemSolicitandoAtivacaoInternet("Para realizar o Sincronismo é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?", this, false);
                    return false;
                }

                List<ConfiguracaoWebService> lista = config.listar("nomeWS", true);
                if (lista != null && !lista.isEmpty()) {
                    ConfiguracaoWebService config = lista.get(0);
                    config.setTipoSincronismo(TipoSincronismo.INICIAL);

                    Intent intent = new Intent(this, SincronismoActivity_.class);
                    intent.putExtra(ConfiguracaoWebService.class.getName(), config);
                    startActivity(intent);

                    return true;
                } else {
                    AlertaUtil.mensagemAlerta(this, MSG_CONFIG_WEB_SERVICE);
                    return false;
                }

            case R.id.atualizar:
                PackageManager manager = this.getPackageManager();
                try {
                    Intent i = manager.getLaunchIntentForPackage("br.com.velp.atualizador");
                    if (i == null) {

                        //Cria o gerador do AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Atualizador Vluminum");
                        builder.setMessage("Aplicativo atualizador não encontrado no dispositivo. Deseja fazer o download?");
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                new Consulta(o).execute();
                                //new Consulta.execute(null);
                            }
                        });
                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        alerta = builder.create();
                        alerta.show();


                    }
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    this.startActivity(i);
                    return true;
                } catch (Exception e) {
                    return false;
                }

            case R.id.actionSair:
                System.exit(0);
            default:
                return false;
        }
    }


    private void setarMsgUltimaDataSincronizacao() {
        Date dataUltimaSincronizacao = sincronismoService.buscarDataUltimoSincronismo("tb_sincronismo");
        if (dataUltimaSincronizacao != null) {
            Date dataAtual = new Date();
            String msg = "Última sincronização realizada ";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            int direncaEmDias = DateTimeUtils.daysDiff(dataUltimaSincronizacao, dataAtual);
            if (direncaEmDias == 0) {
                ultimaDataSincronizacaoTextView.setText(msg + " às " + DateTimeUtils.formatoHora(dataUltimaSincronizacao) + " hs");
                ultimaDataSincronizacaoTextView.setTextColor(Color.parseColor("#088A29"));
            } else if (direncaEmDias >= 1) {
                ultimaDataSincronizacaoTextView.setText(msg + " em " + dateFormat.format(dataUltimaSincronizacao));
                ultimaDataSincronizacaoTextView.setTextColor(Color.RED);
            }
        }
    }


    private void instalarApk() {
        try {

            File apkFile = new File("/sdcard/download/AtualizadorVLuminum.apk");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            startActivity(intent);

            Toast.makeText(this, "Atualizador instalado com sucesso.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "Falha ao instalar o Atualizador.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTaskCompleted() {
        instalarApk();
    }


    private void verificarAtualizacao(){
        try{


            URLVersion = URLBase + "version.txt";
            URLDownload = URLBase + "update.zip";

            // buscando versao disponivel no site
        //    GetVersionFromServer(URLVersion);




            if(versionName.equals("") || versionCode.equals("")){
                return;
            }

            String novaVersao = versionCode+"."+versionName;
            String versaoInstalada = getInstallPackageVersionInfo("Vluminum");

            if(novaVersao.compareTo(versaoInstalada) > 0){

                existeVersaoNovaTextView.setTextColor(Color.parseColor("#0033FF"));

                existeVersaoNovaTextView.setText("Nova versão disponível. Atualize o Vluminum.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInstallPackageVersionInfo(String appName){
        String InstallVersion = "";
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++)
        {

            //apps.get(i).prettyPrint();
            if(apps.get(i).appName.equals(appName))
            {
                InstallVersion = apps.get(i).versionCode+"."+apps.get(i).versionName;
                break;
            }
        }

        return InstallVersion;
    }



    public void GetVersionFromServer( String BuildVersionPath){
        RetrieveFeedTask a = new RetrieveFeedTask();
        a.execute();
    }


    class RetrieveFeedTask extends AsyncTask<Void, Void, Integer> {


        protected Integer doInBackground(Void...params) {
            URL u;
            try {
                HttpURLConnection c = null;
                try {

                    u = new URL("http://162.144.97.138/vluminumws/update_tablet/version.txt");

                    c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.setConnectTimeout(15000);
                    c.setReadTimeout(15000);
                    c.connect();
                }catch (Exception e){
                    e.printStackTrace();
                }
                InputStream in = c.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];

                int len1 = 0;
                while ( (len1 = in.read(buffer)) != -1 ){
                    baos.write(buffer,0, len1);
                }

                String s = baos.toString();

                String[] fields = s.split(";");//version-code=2;version-name=2.1

                versionCode = fields[0].split("=")[1];
                versionName = fields[1].split("=")[1];

                baos.close();

            }catch (Exception e) {
                versionCode = "";
                versionName = "";
                e.printStackTrace();
           //     Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            verificarAtualizacao();
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages)
    {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packs.size();i++)
        {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo();
            newInfo.appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.packageName = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            res.add(newInfo);
        }
        return res;
    }





}




class PInfo {
     String appName = "";
     String packageName = "";
     String versionName = "";
     int versionCode = 0;
    //private Drawable icon;
    private void prettyPrint() {

    }

}






class Consulta extends AsyncTask<Object, Object, Object> {

    private OnTaskCompleted listener;

    public Consulta(OnTaskCompleted listener) {
        this.listener = listener;
    }


    @Override
    protected String doInBackground(Object... objects) {
        int count = 0;
        long total = 0;
        long lenghtOfFile = -1;

        InputStream input = null;
        OutputStream output = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();


        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.connection.timeout", new Integer(10000));
        httpclient.getParams().setParameter("http.socket.timeout", new Integer(30000));

        HttpGet request = new HttpGet("http://189.115.138.37:8080/AtualizadorVLuminum.apk");


        while (true) {
            try {

                request.addHeader("Range", "bytes=" + total + "-");
                HttpResponse response = httpclient.execute(request);

                input = response.getEntity().getContent();
                Header[] header = response.getHeaders("Content-Length");

                if (lenghtOfFile == -1) {
                    lenghtOfFile = Long.parseLong(header[0].getValue());
                }
                byte data[] = new byte[1024];
                int ret = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    bout.write(data, 0, count);

                    if (total >= lenghtOfFile) {
                        bout.flush();
                        break;
                    }
                }
                bout.flush();

                if (total >= lenghtOfFile) {
                    break;
                }
            } catch (Exception e) {
                try {
                    input.close();
                    Thread.sleep(10000);

                } catch (Exception e2) {
                    e.printStackTrace();
                    return "f";
                }
            }
        }

        try {
            File dir = new File("/sdcard/download/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File f = new File("/sdcard/download/AtualizadorVLuminum.apk");
            if (f.exists()) {
                f.delete();
            }
            output = new FileOutputStream("/sdcard/download/AtualizadorVLuminum.apk");
            output.write(bout.toByteArray());
            output.flush();
            output.close();
        } catch (IOException io) {
            io.printStackTrace();
            return "f";
        }

        try {
            input.close();
            httpclient.getConnectionManager().shutdown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "f";
        }
        return "s";
    }

    @Override
    protected void onPostExecute(Object o) {
        listener.onTaskCompleted();
    }

}



interface OnTaskCompleted {
    void onTaskCompleted();
}



