package br.com.velp.vluminum.dao;


import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import br.com.velp.vluminum.entity.Usuario;

public class UsuarioDao extends DaoBase<Usuario> {

    private static UsuarioDao dao;

    private UsuarioDao(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public static UsuarioDao getInstance(Context ctx) {
        if (dao == null) {
            dao = new UsuarioDao(ctx);
        }
        return dao;
    }

    public Usuario buscarPorLogin(String login) {
        try {
            Usuario usuario = getDatabaseHelper()
                    .getDao(Usuario.class).queryBuilder()
                    .where().eq("login", login)
                    .queryForFirst();

            return usuario;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario validarUsuario(String login, String senha) {
        Usuario usuario = null;
        final String MD5 = "MD5";
        try {
            QueryBuilder<Usuario, ?> queryBuilder = getDatabaseHelper().getDao(Usuario.class).queryBuilder();
            Where where = queryBuilder.where();

            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(senha.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            where.like("login", login);
            where.and();
            where.eq("senha", hexString.toString());

            PreparedQuery<Usuario> preparedQuery = queryBuilder.prepare();
            usuario = getDatabaseHelper().getDao(Usuario.class).queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return usuario;
    }

}
