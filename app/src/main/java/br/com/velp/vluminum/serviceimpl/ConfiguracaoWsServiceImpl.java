package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import br.com.velp.vluminum.dao.ConfiguracaoWebServiceDao;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.service.ConfiguracaoWsService;


public class ConfiguracaoWsServiceImpl implements ConfiguracaoWsService, Serializable {

    private ConfiguracaoWebServiceDao dao;

    public ConfiguracaoWsServiceImpl(Context ctx) {
        dao = ConfiguracaoWebServiceDao.getInstance(ctx);
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_configuracao_ws");
        return true;
    }

    @Override
    public boolean salvarOuAtualizar(ConfiguracaoWebService configuracaoWebService) {
        configuracaoWebService.setUsuarioCadastro(1);
        return dao.salvarOuAtualizar(configuracaoWebService);
    }

    @Override
    public List<ConfiguracaoWebService> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }
}
