package br.com.velp.vluminum.enumerator;

public enum Danificado {

    SELECIONE(null, "-- SELECIONE --"),
    NAO(2, "NÃO"),
    SIM(1, "SIM");

    private Integer codigo;
    private String descricao;

    Danificado(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static Danificado getDanificado(Boolean cod) {
        if (cod) {
            return Danificado.SIM;
        } else {
            return Danificado.NAO;
        }
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
