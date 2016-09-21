package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.GrupoServico;


public interface GrupoServicoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(GrupoServico grupo);

    public boolean deletar(GrupoServico grupo);

    public GrupoServico buscarPorGrupo(Integer id);

    public List<GrupoServico> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();
}
