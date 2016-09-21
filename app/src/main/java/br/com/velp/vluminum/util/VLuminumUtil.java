package br.com.velp.vluminum.util;

import br.com.velp.vluminum.entity.Usuario;

public class VLuminumUtil {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        VLuminumUtil.usuarioLogado = usuarioLogado;
    }

}
