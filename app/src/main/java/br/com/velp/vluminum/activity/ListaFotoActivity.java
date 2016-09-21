package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.service.FotoService;
import br.com.velp.vluminum.serviceimpl.FotoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.FotoUtil;

@EActivity(R.layout.lista_foto_activity)
public class ListaFotoActivity extends ListActivityBase {

    private static final String EXTENSAO_FOTO = ".jpg";

    private static final String CAMINHO_DIRETORIO_FOTOS = "/sdcard/vluminum/fotos/";

    @ViewById
    ListView list;

    private FotoService fotoService = new FotoServiceImpl(this);

    private Ponto ponto;

    private OrdemServico ordemServico;

    private List fotos;

    private Bitmap bitmap;

    private File diretorio;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recuperarParametros();
    }

    @Override
    protected void onResume() {
        super.onResume();

        popularListaFotos();
    }

    @Click(R.id.novaFotoBtn)
    void novaFoto() {
        Intent intent = new Intent(this, CadastroFotoActivity_.class);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
        } else if (ordemServico != null) {
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
        }
        startActivity(intent);
        finish();
    }

    @ItemClick(android.R.id.list)
    void editarFoto(Foto foto) {
        Intent intent = new Intent(this, CadastroFotoActivity_.class);
        intent.putExtra(Foto.class.getName(), foto);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
        } else if (ordemServico != null) {
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
        }
        startActivity(intent);
        finish();
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
    }

    private void popularListaFotos() {
        if (ponto != null) {
            fotos = fotoService.buscarPorPonto(ponto);
        } else if (ordemServico != null) {
            fotos = fotoService.buscarPorOrdemServico(ordemServico);
        }

        if (fotos != null && !fotos.isEmpty()) {
            aplicarAdapter(0);
        } else {
            aplicarAdapter(1);
        }
    }

    public void aplicarAdapter(int v) {
        if (v == 0) {
            FotoAdapter adapter = new FotoAdapter();
            setListAdapter(adapter);
        } else {
            FotoAdapter3 adapter3 = new FotoAdapter3();
            setListAdapter(adapter3);
        }
    }

    private void preencherBitmap() {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
        } catch (IOException ex) {
            Log.e(CadastroPontoActivity.class.getName(), "Erro ao recuperar foto.", ex);
            ex.printStackTrace();
        }
    }

    private void removerFoto() {
        final Foto foto = FotoUtil.getInstance().getFoto();
        FotoUtil.getInstance().setFoto(null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente desvincular esta foto do ponto?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            boolean result = fotoService.deletar(foto);
                            if (result) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Foto desvinculada com sucesso!");
                                popularListaFotos();
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
    class FotoAdapter extends ArrayAdapter<String> {

        int place = 0;
        ListView vg = null;

        FotoAdapter() {
            super(ListaFotoActivity.this,
                    R.layout.rowlayout_lista_foto, R.id.text1,
                    fotos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            place = position;
            vg = (ListView) parent;
            View v = convertView;
            v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.rowlayout_lista_foto, null);


            if (position % 2 == 1) {
                v.setBackgroundColor(Color.parseColor("#F8F8F8"));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }

            ImageView imagem = (ImageView) v.findViewById(R.id.imagem);
            //   TextView descricao = (TextView) v.findViewById(R.id.text1);


            // Pegar a posicao da mensagem na lista e atualizar.
            final Foto foto = (Foto) fotos.get(position);

            /*
            if (foto.getLegenda() != null) {
                descricao.setText(foto.getLegenda());
            }

            */

            if (fileUri == null) {
                diretorio = new File(CAMINHO_DIRETORIO_FOTOS);
                if (!diretorio.exists()) {
                    diretorio.mkdirs();
                }
            }

            String nomeFotoSalva = foto.getId() + EXTENSAO_FOTO;
            File arquivo = new File(diretorio.getAbsolutePath() + "/" + nomeFotoSalva);
            fileUri = Uri.fromFile(arquivo);

            preencherBitmap();

            imagem.setImageBitmap(bitmap);
            bitmap = null;

            ImageView pr = (ImageView) v.findViewById(R.id.btnRemoverFoto);
            pr.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    FotoUtil.getInstance().setFoto(foto);
                    removerFoto();
                }
            });

            return v;
        }
    }

    class FotoAdapter3 extends ArrayAdapter<String> {
        FotoAdapter3() {
            super(ListaFotoActivity.this,
                    R.layout.rowlayout_lista_foto, R.id.text1,
                    fotos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            // If convertView is null create a new view, else use convert view
            if (v == null) {
                v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.rowlayout_lista_foto, null);

            } else {
                v = super.getView(position, convertView, parent);
            }

            TextView titulo = (TextView) v.findViewById(R.id.text1);

            titulo.setText("     Nenhum Ponto Encontrada.");

            return v;
        }
    }

}


