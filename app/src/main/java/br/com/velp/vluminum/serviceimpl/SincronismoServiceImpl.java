package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

import br.com.velp.vluminum.dao.SincronismoDao;
import br.com.velp.vluminum.entity.Sincronismo;
import br.com.velp.vluminum.service.SincronismoService;

public class SincronismoServiceImpl implements SincronismoService, Serializable {

    private SincronismoDao dao;

    public SincronismoServiceImpl(Context ctx) {
        dao = SincronismoDao.getInstance(ctx);
    }


    @Override
    public boolean salvarOuAtualizar(Sincronismo sincronismo) {
        try {
            sincronismo.setDataAlteracao(new Date());
            sincronismo.setDataCadastro(new Date());
            sincronismo.setUsuarioAlteracao(1);
            sincronismo.setUsuarioCadastro(1);
            return dao.salvarOuAtualizar(sincronismo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletar(Sincronismo sincronismo) {
        return false;
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_sincronismo");
        return true;
    }


    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public Date buscarDataUltimoSincronismo(String tabela) {
        return dao.buscarDataUltimoSincronismo(tabela);
    }

    @Override
    public String buscarDataFormatadaUltimoSincronismo(String tabela) {
        return dao.buscarDataFormatadaUltimoSincronismo(tabela);
    }

    @Override
    public void atualizarDataSincronismo(String tabela, String data) {
        dao.atualizarDataSincronismo(tabela, data);
    }

    @Override
    public void deletarTabelaSinc(String tabela) {
        dao.deletarTabelaSinc(tabela);
    }


}
