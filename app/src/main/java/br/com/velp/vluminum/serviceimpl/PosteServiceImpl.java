package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.PosteDao;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.PosteService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class PosteServiceImpl implements PosteService, Serializable {

    private PosteDao dao;

    public PosteServiceImpl(Context ctx) {
        dao = PosteDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Poste poste) throws RegraNegocioException {
        if (poste.getId() == null || poste.getId().trim().equals("")) { // Novo poste
            poste.setId(UUID.randomUUID().toString());
            poste.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            poste.setDataCadastro(new Date());
        } else { // Atualização de poste
            poste.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            poste.setDataAlteracao(new Date());
        }

        validarCamposObrigatorios(poste);

        return dao.salvarOuAtualizar(poste);
    }

    private void validarCamposObrigatorios(Poste poste) throws RegraNegocioException {
        StringBuilder msg = new StringBuilder("");

        if (poste.getDescricao() == null || poste.getDescricao().trim().equals("")) {
            msg.append("Descrição\n");
        }

        if (poste.getSigla() == null || poste.getSigla().trim().equals("")) {
            msg.append("Sigla\n");
        }

        if (poste.getTipoPoste() == null) {
            msg.append("Tipo de Poste\n");
        }

        if (!msg.toString().trim().equals("")) {
            throw new RegraNegocioException(msg.toString());
        }
    }

    @Override
    public boolean deletar(Poste poste) throws RegraNegocioException {
        return dao.deletar(poste);
    }

    @Override
    public Poste buscarPorId(String id) {
        return (Poste) dao.buscarPorId(id);
    }

    @Override
    public List<Poste> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_poste");
        return false;
    }
}
