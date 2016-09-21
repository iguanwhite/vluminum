package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_servico")

public class Servico extends EntityBase {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, width = 100)
    private String descricao;

    @DatabaseField()
    private Integer sincronizado;

    @DatabaseField(columnName = "grupo_id", foreign = true, canBeNull = false, foreignAutoCreate = true, foreignAutoRefresh = true)
    private GrupoServico grupo;

    public Servico() {

    }

    public Servico(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public GrupoServico getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoServico grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return descricao;
    }
}

