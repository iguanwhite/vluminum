package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_alimentador")
public class Alimentador extends EntityBase {

    @DatabaseField(id = true)
    private Integer id;
    @DatabaseField(columnName = "descricao", width = 100, canBeNull = false)
    private String descricao;

    public Alimentador() {

    }

    public Alimentador(int id) {
        this.id = id;
    }

    public Alimentador(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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

    public String toString() {
        return descricao;
    }
}
