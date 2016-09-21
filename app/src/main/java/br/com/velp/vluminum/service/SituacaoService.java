package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface SituacaoService extends InterfaceGeneric {

    public Situacao buscarPorId(Integer id);

    public boolean salvarOuAtualizar(Situacao situacao) throws RegraNegocioException;

    public List<Situacao> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

}
