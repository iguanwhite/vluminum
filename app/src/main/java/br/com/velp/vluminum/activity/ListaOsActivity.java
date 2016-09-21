package br.com.velp.vluminum.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.enumerator.SituacaoPonto;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.MotivoTrocaService;
import br.com.velp.vluminum.service.MunicipioService;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.serviceimpl.MotivoTrocaServiceImpl;
import br.com.velp.vluminum.serviceimpl.MunicipioServiceImpl;
import br.com.velp.vluminum.serviceimpl.OrdemServicoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.DateTimeUtils;
import br.com.velp.vluminum.util.OrdemServicoUtil;


@EActivity(R.layout.lista_os_activity)
public class ListaOsActivity extends ListActivityBase {

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

    @ViewById
    EditText logradouroEditText;

    @ViewById
    EditText numOsEditText;

    @ViewById
    EditText textDataInicial;

    @ViewById
    EditText textDataFinal;

    @ViewById
    ImageView btnDataInicial;

    @ViewById
    ImageView btnDataFinal;

    static final int DATE_DIALOG_INI = 0, DATE_DIALOG_FINAL = 1;

    private int mYearInicial, mMonthInicial, mDayInicial,
            mYearFinal, mMonthFinal, mDayFinal;

    DatePickerDialog.OnDateSetListener listenerInicial, listenerFinal;

    private OsParametroConsulta parametroConsulta;

    ActionBarDrawerToggle mDrawerToggle;

    DrawerLayout dLayout;

    @ViewById
    Spinner municipioSpinner;

    private MunicipioService municipioService = new MunicipioServiceImpl(this);

    ArrayAdapter<Municipio> adapter;

    private OrdemServico ordemServico;

    @ViewById
    Spinner prioridadeSpinner;

    @ViewById
    ImageView chamarFiltros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        mYearInicial = c.get(Calendar.YEAR);
        mMonthInicial = c.get(Calendar.MONTH);
        mDayInicial = c.get(Calendar.DAY_OF_MONTH);

        mYearFinal = c.get(Calendar.YEAR);
        mMonthFinal = c.get(Calendar.MONTH);
        mDayFinal = c.get(Calendar.DAY_OF_MONTH);

        // updateDisplayInicial();
        // updateDisplayFinal();


