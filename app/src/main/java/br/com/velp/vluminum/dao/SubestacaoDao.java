package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.Subestacao;

public class SubestacaoDao extends DaoBase<Subestacao> {

    private static SubestacaoDao dao;

    private SubestacaoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static SubestacaoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new SubestacaoDao(ctx);
        }
        return dao;
    }

}
