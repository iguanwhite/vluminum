package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.GrupoServicoService;
import br.com.velp.vluminum.service.OrdemServicoServicoService;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.serviceimpl.GrupoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.ServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.OsServicoUtil;
import br.com.velp.vluminum.util.SpinnerUtil;

@EActivity(R.layout.tab_servico_activity)
public class TabCadastroServicoActivity extends ListActivityBase {

    List<GrupoServico> grupos;

    List<Servico> listaServicos;

    @ViewById
    Spinner grupoServicoSpinner;

    @ViewById
    Spinner servicoSpinner;

    @ViewById
    Button salvarServicoBtn;

    @ViewById
    Button btnAbaMaterial;

    @ViewById
    Button btnAbaServico;

    private OrdemServico ordemServico;

    private GrupoServicoService grupoService = new GrupoServicoServiceImpl(this);

    private ServicoService servicoService = new ServicoServiceImpl(this);

    private OrdemServicoServicoService osServicoService = new OrdemServicoServicoServiceImpl(this);

    private Ponto ponto;

    private Grupo grupo;

    private Tipo tipo;

    private Servico servico;

    private List osServicos;

    @Click(R.id.btnAbaMaterial)
    void abaMaterial() {
        Intent intent = new Intent(this, TabCadastroMaterialActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        finish();
    }

    @AfterViews
    void inicializar() {
        recuperarParametrosConsulta();

        carregarSpinners();

        btnAbaServico.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        popularListaServico();

    }


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = null;
        if (ponto != null) {

            intent = new Intent(this, ListaPontoOsActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);

        }else{
            intent = new Intent(this, CadastroOsActivity_.class);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);

        }

