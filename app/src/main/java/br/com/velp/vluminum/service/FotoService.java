package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public interface FotoService {
    public boolean salvarOuAtualizar(Foto foto) throws CampoObrigatorioException;

    public boolean deletar(Foto foto) throws RegraNegocioException;

    public Foto buscarPorId(Integer id);

    public List<Foto> buscarPorPonto(Ponto ponto);

    public List<Foto> buscarPorOrdemServico(OrdemServico ordemServico);

    public List<Foto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta);

    public long count();
}
