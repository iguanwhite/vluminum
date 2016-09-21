package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.MaterialIdentificadoDao;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.MaterialIdentificadoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class MaterialIdentificadoServiceImpl implements MaterialIdentificadoService, Serializable {

    private MaterialIdentificadoDao dao;

    public MaterialIdentificadoServiceImpl(Context ctx) {
        dao = MaterialIdentificadoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(MaterialIdentificado m, boolean os, Context ctx, int idGrupo) throws RegraNegocioException {
        if (m.getIdMaterialIdentificado() == null) {
            m.setIdMaterialIdentificado(UUID.randomUUID().toString());
            m.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            m.setDataCadastro(new Date());
        } else {
            m.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            m.setDataAlteracao(new Date());
        }

        if (m.getClassificacao() != 0) {
            validarCamposObrigatorios(m, os, ctx, idGrupo);
        }

        return dao.salvarOuAtualizar(m);
    }

    @Override
    public boolean salvarOuAtualizar(MaterialIdentificado m, boolean os) throws RegraNegocioException {
        if (m.getIdMaterialIdentificado() == null) {
            m.setIdMaterialIdentificado(UUID.randomUUID().toString());
            m.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            m.setDataCadastro(new Date());
        } else {
            m.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            m.setDataAlteracao(new Date());
        }

        if (m.getClassificacao() != 0) {
            validarCamposObrigatorios(m, false, null, 0);
        }

        return dao.salvarOuAtualizar(m);
    }


    private void validarCamposObrigatorios(MaterialIdentificado m, boolean os, Context ctx, int idGrupo) throws RegraNegocioException {
        StringBuilder msg = new StringBuilder("");

        if (m.getMaterial() == null) {
            msg.append("Material\n");
        }
/* Remoção temporária de validação.
        if (os) {// Apenas valida se for cadastro vindo da OS (Numero de Série).

            if (m.getNumSerie() != null && m.getNumSerie().length() == 0) {
                msg.append("Nº de Série\n");
            }

            if (m.getNumSerie().length() > 0 && m.getNumSerie().length() < 5) {
                msg.append("Nº de Série deve possuir no mínimo 5 caracteres.\n");
            }

            MaterialIdentificado m2 = buscarPorNumSerieEGrupo(m.getNumSerie(), idGrupo, ctx);
            if (m2 != null) {
                msg.append("Já existe um material neste grupo com este Nº de Série.\n");
            }
        }
        if (!msg.toString().trim().equals("")) {
            throw new RegraNegocioException(msg.toString());
        }
        */

    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(MaterialIdentificado.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public MaterialIdentificado buscarPorNumSerie(String numSerie) {
        return (MaterialIdentificado) dao.buscarPorNumSerie(numSerie);
    }

    @Override
    public MaterialIdentificado buscarPorNumSerieEGrupo(String numSerie, int idGrupo, Context ctx) {
        return (MaterialIdentificado) dao.buscarPorNumSerieEGrupo(numSerie, idGrupo, ctx);
    }


    @Override
    public boolean deletar(MaterialIdentificado m) throws RegraNegocioException {
        return dao.deletar(m);
    }

    @Override
    public MaterialIdentificado buscarPorId(String id) {
        return (MaterialIdentificado) dao.buscarPorId(id);
    }

    @Override
    public List<MaterialIdentificado> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    public List<MaterialIdentificado> listarPorPonto(String idPonto) {
        return dao.listarPorPonto(idPonto);
    }

    public List<MaterialIdentificado> listarPorOs(String idOs) {
        return dao.listarPorOs(idOs);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public List<MaterialIdentificado> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_material_identificado");
        return true;
    }

}
