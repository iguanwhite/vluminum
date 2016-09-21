package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;

/**
 * Created by Diogo.Salvador on 16/01/2015
 */
@DatabaseTable(tableName = "tb_usuario_cliente")
public class UsuarioCliente extends EntityBase {

    @DatabaseField(id = true)
    private Integer id;
    @DatabaseField(columnName = "id_usuario")
    private Integer idUsuario;
    @DatabaseField(columnName = "id_cliente")
    private Integer idCliente;

    public UsuarioCliente() {

    }

    public UsuarioCliente(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}
