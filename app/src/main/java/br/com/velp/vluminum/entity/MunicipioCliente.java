package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.velp.vluminum.dao.EntityBase;

@DatabaseTable(tableName = "tb_municipio_cliente")
public class MunicipioCliente extends EntityBase {

    //////////////////////////////////////////////
    // ATRIBUTOS
    //////////////////////////////////////////////
    @DatabaseField(id = true, columnName = "id_municipio_cliente")
    private Integer id;

    @DatabaseField(columnName = "id_municipio")
    private Integer idMunicipio;

    @DatabaseField(columnName = "id_cliente")
    private Integer idCliente;

    @DatabaseField(columnName = "usuario_vinculacao")
    private Integer usuarioVinculacao;

    @DatabaseField(columnName = "data_vinculacao")
    private Date dataVinculacao;

    //////////////////////////////////////////////
    // CONSTRUTORES
    //////////////////////////////////////////////
    public MunicipioCliente() {

    }

    public MunicipioCliente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getUsuarioVinculacao() {
        return usuarioVinculacao;
    }

    public void setUsuarioVinculacao(Integer usuarioVinculacao) {
        this.usuarioVinculacao = usuarioVinculacao;
    }

    public Date getDataVinculacao() {
        return dataVinculacao;
    }

    public void setDataVinculacao(Date dataVinculacao) {
        this.dataVinculacao = dataVinculacao;
    }
}
