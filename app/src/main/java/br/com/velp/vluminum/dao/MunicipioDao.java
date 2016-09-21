package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.util.VLuminumUtil;

public class MunicipioDao extends DaoBase<Municipio> {

    private static MunicipioDao dao;

    private MunicipioDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static MunicipioDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new MunicipioDao(ctx);
        }
        return dao;
    }

    public List<Municipio> listarPorEstado(Estado estado) {
        try {
            List<Municipio> municipios = getDatabaseHelper()
                    .getDao(Municipio.class).queryBuilder()
                    .where().eq("uf", estado.getSigla())
                    .query();

            return municipios;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<Municipio> listarComOSAbertas() {
        List<String> consulta = null;

        List<Municipio> listMunicipio = new ArrayList<Municipio>();
        String[] metaMunicipio = new String[4];

        Cursor cursor = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select distinct m.id, m.nome, m.uf, c.id_cliente from tb_municipio m " +
                    " inner join tb_ordem_servico o on m.id = o.id_municipio " +
                    " inner join tb_municipio_cliente c on m.id = c.id_municipio " +
                    " inner join tb_situacao s on s.id = o.id_situacao ");
                    //+ " where s.id = 9 ");
            cursor = dao.getDatabaseHelper().getReadableDatabase().rawQuery(query.toString(), null);

            consulta = new LinkedList<String>();
            if (cursor.moveToFirst()) {
                do {


                    metaMunicipio[0] = (cursor.getString(cursor.getColumnIndexOrThrow("id")));
                    metaMunicipio[1] = (cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                    metaMunicipio[2] = (cursor.getString(cursor.getColumnIndexOrThrow("uf")));
                    metaMunicipio[3] = (cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));

                    listMunicipio.add(converterMunicipio(metaMunicipio));

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return listMunicipio;
    }



    public List<Municipio> listarPorCliente() {
        List<String> consulta = null;

        List<Municipio> listMunicipio = new ArrayList<Municipio>();
        String[] metaMunicipio = new String[4];

        Cursor cursor = null;
        try {
            StringBuilder query = new StringBuilder();


            query.append("  select distinct m.id, m.nome, m.uf, c.id_cliente from tb_municipio m " +
           " inner join tb_municipio_cliente c on m.id = c.id_municipio " +
           " inner join tb_usuario_cliente uc on uc.id_cliente = c.id_cliente " +
           " where id_usuario = " + VLuminumUtil.getUsuarioLogado().getId());

            cursor = dao.getDatabaseHelper().getReadableDatabase().rawQuery(query.toString(), null);

            consulta = new LinkedList<String>();
            if (cursor.moveToFirst()) {
                do {


                    metaMunicipio[0] = (cursor.getString(cursor.getColumnIndexOrThrow("id")));
                    metaMunicipio[1] = (cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                    metaMunicipio[2] = (cursor.getString(cursor.getColumnIndexOrThrow("uf")));
                    metaMunicipio[3] = (cursor.getString(cursor.getColumnIndexOrThrow("id_cliente")));

                    listMunicipio.add(converterMunicipio(metaMunicipio));

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return listMunicipio;
    }









    public List<Municipio> listarPorEstado(String uf) {
        try {
            List<Municipio> municipios = getDatabaseHelper()
                    .getDao(Municipio.class).queryBuilder()
                    .where().eq("uf", uf)
                    .query();

            return municipios;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<String> listarEstados() {
        List<String> estados = null;
        Cursor cursor = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT uf FROM tb_municipio ");
            cursor = dao.getDatabaseHelper().getReadableDatabase().rawQuery(query.toString(), null);

            estados = new LinkedList<String>();
            if (cursor.moveToFirst()) {
                do {
                    estados.add(cursor.getString(cursor.getColumnIndexOrThrow("uf")));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return estados;
    }


    private Municipio converterMunicipio(String[] c) {
        Municipio m = new Municipio();
        m.setId(Integer.valueOf(c[0]));
        m.setNome(c[1]);
        m.setUf(c[2]);
        m.setIdCliente(Integer.valueOf(c[3]));

        return m;
    }


}
