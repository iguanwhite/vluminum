package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_usuario")
public class Usuario extends EntityBase {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, width = 45)
    private String nome;

    @DatabaseField(canBeNull = false, unique = true, width = 15)
    private String login;

    @DatabaseField(canBeNull = false, width = 10)
    private String senha;

    public Usuario() {

    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String nome, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (!id.equals(usuario.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return login;
    }
}
