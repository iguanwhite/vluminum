package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Tipo;


public interface TipoService extends InterfaceGeneric {

    public boolean salvarOuAtualizar(Tipo tipo);

    public boolean deletar(Tipo tipo);

    public Tipo buscarPorTipo(Integer id);

    public List<Tipo> listarPorGrupo(Integer idGrupo);

    public long count();

}
