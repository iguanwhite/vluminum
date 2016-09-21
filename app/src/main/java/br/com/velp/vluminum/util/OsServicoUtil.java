package br.com.velp.vluminum.util;

import br.com.velp.vluminum.entity.OrdemServicoServico;

public class OsServicoUtil {

    private static OsServicoUtil instance;
    private OrdemServicoServico osServico;


    public static OsServicoUtil getInstance() {

        if (instance == null) {
            instance = new OsServicoUtil();

        }

        return instance;
    }

    public static void setInstance(OsServicoUtil instance) {
        OsServicoUtil.instance = instance;
    }

    public OrdemServicoServico getOrdemServicoServico() {
        return osServico;
    }

    public void setOrdemServicoServico(OrdemServicoServico osServico) {
        this.osServico = osServico;
    }
}
