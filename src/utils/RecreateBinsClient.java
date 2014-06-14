package utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import geometricprimitives.MArea;
import geometricprimitives.MPointDouble;

/**
 * This class is a client for the generation of bin images from the generated
 * Bin-*.txt given at the end of the algorithm and the original input points
 * file.
 * 
 * @author Moises Baly
 * 
 */
public class RecreateBinsClient {
    private static final String PIECES_PATH = "_your_original_points_file_path_";
    private static final String BINS_PATH = "_your_output_points_file_path_";
    private static final String OUTPUT = "_your_desired_output_directory_path_";

    private static Dimension binDimension;
    private static Dimension viewPortDimension;

    private static class BinLine {
	private int id;
	private double rotation;
	private MPointDouble position;

	public BinLine(int id, double rotation, MPointDouble position) {
	    super();
	    this.id = id;
	    this.rotation = rotation;
	    this.position = position;
	}
    }

    private static class BinFile {
	private String name;
	private int nLines;
	private ArrayList<BinLine> lines;

	public BinFile(int nLines) {
	    super();
	    this.nLines = nLines;
	    lines = new ArrayList<RecreateBinsClient.BinLine>();
	}

	public void addLine(BinLine line) {
	    this.lines.add(line);
	}

	@Override
	public String toString() {
	    System.out.println(nLines);
	    for (BinLine line : lines) {
		System.out.println(line.id + " " + line.rotation + " "
			+ line.position.toString());
	    }
	    return "nothing";

	}
    }

    public static void main(String[] args) throws Exception {
	System.out
		.println("Begin..............................................");
	MArea[] pieces = loadPieces(PIECES_PATH);
	Map<Integer, MArea> piecesMap = new HashMap<Integer, MArea>();
	for (MArea piece : pieces) {
	    piecesMap.put(piece.getID(), piece);
	}
	BinFile[] bins = loadBinResultFiles(BINS_PATH);
	// recreate bins images
	int b = 0;
	for (BinFile bin : bins) {
	    b++;
	    ArrayList<MArea> piecesInThisBin = new ArrayList<MArea>();
	    for (BinLine line : bin.lines) {
		MArea piece = piecesMap.get(line.id);
		if (piece == null)
		    throw new Exception("Piece not found in the file");
		piece.rotate(line.rotation);
		piece.placeInPosition(line.position.x, line.position.y);
		piecesInThisBin.add(piece);
		piecesMap.remove(line.id);
	    }
	    Utils.drawMAreasToFile(piecesInThisBin, viewPortDimension,
		    binDimension, (OUTPUT + bin.name));
	}
	if (!piecesMap.isEmpty()) {
	    throw new Exception(
		    "Some pieces where not present in the bin output files");
	}
	System.out
		.println("...................................................End");
    }

    private static MArea[] loadPieces(String pathToFile)
	    throws FileNotFoundException {
	Scanner sc = new Scanner(new File(pathToFile));
	binDimension = new Dimension(sc.nextInt(), sc.nextInt());
	double x1 = binDimension.getWidth();
	double y1 = binDimension.getHeight();
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
		    return null;

		MPointDouble[] points = new MPointDouble[src.length - 1];
		for (int j = 1; j < src.length; j++) {
		    String[] point = src[j].split(",");
		    double x = Double.valueOf(point[0]);
		    double y = Double.valueOf(point[1]);
		    points[j - 1] = new MPointDouble(x, y);
		}
		MArea outer = pieces[n - 1];
		outer.placeInPosition(0, 0);
		MArea inner = new MArea(points, n);
		inner.placeInPosition(0, 0);
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
	return pieces;
    }

    private static BinFile[] loadBinResultFiles(String pathToDirectory)
	    throws FileNotFoundException {
	File folder = new File(pathToDirectory);
	File[] listOfFiles = folder.listFiles();
	ArrayList<String> fileNames = new ArrayList<String>();
	for (int i = 0; i < listOfFiles.length; i++) {
	    if (listOfFiles[i].isFile()) {
		fileNames.add(listOfFiles[i].getName());
	    }
	}
	ArrayList<BinFile> binFiles = new ArrayList<BinFile>();
	for (String file : fileNames) {
	    binFiles.add(readBinFile(pathToDirectory + file, file));
	}
	return binFiles.toArray(new BinFile[0]);
    }

    private static BinFile readBinFile(String filename, String name)
	    throws FileNotFoundException {
	Scanner sc = new Scanner(new File(filename));
	int N = sc.nextInt();
	sc.nextLine();
	BinFile binFile = new BinFile(N);
	binFile.name = name;
	int n = 0;
	while (n < N) {
	    String s = sc.nextLine();
	    String[] src = s.split("\\s+");
	    int id = Integer.valueOf(src[0]);
	    double rotation = Double.valueOf(src[1]);

	    String[] point = src[2].split(",");
	    double x = Double.valueOf(point[0]);
	    double y = Double.valueOf(point[1]);
	    MPointDouble position = new MPointDouble(x, y);

	    BinLine line = new BinLine(id, rotation, position);

	    binFile.addLine(line);
	    ++n;
	}
	sc.close();
	return binFile;
    }
}
