package br.com.velp.vluminum.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Pergunta;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.OpcaoPerguntaService;
import br.com.velp.vluminum.service.PerguntaService;
import br.com.velp.vluminum.service.RespostaService;
import br.com.velp.vluminum.serviceimpl.OpcaoPerguntaServiceImpl;
import br.com.velp.vluminum.serviceimpl.PerguntaServiceImpl;
import br.com.velp.vluminum.serviceimpl.RespostaServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;

@EActivity(R.layout.questionario_activity)
public class QuestionarioActivity extends ActivityBase {

    @ViewById
    LinearLayout containerPrincipal;

    private PerguntaService perguntaService = new PerguntaServiceImpl(this);

    private OpcaoPerguntaService opcaoPerguntaService = new OpcaoPerguntaServiceImpl(this);

    private RespostaService respostaService = new RespostaServiceImpl(this);

    private List<Pergunta> perguntas;

    private Ponto ponto;

    @AfterViews
    void inicializar() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());

        criarQuestionario();

        Button button = new Button(this);
        button.setText("Salvar");
        containerPrincipal.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

    }

    void salvar() {
        respostaService.apagarRespostaPonto(ponto.getId());

        for (int i = 0; i < containerPrincipal.getChildCount(); i++) {
            if (containerPrincipal.getChildAt(i) instanceof EditText) {
                EditText campo = (EditText) containerPrincipal.getChildAt(i);

                Resposta resposta = new Resposta();
                resposta.setId(UUID.randomUUID().toString());
                resposta.setPonto(ponto);
                resposta.setPergunta(new Pergunta(campo.getId()));
                resposta.setResposta(campo.getText().toString());
                resposta.setSincronizado(0);

                try {
                    respostaService.salvarOuAtualizar(resposta);
                } catch (RegraNegocioException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            if (containerPrincipal.getChildAt(i) instanceof Spinner) {
                Spinner campo = (Spinner) containerPrincipal.getChildAt(i);
                OpcaoPergunta opcao = (OpcaoPergunta) campo.getSelectedItem();

                Resposta resposta = new Resposta();
                resposta.setId(UUID.randomUUID().toString());
                resposta.setPonto(ponto);
                resposta.setPergunta(opcao.getPergunta());
                resposta.setOpcaoPergunta(opcao);
                resposta.setSincronizado(0);

                try {
                    respostaService.salvarOuAtualizar(resposta);
                } catch (RegraNegocioException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            if (containerPrincipal.getChildAt(i) instanceof CheckBox) {
                CheckBox campo = (CheckBox) containerPrincipal.getChildAt(i);

                if (!campo.isChecked()) {
                    continue;
                }

                Resposta resposta = new Resposta();
                resposta.setId(UUID.randomUUID().toString());
                resposta.setPonto(ponto);

                OpcaoPergunta opcao = opcaoPerguntaService.buscarPorId(campo.getId());
                resposta.setPergunta(opcao.getPergunta());
                resposta.setOpcaoPergunta(opcao);
                resposta.setSincronizado(0);

                try {
                    respostaService.salvarOuAtualizar(resposta);
                } catch (RegraNegocioException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
        }

        AlertaUtil.mostrarMensagem(this, "Respostas cadastradas com sucesso!");
        finish();

    }

    private void criarQuestionario() {
        perguntas = perguntaService.listarAtivas();

        if (perguntas == null || perguntas.isEmpty()) {
            AlertaUtil.mostrarMensagem(this, "Nenhuma pergunta encontrada!");
            finish();
        }

        for (Pergunta pergunta : perguntas) {
            criarPergunta(pergunta);
            criarCampoResposta(pergunta);
        }
    }

    private void criarPergunta(Pergunta pergunta) {
        TextView textView = new TextView(this);
        textView.setText(pergunta.getOrdem() + ". " + pergunta.getDescricao());
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        containerPrincipal.addView(textView);
    }

    private void criarCampoResposta(Pergunta pergunta) {
        final Integer perguntaId = pergunta.getId();
        final Integer tipoPergunta = pergunta.getTipoPergunta();
        final List<OpcaoPergunta> opcoesPergunta = opcaoPerguntaService.buscarPorPergunta(pergunta);

        switch (tipoPergunta) {
            case 1:
                EditText editText = new EditText(this);
                editText.setId(perguntaId);

                //Verifca se possui resposta
                String texto = respostaService.buscaRespostaText(perguntaId, ponto.getId());
                if (!texto.equals("")) {
                    editText.setText(texto);
                }
                containerPrincipal.addView(editText);
                break;

            case 2:
                Spinner spinner = new Spinner(this);
                spinner.setAdapter(new ArrayAdapter<OpcaoPergunta>(this, android.R.layout.simple_spinner_item, opcoesPergunta));

                OpcaoPergunta opcao = respostaService.buscaRespostaSpinner(perguntaId, ponto.getId());
                if (opcao != null) {
                    ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                    int spinnerPosition = myAdap.getPosition(opcao);
                    spinner.setSelection(spinnerPosition);
                }
                containerPrincipal.addView(spinner);
                break;

            case 3:
                for (OpcaoPergunta opcaoPergunta : opcoesPergunta) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setId(opcaoPergunta.getId());
                    checkBox.setText(opcaoPergunta.getDescricao());

                    //Verifca se possui resposta
                    if (respostaService.buscaRespostaMultiplaEscolha(perguntaId, ponto.getId(), opcaoPergunta.getId())) {
                        checkBox.setChecked(true);
                    }


                    containerPrincipal.addView(checkBox);
                }
                break;

        }
    }

}


