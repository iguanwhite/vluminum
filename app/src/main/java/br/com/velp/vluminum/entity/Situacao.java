package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_situacao")
public class Situacao extends EntityBase {

    public static final Integer SITUACAO_ATRIBUIDA = 8;
    public static final Integer SITUACAO_EM_CAMPO = 9;
    public static final Integer SITUACAO_FINALIZADA = 11;
    public static final Integer EM_ESTOQUE = 1;

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;
    @DatabaseField(canBeNull = false, width = 100)
    private String descricao;
    @DatabaseField(canBeNull = false)
    private Integer tipo;
    @DatabaseField(columnName = "flgExigeMotivo")
    private Boolean flgExigeMotivo;
    @DatabaseField(columnName = "flg_ativo", canBeNull = false)
    private Integer flgAtivo;
    @DatabaseField(columnName = "fluxo_status", width = 50)
    private String fluxoStatus;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Situacao() {

    }

    public Situacao(String id) {
        this.id = id;
    }


    //////////////////////////////////////////////
    // GETTERS E SETTERS
    //////////////////////////////////////////////


    public Boolean getFlgExigeMotivo() {
        return flgExigeMotivo;
    }

    public void setFlgExigeMotivo(Boolean flgExigeMotivo) {
        this.flgExigeMotivo = flgExigeMotivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getFlgAtivo() {
        return flgAtivo;
    }

    public void setFlgAtivo(Integer flgAtivo) {
        this.flgAtivo = flgAtivo;
    }

    public String getFluxoStatus() {
        return fluxoStatus;
    }

    public void setFluxoStatus(String fluxoStatus) {
        this.fluxoStatus = fluxoStatus;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Situacao)) return false;

        Situacao situacao = (Situacao) o;

        if (!id.equals(situacao.id)) return false;

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
