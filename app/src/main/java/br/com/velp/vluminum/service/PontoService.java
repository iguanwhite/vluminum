package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoResultadoConsulta;

public interface PontoService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(Ponto ponto) throws RegraNegocioException, CampoObrigatorioException;

    public boolean deletar(Ponto ponto) throws RegraNegocioException;

    public Ponto buscarPorId(String id);

    public Ponto buscarPorCoordenadas(double latitude, double longitude);

    public  List<Ponto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta);

    public  List<PontoResultadoConsulta> buscarPorParametroConsulta2(PontoParametroConsulta parametroConsulta);

    public List<Ponto> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public long count();

    public Ponto buscarPorPlaqueta(String plaqueta);

    public void deletarRegistrosNaoSinc();

    public List<PontoResultadoConsulta> buscarEspecificos(PontoParametroConsulta parametroConsulta);
}
