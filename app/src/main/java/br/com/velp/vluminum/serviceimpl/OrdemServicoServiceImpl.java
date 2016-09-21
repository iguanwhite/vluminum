package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.velp.vluminum.dao.OrdemServicoDao;
import br.com.velp.vluminum.dao.OrdemServicoPontoDao;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.enumerator.SituacaoPonto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.OsParametroConsulta;
import br.com.velp.vluminum.service.OrdemServicoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class OrdemServicoServiceImpl implements OrdemServicoService, Serializable {

    private OrdemServicoDao dao;

    private OrdemServicoPontoDao ordemServicoPontoDao;

    public OrdemServicoServiceImpl(Context ctx) {
        dao = OrdemServicoDao.getInstance(ctx);
        ordemServicoPontoDao = OrdemServicoPontoDao.getInstance(ctx);
    }

    @Override
    public boolean salvarOuAtualizar(OrdemServico os) throws CampoObrigatorioException, RegraNegocioException {
        if (os.getId() == null) {
            os.setId(UUID.randomUUID().toString());
            os.setDataAtribuicao(new Date());
            os.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
       //     os.setDataCadastro(new Date());
            os.setSituacao(new Situacao(Situacao.SITUACAO_EM_CAMPO.toString()));
            os.setDataAlteracao(new Date());
        } else {
            // Ao finalizar uma os com somente 1 ponto, mudar a situação do ponto para finalizado
            Long quantidadePontosDaOS = ordemServicoPontoDao.quantidadePontosDaOs(os.getId());
            if (quantidadePontosDaOS != null && quantidadePontosDaOS == 1) {
                OrdemServicoPonto ordemServicoPonto = ordemServicoPontoDao.listarPorOs(os.getId()).get(0);
                ordemServicoPonto.setSituacao(SituacaoPonto.FINALIZADA.getCodigo());
                ordemServicoPontoDao.salvarOuAtualizar(ordemServicoPonto);
            }

            os.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            os.setDataAlteracao(new Date());
        }
        os.setUsuarioExecucao(VLuminumUtil.getUsuarioLogado().getId());
        os.setUsuarioAtribuicao(VLuminumUtil.getUsuarioLogado().getId());
        validarCamposObrigatorios(os);
        validarDados(os);

        return dao.salvarOuAtualizar(os);
    }

    private void validarDados(OrdemServico os) throws RegraNegocioException {
        StringBuilder msg = new StringBuilder("");

        if (os.getCep() != null && !os.getCep().trim().equals("") && os.getCep().trim().replaceAll("\\D", "").length() != 8) {
            msg.append("O CEP deve possuir 8 dígitos\n");
        }

        if (!msg.toString().trim().equals("")) {
            throw new RegraNegocioException(msg.toString());
        }
    }

    private void validarCamposObrigatorios(OrdemServico os) throws CampoObrigatorioException {
        StringBuilder msg = new StringBuilder("");

        if (os.getMotivoAbertura() == null) {
            msg.append("Motivo\n");
        }

        if (os.getLogradouro() == null || os.getLogradouro().trim().equals("")) {
            msg.append("Logradouro\n");
        }

        if (os.getMunicipio() == null || os.getMunicipio().getId() == null) {
            msg.append("Município\n");
        }

        if (!msg.toString().trim().equals("")) {
            throw new CampoObrigatorioException(msg.toString());
        }
    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(OrdemServico.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean deletar(OrdemServico os) throws RegraNegocioException {
        return dao.deletar(os);
    }

    @Override
    public OrdemServico buscarPorId(String id) {
        return (OrdemServico) dao.buscarPorId(id);
    }

    @Override
    public List<OrdemServico> buscarPorParametroConsulta(OsParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    @Override
    public List<OrdemServico> buscarPorUsuarioAtribuicao(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.buscarPorUsuarioAtribuicao(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public List<OrdemServico> buscarPorPontoEUsuarioAtribuicao(String idPonto, String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.buscarPorPontoEUsuarioAtribuicao(idPonto, nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public long count() {
        return dao.count();
    }


    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_ordem_servico");
        return true;
    }
}
