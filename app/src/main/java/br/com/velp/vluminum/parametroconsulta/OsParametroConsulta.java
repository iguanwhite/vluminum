package br.com.velp.vluminum.parametroconsulta;

import java.io.Serializable;
import java.util.Date;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.enumerator.Prioridade;

public class OsParametroConsulta implements Serializable {

    private Municipio municipio;
    private String logradouro;
    private String numeroOs;
    private Prioridade prioridade;
    private Ponto ponto;
    private Integer sincronizado;
    private Date dataInicial, dataFinal;

    private Integer situacao;

    public OsParametroConsulta() {
    }

    public OsParametroConsulta(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public OsParametroConsulta(Municipio municipio, String logradouro, String numeroOs, Prioridade prioridade, Ponto ponto, int sincronizado) {
        this.municipio = municipio;
        this.logradouro = logradouro;
        this.numeroOs = numeroOs;
        this.prioridade = prioridade;
        this.ponto = ponto;
        this.sincronizado = sincronizado;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumeroOs() {
        return numeroOs;
    }

    public void setNumeroOs(String numeroOs) {
        this.numeroOs = numeroOs;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
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
}
