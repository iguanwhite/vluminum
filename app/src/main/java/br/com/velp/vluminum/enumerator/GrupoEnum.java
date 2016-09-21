package br.com.velp.vluminum.enumerator;

public enum GrupoEnum {

    MADEIRA(1, "SIM"),
    AÇO(2, "NÃO");

    private Integer codigo;
    private String descricao;

    GrupoEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static GrupoEnum getCodigo(Integer cod) {
        switch (cod) {
            case 1:
                return MADEIRA;
            case 2:
                return AÇO;
            default:
                return null;

        }
    }

    public static GrupoEnum getDanificado(Boolean cod) {
        if (cod) {
            return GrupoEnum.MADEIRA;
        } else {
            return GrupoEnum.AÇO;
        }
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
