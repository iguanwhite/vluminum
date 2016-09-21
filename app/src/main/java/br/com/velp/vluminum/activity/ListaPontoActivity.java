package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoResultadoConsulta;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.service.PontoService;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.serviceimpl.PontoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;

@EActivity(R.layout.lista_ponto_activity)
public class ListaPontoActivity extends ListActivityBase {

    @ViewById
    TextView qtdeSinc;

    DrawerLayout dLayout;

    @ViewById
    ListView list;

    private PontoService pontoService = new PontoServiceImpl(this);

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private List<Ponto> pontos;

    private List<PontoResultadoConsulta> pontosResultado;

    private OrdemServico ordemServico;

    private PontoParametroConsulta parametroConsulta;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    private Boolean associarPontoOs;

    ArrayAdapter<Municipio> adapter;


    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText pontoReferenciaEditText;

    @ViewById
    EditText numPlaquetaTransformadorEditText;


    @ViewById
    Spinner municipioSpinner;

    @ViewById
    TextView textView3;

    @AfterViews
    void inicializar() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        OrdemServicoUtil.getInstance().setColetarMaterial(false);

        recuperarParametros();

        carregarSpinners();

        popularListaPontos();
    }

    void carregarSpinners() {
        List<Municipio> municipios = municipioService.listarComOSAbertas();
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

    @Click(R.id.novoPontoBtn2)
    void abrirTelaPonto() {
        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
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
        parametroConsulta = new PontoParametroConsulta();

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

            popularListaPontos();
            dLayout.closeDrawers();
        }
    }


    @ItemClick(android.R.id.list)
    void selecionarPonto(PontoResultadoConsulta pontoResultadoConsulta) {

        Ponto ponto = pontoService.buscarPorId(pontoResultadoConsulta.getIdPonto());

        if (associarPontoOs != null && associarPontoOs) {
            associarPontoAOrdemServico(ponto);
        } else {
            Intent intent = new Intent(this, CadastroPontoActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
            finish();
        }
    }


    @ItemLongClick(android.R.id.list)
    void selecionarLongoPonto(PontoResultadoConsulta pontoResultadoConsulta) {

        final PontoResultadoConsulta pf = pontoResultadoConsulta;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Ponto");
        alertDialogBuilder
                .setMessage("Deseja realmente excluir o ponto selecionado?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Ponto ponto = pontoService.buscarPorId(pf.getIdPonto());
                        excluirPonto(ponto);
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


    private void excluirPonto(Ponto p)  {
        try {
            if(pontoService.deletar(p)){
                AlertaUtil.mostrarMensagem(this, "Ponto removido com sucesso");
                popularListaPontos();

            }
        } catch (RegraNegocioException e) {
            e.printStackTrace();
            AlertaUtil.mostrarMensagem(this, "Falha ao remover o Ponto.!");
            return;
        }

    }


    private void associarPontoAOrdemServico(Ponto ponto) {
        OrdemServicoPonto ordemServicoPonto = new OrdemServicoPonto();

        ordemServicoPonto.setPonto(ponto);
        ordemServicoPonto.setOs(ordemServico);
        try {
            if (osJaAssociadaAoPonto(ordemServicoPonto.getOs().getId(), ponto.getId())) {
                AlertaUtil.mostrarMensagem(this, "Ponto selecionado já associado à OS!");
                return;
            } else {
                ordemServicoPonto.setSincronizado(0);
                ordemServicoPontoService.salvarOuAtualizar(ordemServicoPonto);
            }
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(ListaPontoOsActivity.class.getName(), "Erro ao associar ponto à os", ex);
            AlertaUtil.mensagemErro(this, ex.getMessage());
            return;
        }

        AlertaUtil.mostrarMensagem(this, "Ponto associado à OS com sucesso!");

        Intent intent = new Intent(this, ListaPontoOsActivity_.class);
        intent.putExtra(OrdemServico.class.getName(), parametroConsulta.getOrdemServico());
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
    }

    private boolean osJaAssociadaAoPonto(String idOs, String idPonto) {
        List<OrdemServicoPonto> lista = ordemServicoPontoService.listarPorOs(idOs);
        List<Ponto> pontos = new ArrayList<Ponto>();
        for (OrdemServicoPonto ordemServicoPonto : lista) {
            pontos.add(ordemServicoPonto.getPonto());
        }
        boolean contem = false;
        for (Ponto p : pontos) {
            if (p.getId().equals(idPonto)) {
                contem = true;
                break;
            }
        }

        return contem;
    }

    @Click(R.id.chamarFiltros)
    void chamarFiltros() {
        dLayout.openDrawer(Gravity.LEFT);
    }

    @Click(R.id.limparPesquisaPontoBtn)
    void limpar() {
        carregarSpinners();
        logradouroEditText.setText("");
        pontoReferenciaEditText.setText("");
        numPlaquetaTransformadorEditText.setText("");
    }


    private void recuperarParametros() {
        Intent intent = getIntent();
        parametroConsulta = (PontoParametroConsulta) intent.getSerializableExtra(PontoParametroConsulta.class.getName());
        associarPontoOs = (Boolean) intent.getSerializableExtra("associarPontoOs");
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
    }

    private void setarMunicipioAtual(){
        textView3.setText("Município: " + parametroConsulta.getMunicipio().getNome());


    }

    private void ativarGPS() {
        GPSUtil gpsUtil = new GPSUtil(this);
        gpsUtil.ativarGPS();
    }

    private void popularListaPontos() {
        if (parametroConsulta == null) {
            parametroConsulta = new PontoParametroConsulta();
            Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
            if (!municipio.getNome().contains("--")) {
                parametroConsulta.setMunicipio(municipio);
            }

        }

        if (ordemServico != null) {
            pontosResultado = pontoService.buscarEspecificos(parametroConsulta);
        } else {
            pontosResultado = pontoService.buscarPorParametroConsulta2(parametroConsulta);
        }

        setarMunicipioAtual();

        // Seta o adapter
        if (pontosResultado != null && !pontosResultado.isEmpty()) {
            qtdeSinc.setText(String.valueOf(pontosResultado.size()));
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
            qtdeSinc.setText("Nenhum registro encontrado.");
        }
    }

    public void aplicarAdapter(int v) {
        if (v == 0) {
            PontoAdapter adapter = new PontoAdapter();
            setListAdapter(adapter);
        } else {
            PontoAdapter3 adapter3 = new PontoAdapter3();
            setListAdapter(adapter3);
        }
    }


    /**
     * Adapter android (Controle geral dos elementos da lista corrente).
     *
     * @author Henrique Frederico
     */
    class PontoAdapter extends ArrayAdapter<String> {
        int place = 0;
        ListView vg = null;

        PontoAdapter() {
            super(ListaPontoActivity.this, R.layout.rowlayout_lista_ponto, R.id.text1, (List) pontosResultado);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_ponto, null);


            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }


            TextView plaquetaN = (TextView) v.findViewById(R.id.text1N);
            TextView enderecoN = (TextView) v.findViewById(R.id.text2N);
            TextView osN = (TextView) v.findViewById(R.id.text3N);

            TextView plaqueta = (TextView) v.findViewById(R.id.text1);
            TextView endereco = (TextView) v.findViewById(R.id.text2);
            TextView os = (TextView) v.findViewById(R.id.text3);

            // Pegar a posicao da mensagem na lista e atualizar.
            PontoResultadoConsulta f = (PontoResultadoConsulta) pontosResultado.get(position);

            plaquetaN.setText("Plaqueta: ");
            if (f.getNumPlaquetaTransformador() != null && f.getNumPlaquetaTransformador().length() > 2) {
                plaqueta.setText(f.getNumPlaquetaTransformador());
            } else {
                plaqueta.setText("NÃO POSSUI");
            }

            enderecoN.setText("Endereço: ");
            String e = f.getLogradouro().toUpperCase() + "," + f.getNumLogradouro().toUpperCase() + " " + f.getBairro().toUpperCase();

            if (e.length() < 50) {
                endereco.setText(e + "                                                                                                                                                                                                                                      ");
            } else {
                endereco.setText(e + "                                                                                                                                                                                                                                      ");
            }

            if (f.getOrdensServicoDoPonto() != null && !f.getOrdensServicoDoPonto().isEmpty()) {
                osN.setText("OS´s: ");
                os.setText(f.getOrdensServicoDoPonto());
            }

            return v;

        }

    }


    class PontoAdapter3 extends ArrayAdapter<String> {
        PontoAdapter3() {
            super(ListaPontoActivity.this, R.layout.rowlayout_lista_ponto, R.id.text1, (List) pontosResultado);
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

            titulo.setText("     Nenhum Ponto Encontrada.");

            return v;

        }

    }


}


