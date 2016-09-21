package br.com.velp.vluminum.parametroconsulta;

import java.io.Serializable;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;

public class PontoResultadoConsulta implements Serializable, Cloneable {

    private String idPonto;
    private String numPlaquetaTransformador;
    private String logradouro;
    private String numLogradouro;
    private String bairro;
    private String ordensServicoDoPonto;


    public PontoResultadoConsulta() {
    }

    public String getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(String idPonto) {
        this.idPonto = idPonto;
    }

    public String getNumPlaquetaTransformador() {
        return numPlaquetaTransformador;
    }

    public void setNumPlaquetaTransformador(String numPlaquetaTransformador) {
        this.numPlaquetaTransformador = numPlaquetaTransformador;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumLogradouro() {
        return numLogradouro;
    }

    public void setNumLogradouro(String numLogradouro) {
        this.numLogradouro = numLogradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getOrdensServicoDoPonto() {
        return ordensServicoDoPonto;
    }

    public void setOrdensServicoDoPonto(String ordensServicoDoPonto) {
        this.ordensServicoDoPonto = ordensServicoDoPonto;
    }

    @Override
    public PontoResultadoConsulta clone() throws CloneNotSupportedException {
        return (PontoResultadoConsulta) super.clone();
    }
}
