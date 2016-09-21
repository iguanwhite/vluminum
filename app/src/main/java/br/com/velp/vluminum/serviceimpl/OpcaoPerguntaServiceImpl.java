package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.OpcaoPerguntaDao;
import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Pergunta;
import br.com.velp.vluminum.service.OpcaoPerguntaService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class OpcaoPerguntaServiceImpl implements OpcaoPerguntaService, Serializable {

    private OpcaoPerguntaDao dao;

    public OpcaoPerguntaServiceImpl(Context ctx) {
        dao = OpcaoPerguntaDao.getInstance(ctx);
    }

    @Override
    public List<OpcaoPergunta> buscarPorPergunta(Pergunta pergunta) {
        return dao.buscarPorPergunta(pergunta);
    }

    @Override
    public boolean salvarOuAtualizar(OpcaoPergunta opcao) {
        opcao.setDataCadastro(new Date());
        opcao.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());

        return dao.salvarOuAtualizar(opcao);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_opcao_pergunta");
        return true;
    }

    public OpcaoPergunta buscarPorId(Integer id) {
        return dao.buscarPorId(id);
    }
}
