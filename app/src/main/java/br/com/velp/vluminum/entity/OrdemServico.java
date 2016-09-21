package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.velp.vluminum.dao.EntityBase;
import br.com.velp.vluminum.enumerator.MotivoAbertura;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.enumerator.TipoVeiculo;

@DatabaseTable(tableName = "tb_ordem_servico")
public class OrdemServico extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(columnName = "id_situacao", foreign = true, foreignAutoRefresh = true)
    private Situacao situacao;

    @DatabaseField(columnName = "id_municipio", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Municipio municipio;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private MotivoAbertura motivoAbertura;

    @DatabaseField(width = 1000)
    private String obs;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private TipoVeiculo tipoCesto;

    @DatabaseField(columnName = "prazo_atendimento")
    private Integer prazoAtendimento;

    @DatabaseField(canBeNull = false, width = 150)
    private String logradouro;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Prioridade prioridade;

    @DatabaseField(columnName = "obs_campo", width = 100)
    private String obsCampo;

    @DatabaseField(columnName = "num_logradouro", width = 10)
    private String numLogradouro;

    @DatabaseField(width = 50)
    private String bairro;

    @DatabaseField(width = 10)
    private String cep;

    @DatabaseField(columnName = "data_retorno")
    private Date dataRetorno;

    @DatabaseField(columnName = "data_atribuicao")
    private Date dataAtribuicao;

    @DatabaseField(columnName = "data_sincronismo")
    private Date dataSincronismo;

    @DatabaseField(columnName = "ponto_referencia", width = 50)
    private String pontoReferencia;

    @DatabaseField(columnName = "data_despacho")
    private Date dataDespacho;

    @DatabaseField(columnName = "num_os", canBeNull = false)
    private Integer numOs;

    @DatabaseField(columnName = "usuario_atribuicao")
    private Integer usuarioAtribuicao;

    @DatabaseField(columnName = "usuario_execucao")
    private Integer usuarioExecucao;

    @DatabaseField
    private Integer sincronizado;

    @DatabaseField(columnName = "id_motivo_troca", foreign = true)
    private MotivoTroca motivoEncerramento;

    @DatabaseField(columnName = "id_chamado")
    private Integer idChamado;

    @DatabaseField(columnName = "tel_chamado")
    private String telChamado;


    @DatabaseField(columnName = "nome_chamado")
    private String nomeChamado;


    @DatabaseField(columnName = "protocolo_chamado")
    private String protocoloChamado;


    @DatabaseField(columnName = "qtd_chamado")
    private String qtdChamado;


    @DatabaseField()
    private Integer idCliente;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public OrdemServico() {
    }

    public OrdemServico(String id) {
        this.id = id;
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

    public TipoVeiculo getTipoCesto() {
        return tipoCesto;
    }

    public void setTipoCesto(TipoVeiculo tipoCesto) {
        this.tipoCesto = tipoCesto;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public MotivoAbertura getMotivoAbertura() {
        return motivoAbertura;
    }

    public void setMotivoAbertura(MotivoAbertura motivoAbertura) {
        this.motivoAbertura = motivoAbertura;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Integer getPrazoAtendimento() {
        return prazoAtendimento;
    }

    public void setPrazoAtendimento(Integer prazoAtendimento) {
        this.prazoAtendimento = prazoAtendimento;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getObsCampo() {
        return obsCampo;
    }

    public void setObsCampo(String obsCampo) {
        this.obsCampo = obsCampo;
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

    public Date getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Date getDataAtribuicao() {
        return dataAtribuicao;
    }

    public void setDataAtribuicao(Date dataAtribuicao) {
        this.dataAtribuicao = dataAtribuicao;
    }

    public Date getDataSincronismo() {
        return dataSincronismo;
    }

    public void setDataSincronismo(Date dataSincronismo) {
        this.dataSincronismo = dataSincronismo;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public Date getDataDespacho() {
        return dataDespacho;
    }

    public void setDataDespacho(Date dataDespacho) {
        this.dataDespacho = dataDespacho;
    }

    public Integer getNumOs() {
        return numOs;
    }

    public void setNumOs(Integer numOs) {
        this.numOs = numOs;
    }

    public Integer getUsuarioAtribuicao() {
        return usuarioAtribuicao;
    }

    public void setUsuarioAtribuicao(Integer usuarioAtribuicao) {
        this.usuarioAtribuicao = usuarioAtribuicao;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public MotivoTroca getMotivoEncerramento() {
        return motivoEncerramento;
    }

    public void setMotivoEncerramento(MotivoTroca motivoEncerramento) {
        this.motivoEncerramento = motivoEncerramento;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getUsuarioExecucao() {
        return usuarioExecucao;
    }

    public void setUsuarioExecucao(Integer usuarioExecucao) {
        this.usuarioExecucao = usuarioExecucao;
    }

    public Integer getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(Integer idChamado) {
        this.idChamado = idChamado;
    }

    //////////////////////////////////////////////
    // MÉTODOS SOBRESCRITOS
    //////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdemServico)) return false;

        OrdemServico poste = (OrdemServico) o;

        if (!id.equals(poste.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "";
    }

    public String getTelChamado() {
        return telChamado;
    }

    public void setTelChamado(String telChamado) {
        this.telChamado = telChamado;
    }

    public String getNomeChamado() {
        return nomeChamado;
    }

    public void setNomeChamado(String nomeChamado) {
        this.nomeChamado = nomeChamado;
    }

    public String getProtocoloChamado() {
        return protocoloChamado;
    }

    public void setProtocoloChamado(String protocoloChamado) {
        this.protocoloChamado = protocoloChamado;
    }

    public String getQtdChamado() {
        return qtdChamado;
    }

    public void setQtdChamado(String qtdChamado) {
        this.qtdChamado = qtdChamado;
    }
}