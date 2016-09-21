package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.PontoServico;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoResultadoConsulta;


public interface PontoServicoService {
    public boolean salvarOuAtualizar(PontoServico os) throws RegraNegocioException;

    public boolean deletar(PontoServico os) throws RegraNegocioException;

    public PontoServico buscarPorId(Integer id);

    public List<PontoServico> listarPorPonto(String idPonto);

    public void deletarPorPonto(String idPonto);

    public boolean deletarPorPontoEServico(String idPonto, Integer idServico);

    public List<PontoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();


}
