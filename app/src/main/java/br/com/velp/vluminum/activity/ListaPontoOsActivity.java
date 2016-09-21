package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.enumerator.SituacaoPonto;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.MotivoTrocaService;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.serviceimpl.MotivoTrocaServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoPontoServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.OrdemServicoUtil;

@EActivity(R.layout.lista_ponto_os_activity)
public class ListaPontoOsActivity extends ListActivityBase {

    @ViewById
    ListView list;

    @ViewById
    Button desvincularPontoBtn;

    private OrdemServicoPontoService ordemServicoPontoService = new OrdemServicoPontoServiceImpl(this);

    private List pontos;

    private Set<Ponto> pontosSelecionados = new HashSet<Ponto>();

    private OrdemServico ordemServico;

    private AlertDialog alerta;

    private MotivoTroca motivoEncerramentoSelecionado;

    private MotivoTrocaService motivoTrocaService = new MotivoTrocaServiceImpl(this);

    private boolean osFinalizada = false;

    @AfterViews
    void inicializar() {
        recuperarParametros();

        popularListaPontos();
    }

    @Click(R.id.associarPontoBtn)
    void abrirTelaPesquisaPonto() {
        //  Intent intent = new Intent(this, ListaPontoActivity_.class);

        Intent intent = new Intent(this, PesquisaPontoActivity_.class);
        intent.putExtra("associarPontoOs", true);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
    }

    @Click(R.id.desvincularPontoBtn)
    void desvincularPontos() {
        if (verificarSeExistemPontosSelecionados()) {
            removerPontos();
        }
    }

    @Click(R.id.finalizarPontoBtn)
    void finalizarPontos() {
        if (verificarSeExistemPontosSelecionados()) {
            atualizarOrdemServicoPontoComoFinalizado();
        }
    }

    @Override
    public void onBackPressed() {
        if(validarFinalizacaoOS()){
            voltarTelaAnterior();
        }else{
            mostrarDialogSelecaoMotivoEncerramento();
        }


    }


    private boolean validarFinalizacaoOS() {

        if (!osFinalizada) {
            return true;
        } else {
            if (motivoEncerramentoSelecionado == null) {
                return false;
            } else {
                return true;
            }
        }

    }

    private void voltarTelaAnterior() {
        if (osFinalizada) {
            Intent intent = new Intent(this, ListaOsActivity_.class);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(this, CadastroOsActivity_.class);
            if (ordemServico != null) {
                intent.putExtra(OrdemServico.class.getName(), ordemServico);
            }
            startActivity(intent);
            finish();

        }


    }

    private boolean verificarSeExistemPontosSelecionados() {
        if (pontosSelecionados == null || pontosSelecionados.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Selecione pelo menos um ponto!");
            return false;
        }
        return true;
    }

    @ItemClick(android.R.id.list)
    void abrirTelaCadastroPonto(Ponto ponto) {
        OrdemServicoUtil.getInstance().setColetarMaterial(true);
        Intent intent = new Intent(this, CadastroPontoActivity_.class);
        intent.putExtra(Ponto.class.getName(), ponto);
        intent.putExtra(OrdemServico.class.getName(), ordemServico);
        startActivity(intent);
        finish();
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
    }

    private void popularListaPontos() {
        List<OrdemServicoPonto> resultado = ordemServicoPontoService.listarPorOs(ordemServico.getId());
        pontos = new ArrayList();
        for (OrdemServicoPonto ordemServicoPonto : resultado) {
            Ponto ponto = ordemServicoPonto.getPonto();
            ponto.setSituacaoDoPontoNaOs(ordemServicoPonto.getSituacao());
            pontos.add(ponto);
        }

        if (pontos != null && !pontos.isEmpty()) {
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
        }
    }

    private void popularListaPontosFecharOs() {
        Long resultado = ordemServicoPontoService.quantidadePontosDaOsNaoFinalizados(ordemServico.getId());


        if (resultado != null && resultado == 0) {
            osFinalizada = true;
            mostrarDialogSelecaoMotivoEncerramento();
        }
        popularListaPontos();
    }


    void mostrarDialogSelecaoMotivoEncerramento() {

        final List<MotivoTroca> motivos = motivoTrocaService.listar("descricao", true);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rowlayout_lista_motivo_troca, motivos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o Motivo de Encerramento da OS:");

        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int indiceItemSelecionado) {
                motivoEncerramentoSelecionado = motivos.get(indiceItemSelecionado);

                finalizar();

                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();
    }


    private void finalizar() {

        ordemServico.setMotivoEncerramento(motivoEncerramentoSelecionado);

        boolean result = false;
        // Muda a situação da OS
        ordemServico.setSituacao(new Situacao(Situacao.SITUACAO_FINALIZADA.toString()));
        ordemServico.setSincronizado(0);

        OrdemServicoService osService = new OrdemServicoServiceImpl(this);
        try {
            result = osService.salvarOuAtualizar(ordemServico);
        } catch (RegraNegocioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroPosteActivity.class.getName(), "Erro ao finalizar OS", ex);
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
            return;
        }

