package br.com.conde.semsoma;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** A definicao em casos que se confere de cabeca. */
class JuizTest {

    @Test
    void oDobroDecideSeUmEDoisConvivem() {
        assertThat(Juiz.semSoma(Conjunto.de(1, 2), Regra.COM_DOBRO)).isFalse();
        assertThat(Juiz.semSoma(Conjunto.de(1, 2), Regra.SEM_DOBRO)).isTrue();
    }

    @Test
    void umDoisTresCaiNasDuasRegras() {
        assertThat(Juiz.semSoma(Conjunto.de(1, 2, 3), Regra.COM_DOBRO)).isFalse();
        assertThat(Juiz.semSoma(Conjunto.de(1, 2, 3), Regra.SEM_DOBRO)).isFalse();
    }

    @Test
    void impareseMetadeDeCimaPassam() {
        assertThat(Juiz.semSoma(Conjunto.de(1, 3, 5, 7, 9), Regra.COM_DOBRO)).isTrue();
        assertThat(Juiz.semSoma(Conjunto.de(6, 7, 8, 9, 10), Regra.COM_DOBRO)).isTrue();
    }

    @Test
    void oVazioEOUnitarioSaoSemSoma() {
        assertThat(Juiz.semSoma(0L, Regra.COM_DOBRO)).isTrue();
        assertThat(Juiz.semSoma(Conjunto.de(7), Regra.COM_DOBRO)).isTrue();
    }

    @Test
    void quatroEOitoNaoConvivemComODobro() {
        assertThat(Juiz.semSoma(Conjunto.de(3, 4, 8), Regra.COM_DOBRO)).isFalse();
        assertThat(Juiz.semSoma(Conjunto.de(3, 4, 8), Regra.SEM_DOBRO)).isTrue();
    }

    @Test
    void contagensPequenasDeCabeca() {
        // n = 3: {}, {1}, {2}, {3}, {1,3}, {2,3}. {1,2} cai pelo 1+1.
        assertThat(Juiz.conta(3, Regra.COM_DOBRO)).isEqualTo(6);
        // sem o dobro {1,2} volta.
        assertThat(Juiz.conta(3, Regra.SEM_DOBRO)).isEqualTo(7);
    }

    @Test
    void conjuntoGuardaEDevolveOsElementos() {
        long m = Conjunto.de(2, 5, 9);
        assertThat(Conjunto.elementos(m)).containsExactly(2, 5, 9);
        assertThat(Conjunto.tamanho(m)).isEqualTo(3);
        assertThat(Conjunto.texto(m)).isEqualTo("{2, 5, 9}");
        assertThat(Conjunto.impares(6)).isEqualTo(Conjunto.de(1, 3, 5));
        assertThat(Conjunto.intervalo(4, 6)).isEqualTo(Conjunto.de(4, 5, 6));
    }
}
