package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.OrdemServicoServico;


public class OrdemServicoServicoDao extends DaoBase<OrdemServicoServico> {

    private static OrdemServicoServicoDao dao;

    private OrdemServicoServicoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static OrdemServicoServicoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new OrdemServicoServicoDao(ctx);
        }
        return dao;
    }

    public List<OrdemServicoServico> listarPorPonto(String pontoId) {
        List<OrdemServicoServico> resultado = null;

        try {
            QueryBuilder<OrdemServicoServico, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoServico.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("ponto_id", pontoId);

            PreparedQuery<OrdemServicoServico> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoServico.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resultado;
    }


    public List<OrdemServicoServico> listarPorOrdemServico(String osId) {
        List<OrdemServicoServico> resultado = null;

        try {
            QueryBuilder<OrdemServicoServico, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoServico.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("os_id", osId);

            PreparedQuery<OrdemServicoServico> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoServico.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resultado;
    }


    public List<OrdemServicoServico> listarNaoSincronizado() {
        List<OrdemServicoServico> resultado = null;

        try {
            QueryBuilder<OrdemServicoServico, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoServico.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("sincronizado", 0);

            PreparedQuery<OrdemServicoServico> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoServico.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resultado;
    }

}
