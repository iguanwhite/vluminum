package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.GrupoDao;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.service.GrupoService;

public class GrupoServiceImpl implements GrupoService, Serializable {

    private GrupoDao dao;

    public GrupoServiceImpl(Context ctx) {
        dao = GrupoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Grupo grupo) {
        dao.salvarOuAtualizar(grupo);
        return true;
    }

    @Override
    public boolean deletar(Grupo grupo) {
        dao.deletar(grupo);
        return true;
    }

    @Override
    public Grupo buscarPorGrupo(Integer id) {
        Grupo grupo = dao.buscarPorId(id);
        return grupo;
    }

    @Override
    public List<Grupo> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<Grupo> listarCompletos() {
        return dao.listarCompletos();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_grupo");
        return true;
    }
}
