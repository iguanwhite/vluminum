package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_grupo_servico")
public class GrupoServico extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true, columnName = "id_grupo_servico")
    private Integer id;

    @DatabaseField()
    private String descricao;


    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public GrupoServico() {

    }

    public GrupoServico(Integer id) {
        this.id = id;
    }

    public GrupoServico(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    //////////////////////////////////////////////
    // GETTERS E SETTERS
    //////////////////////////////////////////////


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


    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrupoServico)) return false;

        GrupoServico grupo = (GrupoServico) o;

        if (!id.equals(grupo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return descricao;
    }
}

