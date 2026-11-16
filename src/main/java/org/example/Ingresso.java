package org.example;

//Model
public class Ingresso {
    private final String filme;
    private final int quantidade;
    private final String tipo;

    public Ingresso(String filme, int quantidade, String tipo) {
        if(!"orbita".equals(filme) && !"codigo".equals(filme) && !"oceano".equals(filme)) {
            throw new IllegalArgumentException("Escolha um filme válido");
        }

        if (quantidade < 1 || quantidade > 10) {
            throw new IllegalArgumentException("Escolha entre 1 e 10 ingressos");
        }

        if (!"inteira".equalsIgnoreCase(tipo) && !"meia".equalsIgnoreCase(tipo)) {
            throw new IllegalArgumentException("Escolha inteira ou meia");
        }
        this.filme = filme;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }

    public String getTituilo() {
        return switch (filme) {
            case "orbida" -> "Além da Órbita";
            case "codigo" -> "Código Secreto";
            default -> "Oceano Vivo";
        };
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPrecoUnitarioCentavos() {
        return tipo.equalsIgnoreCase("meia") ? 1500 : 3000;
    }

    public int calcularTotalCentavos() {
        return quantidade * getPrecoUnitarioCentavos();
    }
}
