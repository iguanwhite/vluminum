package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.entity.UsuarioCliente;

public interface UsuarioClienteService extends InterfaceGeneric {

    public void salvarOuAtualizar(UsuarioCliente usuarioCliente);

    public void deletar(UsuarioCliente usuarioCliente);

    public Usuario buscarPorId(Integer id);

    public List<UsuarioCliente> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<UsuarioCliente> listarPorUsuario(Integer idUsuario);

}
