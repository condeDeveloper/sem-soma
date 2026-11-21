package br.com.conde.semsoma;

import java.util.List;

/**
 * A definicao, ao pe da letra, e a forca bruta sobre os 2^n subconjuntos.
 *
 * <p>Nao usa deslocamento de bits nem a ordem em que a busca acrescenta elementos: le a
 * lista de elementos e testa todo par contra todo terceiro. E lento de proposito. Nao
 * importa nada da {@link Busca}.
 */
public final class Juiz {

    private Juiz() {}

    /** Se o conjunto e sem soma, testando todo x, y, z. */
    public static boolean semSoma(long mascara, Regra regra) {
        List<Integer> a = Conjunto.elementos(mascara);
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.size(); j++) {
                if (i == j && !regra.contaDobro()) {
                    continue;
                }
                int soma = a.get(i) + a.get(j);
                for (int z : a) {
                    if (z == soma) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Quantos subconjuntos de {1..n} sao sem soma, olhando um por um. */
    public static long conta(int n, Regra regra) {
        if (n > 26) {
            throw new IllegalArgumentException("forca bruta so ate 26: " + n);
        }
        long total = 0;
        for (long m = 0; m < 1L << n; m++) {
            if (semSoma(m << 1, regra)) {
                total++;
            }
        }
        return total;
    }

    /** O maior tamanho e quantos conjuntos o atingem, olhando um por um. */
    public static long[] maximos(int n, Regra regra) {
        int maior = 0;
        long quantos = 0;
        for (long m = 0; m < 1L << n; m++) {
            long mascara = m << 1;
            int t = Long.bitCount(mascara);
            if (t < maior || !semSoma(mascara, regra)) {
                continue;
            }
            if (t > maior) {
                maior = t;
                quantos = 0;
            }
            quantos++;
        }
        return new long[] {maior, quantos};
    }
}
