package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.MaterialDao;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.MaterialService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class MaterialServiceImpl implements MaterialService, Serializable {

    private MaterialDao dao;

    public MaterialServiceImpl(Context ctx) {
        dao = MaterialDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Material material) throws RegraNegocioException {
        if (material.getIdMaterial() == null) {
            material.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            material.setDataCadastro(new Date());
        } else {
            material.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            material.setDataAlteracao(new Date());
            material.setSincronizado(0);
        }

        validarCamposObrigatorios(material);

        return dao.salvarOuAtualizar(material);
    }

    private void validarCamposObrigatorios(Material m) throws RegraNegocioException {
        StringBuilder msg = new StringBuilder("");
        //TODO código comentado porque na web não existe este campo.
        /*
        if (m.getNumSerie() == null || m.getNumSerie().trim().equals("")) { //TODO colocar validacao de acordo com estrutura a ser definida.
            msg.append("Número de Série\n");
        }
        */
        if (!msg.toString().trim().equals("")) {
            throw new RegraNegocioException(msg.toString());
        }

    }


    @Override
    public boolean deletar(Material m) throws RegraNegocioException {
        return dao.deletar(m);
    }

    @Override
    public Material buscarPorId(Integer id) {
        return (Material) dao.buscarPorId(id);
    }

    @Override
    public List<Material> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<Material> listarPorTipo(Integer idTipo) {
        return dao.listarPorTipo(idTipo);
    }

    @Override
    public List<Material> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    @Override
    public long count() {
        return dao.count();
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_material");
        return true;
    }
}
