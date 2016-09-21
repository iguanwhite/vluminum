package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.Poste;

public class PosteDao extends DaoBase<Poste> {

    private static PosteDao dao;

    private PosteDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static PosteDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new PosteDao(ctx);
        }
        return dao;
    }

}
