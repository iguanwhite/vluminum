package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_ponto_servico")
public class PontoServico extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true) // not null
    private Servico servico;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    // not null
    private Ponto ponto;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public PontoServico() {

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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PontoServico)) return false;

        PontoServico poste = (PontoServico) o;

        if (!id.equals(poste.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "";
    }
}
