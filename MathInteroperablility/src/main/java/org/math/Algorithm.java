package org.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    public static class MatrixMethods
    {
        public MatrixMethods() {
        }

        public List<Integer> KramersMethod(Matrix inputmatrix, Integer[] intarg, int maindeterminant)
        {
            List<Integer> result = new ArrayList<>();

            for(int i = 0; i < inputmatrix.getMatrix().length; i++)
            {
                Integer[][] middleman = new Integer[inputmatrix.getMatrix().length][inputmatrix.getMatrix().length];
                for(int x = 0; x < inputmatrix.getMatrix().length;x++)
                    for(int y = 0; y < inputmatrix.getMatrix().length;y++)
                        middleman[x][y]=inputmatrix.getMatrixElement(x,y);
                for(int j = 0; j < middleman.length; j++)
                {
                    middleman[j][i] = intarg[j];
                }
                Matrix closenessmatrix = new Matrix(middleman);
                result.add((closenessmatrix.getMatrixdeterminant()/(maindeterminant)));
            }

            return result;
        }

        public List<Integer> GausMethod(Matrix inputmatrix, Integer[] intarg)
        {
            List<Integer> result = new ArrayList<>();
            Integer[][] midMatrix = new Integer[inputmatrix.getMatrix().length][inputmatrix.getMatrix().length];
            Integer[] midArg = new Integer[intarg.length];
            Integer jx = 0;
            //first
            for(int j = 1; j < inputmatrix.getMatrix().length; j++){
                if(j == 0){
                    jx = 0;
                    midArg[0] = intarg[0];
                }
                else{
                    jx = j-1;
                    midArg[j] = intarg[j]*inputmatrix.getMatrixElement(jx, j) - intarg[j]*inputmatrix.getMatrixElement(jx, j);
                }
                for(int i = 0; i < inputmatrix.getMatrix().length; i++) {
                    if (j != 0){
                        midMatrix[i][j] = inputmatrix.getMatrixElement(i, jx) * inputmatrix.getMatrixElement(jx, j) - inputmatrix.getMatrixElement(i, j) * inputmatrix.getMatrixElement(jx, jx);
                    }
                    else{
                        midMatrix[i][0] = inputmatrix.getMatrixElement(i, 0);
                    }
                }

            }

            for (int i = inputmatrix.getMatrix().length - 1; i >= 0; i--)
            {
                if (i == inputmatrix.getMatrix().length - 1){
                    continue;
                }
                else{
                    for(int j = inputmatrix.getMatrix().length - 1; j > i; j--){
                        midArg[i] -= result.get(j)*midMatrix[j][i];
                    }
                }
                result.add(i, midMatrix[i][i]/midArg[i]);
            }

            return result;
        }

    }

}
