package br.com.velp.vluminum.util;

import br.com.velp.vluminum.entity.MaterialIdentificado;

public class MaterialUtil {

    private static MaterialUtil instance;
    private MaterialIdentificado materialIdentificado;


    public static MaterialUtil getInstance() {

        if (instance == null) {
            instance = new MaterialUtil();

        }

        return instance;
    }

    public static void setInstance(MaterialUtil instance) {
        MaterialUtil.instance = instance;
    }

    public MaterialIdentificado getMaterialIdentificado() {
        return materialIdentificado;
    }

    public void setMaterialIdentificado(MaterialIdentificado materialIdentificado) {
        this.materialIdentificado = materialIdentificado;
    }
}
