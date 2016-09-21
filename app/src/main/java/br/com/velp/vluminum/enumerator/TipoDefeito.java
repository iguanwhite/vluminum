package br.com.velp.vluminum.enumerator;

public enum TipoDefeito {

    TOLERAVEL(1, "TOLERÁVEL"),
    GRAVE(2, "GRAVE"),
    CRITICO(3, "CRÍTICO");

    private Integer codigo;
    private String descricao;

    TipoDefeito(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
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
