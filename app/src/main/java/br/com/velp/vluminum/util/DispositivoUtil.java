package br.com.velp.vluminum.util;

import android.content.Context;
import android.content.res.Configuration;

public class DispositivoUtil {

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
