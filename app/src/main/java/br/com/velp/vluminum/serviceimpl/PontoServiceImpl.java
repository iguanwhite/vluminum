package br.com.velp.vluminum.serviceimpl;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.velp.vluminum.dao.PontoDao;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.exception.CampoObrigatorioException;
import br.com.velp.vluminum.exception.RegraNegocioException;
import br.com.velp.vluminum.parametroconsulta.PontoParametroConsulta;
import br.com.velp.vluminum.parametroconsulta.PontoResultadoConsulta;
import br.com.velp.vluminum.service.PontoService;
import br.com.velp.vluminum.util.VLuminumUtil;

public class PontoServiceImpl implements PontoService, Serializable {

    private PontoDao dao;

    public PontoServiceImpl(Context ctx) {
        dao = PontoDao.getInstance(ctx);
    }

    public static List<PontoResultadoConsulta> cloneList(List<PontoResultadoConsulta> list) {
        List<PontoResultadoConsulta> clone = new ArrayList<PontoResultadoConsulta>(list.size());
        try {
            for (PontoResultadoConsulta item : list) clone.add(item.clone());
        } catch (Exception e) {
            e.getMessage();
        }

        return clone;
    }

    @Override
    public boolean salvarOuAtualizar(Ponto ponto) throws RegraNegocioException, CampoObrigatorioException {
        if (ponto.getId() == null) { // Novo ponto
            ponto.setId(UUID.randomUUID().toString());
            ponto.setUsuarioCadastro(VLuminumUtil.getUsuarioLogado().getId());
            ponto.setDataCadastro(new Date());
            ponto.setDataAlteracao(new Date());
        } else { // Atualização de ponto
            ponto.setUsuarioAlteracao(VLuminumUtil.getUsuarioLogado().getId());
            ponto.setDataAlteracao(new Date());

        }

        if (ponto.getSincronizado() != null) {
            validarCamposObrigatorios(ponto);
            validarDados(ponto);
        }
        return dao.salvarOuAtualizar(ponto);
    }

    private void validarDados(Ponto ponto) throws RegraNegocioException {
        StringBuilder msg = new StringBuilder("");

        if (ponto.getCep() != null && !ponto.getCep().trim().equals("") && ponto.getCep().trim().replaceAll("\\D", "").length() != 8) {
            msg.append("O CEP deve possuir 8 dígitos\n");
        }

        if (!msg.toString().trim().equals("")) {
            throw new RegraNegocioException(msg.toString());
        }
    }

    private void validarCamposObrigatorios(Ponto ponto) throws CampoObrigatorioException {
        StringBuilder msg = new StringBuilder("");

        if (ponto.getLogradouro() == null || ponto.getLogradouro().trim().equals("")) {
            msg.append("Logradouro\n");
        }

        if (ponto.getMunicipio() == null || ponto.getMunicipio().getId() == null) {
            msg.append("Município\n");
        }

        if (ponto.getPoste() == null || ponto.getPoste().getId() == null) {
            msg.append("Poste\n");
        }

        if (!msg.toString().trim().equals("")) {
            throw new CampoObrigatorioException(msg.toString());
        }
    }

    @Override
    public boolean deletar(Ponto ponto) throws RegraNegocioException {
        return dao.deletar(ponto);
    }

    @Override
    public Ponto buscarPorId(String id) {
        return (Ponto) dao.buscarPorId(id);
    }

    @Override
    public Ponto buscarPorCoordenadas(double latitude, double longitude) {
        return (Ponto) dao.buscarPorCoordenadas(latitude, longitude);
    }

    @Override
    public List<Ponto> buscarPorParametroConsulta(PontoParametroConsulta parametroConsulta) {
        return dao.buscarPorParametroConsulta(parametroConsulta);
    }

    @Override
    public  List<PontoResultadoConsulta> buscarPorParametroConsulta2(PontoParametroConsulta parametroConsulta) {
        List<PontoResultadoConsulta> l = dao.buscarPorParametroConsulta2(parametroConsulta);

        return converterLista(l);
    }

    @Override
    public List<Ponto> listar(String nomeColunaOrderBy, boolean ordenacaoAscendente) {
        return dao.listar(nomeColunaOrderBy, ordenacaoAscendente);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public Ponto buscarPorPlaqueta(String plaqueta) {
        return dao.buscarPorPlaqueta(plaqueta);
    }

    @Override
    public List<PontoResultadoConsulta> buscarEspecificos(PontoParametroConsulta parametroConsulta) {
        List<PontoResultadoConsulta> l = dao.buscarEspecificos(parametroConsulta);

        return converterLista(l);
    }

    private List<PontoResultadoConsulta> converterLista(List<PontoResultadoConsulta> l) {
        Map<String, PontoResultadoConsulta> mapIds = new HashMap<String, PontoResultadoConsulta>();

        List<PontoResultadoConsulta> copia = new ArrayList<PontoResultadoConsulta>();


        for (PontoResultadoConsulta p : l) {

            if (mapIds.get(p.getIdPonto()) == null) {
                mapIds.put(p.getIdPonto(), p);
            }else{
                PontoResultadoConsulta pr = mapIds.get(p.getIdPonto());
                p.setOrdensServicoDoPonto(pr.getOrdensServicoDoPonto() + ", "+p.getOrdensServicoDoPonto());
                mapIds.put(p.getIdPonto(), p);
            }
        }

        for (Map.Entry<String, PontoResultadoConsulta> entry : mapIds.entrySet())
        {
            copia.add(entry.getValue());
        }

        return copia;
    }

    @Override
    public boolean deletarTudo() {
        dao.deletarTodosRegistros("tb_ponto");
        return false;
    }

    @Override
    public void deletarRegistrosNaoSinc() {
        try {
            dao.deletarRegistrosNaoSinc(Ponto.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
