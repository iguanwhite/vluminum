package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public interface MaterialService extends InterfaceGeneric {
    public boolean salvarOuAtualizar(Material m) throws RegraNegocioException;

    public boolean deletar(Material m) throws RegraNegocioException;

    public Material buscarPorId(Integer id);

    public List<Material> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente);

    public List<Material> listarPorTipo(Integer idTipo);

    public List<Material> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta);

    public long count();
}
