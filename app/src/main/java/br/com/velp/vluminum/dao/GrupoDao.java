package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.Tipo;


public class GrupoDao extends DaoBase<Grupo> {

    private static GrupoDao dao;

    private GrupoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static GrupoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new GrupoDao(ctx);
        }
        return dao;
    }


    public List<Grupo> listarCompletos() {
        List<Grupo> resultado = new ArrayList<Grupo>();

        Cursor cursor = null;
        try {
            cursor = getDatabaseHelper().getWritableDatabase().rawQuery("select distinct g.id_grupo as id from tb_grupo g inner join tb_tipo t on (g.id_grupo = t.id_grupo) inner join tb_material m on (m.id_tipo = t.id_tipo)", null);

            if (cursor.moveToFirst()) {
                do {
                    int indiceId = cursor.getColumnIndex("id");
                    String id = cursor.getString(indiceId);

                    Grupo g = buscarPorId(id);

                    resultado.add(g);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return resultado;

    }



}
