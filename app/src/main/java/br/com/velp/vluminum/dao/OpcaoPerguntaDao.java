package br.com.velp.vluminum.dao;


import android.content.Context;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Pergunta;

public class OpcaoPerguntaDao extends DaoBase<OpcaoPergunta> {

    private static OpcaoPerguntaDao dao;

    private OpcaoPerguntaDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static OpcaoPerguntaDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new OpcaoPerguntaDao(ctx);
        }
        return dao;
    }

    public List<OpcaoPergunta> buscarPorPergunta(Pergunta pergunta) {
        try {
            List<OpcaoPergunta> opcoesPergunta = getDatabaseHelper()
                    .getDao(OpcaoPergunta.class).queryBuilder()
                    .where().eq("pergunta_id", pergunta.getId())
                    .query();

            return opcoesPergunta;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

}
