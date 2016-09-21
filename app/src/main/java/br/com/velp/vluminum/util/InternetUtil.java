package br.com.velp.vluminum.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

import br.com.velp.vluminum.activity.LoginActivity;

public class InternetUtil {

    private Context context;

    public InternetUtil(Context context) {
        this.context = context;
    }

    public boolean isInternetAtiva() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void mostrarMensagemSolicitandoAtivacaoInternet(final String msg, final Activity activity, final boolean finalizarActivity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Vluminum");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (finalizarActivity) {
                            activity.finish();
                        }

                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        activity.startActivityForResult(intent, 1);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (!(activity instanceof LoginActivity)) {
                            activity.finish();
                        }

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
