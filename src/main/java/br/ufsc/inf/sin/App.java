package br.ufsc.inf.sin;

import br.ufsc.inf.sin.viz.ConsoleGameInterface;


/**
 *
 * Jogo dos 8
 *
 * A implementação leva em conta um tabuleiro final que pode ser de qualquer tamanho desde que seja quadrado.
 * Este programa toma um segundo tabuleiro desordenado e, considerando as regras de movimentação, gera os tabuleiros
 * intermediários que levam a posição final.
 *
 * A heurística utilizada é a de expandir jogadas válidas e visitar as próximas utilizando o algoritmo A* com uma
 * função de custo do tipo Distância Manhattan somada ao custo total de jogadas (1 por jogada) mais duas vezes a permutação de
 * peças com posiçõs finais na mesma linha e com movimento de sobreposição.
 *
 * Encontramos dificuldades na implementação da função de custo.
 *
 * Uma limitação inerente ao jogo e não a implementação é a impossibilidade de resolver determinados tabuleiros. Estima-se que
 * no jogo dos 15, pelo menos metade dos possíveis arranjos de tabuleiro não tenham solução para um tabuleiro final arbitrário.
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        new ConsoleGameInterface();
    }
}
