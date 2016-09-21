package br.com.velp.vluminum.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.velp.vluminum.dao.EntityBase;
import br.com.velp.vluminum.enumerator.TipoSincronismo;

@DatabaseTable(tableName = "tb_configuracao_ws")
public class ConfiguracaoWebService extends EntityBase {

    @DatabaseField(id = true)
    private Integer idUsuario;

    @DatabaseField(columnName = "url", width = 100, canBeNull = false)
    private String url;

    @DatabaseField(columnName = "porta", canBeNull = false)
    private String porta;

    @DatabaseField(columnName = "usuario", canBeNull = false)
    private String usuario;

    @DatabaseField(columnName = "senha", canBeNull = false)
    private String senha;

    @DatabaseField(columnName = "nomeWS", canBeNull = false)
    private String nomeWS;

    private TipoSincronismo tipoSincronismo;

    public ConfiguracaoWebService() {

    }

    public ConfiguracaoWebService(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer id) {
        this.idUsuario = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getNomeWS() {
        return nomeWS;
    }

    public void setNomeWS(String nomeWS) {
        this.nomeWS = nomeWS;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoSincronismo getTipoSincronismo() {
        return tipoSincronismo;
    }

    public void setTipoSincronismo(TipoSincronismo tipoSincronismo) {
        this.tipoSincronismo = tipoSincronismo;
    }
}
