package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public class RespostaDao extends DaoBase<Resposta> {

    private static RespostaDao dao;

    private RespostaDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static RespostaDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new RespostaDao(ctx);
        }
        return dao;
    }

    public List<Resposta> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {

        List<Resposta> resposta = null;

        final Integer sincronizado = parametroConsulta.getSincronizado();

        try {
            QueryBuilder<Resposta, ?> queryBuilder = getDatabaseHelper().getDao(Resposta.class).queryBuilder();
            Where where = queryBuilder.where();
            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            PreparedQuery<Resposta> preparedQuery = queryBuilder.prepare();
            resposta = getDatabaseHelper().getDao(Resposta.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return resposta;
    }

    public boolean buscaRespostaMultiplaEscolha(Integer idPergunta, String idPonto, Integer resp) {
        List<Resposta> resposta = null;


        try {
            QueryBuilder<Resposta, ?> queryBuilder = getDatabaseHelper().getDao(Resposta.class).queryBuilder();
            Where where = queryBuilder.where();

            where.eq("pergunta_id", idPergunta);
            where.and();
            where.eq("ponto_id", idPonto);
            where.and();
            where.eq("opcaoPergunta_id", resp);


            PreparedQuery<Resposta> preparedQuery = queryBuilder.prepare();
            resposta = getDatabaseHelper().getDao(Resposta.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (resposta != null && !resposta.isEmpty()) {
            return true;
        }
        return false;
    }

    public String buscaRespostaTexto(Integer idPergunta, String idPonto) {
        List<Resposta> resposta = null;


        try {
            QueryBuilder<Resposta, ?> queryBuilder = getDatabaseHelper().getDao(Resposta.class).queryBuilder();
            Where where = queryBuilder.where();

            where.eq("pergunta_id", idPergunta);
            where.and();
            where.eq("ponto_id", idPonto);


            PreparedQuery<Resposta> preparedQuery = queryBuilder.prepare();
            resposta = getDatabaseHelper().getDao(Resposta.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

        if (resposta != null && !resposta.isEmpty()) {
            return resposta.get(0).getResposta();
        }
        return "";
    }

    public OpcaoPergunta buscaRespostaSpinner(Integer idPergunta, String idPonto) {
        List<Resposta> resposta = null;


        try {
            QueryBuilder<Resposta, ?> queryBuilder = getDatabaseHelper().getDao(Resposta.class).queryBuilder();
            Where where = queryBuilder.where();

            where.eq("pergunta_id", idPergunta);
            where.and();
            where.eq("ponto_id", idPonto);


            PreparedQuery<Resposta> preparedQuery = queryBuilder.prepare();
            resposta = getDatabaseHelper().getDao(Resposta.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (resposta != null && !resposta.isEmpty()) {
            return resposta.get(0).getOpcaoPergunta();
        }
        return null;
    }

    public boolean apagarRespostaPonto(String idPonto) {
        try {
            DeleteBuilder<Resposta, ?> deleteBuilder = getDatabaseHelper().getDao(Resposta.class).deleteBuilder();
            Where where = deleteBuilder.where();


            where.eq("ponto_id", idPonto);


            PreparedDelete<Resposta> preparedQuery = deleteBuilder.prepare();
            getDatabaseHelper().getDao(Resposta.class).delete(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