        listenerInicial = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                mYearInicial = arg1;
                mMonthInicial = arg2;
                mDayInicial = arg3;
                updateDisplayInicial();
            }
        };


        listenerFinal = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                mYearFinal = arg1;
                mMonthFinal = arg2;
                mDayFinal = arg3;
                updateDisplayFinal();
            }
        };

    }

    @AfterViews
    void inicializar() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recuperarParametrosConsulta();
        carregarSpinners();
        popularListaOs();


        // add a click listener to the button
        btnDataInicial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_INI);
            }
        });

        // add a click listener to the button
        btnDataFinal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_FINAL);
            }
        });


    }

    private void recuperarParametrosConsulta() {
        Intent intent = getIntent();

        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        parametroConsulta = (OsParametroConsulta) intent.getSerializableExtra(OsParametroConsulta.class.getName());
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        parametroConsulta = new OsParametroConsulta();
        if (ponto != null) {

            parametroConsulta.setPonto(ponto);
        }

        //    parametroConsulta.setSincronizado(0);


        parametroConsulta.setSituacao(Situacao.SITUACAO_FINALIZADA);
    }

    @Click(R.id.chamarFiltros)
    void chamarFiltros() {
        dLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_INI:
                return new DatePickerDialog(this, listenerInicial, mYearInicial, mMonthInicial, mDayInicial);
            case DATE_DIALOG_FINAL:
                return new DatePickerDialog(this, listenerFinal, mYearFinal, mMonthFinal, mDayFinal);
        }
        return null;
    }

    private void carregarSpinners() {
        //    prioridadeSpinner.setAdapter(new ArrayAdapter<Prioridade>(this, android.R.layout.simple_spinner_item, Prioridade.values()));

        List<Municipio> municipios = municipioService.listarComOSAbertas();
        List<Municipio> municipiosComOpcaoSelecione = new LinkedList<Municipio>();
        municipiosComOpcaoSelecione.add(new Municipio(null, "-- SELECIONE --", null));
        municipiosComOpcaoSelecione.addAll(municipios);
        adapter = new ArrayAdapter<Municipio>(this, android.R.layout.simple_spinner_item, municipiosComOpcaoSelecione);
        municipioSpinner.setAdapter(adapter);

    }

    @Click(R.id.pesquisarOsBtn)
    void pesquisar() {

        Municipio municipio = (Municipio) municipioSpinner.getSelectedItem();
        parametroConsulta.setMunicipio(municipio);

        String logradouro = (logradouroEditText.getText() != null && logradouroEditText.getText().length() > 0) ? logradouroEditText.getText().toString() : "";
        parametroConsulta.setLogradouro(logradouro);

        String numeroOs = (numOsEditText.getText() != null && numOsEditText.getText().length() > 0) ? numOsEditText.getText().toString() : "";
        parametroConsulta.setNumeroOs(numeroOs);


        String dataInicial = (textDataInicial.getText() != null && textDataInicial.getText().length() > 0) ? textDataInicial.getText().toString() : null;
        String dataFinal = (textDataFinal.getText() != null && textDataFinal.getText().length() > 0) ? textDataFinal.getText().toString() : null;

        if (dataInicial != null && dataFinal != null) {
            Date dInicial = null, dFinal = null;


            try {
                dInicial = DateTimeUtils.converteStringParaDateFormatoPadrao(dataInicial);
            } catch (Exception e) {
                e.printStackTrace();

            }

            try {
                dFinal = DateTimeUtils.converteStringParaDateFormatoPadrao(dataFinal);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (dInicial.after(dFinal)) {
                Toast.makeText(this, "Data inicial não pode ser maior que a data final ", Toast.LENGTH_LONG).show();
                return;
            } else {
                parametroConsulta.setDataInicial(dInicial);
                parametroConsulta.setDataFinal(dFinal);


            }


        } else if (dataInicial != null && dataFinal == null) {
            Toast.makeText(this, "Informe a data final. ", Toast.LENGTH_LONG).show();
            return;

        } else if (dataInicial == null && dataFinal != null) {
            Toast.makeText(this, "Informe a data inicial. ", Toast.LENGTH_LONG).show();
            return;

        }

        // Prioridade prioridade = (Prioridade) prioridadeSpinner.getSelectedItem();
        //  parametroConsulta.setPrioridade(prioridade);

        parametroConsulta.setSituacao(Situacao.SITUACAO_FINALIZADA);

        popularListaOs();
        dLayout.closeDrawers();
    }


    /**
     * Método para atualizar os valores no campo data.
     *
     * @author Henrique Frederico
     */
    private void updateDisplayInicial() {
        textDataInicial.setText(new StringBuilder().append(pad(mDayInicial)).append("/")
                        // Month is 0 based so add 1
                        .append(pad(mMonthInicial + 1)).append("/").append(mYearInicial).append(" ")
        );
    }

    private void updateDisplayFinal() {
        textDataFinal.setText(new StringBuilder().append(pad(mDayFinal)).append("/")
                        // Month is 0 based so add 1
                        .append(pad(mMonthFinal + 1)).append("/").append(mYearFinal).append(" ")
        );
    }


    /**
     * Método para atualizar os valores no campo data.
     *
     * @param c - valor do campo data
     * @author Henrique Frederico
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Click(R.id.limparPesquisaOsBtn)
    void limpar() {
        logradouroEditText.setText("");
        numOsEditText.setText("");
        // prioridadeSpinner.setSelection(0);
        municipioSpinner.setSelection(0);
        textDataInicial.setText("");
        textDataFinal.setText("");
        recuperarParametrosConsulta();

    }


    @Override
    public void onBackPressed() {
        if (ponto != null) {
            Intent intent = new Intent(this, CadastroPontoActivity_.class);
            intent.putExtra(Ponto.class.getName(), ponto);
            startActivity(intent);
        }
        finish();
    }

    @Click(R.id.novaOsBtn)
    void abrirTelaCadastroOs() {
        OrdemServicoUtil.getInstance().setNovaOS(true);
        Intent intent = new Intent(this, CadastroOsActivity_.class);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
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


    private void popularListaOs() {
        try {
            /*if (ponto == null) {
                ordemServicos = osService.buscarPorUsuarioAtribuicao("data_atribuicao", true);
            } else {
                ordemServicos = osService.buscarPorPontoEUsuarioAtribuicao(ponto.getId(), "data_atribuicao", true);
            }*/
            ordemServicos = osService.buscarPorParametroConsulta(parametroConsulta);

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
                if (ordemServicos.size() > 0) {
                    qtdeSinc.setText(String.valueOf(ordemServicos.size()));
                } else {
                    qtdeSinc.setText("Nenhum registro encontrado.");
                }

                excluirFinalizados();

                aplicarAdapter(0);
            } else {
                aplicarAdapter(1);
                qtdeSinc.setText("Nenhum registro encontrado.");
                excluirFinalizados();
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
            super(ListaOsActivity.this,
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
            TextView dataN = (TextView) v.findViewById(R.id.text6N);
            TextView enderecoN = (TextView) v.findViewById(R.id.text2N);
            TextView motivoN = (TextView) v.findViewById(R.id.text3N);
            TextView cidadeN = (TextView) v.findViewById(R.id.text5N);

            TextView numOs = (TextView) v.findViewById(R.id.text1);
            TextView data = (TextView) v.findViewById(R.id.text6);
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

            numOsN.setText("Número: ");
            dataN.setText("Data: ");
            enderecoN.setText("Endereço: ");
            motivoN.setText("Motivo: ");
            cidadeN.setText("Município: ");

            int tamanho = verificarTamanhoTela();
            numOs.setText(os.getNumOs().toString());
            data.setText(DateTimeUtils.converterDataParaStringFormatoPadrao(os.getDataCadastro()));
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
                if (os.getBairro() != null && os.getBairro().trim().length() > 0) {
                    cidade.setText(os.getBairro() + ", " + os.getMunicipio().getNome());
                } else {
                    cidade.setText(os.getMunicipio().getNome());
                }

            } else {
                cidade.setText("NÃO INFORMADO");
            }

            if (os.getSituacao() != null && os.getSituacao().getId().equals(Situacao.SITUACAO_EM_CAMPO.toString())) {
                final int codCor = Color.parseColor("#336600");
                numOs.setTextColor(codCor);
                data.setTextColor(codCor);
                endereco.setTextColor(codCor);
                motivo.setTextColor(codCor);
                cidade.setTextColor(codCor);
            }

            if (os.getMotivoEncerramento() != null && os.getMotivoEncerramento().getId() != null) {
                numOs.setTextColor(Color.GRAY);
                endereco.setTextColor(Color.GRAY);
                data.setTextColor(Color.GRAY);
                motivo.setTextColor(Color.GRAY);
                cidade.setTextColor(Color.GRAY);
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
            super(ListaOsActivity.this,
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


