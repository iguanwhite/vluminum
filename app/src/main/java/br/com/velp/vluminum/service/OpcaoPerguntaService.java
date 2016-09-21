package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Pergunta;

public interface OpcaoPerguntaService extends InterfaceGeneric {

    public List<OpcaoPergunta> buscarPorPergunta(Pergunta pergunta);

    public OpcaoPergunta buscarPorId(Integer id);

    public boolean salvarOuAtualizar(OpcaoPergunta opcao);

}
