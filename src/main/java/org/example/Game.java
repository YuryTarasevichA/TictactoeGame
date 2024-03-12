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
    private static int SIZE_X;
    private static int SIZE_Y;

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the size of the field:");
        SIZE_X = scanner.nextInt();
        SIZE_Y = SIZE_X;
        field = new char[SIZE_X][SIZE_Y];
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    // распечатать поле игры
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

    // ход пользователя
    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Enter the coordinates Х и Y  (1 to 3) space separated: ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    // ход компьютера
    private static void aiTurn() {
        int x, y;
        for (int row = 0; row < SIZE_X; row++) {
            for (int col = 0; col < SIZE_Y; col++) {
                if (field[row][col] == DOT_HUMAN) { // если находим клетку с Х пользователя
                    //Пытаемся поставить 0 рядом с крестом
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j < 1; j++) {
                            if (isCellEmpty(row + i, col + j)) { // проверяем, что клетка свободна
                                field[row + i][col + j] = DOT_AI;
                                return;
                            }
                        }
                    }
                }
            }
        }
        // Если не нашли клетку рядом с крестиком, ищем свободную клетку для блокировки или победы

        checkForBlocking(DOT_HUMAN);
        for (int row = 0; row < SIZE_X; row++) {
            for (int col = 0; col < SIZE_Y; col++) {
                if (isCellEmpty(row, col)) {
                    field[row][col] = DOT_AI;
                    return;
                }
            }
        }
    }

    private static void checkForBlocking(char symbol) {
        // Проверяем строки и столбцы на наличие потенциальных выигрышных ходов.
        for (int i = 0; i < SIZE_X; i++) {
            int countRow = 0;
            int countCol = 0;
            int blockRow = -1;
            int blockCol = -1;

            for (int j = 0; j < SIZE_Y; j++) {
                if (field[i][j] == symbol) {
                    countRow++;
                } else if (isCellEmpty(i, j)) {
                    blockRow = j;
                }

                if (field[j][i] == symbol) {
                    countCol++;
                } else if (isCellEmpty(j, i)) {
                    blockCol = j;
                }
            }

            if (countRow == SIZE_Y - 1 && blockRow != -1) {
                field[i][blockRow] = DOT_AI;
                return;
            }

            if (countCol == SIZE_X - 1 && blockCol != -1) {
                field[blockCol][i] = DOT_AI;
                return;
            }
        }

        // Проверяем диагонали на наличие потенциальных выигрышных ходов.
        int countDiag1 = 0;
        int countDiag2 = 0;
        int blockDiag1 = -1;
        int blockDiag2 = -1;

        for (int i = 0; i < SIZE_X; i++) {
            if (field[i][i] == symbol) {
                countDiag1++;
            } else if (isCellEmpty(i, i)) {
                blockDiag1 = i;
            }

            if (field[i][SIZE_X - 1 - i] == symbol) {
                countDiag2++;
            } else if (isCellEmpty(i, SIZE_X - 1 - i)) {
                blockDiag2 = i;
            }
        }

        if (countDiag1 == SIZE_X - 1 && blockDiag1 != -1) {
            field[blockDiag1][blockDiag1] = DOT_AI;
            return;
        }

        if (countDiag2 == SIZE_X - 1 && blockDiag2 != -1) {
            field[blockDiag2][SIZE_X - 1 - blockDiag2] = DOT_AI;
        }
    }


    // пуста ли ячейка
    private static boolean isCellEmpty(int x, int y) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // Проверяем, что индексы находятся в допустимом диапазоне
            return false;
        }
        return field[x][y] == DOT_EMPTY;
    }

    // действительна ли ячейка
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < SIZE_X
                && y >= 0 && y < SIZE_Y;
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
        int size = SIZE_X;
        for (int i = 0; i < size; i++) {
            boolean rowWin = true;
            boolean colWin = true;
            for (int j = 0; j < size; j++) {
                if (field[i][j] != symbol) {
                    rowWin = false;
                }
                if (field[j][i] != symbol) {
                    colWin = false;
                }
            }
            if (rowWin || colWin) {
                return true;
            }
        }
        boolean diag1Win = true;
        boolean diag2Win = true;
        for (int i = 0; i < size; i++) {
            if (field[i][i] != symbol) {
                diag1Win = false;
            }
            if (field[i][size - 1 - i] != symbol) {
                diag2Win = false;
            }
        }

        return diag1Win || diag2Win;

    }

//        if ((field[i][0] == symbol && field[i][2] == symbol && field[i][2] == symbol) ||
//                (field[0][i] == symbol && field[1][i] == symbol && field[2][i] == symbol)) {
//            return true;
//        }
//    }
//        return ((field[0][0] == symbol && field[1][1] == symbol && field[2][2] == symbol) ||
//            (field[0][2] == symbol && field[1][1] == symbol && field[2][0] == symbol));
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
