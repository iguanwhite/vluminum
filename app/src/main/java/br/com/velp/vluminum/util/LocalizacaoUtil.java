package br.com.velp.vluminum.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import br.com.velp.vluminum.R;

public class LocalizacaoUtil implements LocationListener {

    private static final int UM_SEGUNDO = 1000;
    private static final int TEMPO_TOTAL_BUSCA = 10;
    protected ProgressDialog progressDialog;
    private Context context;
    private LocationManager locationManager;
    private Location location;
    private volatile boolean stop = false;

    public LocalizacaoUtil(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    public boolean isGPSAtivo() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkAtivo() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public Location capturarCoordenadasGPS() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    Looper.myLooper();
                    Looper.prepare();

                    progressDialog = ProgressDialog.show(context, null, context.getString(R.string.obtendoCoordenadas), true);
                    if (isGPSAtivo()) {
                        ativarGPS();
                    }

                    if (location == null && isNetworkAtivo()) {
                        ativarNetwork();
                    }

                    Looper.loop();
                }
            }).start();

            int tempoBusca = 0;
            while (!stop) {
                if (TEMPO_TOTAL_BUSCA == tempoBusca) {
                    break;
                }

                Thread.sleep(UM_SEGUNDO);
                tempoBusca++;
            }
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            desativarGPS();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
        return null;
    }

    public void ativarGPS() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.myLooper());
    }

    private void ativarNetwork() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.myLooper());
    }

    private void desativarGPS() {
        locationManager.removeUpdates(LocalizacaoUtil.this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        stop = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Provider desabilitado
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Provider habilitado
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Status do provider alterado
    }

}
