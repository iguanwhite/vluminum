package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.GrupoService;
import br.com.velp.vluminum.service.MaterialIdentificadoService;
import br.com.velp.vluminum.service.MaterialService;
import br.com.velp.vluminum.service.OrdemServicoServicoService;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.service.TipoService;
import br.com.velp.vluminum.serviceimpl.GrupoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialIdentificadoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.ServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.TipoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;

import android.widget.CompoundButton.OnCheckedChangeListener;

import br.com.velp.vluminum.util.MaterialUtil;
import br.com.velp.vluminum.util.SpinnerUtil;

@EActivity(R.layout.tab_material_activity)
public class TabCadastroMaterialActivity extends ListActivityBase {

    private static final int LER_CODIGO_BARRAS_REQUEST_CODE = 0;
    List<Tipo> listaTipos;
    List<Material> listaMateriais;
    @ViewById
    Button btnAbaServico;
    @ViewById
    Button btnAbaMaterial;
    @ViewById
    Spinner grupoMaterialSpinner;
    @ViewById
    Spinner materialSpinner;
    @ViewById
    TextView textNumSerie;
    @ViewById
    EditText numSerieEditText;
    @ViewById
    Button lerCodigoBarrasBtn;
    private MaterialIdentificadoService materialIdentificadoService = new MaterialIdentificadoServiceImpl(this);
    private MaterialService materialService = new MaterialServiceImpl(this);
    private GrupoService grupoService = new GrupoServiceImpl(this);
    private ServicoService servicoService = new ServicoServiceImpl(this);
    private OrdemServicoServicoService osServicoService = new OrdemServicoServicoServiceImpl(this);
    private TipoService tipoService = new TipoServiceImpl(this);
    private Ponto ponto;
    private MaterialIdentificado materialIdentificado;
    private Material material;
    private Grupo grupo;
    private Tipo tipo;
    private List materiais;
    private OrdemServico ordemServico;