        if (result) {
            String msg = "OS finalizada com sucesso!";
            AlertaUtil.mostrarMensagem(this, msg);
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

    private void consultarServicosPonto(Ponto ponto) {
        if (ponto != null) {
            Intent intent = new Intent(this, TabCadastroMaterialActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
            startActivity(intent);
            finish();
        }
    }

    private void atualizarOrdemServicoPontoComoFinalizado() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente finalizar este(s) ponto(s) da OS?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            for (Ponto p : pontosSelecionados) {
                                OrdemServicoPonto ordemServicoPonto = ordemServicoPontoService.buscarPorOsEPonto(ordemServico.getId(), p.getId());
                                ordemServicoPonto.setSituacao(SituacaoPonto.FINALIZADA.getCodigo());
                                ordemServicoPonto.setSincronizado(0);
                                ordemServicoPontoService.salvarOuAtualizar(ordemServicoPonto);
                                p.setSituacaoDoPontoNaOs(SituacaoPonto.FINALIZADA.getCodigo());
                            }

                            AlertaUtil.mostrarMensagem(getApplicationContext(), "Ponto(s) finalizado(s) com sucesso!");

                            popularListaPontosFecharOs();
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

    private void removerPontos() {
        OrdemServicoUtil.getInstance().setPonto(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente desvincular este(s) ponto(s) da OS?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            boolean existePontoFinalizado = false;
                            boolean algumPontoFoiDesvinculado = false;
                            for (Ponto p : pontosSelecionados) {
                                if (p.getSituacaoDoPontoNaOs() != null && p.getSituacaoDoPontoNaOs().equals(SituacaoPonto.FINALIZADA.getCodigo())) {
                                    existePontoFinalizado = true;
                                } else {
                                    //ordemServicoPontoService.deletarPorNumOsEPonto(ordemServico.getId(), p.getId());
                                    OrdemServicoPonto o = ordemServicoPontoService.buscarPorOsEPonto(ordemServico.getId(), p.getId());
                                    o.setOs(null);
                                    o.setSincronizado(0);
                                    ordemServicoPontoService.salvarOuAtualizar(o);

                                    algumPontoFoiDesvinculado = true;
                                }
                            }
                            if (algumPontoFoiDesvinculado) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Ponto(s) desvinculado(s) com sucesso!");
                            }
                            if (existePontoFinalizado) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Ponto(s) finalizados não podem ser desvinculados!");
                            }

                            popularListaPontos();
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
    class PontoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        PontoAdapter() {
            super(ListaPontoOsActivity.this,
                    R.layout.rowlayout_lista_os_ponto, R.id.text1,
                    pontos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rowlayout_lista_os_ponto, null);
            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }

            TextView plaquetaTitulo = (TextView) v.findViewById(R.id.titulo1);
            TextView plaquetaTexto = (TextView) v.findViewById(R.id.texto1);

            TextView enderecoTitulo = (TextView) v.findViewById(R.id.titulo2);
            TextView enderecoTexto = (TextView) v.findViewById(R.id.texto2);

            // Pegar a posicao da mensagem na lista e atualizar.
            final Ponto ponto = (Ponto) pontos.get(position);

            plaquetaTitulo.setText("Plaqueta: ");
            if (ponto.getNumPlaquetaTransf() != null && ponto.getNumPlaquetaTransf().length() > 2) {
                plaquetaTexto.setText(ponto.getNumPlaquetaTransf());
            } else {
                plaquetaTexto.setText("NÃO POSSUI");
            }

            final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        pontosSelecionados.add(ponto);
                    } else {
                        pontosSelecionados.remove(ponto);
                    }
                }
            });

            ImageView servicosBtn = (ImageView) v.findViewById(R.id.servicosBtn);
            servicosBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    consultarServicosPonto(ponto);
                }
            });

            enderecoTitulo.setText("Endereço: ");
            StringBuilder endereco = new StringBuilder();
            endereco.append(ponto.getLogradouro()).append(", Nº ").append(ponto.getNumLogradouro());
            if (ponto.getBairro() != null && !ponto.getBairro().isEmpty()) {
                endereco.append(", B. ").append(ponto.getBairro());
            }
            if (ponto.getMunicipio() != null && !ponto.getMunicipio().getNome().isEmpty()) {
                endereco.append(", ").append(ponto.getMunicipio().getNome());
            }
            if (ponto.getPontoReferencia() != null && !ponto.getPontoReferencia().isEmpty()) {
                endereco.append(" (").append(ponto.getPontoReferencia()).append(")");
            }
            enderecoTexto.setText(endereco.toString().toUpperCase());

            if (ponto.getSituacaoDoPontoNaOs() != null
                    && ponto.getSituacaoDoPontoNaOs().equals(SituacaoPonto.FINALIZADA.getCodigo())) {
                plaquetaTexto.setTextColor(Color.GRAY);
                enderecoTexto.setTextColor(Color.GRAY);
            }

            return v;
        }
    }

    class PontoAdapter3 extends ArrayAdapter<String> {

        PontoAdapter3() {
            super(ListaPontoOsActivity.this,
                    R.layout.rowlayout_lista_ponto, R.id.text1,
                    pontos);
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


