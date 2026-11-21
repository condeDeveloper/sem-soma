package br.com.conde.semsoma;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumera todos os subconjuntos sem soma de {1..n}, um por um, sem olhar nenhum que nao
 * seja.
 *
 * <p>Os elementos entram em ordem crescente. Quem entra e sempre o maior, entao a unica
 * soma que ele pode quebrar e a dele mesmo: basta ele nao estar na mascara das somas ja
 * feitas. Depois de entrar, ele soma com todos os anteriores de uma vez, num
 * deslocamento: {@code conjunto << y}.
 */
public final class Busca {

    /** O que a varredura de um n devolve. */
    public record Resultado(
            int n,
            Regra regra,
            long total,
            long[] porTamanho,
            long soImpares,
            long soMetadeDeCima,
            long nosDois,
            int maior,
            List<Long> maximos) {

        /** Os que nao sao so impares nem so da metade de cima. */
        public long nenhumDosDois() {
            return total - soImpares - soMetadeDeCima + nosDois;
        }
    }

    private final int n;
    private final Regra regra;
    private final long pares;
    private final long metadeDeBaixo;
    private final long[] porTamanho;
    private long total;
    private long soImpares;
    private long soMetadeDeCima;
    private long nosDois;
    private int maior;
    private final List<Long> maximos = new ArrayList<>();

    private Busca(int n, Regra regra) {
        if (n < 0 || n > Conjunto.MAXIMO) {
            throw new IllegalArgumentException("n fora de 0.." + Conjunto.MAXIMO + ": " + n);
        }
        this.n = n;
        this.regra = regra;
        this.pares = Conjunto.intervalo(1, n) & ~Conjunto.impares(n);
        this.metadeDeBaixo = Conjunto.intervalo(1, n / 2);
        this.porTamanho = new long[n + 2];
    }

    /** Varre {1..n} inteiro. */
    public static Resultado varre(int n, Regra regra) {
        Busca b = new Busca(n, regra);
        b.desce(1, 0L, 0L);
        return new Resultado(n, regra, b.total, b.porTamanho, b.soImpares, b.soMetadeDeCima,
                b.nosDois, b.maior, List.copyOf(b.maximos));
    }

    private void desce(int proximo, long conjunto, long somas) {
        registra(conjunto);
        for (int y = proximo; y <= n; y++) {
            if ((somas >>> y & 1L) != 0) {
                continue;
            }
            long novas = somas | (conjunto << y);
            if (regra.contaDobro() && 2 * y <= n) {
                novas |= 1L << (2 * y);
            }
            desce(y + 1, conjunto | (1L << y), novas);
        }
    }

    private void registra(long conjunto) {
        total++;
        int t = Long.bitCount(conjunto);
        porTamanho[t]++;
        boolean impar = (conjunto & pares) == 0;
        boolean deCima = (conjunto & metadeDeBaixo) == 0;
        if (impar) {
            soImpares++;
        }
        if (deCima) {
            soMetadeDeCima++;
        }
        if (impar && deCima) {
            nosDois++;
        }
        if (t > maior) {
            maior = t;
            maximos.clear();
        }
        if (t == maior) {
            maximos.add(conjunto);
        }
    }
}
