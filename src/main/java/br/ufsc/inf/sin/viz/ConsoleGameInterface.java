package br.ufsc.inf.sin.viz;

import br.ufsc.inf.sin.game.Board;
import br.ufsc.inf.sin.game.Game;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * Classe de suporte a interface.
 *
 */
public class ConsoleGameInterface {
    Scanner input;
    PrintStream output;
    Game game;

    public ConsoleGameInterface() throws Exception {

        game = new Game();
        input = new Scanner(System.in);
        output = System.out;

        switchMenu(Menus.INICIAL);
    }

    private enum Menus {
        INICIAL("" +
                "Digite a sua escolha:" + System.lineSeparator() +
                "1) Rodar exemplo 8 peças" + System.lineSeparator() +
                "2) Rodar exemplo 15 peças" + System.lineSeparator() +
                "3) Novo jogo" + System.lineSeparator() +
                "!q) Sair" + System.lineSeparator() +
                "", 1, 2, 3),
        JOGO("" +
                "Digite a sua escolha:" + System.lineSeparator() +
                "1) Definir tabuleiro FINAL" + System.lineSeparator() +
                "2) Definir tabuleiro INICIAL" + System.lineSeparator() +
                "3) Definir tabuleiro INICIAL como o FINAL embaralhado" + System.lineSeparator() +
                "4) Mostrar tabuleiros" + System.lineSeparator() +
                "5) Resolver!" + System.lineSeparator() +
                "6) Voltar" + System.lineSeparator() +
                "!q) Sair" + System.lineSeparator() +
                "", 1, 2, 3, 4, 5, 6);

        private String msg;
        private Integer[] choices;

        Menus(String msg, Integer... choices) {
            this.msg = msg;
            this.choices = choices;
        }

        public String getMsg(){
            return msg;
        }

        public Boolean validChoice(Integer choice){
            return Arrays.binarySearch(this.choices, choice) >= 0;
        }
    }

    private void menuState(Menus menu, Integer choice) throws Exception {
        switch (menu) {
            case INICIAL:
                switch (choice){
                    case 1:
                        game.playDefault8Game();
                        switchMenu(Menus.INICIAL);
                        break;
                    case 2:
                        game.playDefault15Game();
                        switchMenu(Menus.INICIAL);
                        break;
                    case 3:
                        game = new Game();
                        switchMenu(Menus.JOGO);
                        break;
                }
                break;
            case JOGO:
                switch (choice){
                    case 1:
                        output.println("Digite as peças do tabuleiro FINAL separadas por \",\". O espaço vazio é representado pelo \"0\"");
                        output.println("Ex: \"1,2,3,4,0,5,6,7,8\".");
                        output.println((new Board(1,2,3,4,0,5,6,7,8)).toString());
                        String finalBoardString = input.nextLine();
                        try{
                            game.setFinalBoard(finalBoardString);
                        } catch (Exception e){
                            output.println("-----------------");
                            output.println("Tabuleiro inválido!");
                            output.println("-----------------");
                            menuState(Menus.JOGO, 1);
                        }
                        game.printFinalBoard();
                        switchMenu(Menus.JOGO);
                        break;
                    case 2:
                        output.println("Digite as peças do tabuleiro INICIAL separadas por \",\". Ex: \"3,5,7,1,2,8,0,6,4\". O espaço vazio é representado pelo \"0\"");
                        output.println("Ex: \"3,5,7,1,2,8,0,6,4\".");
                        output.println((new Board(3,5,7,1,2,8,0,6,4)).toString());
                        String initialBoardString = input.nextLine();
                        try{
                            game.setInitialBoard(initialBoardString);
                        } catch (Exception e){
                            output.println("-----------------");
                            output.println("Tabuleiro inválido!");
                            output.println("-----------------");
                            menuState(Menus.JOGO, 2);
                        }
                        switchMenu(Menus.JOGO);
                        break;
                    case 3:
                        output.println("Embaralhando o tabuleiro final:");
                        output.println("Digite um número MÍNIMO de embaralhamentos");
                        Integer min = inputNumber();
                        if(min == null){
                            menuState(Menus.JOGO, 3);
                        }
                        output.println("Digite um número MÁXIMO de embaralhamentos");
                        Integer max = inputNumber();
                        if(max == null){
                            menuState(Menus.JOGO, 3);
                        }
                        game.setInitialAsFinalScrambledBoard(min, max);
                        switchMenu(Menus.JOGO);
                        break;
                    case 4:
                        game.printFinalBoard();
                        game.printInitialBoard();
                        switchMenu(Menus.JOGO);
                        break;
                    case 5:
                        game.solve();
                        game.printStatistics();
                        switchMenu(Menus.JOGO);
                        break;
                    case 6:
                        switchMenu(Menus.INICIAL);
                        break;
                }
                break;
        }

    }

    private void switchMenu(Menus menu) throws Exception {
        output.println(menu.getMsg());
        Integer choice = parseMenuChoice(menu, input.nextLine());
        if(choice == 0){
            switchMenu(menu);
        } else {
            menuState(menu, choice);
        }
    }

    private Integer parseMenuChoice(Menus menu, String choice){
        choice = choice.trim();
        Integer menuChoice = 0;

        if (choice.equals("!q")){
            output.println("Até logo!");
            System.exit(0);
        }
        try {
            menuChoice = Integer.parseInt(choice);
            menuChoice = menu.validChoice(menuChoice) ? menuChoice : 0;
        }catch (NumberFormatException nfe){}

        if (menuChoice == 0){
            output.println("-----------------");
            output.println("Escolha inválida! Digite o número correspondente a sua escolha ou \"!q\" para sair.");
            output.println("-----------------");
        }

        return menuChoice;
    }

    private Integer inputNumber(){
        Integer number = null;
        try {
            number = input.nextInt();
        }catch (Exception nfe){}

        if (number == 0){
            output.println("-----------------");
            output.println("Escolha inválida! Digite um número.");
            output.println("-----------------");
        }
        return number;
    }

}