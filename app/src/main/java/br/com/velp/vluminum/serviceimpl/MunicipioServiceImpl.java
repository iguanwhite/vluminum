package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.MunicipioDao;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.enumerator.Estado;
import br.com.velp.vluminum.service.MunicipioService;

public class MunicipioServiceImpl implements MunicipioService, Serializable {

    private MunicipioDao dao;

    public MunicipioServiceImpl(Context ctx) {
        dao = MunicipioDao.getInstance(ctx);
    }

    @Override
    public Municipio buscarPorId(Integer id) {
        return (Municipio) dao.buscarPorId(id);
    }

    @Override
    public List<Municipio> listarPorEstado(Estado estado) {
        return dao.listarPorEstado(estado);
    }

    @Override
    public List<Municipio> listarPorEstado(String uf) {
        return dao.listarPorEstado(uf);
    }

    @Override
    public List<String> listarEstados() {
        return dao.listarEstados();
    }

    @Override
    public boolean salvarOuAtualizar(Municipio municipio) {
        municipio.setUsuarioCadastro(1);
        municipio.setDataCadastro(new Date());
        return dao.salvarOuAtualizar(municipio);
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_municipio");
        return true;
    }

    @Override
    public List<Municipio> listarComOSAbertas(){
      return dao.listarComOSAbertas();
    }

    @Override
    public List<Municipio> listarPorCliente() {
        return dao.listarPorCliente();
    }



}
