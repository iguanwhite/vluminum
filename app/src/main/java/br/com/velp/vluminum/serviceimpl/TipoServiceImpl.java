package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.TipoDao;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.service.TipoService;

public class TipoServiceImpl implements TipoService, Serializable {

    private TipoDao dao;

    public TipoServiceImpl(Context ctx) {
        dao = TipoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Tipo tipo) {

        return dao.salvarOuAtualizar(tipo);
    }

    @Override
    public boolean deletar(Tipo tipo) {
        return dao.deletar(tipo);
    }

    @Override
    public Tipo buscarPorTipo(Integer id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<Tipo> listarPorGrupo(Integer idGrupo) {
        return dao.listarPorGrupo(idGrupo);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_tipo");
        return true;
    }
}
