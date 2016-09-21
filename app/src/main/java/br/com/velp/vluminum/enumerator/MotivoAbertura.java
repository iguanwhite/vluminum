package br.com.velp.vluminum.enumerator;

public enum MotivoAbertura {

    MANUTENCAO_VARIOS_PONTOS(5, "MANUTENÇÃO DE VÁRIOS PONTOS"),
    MANUTENCAO_PONTO_ACESO(6, "MANUTENÇÃO EM PONTO ACESO DURANTE O DIA"),
    MANUTENCAO_PONTO_APAGADO(7, "MANUTENÇÃO EM PONTO APAGADO"),
    SUBSTITUICAO_LUMINARIA(8, "SUBSTITUIÇÃO DE LUMINÁRIA"),
    PODA_DE_ARVORE(9, "PODA DE ÁRVORE"),
    DIVERSOS(10, "DIVERSOS"),
    MANUTENÇÃO_DE_PONTOS_SEQUENCIAIS_APAGADOS(11, "MANUTENÇÃO DE PONTOS SEQUENCIAIS APAGADOS"),
    MANUTENÇÃO_DE_PONTOS_SEQUENCIAIS_ACESOS_DURANTE_O_DIA(12, "MANUTENÇÃO DE PONTOS SEQUENCIAIS ACESOS DURANTE O DIA");


    private Integer codigo;
    private String descricao;

    MotivoAbertura(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static MotivoAbertura getPrioridade(Integer cod) {
        switch (cod) {
            case 5:
                return MANUTENCAO_VARIOS_PONTOS;
            case 6:
                return MANUTENCAO_PONTO_ACESO;
            case 7:
                return MANUTENCAO_PONTO_APAGADO;
            case 8:
                return SUBSTITUICAO_LUMINARIA;
            case 9:
                return PODA_DE_ARVORE;
            case 10:
                return DIVERSOS;
            case 11:
                return MANUTENÇÃO_DE_PONTOS_SEQUENCIAIS_APAGADOS;
            case 12:
                return MANUTENÇÃO_DE_PONTOS_SEQUENCIAIS_ACESOS_DURANTE_O_DIA;

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
