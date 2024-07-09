/* Class to hold wireframe data

  COMPLETE THE IMPLEMENTATION OF THIS MODULE

  You can add constructors, data, and any methods as you see fit

 */

import java.util.Arrays;

public class Wireframe {

    public int numTriangles = 0;
    public int numVertices = 0;

    // Vertex coordinates in the world scene for each vertex of the triangle
    public double[] xv, yv, zv;

    // Triangle mapping
    public int[] vtx1, vtx2, vtx3;

    private double[] oriXv, oriYv, oriZv;

    public void oriDataCopy(){
        oriXv = Arrays.copyOf(xv, numVertices);
        oriYv = Arrays.copyOf(yv, numVertices);
        oriZv = Arrays.copyOf(zv, numVertices);
    }

    // Here we apply the rotations and scaling before drawing the display
    public void toView(double[][] tmx, double scalingFactor) {

        for (int i = 0; i < numVertices; i++) {

            double x, y, z;

            // rotation
            x = tmx[0][0]*xv[i] + tmx[0][1]*yv[i] + tmx[0][2]*zv[i];
            y = tmx[1][0]*xv[i] + tmx[1][1]*yv[i] + tmx[1][2]*zv[i];
            z = tmx[2][0]*xv[i] + tmx[2][1]*yv[i] + tmx[2][2]*zv[i];

            // scaling
            x *= scalingFactor;
            y *= scalingFactor;
            z *= scalingFactor;

            // update
            xv[i] = x;
            yv[i] = y;
            zv[i] = z;
        }
    }

    public boolean isBackFace(int i) {

        int v1 = vtx1[i];
        int v2 = vtx2[i];
        int v3 = vtx3[i];

        double ax = xv[v2] - xv[v1];    // x from v2 to v1
        double ay = yv[v2] - yv[v1];    // y from v2 to v1
        double bx = xv[v3] - xv[v1];    // x from v3 to v1
        double by = yv[v3] - yv[v1];    // y from v3 to v1

        // cross product(2D) z = Ax * By - Bx * Ay
        double crossProduct = ax*by - ay*bx;

        // as crossProduct gives a vector which is perpendicular to both the vectors being multiplied (i.e z),
        // negative crossProduct = back facing
        return crossProduct < 0;
    }

    public void restoreOriginalData() {
        // Restore the original data from the stored copies
        System.arraycopy(oriXv, 0, xv, 0, numVertices);
        System.arraycopy(oriYv, 0, yv, 0, numVertices);
        System.arraycopy(oriZv, 0, zv, 0, numVertices);
    }
}
