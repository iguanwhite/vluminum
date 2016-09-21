package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.AlimentadorDao;
import br.com.velp.vluminum.entity.Alimentador;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.AlimentadorService;


public class AlimentadorServiceImpl implements AlimentadorService, Serializable {

    private AlimentadorDao dao;

    public AlimentadorServiceImpl(Context ctx) {
        dao = AlimentadorDao.getInstance(ctx);
    }

    @Override
    public Alimentador buscarPorId(Integer id) {
        return (Alimentador) dao.buscarPorId(id);
    }

    @Override
    public List<Alimentador> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public boolean salvarOuAtualizar(Alimentador alimentador) throws RegraNegocioException {
        return dao.salvarOuAtualizar(alimentador);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_alimentador");
        return true;
    }
}
