package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoResultadoConsulta;
import br.com.velp.vluminum.util.DateTimeUtils;
import br.com.velp.vluminum.util.VLuminumUtil;

public class PontoDao extends DaoBase<Ponto> {

    private static PontoDao dao;

    private PontoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static PontoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new PontoDao(ctx);
        }
        return dao;
    }

    public Ponto buscarPorId(String id) {
        try {
            Ponto ponto = getDatabaseHelper()
                    .getDao(Ponto.class).queryBuilder()
                    .where().eq("id", id)
                    .queryForFirst();

            return ponto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Ponto buscarPorPlaqueta(String plaqueta) {
        try {
            Ponto ponto = getDatabaseHelper()
                    .getDao(Ponto.class).queryBuilder()
                    .where().eq("num_plaqueta_transf", plaqueta)
                    .queryForFirst();

            return ponto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PontoResultadoConsulta> buscarEspecificos(PontoParametroConsulta parametroConsulta) {
        List<PontoResultadoConsulta> listaPontos = new ArrayList<PontoResultadoConsulta>();

        final String logradouro = parametroConsulta.getLogradouro();
        final String pontoReferencia = parametroConsulta.getPontoDeReferencia();
        final String numPlaquetaTransformador = parametroConsulta.getNumPlaquetaTransformador();
        final Integer sincronizado = parametroConsulta.getSincronizado();
        final Municipio municipio = parametroConsulta.getMunicipio();

        boolean possuiWhere = false;

        StringBuilder query = new StringBuilder();

        query.append(" select distinct os.num_os, p.id, p.num_plaqueta_transf, p.logradouro, p.num_logradouro, p.bairro from tb_ponto p ");
        query.append(" left join tb_ordem_ponto op on (op.ponto_id = p.id) ");
        query.append(" left join tb_ordem_servico os on (op.os_id = os.id) ");


        if (logradouro != null && !logradouro.trim().equals("")) {
            query.append(" where p.logradouro like '%" + logradouro + "%'");
            possuiWhere = true;
        }

        if (municipio != null && municipio.getId() != null) {
            if (possuiWhere) {
                query.append(" and p.municipio_id = " + municipio.getId());
            }else{
                query.append(" where p.municipio_id = " + municipio.getId());
            }
            possuiWhere = true;
        }

        if (pontoReferencia != null && !pontoReferencia.trim().equals("")) {
            if (possuiWhere) {
                query.append(" and p.ponto_referencia like '%" + pontoReferencia + "%'");
            }else{
                query.append(" where p.ponto_referencia like '%" + pontoReferencia + "%'");
            }
            possuiWhere = true;

        }

        if (numPlaquetaTransformador != null && !numPlaquetaTransformador.trim().equals("")) {
            if (possuiWhere) {
                query.append(" and p.num_plaqueta_transf like '%" + numPlaquetaTransformador + "%'");
            }else{
                query.append(" where p.num_plaqueta_transf like '%" + numPlaquetaTransformador + "%'");
            }
            possuiWhere = true;

        }
//TODO - Verificar isso.
        /*if (numPlaquetaTransformador != null && !numPlaquetaTransformador.trim().equals("")) {
            if (possuiWhere) {
                query.append(" and p.sincronizado = 0");
            }else{
                query.append(" where p.sincronizado = 0");
            }

    } */

        /*
        if (possuiWhere) {
            query.append(" and os.usuario_atribuicao = ").append(VLuminumUtil.getUsuarioLogado().getId());
        }else{
            query.append(" where os.usuario_atribuicao = ").append(VLuminumUtil.getUsuarioLogado().getId());
        }

        */

        query.append(" order by os.num_os, os.id_motivo_troca");


        try {


            Cursor c = getDatabaseHelper().getWritableDatabase().rawQuery(query.toString(), null);
            c.getCount();
            if (c.moveToFirst()) {


                int idxIdPonto = c.getColumnIndex("id");
                int idxIdOS = c.getColumnIndex("num_os");
                int idxNumPlaqueta = c.getColumnIndex("num_plaqueta_transf");
                int idxLogradouro = c.getColumnIndex("logradouro");
                int idxNumLogradouro = c.getColumnIndex("num_logradouro");
                int idxBairro = c.getColumnIndex("bairro");

                do {

                    PontoResultadoConsulta pontoR = new PontoResultadoConsulta();

                    pontoR.setIdPonto(c.getString(idxIdPonto));
                    pontoR.setBairro(c.getString(idxBairro));

                    pontoR.setLogradouro(c.getString(idxLogradouro));
                    pontoR.setNumLogradouro(c.getString(idxNumLogradouro));
                    pontoR.setNumPlaquetaTransformador(c.getString(idxNumPlaqueta));
                    pontoR.setOrdensServicoDoPonto(c.getString(idxIdOS));

                    listaPontos.add(pontoR);
                } while (c.moveToNext());

            }

            if (c != null && !c.isClosed()) {
                c.close();
            }

            return listaPontos;
        } catch (Exception e) {
         e.toString();
        }

   return listaPontos;



    }

    public List<PontoResultadoConsulta> buscarPorParametroConsulta2(PontoParametroConsulta parametroConsulta) {

        List<PontoResultadoConsulta> listaPontos = new ArrayList<PontoResultadoConsulta>();

        final String logradouro = parametroConsulta.getLogradouro();
        final String pontoReferencia = parametroConsulta.getPontoDeReferencia();
        final String numPlaquetaTransformador = parametroConsulta.getNumPlaquetaTransformador();
        final Integer sincronizado = parametroConsulta.getSincronizado();
        final Municipio municipio = parametroConsulta.getMunicipio();

        boolean possuiWhere = false;

        StringBuilder query = new StringBuilder();

        //logradouro, municipio,  ponto referencia, numPlaqueta, sincronizado


        query.append(" select p.id, null as num_os, p.num_plaqueta_transf, p.logradouro, p.num_logradouro, p.bairro from tb_ponto p ");

        if (logradouro != null && !logradouro.trim().equals("")) {
            query.append(" where p.logradouro like '%" + logradouro + "%'");
            possuiWhere = true;
        }

        if (municipio != null && municipio.getId() != null) {
            if (possuiWhere) {
                query.append(" and p.municipio_id = " + municipio.getId());
            }else{
                query.append(" where p.municipio_id = " + municipio.getId());
            }
            possuiWhere = true;
        }

        if (pontoReferencia != null && !pontoReferencia.trim().equals("")) {
            if (possuiWhere) {
                query.append(" and p.ponto_referencia like '%" + pontoReferencia + "%'");
            }else{
                query.append(" where p.ponto_referencia like '%" + pontoReferencia + "%'");
            }
            possuiWhere = true;

        }

        if (numPlaquetaTransformador != null && !numPlaquetaTransformador.trim().equals("")) {
            if (possuiWhere) {
                query.append(" and p.num_plaqueta_transf like '%" + numPlaquetaTransformador + "%'");
            }else{
                query.append(" where p.num_plaqueta_transf like '%" + numPlaquetaTransformador + "%'");
            }


        }


        try {


            Cursor c = getDatabaseHelper().getWritableDatabase().rawQuery(query.toString(), null);
            c.getCount();
            if (c.moveToFirst()) {


                int idxIdPonto = c.getColumnIndex("id");
                int idxIdOS = c.getColumnIndex("num_os");
                int idxNumPlaqueta = c.getColumnIndex("num_plaqueta_transf");
                int idxLogradouro = c.getColumnIndex("logradouro");
                int idxNumLogradouro = c.getColumnIndex("num_logradouro");
                int idxBairro = c.getColumnIndex("bairro");

                do {

                    PontoResultadoConsulta pontoR = new PontoResultadoConsulta();

                    pontoR.setIdPonto(c.getString(idxIdPonto));
                    pontoR.setBairro(c.getString(idxBairro));

                    pontoR.setLogradouro(c.getString(idxLogradouro));
                    pontoR.setNumLogradouro(c.getString(idxNumLogradouro));
                    pontoR.setNumPlaquetaTransformador(c.getString(idxNumPlaqueta));
                    pontoR.setOrdensServicoDoPonto(c.getString(idxIdOS));

                    listaPontos.add(pontoR);
                } while (c.moveToNext());

            }

            if (c != null && !c.isClosed()) {
                c.close();
            }

            return listaPontos;
        } catch (Exception e) {
            e.toString();
        }

        return listaPontos;


    }





    public List<Ponto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {

        List<Ponto> pontos = null;
        final String logradouro = parametroConsulta.getLogradouro();
        final String pontoReferencia = parametroConsulta.getPontoDeReferencia();
        final String numPlaquetaTransformador = parametroConsulta.getNumPlaquetaTransformador();
        final Integer sincronizado = parametroConsulta.getSincronizado();
        final Municipio municipio = parametroConsulta.getMunicipio();

        boolean possuiLogradouro = false;
        boolean possuiMunicipio = false;
        boolean possuiPontoReferencia = false;

        try {
            QueryBuilder<Ponto, ?> queryBuilder = getDatabaseHelper().getDao(Ponto.class).queryBuilder();
            Where where = queryBuilder.where();
            if (logradouro != null && !logradouro.trim().equals("")) {
                where.like("logradouro", "%" + logradouro + "%");
                possuiLogradouro = true;
            }

            if (municipio != null && municipio.getId() != null) {
                if (possuiLogradouro) {
                    where.and();
                }
                where.eq("municipio_id", municipio.getId());
                possuiMunicipio = true;
            }

            if (pontoReferencia != null && !pontoReferencia.trim().equals("")) {
                if (possuiLogradouro || possuiMunicipio) {
                    where.and();
                }
                where.like("ponto_referencia", "%" + pontoReferencia + "%");
                possuiPontoReferencia = true;
            }

            if (numPlaquetaTransformador != null && !numPlaquetaTransformador.trim().equals("")) {
                if (possuiLogradouro || possuiMunicipio || possuiPontoReferencia) {
                    where.and();
                }
                where.like("num_plaqueta_transf", "%" + numPlaquetaTransformador + "%");
            }

            if (sincronizado != null) {
                where.like("sincronizado", 0);
            }

            PreparedQuery<Ponto> preparedQuery = queryBuilder.prepare();
            pontos = getDatabaseHelper().getDao(Ponto.class).query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return pontos;
    }



    public Ponto buscarPorCoordenadas(double latitude, double longitude) {
        try {
            Ponto ponto = getDatabaseHelper()
                    .getDao(Ponto.class).queryBuilder()
                    .where().eq("latitude", latitude)
                    .and().eq("longitude", longitude)
                    .queryForFirst();

            return ponto;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
