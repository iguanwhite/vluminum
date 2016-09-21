package br.com.velp.vluminum.service;

import java.util.Date;

import br.com.velp.vluminum.entity.Sincronismo;

public interface SincronismoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(Sincronismo sincronismo);

    public boolean deletar(Sincronismo sincronismo);

    public long count();

    public Date buscarDataUltimoSincronismo(String tabela);

    public String buscarDataFormatadaUltimoSincronismo(String tabela);

    public void atualizarDataSincronismo(String tabela, String data);

    public void deletarTabelaSinc(String tabela);

}
