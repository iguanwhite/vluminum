package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Grupo;


public interface GrupoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(Grupo grupo);

    public boolean deletar(Grupo grupo);

    public Grupo buscarPorGrupo(Integer id);

    public List<Grupo> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<Grupo> listarCompletos();

    public long count();
}
