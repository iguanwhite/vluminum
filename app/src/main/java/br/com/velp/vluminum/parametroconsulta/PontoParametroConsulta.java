package br.com.velp.vluminum.parametroconsulta;

import java.io.Serializable;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;

public class PontoParametroConsulta implements Serializable {

    private OrdemServico ordemServico;
    private String logradouro;
    private String pontoDeReferencia;
    private String numPlaquetaTransformador;
    private Integer sincronizado;
    private Municipio municipio;


    public PontoParametroConsulta() {
    }

    public PontoParametroConsulta(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public PontoParametroConsulta(OrdemServico ordemServico, String logradouro, String pontoDeReferencia, String numPlaquetaTransformador, Integer sincronizado, Municipio municipio) {
        this.ordemServico = ordemServico;
        this.logradouro = logradouro;
        this.pontoDeReferencia = pontoDeReferencia;
        this.numPlaquetaTransformador = numPlaquetaTransformador;
        this.sincronizado = sincronizado;
        this.municipio = municipio;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getPontoDeReferencia() {
        return pontoDeReferencia;
    }

    public void setPontoDeReferencia(String pontoDeReferencia) {
        this.pontoDeReferencia = pontoDeReferencia;
    }

    public String getNumPlaquetaTransformador() {
        return numPlaquetaTransformador;
    }

    public void setNumPlaquetaTransformador(String numPlaquetaTransformador) {
        this.numPlaquetaTransformador = numPlaquetaTransformador;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

}
