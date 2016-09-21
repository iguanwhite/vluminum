package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;

@EActivity(R.layout.pesquisa_os_activity)
public class PesquisaOsActivity extends ActivityBase {

    @ViewById
    Spinner municipioSpinner;

    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText numOsEditText;

    @ViewById
    Spinner prioridadeSpinner;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    ArrayAdapter<Municipio> adapter;

    @AfterViews
    void inicializar() {
        carregarSpinners();
    }

    private void carregarSpinners() {
        prioridadeSpinner.setAdapter(new ArrayAdapter<Prioridade>(this, android.R.layout.simple_spinner_item, Prioridade.values()));

        List<Municipio> municipios = municipioService.listarComOSAbertas();
        List<Municipio> municipiosComOpcaoSelecione = new LinkedList<Municipio>();
        municipiosComOpcaoSelecione.add(new Municipio(null, "-- SELECIONE --", null));
        municipiosComOpcaoSelecione.addAll(municipios);
        adapter = new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipiosComOpcaoSelecione);
        municipioSpinner.setAdapter(adapter);

    }



    @Click(R.id.pesquisarOsBtn)
    void pesquisar() {
        OsParametroConsulta parametroConsulta = new OsParametroConsulta();

        Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
        parametroConsulta.setMunicipio(municipio);

        String logradouro = (logradouroEditText.getText() != null && logradouroEditText.getText().length() > 0) ? logradouroEditText.getText().toString() : "";
        parametroConsulta.setLogradouro(logradouro);

        String numeroOs = (numOsEditText.getText() != null && numOsEditText.getText().length() > 0) ? numOsEditText.getText().toString() : "";
        parametroConsulta.setNumeroOs(numeroOs);

        Prioridade prioridade = (Prioridade) prioridadeSpinner.getSelectedItem();
        parametroConsulta.setPrioridade(prioridade);

        abrirTelaListagemOs(parametroConsulta);
    }

    @Click(R.id.limparPesquisaPontoBtn)
    void limpar() {
        logradouroEditText.setText("");
        prioridadeSpinner.setSelection(0);
    }

    private void abrirTelaListagemOs(OsParametroConsulta parametroConsulta) {
        Intent intent = new Intent(this, ListaOsActivity_.class);
        intent.putExtra(OsParametroConsulta.class.getName(), parametroConsulta);
        startActivity(intent);
        finish();
    }

}
