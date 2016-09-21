package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.service.MaterialIdentificadoService;
import br.com.velp.vluminum.serviceimpl.MaterialIdentificadoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.MaterialUtil;

@EActivity(R.layout.lista_material_activity)
public class ListaMaterialActivity extends ListActivityBase {

    @ViewById
    ListView list;

    private MaterialIdentificadoService materialService = new MaterialIdentificadoServiceImpl(this);

    private List materiais;

    private Ponto ponto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recuperarParametrosConsulta();
    }

    @Override
    protected void onResume() {
        super.onResume();
        popularListaMateriais();

    }

    @Click(R.id.novoMaterialBtn)
    void abrirTelaCadastro() {
        MaterialIdentificado mi = (MaterialIdentificado) list.getSelectedItem();

        Intent intent = new Intent(this, CadastroMaterialActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        intent.putExtra(MaterialIdentificado.class.getName(), mi);
        startActivity(intent);
        finish();
    }

    /* @ItemClick(android.R.id.list)
     void abrirTelaCadastro(MaterialIdentificado mi) {

         //MaterialIdentificado mi =  (MaterialIdentificado)materialListView.getSelectedItem();
         Intent intent = new Intent(this, CadastroMaterialActivity_.class);
         intent.putExtra(Ponto.class.getName(), ponto);
         intent.putExtra(MaterialIdentificado.class.getName(), mi);
         startActivity(intent);
         finish();
     }

 */
    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
    }

    private void popularListaMateriais() {

        materiais = materialService.listarPorPonto(ponto.getId());
        if (materiais == null || materiais.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhum material encontrado!");
        }

        if (materiais != null && !materiais.isEmpty()) {
            List<MaterialIdentificado> listaAux = new ArrayList<MaterialIdentificado>();
            for (Object item : materiais) {
                MaterialIdentificado i = (MaterialIdentificado) item;
                if (i.getSituacao() != Situacao.EM_ESTOQUE) {
                    listaAux.add(i);
                }

            }
            materiais = listaAux;
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
        }
    }

    public void aplicarAdapter(int v) {
        if (v == 0) {
            MaterialAdapter adapter = new MaterialAdapter();
            setListAdapter(adapter);
        } else {
            MaterialAdapter3 adapter3 = new MaterialAdapter3();
            setListAdapter(adapter3);
        }
    }

    private void removerMaterial() {
        final MaterialIdentificado m = MaterialUtil.getInstance().getMaterialIdentificado();
        MaterialUtil.getInstance().setMaterialIdentificado(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente desvincular este Material do Ponto?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            //  m.setIdPonto(null);
                            m.setClassificacao(1);
                            m.setSituacao(Situacao.EM_ESTOQUE);

                            if (m.getCriadoWeb() == false) {
                                m.setSincronizado(null);
                            }

                            boolean result = materialService.salvarOuAtualizar(m, false);
                            materiais.remove(m);
                            if (result) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Material desvinculado com sucesso!");
                                popularListaMateriais();
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        MaterialIdentificado m = (MaterialIdentificado) l.getItemAtPosition(position);
        if (m.getClassificacao() != 0) {
            MaterialUtil.getInstance().setMaterialIdentificado(m);
            removerMaterial();
        }
    }

    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class MaterialAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        MaterialAdapter() {
            super(ListaMaterialActivity.this,
                    R.layout.rowlayout_lista_material, R.id.text1,
                    materiais);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_material, null);

            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }


            TextView tipoN = (TextView) v.findViewById(R.id.text1N);
            TextView materialN = (TextView) v.findViewById(R.id.text2N);
            TextView tipo = (TextView) v.findViewById(R.id.text1);
            TextView material = (TextView) v.findViewById(R.id.text2);

            // Pegar a posicao da mensagem na lista e atualizar.
            final MaterialIdentificado m = (MaterialIdentificado) materiais.get(position);

            tipoN.setText("Tipo: ");
            if (m.getMaterial() != null && m.getMaterial().getTipo() != null && m.getMaterial().getTipo().getDescricao() != null) {
                tipo.setText(m.getMaterial().getTipo().getDescricao()
                        + "                                                                                                                                                                                            ");

            }

            materialN.setText("Material: ");
            if (m.getMaterial() != null && m.getMaterial().getDescricao() != null) {
                material.setText(m.getMaterial().getDescricao());
            }

            MaterialUtil.getInstance().setMaterialIdentificado(m);

            ImageView pr = (ImageView) v.findViewById(R.id.btnRemoverPonto);
            pr.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    removerMaterial();
                }
            });

            if (m.getClassificacao() == 0) {
                // v.setBackgroundColor(Color.rgb(0, 204, 102));
                v.setBackgroundColor(Color.parseColor("#D0D0D0"));
                pr.setVisibility(View.INVISIBLE);


                tipoN.setTextColor(Color.parseColor("#A8A8A8"));
                tipo.setTextColor(Color.parseColor("#A8A8A8"));

                materialN.setTextColor(Color.parseColor("#A8A8A8"));
                material.setTextColor(Color.parseColor("#A8A8A8"));


            }


            /*else if (m.getClassificacao() == 2) {
                //v.setBackgroundColor(Color.rgb(0, 204, 102));
                v.setBackgroundColor(Color.parseColor("#009900"));


            } */


            return v;
        }
    }

    class MaterialAdapter3 extends ArrayAdapter<String> {

        MaterialAdapter3() {
            super(ListaMaterialActivity.this,
                    R.layout.rowlayout_lista_ponto, R.id.text1,
                    materiais);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            // If convertView is null create a new view, else use convert view
            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_material, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Material Encontrado.");

            return v;
        }

    }


}


