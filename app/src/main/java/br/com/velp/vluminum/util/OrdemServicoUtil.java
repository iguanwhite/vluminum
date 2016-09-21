package br.com.velp.vluminum.util;

import java.util.ArrayList;
import java.util.List;

import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;

public class OrdemServicoUtil {

    private static OrdemServicoUtil instance;

    private static List<OrdemServicoPonto> ordemServicoPontoList;

    private Ponto ponto;

    private boolean coletarPonto;

    private boolean coletarMaterial;

    private boolean novaOS;

    public static OrdemServicoUtil getInstance() {

        if (instance == null) {
            instance = new OrdemServicoUtil();
            ordemServicoPontoList = new ArrayList<OrdemServicoPonto>();
        }

        return instance;
    }

    public static void setInstance(OrdemServicoUtil instance) {
        OrdemServicoUtil.instance = instance;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public boolean isNovaOS() {
        return novaOS;
    }

    public void setNovaOS(boolean novaOS) {
        this.novaOS = novaOS;
    }

    public boolean isColetarPonto() {
        return coletarPonto;
    }

    public void setColetarPonto(boolean coletarPonto) {
        this.coletarPonto = coletarPonto;
    }

    public boolean isColetarMaterial() {
        return coletarMaterial;
    }

    public void setColetarMaterial(boolean coletarMaterial) {
        this.coletarMaterial = coletarMaterial;
    }

    public List<OrdemServicoPonto> getOrdemServicoPontoList() {
        return ordemServicoPontoList;
    }

    public void setOrdemServicoPontoList(List<OrdemServicoPonto> ordemServicoPontoList) {
        this.ordemServicoPontoList = ordemServicoPontoList;
    }


}