    @Click(R.id.btnAbaServico)
    void abaServico() {
        Intent intent = new Intent(this, TabCadastroServicoActivity_.class);
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

        btnAbaMaterial.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        textNumSerie.setFocusable(true);
        textNumSerie.setFocusableInTouchMode(true);
        textNumSerie.requestFocus();

        popularListaMateriais();
    }


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = null;
        if (ponto != null) {

            intent = new Intent(this, ListaPontoOsActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);

        } else {
            intent = new Intent(this, CadastroOsActivity_.class);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);

        }

        startActivity(intent);


    }

    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        materialIdentificado = (MaterialIdentificado) intent.getSerializableExtra(MaterialIdentificado.class.getName());

    }

    private void popularListaMateriais() {
        if (ponto != null) {
            materiais = materialIdentificadoService.listarPorPonto(ponto.getId());
        } else {
            materiais = materialIdentificadoService.listarPorOs(ordemServico.getId());

        }

        if (materiais == null || materiais.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhum material encontrado!");
        }

        if (materiais != null && !materiais.isEmpty()) {
            excluirRemovidos();
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
        }

    }

    private void excluirRemovidos() {
        List copia = materiais;

        for (Object o : copia) {
            MaterialIdentificado m = (MaterialIdentificado) o;
            if (m.getClassificacao() == 0 && (m.getSincronizado() != null && m.getSincronizado() == 1)) {
           // if (m.getClassificacao() == 0 && m.getSincronizado() == 1) {
                materiais.remove(m);
            }
        }
    }


    public void aplicarAdapter(int v) {
        if (v == 0) {
            MaterialAdapter adapter = new MaterialAdapter();
            Parcelable state = getListView().onSaveInstanceState();
            getListView().setAdapter(adapter);
            getListView().onRestoreInstanceState(state);
            setListAdapter(adapter);
        } else {
            MaterialAdapter3 adapter3 = new MaterialAdapter3();
            setListAdapter(adapter3);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        MaterialIdentificado m = (MaterialIdentificado) l.getItemAtPosition(position);
        if (m.getClassificacao() != 0) {
            MaterialUtil.getInstance().setMaterialIdentificado(m);
            removerMaterial();
        }
    }

    private void removerMaterial() {
        final MaterialIdentificado m = MaterialUtil.getInstance().getMaterialIdentificado();
        MaterialUtil.getInstance().setMaterialIdentificado(null);


        final Dialog dialog = new Dialog(TabCadastroMaterialActivity.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Vluminum");
        dialog.setCancelable(true);

        final LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.LinearLayout03);


        final RadioGroup radio = (RadioGroup) dialog.findViewById(R.id.radio);

        final RadioButton rdEstoque = (RadioButton) dialog.findViewById(R.id.rdEstoque);
        rdEstoque.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
            }
        });

        final RadioButton rdDanificado = (RadioButton) dialog.findViewById(R.id.rdDanificado);
        rdDanificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
            }
        });

        final RadioButton rdTroca = (RadioButton) dialog.findViewById(R.id.rdTroca);
        rdTroca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });

        Button btnNao = (Button) dialog.findViewById(R.id.btnNao);
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button btnSim = (Button) dialog.findViewById(R.id.btnSim);
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rdTroca.isChecked()) {

                    EditText numSerie = (EditText) dialog.findViewById(R.id.numSerieEditTextCustom);

                    try {

                        m.setNumSerie(numSerie.getText().toString());
                        boolean result = materialIdentificadoService.salvarOuAtualizar(m, true, null, 0);
                        if (result) {
                            AlertaUtil.mostrarMensagem(TabCadastroMaterialActivity.this, "Material atualizado com sucesso!");


                            if (m.getMaterial() != null) {
                                adicionarServico(m, false, true); //retira e
                                adicionarServico(m, true, true);  // coloca novamente.
                            }
                            onResume();
                            dialog.cancel();
                        }
                    } catch (Exception ex) {
                        AlertaUtil.mensagemCamposObrigatorios(TabCadastroMaterialActivity.this, ex.getMessage());
                    }

                } else if (!rdEstoque.isChecked() && !rdDanificado.isChecked()) {
                    Toast toast = Toast.makeText(TabCadastroMaterialActivity.this, "Selecione se o Material está em estoque, danificado ou troca antes de confirmar. ", Toast.LENGTH_LONG);
                    toast.show();

                } else {
                    try {
                        if (rdEstoque.isChecked()) {
                            m.setSituacao(1);
                        } else {
                            m.setSituacao(3);
                        }

                        m.setClassificacao(0);
                        boolean result = materialIdentificadoService.salvarOuAtualizar(m, true, null, 0);
                        if (result) {
                            AlertaUtil.mostrarMensagem(TabCadastroMaterialActivity.this, "Material desvinculado com sucesso!");


                            if (m.getMaterial() != null) {
                                adicionarServico(m, false, true);
                            }

                            onResume();
                            dialog.cancel();
                        }
                    } catch (Exception ex) {
                        AlertaUtil.mensagemCamposObrigatorios(TabCadastroMaterialActivity.this, ex.getMessage());
                    }

                }
            }
        });


        dialog.show();
    }

    void carregarSpinners() {
     //   List<Grupo> grupos = grupoService.listar("descricao", true);
        List<Grupo> grupos = grupoService.listarCompletos();
        List<Grupo> gruposComOpcaoSelecione = new LinkedList<Grupo>();
        gruposComOpcaoSelecione.add(new Grupo(null, "-- SELECIONE --"));
        gruposComOpcaoSelecione.addAll(grupos);
        grupoMaterialSpinner.setAdapter(new ArrayAdapter<Grupo>(this, android.R.layout.simple_spinner_item, gruposComOpcaoSelecione));

        if (materialIdentificado != null) {

            material = materialIdentificado.getMaterial();
            tipo = material.getTipo();

            grupo = grupoService.buscarPorGrupo(material.getTipo().getGrupo().getId());

            grupoMaterialSpinner.setSelection(SpinnerUtil.getIndice(grupoMaterialSpinner, grupo.getDescricao()));
        }
    }

    @ItemSelect(R.id.grupoMaterialSpinner)
    public void grupoClicked(boolean selected, int position) {

        Grupo grupo = (Grupo) grupoMaterialSpinner.getSelectedItem();

        listaTipos = new LinkedList<Tipo>();
        listaTipos.add(new Tipo(null, "-- SELECIONE --", null));
        listaTipos.addAll(tipoService.listarPorGrupo(grupo.getId()));

        tipoClicked(false, 0);
    }

    @ItemSelect(R.id.tipoMaterialSpinner)
    public void tipoClicked(boolean selected, int position) {
        listaMateriais = new ArrayList<Material>();

        for (Tipo t : listaTipos) {
            List<Material> listM = materialService.listarPorTipo(t.getId());
            for (Material m : listM) {
                listaMateriais.add(m);
            }
        }

        materialSpinner.setAdapter(new ArrayAdapter<Material>(this, android.R.layout.simple_spinner_item, listaMateriais));
        if (material != null) {
            materialSpinner.setSelection(SpinnerUtil.getIndice(materialSpinner, material.getDescricao()));
        }
    }

    @Click(R.id.salvarMaterialBtn)
    void salvar() {
        boolean result = false;

        Grupo grupo = (Grupo) grupoMaterialSpinner.getSelectedItem();

        if (grupo.getDescricao().contains("SELECIONE")) {
            AlertaUtil.mensagemAlerta(this, "Selecione o Grupo do Material.");
            return;
        }


        MaterialIdentificado materialIdentificado = null;
        Material material = (Material) materialSpinner.getSelectedItem();

        if (this.materialIdentificado != null) {
            materialIdentificado = this.materialIdentificado;
        } else {
            materialIdentificado = new MaterialIdentificado();
            materialIdentificado.setIdMaterialIdentificado(UUID.randomUUID().toString());
        }

        materialIdentificado.setMaterial(material);
        if (ponto != null) {
            materialIdentificado.setIdPonto(ponto.getId());
        }

        materialIdentificado.setClassificacao(2);
        materialIdentificado.setSituacao(2);
        materialIdentificado.setSincronizado(0);

        materialIdentificado.setIdOs(ordemServico.getId());

        materialIdentificado.setNumSerie(numSerieEditText.getText().toString());


        try {
            result = materialIdentificadoService.salvarOuAtualizar(materialIdentificado, true, this.getBaseContext(), grupo.getId());
        } catch (Exception ex) {
            Log.e(TabCadastroMaterialActivity.class.getName(), "Erro ao salvar Material Identificado", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        if (result) {
            AlertaUtil.mostrarMensagem(this, "Material cadastrado com sucesso!");
            adicionarServico(materialIdentificado, true, false);
            numSerieEditText.setText("");

            onResume();
        }
    }

    private boolean isValidBeforeSave() {


        return true;
    }

    private void adicionarServico(MaterialIdentificado m, boolean insercao, boolean troca) {
        OrdemServicoServico os = new OrdemServicoServico();
        os.setPonto(ponto);
        os.setMaterialIdentificado(m);
        os.setSincronizado(0);

        boolean result = false;

        Servico s = null;


        Grupo g = null;
        if (insercao) {


            if (troca) {
                g = m.getMaterial().getTipo().getGrupo();
            } else {
                g = (Grupo) grupoMaterialSpinner.getSelectedItem();
            }

            if (g.getId() == 1) { //Braço
                s = servicoService.buscarPorId(851);
            } else if (g.getId() == 2) { // Lâmpada
                s = servicoService.buscarPorId(845);
            } else if (g.getId() == 3) { // Luminária
                s = servicoService.buscarPorId(855);
            } else if (g.getId() == 6) { //Reator
                s = servicoService.buscarPorId(849);
            } else if (g.getId() == 8) { //Diversos
                s = servicoService.buscarPorId(854);
            } else if (g.getId() == 7) { //Rele
                s = servicoService.buscarPorId(848);
            }


        } else { // Remoção

            if (troca) {
                g = m.getMaterial().getTipo().getGrupo();
            } else {
                g = (Grupo) grupoMaterialSpinner.getSelectedItem();
            }

            if (g.getId() == 1) { //Braço
                s = servicoService.buscarPorId(852);
            } else if (g.getId() == 2) { // Lâmpada
                s = servicoService.buscarPorId(846);
            } else if (g.getId() == 3) { // Luminária
                s = servicoService.buscarPorId(856);
            } else if (g.getId() == 6) { //Reator
                s = servicoService.buscarPorId(850);
            } else if (g.getId() == 8) { //Diversos
                s = servicoService.buscarPorId(859);
            } else if (g.getId() == 7) { //Rele
                s = servicoService.buscarPorId(847);
            }
        }

        os.setServico(s);

        try {
            //Atualização de Serviços para Sincronismo.

            if (s != null) {
                s.setSincronizado(0); // Apenas para Servicos completos (Grupo + Descricao Servico)
                result = servicoService.salvarOuAtualizar(s);
            } else {
                result = true; // Só Material, sem serviço.
            }

        } catch (Exception e) {
            Log.e(CadastroServicoActivity.class.getName(), "Erro ao atualizar Serviço", e);
            AlertaUtil.mensagemErro(this, e.getMessage());
            return;
        }

        if (result) {
            try {
                osServicoService.salvarOuAtualizar(os);
            } catch (RegraNegocioException e) {
                e.printStackTrace();
            }

        }
    }


    @Click(R.id.lerCodigoBarrasBtn)
    void lerCodigoBarras() {
        final String nomePacoteAppCodBarras = "com.google.zxing.client.android.SCAN";
        try {
            Intent intent = new Intent(nomePacoteAppCodBarras);
            startActivityForResult(intent, LER_CODIGO_BARRAS_REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Vluminum");
            alertDialogBuilder
                    .setMessage("Para ler o Código de Barras é necessário instalar a aplicação 'Barcode Scanner'.\n Deseja instalar?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://search?q=" + nomePacoteAppCodBarras));
                            startActivity(marketIntent);
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
    }

    @OnActivityResult(LER_CODIGO_BARRAS_REQUEST_CODE)
    void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            if (format != null && !format.equals("CODE_128")) {
                AlertaUtil.mostrarMensagem(this, "Código de Barras de formato inválido!");
            }
            if (contents != null && !contents.isEmpty()) {
                String idMaterial = contents;
                materialIdentificado = materialIdentificadoService.buscarPorId(idMaterial);
                if (materialIdentificado == null || materialIdentificado.getIdMaterialIdentificado() == null) {
                    AlertaUtil.mostrarMensagem(this, "Material Identificado não encontrado!");
                } else {
                    carregarDadosDoMaterialIdentificado();
                }
            }
        } else {
            AlertaUtil.mostrarMensagem(this, "Código de Barras não capturado!");
        }
    }

    private void carregarDadosDoMaterialIdentificado() {
        if (materialIdentificado != null && materialIdentificado.getIdMaterialIdentificado() != null) {
            if (materialIdentificado.getMaterial() != null) {
                Tipo tipo = materialIdentificado.getMaterial().getTipo();
                Grupo grupo = grupoService.buscarPorGrupo(tipo.getGrupo().getId());
                grupoMaterialSpinner.setSelection(SpinnerUtil.getIndice(grupoMaterialSpinner, grupo.getDescricao()));

                Material material = materialIdentificado.getMaterial();
                materialSpinner.setSelection(SpinnerUtil.getIndice(materialSpinner, material.getDescricao()));

                numSerieEditText.setText(materialIdentificado.getNumSerie());
            }
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
    class MaterialAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        MaterialAdapter() {
            super(TabCadastroMaterialActivity.this,
                    R.layout.rowlayout_lista_material, R.id.text1,
                    materiais);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_material_serie, null);

            v.setHorizontalScrollBarEnabled(false);
            v.setVerticalScrollBarEnabled(false);

            TextView tipoN = (TextView) v.findViewById(R.id.text1N);
            TextView materialN = (TextView) v.findViewById(R.id.text2N);
            TextView tipo = (TextView) v.findViewById(R.id.text1);
            TextView material = (TextView) v.findViewById(R.id.text2);
            TextView numero = (TextView) v.findViewById(R.id.text3);
            TextView numeroN = (TextView) v.findViewById(R.id.text3N);

            MaterialIdentificado m = (MaterialIdentificado) materiais.get(position);


            tipoN.setText("Tipo: ");

            if (m.getMaterial() != null) {
                tipo.setText(m.getMaterial().getTipo().getDescricao());
            } else {
                tipo.setText("Não Informado");
            }

            materialN.setText("Material: ");
            if (m.getMaterial() != null) {
                material.setText(m.getMaterial().getDescricao() + "                                                                                                                                                                                                                                      ");
            } else {
                material.setText("Não Informado" + "                                                                                                                                                                                                                                      ");
            }

            numeroN.setText("Nº de Série: ");
            if (m.getNumSerie() != null && m.getNumSerie().trim().length() > 0) {
                numero.setText(m.getNumSerie());
            } else {
                numero.setText("Não Informado");
            }


            MaterialUtil.getInstance().setMaterialIdentificado(m);

            ImageView pr = (ImageView) v.findViewById(R.id.btnRemoverPonto);

            if (m.getClassificacao() == 0) {
                v.setBackgroundColor(Color.parseColor("#D0D0D0"));

                tipoN.setTextColor(Color.parseColor("#A8A8A8"));
                tipo.setTextColor(Color.parseColor("#A8A8A8"));

                materialN.setTextColor(Color.parseColor("#A8A8A8"));
                material.setTextColor(Color.parseColor("#A8A8A8"));

                numero.setTextColor(Color.parseColor("#A8A8A8"));
                numeroN.setTextColor(Color.parseColor("#A8A8A8"));


            }

            return v;
        }
    }

    class MaterialAdapter3 extends ArrayAdapter<String> {

        MaterialAdapter3() {
            super(TabCadastroMaterialActivity.this,
                    R.layout.rowlayout_lista_material_serie, R.id.text1,
                    materiais);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_material_serie, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Material Encontrado.");

            return v;
        }

    }

}
