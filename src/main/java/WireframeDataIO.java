/*
Utility for reading data from the triangle mesh files.

Complete the implementation of the read() method to return a Wireframe instance from a File object

The Scanner utility is highly recommended

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class WireframeDataIO {
    public static Wireframe read(File myFile) throws FileNotFoundException { // chosen file passed as para

        Wireframe newWire = new Wireframe(); // create new instance of Wireframe which will be return back to main and wireframe drawer

        Scanner scanner = new Scanner(myFile);
        String verticesFirstLine = scanner.nextLine(); // first line of the vertices' part data is the number of vertices
        newWire.numVertices = Integer.parseInt(verticesFirstLine);  // Strint to int

        newWire.xv = new double[newWire.numVertices];
        newWire.yv = new double[newWire.numVertices];
        newWire.zv = new double[newWire.numVertices];

        for (int n = 0; n < newWire.numVertices; n++) {
            // data format: Vertices index  x y z
            String verticesData = scanner.nextLine();
            StringTokenizer strTok = new StringTokenizer(verticesData, " \t"); // cut String into different part by tab
            int i = Integer.parseInt(strTok.nextToken()); // index
            newWire.xv[i] = Double.parseDouble(strTok.nextToken()); // x
            newWire.yv[i] = Double.parseDouble(strTok.nextToken()); // y
            newWire.zv[i] = Double.parseDouble(strTok.nextToken()); // z
        }

        String trianglesFirstLine = scanner.nextLine(); // first line of the triangles' part data is the number of vertices
        newWire.numTriangles = Integer.parseInt(trianglesFirstLine);  // Strint to int

        newWire.vtx1 = new int[newWire.numTriangles];
        newWire.vtx2 = new int[newWire.numTriangles];
        newWire.vtx3 = new int[newWire.numTriangles];

        for (int n = 0; n < newWire.numTriangles; n++) {
            // data format: Triangle's index  x y z
            String triangleData = scanner.nextLine();
            StringTokenizer strTok = new StringTokenizer(triangleData, " \t") ;
            int i = Integer.parseInt(strTok.nextToken()); // index
            newWire.vtx1[i] = Integer.parseInt(strTok.nextToken()); // pt1
            newWire.vtx2[i] = Integer.parseInt(strTok.nextToken()); // pt2
            newWire.vtx3[i] = Integer.parseInt(strTok.nextToken()); // pt3
        }

        scanner.close();
        return newWire;
    }
}

