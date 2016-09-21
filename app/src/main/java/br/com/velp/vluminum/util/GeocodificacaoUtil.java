package br.com.velp.vluminum.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class GeocodificacaoUtil {

    private Context context;
    private Geocoder geocoder;

    public GeocodificacaoUtil(Context context) {
        this.context = context;
        geocoder = new Geocoder(context);
    }

    public Address getEndereco(final double latitude, final double longitude) throws IOException {
        List<Address> enderecos = null;
        Address address = null;

        if (geocoder.isPresent()) {
            try {
                enderecos = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException ex) {
                Log.e(GeocodificacaoUtil.class.getName(), "Erro ao obter Endereco", ex);
            }

            if (enderecos != null && !enderecos.isEmpty()) {
                address = enderecos.get(0);
            }
        }

        return address;
    }

}
