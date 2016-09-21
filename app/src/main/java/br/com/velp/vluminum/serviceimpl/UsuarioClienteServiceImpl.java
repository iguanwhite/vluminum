package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.UsuarioClienteDao;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.entity.UsuarioCliente;
import br.com.velp.vluminum.service.UsuarioClienteService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class UsuarioClienteServiceImpl implements UsuarioClienteService, Serializable {

    private UsuarioClienteDao dao;

    public UsuarioClienteServiceImpl(Context ctx) {
        dao = UsuarioClienteDao.getInstance(ctx);
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_usuario_cliente");
        return false;
    }

    @Override
    public void salvarOuAtualizar(UsuarioCliente usuarioCliente) {
        usuarioCliente.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
        usuarioCliente.setDataCadastro(new Date());
        dao.salvarOuAtualizar(usuarioCliente);
    }

    @Override
    public void deletar(UsuarioCliente usuarioCliente) {

    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return null;
    }

    @Override
    public List<UsuarioCliente> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<UsuarioCliente> listarPorUsuario(Integer idUsuario) {
        return dao.listarPorUsuario(idUsuario);
    }
}
