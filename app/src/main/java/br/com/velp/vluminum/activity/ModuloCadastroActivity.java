package br.com.velp.vluminum.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.velp.vluminum.R;

@EActivity(R.layout.modulo_cadastro_activity)
// TODO: CASO NÃO FOR MAIS USAR, DELETAR!!!
public class ModuloCadastroActivity extends Activity {

    @ViewById
    TextView cadastroPontoTextView;

    @Click(R.id.cadastroPontoTextView)
    void abrirTelaCadastroPonto() {
        startActivity(new Intent(this, PesquisaPontoActivity_.class));
    }
}
