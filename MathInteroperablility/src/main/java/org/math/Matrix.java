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
        for(int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        int middlemanx = matrix.length;
        int middlemany = matrix.length;
        if(middlemanx == middlemany)
        {
            int result = 0;
            for(int i = 0; i < middlemany; i++)
            {
                Integer[][] temp = new Integer[middlemanx-1][middlemany-1];
                int jnumerator = 0;
                int knumerator = 0;
                for(int j  = 0; j < middlemanx; j++)
                {
                    if(j == i) continue;
                    for(int k = 0; k < middlemany; k++)
                    {
                        System.out.println(k + ","+j+" temp matrix elements: ");
                        for(int p = 0; p < matrix.length-1; p++) {
                            System.out.println(Arrays.toString(temp[p]));
                        }
                        System.out.println("knum:"+knumerator);
                        System.out.println("jnum:"+jnumerator);
                        System.out.println("cur:"+matrix[k][j]);
                        if(k == i) continue;
//                        temp[jnumerator][knumerator] = matrix[k][j];
                        temp[jnumerator][knumerator] = matrix[k][j];
                        knumerator++;
                    }
                    jnumerator++;
                }
                temp = new Integer[][]{{1, 1}, {1, 1}};
                Matrix downdegreedmatrix = new Matrix(temp);
                result+=Math.pow(-1d,i)*matrix[0][i]*downdegreedmatrix.getMatrixdeterminant();
            }
            return result;
        }
        else
        {
            System.out.println("Not interoperable");
            return null;
        }
    }
}
