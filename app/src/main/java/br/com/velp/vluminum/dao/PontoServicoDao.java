package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.PontoServico;


public class PontoServicoDao extends DaoBase<PontoServico> {

    private static PontoServicoDao dao;

    private PontoServicoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static PontoServicoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new PontoServicoDao(ctx);
        }
        return dao;
    }

    public List<PontoServico> listarPorPonto(String idPonto) {
        List<PontoServico> listPonto = null;

        try {
            QueryBuilder<PontoServico, ?> queryBuilder = getDatabaseHelper().getDao(PontoServico.class).queryBuilder();
            Where where = queryBuilder.where();

            where.eq("ponto_id", idPonto);

            PreparedQuery<PontoServico> preparedQuery = queryBuilder.prepare();
            listPonto = getDatabaseHelper().getDao(PontoServico.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return listPonto;
    }

    public void deletarPorPonto(String idPonto) {

        DeleteBuilder<PontoServico, ?> deleteBuilder = null;
        try {
            deleteBuilder = getDatabaseHelper().getDao(PontoServico.class).deleteBuilder();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteBuilder.where().eq("ponto_id", idPonto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public boolean deletarPorPontoEServico(String idPonto, Integer idServico) {

        DeleteBuilder<PontoServico, ?> deleteBuilder = null;

        boolean result = true;
        try {
            deleteBuilder = getDatabaseHelper().getDao(PontoServico.class).deleteBuilder();
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }
        try {
            deleteBuilder.where().eq("ponto_id", idPonto).and().eq("servico_id", idServico);
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }
        try {
            deleteBuilder.delete();
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }

        return result;


    }


}
