package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_pergunta")
public class Pergunta extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, width = 500, unique = true)
    private String descricao;

    @DatabaseField(columnName = "tipo_pergunta")
    private Integer tipoPergunta;

    @DatabaseField(canBeNull = false)
    private Integer ordem;

    @DatabaseField(canBeNull = false)
    private Boolean ativa;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Pergunta() {
    }

    public Pergunta(Integer id) {
        this.id = id;
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

    public Integer getTipoPergunta() {
        return tipoPergunta;
    }

    public void setTipoPergunta(Integer tipoPergunta) {
        this.tipoPergunta = tipoPergunta;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pergunta)) return false;

        Pergunta pergunta = (Pergunta) o;

        if (id != null ? !id.equals(pergunta.id) : pergunta.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
