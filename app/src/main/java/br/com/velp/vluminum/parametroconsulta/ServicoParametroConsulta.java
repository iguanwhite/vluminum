package br.com.velp.vluminum.parametroconsulta;

import java.io.Serializable;

import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Ponto;

public class ServicoParametroConsulta implements Serializable {

    private Ponto ponto;
    private GrupoServico grupoServico;
    private Integer sincronizado;

    public ServicoParametroConsulta() {
    }

    public ServicoParametroConsulta(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public GrupoServico getGrupoServico() {
        return grupoServico;
    }


    public void setGrupoServico(GrupoServico grupoServico) {
        this.grupoServico = grupoServico;
    }

    public boolean foiInformadoAlgumParametroConsulta() {
        if ((grupoServico != null)) {
            return true;
        }

        return false;
    }
}
