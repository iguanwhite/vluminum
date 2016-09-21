package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.SituacaoDao;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.SituacaoService;


public class SituacaoServiceImpl implements SituacaoService, Serializable {

    private SituacaoDao dao;

    public SituacaoServiceImpl(Context ctx) {
        dao = SituacaoDao.getInstance(ctx);
    }

    @Override
    public Situacao buscarPorId(Integer id) {
        return (Situacao) dao.buscarPorId(id);
    }

    @Override
    public List<Situacao> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public boolean salvarOuAtualizar(Situacao situacao) throws RegraNegocioException {
        return dao.salvarOuAtualizar(situacao);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_situacao");
        return true;
    }
}
