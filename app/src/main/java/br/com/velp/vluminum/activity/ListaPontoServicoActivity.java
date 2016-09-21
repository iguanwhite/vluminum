package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import br.com.velp.vluminum.service.PontoServicoService;
import br.com.velp.vluminum.serviceimpl.PontoServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.ServicoUtil;

@EActivity(R.layout.lista_ponto_servico_activity)
public class ListaPontoServicoActivity extends ListActivityBase {

    @ViewById
    ListView list;

    private PontoServicoService pontoServicoService = new PontoServicoServiceImpl(this);

    private List servicos;

    private Ponto ponto;

    @AfterViews
    void inicializar() {
        recuperarParametros();

        popularListaServicos();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        popularListaServicos();

    }

    @Click(R.id.associarServicoBtn)
    void abrirTelaPesquisaServico() {
        Intent intent = new Intent(this, PesquisaServicoActivity_.class);
        intent.putExtra("associarPontoServico", true);
        intent.putExtra(Ponto.class.getName(), ponto);
        startActivity(intent);
        finish();
    }

    @ItemClick(android.R.id.list)
    void abrirTelaCadastroServico(Servico servico) {
        Intent intent = new Intent(this, CadastroServicoActivity_.class);
        intent.putExtra(Servico.class.getName(), servico);
        intent.putExtra(Ponto.class.getName(), ponto);
        startActivity(intent);
        finish();
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
    }

    private void popularListaServicos() {
        List<PontoServico> resultado = pontoServicoService.listarPorPonto(ponto.getId());
        servicos = new ArrayList();
        for (PontoServico pontoServico : resultado) {
            servicos.add(pontoServico.getServico());
        }

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

    private void removerServico() {
        final Servico s = ServicoUtil.getInstance().getServico();
        ServicoUtil.getInstance().setServico(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente desvincular este serviço do ponto?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            boolean result = pontoServicoService.deletarPorPontoEServico(ponto.getId(), s.getId());
                            if (result) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Ponto desvinculado com sucesso!");
                                popularListaServicos();
                            }
                        } catch (Exception ex) {
                            AlertaUtil.mensagemCamposObrigatorios(getApplicationContext(), ex.getMessage());
                        }
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

    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class ServicoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        ServicoAdapter() {
            super(ListaPontoServicoActivity.this,
                    R.layout.rowlayout_lista_servico_ponto, R.id.text1,
                    servicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_servico_ponto, null);

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


            // Pegar a posicao da mensagem na lista e atualizar.
            final Servico servico = (Servico) servicos.get(position);

            grupoN.setText("Grupo: ");
            if (servico.getGrupo() != null) {
                grupo.setText(servico.getGrupo().getDescricao() + "                                                                                                                                                                                                                                      ");
            } else {
                grupo.setText("Não informado" + "                                                                                                                                                                                                                                      ");
            }

            ImageView pr = (ImageView) v.findViewById(R.id.btnRemoverPonto);
            pr.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ServicoUtil.getInstance().setServico(servico);
                    removerServico();
                }
            });

            descricaoN.setText("Serviço: ");
            descricao.setText(servico.getDescricao());
            n.setText("");

            return v;
        }
    }

    class ServicoAdapter3 extends ArrayAdapter<String> {

        ServicoAdapter3() {
            super(ListaPontoServicoActivity.this,
                    R.layout.rowlayout_lista_servico_ponto, R.id.text1,
                    servicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            // If convertView is null create a new view, else use convert view
            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_servico_ponto, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Serviço Encontrado.");

            return v;
        }

    }

}


