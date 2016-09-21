package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.TipoPoste;

public class TipoPosteDao extends DaoBase<TipoPoste> {

    private static TipoPosteDao dao;

    private TipoPosteDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static TipoPosteDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new TipoPosteDao(ctx);
        }
        return dao;
    }

}
