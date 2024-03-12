package org.example;

import java.util.Random;
import java.util.Scanner;

public class Game {
    //private static final int WIN_COUNT = 3;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '.';

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static char[][] field;
    private static final int SIZE_X = 3;
    private static final int SIZE_Y = 3;

    public static void main(String[] args) {
        initialize();
        printField();
        while (true) {
            humanTurn();
            printField();
            if (gameCheck(DOT_HUMAN, "You won!"))
                break;

            aiTurn();
            printField();
            if (gameCheck(DOT_AI, "Computer won!"))
                break;
        }
    }

    private static void initialize() {
        field = new char[SIZE_X][SIZE_Y];
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < SIZE_X * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-" : i / 2 + 1);
        }
        System.out.println();

        for (int i = 0; i < SIZE_X; i++) {
            System.out.print(i + 1 + "|");

            for (int j = 0; j < SIZE_Y; j++)
                System.out.print(field[i][j] + "|");

            System.out.println();
        }

        for (int i = 0; i < SIZE_X * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Enter the coordinates Х и Y  (1 to 3) space separated: ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < SIZE_X
                && y >= 0 && y < SIZE_Y;
    }

    private static void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(SIZE_X);
            y = RANDOM.nextInt(SIZE_Y);
        } while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    private static boolean gameCheck(char symbol, String message) {
        if (checkWin(symbol)) {
            System.out.println(message);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Draw");
            return true;
        }
        return false;
    }

    private static boolean checkDraw() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }

        return true;
    }

    private static boolean checkWin(char symbol) {
        for (int i = 0; i < 3; i++) {
            if ((field[i][0] == symbol && field[i][2] == symbol && field[i][2] == symbol) ||
                    (field[0][i] == symbol && field[1][i] == symbol && field[2][i] == symbol)) {
                return true;
            }
        }
        return ((field[0][0] == symbol && field[1][1] == symbol && field[2][2] == symbol) ||
                (field[0][2] == symbol && field[1][1] == symbol && field[2][0] == symbol));
    }
    // Проверка по трем горизонталям
//        if (field[0][0] == symbol && field[0][1] == symbol && field[0][2] == symbol) return true;
//        if (field[1][0] == symbol && field[1][1] == symbol && field[1][2] == symbol) return true;
//        if (field[2][0] == symbol && field[2][1] == symbol && field[2][2] == symbol) return true;
//
//        // Проверка по диагоналям
//        if (field[0][0] == symbol && field[1][1] == symbol && field[2][2] == symbol) return true;
//        if (field[0][2] == symbol && field[1][1] == symbol && field[2][0] == symbol) return true;
//
//        // Проверка по трем вертикалям
//        if (field[0][0] == symbol && field[1][0] == symbol && field[2][0] == symbol) return true;
//        if (field[0][1] == symbol && field[1][1] == symbol && field[2][1] == symbol) return true;
//        if (field[0][2] == symbol && field[1][2] == symbol && field[2][2] == symbol) return true;



}
