package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.ConfiguracaoWebService;

public interface ConfiguracaoWsService extends InterfaceGeneric {

    public boolean salvarOuAtualizar(ConfiguracaoWebService ConfiguracaoWebService);

    public List<ConfiguracaoWebService> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

}
