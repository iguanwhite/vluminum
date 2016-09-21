package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.PerguntaDao;
import br.com.velp.vluminum.entity.Pergunta;
import br.com.velp.vluminum.service.PerguntaService;

public class PerguntaServiceImpl implements PerguntaService, Serializable {

    private PerguntaDao dao;

    public PerguntaServiceImpl(Context ctx) {
        dao = PerguntaDao.getInstance(ctx);
    }

    @Override
    public List<Pergunta> listarAtivas() {
        return dao.listarAtivas();
    }

    @Override
    public boolean salvarOuAtualizar(Pergunta p) {
        dao.salvarOuAtualizar(p);
        return true;
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_pergunta");
        return true;
    }
}
