package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.RespostaDao;
import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.service.RespostaService;

public class RespostaServiceImpl implements RespostaService, Serializable {

    private RespostaDao dao;

    public RespostaServiceImpl(Context ctx) {
        dao = RespostaDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Resposta resposta) throws RegraNegocioException {
        return dao.salvarOuAtualizar(resposta);
    }

    @Override
    public boolean deletar(Resposta resposta) throws RegraNegocioException {
        return false;
    }

    @Override
    public Resposta buscarPorId(Integer id) {
        return null;
    }

    @Override
    public List<Resposta> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    @Override
    public boolean buscaRespostaMultiplaEscolha(Integer idPergunta, String idPonto, Integer resp) {
        return dao.buscaRespostaMultiplaEscolha(idPergunta, idPonto, resp);
    }

    @Override
    public OpcaoPergunta buscaRespostaSpinner(Integer idPergunta, String idPonto) {
        return dao.buscaRespostaSpinner(idPergunta, idPonto);
    }

    @Override
    public String buscaRespostaText(Integer idPergunta, String idPonto) {
        return dao.buscaRespostaTexto(idPergunta, idPonto);
    }

    @Override
    public boolean apagarRespostaPonto(String idPonto) {
        return dao.apagarRespostaPonto(idPonto);
    }


}
