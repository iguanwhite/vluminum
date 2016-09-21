package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.MunicipioCliente;

public interface MunicipioClienteService extends InterfaceGeneric {

    public List<MunicipioCliente> listar();

    public boolean salvarOuAtualizar(MunicipioCliente municipioCliente);

    public Integer buscaClientePorIdMunicipio(Integer idMunicipio);
}
