package org.math;

import java.util.Arrays;

public class Matrix {
    private final Integer[][] matrix;
    public Matrix(Integer[][] matrix) {
        this.matrix = matrix;
    }

    public Integer[][] getMatrix() {
        return matrix;
    }
    public Integer getMatrixElement(int x, int y) {
        return matrix[x][y];
    }

    public Integer getMatrixdeterminant()
    {
        System.out.println("matrix elements: ");
        int result = 0;
        if(matrix.length > 2) {
            for (int i = 0; i < matrix.length; i++) {
                System.out.println(Arrays.toString(matrix[i]));
            }
            int middlemanx = matrix.length;
            int middlemany = matrix.length;
            System.out.println(matrix.length);
            for (int i = 0; i < middlemany; i++) {
                Integer[][] temp = new Integer[middlemanx - 1][middlemany - 1];
                int jnumerator = 0;
                for (int j = 0; j < middlemanx; j++) {
                    int knumerator = 0;
                    System.out.println("J");
                    System.out.println("knum:" + knumerator);
                    System.out.println("jnum:" + jnumerator);
                    System.out.println("j:" + j);
                    if (j == i) continue;
                    for (int k = 0; k < middlemany; k++) {
                        System.out.println("K");
                        System.out.println("k:" + k);
                        System.out.println("j:" + j);
                        System.out.println("knum:" + knumerator);
                        System.out.println("jnum:" + jnumerator);
                        System.out.println("cur:" + matrix[k][j]);
                        if (k == i) continue;
//                        temp[jnumerator][knumerator] = matrix[k][j];
                        temp[knumerator][jnumerator] = matrix[k][j];
                        System.out.println(k + "," + j + " temp matrix elements: ");
                        for (int p = 0; p < matrix.length - 1; p++) {
                            System.out.println(Arrays.toString(temp[p]));
                        }
                        knumerator++;
                    }
                    jnumerator++;
                }
                Matrix downdegreedmatrix = new Matrix(temp);
                System.out.println(i + "result:");
                System.out.println(Math.pow(-1d, i));
                System.out.println(matrix[0][i]);
                result += Math.pow(-1d, i) * matrix[0][i] * downdegreedmatrix.getMatrixdeterminant();

                System.out.println("matrix determinant: " + result);
            }
        }
        else if(matrix.length == 2)
        {
            result = matrix[0][0] * matrix[1][1] - matrix[0][1]*matrix[1][0];
        }
        else result = matrix[0][0];
        return result;
    }
}
