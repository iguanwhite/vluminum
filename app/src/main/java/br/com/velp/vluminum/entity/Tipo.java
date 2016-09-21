package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;


@DatabaseTable(tableName = "tb_tipo")
public class Tipo extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true, columnName = "id_tipo")
    private Integer id;

    @DatabaseField()
    private String descricao;

    @DatabaseField(columnName = "id_grupo", foreign = true, foreignAutoRefresh = true)
    private Grupo grupo;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Tipo() {

    }

    public Tipo(Integer id) {
        this.id = id;
    }

    public Tipo(Integer id, String descricao, Grupo grupo) {
        this.id = id;
        this.descricao = descricao;
        this.grupo = grupo;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tipo)) return false;

        Tipo tipo = (Tipo) o;

        if (!id.equals(tipo.id)) return false;

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

