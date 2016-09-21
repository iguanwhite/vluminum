package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.enumerator.TipoDefeito;
import br.com.velp.vluminum.enumerator.TipoPoste;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.PosteService;
import br.com.velp.vluminum.serviceimpl.PosteServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;

@EActivity(R.layout.cadastro_poste_activity)
public class CadastroPosteActivity extends ActivityBase {

    @ViewById
    EditText siglaEditText;

    @ViewById
    EditText descricaoEditText;

    @ViewById
    EditText alturaEditText;

    @ViewById
    Spinner tipoPosteSpinner;

    @ViewById
    Spinner tipoDefeitoSpinner;

    @ViewById
    EditText barramentoEditText;

    @ViewById
    EditText informacaoTecnicaEditText;

    private PosteService service = new PosteServiceImpl(this);

    @AfterViews
    void carregarSpinners() {
        tipoPosteSpinner.setAdapter(new ArrayAdapter<TipoPoste>(this, android.R.layout.simple_spinner_item, TipoPoste.values()));

        tipoDefeitoSpinner.setAdapter(new ArrayAdapter<TipoDefeito>(this, android.R.layout.simple_spinner_item, TipoDefeito.values()));
    }

    @Click(R.id.salvarPosteBtn)
    void salvar() {
        boolean result = false;
        Poste poste = obterDadosInformados();

        try {
            result = service.salvarOuAtualizar(poste);
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroPosteActivity.class.getName(), "Erro ao salvar Poste", ex);
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
            return;
        }

        if (result) {
            AlertaUtil.mostrarMensagem(this, "Poste cadastrado com sucesso!");
            startActivity(new Intent(this, DashboardActivity_.class));
            finish();
        }
    }

    private Poste obterDadosInformados() {
        Poste poste = new Poste();

        String sigla = (siglaEditText.getText() != null) ? siglaEditText.getText().toString() : null;
        poste.setSigla(sigla);

        String descricao = (descricaoEditText.getText() != null) ? descricaoEditText.getText().toString() : null;
        poste.setDescricao(descricao);

        if (alturaEditText.getText() != null && !alturaEditText.getText().toString().equals("")) {
            BigDecimal altura = new BigDecimal(alturaEditText.getText().toString());
            poste.setAltura(altura);
        }

        TipoPoste tipoPoste = (TipoPoste) tipoPosteSpinner.getSelectedItem();
        poste.setTipoPoste(tipoPoste);

        TipoDefeito tipoDefeito = (TipoDefeito) tipoDefeitoSpinner.getSelectedItem();
        poste.setTipoDefeito(tipoDefeito);

        String barramento = (barramentoEditText.getText() != null) ? barramentoEditText.getText().toString() : null;
        poste.setBarramento(barramento);

        String informacaoTecnica = (informacaoTecnicaEditText.getText() != null) ? informacaoTecnicaEditText.getText().toString() : null;
        poste.setInformacoesTecnicas(informacaoTecnica);

        return poste;
    }

}
