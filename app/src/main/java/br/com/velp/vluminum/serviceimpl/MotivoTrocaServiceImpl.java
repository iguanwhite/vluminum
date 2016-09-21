package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.MotivoTrocaDao;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.service.MotivoTrocaService;

public class MotivoTrocaServiceImpl implements MotivoTrocaService, Serializable {

    private MotivoTrocaDao dao;

    public MotivoTrocaServiceImpl(Context ctx) {
        dao = MotivoTrocaDao.getInstance(ctx);
    }

    @Override
    public MotivoTroca buscarPorId(Integer id) {
        return dao.buscarPorId(id);
    }

    @Override
    public List<MotivoTroca> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public boolean salvarOuAtualizar(MotivoTroca motivoTroca) {
        return dao.salvarOuAtualizar(motivoTroca);
    }

    @Override
    public boolean deletarTudo() {
        return dao.deletarTodosRegistros("tb_motivo_troca");
    }
}
