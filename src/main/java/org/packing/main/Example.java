package org.packing.main;

import org.packing.core.Bin;
import org.packing.core.BinPacking;
import org.packing.primitives.MArea;
import org.packing.utils.Utils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by moises on 5/10/16.
 */

public class Example {

	public static void main(String[] args) throws IOException {
		Example ex = new Example();

		System.out.println("===========> Example 1 - Rectangles.txt");
		ex.launch("/Rectangles.txt");

		System.out.println("===========> Example 2 - Shapes0.txt");
		ex.launch("/Shapes0.txt");

	}

	private void launch(String fileName) throws IOException {
		InputStream in = getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Object[] result = Utils.loadPieces(reader);

		Dimension binDimension = (Dimension) result[0];
		Dimension viewPortDimension = (Dimension) result[1];
		MArea[] pieces = (MArea[]) result[2];

		Bin[] bins = BinPacking.BinPackingStrategy(pieces, binDimension, viewPortDimension);
		System.out.println("Generating bin images.........................");
		drawbinToFile(bins, viewPortDimension);
		System.out.println();
		System.out.println("Generating bin description files....................");
		createOutputFiles(bins);
		System.out.println("DONE!!!");

	}

	private void drawbinToFile(Bin[] bins, Dimension viewPortDimension) throws IOException {
		for (int i = 0; i < bins.length; i++) {

			MArea[] areasInThisbin = bins[i].getPlacedPieces();
			ArrayList<MArea> areas = new ArrayList<MArea>();
			for (MArea area : areasInThisbin) {
				areas.add(area);
			}
			Utils.drawMAreasToFile(areas, viewPortDimension, bins[i].getDimension(), ("Bin-" + String.valueOf(i + 1)));
			System.out.println("Generated image for bin " + String.valueOf(i + 1));
		}
	}

	private void createOutputFiles(Bin[] bins) throws IOException {
		for (int i = 0; i < bins.length; i++) {
			PrintWriter writer = new PrintWriter("Bin-" + String.valueOf(i + 1) + ".txt", "UTF-8");
			writer.println(bins[i].getPlacedPieces().length);
			MArea[] areasInThisbin = bins[i].getPlacedPieces();
			for (MArea area : areasInThisbin) {
				double offsetX = area.getBoundingBox2D().getX();
				double offsetY = area.getBoundingBox2D().getY();
				writer.println(area.getID() + " " + area.getRotation() + " " + offsetX + "," + offsetY);
			}
			writer.close();
			System.out.println("Generated points file for bin " + String.valueOf(i + 1));
		}
	}
}
