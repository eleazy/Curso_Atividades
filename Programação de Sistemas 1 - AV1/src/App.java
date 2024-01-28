import java.util.Scanner;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
     
        Scanner scanner = new Scanner(System.in);

        int[] rows_cols = new int[5];
        String[] message = { "linhas da matriz A: ", "colunas da matriz A: ", "linhas da matriz B: ", "colunas da matriz B: " };
        for (int i = 0; i < 4; i++) {
            System.out.print("Informe o número de " + message[i]);
            rows_cols[i] = scanner.nextInt();
        }

        if (rows_cols[1] != rows_cols[2]) {
            System.out.println("Multiplicação de matrizes não é possível. É necessário que o número de colunas da matriz A seja igual ao número de linhas da matriz B");
            scanner.close();
            return;
        }
 
        String[][] matrixA = new String[rows_cols[0]][rows_cols[1]];
        String[][] matrixB = new String[rows_cols[2]][rows_cols[3]];     
       
        System.out.println("\nInforme os elementos da matriz A:");
        readMatrix(scanner, matrixA);

        System.out.println("\nInforme os elementos da matriz B:");
        readMatrix(scanner, matrixB);
        
        System.out.println("\nMatriz A:");
        System.out.println(formatColumns(convertMatrix(matrixA), rows_cols[1]));
        System.out.println("\nX");
        System.out.println("\nMatriz B:");
        System.out.println(formatColumns(convertMatrix(matrixB), rows_cols[3])); 
        System.out.println("\n=\n\nMatriz C:");

        int[] resultMatrix = multMatrix(matrixA, matrixB);
        String[] stringResult = new String[resultMatrix.length];

        for (int i = 0; i < resultMatrix.length; i++) {
            stringResult[i] = Integer.toString(resultMatrix[i]);
        }
        
        System.out.println(formatColumns(stringResult, rows_cols[3])+"\n");
        scanner.close();
    }

    public static void readMatrix(Scanner scanner, String[][] matrix) {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                System.out.print("Informe o número da linha " + (r + 1) + " e coluna " + (c + 1) + ": ");
                matrix[r][c] = scanner.next();
                if (!matrix[r][c].matches("-?\\d+"))  {
                    System.out.println("Elemento inválido.");
                    c--;
                }
            }
        }
    }

    public static int[] multMatrix(String[][] A, String[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        int[] resultMatrix = new int[rowsA * colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += Integer.parseInt(A[i][k]) *Integer.parseInt(B[k][j]) ;
                }
                resultMatrix[i * colsB + j] = sum;
            }
        }
        return resultMatrix;
    }

    public static String[] convertMatrix(String[][] matrix) {
        String[] matrixArray = new String[matrix.length * matrix[0].length];
        
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixArray[i * matrix[i].length + j] = matrix[i][j];
            }
        }
        return matrixArray;
    }

    public static String formatColumns(String[] input, int colNum) {   
        int len = 0;
        for (int d = 0; d < colNum; d++) {
            len = 0;
            for (int i = d; i < input.length; i += colNum) {
                len = input[i].length() > len ? input[i].length() : len;
            }
            for (int b = d; b < input.length; b += colNum) {
                while (input[b].length() < len) {
                    input[b] += ' ';
                }
            }
        }

        StringBuilder r = new StringBuilder();
        int ii = 0;
        for (int j = 0; j < input.length; j += colNum) {
            for (int i = ii; i < ii + colNum; i++) {
                if ((i == (ii + colNum - 1)) && (i < input.length)) {
                    r.append(input[i]);
                } else if (i < input.length) {
                    r.append(input[i] + " | ");
                }
            }
             if (j < input.length-colNum ) {
                r.append("\n");
            }
            ii += colNum;
        }
       if (r.length() > 5 && r.charAt(r.length() - 2) == '|') {
            r.delete(r.length() - 3, r.length());
        }
        return r.toString();
    }
}
