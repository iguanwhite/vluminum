package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.UsuarioDao;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService, Serializable {

    private UsuarioDao dao;

    public UsuarioServiceImpl(Context ctx) {
        dao = UsuarioDao.getInstance(ctx);
    }

    @Override
    public void salvarOuAtualizar(Usuario usuario) throws RegraNegocioException {
        usuario.setDataCadastro(new Date());
        usuario.setUsuarioCadastro(1);
        usuario.setUsuarioAlteracao(1);
        usuario.setDataAlteracao(new Date());
        dao.salvarOuAtualizar(usuario);
    }

    @Override
    public void deletar(Usuario usuario) throws RegraNegocioException {
        dao.deletar(usuario);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return (Usuario) dao.buscarPorId(id);
    }

    @Override
    public Usuario buscarPorLogin(String login) {
        return dao.buscarPorLogin(login);
    }

    @Override
    public Usuario validarUsuario(String usuario, String senha) {
        return dao.validarUsuario(usuario, senha);
    }

    @Override
    public List<Usuario> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_usuario");
        return false;
    }

}
