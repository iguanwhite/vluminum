package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.MunicipioClienteDao;
import br.com.velp.vluminum.entity.MunicipioCliente;
import br.com.velp.vluminum.service.MunicipioClienteService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class MunicipioClienteServiceImpl implements MunicipioClienteService, Serializable {

    private MunicipioClienteDao dao;

    public MunicipioClienteServiceImpl(Context ctx) {
        dao = MunicipioClienteDao.getInstance(ctx);
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_municipio_cliente");
        return true;
    }

    @Override
    public List<MunicipioCliente> listar() {
        return dao.listar("id_municipio_cliente", false);
    }

    @Override
    public boolean salvarOuAtualizar(MunicipioCliente municipioCliente) {
        municipioCliente.setDataCadastro(new Date());
        municipioCliente.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
        municipioCliente.setDataAlteracao(new Date());
        return dao.salvarOuAtualizar(municipioCliente);
    }

    @Override
    public Integer buscaClientePorIdMunicipio(Integer idMunicipio) {
        return dao.buscaClientePorIdMunicipio(idMunicipio);
    }

}
