package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.entity.TipoPoste;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface TipoPosteService extends InterfaceGeneric {

    public boolean salvarOuAtualizar(TipoPoste poste) throws RegraNegocioException;

    public boolean deletar(TipoPoste poste) throws RegraNegocioException;

    public Poste buscarPorId(Integer id);

    public List<TipoPoste> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();

}
