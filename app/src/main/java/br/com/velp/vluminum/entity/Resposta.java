package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_resposta")
public class Resposta extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(foreign = true)
    private Pergunta pergunta;

    @DatabaseField(foreign = true)
    private OpcaoPergunta opcaoPergunta;

    @DatabaseField(canBeNull = false, foreign = true)
    private Ponto ponto;

    @DatabaseField(width = 200)
    private String resposta;

    @DatabaseField(width = 500)
    private String observacao;

    @DatabaseField()
    private Integer sincronizado;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Resposta() {

    }

    //////////////////////////////////////////////
    // GETTERS E SETTERS
    //////////////////////////////////////////////
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public OpcaoPergunta getOpcaoPergunta() {
        return opcaoPergunta;
    }

    public void setOpcaoPergunta(OpcaoPergunta opcaoPergunta) {
        this.opcaoPergunta = opcaoPergunta;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resposta)) return false;

        Resposta resposta = (Resposta) o;

        if (!id.equals(resposta.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Resposta{" +
                "id='" + id + '\'' +
                ", opcaoPergunta=" + opcaoPergunta +
                ", ponto=" + ponto +
                '}';
    }
}
