package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.exception.RegraNegocioException;


public interface OrdemServicoPontoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(OrdemServicoPonto os) throws RegraNegocioException;

    public boolean deletar(OrdemServicoPonto os) throws RegraNegocioException;

    public OrdemServicoPonto buscarPorId(Integer id);

    public List<OrdemServicoPonto> listarPorOs(String numOs);

    public List<OrdemServicoPonto> buscarPorPonto(String pontoId);

    public OrdemServicoPonto buscarPorOsEPonto(String osId, String pontoId);

    public void deletarPorNumOs(String numOs);

    public boolean deletarPorNumOsEPonto(String idOs, String idPonto);

    public List<OrdemServicoPonto> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<OrdemServicoPonto> listarNaoSincronizados();

    public long count();

    public Long quantidadePontosDaOsNaoFinalizados(String idOs);

    public Long quantidadePontosDaOs(String idOs);

    public void deletarRegistrosNaoSinc();

}
