package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_sincronismo")
public class Sincronismo extends EntityBase {

    @DatabaseField(columnName = "id_sincronismo", id = true)
    private Integer id;

    @DatabaseField(columnName = "tabela", unique = true)
    private String tabela;

    @DatabaseField(columnName = "data_sincronismo", canBeNull = false)
    private Date dataSincronismo;


    @DatabaseField()
    private Integer sincronizado;

    public Sincronismo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public Date getDataSincronismo() {
        return dataSincronismo;
    }

    public void setDataSincronismo(Date dataSincronismo) {
        this.dataSincronismo = dataSincronismo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sincronismo)) return false;

        Sincronismo that = (Sincronismo) o;

        if (!tabela.equals(that.tabela)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tabela.hashCode();
    }

    @Override
    public String toString() {
        return tabela;
    }
}
