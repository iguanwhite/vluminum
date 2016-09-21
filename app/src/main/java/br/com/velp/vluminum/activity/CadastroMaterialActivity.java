package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.GrupoService;
import br.com.velp.vluminum.service.MaterialIdentificadoService;
import br.com.velp.vluminum.service.MaterialService;
import br.com.velp.vluminum.service.TipoService;
import br.com.velp.vluminum.serviceimpl.GrupoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialIdentificadoServiceImpl;
import br.com.velp.vluminum.serviceimpl.MaterialServiceImpl;
import br.com.velp.vluminum.serviceimpl.TipoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;
import br.com.velp.vluminum.util.SpinnerUtil;

@EActivity(R.layout.cadastro_material_activity)
public class CadastroMaterialActivity extends ActivityBase {

    @ViewById
    Spinner grupoMaterialSpinner;

    @ViewById
    Spinner tipoMaterialSpinner;

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

    private TipoService tipoService = new TipoServiceImpl(this);

    private Ponto ponto;

    private MaterialIdentificado materialIdentificado;

    private Material material;

    private Grupo grupo;

    private Tipo tipo;

    private static final int LER_CODIGO_BARRAS_REQUEST_CODE = 0;

    @AfterViews
    void inicializar() {
        recuperarParametros();

        carregarSpinners();
    }

    @Override
    public void onBackPressed() {
        voltarTelaAnterior();
    }

    private void verificarNumeroSerie() {
        if (OrdemServicoUtil.getInstance().isColetarMaterial()) {
            textNumSerie.setVisibility(View.VISIBLE);
            numSerieEditText.setVisibility(View.VISIBLE);
        } else {
            textNumSerie.setVisibility(View.INVISIBLE);
            numSerieEditText.setVisibility(View.INVISIBLE);
        }
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        materialIdentificado = (MaterialIdentificado) intent.getSerializableExtra(MaterialIdentificado.class.getName());
    }

    private void carregarSpinners() {
        List<Grupo> grupos = grupoService.listar("descricao", true);
        List<Grupo> gruposComOpcaoSelecione = new LinkedList<Grupo>();
        gruposComOpcaoSelecione.add(new Grupo(null, "-- SELECIONE --"));
        gruposComOpcaoSelecione.addAll(grupos);
        grupoMaterialSpinner.setAdapter(new ArrayAdapter<Grupo>(this, android.R.layout.simple_spinner_item, gruposComOpcaoSelecione));

        if (materialIdentificado != null) {
            material = materialIdentificado.getMaterial();

            grupo = grupoService.buscarPorGrupo(material.getTipo().getGrupo().getId());

            grupoMaterialSpinner.setSelection(SpinnerUtil.getIndice(grupoMaterialSpinner, grupo.getDescricao()));
        }
    }

    @ItemSelect(R.id.grupoMaterialSpinner)
    public void grupoClicked(boolean selected, int position) {
        Grupo grupo = (Grupo) grupoMaterialSpinner.getSelectedItem();

        List<Tipo> tipos = tipoService.listarPorGrupo(grupo.getId());
        List<Tipo> tiposComOpcaoSelecione = new LinkedList<Tipo>();
        tiposComOpcaoSelecione.add(new Tipo(null, "-- SELECIONE --", null));
        tiposComOpcaoSelecione.addAll(tipos);
        tipoMaterialSpinner.setAdapter(new ArrayAdapter<Tipo>(this, android.R.layout.simple_spinner_item, tiposComOpcaoSelecione));
        if (tipo != null) {
            tipoMaterialSpinner.setSelection(SpinnerUtil.getIndice(tipoMaterialSpinner, tipo.getDescricao()));
        }
    }

    @ItemSelect(R.id.tipoMaterialSpinner)
    public void tipoClicked(boolean selected, int position) {
        Tipo tipo = (Tipo) tipoMaterialSpinner.getSelectedItem();

        List<Material> listaMateriais = materialService.listarPorTipo(tipo.getId());
        materialSpinner.setAdapter(new ArrayAdapter<Material>(this, android.R.layout.simple_spinner_item, listaMateriais));
        if (materialSpinner != null && material != null && material.getDescricao() != null) {
            materialSpinner.setSelection(SpinnerUtil.getIndice(materialSpinner, material.getDescricao()));
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

    @Click(R.id.salvarMaterialBtn)
    void salvar() {
        boolean result = false;
        // TODO: E SE ESSE MATERIAL SELECIONADO, INFORMADO JÁ ESTIVER ASSOCIADO A OUTRO PONTO????
        MaterialIdentificado materialIdentificado = null;
        Material material = (Material) materialSpinner.getSelectedItem();

        if (this.materialIdentificado != null) {
            materialIdentificado = this.materialIdentificado;
        } else {
            materialIdentificado = new MaterialIdentificado();
        }

        materialIdentificado.setMaterial(material);
        materialIdentificado.setIdPonto(ponto.getId());
        materialIdentificado.setIdCliente(ponto.getIdCliente());
        materialIdentificado.setSincronizado(0);
        materialIdentificado.setClassificacao(2);
        materialIdentificado.setCriadoWeb(false);
        materialIdentificado.setDataAlteracao(new Date());
        if (numSerieEditText.getText() != null && !numSerieEditText.getText().toString().equals("")) {
            materialIdentificado.setNumSerie(numSerieEditText.getText().toString());
        }
        try {
            result = materialIdentificadoService.salvarOuAtualizar(materialIdentificado, false);
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroPosteActivity.class.getName(), "Erro ao salvar Material Identificado", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        if (result) {
            AlertaUtil.mostrarMensagem(this, "Material cadastrado com sucesso!");
            voltarTelaAnterior();
        }
    }

    private void voltarTelaAnterior() {
        Intent intent = new Intent(this, ListaMaterialActivity_.class);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
        }
        startActivity(intent);
        finish();
    }

    private void carregarDadosDoMaterialIdentificado() {
        if (materialIdentificado != null && materialIdentificado.getIdMaterialIdentificado() != null) {
            if (materialIdentificado.getMaterial() != null) {
                Tipo tipo = materialIdentificado.getMaterial().getTipo();
                tipoMaterialSpinner.setSelection(SpinnerUtil.getIndice(tipoMaterialSpinner, tipo.getDescricao()));

                Grupo grupo = grupoService.buscarPorGrupo(tipo.getGrupo().getId());
                grupoMaterialSpinner.setSelection(SpinnerUtil.getIndice(grupoMaterialSpinner, grupo.getDescricao()));

                Material material = materialIdentificado.getMaterial();
                materialSpinner.setSelection(SpinnerUtil.getIndice(materialSpinner, material.getDescricao()));

                numSerieEditText.setText(materialIdentificado.getNumSerie());
            }
        }
    }

}
