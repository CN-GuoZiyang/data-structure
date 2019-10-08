import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Comparator;

public class CommonTransposition {
    private static int rowNumber = 0;
    private static int colNumber = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static int[][] matrix;
    private static ArrayList<Triple> rawMatrix = new ArrayList<>();
    private static ArrayList<Triple> targetMatrix = new ArrayList<>();

    public static void main(String[] args) {
        System.out.print("请输入矩阵的行数：");
        rowNumber = scanner.nextInt();
        System.out.print("请输入矩阵的列数：");
        colNumber = scanner.nextInt();
        matrix = new int[rowNumber][colNumber];
        System.out.println("请输入矩阵：");
        for(int row = 0; row < rowNumber; row ++) {
            for(int col = 0; col < colNumber; col ++) {
                matrix[row][col] = scanner.nextInt();
            }
        }
        convertToTriple();
        transposition();
    }

    private static void convertToTriple() {
        for(int row = 0; row < rowNumber; row ++) {
            for(int col = 0; col < colNumber; col ++) {
                if(matrix[row][col] != 0) {
                    rawMatrix.add(new Triple(row, col, matrix[row][col]));
                }
            }
        }
        System.out.println("\n原矩阵三元组如下：");
        for(Triple triple : rawMatrix) {
            System.out.println(triple.toString());
        }
        System.out.println();
    }

    private static void transposition() {
        for(Triple triple : rawMatrix) {
            targetMatrix.add(new Triple(triple.getCol(), triple.getRow(), triple.getNumber()));
        }
        Collections.sort(targetMatrix, new TripleComparator());
        System.out.println("\n转置后矩阵三元组如下：");
        for(Triple triple : targetMatrix) {
            System.out.println(triple.toString());
        }
        System.out.println();
    }
}

class TripleComparator implements Comparator<Triple> {
    public int compare(Triple triple1, Triple triple2) {
        if(triple1.getRow() > triple2.getRow()) {
            return 1;
        } else if(triple1.getRow() < triple2.getRow()) {
            return -1;
        } else {
            if(triple1.getCol() > triple2.getCol()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}

class Triple {
    private int row;
    private int col;
    private int number;

    public Triple(int row, int col, int number) {
        this.setRow(row);
        this.setCol(col);
        this.setNumber(number);
    }

    @Override
    public String toString() {
        return "" + row + "\t" + col + "\t" + number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}