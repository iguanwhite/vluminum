package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;

public interface ServicoService extends InterfaceGeneric {

    public boolean salvarOuAtualizar(Servico servico);

    public Servico buscarPorId(Integer id);


    public List<Servico> listarPorGrupo(Integer idGrupo);

    public List<Servico> buscarPorParametroConsulta(ServicoParametroConsulta parametroConsulta);

    public void deletarRegistrosNaoSinc();
}
