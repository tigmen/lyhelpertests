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



    }

}
