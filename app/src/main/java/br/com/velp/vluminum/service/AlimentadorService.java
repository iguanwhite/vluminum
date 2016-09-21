package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Alimentador;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface AlimentadorService extends InterfaceGeneric {
    public Alimentador buscarPorId(Integer id);

    public boolean salvarOuAtualizar(Alimentador alimentador) throws RegraNegocioException;

    public List<Alimentador> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);
}
