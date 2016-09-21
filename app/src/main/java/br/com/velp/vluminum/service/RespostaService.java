package br.com.velp.vluminum.service;

import java.util.List;

import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;

public interface RespostaService {
    public boolean salvarOuAtualizar(Resposta resposta) throws RegraNegocioException;

    public boolean deletar(Resposta resposta) throws RegraNegocioException;

    public Resposta buscarPorId(Integer id);

    public List<Resposta> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta);

    public boolean buscaRespostaMultiplaEscolha(Integer idPergunta, String idPonto, Integer resp);

    public OpcaoPergunta buscaRespostaSpinner(Integer idPergunta, String idPonto);

    public String buscaRespostaText(Integer idPergunta, String idPonto);

    public boolean apagarRespostaPonto(String idPonto);


}
