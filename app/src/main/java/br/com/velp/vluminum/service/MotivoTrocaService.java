package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.MotivoTroca;

public interface MotivoTrocaService extends InterfaceGeneric {

    public MotivoTroca buscarPorId(Integer id);

    public List<MotivoTroca> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public boolean salvarOuAtualizar(MotivoTroca motivoTroca);

}
