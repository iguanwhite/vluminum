package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.TipoPosteDao;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.entity.TipoPoste;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.TipoPosteService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class TipoPosteServiceImpl implements TipoPosteService, Serializable {

    private TipoPosteDao dao;

    public TipoPosteServiceImpl(Context ctx) {
        dao = TipoPosteDao.getInstance(ctx);
    }


    @Override
    public boolean salvarOuAtualizar(TipoPoste poste) throws RegraNegocioException {
        poste.setDataAlteracao(new Date());
        poste.setDataCadastro(new Date());
        poste.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
        poste.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
        return false;
    }

    @Override
    public boolean deletar(TipoPoste poste) throws RegraNegocioException {
        return false;
    }

    @Override
    public Poste buscarPorId(Integer id) {
        return null;
    }

    @Override
    public List<TipoPoste> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_tipo_poste");
        return true;
    }


    @Override
    public long count() {
        return dao.count();
    }


}
