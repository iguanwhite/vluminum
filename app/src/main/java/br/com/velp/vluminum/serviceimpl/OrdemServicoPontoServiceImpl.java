package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.OrdemServicoPontoDao;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.OrdemServicoPontoService;
import br.com.velp.vluminum.util.VLuminumUtil;


public class OrdemServicoPontoServiceImpl implements OrdemServicoPontoService, Serializable {

    private OrdemServicoPontoDao dao;

    public OrdemServicoPontoServiceImpl(Context ctx) {
        dao = OrdemServicoPontoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(OrdemServicoPonto osPonto) throws RegraNegocioException {
        if (osPonto.getId() == null) {
            osPonto.setId(UUID.randomUUID().toString());
            osPonto.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            osPonto.setDataCadastro(new Date());
            osPonto.setDataAlteracao(new Date());
        } else {
            osPonto.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            osPonto.setDataAlteracao(new Date());
        }

        return dao.salvarOuAtualizar(osPonto);
    }

    @Override
    public boolean deletar(OrdemServicoPonto os) throws RegraNegocioException {
        return dao.deletar(os);
    }

    @Override
    public OrdemServicoPonto buscarPorId(Integer id) {
        return dao.buscarPorId(id);
    }

    @Override
    public OrdemServicoPonto buscarPorOsEPonto(String osId, String pontoId)  {
        return dao.buscarPorOsEPonto(osId, pontoId);
    }

    @Override
    public List<OrdemServicoPonto> listarPorOs(String idOS) {
        return dao.listarPorOs(idOS);
    }

    @Override
    public List<OrdemServicoPonto> buscarPorPonto(String pontoId) {
        return dao.buscarPorPonto(pontoId);
    }

    @Override
    public void deletarPorNumOs(String numOs) {
        dao.deletarPorNumOs(numOs);
    }

    @Override
    public boolean deletarPorNumOsEPonto(String idOs, String idPonto) {
        return dao.deletarPorNumOsEPonto(idOs, idPonto);
    }

    @Override
    public List<OrdemServicoPonto> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<OrdemServicoPonto> listarNaoSincronizados() {
        return dao.listarNaoSincronizados();
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public Long quantidadePontosDaOsNaoFinalizados(String idOs) {
        return dao.quantidadePontosDaOsNaoFinalizados(idOs);
    }

    @Override
    public Long quantidadePontosDaOs(String idOs) {
        return dao.quantidadePontosDaOs(idOs);
    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(OrdemServicoPonto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean deletarTudo() {
        return dao.deletarTodosRegistros("tb_ordem_ponto");
    }
}
