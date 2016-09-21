package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Pergunta;

public interface PerguntaService extends InterfaceGeneric {

    public List<Pergunta> listarAtivas();

    public boolean salvarOuAtualizar(Pergunta p);

}
