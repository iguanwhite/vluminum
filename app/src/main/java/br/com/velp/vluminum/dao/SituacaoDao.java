package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.Situacao;

public class SituacaoDao extends DaoBase<Situacao> {

    private static SituacaoDao dao;

    private SituacaoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static SituacaoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new SituacaoDao(ctx);
        }
        return dao;
    }

}
