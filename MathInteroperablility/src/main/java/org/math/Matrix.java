package org.math;

import java.util.Arrays;

public class Matrix {
    private Integer[][] matrix;
    public Matrix(Integer[][] inputmatrix) {
        this.matrix = inputmatrix;
    }

    public Integer[][] getMatrix() {
        return matrix;
    }
    public Integer getMatrixElement(int x, int y) {
        return matrix[x][y];
    }

    //название говорит само за себя
    public Integer getMatrixdeterminant()
    {
        int result = 0;
        if(matrix.length > 2) {
            int middlemanx = matrix.length;
            int middlemany = matrix.length;
            for (int i = 0; i < middlemany; i++) {
                Integer[][] temp = new Integer[middlemanx - 1][middlemany - 1];
                int jnumerator = 0;
                for (int j = 0; j < middlemanx; j++) {
                    int knumerator = 0;
                    if (j == i) continue;
                    for (int k = 1; k < middlemany; k++) {
                        temp[knumerator][jnumerator] = matrix[k][j];
                        knumerator++;
                    }
                    jnumerator++;
                }
                Matrix downdegreedmatrix = new Matrix(temp);
                result += Math.pow(-1d, i) * matrix[0][i] * downdegreedmatrix.getMatrixdeterminant();
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
