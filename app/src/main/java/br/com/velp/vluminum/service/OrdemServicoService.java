package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;

public interface OrdemServicoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(OrdemServico os) throws CampoObrigatorioException, RegraNegocioException;

    public boolean deletar(OrdemServico os) throws RegraNegocioException;

    public OrdemServico buscarPorId(String id);

    public List<OrdemServico> buscarPorParametroConsulta(OsParametroConsulta parametroConsulta);

    public List<OrdemServico> buscarPorUsuarioAtribuicao(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<OrdemServico> buscarPorPontoEUsuarioAtribuicao(String idPonto, String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();

    public void deletarRegistrosNaoSinc();


}
