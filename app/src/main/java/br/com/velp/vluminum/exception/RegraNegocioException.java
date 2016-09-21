package br.com.velp.vluminum.exception;

/**
 * Classe responsável por padronizar as exceções padrão lançadas pela camada de
 * serviço (regra de negócio).
 * <p/>
 * Created by Bruno Leonardo on 06/12/2014.
 */
public class RegraNegocioException extends Exception {

    private static final long serialVersionUID = -3076694111010684064L;

    public RegraNegocioException() {
    }

    public RegraNegocioException(String message) {
        super(message);
    }

    public RegraNegocioException(Throwable cause) {
        super(cause);
    }

    public RegraNegocioException(String message, Throwable cause) {
        super(message, cause);
    }

}
