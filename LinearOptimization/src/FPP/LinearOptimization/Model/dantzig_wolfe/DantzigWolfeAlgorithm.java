package FPP.LinearOptimization.Model.dantzig_wolfe;

public class DantzigWolfeAlgorithm {

    public static void main(String[] args) {
        Double[] targetFunction = new Double[5];
        targetFunction[1] = (double) -10;
        targetFunction[2] = (double) -10;
        targetFunction[3] = (double) -8;
        targetFunction[4] = (double) -8;

        Double[] restrictionOne = new Double[5];
        restrictionOne[0] = (double) 200;
        restrictionOne[1] = (double) 1;
        restrictionOne[2] = (double) 2;

        Double[] restrictionTwo = new Double[5];
        restrictionTwo[0] = (double) 80;
        restrictionTwo[1] = (double) 1;
        restrictionTwo[2] = (double) 0;

        Double[] restrictionThree = new Double[5];
        restrictionThree[0] = (double) 150;
        restrictionThree[3] = (double) 1;
        restrictionThree[4] = (double) 2;

        Double[] restrictionFour = new Double[5];
        restrictionFour[0] = (double) 30;
        restrictionFour[3] = (double) 1;
        restrictionFour[4] = (double) 0;

        Double[] connectingRestriction = new Double[5];
        connectingRestriction[0] = (double) 180;
        connectingRestriction[1] = (double) 1;
        connectingRestriction[2] = (double) 2;
        connectingRestriction[3] = (double) 1;
        connectingRestriction[4] = (double) 1;

        // Variablenvektoren
        Integer[] x_1 = new Integer[2];
        x_1[0] = 1;
        x_1[1] = 2;

        Integer[] x_2 = new Integer[2];
        x_2[0] = 3;
        x_2[1] = 4;

        //Zielfunktionsvektoren
        double[] c_1 = new double[2];
        c_1[0] = targetFunction[1];
        c_1[1] = targetFunction[2];

        Double[] c_2 = new Double[2];
        c_2[0] = targetFunction[3];
        c_2[1] = targetFunction[4];

        // Koeffizientenmatrizen
        Double[] h_1 = new Double[2];
        h_1[0] = connectingRestriction[1];
        h_1[1] = connectingRestriction[2];

        Double[] h_2 = new Double[2];
        h_2[0] = connectingRestriction[3];
        h_2[1] = connectingRestriction[4];

        Double d = connectingRestriction[0];

        Double[][] a_1 = new Double[2][2];
        a_1[0][0] = restrictionOne[1];
        a_1[0][1] = restrictionOne[2];
        a_1[1][0] = restrictionTwo[1];
        a_1[1][1] = restrictionTwo[2];

        Double[][] a_2 = new Double[2][2];
        a_2[0][0] = restrictionThree[1];
        a_2[0][1] = restrictionThree[2];
        a_2[1][0] = restrictionFour[1];
        a_2[1][1] = restrictionFour[2];

        int[] x11 = new int[]{0, 0};
        int[] x12 = new int[]{80, 0};
        int[] x13 = new int[]{80, 60};
        int[] x14 = new int[]{0, 100};

        int[] x21 = new int[]{0, 0};
        int[] x22 = new int[]{30, 0};
        int[] x23 = new int[]{30, 60};
        int[] x24 = new int[]{0, 75};

        double xi = 0;
        double[][] basisinverse = new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

        //Indizes Basisvariablen
        double[][] beta = new double[][]{{0, 1}, {1, 1}, {2, 1}};
        double[][] betaVector = new double[][]{{0, 0}, {0, 0}, {0, 0}};

        double[][] anfangstableau = new double[][]{{1, 0, 0, 180}, {0, 1, 0, 1}, {0, 0, 1, 1}, {0, 0, 0, 0}};

        int i = 1;

        /*
         * @TODO
         * SIMPLEX
         */

        int solutionX1 = 80;
        int solutionX2 = 60;
        int solutionF = -1400;

        /*
         * @TODO
         * Eckpunkt finden
         */

        if (solutionF < 0) {
        } else {
        	// lokales Optimum
        }

        /*
         * h berechnen
         */

        double[] h = new double[]{200, 1, 0, -1400};

        int s = 1;

        beta[0][0] = 1;
        beta[0][1] = 3;

        betaVector[0][0] = 80;
        betaVector[0][1] = 60;

        /*
         * @TODO
         * Austauschschritt möglich mit SIMPLEX library
         */

        for(int n = 0; n < 4; n++) {
        	anfangstableau[0][n] = anfangstableau[0][n] / h[0];
        }

        for(int n = 0; n < 4; n++) {
        	anfangstableau[1][n] = anfangstableau[1][n] - (h[1] * anfangstableau[0][n]);
        }

        for(int n = 0; n < 4; n++) {
        	anfangstableau[2][n] = anfangstableau[2][n] - (h[2] * anfangstableau[0][n]);
        }

        for(int n = 0; n < 4; n++) {
        	anfangstableau[3][n] = anfangstableau[3][n] - (h[3] * anfangstableau[0][n]);
        }

        System.out.println(anfangstableau[3][3]);
    }

}
