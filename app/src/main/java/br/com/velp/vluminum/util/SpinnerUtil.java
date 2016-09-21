package br.com.velp.vluminum.util;

import android.widget.Spinner;

public class SpinnerUtil {

    // Retorna o índice do elemento na lista. Útil para selecionar este elemento no spinner.
    public static int getIndice(Spinner spinner, String descricaoItem) {
        int indice = 0;
        if (descricaoItem != null && !descricaoItem.isEmpty()) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(descricaoItem.trim())) {
                    indice = i;
                    break;
                }
            }
        }
        return indice;
    }

}
