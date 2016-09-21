package br.com.velp.vluminum.util;

import br.com.velp.vluminum.entity.Foto;

public class FotoUtil {

    private static FotoUtil instance;
    private Foto foto;


    public static FotoUtil getInstance() {

        if (instance == null) {
            instance = new FotoUtil();

        }

        return instance;
    }

    public static void setInstance(FotoUtil instance) {
        FotoUtil.instance = instance;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }


}
