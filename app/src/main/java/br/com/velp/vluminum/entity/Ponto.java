package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.velp.vluminum.dao.EntityBase;
import br.com.velp.vluminum.enumerator.Danificado;
import br.com.velp.vluminum.enumerator.Direcao;
import br.com.velp.vluminum.enumerator.TipoPonto;

@DatabaseTable(tableName = "tb_ponto")
public class Ponto extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Direcao direcao;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Danificado danificado;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private TipoPonto tipoPonto;

    @DatabaseField(columnName = "num_plaqueta_transf", width = 15)
    private String numPlaquetaTransf;

    @DatabaseField(canBeNull = false, width = 150)
    private String logradouro;

    @DatabaseField(columnName = "num_logradouro", width = 10)
    private String numLogradouro;

    @DatabaseField(width = 50)
    private String bairro;

    @DatabaseField(width = 10)
    private String cep;

    @DatabaseField(columnName = "ponto_referencia", width = 50)
    private String pontoReferencia;

    @DatabaseField(width = 18)
    private Double latitude;

    @DatabaseField(width = 18)
    private Double longitude;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Poste poste;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Municipio municipio;

    @DatabaseField()
    private Integer numTrafo;

    @DatabaseField()
    private Integer sincronizado;

    @DatabaseField()
    private Integer idCliente;

    @DatabaseField(columnName = "data_sincronismo")
    private Date dataSincronismo;

    private transient String ordensServicoDoPonto;

    private transient Integer situacaoDoPontoNaOs;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public Ponto() {

    }

    public Ponto(String id) {
        this.id = id;
    }

    //////////////////////////////////////////////
    // GETTERS E SETTERS
    //////////////////////////////////////////////

    public Integer getNumTrafo() {
        return numTrafo;
    }

    public void setNumTrafo(Integer numTrafo) {
        this.numTrafo = numTrafo;
    }

    public void setNumTrafo(String numTrafo) {
        try {
            this.numTrafo = Integer.parseInt(numTrafo);
        } catch (Exception e) {
            this.numTrafo = 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Direcao getDirecao() {
        return direcao;
    }

    public void setDirecao(Direcao direcao) {
        this.direcao = direcao;
    }

    public TipoPonto getTipoPonto() {
        return tipoPonto;
    }

    public void setTipoPonto(TipoPonto tipoPonto) {
        this.tipoPonto = tipoPonto;
    }

    public Danificado getDanificado() {
        return danificado;
    }

    public void setDanificado(Danificado danificado) {
        this.danificado = danificado;
    }

    public String getNumPlaquetaTransf() {
        return numPlaquetaTransf;
    }

    public void setNumPlaquetaTransf(String numPlaquetaTransf) {
        if (numPlaquetaTransf != null) {
            numPlaquetaTransf = numPlaquetaTransf.trim();
        }
        this.numPlaquetaTransf = numPlaquetaTransf;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumLogradouro() {
        return numLogradouro;
    }

    public void setNumLogradouro(String numLogradouro) {
        this.numLogradouro = numLogradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public Double getLatitude() {
        if (latitude == null) {
            latitude = 0D;
        }
        return latitude;
    }

    public void setLatitude(Double latitude) {
        if (latitude == null) {
            latitude = 0D;
        }
        this.latitude = latitude;
    }

    public Double getLongitude() {
        if (longitude == null) {
            longitude = 0D;
        }
        return longitude;
    }

    public void setLongitude(Double longitude) {
        if (longitude == null) {
            longitude = 0D;
        }
        this.longitude = longitude;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getEnderecoFormatado() {
        StringBuilder endereco = new StringBuilder();
        endereco = endereco.append(logradouro).append(", Nº ").append(numLogradouro).append(", B. ").append(bairro).append(", CEP. ").append(cep);
        return endereco.toString();
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getOrdensServicoDoPonto() {
        return ordensServicoDoPonto;
    }

    public void setOrdensServicoDoPonto(String ordensServicoDoPonto) {
        this.ordensServicoDoPonto = ordensServicoDoPonto;
    }

    public Date getDataSincronismo() {
        return dataSincronismo;
    }

    public void setDataSincronismo(Date dataSincronismo) {
        this.dataSincronismo = dataSincronismo;
    }

    public Integer getSituacaoDoPontoNaOs() {
        return situacaoDoPontoNaOs;
    }

    public void setSituacaoDoPontoNaOs(Integer situacaoDoPontoNaOs) {
        this.situacaoDoPontoNaOs = situacaoDoPontoNaOs;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ponto)) return false;

        Ponto ponto = (Ponto) o;

        if (!id.equals(ponto.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return numPlaquetaTransf;
    }
}
