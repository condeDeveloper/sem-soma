package br.com.conde.semsoma;

import java.util.List;
import java.util.Locale;

/** As tabelas do README. Cada uma roda sozinha: {@code exec:java -Dexec.args="nome"}. */
public final class Medidor {

    private static final List<String> TABELAS =
            List.of("contagem", "estrutura", "maximos", "variante", "juiz");

    public static void main(String[] argumentos) {
        String tabela = argumentos.length > 0 ? argumentos[0] : "todas";
        if (tabela.equals("todas")) {
            for (String nome : TABELAS) {
                roda(nome);
                System.out.println();
            }
            return;
        }
        roda(tabela);
    }

    private static void roda(String tabela) {
        switch (tabela) {
            case "contagem" -> contagem();
            case "estrutura" -> estrutura();
            case "maximos" -> maximos();
            case "variante" -> variante();
            case "juiz" -> juiz();
            default -> {
                System.out.println("tabela desconhecida: " + tabela);
                System.out.println("tabelas: " + String.join(", ", TABELAS));
            }
        }
    }

    /** A razao s(n) / 2^(n/2), com n/2 real. */
    public static double razao(long total, int n) {
        return total / Math.pow(2, n / 2.0);
    }

    private static void contagem() {
        titulo("Todos os subconjuntos sem soma de {1..n}, um por um");
        System.out.println("n   pares: s(n)     s/2^(n/2)    impares: s(n)    s/2^(n/2)");
        for (int k = 1; k <= 25; k++) {
            int par = 2 * k;
            int impar = 2 * k - 1;
            long sp = Busca.varre(par, Regra.COM_DOBRO).total();
            long si = Busca.varre(impar, Regra.COM_DOBRO).total();
            System.out.printf(Locale.ROOT, "%-3d %-15s %-12.4f %-3d %-12s %.4f%n",
                    par, pontos(sp), razao(sp, par), impar, pontos(si), razao(si, impar));
        }
    }

    private static void estrutura() {
        titulo("Quem sao os conjuntos sem soma");
        System.out.println("Todo conjunto de impares e sem soma (impar + impar = par), e todo");
        System.out.println("conjunto acima de n/2 tambem (a soma passa de n).");
        System.out.println();
        System.out.println("n   total          so impares   so acima de n/2  nenhum dos dois  fracao");
        for (int n = 10; n <= 50; n += 5) {
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            System.out.printf(Locale.ROOT, "%-3d %-14s %-12s %-16s %-16s %.2f%%%n",
                    n, pontos(r.total()), pontos(r.soImpares()), pontos(r.soMetadeDeCima()),
                    pontos(r.nenhumDosDois()), 100.0 * r.nenhumDosDois() / r.total());
        }
    }

    private static void maximos() {
        titulo("Os maiores conjuntos sem soma");
        System.out.println("n   maior  quantos  quais (ate quatro)");
        for (int n = 1; n <= 40; n++) {
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            StringBuilder quais = new StringBuilder();
            if (r.maximos().size() <= 4) {
                for (long m : r.maximos()) {
                    quais.append(Conjunto.texto(m)).append(' ');
                }
            }
            System.out.printf(Locale.ROOT, "%-3d %-6d %-8d %s%n",
                    n, r.maior(), r.maximos().size(), quais.toString().trim());
        }
    }

    private static void variante() {
        titulo("Com o dobro proibido e sem ele");
        System.out.println("n   com dobro (x+x=z conta)  sem dobro (so x diferente de y)  razao  maiores");
        for (int n = 5; n <= 40; n += 5) {
            Busca.Resultado com = Busca.varre(n, Regra.COM_DOBRO);
            Busca.Resultado sem = Busca.varre(n, Regra.SEM_DOBRO);
            System.out.printf(Locale.ROOT, "%-3d %-24s %-32s %-6.2f %d e %d%n",
                    n, pontos(com.total()), pontos(sem.total()),
                    (double) sem.total() / com.total(), com.maior(), sem.maior());
        }
    }

    private static void juiz() {
        titulo("A busca contra a forca bruta");
        System.out.println("n   forca bruta  busca  com dobro  sem dobro  maiores");
        for (int n = 1; n <= 20; n++) {
            long jc = Juiz.conta(n, Regra.COM_DOBRO);
            long js = Juiz.conta(n, Regra.SEM_DOBRO);
            long[] jm = Juiz.maximos(n, Regra.COM_DOBRO);
            Busca.Resultado bc = Busca.varre(n, Regra.COM_DOBRO);
            Busca.Resultado bs = Busca.varre(n, Regra.SEM_DOBRO);
            boolean ok = jc == bc.total() && js == bs.total()
                    && jm[0] == bc.maior() && jm[1] == bc.maximos().size();
            System.out.printf(Locale.ROOT, "%-3d %-12s %-6s %-10s %-10s %d x %d%n",
                    n, pontos(1L << n), ok ? "bate" : "DIVERGE", pontos(jc), pontos(js),
                    jm[0], jm[1]);
        }
    }

    // --------------------------------------------------------------- utilidades

    private static void titulo(String texto) {
        System.out.println("== " + texto);
        System.out.println();
    }

    /** 1234567 vira 1.234.567. */
    public static String pontos(long valor) {
        return String.format(Locale.ROOT, "%,d", valor).replace(',', '.');
    }
}
