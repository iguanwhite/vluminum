package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.util.Log;
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
import java.util.Random;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.PontoServico;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.service.GrupoServicoService;
import br.com.velp.vluminum.service.PontoServicoService;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.serviceimpl.GrupoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PontoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.ServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.SpinnerUtil;

@EActivity(R.layout.cadastro_servico_activity)
public class CadastroServicoActivity extends ActivityBase {

    @ViewById
    EditText fieldDescricao;

    @ViewById
    Spinner grupoServicoSpinner;

    @ViewById
    Button salvarServicoBtn;

    private ServicoService servicoService = new ServicoServiceImpl(this);

    private GrupoServicoService grupoService = new GrupoServicoServiceImpl(this);

    private PontoServicoService pontoServicoService = new PontoServicoServiceImpl(this);

    List<GrupoServico> gruposServicos;

    private Servico servico;

    private Ponto ponto;

    @AfterViews
    void inicializar() {
        carregarSpinners();
        recuperarParametros();
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        servico = (Servico) intent.getSerializableExtra(Servico.class.getName());
    }


    private void carregarDadosServico(Servico servico) {
        grupoServicoSpinner.setSelection(SpinnerUtil.getIndice(grupoServicoSpinner, servico.getGrupo().getDescricao()));
        fieldDescricao.setText(servico.getDescricao());
        salvarServicoBtn.setText("Atualizar");
    }

    void carregarSpinners() {
        gruposServicos = grupoService.listar("descricao", true);

        if (gruposServicos != null && gruposServicos.size() > 0) {
            List<GrupoServico> gruposServicosComOpcaoSelecione = new LinkedList<GrupoServico>();
            gruposServicosComOpcaoSelecione.add(new GrupoServico(null, "-- SELECIONE --"));
            gruposServicosComOpcaoSelecione.addAll(gruposServicos);
            grupoServicoSpinner.setAdapter(new ArrayAdapter<GrupoServico>(this, android.R.layout.simple_spinner_item, gruposServicosComOpcaoSelecione));

            if (servico != null) {
                carregarDadosServico(servico);
            }
        }
    }


    @Click(R.id.salvarServicoBtn)
    void salvar() {

        boolean result = false;
        boolean edit = false;

        Servico servico = null;
        GrupoServico g = (GrupoServico) grupoServicoSpinner.getSelectedItem();

        if (this.servico != null) {
            servico = this.servico;
            edit = true;
        } else {
            servico = new Servico();
            Random rand = new Random();
            int randomNum = rand.nextInt((6000 - 1) + 1) + 1;

            servico.setId(randomNum);
        }

        servico.setDescricao(fieldDescricao.getText().toString());

        if (g != null) {
            servico.setGrupo(g);
        }

        // Remocao da Vinculacao do Ponto
        try {
            pontoServicoService.deletarPorPontoEServico(ponto.getId(), servico.getId());
        } catch (Exception ex) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao excluir ligação de Ponto com Serviço", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        try {
            servico.setSincronizado(0);
            result = servicoService.salvarOuAtualizar(servico);

        } catch (Exception ex) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao salvar Serviço", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }
        //Vinculacao do Servico ao ponto.
        try {
            PontoServico pontoServico = new PontoServico();
            pontoServico.setServico(servico);
            pontoServico.setPonto(ponto);
            pontoServicoService.salvarOuAtualizar(pontoServico);

        } catch (Exception ex) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao vincular Serviço ao Ponto!", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }


        if (result) {
            if (!edit) {
                AlertaUtil.mostrarMensagem(this, "Serviço cadastrado e vinculado ao Ponto com sucesso!");
            } else {
                AlertaUtil.mostrarMensagem(this, "Serviço vinculado atualizado com sucesso!");
            }

            voltarTelaCadastroPonto();
        }
    }


    private void voltarTelaCadastroPonto() {
        finish();
    }

}
