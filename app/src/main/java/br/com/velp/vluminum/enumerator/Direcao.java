package br.com.velp.vluminum.enumerator;

public enum Direcao {

    SELECIONE(null, "-- SELECIONE --"),
    A(1, "A"),
    B(2, "B"),
    C(3, "C");

    private Integer codigo;
    private String descricao;

    Direcao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static Direcao getDirecao(Integer cod) {
        switch (cod) {
            case 1:
                return A;
            case 2:
                return B;
            case 3:
                return C;
            default:
                return null;

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