        startActivity(intent);


    }



    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        servico = (Servico) intent.getSerializableExtra(Servico.class.getName());
    }


    private void popularListaServico() {

        if(ponto != null){
            osServicos = osServicoService.listarPorPonto(ponto.getId());

        }else{
            osServicos = osServicoService.listarPorOrdemServico(ordemServico.getId());

        }

        if (osServicos == null || osServicos.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhum serviço encontrado!");
        }

        if (osServicos != null && !osServicos.isEmpty()) {
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
        }



    }

    public void aplicarAdapter(int v) {
        if (v == 0) {
            OsServicoAdapter adapter = new OsServicoAdapter();

            Parcelable state = getListView().onSaveInstanceState();

            getListView().setAdapter(adapter);

            getListView().onRestoreInstanceState(state);

            setListAdapter(adapter);
        } else {
            OsServicoAdapter3 adapter3 = new OsServicoAdapter3();
            setListAdapter(adapter3);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        OrdemServicoServico m = (OrdemServicoServico) l.getItemAtPosition(position);

        if (!m.getServico().getGrupo().getDescricao().contains("INSTALAÇÃO") && !m.getServico().getGrupo().getDescricao().contains("REMOÇÃO")) {
            OsServicoUtil.getInstance().setOrdemServicoServico(m);

            removerServico();
        }

    }

    private void removerServico() {
        final OrdemServicoServico m = OsServicoUtil.getInstance().getOrdemServicoServico();
        OsServicoUtil.getInstance().setOrdemServicoServico(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente desvincular este Serviço do Ponto?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            boolean result = osServicoService.deletar(m);
                            if (result) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Serviço desvinculado com sucesso!");
                                onResume();
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

    private void carregarDadosServico(Servico servico) {
        grupoServicoSpinner.setSelection(SpinnerUtil.getIndice(grupoServicoSpinner, servico.getGrupo().getDescricao()));
        salvarServicoBtn.setText("Atualizar");
    }

    void carregarSpinners() {
        grupos = grupoService.listar("descricao", true);

        if (grupos != null && grupos.size() > 0) {
            List<GrupoServico> gruposComOpcaoSelecione = new LinkedList<GrupoServico>();
            gruposComOpcaoSelecione.add(new GrupoServico(null, "-- SELECIONE --"));
            gruposComOpcaoSelecione.addAll(grupos);

            grupoServicoSpinner.setAdapter(new ArrayAdapter<GrupoServico>(this, android.R.layout.simple_spinner_item, gruposComOpcaoSelecione));

            if (servico != null) {
                carregarDadosServico(servico);
            }

        }

    }

    @ItemSelect(R.id.grupoServicoSpinner)
    public void grupoClicked(boolean selected, int position) {

        GrupoServico grupo = (GrupoServico) grupoServicoSpinner.getSelectedItem();

        listaServicos = servicoService.listarPorGrupo(grupo.getId());

        servicoSpinner.setAdapter(new ArrayAdapter<Servico>(this, android.R.layout.simple_spinner_item, listaServicos));

        tioClicked(false, 0);
    }

    @ItemSelect(R.id.servicoSpinner)
    public void tioClicked(boolean selected, int position) {

        servico = (Servico) servicoSpinner.getSelectedItem();
        if (servico != null) {
            servicoSpinner.setSelection(SpinnerUtil.getIndice(servicoSpinner, servico.getDescricao()));
        }


    }

    @Click(R.id.salvarServicoBtn)
    void salvar() {

        boolean result = false;
        boolean edit = false;

        servico = (Servico) servicoSpinner.getSelectedItem();





        GrupoServico g = (GrupoServico) grupoServicoSpinner.getSelectedItem();

        if (g.getDescricao().contains("SELECIONE")) {
            AlertaUtil.mensagemAlerta(this, "Selecione o Grupo de Serviço.");
            return;
        }



        OrdemServicoServico os = new OrdemServicoServico();

        if (this.servico != null) {
            servico = this.servico;
            edit = true;

        } else {
            servico = new Servico();
            Random rand = new Random();
            servico.setDescricao("Não Possui");
            int randomNum = rand.nextInt((6000 - 1) + 1) + 1;

            servico.setId(randomNum);
        }
        if (g != null) {
            servico.setGrupo(g);
        }


        try {
            //Atualização de Serviços para Sincronismo.
            if (edit) {
                servico.setSincronizado(0); // Apenas para Servicos completos (Grupo + Descricao Servico)
            }

            result = servicoService.salvarOuAtualizar(servico);
        } catch (Exception e) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao salvar Serviço", e);
            AlertaUtil.mensagemErro(this, e.getMessage());
            return;
        }
        os.setServico(servico);
        os.setPonto(ponto);
        os.setOs(ordemServico);
      //  os.setPonto(null);
        os.setSincronizado(0);

        try {
            result = osServicoService.salvarOuAtualizar(os);
        } catch (Exception e) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao salvar Serviço", e);
            AlertaUtil.mensagemErro(this, e.getMessage());
            return;
        }

        if (result) {
            if (!edit) {
                AlertaUtil.mostrarMensagem(this, "Serviço cadastrado e vinculado ao Ponto com sucesso!");
            } else {
                AlertaUtil.mostrarMensagem(this, "Serviço vinculado atualizado com sucesso!");
            }

            onResume();
        }
    }

    private void voltarTelaCadastroPonto() {
        finish();
    }

    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class OsServicoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        OsServicoAdapter() {
            super(TabCadastroServicoActivity.this,
                    R.layout.rowlayout_lista_material, R.id.text1,
                    osServicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_material, null);


            TextView grupoN = (TextView) v.findViewById(R.id.text1N);
            TextView servicoN = (TextView) v.findViewById(R.id.text2N);
            TextView grupo = (TextView) v.findViewById(R.id.text1);
            TextView servico = (TextView) v.findViewById(R.id.text2);


            OrdemServicoServico m = (OrdemServicoServico) osServicos.get(position);


            grupoN.setText("Grupo: ");
            if (m.getServico().getGrupo() != null) {
                grupo.setText(m.getServico().getGrupo().getDescricao() + "                                                                                                                                                                                                                                      ");

            } else {
                grupo.setText("Não Possui" + "                                                                                                                                                                                                                                      ");
            }


            servicoN.setText("Serviço: ");
            servico.setText(m.getServico().getDescricao());

            OsServicoUtil.getInstance().setOrdemServicoServico(m);

            ImageView pr = (ImageView) v.findViewById(R.id.btnRemoverPonto);
            pr.setVisibility(View.INVISIBLE);

            return v;
        }
    }

    class OsServicoAdapter3 extends ArrayAdapter<String> {

        OsServicoAdapter3() {
            super(TabCadastroServicoActivity.this,
                    R.layout.rowlayout_lista_ponto, R.id.text1,
                    osServicos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_material, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Serviço Encontrado.");

            return v;
        }

    }

}
