package br.com.velp.vluminum.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.service.MotivoTrocaService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.serviceimpl.MotivoTrocaServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;


@EActivity(R.layout.lista_os_ponto_activity)
public class ListaOsPontoActivity extends ListActivityBase {

    @ViewById
    TextView qtdeSinc;

    @ViewById
    ListView list;

    @ViewById
    Button novaOsBtn;

    private OrdemServicoService osService = new OrdemServicoServiceImpl(this);

    private MotivoTrocaService motivoTrocaService = new MotivoTrocaServiceImpl(this);

    private List ordemServicos;

    private Ponto ponto;

    @AfterViews
    void inicializar() {
        recuperarParametrosConsulta();

        popularListaOs();
    }

    @Override
    public void onBackPressed() {
        finish();
/*
        if (ponto != null) {
            Intent intent = new Intent(this, CadastroPontoActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
        */
    }

    @Click(R.id.novaOsBtn)
    void abrirTelaCadastroOs() {
        OrdemServicoUtil.getInstance().setNovaOS(true);
        Intent intent = new Intent(this, CadastroOsActivity_.class);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
            intent.putExtra("cadastroAcessadoAPartirDoMapa", true);

        }
        startActivity(intent);
        finish();
    }

    @ItemClick(android.R.id.list)
    void abrirTelaCadastroOs(OrdemServico os) {
        if (os.getMotivoEncerramento() != null && os.getMotivoEncerramento().getId() != null) {
            AlertaUtil.mostrarMensagem(this, "Ordem de Serviço finalizada!");
        } else {
            Intent intent = new Intent(this, CadastroOsActivity_.class);
            intent.putExtra(OrdemServico.class.getName(), os);
            if (ponto != null) {
                intent.putExtra(Ponto.class.getName(), ponto);
            }
            startActivity(intent);
            finish();
        }
    }

    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
    }

    private void popularListaOs() {
        try {
            //  if (ponto == null) {
            //      ordemServicos = osService.buscarPorUsuarioAtribuicao("data_atribuicao", true);
            //  } else {
            ordemServicos = osService.buscarPorPontoEUsuarioAtribuicao(ponto.getId(), "data_atribuicao", true);
            //   }

            if (ordemServicos != null && !ordemServicos.isEmpty()) {
                List<Object> listaAux = new ArrayList<Object>();
                for (Object item : ordemServicos) {
                    OrdemServico o = (OrdemServico) item;

                    if (o.getSincronizado() == null || o.getSincronizado() == 0) {
                        listaAux.add(o);

                    } else if (o.getSincronizado() == 1 && !o.getSituacao().getId().equals(Situacao.SITUACAO_FINALIZADA.toString())) {
                        listaAux.add(o);
                    }

                }
                ordemServicos = listaAux;
                qtdeSinc.setText(String.valueOf(ordemServicos.size()));
                excluirFinalizados();

                aplicarAdapter(0);
            } else {
                aplicarAdapter(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluirFinalizados() {
        try {
            List copia = ordemServicos;

            for (Object o : copia) {
                OrdemServico os = (OrdemServico) o;
                if (os.getMotivoEncerramento() != null && os.getSincronizado() == 1) {
                    ordemServicos.remove(os);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void aplicarAdapter(int v) {
        if (v == 0) {
            OrdemServicoAdapter adapter = new OrdemServicoAdapter();
            setListAdapter(adapter);
        } else {
            OrdemServicoAdapter3 adapter3 = new OrdemServicoAdapter3();
            setListAdapter(adapter3);
        }
    }

    private int verificarTamanhoTela() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return 3;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return 2;

            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return 1;
            default:
                return 1;
        }

    }


    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class OrdemServicoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        OrdemServicoAdapter() {
            super(ListaOsPontoActivity.this,
                    R.layout.rowlayout_lista_os, R.id.text1,
                    ordemServicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_os, null);
            TextView numOsN = (TextView) v.findViewById(R.id.text1N);
            TextView enderecoN = (TextView) v.findViewById(R.id.text2N);
            TextView motivoN = (TextView) v.findViewById(R.id.text3N);
            TextView cidadeN = (TextView) v.findViewById(R.id.text5N);

            TextView numOs = (TextView) v.findViewById(R.id.text1);
            TextView endereco = (TextView) v.findViewById(R.id.text2);
            TextView motivo = (TextView) v.findViewById(R.id.text3);
            TextView cidade = (TextView) v.findViewById(R.id.text5);

            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }

            // Pegar a posicao da mensagem na lista e atualizar.
            OrdemServico os = (OrdemServico) ordemServicos.get(position);


            //   Ordernar data atribuicao ou cadastro?

            numOsN.setText("Número: ");
            enderecoN.setText("Endereço: ");
            motivoN.setText("Motivo: ");

            cidadeN.setText("Município: ");

            int tamanho = verificarTamanhoTela();


            numOs.setText(os.getNumOs().toString());
            String e = os.getLogradouro().toUpperCase() + "," + os.getNumLogradouro().toUpperCase() + " " + os.getBairro().toUpperCase();

            if (e.length() >= 30) {
                try {
                    if (tamanho == 1 || tamanho == 2) {
                        endereco.setText(e.substring(0, 25) + " ...                                                                                                                                                                                                                    ");

                    } else if (tamanho == 3) {
                        endereco.setText(e + "                                                                                                                                                                                                                                      ");
                    }

                } catch (Exception ex) {
                    endereco.setText(e + "                                                                                                                                                                                                                                      ");
                }


            } else {
                endereco.setText(e + "                                                                                                                                                                                                                                      ");
            }

            motivo.setText(os.getMotivoAbertura().getDescricao());

            if (os.getMunicipio() != null) {
                cidade.setText(os.getMunicipio().getNome());
            } else {
                cidade.setText("NÃO INFORMADO");
            }


            if (os.getSituacao() != null && os.getSituacao().getId().equals(Situacao.SITUACAO_EM_CAMPO.toString())) {
                final int codCor = Color.parseColor("#336600");
                numOs.setTextColor(codCor);
                endereco.setTextColor(codCor);
                motivo.setTextColor(codCor);
                cidade.setTextColor(codCor);
            }

            if (os.getMotivoEncerramento() != null && os.getMotivoEncerramento().getId() != null) {
                numOs.setTextColor(Color.GRAY);
                endereco.setTextColor(Color.GRAY);
                motivo.setTextColor(Color.GRAY);
                Integer motivoId = os.getMotivoEncerramento().getId();
                MotivoTroca motivoEnc = motivoTrocaService.buscarPorId(motivoId);
                motivo.setText(motivoEnc.getDescricao());
                motivo.setTextColor(Color.GRAY);
            }

            return v;
        }

    }


    class OrdemServicoAdapter3 extends ArrayAdapter<String> {

        OrdemServicoAdapter3() {
            super(ListaOsPontoActivity.this,
                    R.layout.rowlayout_lista_os, R.id.text1,
                    ordemServicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            // If convertView is null create a new view, else use convert view
            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_os, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhuma Ordem de Serviço Encontrada.");

            return v;
        }

    }


}


