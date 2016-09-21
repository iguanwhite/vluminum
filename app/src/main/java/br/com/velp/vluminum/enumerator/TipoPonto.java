package br.com.velp.vluminum.enumerator;

public enum TipoPonto {

    SELECIONE(null, "-- SELECIONE --"),
    AÉREO(3, "AÉREO"),
    ORNAMENTAL(2, "ORNAMENTAL"),
    PADRAO(0, "PADRÃO"),
    SUBTERRANEO(1, "SUBTERRÂNEO");

    private Integer codigo;
    private String descricao;

    private TipoPonto(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static String getDescricao(Integer codigo) {
        for (TipoPonto value : values()) {
            if (value.getCodigo().equals(codigo)) {
                return value.getDescricao();
            }
        }
        return null;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
