package br.com.velp.vluminum.dao;


import android.content.Context;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.UsuarioCliente;

public class UsuarioClienteDao extends DaoBase<UsuarioCliente> {

    private static UsuarioClienteDao dao;

    private UsuarioClienteDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static UsuarioClienteDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new UsuarioClienteDao(ctx);
        }
        return dao;
    }

    public List<UsuarioCliente> listarPorUsuario(Integer idUsuario) {
        try {
            List<UsuarioCliente> lista = getDatabaseHelper()
                    .getDao(UsuarioCliente.class).queryBuilder()
                    .where().eq("id_usuario", idUsuario)
                    .query();

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }


}
