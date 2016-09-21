package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_material_identificado")
public class MaterialIdentificado extends EntityBase {

    @DatabaseField(columnName = "id_material_identificado", id = true, width = 36)
    private String idMaterialIdentificado;

    @DatabaseField(columnName = "id_material", foreign = true, foreignAutoRefresh = true)
    private Material material;

    @DatabaseField(columnName = "id_cliente")
    private Integer idCliente;

    @DatabaseField(columnName = "id_ponto", width = 36)
    private String idPonto;

    @DatabaseField(columnName = "id_os", width = 36)
    private String idOs;

    @DatabaseField(columnName = "num_serie", width = 20)
    private String numSerie;

    @DatabaseField(columnName = "custo")
    private Double custo;

    @DatabaseField(columnName = "data_retirada")
    private Date dataRetirada;

    @DatabaseField(columnName = "posicao_instalacao")
    private Integer posicaoInstalacao;

    @DatabaseField(columnName = "data_instalacao")
    private Date dataInstalacao;

    @DatabaseField(columnName = "situacao")
    private Integer situacao;

    @DatabaseField(columnName = "data_sincronizacao")
    private Date dataSincronizacao;

    @DatabaseField()
    private Integer sincronizado;

    @DatabaseField()
    private Integer classificacao;

    //Flag que indica se o Material Identificado foi criado na web ou no mobila
    @DatabaseField()
    private Boolean criadoWeb;

    public MaterialIdentificado() {

    }

    public MaterialIdentificado(String id) {
        this.idMaterialIdentificado = id;
    }

    public String getIdMaterialIdentificado() {
        return idMaterialIdentificado;
    }

    public void setIdMaterialIdentificado(String idMaterialIdentificado) {
        this.idMaterialIdentificado = idMaterialIdentificado;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(String idPonto) {
        this.idPonto = idPonto;
    }

    public String getIdOs() {
        return idOs;
    }

    public void setIdOs(String idOs) {
        this.idOs = idOs;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Integer getPosicaoInstalacao() {
        return posicaoInstalacao;
    }

    public void setPosicaoInstalacao(Integer posicaoInstalacao) {
        this.posicaoInstalacao = posicaoInstalacao;
    }

    public Date getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(Date dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Date getDataSincronizacao() {
        return dataSincronizacao;
    }

    public void setDataSincronizacao(Date dataSincronizacao) {
        this.dataSincronizacao = dataSincronizacao;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    @Override
    public String toString() {
        if (material.getDescricao() != null)
            return material.getDescricao();
        else
            return material.getIdMaterial().toString();
    }

    public Integer getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Integer classificacao) {
        this.classificacao = classificacao;
    }

    public Boolean getCriadoWeb() {
        return criadoWeb;
    }

    public void setCriadoWeb(Boolean criadoWeb) {
        this.criadoWeb = criadoWeb;
    }
}
