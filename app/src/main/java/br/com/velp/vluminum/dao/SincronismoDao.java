package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.velp.vluminum.entity.Sincronismo;

public class SincronismoDao extends DaoBase<Sincronismo> {

    private static SincronismoDao dao;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SincronismoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static SincronismoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new SincronismoDao(ctx);
        }
        return dao;
    }

    public Date buscarDataUltimoSincronismo(String tabela) {
        try {
            String select = "select data_sincronismo from tb_sincronismo where tabela = '" + tabela + "'";
            String data = null;
            Cursor c = dao.getDatabaseHelper().getReadableDatabase().rawQuery(select, null);
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    data = c.getString(c.getColumnIndex("data_sincronismo"));
                    Date date = format.parse(data);
                    return date;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String buscarDataFormatadaUltimoSincronismo(String tabela) {
        try {
            String select = "select data_sincronismo from tb_sincronismo where tabela = '" + tabela + "'";
            String data = null;
            Cursor c = dao.getDatabaseHelper().getReadableDatabase().rawQuery(select, null);
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    data = c.getString(c.getColumnIndex("data_sincronismo"));
                    Date date = format.parse(data);
                    return format.format(date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizarDataSincronismo(String tabela, String data) {
        try {
            //Verifica se existe, se n existir cria
            String select = "select data_sincronismo from tb_sincronismo where tabela = '" + tabela + "'";
            Cursor c = dao.getDatabaseHelper().getReadableDatabase().rawQuery(select, null);
            if (c.getCount() == 0) {
                Sincronismo sinc = new Sincronismo();
                sinc.setTabela(tabela);
                sinc.setDataSincronismo(new java.util.Date(format.parse(data).getTime()));
                dao.salvarOuAtualizar(sinc);
            } else if (c.moveToFirst()) {
                UpdateBuilder<Sincronismo, ?> updateBuilder = getDatabaseHelper().getDao(Sincronismo.class).updateBuilder();
                updateBuilder.where().eq("tabela", tabela);
                updateBuilder.updateColumnValue("data_sincronismo", new java.util.Date(format.parse(data).getTime()));
                updateBuilder.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletarTabelaSinc(String tabela) {
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(ctx, DatabaseHelper.class);
            Dao dao = helper.getDao(Sincronismo.class);
            DeleteBuilder<Sincronismo, ?> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("tabela", tabela);
            deleteBuilder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
