package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.velp.vluminum.dao.ServicoDao;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.parametroconsulta.ServicoParametroConsulta;
import br.com.velp.vluminum.service.ServicoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class ServicoServiceImpl implements ServicoService, Serializable {

    private ServicoDao dao;

    public ServicoServiceImpl(Context ctx) {
        dao = ServicoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(Servico servico) {
        if (servico.getId() == null || servico.getDataCadastro() == null) {
            servico.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            servico.setDataCadastro(new Date());
        } else {
            servico.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            servico.setDataAlteracao(new Date());
        }

        return dao.salvarOuAtualizar(servico);
    }

    @Override
    public List<Servico> listarPorGrupo(Integer idGrupo) {
        return dao.listarPorGrupo(idGrupo);
    }


    @Override
    public List<Servico> buscarPorParametroConsulta(ServicoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    public Servico buscarPorId(Integer id) {
        return dao.buscarPorId(id);
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_servico");
        return false;
    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(Servico.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
