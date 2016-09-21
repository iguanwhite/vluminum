package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.PontoServicoDao;
import br.com.velp.vluminum.entity.PontoServico;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.PontoServicoService;
import br.com.velp.vluminum.util.VLuminumUtil;


public class PontoServicoServiceImpl implements PontoServicoService, Serializable {

    private PontoServicoDao dao;

    public PontoServicoServiceImpl(Context ctx) {
        dao = PontoServicoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(PontoServico pontoServico) throws RegraNegocioException {
        if (pontoServico.getId() == null) {
            pontoServico.setId(UUID.randomUUID().toString());
            pontoServico.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            pontoServico.setDataCadastro(new Date());
        } else {
            pontoServico.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            pontoServico.setDataAlteracao(new Date());
        }

        return dao.salvarOuAtualizar(pontoServico);
    }

    @Override
    public boolean deletar(PontoServico os) throws RegraNegocioException {
        return dao.deletar(os);
    }

    @Override
    public PontoServico buscarPorId(Integer id) {
        return (PontoServico) dao.buscarPorId(id);
    }

    @Override
    public List<PontoServico> listarPorPonto(String idPonto) {
        return dao.listarPorPonto(idPonto);
    }

    @Override
    public void deletarPorPonto(String idPonto) {
        dao.deletarPorPonto(idPonto);
    }

    @Override
    public boolean deletarPorPontoEServico(String idPonto, Integer idServico) {
        return dao.deletarPorPontoEServico(idPonto, idServico);
    }

    @Override
    public List<PontoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public long count() {
        return dao.count();
    }


}
