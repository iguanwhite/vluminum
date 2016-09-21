package br.com.velp.vluminum.enumerator;

public enum Prioridade {
    SELECIONE(null, "-- SELECIONE --"),
    BAIXA(0, "BAIXA"),
    MEDIA(1, "MÉDIA"),
    ALTA(2, "ALTA"),
    CRITICA(3, "CRÍTICA");

    private Integer codigo;
    private String descricao;

    Prioridade(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static Prioridade getPrioridade(Integer cod) {
        switch (cod) {
            case 0:
                return BAIXA;
            case 1:
                return MEDIA;
            case 2:
                return ALTA;
            case 3:
                return CRITICA;
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
