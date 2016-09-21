package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.OrdemServicoServicoDao;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.OrdemServicoServicoService;
import br.com.velp.vluminum.util.VLuminumUtil;


public class OrdemServicoServicoServiceImpl implements OrdemServicoServicoService, Serializable {

    private OrdemServicoServicoDao dao;

    public OrdemServicoServicoServiceImpl(Context ctx) {
        dao = OrdemServicoServicoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(OrdemServicoServico osPonto) throws RegraNegocioException {
        if (osPonto.getId() == null) {
            osPonto.setId(UUID.randomUUID().toString());
            osPonto.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            osPonto.setDataCadastro(new Date());
        } else {
            osPonto.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            osPonto.setDataAlteracao(new Date());
        }


        return dao.salvarOuAtualizar(osPonto);
    }


    @Override
    public boolean deletar(OrdemServicoServico os) throws RegraNegocioException {
        return dao.deletar(os);
    }


    @Override
    public OrdemServicoServico buscarPorId(Integer id) {
        return (OrdemServicoServico) dao.buscarPorId(id);
    }

    @Override
    public List<OrdemServicoServico> listarPorPonto(String pontoId) {
        return dao.listarPorPonto(pontoId);
    }

    @Override
    public List<OrdemServicoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<OrdemServicoServico> listarNaoSincronizados() {
        return dao.listarNaoSincronizado();
    }

    @Override
    public List<OrdemServicoServico> listarPorOrdemServico(String osId) {
        return dao.listarPorOrdemServico(osId);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(OrdemServicoServico.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deletarTudo() {
        return false;
    }
}
