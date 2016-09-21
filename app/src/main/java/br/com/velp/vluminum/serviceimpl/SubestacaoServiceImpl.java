package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.SubestacaoDao;
import br.com.velp.vluminum.entity.Subestacao;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.SubestacaoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class SubestacaoServiceImpl implements SubestacaoService, Serializable {

    private SubestacaoDao dao;

    public SubestacaoServiceImpl(Context ctx) {
        dao = SubestacaoDao.getInstance(ctx);
    }

    @Override
    public Subestacao buscarPorId(Integer id) {
        return (Subestacao) dao.buscarPorId(id);
    }

    @Override
    public List<Subestacao> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public void salvarOuAtualizar(Subestacao subestacao) throws RegraNegocioException {
        subestacao.setDataAlteracao(new Date());
        subestacao.setDataCadastro(new Date());
        subestacao.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
        subestacao.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
        dao.salvarOuAtualizar(subestacao);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_subestacao");
        return false;
    }

}
