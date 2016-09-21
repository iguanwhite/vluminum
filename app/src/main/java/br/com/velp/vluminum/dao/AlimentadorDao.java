package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.Alimentador;

public class AlimentadorDao extends DaoBase<Alimentador> {

    private static AlimentadorDao dao;

    private AlimentadorDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static AlimentadorDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new AlimentadorDao(ctx);
        }
        return dao;
    }

}
