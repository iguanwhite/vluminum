package br.com.velp.vluminum.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import br.com.velp.vluminum.R;
import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.FotoService;
import br.com.velp.vluminum.serviceimpl.FotoServiceImpl;
import br.com.velp.vluminum.util.AlertaUtil;
import br.com.velp.vluminum.util.GPSUtil;

@EActivity(R.layout.cadastro_foto_activity)
public class CadastroFotoActivity extends ActivityBase {

    private static final int CAPTURAR_IMAGEM_REQUEST_CODE = 1;

    private static final String EXTENSAO_FOTO = ".jpg";

    private static final String CAMINHO_DIRETORIO_FOTOS = "/sdcard/vluminum/fotos/";

    private static final String SALVAR = "Salvar";

    private static final String EDITAR = "Editar";

    private static final String ATUALIZAR = "Atualizar";

    @ViewById
    EditText legendaEditText;

    @ViewById
    ImageView fotoImageView;

    @ViewById
    Button capturarImagemBtn;

    @ViewById
    Button salvarFotoBtn;

    @ViewById
    Button excluirFotoBtn;

    private FotoService fotoService = new FotoServiceImpl(this);

    private Bitmap bitmap;

    private File diretorio;

    private Uri fileUri;

    private String nomeFoto;

    private Ponto ponto;

    private OrdemServico ordemServico;

    private Foto foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (fileUri == null) {
            diretorio = new File(CAMINHO_DIRETORIO_FOTOS);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            nomeFoto = UUID.randomUUID().toString() + EXTENSAO_FOTO;
            File arquivo = new File(diretorio.getAbsolutePath() + "/" + nomeFoto);
            fileUri = Uri.fromFile(arquivo);
        }

        recuperarParametros();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onBackPressed() {
        voltarTelaAnterior();
    }

    @AfterViews
    void inicializar() {
        if (foto == null) {
            excluirFotoBtn.setVisibility(View.INVISIBLE);
        } else {
            habilitarModoEdicao(true);
        }
    }

    private void recuperarParametros() {
        Intent intent = getIntent();
        ponto = (Ponto) intent.getSerializableExtra(Ponto.class.getName());
        ordemServico = (OrdemServico) intent.getSerializableExtra(OrdemServico.class.getName());
        foto = (Foto) intent.getSerializableExtra(Foto.class.getName());
    }

    @OnActivityResult(CAPTURAR_IMAGEM_REQUEST_CODE)
    void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            try {
                resizeFoto(fileUri.getPath(), 640, 480);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                Log.i("Foto", fileUri.getPath());
            } catch (IOException ex) {
                Log.e(CadastroPontoActivity.class.getName(), "Erro ao recuperar foto.", ex);
                ex.printStackTrace();
            }

            mostrarFoto(bitmap);
        } else {
            AlertaUtil.mostrarMensagem(this, "Foto não capturada!");
        }
    }

    @Click(R.id.capturarImagemBtn)
    void capturarImagem() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(camera, CAPTURAR_IMAGEM_REQUEST_CODE);
    }

    @Click(R.id.salvarFotoBtn)
    void salvar() {
        if (bitmap == null) {
            AlertaUtil.mensagemInformacao(this, "Foto não encontrada!");
            return;
        }

        if (salvarFotoBtn.getText().toString().equals(EDITAR)) {
            habilitarModoEdicao(false);
            return;
        }

        boolean result = false;
        Foto foto = obterDadosInformados();
        foto.setSincronizado(0);

        Location location = capturarCoordenadasGPS();
        if (location != null) {
            foto.setLatitude(location.getLatitude());
            foto.setLongitude(location.getLongitude());
        }

        if (ponto != null) {
            foto.setPonto(ponto);
        }

        if (ordemServico != null) {
            foto.setOrdemServico(ordemServico);
        }

        try {
            result = fotoService.salvarOuAtualizar(foto);
        } catch (CampoObrigatorioException ex) {
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
        } catch (Exception ex) {
            Log.e(CadastroFotoActivity.class.getName(), "Erro ao salvar Foto", ex);
            AlertaUtil.mensagemCamposObrigatorios(this, ex.getMessage());
            return;
        }

        if (result) {
            String msg = (salvarFotoBtn.getText().toString().equals(SALVAR)) ? "Foto cadastrada com sucesso!" : "Foto atualizada com sucesso!";
            AlertaUtil.mostrarMensagem(this, msg);
            voltarTelaAnterior();
        }
    }

    private Location capturarCoordenadasGPS() {
        Location location = null;
        GPSUtil gpsUtil = new GPSUtil(this);
        if (gpsUtil.isGPSAtivo()) {
            location = gpsUtil.capturarCoordenadasGPS();
        }

        return location;
    }

    @Click(R.id.excluirFotoBtn)
    void excluir() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage("Deseja realmente excluir a foto?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            boolean result = fotoService.deletar(foto);
                            if (result) {
                                AlertaUtil.mostrarMensagem(getApplicationContext(), "Foto excluída com sucesso!");
                                voltarTelaAnterior();
                            }
                        } catch (RegraNegocioException ex) {
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

    private void voltarTelaAnterior() {
        Intent intent = new Intent(this, ListaFotoActivity_.class);
        if (ponto != null) {
            intent.putExtra(Ponto.class.getName(), ponto);
        }
        if (ordemServico != null) {
            intent.putExtra(OrdemServico.class.getName(), ordemServico);
        }

        startActivity(intent);
        finish();
    }

    private void habilitarModoEdicao(boolean opcao) {
        if (opcao) {
            carregarDadosFoto();
            habilitarComponentes(false);
            salvarFotoBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
            salvarFotoBtn.setText(EDITAR);
        } else {
            habilitarComponentes(true);
            salvarFotoBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
            salvarFotoBtn.setText(ATUALIZAR);
        }
    }

    private Foto obterDadosInformados() {
        if (foto == null || foto.getId() == null || foto.getId().isEmpty()) {
            foto = new Foto();
            foto.setId(nomeFoto.substring(0, nomeFoto.lastIndexOf(".")));
        }

        String legenda = (legendaEditText.getText() != null) ? legendaEditText.getText().toString() : null;
        foto.setLegenda(legenda);

        return foto;
    }

    private void carregarDadosFoto() {
        if (foto.getLegenda() != null) {
            legendaEditText.setText(foto.getLegenda());
        }

        String nomeFotoSalva = foto.getId() + EXTENSAO_FOTO;
        File arquivo = new File(diretorio + "/" + nomeFotoSalva);
        fileUri = Uri.fromFile(arquivo);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
        } catch (IOException ex) {
            Log.e(CadastroPontoActivity.class.getName(), "Erro ao recuperar foto.", ex);
            ex.printStackTrace();
        }

        mostrarFoto(bitmap);
    }

    private void habilitarComponentes(boolean opcao) {
        legendaEditText.setEnabled(opcao);
        capturarImagemBtn.setEnabled(opcao);
    }

    private void mostrarFoto(Bitmap bitmap) {
        try {
            Display display = getWindowManager().getDefaultDisplay();
            int largura = (display.getWidth() * 60) / 100;
            int altura = (display.getHeight() * 55) / 100;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(largura, altura);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            fotoImageView.setLayoutParams(layoutParams);
            fotoImageView.setImageBitmap(bitmap);
            bitmap = null;
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void resizeFoto(String pathOfImage, int dstWidth, int dstHeight) {
        try {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(pathOfImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(pathOfImage);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            try {
                FileOutputStream out = new FileOutputStream(pathOfImage);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                Log.e("Image", e.getMessage(), e);
            }
        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
        }
    }
}
