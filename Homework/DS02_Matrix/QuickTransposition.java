import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Comparator;

public class QuickTransposition {
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
        quickTransposition();
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

    private static void quickTransposition() {
        for(int i = 0; i < rawMatrix.size(); i ++) {
            targetMatrix.add(new Triple());
        }
        int[] num = new int[colNumber];     //num中记录第col列的非零元素个数
        for(int i = 0; i < colNumber; i ++) {
            num[i] = 0;
        }
        for(Triple triple : rawMatrix) {
            num[triple.getCol()] ++;
        }
        int[] cpot = new int[colNumber];
        cpot[0] = 0;
        for(int i = 1; i < colNumber; i ++) {
            cpot[i] = cpot[i - 1] + num[i - 1];
        }
        for(Triple triple : rawMatrix) {
            int col = triple.getCol();
            int row = triple.getRow();
            targetMatrix.get(cpot[col]).setRow(col);
            targetMatrix.get(cpot[col]).setCol(row);
            targetMatrix.get(cpot[col]).setNumber(triple.getNumber());
            cpot[col] ++;
        }
        System.out.println("\n转置后的矩阵三元组如下：");
        for(Triple triple : targetMatrix) {
            System.out.println(triple.toString());
        }
        System.out.println();
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

    public Triple() {}

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