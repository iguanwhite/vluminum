package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.FotoDao;
import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.FotoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class FotoServiceImpl implements FotoService, Serializable {

    private FotoDao dao;

    public FotoServiceImpl(Context ctx) {
        dao = FotoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Foto foto) throws CampoObrigatorioException {
        if (foto.getDataCadastro() == null || foto.getId().trim().equals("")) { // Nova foto
            foto.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            foto.setDataCadastro(new Date());
        } else { // Atualização de foto
            foto.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            foto.setDataAlteracao(new Date());
        }

        validarCamposObrigatorios(foto);

        return dao.salvarOuAtualizar(foto);
    }

    private void validarCamposObrigatorios(Foto foto) throws CampoObrigatorioException {
        StringBuilder msg = new StringBuilder("");

        if (!msg.toString().trim().equals("")) {
            throw new CampoObrigatorioException(msg.toString());
        }
    }

    @Override
    public boolean deletar(Foto foto) throws RegraNegocioException {
        return dao.deletar(foto);
    }

    @Override
    public Foto buscarPorId(Integer id) {
        return (Foto) dao.buscarPorId(id);
    }

    @Override
    public List<Foto> buscarPorPonto(Ponto ponto) {
        return dao.buscarPorPonto(ponto);
    }

    @Override
    public List<Foto> buscarPorOrdemServico(OrdemServico ordemServico) {
        return dao.buscarPorOrdemServico(ordemServico);
    }

    @Override
    public List<Foto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    @Override
    public long count() {
        return dao.count();
    }

}
