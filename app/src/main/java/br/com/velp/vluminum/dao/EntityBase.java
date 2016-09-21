package br.com.velp.vluminum.dao;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe abstrata responsável por conter os atributos comuns de uma entidade persistente.
 * <p/>
 * Created by Bruno Leonardo on 06/12/2014.
 */
public abstract class EntityBase implements Serializable {

    @DatabaseField(columnName = "data_cadastro", canBeNull = false)
    private Date dataCadastro;

    @DatabaseField(columnName = "data_alteracao")
    private Date dataAlteracao;

    @DatabaseField(columnName = "usuario_cadastro", canBeNull = false)
    private Integer usuarioCadastro;

    @DatabaseField(columnName = "usuario_alteracao")
    private Integer usuarioAlteracao;

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Integer usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public Integer getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Integer usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }
}
