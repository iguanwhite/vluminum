package br.com.velp.vluminum.dao;


import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.enumerator.Prioridade;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;
import br.com.velp.vluminum.util.DateTimeUtils;
import br.com.velp.vluminum.util.VLuminumUtil;
import br.com.velp.vluminum.entity.Situacao;

public class OrdemServicoDao extends DaoBase<OrdemServico> {

    private static OrdemServicoDao dao;

    private OrdemServicoDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static OrdemServicoDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new OrdemServicoDao(ctx);
        }
        return dao;
    }

    public List<OrdemServico> buscarPorUsuarioAtribuicao(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        try {
            List<OrdemServico> ordensServico = getDatabaseHelper()
                    .getDao(OrdemServico.class)
                    .queryBuilder()
                    .orderBy("id_motivo_troca", true)
                    .orderBy(nomeColunaOrderBy, ordenacaoAscendente)
                    .where().eq("usuario_atribuicao", VLuminumUtil.getUsuarioLogado().getId())
                    .query();

            return ordensServico;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    public List<OrdemServico> buscarPorPontoEUsuarioAtribuicao(String idPonto, String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        List<OrdemServico> results = null;

        try {
            QueryBuilder<OrdemServicoPonto, ?> ordemServicoPontoQueryBuilder = getDatabaseHelper().getDao(OrdemServicoPonto.class).queryBuilder();
            ordemServicoPontoQueryBuilder.where().eq("ponto_id", idPonto);

            QueryBuilder<OrdemServico, ?> ordemServicoQueryBuilder = getDatabaseHelper().getDao(OrdemServico.class).queryBuilder();
            ordemServicoQueryBuilder.where().eq("usuario_atribuicao", VLuminumUtil.getUsuarioLogado().getId());

            results = ordemServicoQueryBuilder.join(ordemServicoPontoQueryBuilder).orderBy(nomeColunaOrderBy, ordenacaoAscendente).
                    orderBy("id_motivo_troca", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        return results;
    }

    public List<OrdemServico> buscarPorParametroConsulta(OsParametroConsulta parametroConsulta) {

        List<OrdemServico> listOs = new LinkedList<OrdemServico>();
        final Municipio municipio = parametroConsulta.getMunicipio();
        final String logradouro = parametroConsulta.getLogradouro();
        final String numeroOs = parametroConsulta.getNumeroOs();
        final Prioridade prioridade = parametroConsulta.getPrioridade();
        final Ponto ponto = parametroConsulta.getPonto();
        final Integer sincronizado = parametroConsulta.getSincronizado();
		final Integer situacao = parametroConsulta.getSituacao();
        final Date dataInicial = parametroConsulta.getDataInicial();
        final Date dataFinal = parametroConsulta.getDataFinal();

        boolean possuiUsuario = true;
        boolean possuiPonto = false;
        boolean possuiMunicipio = false;
        boolean possuiLogradouro = false;
        boolean possuiNumeroOs = false;
        boolean possuiPrioridade = false;
		boolean possuiSincronismo = false;

        StringBuilder query = new StringBuilder();
        query.append(" select distinct os.id, os.id  from tb_ordem_servico os ");
        query.append(" left join tb_ordem_ponto os_ponto on (os.id = os_ponto.os_id) ");
        query.append(" where os.usuario_atribuicao = ").append(VLuminumUtil.getUsuarioLogado().getId());

        if (ponto != null && ponto.getId() != null) {
            if (possuiUsuario) {
                query.append(" and ");
            }
            query.append(" os_ponto.ponto_id = '").append(ponto.getId()).append("'");
            possuiPonto = true;
        }

        if (municipio != null && municipio.getId() != null) {
            if (possuiUsuario || possuiPonto) {
                query.append(" and ");
            }
            query.append(" os.id_municipio = ").append(municipio.getId());
            possuiMunicipio = true;
        }

        if (logradouro != null && !logradouro.trim().equals("")) {
            if (possuiUsuario || possuiPonto || possuiMunicipio) {
                query.append(" and ");
            }
            query.append(" os.logradouro like '%").append(logradouro.toUpperCase()).append("%'");
            possuiLogradouro = true;
        }

        if (numeroOs != null && !numeroOs.trim().equals("")) {
            if (possuiUsuario || possuiPonto || possuiMunicipio || possuiLogradouro) {
                query.append(" and ");
            }
            query.append(" os.num_os = ").append(numeroOs);
            possuiNumeroOs = true;
        }


        if (sincronizado != null) {
            if (possuiUsuario || possuiPonto || possuiMunicipio || possuiLogradouro || possuiNumeroOs || possuiPrioridade) {
                query.append(" and ");
            }
            query.append(" os.sincronizado = ").append(sincronizado);
            possuiSincronismo = true;

        }

        if (dataInicial != null) {
            if (possuiUsuario || possuiPonto || possuiMunicipio || possuiLogradouro || possuiNumeroOs || possuiPrioridade || possuiSincronismo) {
                query.append(" and ");
            }

            String di = DateTimeUtils.converterDataParaStringFormatoCompleto(dataInicial);
            String df = DateTimeUtils.converterDataParaStringFormatoCompleto(dataFinal);

            query.append(" os.data_cadastro between '").append(di)
                 .append("' and '").append(df).append("'");

        }


		/*
		  if (situacao != null) {
            if (possuiUsuario || possuiPonto || possuiMunicipio || possuiLogradouro || possuiNumeroOs || possuiPrioridade || possuiSincronismo) {
                query.append(" and ");
            }
    
            query.append(" os.sincronizado IS NULL OR os.sincronizado  <> 1 ");

        }
        */
        //query.append(" order by os.num_os, os.id_motivo_troca");
        query.append(" order by os.data_cadastro, os.id_motivo_troca");

        Cursor cursor = null;
        try {
            cursor = getDatabaseHelper().getWritableDatabase().rawQuery(query.toString(), null);

            if (cursor.moveToFirst()) {
                do {
                    int indiceOsId = cursor.getColumnIndex("id");
                    String osId = cursor.getString(indiceOsId);

                    OrdemServico os = buscarPorId(osId);

                    listOs.add(os);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return listOs;
    }

}
