package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;
import br.com.velp.vluminum.service.GrupoServicoService;
import br.com.velp.vluminum.serviceimpl.GrupoServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;

@EActivity(R.layout.pesquisa_servico_activity)
public class PesquisaServicoActivity extends ActivityBase {

    @ViewById
    Spinner grupoServicoSpinner;

    private GrupoServicoService grupoService = new GrupoServicoServiceImpl(this);

    private Boolean associarPontoServico;

    private Ponto ponto;

    @AfterViews
    void inicializar() {
        carregarSpinners();
        recuperarParametros();
    }


    private void recuperarParametros() {
        Intent intent = getIntent();
        associarPontoServico = (Boolean) intent.getSerializableExtra("associarPontoServico");
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
    }


    @Click(R.id.novoPontoBtn)
    void abrirTelaCadastroPonto() {
        Intent intent = new Intent(this, CadastroServicoActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        startActivity(intent);
        finish();
    }


    private void carregarSpinners() {
        List<GrupoServico> listaGrupos = grupoService.listar("descricao", true);
        if (listaGrupos != null && listaGrupos.size() > 0) {
            grupoServicoSpinner.setAdapter(new ArrayAdapter<GrupoServico>(this, android.R.layout.simple_spinner_item, listaGrupos));
        }
    }

    @Click(R.id.pesquisarPontoBtn)
    void pesquisar() {
        ServicoParametroConsulta parametroConsulta = new ServicoParametroConsulta();
        parametroConsulta.setPonto(ponto);

        GrupoServico g = (GrupoServico) grupoServicoSpinner.getSelectedItem();

        if (g != null) {
            parametroConsulta.setGrupoServico(g);
        }

        if (parametroConsulta.foiInformadoAlgumParametroConsulta()) {
            abrirTelaListagemServico(parametroConsulta);
        } else {
            AlertaUtil.mostrarMensagem(this, "Informe pelo menos um dos campos!");
        }
    }

    @Click(R.id.limparPesquisaPontoBtn)
    void limpar() {

    }

    private void abrirTelaListagemServico(ServicoParametroConsulta parametroConsulta) {
        Intent intent = new Intent(this, ListaServicoActivity_.class);
        intent.putExtra(ServicoParametroConsulta.class.getName(), parametroConsulta);
        if (associarPontoServico != null && associarPontoServico) {
            intent.putExtra("associarPontoServico", true);
        }

        startActivity(intent);
        finish();
    }
}