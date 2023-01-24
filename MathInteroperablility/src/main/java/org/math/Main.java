package org.math;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Integer[][] middleman = {
                {1,11,3,4},
                {5,6,-2,8},
                {4,6,5,-1},
                {4,3,8,7}};
        Matrix matrix = new Matrix(middleman);
        for(int i = 0; i < matrix.getMatrix().length; i++) System.out.println(Arrays.toString(matrix.getMatrix()[i]));
        System.out.println("matrix determinant: " + matrix.getMatrixdeterminant());
    }
}