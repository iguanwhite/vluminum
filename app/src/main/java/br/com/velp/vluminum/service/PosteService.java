package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface PosteService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(Poste poste) throws RegraNegocioException;

    public boolean deletar(Poste poste) throws RegraNegocioException;

    public Poste buscarPorId(String id);

    public List<Poste> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();
}
