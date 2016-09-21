package br.com.velp.vluminum.enumerator;

public enum TipoPergunta {

    ABERTA(1, "Aberta"),
    UNICA_ESCOLHA(2, "Única Escolha"),
    MULTIPLA_ESCOLHA(3, "Múltipla Escolha");

    private Integer codigo;
    private String descricao;

    TipoPergunta(Integer codigo, String descricao) {
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
