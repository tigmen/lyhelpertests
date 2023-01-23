package org.math;

public class Main {
    public static void main(String[] args) {
        Integer[][] middleman = {{1,2,3},{2,3,4},{3,4,5}};
        Matrix matrix = new Matrix(middleman);
        System.out.println(matrix.getMatrixdeterminant());
    }
}