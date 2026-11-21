package br.com.conde.semsoma;

/**
 * As duas leituras de "sem soma".
 *
 * <p>Um conjunto A de inteiros positivos e sem soma quando nao existem x, y, z em A com
 * x + y = z. A pergunta que a definicao deixa no ar e se x pode ser igual a y.
 */
public enum Regra {

    /** x = y vale: {1, 2} nao e sem soma, porque 1 + 1 = 2. E a definicao do livro. */
    COM_DOBRO,

    /** So x diferente de y: {1, 2} passa, {1, 2, 3} nao. */
    SEM_DOBRO;

    /** Se o dobro de um elemento conta como soma proibida. */
    public boolean contaDobro() {
        return this == COM_DOBRO;
    }
}
