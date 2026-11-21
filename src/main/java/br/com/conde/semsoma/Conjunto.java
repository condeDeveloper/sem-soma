package br.com.conde.semsoma;

import java.util.ArrayList;
import java.util.List;

/**
 * Um subconjunto de {1, ..., n} guardado num {@code long}: o bit i ligado quer dizer que
 * i esta no conjunto. Cabe ate n = 62.
 */
public final class Conjunto {

    /** O maior n que cabe num long com o bit 0 sobrando. */
    public static final int MAXIMO = 62;

    private Conjunto() {}

    /** Monta a mascara a partir dos elementos. */
    public static long de(int... elementos) {
        long mascara = 0;
        for (int e : elementos) {
            if (e < 1 || e > MAXIMO) {
                throw new IllegalArgumentException("fora de 1.." + MAXIMO + ": " + e);
            }
            mascara |= 1L << e;
        }
        return mascara;
    }

    /** Os elementos em ordem crescente. */
    public static List<Integer> elementos(long mascara) {
        List<Integer> lista = new ArrayList<>();
        for (long m = mascara; m != 0; m &= m - 1) {
            lista.add(Long.numberOfTrailingZeros(m));
        }
        return lista;
    }

    /** Quantos elementos. */
    public static int tamanho(long mascara) {
        return Long.bitCount(mascara);
    }

    /** Todos os numeros de 1 a ate. */
    public static long intervalo(int de, int ate) {
        long mascara = 0;
        for (int i = de; i <= ate; i++) {
            mascara |= 1L << i;
        }
        return mascara;
    }

    /** Os impares de 1 a n. */
    public static long impares(int n) {
        long mascara = 0;
        for (int i = 1; i <= n; i += 2) {
            mascara |= 1L << i;
        }
        return mascara;
    }

    /** O conjunto como texto: {1, 4, 7}. */
    public static String texto(long mascara) {
        List<Integer> lista = elementos(mascara);
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(lista.get(i));
        }
        return sb.append('}').toString();
    }
}
