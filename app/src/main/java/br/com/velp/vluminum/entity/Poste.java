package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import br.com.velp.vluminum.dao.EntityBase;
import br.com.velp.vluminum.enumerator.TipoDefeito;
import br.com.velp.vluminum.enumerator.TipoPoste;

@DatabaseTable(tableName = "tb_poste")
public class Poste extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false, width = 5)
    private String sigla;

    @DatabaseField(canBeNull = false, width = 150)
    private String descricao;

    @DatabaseField
    private BigDecimal altura;

    @DatabaseField(columnName = "tipo_poste", dataType = DataType.ENUM_INTEGER, canBeNull = false)
    private TipoPoste tipoPoste;

    @DatabaseField(columnName = "tipo_defeito", dataType = DataType.ENUM_INTEGER)
    private TipoDefeito tipoDefeito;

    @DatabaseField(width = 10)
    private String barramento;

    @DatabaseField(columnName = "inf_tecnica", width = 4000)
    private String informacoesTecnicas;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Poste() {

    }

    public Poste(String id) {
        this.id = id;
    }

    public Poste(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Poste(String id, String sigla, String descricao, BigDecimal altura, TipoPoste tipoPoste, TipoDefeito tipoDefeito, String barramento, String informacoesTecnicas) {
        this.id = id;
        this.sigla = sigla;
        this.descricao = descricao;
        this.altura = altura;
        this.tipoPoste = tipoPoste;
        this.tipoDefeito = tipoDefeito;
        this.barramento = barramento;
        this.informacoesTecnicas = informacoesTecnicas;
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public TipoPoste getTipoPoste() {
        return tipoPoste;
    }

    public void setTipoPoste(TipoPoste tipoPoste) {
        this.tipoPoste = tipoPoste;
    }

    public TipoDefeito getTipoDefeito() {
        return tipoDefeito;
    }

    public void setTipoDefeito(TipoDefeito tipoDefeito) {
        this.tipoDefeito = tipoDefeito;
    }

    public String getBarramento() {
        return barramento;
    }

    public void setBarramento(String barramento) {
        this.barramento = barramento;
    }

    public String getInformacoesTecnicas() {
        return informacoesTecnicas;
    }

    public void setInformacoesTecnicas(String informacoesTecnicas) {
        this.informacoesTecnicas = informacoesTecnicas;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poste)) return false;

        Poste poste = (Poste) o;

        if (!id.equals(poste.id)) return false;

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
