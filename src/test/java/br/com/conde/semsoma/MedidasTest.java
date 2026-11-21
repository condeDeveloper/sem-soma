package br.com.conde.semsoma;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Os numeros que estao escritos no README, presos aqui.
 *
 * <p>Se uma medida mudar, este arquivo quebra antes de o texto ficar mentindo. A varredura
 * de 0 a 50 roda uma vez so, no comeco.
 */
class MedidasTest {

    private static final long[] TOTAL = new long[51];

    @BeforeAll
    static void varreTudo() {
        for (int n = 0; n <= 50; n++) {
            TOTAL[n] = Busca.varre(n, Regra.COM_DOBRO).total();
        }
    }

    @Test
    void aTabelaDaContagem() {
        assertThat(TOTAL[10]).isEqualTo(151);
        assertThat(TOTAL[20]).isEqualTo(9_583);
        assertThat(TOTAL[30]).isEqualTo(415_543);
        assertThat(TOTAL[36]).isEqualTo(3_540_355);
        assertThat(TOTAL[39]).isEqualTo(10_747_207);
        assertThat(TOTAL[40]).isEqualTo(14_158_720);
        assertThat(TOTAL[49]).isEqualTo(322_546_325);
        assertThat(TOTAL[50]).isEqualTo(417_931_547);
    }

    @Test
    void aRazaoSobeEDepoisCaiNasDuasParidades() {
        int picoPar = 0;
        int picoImpar = 0;
        for (int n = 1; n <= 50; n++) {
            double r = Medidor.razao(TOTAL[n], n);
            if (n % 2 == 0 && (picoPar == 0 || r > Medidor.razao(TOTAL[picoPar], picoPar))) {
                picoPar = n;
            }
            if (n % 2 == 1 && (picoImpar == 0 || r > Medidor.razao(TOTAL[picoImpar], picoImpar))) {
                picoImpar = n;
            }
        }
        assertThat(picoPar).isEqualTo(36);
        assertThat(picoImpar).isEqualTo(39);
        assertThat(Medidor.razao(TOTAL[36], 36)).isBetween(13.505, 13.506);
        assertThat(Medidor.razao(TOTAL[39], 39)).isBetween(14.494, 14.495);
        assertThat(Medidor.razao(TOTAL[50], 50)).isBetween(12.455, 12.456);
        assertThat(Medidor.razao(TOTAL[49], 49)).isBetween(13.594, 13.595);
    }

    @Test
    void aTabelaDaEstrutura() {
        Busca.Resultado r = Busca.varre(50, Regra.COM_DOBRO);
        assertThat(r.soImpares()).isEqualTo(33_554_432);
        assertThat(r.soMetadeDeCima()).isEqualTo(33_554_432);
        assertThat(r.nenhumDosDois()).isEqualTo(350_826_779);
        assertThat(100.0 * r.nenhumDosDois() / r.total()).isBetween(83.935, 83.945);
        Busca.Resultado dez = Busca.varre(10, Regra.COM_DOBRO);
        assertThat(dez.nenhumDosDois()).isEqualTo(91);
    }

    @Test
    void osMaioresSaoDoisNosImparesETresNosPares() {
        for (int n = 1; n <= 40; n++) {
            Busca.Resultado r = Busca.varre(n, Regra.COM_DOBRO);
            assertThat(r.maior()).as("n=%d", n).isEqualTo((n + 1) / 2);
            int esperado;
            if (n % 2 == 1) {
                esperado = n == 1 ? 1 : 2;
            } else {
                esperado = switch (n) {
                    case 2, 4, 8 -> n == 2 ? 2 : 4;
                    case 6 -> 5;
                    default -> 3;
                };
            }
            assertThat(r.maximos()).as("n=%d", n).hasSize(esperado);
        }
    }

    @Test
    void oTerceiroMaximoDosParesEOBlocoQueParaAntesDeN() {
        Busca.Resultado r = Busca.varre(10, Regra.COM_DOBRO);
        assertThat(r.maximos()).containsExactlyInAnyOrder(
                Conjunto.impares(10), Conjunto.intervalo(5, 9), Conjunto.intervalo(6, 10));
        Busca.Resultado oito = Busca.varre(8, Regra.COM_DOBRO);
        assertThat(oito.maximos()).contains(Conjunto.de(2, 3, 7, 8));
    }

    @Test
    void aTabelaDaVariante() {
        Busca.Resultado com = Busca.varre(40, Regra.COM_DOBRO);
        Busca.Resultado sem = Busca.varre(40, Regra.SEM_DOBRO);
        assertThat(sem.total()).isEqualTo(30_497_011);
        assertThat((double) sem.total() / com.total()).isBetween(2.15, 2.16);
        assertThat(sem.maior()).isEqualTo(21);
        assertThat(Busca.varre(39, Regra.SEM_DOBRO).maior()).isEqualTo(20);
        for (int n = 10; n <= 30; n++) {
            int esperado = n % 2 == 0 ? n / 2 + 1 : (n + 1) / 2;
            assertThat(Busca.varre(n, Regra.SEM_DOBRO).maior()).as("n=%d", n).isEqualTo(esperado);
        }
    }

    @Test
    void oJuizBateAte20() {
        assertThat(Juiz.conta(20, Regra.COM_DOBRO)).isEqualTo(9_583);
        assertThat(Juiz.conta(20, Regra.SEM_DOBRO)).isEqualTo(18_713);
    }
}
