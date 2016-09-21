package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.exception.RegraNegocioException;


public interface OrdemServicoServicoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(OrdemServicoServico os) throws RegraNegocioException;

    public boolean deletar(OrdemServicoServico os) throws RegraNegocioException;

    public OrdemServicoServico buscarPorId(Integer id);

    public List<OrdemServicoServico> listarPorPonto(String pontoId);

    public List<OrdemServicoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<OrdemServicoServico> listarNaoSincronizados();

    public List<OrdemServicoServico> listarPorOrdemServico(String osId);

    public long count();

    public void deletarRegistrosNaoSinc();
}
