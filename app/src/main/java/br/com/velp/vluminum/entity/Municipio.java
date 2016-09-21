package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_municipio")
public class Municipio extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, width = 80)
    private String nome;

    @DatabaseField(canBeNull = false, width = 2)
    private String uf;

    @DatabaseField()
    private Integer idCliente;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Municipio() {

    }

    public Municipio(Integer id) {
        this.id = id;
    }

    public Municipio(Integer id, String nome, String uf) {
        this.id = id;
        this.nome = nome;
        this.uf = uf;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Municipio)) return false;

        Municipio municipio = (Municipio) o;

        if (!id.equals(municipio.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return nome;
    }
}
