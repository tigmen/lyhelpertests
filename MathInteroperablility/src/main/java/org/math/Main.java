package org.math;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Integer[][] middleman = {
                {1,0,1},
                {0,1,1},
                {1,2,4}};
        Integer[] adaptive = {4,5,17};
        Matrix matrix = new Matrix(middleman);
        for(int i = 0; i < matrix.getMatrix().length; i++) System.out.println(Arrays.toString(matrix.getMatrix()[i]));
        System.out.println("matrix determinant: " + matrix.getMatrixdeterminant());
        Algorithm.MatrixMethods matrixMethods = new Algorithm.MatrixMethods();
        System.out.println(matrixMethods.KramersMethod(matrix,adaptive,matrix.getMatrixdeterminant()));
    }
}