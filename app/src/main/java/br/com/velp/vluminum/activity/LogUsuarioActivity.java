package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.velp.vluminum.R;

@EActivity(R.layout.log_usuario_activity)
public class LogUsuarioActivity extends Activity {

    @ViewById
    TextView textLog;

    @AfterViews
    void inicializar() {
        Bundle b = getIntent().getExtras();
        textLog.setText("ATENÇÃO! OCORREU ALGUM ERRO NOS SINCRONISMO. CONTATE O SUPORTE \n" + b.getString("log"));
    }

    @Click(R.id.buttonVoltar)
    public void voltar() {
        this.finish();
    }

}
