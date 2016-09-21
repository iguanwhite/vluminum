package br.com.velp.vluminum.util;

import br.com.velp.vluminum.entity.Servico;

public class ServicoUtil {

    private static ServicoUtil instance;
    private Servico servico;


    public static ServicoUtil getInstance() {

        if (instance == null) {
            instance = new ServicoUtil();

        }

        return instance;
    }

    public static void setInstance(ServicoUtil instance) {
        ServicoUtil.instance = instance;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
