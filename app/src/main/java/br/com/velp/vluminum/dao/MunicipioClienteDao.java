package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import br.com.velp.vluminum.entity.MunicipioCliente;

public class MunicipioClienteDao extends DaoBase<MunicipioCliente> {

    private static MunicipioClienteDao dao;

    private MunicipioClienteDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static MunicipioClienteDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new MunicipioClienteDao(ctx);
        }
        return dao;
    }


    public Integer buscaClientePorIdMunicipio(Integer idMunicipio) {
        try {
            QueryBuilder<MunicipioCliente, ?> queryBuilder = getDatabaseHelper().getDao(MunicipioCliente.class).queryBuilder();
            Where where = queryBuilder.where();

            where.eq("id_municipio", idMunicipio);

            PreparedQuery<MunicipioCliente> preparedQuery = queryBuilder.prepare();
            List<MunicipioCliente> m = getDatabaseHelper().getDao(MunicipioCliente.class).query(preparedQuery);
            if (m != null && !m.isEmpty()) {
                return m.get(0).getIdCliente();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
