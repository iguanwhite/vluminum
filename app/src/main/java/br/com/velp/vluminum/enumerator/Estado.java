package br.com.velp.vluminum.enumerator;

public enum Estado {

    ACRE("Acre", "AC"),
    ALAGOAS("Alagoas", "AL"),
    AMAZONAS("Amazonas", "AM"),
    AMAPA("Amapá", "AP"),
    BAHIA("Bahia", "BA"),
    CEARA("Ceará", "CE"),
    DISTRITOFEDERAL("Distrito Federal", "DF"),
    ESPIRITOSANTO("Espírito Santo", "ES"),
    GOIAS("Goiás", "GO"),
    MARANHAO("Maranhão", "MA"),
    MINASGERAIS("Minas Gerais", "MG"),
    MATOGROSSODOSUL("Mato Grosso do Sul", "MS"),
    MATOGROSSO("Mato Grosso", "MT"),
    PARA("Pará", "PA"),
    PARAIBA("Paraíba", "PB"),
    PERNAMBUCO("Pernambuco", "PE"),
    PIAUI("Piauí", "PI"),
    PARANA("Paraná", "PR"),
    RIODEJANEIRO("Rio de Janeiro", "RJ"),
    RIOGRANDEDONORTE("Rio Grande do Norte", "RN"),
    RIOGRANDEDOSUL("Rio Grande do Sul", "RS"),
    RONDONIA("Rondônia", "RO"),
    RORAIMA("Roraima", "RR"),
    SANTACATARINA("Santa Catarina", "SC"),
    SERGIPE("Sergipe", "SE"),
    SAOPAULO("São Paulo", "SP"),
    TOCANTINS("Tocantins", "TO");

    private String nome;
    private String sigla;

    private Estado(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public static Estado getEstado(String estado) {
        if (estado != null) {
            for (Estado e : values()) {
                if (estado.trim().equalsIgnoreCase(e.getNome().trim()))
                    return e;
            }
        }
        return null;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    @Override
    public String toString() {
        return sigla;
    }
}

