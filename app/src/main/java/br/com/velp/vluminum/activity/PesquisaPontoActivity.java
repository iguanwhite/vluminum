package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.widget.ArrayAdapter;
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
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;

@EActivity(R.layout.pesquisa_ponto_activity)
public class PesquisaPontoActivity extends ActivityBase {

    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText pontoReferenciaEditText;

    @ViewById
    EditText numPlaquetaTransformadorEditText;

    @ViewById
    Spinner municipioSpinner;

    private Boolean associarPontoOs;

    private OrdemServico ordemServico;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    ArrayAdapter<Municipio> adapter;

    @AfterViews
    void inicializar() {
        ativarGPS();

        OrdemServicoUtil.getInstance().setColetarMaterial(false);

        recuperarParametros();

        carregarSpinners();
    }

    @Override
    public void onBackPressed() {
        finish();
        if (ordemServico != null) {
            Intent intent = new Intent(this, ListaPontoOsActivity_.class);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
            startActivity(intent);
        }
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        associarPontoOs = (Boolean) intent.getSerializableExtra("associarPontoOs");
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
    }

    private void ativarGPS() {
        GPSUtil gpsUtil = new GPSUtil(this);
        gpsUtil.ativarGPS();
    }

    void carregarSpinners() {
        List<Municipio> municipios = municipioService.listarPorCliente();
        List<Municipio> municipiosComOpcaoSelecione = new LinkedList<Municipio>();
        municipiosComOpcaoSelecione.add(new Municipio(null, "-- SELECIONE --", null));
        municipiosComOpcaoSelecione.addAll(municipios);

        adapter = new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipiosComOpcaoSelecione);
        municipioSpinner.setAdapter(adapter);

        aplicarCidadeOsSelecao();

    }


    private void aplicarCidadeOsSelecao() {
        if (ordemServico != null) {
            Municipio compareValue = ordemServico.getMunicipio();

            if (!compareValue.equals(null)) {
                int spinnerPostion = adapter.getPosition(compareValue);
                municipioSpinner.setSelection(spinnerPostion);
                spinnerPostion = 0;
            }
        }

    }


    @Click(R.id.novoPontoBtn)
    void abrirTelaCadastroPonto() {
        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
    }

    @Click(R.id.mapaBtn)
    void visualizarMapa() {
        Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();

        if (municipio != null && !municipio.getNome().contains("SELECIONE")) {
            Intent intent = new Intent(this, MapaPontoActivity_.class);
            intent.putExtra(Municipio.class.getName(), municipio);
            if (ordemServico != null) {
                intent.putExtra(OrdemServico.class.getName(), ordemServico);
                intent.putExtra("associarPontoOs", true);
            } else {
                intent.putExtra("associarPontoOs", false);
            }

            startActivity(intent);
        } else {
            AlertaUtil.mostrarMensagem(this, "Selecione o Município para visualizar os pontos no mapa!");
        }

    }

    @Click(R.id.pesquisarPontoBtn)
    void pesquisar() {
        PontoParametroConsulta parametroConsulta = new PontoParametroConsulta();

        if (ordemServico != null) {
            parametroConsulta.setOrdemServico(ordemServico);
        }

        String logradouro = (logradouroEditText.getText() != null) ? logradouroEditText.getText().toString() : "";
        parametroConsulta.setLogradouro(logradouro);

        String pontoDeReferencia = (pontoReferenciaEditText.getText() != null) ? pontoReferenciaEditText.getText().toString() : "";
        parametroConsulta.setPontoDeReferencia(pontoDeReferencia);

        String numPlaquetaTransformador = (numPlaquetaTransformadorEditText.getText() != null) ? numPlaquetaTransformadorEditText.getText().toString() : "";
        parametroConsulta.setNumPlaquetaTransformador(numPlaquetaTransformador);

        Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
        parametroConsulta.setMunicipio(municipio);

        boolean municipioInformado = (parametroConsulta.getMunicipio().getId() == null) ? false : true;
        boolean logradouroInformado = (parametroConsulta.getLogradouro() == null || parametroConsulta.getLogradouro().trim().isEmpty()) ? false : true;
        boolean plaquetaInformada = (parametroConsulta.getNumPlaquetaTransformador() == null || parametroConsulta.getNumPlaquetaTransformador().trim().isEmpty()) ? false : true;
        boolean referenciaInformada = (parametroConsulta.getPontoDeReferencia() == null || parametroConsulta.getPontoDeReferencia().trim().isEmpty()) ? false : true;

        if (municipioInformado == false && logradouroInformado == false && plaquetaInformada == false && referenciaInformada == false) {
            AlertaUtil.mostrarMensagem(this, "Favor informar o Município e Logradouro, Ponto de Referência ou o Nº Plaqueta!");
        } else {
            if (municipioInformado && !logradouroInformado) {
                AlertaUtil.mostrarMensagem(this, "Para pesquisar por Município informe o Logradouro!");
                return;
            }

            if (!municipioInformado && logradouroInformado) {
                AlertaUtil.mostrarMensagem(this, "Para pesquisar por Logradouro informe o Município!");
                return;
            }

            if (logradouroInformado && parametroConsulta.getLogradouro().trim().length() < 3) {
                AlertaUtil.mostrarMensagem(this, "Favor informar no mínimo 3 caracteres no campo Logradouro!");
                return;
            }

            if (referenciaInformada && parametroConsulta.getPontoDeReferencia().trim().length() < 3) {
                AlertaUtil.mostrarMensagem(this, "Favor informar no mínimo 3 caracteres no campo Ponto de Referência!");
                return;
            }


            if (plaquetaInformada && parametroConsulta.getNumPlaquetaTransformador().trim().length() < 3) {
                AlertaUtil.mostrarMensagem(this, "Favor informar no mínimo 3 caracteres no campo Nº Plaqueta!");
                return;
            }

            abrirTelaListagemPontos(parametroConsulta);
        }
    }

    @Click(R.id.limparPesquisaPontoBtn)
    void limpar() {
        carregarSpinners();
        logradouroEditText.setText("");
        pontoReferenciaEditText.setText("");
        numPlaquetaTransformadorEditText.setText("");
    }

    private void abrirTelaListagemPontos(PontoParametroConsulta parametroConsulta) {

        Intent intent;

        /*
        if (ordemServico != null) {
            intent = new Intent(this, ListaPontoOsActivity_.class);
        } else {
            intent = new Intent(this, ListaPontoActivity_.class);
        }
        */

        intent = new Intent(this, ListaPontoActivity_.class);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        intent.putExtra(PontoParametroConsulta.class.getName(), parametroConsulta);
        if (associarPontoOs != null && associarPontoOs) {
            intent.putExtra("associarPontoOs", true);
        }

        startActivity(intent);
        finish();
    }
}