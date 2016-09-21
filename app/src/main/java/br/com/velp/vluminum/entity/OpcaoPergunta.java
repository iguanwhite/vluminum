package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_opcao_pergunta")
public class OpcaoPergunta extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Pergunta pergunta;

    @DatabaseField(canBeNull = false, width = 100)
    private String descricao;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public OpcaoPergunta() {

    }

    public OpcaoPergunta(Integer id, Pergunta pergunta, String descricao) {
        this.id = id;
        this.pergunta = pergunta;
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

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
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
        if (!(o instanceof OpcaoPergunta)) return false;

        OpcaoPergunta that = (OpcaoPergunta) o;

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
