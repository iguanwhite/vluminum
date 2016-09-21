package br.com.velp.vluminum.dao;


import android.content.Context;

import br.com.velp.vluminum.entity.ConfiguracaoWebService;

public class ConfiguracaoWebServiceDao extends DaoBase<ConfiguracaoWebService> {

    private static ConfiguracaoWebServiceDao dao;

    private ConfiguracaoWebServiceDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static ConfiguracaoWebServiceDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new ConfiguracaoWebServiceDao(ctx);
        }
        return dao;
    }

}
