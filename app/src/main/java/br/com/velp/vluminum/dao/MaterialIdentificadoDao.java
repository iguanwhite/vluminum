package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.MaterialService;
import br.com.velp.vluminum.serviceimpl.MaterialServiceImpl;
import br.com.velp.vluminum.util.DateTimeUtils;

public class MaterialIdentificadoDao extends DaoBase<MaterialIdentificado> {

    private static MaterialIdentificadoDao dao;

    private MaterialIdentificadoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static MaterialIdentificadoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new MaterialIdentificadoDao(ctx);
        }
        return dao;
    }

    public List<MaterialIdentificado> listarPorPonto(String idPonto) {
        try {

            List<MaterialIdentificado> lista = getDatabaseHelper().getDao(MaterialIdentificado.class).queryBuilder().orderBy("classificacao", false)
                    .where().like("id_ponto", idPonto)
                    .query();

            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }


    public MaterialIdentificado buscarPorNumSerie(String numSerie) {

        try {
            MaterialIdentificado m = getDatabaseHelper()
                    .getDao(MaterialIdentificado.class).queryBuilder()
                    .where().eq("num_serie", numSerie)
                    .queryForFirst();

            return m;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public MaterialIdentificado buscarPorNumSerieEGrupo(String numSerie, int idGrupo, Context ctx) {


        Cursor c = getDatabaseHelper().getWritableDatabase().rawQuery(
                "select * from tb_material_identificado m inner join tb_material a on m.id_material = a.id_material inner join tb_tipo t on t.id_tipo = a.id_tipo where m.num_serie = '" + numSerie + "' and t.id_grupo = " + idGrupo
                , null);


        MaterialIdentificado mi = null;
        if (c.getCount() > 0) {
            c.moveToFirst();

            int idxIdMaterialIdentificado = c.getColumnIndex("id_material_identificado");
            int idxIdMaterial = c.getColumnIndex("id_material");
            int idxIdCliente = c.getColumnIndex("id_cliente");
            int idxIdPonto = c.getColumnIndex("id_ponto");
            int idxIdOS = c.getColumnIndex("id_os");
            int idxNumSerie = c.getColumnIndex("num_serie");
            int idxCusto = c.getColumnIndex("custo");
            int idxDataRetirada = c.getColumnIndex("data_retirada");
            int idxPosicaoInstalacao = c.getColumnIndex("posicao_instalacao");
            int idxDataInstalacao = c.getColumnIndex("data_instalacao");
            int idxSituacao = c.getColumnIndex("situacao");
            int idxDataSincronizacao = c.getColumnIndex("data_sincronizacao");
            int idxSincronizado = c.getColumnIndex("sincronizado");
            int idxClassificacao = c.getColumnIndex("classificacao");

            mi = new MaterialIdentificado();

            mi.setIdMaterialIdentificado(c.getString(idxIdMaterialIdentificado));
            mi.setMaterial(buscarMaterialCorrespondente(c.getInt(idxIdMaterial), ctx));
            mi.setIdCliente(c.getInt(idxIdCliente));
            mi.setIdPonto(c.getString(idxIdPonto));
            mi.setIdOs(c.getString(idxIdOS));
            mi.setNumSerie(c.getString(idxNumSerie));
            mi.setCusto(c.getDouble(idxCusto));
            mi.setDataRetirada(DateTimeUtils.converteStringParaDateFormatoCompleto(c.getString(idxDataRetirada)));
            mi.setPosicaoInstalacao(c.getInt(idxPosicaoInstalacao));
            mi.setDataInstalacao(DateTimeUtils.converteStringParaDateFormatoCompleto(c.getString(idxDataInstalacao)));
            mi.setSituacao(c.getInt(idxSituacao));
            mi.setDataSincronizacao(DateTimeUtils.converteStringParaDateFormatoCompleto(c.getString(idxDataSincronizacao)));
            mi.setSincronizado(c.getInt(idxSincronizado));
            mi.setClassificacao(c.getInt(idxClassificacao));

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

        return mi;


    }

    private Material buscarMaterialCorrespondente(Integer idMaterial, Context ctx) {

        MaterialService nRN = new MaterialServiceImpl(ctx);

        Material u = nRN.buscarPorId(idMaterial);

        return u;

    }


    public List<MaterialIdentificado> listarPorOs(String idOs) {
        try {
            List<MaterialIdentificado> lista = getDatabaseHelper()
                    .getDao(MaterialIdentificado.class).queryBuilder()
                    .where().eq("id_os", idOs)
                    .query();

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<MaterialIdentificado> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {

        List<MaterialIdentificado> materialIdentificados = null;

        final Integer sincronizado = parametroConsulta.getSincronizado();

        try {
            QueryBuilder<MaterialIdentificado, ?> queryBuilder = getDatabaseHelper().getDao(MaterialIdentificado.class).queryBuilder();
            Where where = queryBuilder.where();
            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            PreparedQuery<MaterialIdentificado> preparedQuery = queryBuilder.prepare();
            materialIdentificados = getDatabaseHelper().getDao(MaterialIdentificado.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return materialIdentificados;
    }


}
