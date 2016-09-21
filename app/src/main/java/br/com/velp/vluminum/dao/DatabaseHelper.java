package br.com.velp.vluminum.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.velp.vluminum.entity.Alimentador;
import br.com.velp.vluminum.entity.ConfiguracaoWebService;
import br.com.velp.vluminum.entity.Foto;
import br.com.velp.vluminum.entity.Grupo;
import br.com.velp.vluminum.entity.GrupoServico;
import br.com.velp.vluminum.entity.Material;
import br.com.velp.vluminum.entity.MaterialIdentificado;
import br.com.velp.vluminum.entity.MotivoTroca;
import br.com.velp.vluminum.entity.Municipio;
import br.com.velp.vluminum.entity.MunicipioCliente;
import br.com.velp.vluminum.entity.OpcaoPergunta;
import br.com.velp.vluminum.entity.OrdemServico;
import br.com.velp.vluminum.entity.OrdemServicoPonto;
import br.com.velp.vluminum.entity.OrdemServicoServico;
import br.com.velp.vluminum.entity.Pergunta;
import br.com.velp.vluminum.entity.Ponto;
import br.com.velp.vluminum.entity.PontoServico;
import br.com.velp.vluminum.entity.Poste;
import br.com.velp.vluminum.entity.Resposta;
import br.com.velp.vluminum.entity.Servico;
import br.com.velp.vluminum.entity.Sincronismo;
import br.com.velp.vluminum.entity.Situacao;
import br.com.velp.vluminum.entity.Subestacao;
import br.com.velp.vluminum.entity.Tipo;
import br.com.velp.vluminum.entity.TipoPoste;
import br.com.velp.vluminum.entity.Usuario;
import br.com.velp.vluminum.entity.UsuarioCliente;

/**
 * Classe responsável por implementar a criação, versionamento e acesso ao banco de dados.
 * <p/>
 * Created by Bruno Leonardo on 06/12/2014.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    // Nome do Banco de Dados
    private static final String DATABASE_FILE_NAME = "vluminum.db";

    // Versão do Banco de Dados. Sempre que alterar o BD incrementar a versão.
    private static final int DATABASE_VERSION = 3;

    private Map<Class, Dao<EntityBase, Object>> daos = new HashMap<Class, Dao<EntityBase, Object>>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), ">>>>>>>>>> Criando o BD...");

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Usuario");
            TableUtils.createTable(connectionSource, Usuario.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Poste");
            TableUtils.createTable(connectionSource, Poste.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Foto");
            TableUtils.createTable(connectionSource, Foto.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Subestacao");
            TableUtils.createTable(connectionSource, Subestacao.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Municipio");
            TableUtils.createTable(connectionSource, Municipio.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Ponto");
            TableUtils.createTable(connectionSource, Ponto.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Pergunta");
            TableUtils.createTable(connectionSource, Pergunta.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Resposta");
            TableUtils.createTable(connectionSource, Resposta.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Opcao Resposta");
            TableUtils.createTable(connectionSource, OpcaoPergunta.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Tipo Poste");
            TableUtils.createTable(connectionSource, TipoPoste.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Tipo");
            TableUtils.createTable(connectionSource, Tipo.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Grupo");
            TableUtils.createTable(connectionSource, Grupo.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Situacao");
            TableUtils.createTable(connectionSource, Situacao.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Ordem de Servico");
            TableUtils.createTable(connectionSource, OrdemServico.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Material");
            TableUtils.createTable(connectionSource, Material.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Material Identificado");
            TableUtils.createTable(connectionSource, MaterialIdentificado.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Ordem Servico Ponto");
            TableUtils.createTable(connectionSource, OrdemServicoPonto.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Sincronismo");
            TableUtils.createTable(connectionSource, Sincronismo.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Grupo Servico");
            TableUtils.createTable(connectionSource, GrupoServico.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Servico");
            TableUtils.createTable(connectionSource, Servico.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Alimentador");
            TableUtils.createTable(connectionSource, Alimentador.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Config WS");
            TableUtils.createTable(connectionSource, ConfiguracaoWebService.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Motivo Troca");
            TableUtils.createTable(connectionSource, MotivoTroca.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Ponto Servic");
            TableUtils.createTable(connectionSource, PontoServico.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Usuario_Cliente");
            TableUtils.createTable(connectionSource, UsuarioCliente.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Ordem Servico - Servico");
            TableUtils.createTable(connectionSource, OrdemServicoServico.class);

            Log.i(DatabaseHelper.class.getName(), "Criando a tabela Municipio Cliente");
            TableUtils.createTable(connectionSource, MunicipioCliente.class);

            Log.i(DatabaseHelper.class.getName(), ">>>>>>>>> BD criado com sucesso!");
        } catch (SQLException ex) {
            Log.e(DatabaseHelper.class.getName(), ">>>>>>>>> Erro ao criar o BD <<<<<<<<<", ex);
            throw new RuntimeException(ex);
        }
    }

    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            switch (oldVersion) {
                // TODO: IMPLEMENTAR NAS ALTERAÇÕES DE BD
                case 1:
            }
           try{
               db.execSQL("ALTER TABLE tb_ordem_servico ADD id_chamado Integer");
           }catch (Exception e){
               e.printStackTrace();
           }

            try{
                db.execSQL("ALTER TABLE tb_ordem_servico ADD tel_chamado String");
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                db.execSQL("ALTER TABLE tb_ordem_servico ADD  nome_chamado String");
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                db.execSQL("ALTER TABLE tb_ordem_servico ADD protocolo_chamado String");
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                db.execSQL("ALTER TABLE tb_ordem_servico ADD qtd_chamado String");
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (android.database.SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Erro ao atualizar BD", e);
            throw new RuntimeException(e);
        }

    }

    public <T> Dao<T, Object> getDAO(Class<EntityBase> entidadeClass) {
        Dao<EntityBase, Object> dao = null;
        if (daos.get(entidadeClass) == null) {
            try {
                dao = getDao(entidadeClass);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "Erro durante getDAO", e);
                throw new RuntimeException(e);
            }
            daos.put(entidadeClass, dao);
        }

        return (Dao<T, Object>) daos.get(entidadeClass);
    }

}