package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.OrdemServicoPonto;


public class OrdemServicoPontoDao extends DaoBase<OrdemServicoPonto> {

    private static OrdemServicoPontoDao dao;

    private OrdemServicoPontoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static OrdemServicoPontoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new OrdemServicoPontoDao(ctx);
        }
        return dao;
    }

    public Long quantidadePontosDaOsNaoFinalizados(String idOs) {
        Long count = null;
        Cursor cursor = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append(" select count(*) from tb_ordem_ponto ")
                    .append(" where os_id = '").append(idOs).append("' ")
                    .append(" and (situacao is null or situacao == 0) ");
            cursor = dao.getDatabaseHelper().getReadableDatabase().rawQuery(query.toString(), null);
            if (cursor != null) {
                if (cursor.getCount() > 0){
                    cursor.moveToFirst();
                    count = (long) cursor.getInt(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return count;
    }

    public Long quantidadePontosDaOs(String idOs) {
        Long count = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("os_id", idOs);

            PreparedQuery<OrdemServicoPonto> preparedQuery = queryBuilder.setCountOf(true).prepare();
            count = getDatabaseHelper().getDao(OrdemServicoPonto.class).countOf(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return count;
    }


    public List<OrdemServicoPonto> listarPorOs(String idOs) {
        List<OrdemServicoPonto> listOs = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("os_id", idOs);

            PreparedQuery<OrdemServicoPonto> preparedQuery = queryBuilder.prepare();
            listOs = getDatabaseHelper().getDao(OrdemServicoPonto.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return listOs;
    }

    public List<OrdemServicoPonto> buscarPorPonto(String pontoId) {
        List<OrdemServicoPonto> resultado = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("ponto_id", pontoId);

            PreparedQuery<OrdemServicoPonto> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoPonto.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resultado;
    }

    public OrdemServicoPonto buscarPorOsEPonto(String osId, String pontoId) {
        OrdemServicoPonto resultado = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("os_id", osId);
            where.and();
            where.eq("ponto_id", pontoId);

            PreparedQuery<OrdemServicoPonto> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return resultado;
        }

        return resultado;
    }

    public void deletarPorNumOs(String idOs) {

        DeleteBuilder<OrdemServicoPonto, ?> deleteBuilder = null;
        try {
            deleteBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).deleteBuilder();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteBuilder.where().eq("os_id", idOs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public boolean deletarPorNumOsEPonto(String idOs, String idPonto) {

        DeleteBuilder<OrdemServicoPonto, ?> deleteBuilder = null;

        boolean result = true;
        try {
            deleteBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).deleteBuilder();
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }
        try {
            deleteBuilder.where().eq("os_id", idOs).and().eq("ponto_id", idPonto);
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

    public List<OrdemServicoPonto> listarNaoSincronizados() {
        List<OrdemServicoPonto> resultado = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> queryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            Where where = queryBuilder.where();
            where.eq("sincronizado", 0);

            PreparedQuery<OrdemServicoPonto> preparedQuery = queryBuilder.prepare();
            resultado = getDatabaseHelper().getDao(OrdemServicoPonto.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resultado;
    }










}
