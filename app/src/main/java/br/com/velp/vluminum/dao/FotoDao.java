package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public class FotoDao extends DaoBase<Foto> {

    private static FotoDao dao;

    private FotoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static FotoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new FotoDao(ctx);
        }
        return dao;
    }

    public List<Foto> buscarPorPonto(Ponto ponto) {
        try {
            List<Foto> fotos = getDatabaseHelper()
                    .getDao(Foto.class).queryBuilder()
                    .where().eq("ponto_id", ponto.getId())
                    .query();

            return fotos;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<Foto> buscarPorOrdemServico(OrdemServico ordemServico) {
        try {
            List<Foto> fotos = getDatabaseHelper()
                    .getDao(Foto.class).queryBuilder()
                    .where().eq("ordem_servico_id", ordemServico.getId())
                    .query();

            return fotos;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<Foto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {

        List<Foto> fotos = null;

        final Integer sincronizado = parametroConsulta.getSincronizado();

        try {
            QueryBuilder<Foto, ?> queryBuilder = getDatabaseHelper().getDao(Foto.class).queryBuilder();
            Where where = queryBuilder.where();
            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            PreparedQuery<Foto> preparedQuery = queryBuilder.prepare();
            fotos = getDatabaseHelper().getDao(Foto.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return fotos;
    }


}
