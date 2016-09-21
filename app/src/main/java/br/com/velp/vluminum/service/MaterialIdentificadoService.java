package br.com.velp.vluminum.service;

import android.content.Context;

import java.util.List;

import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public interface MaterialIdentificadoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(MaterialIdentificado m, boolean os, Context ctx, int idGrupo) throws RegraNegocioException;

    public boolean salvarOuAtualizar(MaterialIdentificado m, boolean os) throws RegraNegocioException;

    public boolean deletar(MaterialIdentificado m) throws RegraNegocioException;

    public MaterialIdentificado buscarPorId(String id);

    public MaterialIdentificado buscarPorNumSerie(String numSerie);

    public MaterialIdentificado buscarPorNumSerieEGrupo(String numSerie, int idGrupo, Context ctx);

    public List<MaterialIdentificado> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<MaterialIdentificado> listarPorPonto(String idPonto);

    public List<MaterialIdentificado> listarPorOs(String idOs);

    public long count();

    public List<MaterialIdentificado> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta);

    public void deletarRegistrosNaoSinc();

}
