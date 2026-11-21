package br.com.conde.semsoma;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** A busca contra a forca bruta, e o que a busca nao pode deixar de cumprir. */
class BuscaTest {

    @Test
    void bateComAForcaBrutaNasDuasRegras() {
        for (Regra regra : Regra.values()) {
            for (int n = 0; n <= 16; n++) {
                assertThat(Busca.varre(n, regra).total())
                        .as("n=%d %s", n, regra).isEqualTo(Juiz.conta(n, regra));
            }
        }
    }

    @Test
    void osMaioresBatemComAForcaBruta() {
        for (int n = 1; n <= 16; n++) {
            long[] juiz = Juiz.maximos(n, Regra.COM_DOBRO);
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            assertThat(r.maior()).as("n=%d", n).isEqualTo((int) juiz[0]);
            assertThat(r.maximos()).as("n=%d", n).hasSize((int) juiz[1]);
        }
    }

    @Test
    void todoConjuntoQueABuscaDevolveEPassaNoJuiz() {
        for (int n = 1; n <= 30; n++) {
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            for (long m : r.maximos()) {
                assertThat(Juiz.semSoma(m, Regra.COM_DOBRO)).as("n=%d %s", n,
                        Conjunto.texto(m)).isTrue();
            }
        }
    }

    @Test
    void asDuasFamiliasDeGracaSaoContadasInteiras() {
        // Todo subconjunto dos impares e todo subconjunto de (n/2, n] e sem soma, entao a
        // busca tem de achar 2^(quantos impares) e 2^(quantos acima de n/2).
        for (int n = 1; n <= 30; n++) {
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            int impares = (n + 1) / 2;
            int deCima = n - n / 2;
            int impareseDeCima = Conjunto.tamanho(Conjunto.impares(n) & Conjunto.intervalo(n / 2 + 1, n));
            assertThat(r.soImpares()).as("n=%d", n).isEqualTo(1L << impares);
            assertThat(r.soMetadeDeCima()).as("n=%d", n).isEqualTo(1L << deCima);
            assertThat(r.nosDois()).as("n=%d", n).isEqualTo(1L << impareseDeCima);
        }
    }

    @Test
    void oHistogramaPorTamanhoSomaOTotal() {
        Busca.Resultado r = Busca.varre(24, Regra.COM_DOBRO);
        long soma = 0;
        for (long c : r.porTamanho()) {
            soma += c;
        }
        assertThat(soma).isEqualTo(r.total());
        assertThat(r.porTamanho()[0]).isEqualTo(1);
        assertThat(r.porTamanho()[1]).isEqualTo(24);
    }

    @Test
    void semODobroNuncaHaMenos() {
        for (int n = 1; n <= 30; n++) {
            assertThat(Busca.varre(n, Regra.SEM_DOBRO).total())
                    .isGreaterThanOrEqualTo(Busca.varre(n, Regra.COM_DOBRO).total());
        }
    }

    @Test
    void umJuizQueAceitaTudoSeriaPego() {
        // controle: contar todos os subconjuntos daria 2^n, e a busca nao da.
        assertThat(Busca.varre(10, Regra.COM_DOBRO).total()).isNotEqualTo(1L << 10);
    }
}
