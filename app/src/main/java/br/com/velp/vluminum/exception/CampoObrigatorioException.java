package br.com.velp.vluminum.exception;

public class CampoObrigatorioException extends Exception {

    private static final long serialVersionUID = -3076694111010684064L;

    public CampoObrigatorioException() {
    }

    public CampoObrigatorioException(String message) {
        super(message);
    }

    public CampoObrigatorioException(Throwable cause) {
        super(cause);
    }

    public CampoObrigatorioException(String message, Throwable cause) {
        super(message, cause);
    }

}
