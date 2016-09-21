package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.GrupoServicoDao;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.service.GrupoServicoService;

public class GrupoServicoServiceImpl implements GrupoServicoService, Serializable {

    private GrupoServicoDao dao;

    public GrupoServicoServiceImpl(Context ctx) {
        dao = GrupoServicoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(GrupoServico grupo) {
        return dao.salvarOuAtualizar(grupo);
    }

    @Override
    public boolean deletar(GrupoServico grupo) {
        dao.deletar(grupo);
        return true;
    }

    @Override
    public GrupoServico buscarPorGrupo(Integer id) {
        return null;
    }

    @Override
    public List<GrupoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }


    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_grupo_servico");
        return true;
    }
}
