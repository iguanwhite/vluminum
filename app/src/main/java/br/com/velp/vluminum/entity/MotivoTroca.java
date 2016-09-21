package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_motivo_troca")
public class MotivoTroca extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, width = 100)
    private String descricao;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public MotivoTroca() {

    }

    public MotivoTroca(Integer id) {
        this.id = id;
    }

    public MotivoTroca(Integer id, String descricao) {
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
        if (!(o instanceof MotivoTroca)) return false;

        MotivoTroca that = (MotivoTroca) o;

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
