package br.com.velp.vluminum.dao;


import android.content.Context;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Pergunta;

public class PerguntaDao extends DaoBase<Pergunta> {

    private static PerguntaDao dao;

    private PerguntaDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static PerguntaDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new PerguntaDao(ctx);
        }
        return dao;
    }

    public List<Pergunta> listarAtivas() {
        try {
            List<Pergunta> perguntas = getDatabaseHelper()
                    .getDao(Pergunta.class).queryBuilder()
                    .where().eq("ativa", Boolean.TRUE)
                    .query();

            return perguntas;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

}
