package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.enumerator.Estado;

public interface MunicipioService extends InterfaceGeneric {
    public Municipio buscarPorId(Integer id);

    public List<Municipio> listarPorEstado(Estado estado);

    public List<Municipio> listarPorEstado(String uf);

    public List<String> listarEstados();

    public boolean salvarOuAtualizar(Municipio municipio);

    public List<Municipio> listarComOSAbertas();

    public List<Municipio> listarPorCliente();

}
