package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_os_servico")
public class OrdemServicoServico extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true, columnName = "id_os_servico")
    private String id;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)

    private Servico servico;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Ponto ponto;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private OrdemServico os;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private MaterialIdentificado materialIdentificado;

    @DatabaseField()
    private Integer sincronizado;


    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public OrdemServicoServico() {

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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Ponto getPonto() {
        return ponto;
    }

    public void setPonto(Ponto ponto) {
        this.ponto = ponto;
    }

    public OrdemServico getOs() {
        return os;
    }

    public void setOs(OrdemServico os) {
        this.os = os;
    }

    public MaterialIdentificado getMaterialIdentificado() {
        return materialIdentificado;
    }

    public void setMaterialIdentificado(MaterialIdentificado materialIdentificado) {
        this.materialIdentificado = materialIdentificado;
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
        if (!(o instanceof OrdemServicoServico)) return false;

        OrdemServicoServico grupo = (OrdemServicoServico) o;

        if (!id.equals(grupo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}

