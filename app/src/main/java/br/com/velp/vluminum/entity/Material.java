package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_material")
public class Material extends EntityBase {

    @DatabaseField(columnName = "id_material", id = true)
    private Integer idMaterial;

    @DatabaseField(columnName = "id_cliente")
    private Integer idCliente;

    @DatabaseField(columnName = "id_tipo", foreign = true, foreignAutoRefresh = true)
    private Tipo tipo;

    @DatabaseField(columnName = "informacao_tec", width = 4000)
    private String informacaoTec;

    @DatabaseField(columnName = "potencia")
    private Integer potencia;

    @DatabaseField(columnName = "descricao", width = 100)
    private String descricao;

    @DatabaseField(columnName = "marca", width = 100)
    private String marca;

    @DatabaseField(columnName = "custo")
    private Double custo;

    @DatabaseField(columnName = "id_servico_instalacao")
    private Long idServicoInstalacao;

    @DatabaseField(columnName = "id_servico_remocao")
    private Long idServicoRemocao;

    @DatabaseField(columnName = "num_serie")
    private String numSerie;

    @DatabaseField()
    private Integer sincronizado;

    public Material() {

    }

    public Material(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Integer getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setIdTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getInformacaoTec() {
        return informacaoTec;
    }

    public void setInformacaoTec(String informacaoTec) {
        this.informacaoTec = informacaoTec;
    }

    public Integer getPotencia() {
        return potencia;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public Long getIdServicoInstalacao() {
        return idServicoInstalacao;
    }

    public void setIdServicoInstalacao(Long idServicoInstalacao) {
        this.idServicoInstalacao = idServicoInstalacao;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Long getIdServicoRemocao() {
        return idServicoRemocao;
    }

    public void setIdServicoRemocao(Long idServicoRemocao) {
        this.idServicoRemocao = idServicoRemocao;
    }

    @Override
    public String toString() {

        if (descricao != null)
            return descricao;
        else
            return idMaterial.toString();
    }

}
