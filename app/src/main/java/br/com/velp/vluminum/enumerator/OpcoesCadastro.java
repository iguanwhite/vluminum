package br.com.velp.vluminum.enumerator;

public enum OpcoesCadastro {

    PONTO("PONTO");

    private String descricao;

    OpcoesCadastro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
