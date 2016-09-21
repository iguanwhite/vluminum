package br.com.velp.vluminum.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.PontoServico;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;
import br.com.velp.vluminum.service.PontoServicoService;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.serviceimpl.PontoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.ServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;

@EActivity(R.layout.lista_servico_activity)
public class ListaServicoActivity extends ListActivityBase {

    @ViewById
    ListView list;

    private ServicoService servicoService = new ServicoServiceImpl(this);

    private PontoServicoService pontoServicoServiceService = new PontoServicoServiceImpl(this);

    private List servicos;

    private ServicoParametroConsulta parametroConsulta;

    private Boolean associarPontoServico;

    @AfterViews
    void inicializar() {
        recuperarParametros();
        popularListaPontos();
    }

    @Click(R.id.novoPontoBtn)
    void abrirTelaCadastroPonto() {
        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        if (associarPontoServico != null && associarPontoServico) {
            intent.putExtra(Ponto.class.getName(), parametroConsulta.getPonto());
        }
        startActivity(intent);
        finish();
    }

    @ItemClick(android.R.id.list)
    void selecionarServico(Servico servico) {
        if (associarPontoServico != null && associarPontoServico) {
            associarServicoAoPonto(servico);
        } else {
            Intent intent = new Intent(this, CadastroPontoActivity_.class);
            intent.putExtra(Ponto.class.getName(), servico);

            startActivity(intent);
            finish();
        }
    }

    private void associarServicoAoPonto(Servico servico) {
        PontoServico pontoServico = new PontoServico();
        pontoServico.setServico(servico);
        pontoServico.setPonto(parametroConsulta.getPonto());
        try {
            if (servicoJaAssociadaAoPonto(servico.getId(), pontoServico.getPonto().getId())) {
                AlertaUtil.mostrarMensagem(this, "Servico selecionado já associado ao Ponto!");
                return;
            } else {
                pontoServicoServiceService.salvarOuAtualizar(pontoServico);
            }
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(ListaPontoOsActivity.class.getName(), "Erro ao associar servico ao Ponto", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        AlertaUtil.mostrarMensagem(this, "Servico associado ao ponto com sucesso!");

        /*
        Intent intent = new Intent(this, ListaPontoServicoActivity_.class);
        intent.putExtra(Ponto.class.getName(), parametroConsulta.getPonto());
        startActivity(intent); */
        finish();
    }

    private boolean servicoJaAssociadaAoPonto(Integer idServico, String idPonto) {
        List<PontoServico> lista = pontoServicoServiceService.listarPorPonto(idPonto);
        List<Servico> servicos = new ArrayList<Servico>();
        for (PontoServico pontoServico : lista) {
            servicos.add(pontoServico.getServico());
        }
        boolean contem = false;
        for (Servico s : servicos) {
            if (s.getId().equals(idServico)) {
                contem = true;
                break;
            }
        }

        return contem;
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        parametroConsulta = (ServicoParametroConsulta) intent.getSerializableExtra(ServicoParametroConsulta.class.getName());
        associarPontoServico = (Boolean) intent.getSerializableExtra("associarPontoServico");
    }

    private void popularListaPontos() {


        if (parametroConsulta == null) {
            AlertaUtil.mensagemErro(this, "Não foi possível recuperar os critérios de pesquisa.");
            return;
        }

        servicos = servicoService.buscarPorParametroConsulta(parametroConsulta);

        if (servicos != null && !servicos.isEmpty()) {
            aplicarAdapter(0);

        } else {
            aplicarAdapter(1);
        }

    }


    public void aplicarAdapter(int v) {

        if (v == 0) {
            ServicoAdapter adapter = new ServicoAdapter();
            setListAdapter(adapter);
        } else {
            ServicoAdapter3 adapter3 = new ServicoAdapter3();
            setListAdapter(adapter3);
        }

    }

    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class ServicoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        ServicoAdapter() {
            super(ListaServicoActivity.this,
                    R.layout.rowlayout_lista_ponto, R.id.text1,
                    servicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_servico, null);

            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }

            TextView grupoN = (TextView) v.findViewById(R.id.text1N);
            TextView descricaoN = (TextView) v.findViewById(R.id.text2N);
            TextView grupo = (TextView) v.findViewById(R.id.text1);
            TextView descricao = (TextView) v.findViewById(R.id.text2);
            TextView n = (TextView) v.findViewById(R.id.text3);

            final Servico servico = (Servico) servicos.get(position);

            grupoN.setText("Grupo: ");
            if (servico.getGrupo() != null) {
                grupo.setText(servico.getGrupo().getDescricao());
            } else {
                grupo.setText("Não informado");
            }

            descricaoN.setText("Serviço: ");
            descricao.setText(servico.getDescricao() + "                                                                                                                                                                                                                                                                                                           ");


            return v;

        }

    }


    class ServicoAdapter3 extends ArrayAdapter<String> {


        ServicoAdapter3() {
            super(ListaServicoActivity.this,
                    R.layout.rowlayout_lista_ponto, R.id.text1,
                    servicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            // If convertView is null create a new view, else use convert view
            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_ponto, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Serviço Encontrado.");

            return v;

        }

    }


}


