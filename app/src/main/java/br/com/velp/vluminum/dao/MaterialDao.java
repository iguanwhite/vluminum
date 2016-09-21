package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public class MaterialDao extends DaoBase<Material> {

    private static MaterialDao dao;

    private MaterialDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static MaterialDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new MaterialDao(ctx);
        }
        return dao;
    }

    public List<Material> listarPorTipo(Integer idTipo) {
        try {
            List<Material> lista = getDatabaseHelper()
                    .getDao(Material.class).queryBuilder()
                    .where().eq("id_tipo", idTipo)
                    .query();

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<Material> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {

        List<Material> material = null;

        final Integer sincronizado = parametroConsulta.getSincronizado();

        try {
            QueryBuilder<Material, ?> queryBuilder = getDatabaseHelper().getDao(Material.class).queryBuilder();
            Where where = queryBuilder.where();
            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            PreparedQuery<Material> preparedQuery = queryBuilder.prepare();
            material = getDatabaseHelper().getDao(Material.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return material;
    }
}
