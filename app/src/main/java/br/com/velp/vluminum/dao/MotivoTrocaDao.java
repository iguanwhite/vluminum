package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.MotivoTroca;

public class MotivoTrocaDao extends DaoBase<MotivoTroca> {

    private static MotivoTrocaDao dao;

    private MotivoTrocaDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static MotivoTrocaDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new MotivoTrocaDao(ctx);
        }
        return dao;
    }

}
