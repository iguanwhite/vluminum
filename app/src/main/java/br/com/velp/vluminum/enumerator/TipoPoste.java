package br.com.velp.vluminum.enumerator;


public enum TipoPoste {

    CURVOS_DUPLOS(1, "CURVOS DUPLOS"),
    CURVOS_SIMPLES(2, "CURVOS SIMPLES"),
    RETOS(3, "RETOS"),
    OCTAGONAL_RETO(4, "OCTAGONAL RETO"),
    NAO_INFORMADO(5, "NAO INFORMADO");

    private Integer codigo;
    private String descricao;

    TipoPoste(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoPoste getTipoPoste(Integer cod) {
        switch (cod) {
            case 1:
                return CURVOS_DUPLOS;
            case 2:
                return CURVOS_SIMPLES;
            case 3:
                return RETOS;
            case 4:
                return OCTAGONAL_RETO;
            case 5:
                return NAO_INFORMADO;
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
