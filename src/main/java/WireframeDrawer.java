/*
Delegated by repaint() on a JPanel. This will draw a Wireframe instance onto a Graphics2D object.

Antialiasing will be applied according to the setting of the boolean parameter
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WireframeDrawer {

    static double scalingFactor = 1.0;

    static class Tri { // build a Tri class to save the Triangle index and avgDepth for libraries of sorting
        int index;
        double avgDepth;

        Tri(int index, double avgDepth){
            this.index = index;
            this.avgDepth = avgDepth;
        }
    }

    public static void draw(Graphics2D g2, Wireframe wired, boolean antiAlias) {
        wired.oriDataCopy(); // create a copy for restore transformation
        wired.toView(Main.getTransform().tmx, scalingFactor); // update the wire data


        if (antiAlias){
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        List<Tri> drawableTriangles = new ArrayList<>();    // Arraylist for sorting triangles for Painter's Algorithm

        for (int i = 0; i < wired.numTriangles; i++) {
            if (!wired.isBackFace(i)) { // back face culling, only draw triangle which is not back facing

                // calculate the average depth of a triangle
                int v1 = wired.vtx1[i];
                int v2 = wired.vtx2[i];
                int v3 = wired.vtx3[i];

                double depth1 = wired.zv[v1];
                double depth2 = wired.zv[v2];
                double depth3 = wired.zv[v3];

                double avgDepth = (depth1+depth2+depth3) / wired.numTriangles;
                drawableTriangles.add(new Tri(i, avgDepth)); // add to the array list
            }
        }

        Collections.sort(drawableTriangles, Comparator.comparingDouble(o -> o.avgDepth)); // use the libray to sort by avg depth

        for (Tri triangle : drawableTriangles) {    // Painter's Algorithm - draw the triangles "from Far to Close"
            int i = triangle.index;
            drawTriangle(g2, wired, i);
        }

        wired.restoreOriginalData();    // restore data for next transformation
    }

    private static void drawTriangle(Graphics2D g2, Wireframe wired, int indx) {

        double x1 = wired.xv[wired.vtx1[indx]]*50; // make it bigger
        double y1 = wired.yv[wired.vtx1[indx]]*50;

        double x2 = wired.xv[wired.vtx2[indx]]*50;
        double y2 = wired.yv[wired.vtx2[indx]]*50;

        double x3 = wired.xv[wired.vtx3[indx]]*50;
        double y3 = wired.yv[wired.vtx3[indx]]*50;

        int[] xs = new int[]{(int) x1, (int) x2, (int) x3};
        int[] ys = new int[]{(int) y1, (int) y2, (int) y3};

        Polygon triangle = new Polygon(xs, ys, 3);

        g2.setColor(Color.gray);
        g2.fill(triangle); // Colour the triangle surface
        g2.setColor(Color.black);
        g2.draw(triangle);
    }
}