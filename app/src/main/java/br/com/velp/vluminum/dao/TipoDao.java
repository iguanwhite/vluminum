package br.com.velp.vluminum.dao;


import android.content.Context;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Tipo;

public class TipoDao extends DaoBase<Tipo> {

    private static TipoDao dao;

    private TipoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static TipoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new TipoDao(ctx);
        }
        return dao;
    }

    public List<Tipo> listarPorGrupo(Integer idGrupo) {
        try {
            List<Tipo> lista = getDatabaseHelper()
                    .getDao(Tipo.class).queryBuilder()
                    .where().eq("id_grupo", idGrupo)
                    .query();

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
}
