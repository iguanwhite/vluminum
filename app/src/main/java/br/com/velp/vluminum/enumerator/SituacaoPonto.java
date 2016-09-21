package br.com.velp.vluminum.enumerator;

public enum SituacaoPonto {

    EM_MANUTENCAO(1),
    FINALIZADA(2);

    private Integer codigo;

    SituacaoPonto(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public static SituacaoPonto getSituacaoPonto(int codigo) {
        switch (codigo) {
            case 1:
                return EM_MANUTENCAO;
            case 2:
                return FINALIZADA;
            default:
                return null;
        }
    }

}
