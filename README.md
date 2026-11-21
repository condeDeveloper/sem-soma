# sem-soma

Um conjunto de inteiros é **sem soma** quando nenhum elemento é a soma de outros dois.
Os ímpares de 1 a n são sem soma, porque ímpar mais ímpar dá par. Tudo que fica acima de n/2
também é, porque qualquer soma ali passa de n.

Cameron e Erdős perguntaram quantos subconjuntos de {1, ..., n} são sem soma. A resposta
cresce como 2^(n/2) vezes uma constante, e a constante é uma para n par e outra para n
ímpar. Este repositório conta **todos**, um por um, até n = 50, e confere a busca contra a
força bruta sobre os 2^n subconjuntos até n = 20.

```bash
mvn -B -ntp verify                              # 21 testes
mvn -B -ntp exec:java -Dexec.args="contagem"    # todos os conjuntos sem soma até 50
mvn -B -ntp exec:java -Dexec.args="estrutura"   # ímpares, metade de cima e o resto
mvn -B -ntp exec:java -Dexec.args="maximos"     # os conjuntos máximos
mvn -B -ntp exec:java -Dexec.args="variante"    # com dobro e sem dobro
mvn -B -ntp exec:java -Dexec.args="juiz"        # a busca contra a força bruta
```

Java 21, sem dependência fora de JUnit e AssertJ.

## Quatro achados

1. **Em n = 50 existem 417.931.547 conjuntos sem soma**, e a busca visitou cada um deles
   sem olhar nenhum que não fosse. Até n = 20 ela bate com a força bruta nas duas regras.
2. **A razão s(n)/2^(n/2) ainda não parou em n = 50.** Ela sobe até 13,51 em n = 36 (pares)
   e 14,49 em n = 39 (ímpares), e depois cai: em n = 50 está em 12,46.
3. **As duas famílias que são sem soma de graça explicam só 16% dos conjuntos em n = 50.**
   Os ímpares e a metade de cima juntos dão 67.108.864 menos a interseção; os outros
   350.826.779 (83,94%) não são nem uma coisa nem outra.
4. **O maior conjunto sem soma tem ⌈n/2⌉ elementos, e há exatamente 2 deles em todo n ímpar
   e exatamente 3 em todo n par de 10 a 40.** O terceiro dos pares é o bloco
   {n/2, ..., n−1}, que para um antes de n.

## A contagem

A busca põe os elementos em ordem crescente. Quem entra é sempre o maior, então a única
soma que ele pode quebrar é a dele mesmo: basta ele não estar na máscara das somas já
feitas. Depois de entrar, ele soma com todos os anteriores de uma vez, num deslocamento de
bits (`conjunto << y`).

```
n   pares: s(n)     s/2^(n/2)    ímpares: s(n)    s/2^(n/2)
10  151             4.7188       9   108          4.7730
20  9.583           9.3584       19  6.976        9.6343
30  415.543         12.6814      29  308.213      13.3020
34  1.739.022       13.2677      33  1.311.244    14.1478
36  3.540.355       13.5054      35  2.630.061    14.1886
38  7.051.789       13.4502      37  5.344.961    14.4175
40  14.158.720      13.5028      39  10.747.207   14.4947
42  28.188.520      13.4413      41  21.295.570   14.3607
46  109.416.660     13.0435      45  83.902.379   14.1449
50  417.931.547     12.4553      49  322.546.325  13.5943
```

O teorema diz que a razão converge, e diz para onde. A tabela diz outra coisa, que não
contradiz o teorema: até n = 50 a razão ainda está descendo de um pico. Quem olhasse só
até n = 36 apostaria num limite perto de 13,5 para os pares. O `contagem` imprime as 50
linhas.

## Quem são os conjuntos sem soma

```
n   total          só ímpares   só acima de n/2  nenhum dos dois  fração
10  151            32           32               91               60.26%
20  9.583          1.024        1.024            7.567            78.96%
30  415.543        32.768       32.768           350.135          84.26%
40  14.158.720     1.048.576    1.048.576        12.062.592       85.20%
45  83.902.379     8.388.608    8.388.608        67.129.259       80.01%
50  417.931.547    33.554.432   33.554.432       350.826.779      83.94%
```

As duas famílias "de graça" têm 2^(n/2) conjuntos cada uma, e o teste confere que a busca
acha as duas inteiras. Mesmo assim elas são a minoria. A fração do resto pula entre n par
e n ímpar e não desce: de n = 30 em diante fica em 80% nos ímpares e entre 84% e 85% nos pares.

## Os maiores

```
n   maior  quantos  quais
5   3      2        {1, 3, 5} {3, 4, 5}
6   3      5
7   4      2        {1, 3, 5, 7} {4, 5, 6, 7}
8   4      4        {1, 3, 5, 7} {2, 3, 7, 8} {4, 5, 6, 7} {5, 6, 7, 8}
9   5      2        {1, 3, 5, 7, 9} {5, 6, 7, 8, 9}
10  5      3        {1, 3, 5, 7, 9} {5, 6, 7, 8, 9} {6, 7, 8, 9, 10}
```

Os ímpares, a metade de cima e, quando n é par, o bloco que começa em n/2 e para em n−1:
n/2 + n/2 = n, que ficou de fora. As exceções são todas pequenas. O n = 8 tem o
{2, 3, 7, 8}, que não é de família nenhuma, e o n = 6 tem cinco. Do 10 ao 40, todo par tem
exatamente três.

## Com o dobro e sem ele

A definição deixa uma dúvida: x + x = z conta? No livro conta, e {1, 2} não é sem soma.
Lendo "dois elementos" como dois elementos **diferentes**, {1, 2} passa.

```
n   com dobro (x+x=z conta)  sem dobro (só x diferente de y)  razão  maiores
10  151                      261                              1.73   5 e 6
20  9.583                    18.713                           1.95   10 e 11
30  415.543                  855.661                          2.06   15 e 16
40  14.158.720               30.497.011                       2.15   20 e 21
```

A leitura frouxa mais que dobra a contagem em n = 40 e, nos pares, ganha um elemento a mais
no maior conjunto: {n/2, ..., n} inteiro, porque n/2 + n/2 = n deixa de contar. Nos
ímpares o maior não muda.

## O que as medidas me corrigiram

**Eu esperava que os ímpares e a metade de cima dominassem.** Pensei que os conjuntos sem
soma fossem, na maioria, de uma das duas famílias, e que a fração do resto fosse caindo com
n. Ela sobe: 60% em n = 10, 84% em n = 50.

**Eu esperava dois conjuntos máximos em todo n.** Os ímpares e a metade de cima. Nos pares
são três a partir de n = 10, e o terceiro é o bloco que para em n−1, que eu não tinha
visto. Antes do 10 há quatro ou cinco.

## Dentro

| arquivo | o que faz |
|---|---|
| `Conjunto` | subconjunto de {1..62} num `long` |
| `Regra` | com dobro (o livro) e sem dobro |
| `Busca` | a enumeração por máscara de somas, com as famílias e os máximos |
| `Juiz` | a definição ao pé da letra sobre os 2^n subconjuntos; não importa a busca |
| `Medidor` | as cinco tabelas acima |

21 testes. Os números deste README estão presos em `MedidasTest`.

## Licença

MIT.
