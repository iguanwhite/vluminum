package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.exception.RegraNegocioException;

public interface UsuarioService extends InterfaceGeneric {

    public void salvarOuAtualizar(Usuario usuario) throws RegraNegocioException;

    public void deletar(Usuario usuario) throws RegraNegocioException;

    public Usuario buscarPorId(Integer id);

    public Usuario buscarPorLogin(String login);

    public Usuario validarUsuario(String usuario, String senha);

    public List<Usuario> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

}
