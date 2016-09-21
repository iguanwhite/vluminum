package br.com.velp.vluminum.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import br.com.velp.vluminum.entity.Usuario;

public class Util {

    public static final String SERVICO_WS = "/service?method=";
    public static String login, senha, endereco, porta, nomeWS;

    public static boolean stringBrancaNull(String str) {
        return str == null && str.trim().equals("");
    }

    public static boolean stringBrancaOuNull(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean listaNotNullVazia(List list) {
        return list != null && !list.isEmpty();
    }

    public static InputStream realizaRequisicao(String metodo, String dataAlteracao, Integer idUsuario, String idsCliente, String idsOs) throws Exception {
        try {
            Log.d("sincronismo", metodo);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario", login);
            jsonObject.accumulate("senha", senha);
            if (dataAlteracao != null) {
                jsonObject.accumulate("data_alteracao", dataAlteracao);
            }
            if (idUsuario != null) {
                jsonObject.accumulate("id_usuario", idUsuario);
            }
            if (idsCliente != null && !idsCliente.toString().equals("")) {
                jsonObject.accumulate("ids_cliente", idsCliente);
            }

            if (idsOs != null && !idsOs.toString().equals("")) {
                jsonObject.accumulate("ids_os", idsOs);
            }

            json = "[";
            json += jsonObject.toString();
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + endereco + ":" + porta + "/" + nomeWS + SERVICO_WS + metodo;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return (httpResponse.getEntity().getContent());
        } catch (Exception ex) {
            Log.i("ERRO", ex.getMessage());
        }
        return null;
    }


    public static InputStream realizaRequisicaoPonto(String metodo, String dataAlteracao, Integer idUsuario, String idsCliente, String idsOs,Integer filtroQuantidade,Integer ultimoIdPontoSinc) throws Exception {
        try {
            Log.d("sincronismo", metodo);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario", login);
            jsonObject.accumulate("senha", senha);
            if (dataAlteracao != null) {
                jsonObject.accumulate("data_alteracao", dataAlteracao);
            }
            if (idUsuario != null) {
                jsonObject.accumulate("id_usuario", idUsuario);
            }
            if (idsCliente != null && !idsCliente.toString().equals("")) {
                jsonObject.accumulate("ids_cliente", idsCliente);
            }

            if (idsOs != null && !idsOs.toString().equals("")) {
                jsonObject.accumulate("ids_os", idsOs);
            }

            if(filtroQuantidade != null){
                jsonObject.accumulate("quantidade", filtroQuantidade);
            }

            if(ultimoIdPontoSinc != null){
                jsonObject.accumulate("ultimo_id_ponto_sinc", ultimoIdPontoSinc);
            }

            json = "[";
            json += jsonObject.toString();
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + endereco + ":" + porta + "/" + nomeWS + SERVICO_WS + metodo;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return (httpResponse.getEntity().getContent());
        } catch (Exception ex) {
            Log.i("ERRO", ex.getMessage());
        }
        return null;
    }


    public static InputStream realizaRequisicaoMaterial(String metodo, String dataAlteracao, Integer idUsuario, String idsCliente, String idsOs,Integer filtroQuantidade,Integer ultimoIdPontoSinc) throws Exception {
        try {
            Log.d("sincronismo", metodo);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario", login);
            jsonObject.accumulate("senha", senha);
            if (dataAlteracao != null) {
                jsonObject.accumulate("data_alteracao", dataAlteracao);
            }
            if (idUsuario != null) {
                jsonObject.accumulate("id_usuario", idUsuario);
            }
            if (idsCliente != null && !idsCliente.toString().equals("")) {
                jsonObject.accumulate("ids_cliente", idsCliente);
            }

            if (idsOs != null && !idsOs.toString().equals("")) {
                jsonObject.accumulate("ids_os", idsOs);
            }

            if(filtroQuantidade != null){
                jsonObject.accumulate("quantidade", filtroQuantidade);
            }

            if(ultimoIdPontoSinc != null){
                jsonObject.accumulate("ultimo_id_ponto_sinc", ultimoIdPontoSinc);
            }

            json = "[";
            json += jsonObject.toString();
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + endereco + ":" + porta + "/" + nomeWS + SERVICO_WS + metodo;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return (httpResponse.getEntity().getContent());
        } catch (Exception ex) {
            Log.i("ERRO", ex.getMessage());
        }
        return null;
    }



    public static void setarUsuarioLogado(String login) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("usuario", login);

            json = "[";
            json += jsonObject.toString();
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + endereco + ":" + porta + "/" + nomeWS + SERVICO_WS + "getUsuarioLogin";

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();

            String result = convertInputStreamToString(inputStream);

            if (result != null && !result.isEmpty()) {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonUsuario = (JSONObject) jsonArray.get(0);

                Usuario usuario = new Usuario();
                usuario.setId(Integer.parseInt(jsonUsuario.getString("id_usuario")));

                VLuminumUtil.setUsuarioLogado(usuario);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static InputStream realizaRequisicaoSet(String metodo, JSONObject jsonObject) throws Exception {
        InputStream inputStream = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            String json = "";

            JSONArray array = new JSONArray();
            array.put(jsonObject);

            json = "[";
            json += jsonObject;
            json += "]";

            StringEntity se = new StringEntity(json);

            String url = "http://" + endereco + ":" + porta + "/" + nomeWS + SERVICO_WS + metodo;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            return inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder(inputStream.available());
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                result.append(line);

            inputStream.close();

        }
        return result.toString();
    }

}
