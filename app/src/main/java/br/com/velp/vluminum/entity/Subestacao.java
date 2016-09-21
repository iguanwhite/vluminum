package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_subestacao")
public class Subestacao extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, unique = true, width = 150)
    private String descricao;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Subestacao() {

    }

    public Subestacao(Integer id) {
        this.id = id;
    }

    public Subestacao(Integer id, String descricao) {
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
        if (!(o instanceof Subestacao)) return false;

        Subestacao that = (Subestacao) o;

        if (!id.equals(that.id)) return false;

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
