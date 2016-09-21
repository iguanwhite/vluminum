package br.com.velp.vluminum.dao;

import android.content.Context;

import br.com.velp.vluminum.entity.GrupoServico;

public class GrupoServicoDao extends DaoBase<GrupoServico> {

    private static GrupoServicoDao dao;

    private GrupoServicoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static GrupoServicoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new GrupoServicoDao(ctx);
        }
        return dao;
    }

}
