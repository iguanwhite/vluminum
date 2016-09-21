package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_ordem_ponto")
public class OrdemServicoPonto extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private OrdemServico os;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Ponto ponto;

    @DatabaseField
    private Integer situacao;

    @DatabaseField
    private Integer sincronizado;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public OrdemServicoPonto() {

    }

    public OrdemServicoPonto(OrdemServico os, Ponto ponto, Integer situacao) {
        this.os = os;
        this.ponto = ponto;
        this.situacao = situacao;
    }

    //////////////////////////////////////////////
    // GETTERS E SETTERS
    //////////////////////////////////////////////
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrdemServico getOs() {
        return os;
    }

    public void setOs(OrdemServico os) {
        this.os = os;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdemServicoPonto)) return false;

        OrdemServicoPonto that = (OrdemServicoPonto) o;

        if (!os.equals(that.os)) return false;
        if (!ponto.equals(that.ponto)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = os.hashCode();
        result = 31 * result + ponto.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "";
    }
}
