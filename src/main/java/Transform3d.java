public class Transform3d {
    private static final int SIZE = 3;

    // Transformation matrices
    private final double[][] tXY = new double[SIZE][SIZE];
    private final double[][] tYZ = new double[SIZE][SIZE];
    private final double[][] tXZ = new double[SIZE][SIZE];
    private final double[][] temp = new double[SIZE][SIZE];

    // Transformation matrix that is visible to users of the class
    public double[][] tmx = new double[SIZE][SIZE];

    private static void setIdentity(double[][] mx) {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                mx[i][j] = 0.0;
            }
            mx[i][i] = 1.0;
        }
    }

    private static void multiply(double[][] mx1, double[][] mx2, double[][] result) {
        for (int i=0; i<mx1.length; i++) { //row size of mx1 i.e 3
            for (int j=0; j<mx2[0].length; j++) { //col size of mx2 i.e 3
                result[i][j] = 0.0; // initialize(reset) to 0.0
                for (int k=0; k<mx1[0].length; k++) { //col size of mx1 i.e 3
                    result[i][j] += mx1[i][k] * mx2[k][j];
                }
            }
        }
    }

    public Transform3d() {
        setIdentity(tXY);
        setIdentity(tXZ);
        setIdentity(tYZ);
        calculate();
    }

    void setXY(double angle) {
        tXY[0][0] = Math.cos(angle);
        tXY[0][1] = -(Math.sin(angle));
        tXY[1][0] = Math.sin(angle);
        tXY[1][1] = Math.cos(angle);
        calculate();
    }

    void setXZ(double angle) {
        tXZ[0][0] = Math.cos(angle);
        tXZ[0][2] = Math.sin(angle);
        tXZ[2][0] = -(Math.sin(angle));
        tXZ[2][2] = Math.cos(angle);
        calculate();
    }

    void setYZ(double angle) {
        tYZ[1][1] = Math.cos(angle);
        tYZ[1][2] = -(Math.sin(angle));
        tYZ[2][1] = Math.sin(angle);
        tYZ[2][2] = Math.cos(angle);
        calculate();
    }

    // Compute full transformation matrix from the 3 rotation matrices
    void calculate() {
        setIdentity(tmx);
        setIdentity(temp);
        multiply(temp,tXY,tmx);
        multiply(tmx,tYZ,temp);
        multiply(temp,tXZ,tmx);
    }
}
