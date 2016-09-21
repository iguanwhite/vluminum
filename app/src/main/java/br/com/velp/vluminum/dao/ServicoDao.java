package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;

public class ServicoDao extends DaoBase<Servico> {

    private static ServicoDao dao;

    private ServicoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static ServicoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new ServicoDao(ctx);
        }
        return dao;
    }

    public Servico buscarPorId(String id) {
        try {
            Servico servico = getDatabaseHelper().getDao(Servico.class).queryBuilder().where().eq("id", id).queryForFirst();
            return servico;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Servico> listarPorGrupo(Integer idGrupo) {
        try {
            List<Servico> lista = getDatabaseHelper()
                    .getDao(Servico.class).queryBuilder()
                    .where().eq("grupo_id", idGrupo)
                    .query();

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }


    public List<Servico> buscarPorParametroConsulta(ServicoParametroConsulta parametroConsulta) {

        List<Servico> servico = null;

        final Integer sincronizado = parametroConsulta.getSincronizado();
        final GrupoServico grupoServico = parametroConsulta.getGrupoServico();

        try {
            QueryBuilder<Servico, ?> queryBuilder = getDatabaseHelper().getDao(Servico.class).queryBuilder();
            Where where = queryBuilder.where();
            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            if (grupoServico != null) {
                where.eq("grupo_id", grupoServico.getId());
            }


            PreparedQuery<Servico> preparedQuery = queryBuilder.prepare();
            servico = getDatabaseHelper().getDao(Servico.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return servico;
    }

}
