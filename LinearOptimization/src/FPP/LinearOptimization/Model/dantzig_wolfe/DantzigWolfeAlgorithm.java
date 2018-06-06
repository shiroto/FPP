package FPP.LinearOptimization.Model.dantzig_wolfe;

import de.lip.bb.Simplex;

public class DantzigWolfeAlgorithm {
    static double[] targetFunction = new double[5];
    static double[] restrictionOne = new double[5];
    static double[] restrictionTwo = new double[5];
    static double[] restrictionThree = new double[5];
    static double[] restrictionFour = new double[5];
    static double[] connectingRestriction = new double[5];

    static int[] x_1 = new int[2];
    static int[] x_2 = new int[2];

    static double[] c_1 = new double[2];
    static double[] c_2 = new double[2];

    static double[] h_1 = new double[2];
    static double[] h_2 = new double[2];

    static double d;

    static double[][] a_1 = new double[2][2];
    static double[][] a_2 = new double[2][2];

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        initialise();

        /*
         * @TODO
         * Eckpunkte
         */
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
         * SIMPLEX
         */
//        Double[][] tableau = new Double[][]{{1d, 2d, 200d}, {1d, 0d, 80d}, {10d, 10d, 0d}};
//        Simplex simplex = new Simplex(tableau, false);
//        Double[] loesung = simplex.loese();
//
//        Double solutionX1 = loesung[0];
//        Double solutionX2 = loesung[1];
//        Double solutionF = loesung[2];

        Double solutionX1 = (double) 80;
        Double solutionX2 = (double) 60;
        Double solutionF = (double) -1400;

        if (solutionF < 0) {
        } else {
            // lokales Optimum
        }

        /*
         * @TODO
         * h berechnen
         */
        double[] h = new double[]{200, 1, 0, -1400};

        int s = 1;

        beta[0][0] = 1;
        beta[0][1] = 3;

        betaVector[0][0] = 80;
        betaVector[0][1] = 60;

        /*
         * Austauschschritt
         */
        anfangstableau = austauschschritt(anfangstableau, h);

        System.out.println(anfangstableau[3][3]);
    }

    private static double[][] austauschschritt(double[][] x, double[] h){

        for(int n = 0; n < 4; n++) {
            x[0][n] = x[0][n] / h[0];
        }

        for(int n = 0; n < 4; n++) {
            x[1][n] = x[1][n] - (h[1] * x[0][n]);
        }

        for(int n = 0; n < 4; n++) {
            x[2][n] = x[2][n] - (h[2] * x[0][n]);
        }

        for(int n = 0; n < 4; n++) {
            x[3][n] = x[3][n] - (h[3] * x[0][n]);
        }

		return x;
    }

    private static void initialise() {
        /*
         * Übergabeparameter!
         */
        targetFunction[1] = (double) -10;
        targetFunction[2] = (double) -10;
        targetFunction[3] = (double) -8;
        targetFunction[4] = (double) -8;

        restrictionOne[0] = (double) 200;
        restrictionOne[1] = (double) 1;
        restrictionOne[2] = (double) 2;

        restrictionTwo[0] = (double) 80;
        restrictionTwo[1] = (double) 1;
        restrictionTwo[2] = (double) 0;

        restrictionThree[0] = (double) 150;
        restrictionThree[3] = (double) 1;
        restrictionThree[4] = (double) 2;

        restrictionFour[0] = (double) 30;
        restrictionFour[3] = (double) 1;
        restrictionFour[4] = (double) 0;

        connectingRestriction[0] = (double) 180;
        connectingRestriction[1] = (double) 1;
        connectingRestriction[2] = (double) 2;
        connectingRestriction[3] = (double) 1;
        connectingRestriction[4] = (double) 1;
        /*
         * Ende Übergabeparameter
         */


        // Variablenvektoren
        x_1[0] = 1;
        x_1[1] = 2;

        x_2[0] = 3;
        x_2[1] = 4;

        //Zielfunktionsvektoren
        c_1[0] = targetFunction[1];
        c_1[1] = targetFunction[2];

        c_2[0] = targetFunction[3];
        c_2[1] = targetFunction[4];

        // Koeffizientenmatrizen
        h_1[0] = connectingRestriction[1];
        h_1[1] = connectingRestriction[2];

        h_2[0] = connectingRestriction[3];
        h_2[1] = connectingRestriction[4];

        d = connectingRestriction[0];

        a_1[0][0] = restrictionOne[1];
        a_1[0][1] = restrictionOne[2];
        a_1[1][0] = restrictionTwo[1];
        a_1[1][1] = restrictionTwo[2];

        a_2[0][0] = restrictionThree[1];
        a_2[0][1] = restrictionThree[2];
        a_2[1][0] = restrictionFour[1];
        a_2[1][1] = restrictionFour[2];
    }

}
