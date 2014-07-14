package utils;

import geometricprimitives.MArea;
import geometricprimitives.MPointDouble;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import core.Bin;
import core.BinPacking;

/**
 * Application client. Contains the input processing and the output generation.
 * 
 * @author Moises Baly
 * 
 */
public class Client {

    public static void main(String[] args) {
	if (args.length < 1) {
	    printUsage();
	} else {
	    try {
		String command = args[0];
		launch(command);
	    } catch (IOException e) {
		System.out
			.println("An error ocurred while processing your file. Please make sure the file follows the specified format. See trace below");
		System.out
			.println("****************************TRACE***************************");
		e.printStackTrace();
		System.out
			.println("************************************************************");
		printFileSpecifications();
	    }
	}
    }

    private static void launch(String fileName) throws IOException {
	Scanner sc = new Scanner(new File(fileName));
	Dimension binDimension = new Dimension(sc.nextInt(), sc.nextInt());
	double x1 = binDimension.getWidth();
	double y1 = binDimension.getHeight();
	Dimension viewPortDimension;
	if (x1 > y1) {
	    viewPortDimension = new Dimension(1500, (int) (1500 / (x1 / y1)));
	} else {
	    viewPortDimension = new Dimension((int) (1500 / (y1 / x1)), 1500);
	}
	int N = sc.nextInt();
	sc.nextLine();
	MArea[] pieces = new MArea[N];
	int n = 0;
	while (n < N) {
	    String s = sc.nextLine();
	    String[] src = s.split("\\s+");
	    if (src[0].equalsIgnoreCase("@")) {
		// hole piece
		if (n <= 0)
		    return;

		MPointDouble[] points = new MPointDouble[src.length - 1];
		for (int j = 1; j < src.length; j++) {
		    String[] point = src[j].split(",");
		    double x = Double.valueOf(point[0]);
		    double y = Double.valueOf(point[1]);
		    points[j - 1] = new MPointDouble(x, y);
		}
		MArea outer = pieces[n - 1];
		//outer.placeInPosition(0, 0);
		MArea inner = new MArea(points, n);
		//inner.placeInPosition(0, 0);
		MArea area = new MArea(outer, inner);
		area.placeInPosition(0, 0);
		pieces[n - 1] = area;
	    } else {
		MPointDouble[] points = new MPointDouble[src.length];
		for (int j = 0; j < src.length; j++) {
		    String[] point = src[j].split(",");
		    double x = Double.valueOf(point[0]);
		    double y = Double.valueOf(point[1]);
		    points[j] = new MPointDouble(x, y);
		}
		pieces[n] = new MArea(points, n + 1);
		++n;
	    }
	}
	sc.close();
	System.out.println("");
	Bin[] bins = BinPacking.BinPackingStrategy(pieces, binDimension,
		viewPortDimension);
	System.out.println("Generating bin images.........................");
	drawbinToFile(bins, viewPortDimension);
	System.out.println();
	System.out
		.println("Generating bin description files....................");
	createOutputFiles(bins);
	System.out.println("DONE!!!");

    }

    private static void drawbinToFile(Bin[] bins, Dimension viewPortDimension)
	    throws IOException {
	for (int i = 0; i < bins.length; i++) {

	    MArea[] areasInThisbin = bins[i].getPlacedPieces();
	    ArrayList<MArea> areas = new ArrayList<MArea>();
	    for (MArea area : areasInThisbin) {
		areas.add(area);
	    }
	    Utils.drawMAreasToFile(areas, viewPortDimension,
		    bins[i].getDimension(), ("Bin-" + String.valueOf(i + 1)));
	    System.out.println("Generated image for bin "
		    + String.valueOf(i + 1));
	}
    }

    private static void createOutputFiles(Bin[] bins) throws IOException {
	for (int i = 0; i < bins.length; i++) {
	    PrintWriter writer = new PrintWriter("Bin-" + String.valueOf(i + 1)
		    + ".txt", "UTF-8");
	    writer.println(bins[i].getPlacedPieces().length);
	    MArea[] areasInThisbin = bins[i].getPlacedPieces();
	    for (MArea area : areasInThisbin) {
		double offsetX = area.getBoundingBox2D().getX();
		double offsetY = area.getBoundingBox2D().getY();
		writer.println(area.getID() + " " + area.getRotation() + " "
			+ offsetX + "," + offsetY);
	    }
	    writer.close();
	    System.out.println("Generated points file for bin "
		    + String.valueOf(i + 1));
	}
    }

    private static void printUsage() {
	System.out.println();
	System.out.println("Usage:");
	System.out.println();
	System.out.println("$java -jar 2DBinpacking.jar <file name>");
	System.out
		.println("<file name>: file describing pieces (see file structure specifications below).");
	System.out.println();
	System.out.println();
	printFileSpecifications();
    }

    private static void printFileSpecifications() {
	System.out
		.println("The input pieces file should be structured as follows: ");
	System.out
		.println("First line: 'width  height',integer bin dimensions separates by a space");
	System.out
		.println("Second line: 'number of pieces', a single integer specifying the number of pieces in this file.");
	System.out
		.println("N lines: each piece contained in a single line-> 'x0,y0 x1,y1 x2,y2 ... xn,yn'.NOTE "
			+ "THAT FIGURE POINTS IN DOUBLE FORMAT MUST BE SPECIFIED IN COUNTERCLOCKWISE ORDER USING THE CARTESIAN COORDINATE SYSTEM.");
	System.out.println();
	System.out.println("An initial example of a file could be as follows:");
	System.out.println("100 100            -> bin dimensions.");
	System.out.println("2                  -> number of pieces");
	System.out.println("0,0 4,0 4,4 0,4    -> first piece.");
	System.out.println("0,0 5,0 5,5 0,5    -> second piece.");
    }

}
