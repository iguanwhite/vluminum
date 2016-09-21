package br.com.velp.vluminum.enumerator;

/**
 * Created by henrique on 17/12/2014.
 */
public enum TipoVeiculo {

    CESTA_SIMPLES(1, "CESTA SIMPLES"),
    CESTA_DUPLA(2, "CESTA DUPLA"),
    CAMINHONETE(3,"CAMINHONETE DE ESCADA METROPOLITANA"),
    EXTENSOR(4, "CAMINHÃO C/ EXTENSOR MUNCK P/ REDES E PODAS");


    private Integer codigo;
    private String descricao;

    TipoVeiculo(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoVeiculo getTipoVeiculo(Integer cod) {
        switch (cod) {
            case 1:
                return CESTA_SIMPLES;
            case 2:
                return CESTA_DUPLA;
            case 3:
                return CAMINHONETE;
            case 4:
                return EXTENSOR;
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
