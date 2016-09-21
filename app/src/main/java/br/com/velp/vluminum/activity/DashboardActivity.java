package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.enumerator.TipoSincronismo;
import br.com.velp.vluminum.service.ConfiguracaoWsService;
import br.com.velp.vluminum.serviceimpl.ConfiguracaoWsServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.DispositivoUtil;
import br.com.velp.vluminum.util.InternetUtil;

@EActivity(R.layout.dashboard_activity)
public class DashboardActivity extends Activity {

    @ViewById
    TextView pontosTextView;

    @ViewById
    TextView ordensServicoTextView;

    @ViewById
    TextView mapaTextView;

    @ViewById
    TextView sincronismoTextView;

    @AfterViews
    void inicializar() {
        if (DispositivoUtil.isTablet(this)) {
            realinharComponentes();
        }
    }

    private void realinharComponentes() {
        pontosTextView.setPadding(120, 20, 0,  0);
        mapaTextView.setPadding(120, 80, 0,  0);

        ordensServicoTextView.setPadding(0, 20, 120, 0);
        sincronismoTextView.setPadding(0, 80, 120, 0);
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
                        startActivity(new Intent(getApplicationContext(), LoginActivity_.class));
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

    @Click(R.id.mapaTextView)
    public void abrirTelaMapa(View view) {
        abrirTelaPontosMapa();
    }

    private void abrirTelaPontosMapa() {
        Intent intent = new Intent(this, MapaActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.pontosTextView)
    public void abrirTelaModuloCadastro() {
        startActivity(new Intent(this, PesquisaPontoActivity_.class));
    }

    @Click(R.id.ordensServicoTextView)
    public void abrirTelaListaOrdemDeServico() {
        startActivity(new Intent(this, ListaOsActivity_.class));
    }

    @Click(R.id.sincronismoTextView)
    public void abrirTelaSincronismo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente sincronizar os dados?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sincronizar();
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

    private void sincronizar() {
        InternetUtil internetUtil = new InternetUtil(this);
        if (!internetUtil.isInternetAtiva()) {
            internetUtil.mostrarMensagemSolicitandoAtivacaoInternet("Para realizar o Sincronismo é necessário que uma Conexão de Internet esteja habilitada.\nDeseja habilitar uma Conexão de Internet?", this, false);
            return;
        }
        ConfiguracaoWsService service = new ConfiguracaoWsServiceImpl(this);
        List<ConfiguracaoWebService> lista = service.listar("nomeWS", true);
        if (lista != null && !lista.isEmpty()) {
            ConfiguracaoWebService config = lista.get(0);
            config.setTipoSincronismo(TipoSincronismo.PARCIAL);

            Intent intent = new Intent(this, SincronismoActivity_.class);
            intent.putExtra(ConfiguracaoWebService.class.getName(), config);

            startActivity(intent);
        } else {
            final String MSG_CONFIG_WEB_SERVICE = "Configuração de Web Service não encontrada.";
            AlertaUtil.mensagemAlerta(this, MSG_CONFIG_WEB_SERVICE);
        }
    }

}
