package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Subestacao;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface SubestacaoService extends InterfaceGeneric {

    public Subestacao buscarPorId(Integer id);

    public List<Subestacao> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public void salvarOuAtualizar(Subestacao subestacao) throws RegraNegocioException;

}
