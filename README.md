2D-Bin-Packing
==============

This project aims to provide basic functionality for solving 2D bin packing problems of irregular (and regular) sets of pieces.  The 2D Bin packing problem consists of, given a set of 2D pieces with unknown form or shape, we have to place them in a series of rectangular bins minimizing the material used; in other words, place all the pieces in as few bins as possible. The problem is NP-Hard, thus there is no guaranty that an algorithm will provide an optimal solution, although we can compare different solutions in terms of how many bins an algorithm has used for a particular set of pieces. This project tries a few heuristics and approximations in order to solve the problem with a reasonable amount of computational effort, given that a brute force approach to the problem would take virtually an infinite amount of time.

The main goal of the project is to provide a starting point for developers who don’t have time to write code from scratch for this particular problem, or anybody who would like to improve, modify, rewrite or integrate the code. 

This is a work in progress, so feel free to write me to moises.baly@gmail.com for bugs, suggestions, improvements or whatever you consider is appropriate. Also, feel free to download, modify or use the code in any project you need.

A demo for the packing of a bin with irregular parts can be found clicking in the image below:
<a href="http://www.youtube.com/watch?feature=player_embedded&v=Oux4w0iFFww
" target="_blank"><img src="http://img.youtube.com/vi/Oux4w0iFFww/0.jpg" 
alt="IMAGE ALT TEXT HERE" width="300" height="200" align="center"/></a>

**Basic use**

You can find a jar package with executable code to give you an idea of what the application does. To execute the jar file use:

```
$ java –jar 2DBinPacking.jar <input_file> 
```

input_file: This is the pieces descriptor file, it contains the bin dimensions in which to place the pieces, the number of pieces, and the points that describe the 2D contour of the pieces themselves. An example of such file would be:

```
2000 1170                 -> bin dimensions, separated by space.
2                         -> single integer, number of pieces.
X11,Y11 X12,Y12 … X1M,Y1M -> N lines, each with the description of a piece.
X21,Y21 X22,Y22 … X1M,Y1M -> Each line contains the points separated by a single space and X,Y position of the point separated by a comma.
```

You can find some test sets under `src/test_files`. If you launch the program with the set `Shapes5.txt` the algorithm should finish rather quickly and give you an idea of what are the inputs and outputs of the program. Please note that the points that describe the figures should be given in counterclockwise order.

The program will produce various items as a result:

For each Bin that was used, it will output an image of that bin, with the pieces that have been placed inside. For example, `Bin-1.png`. Also, it will produce a text file that contains information that will allow reconstructing the bins later on; for our previous bin `Bin-1`.png, it will produce `Bin-1.txt`. This text files are structured as follows:

1. The first line contains the number of pieces in this Bin (N).
2. N lines follow, each one containing:
  - Piece ID (corresponds to its position in the input file).
  - Final rotation of the piece with respect to its original rotation (if no rotation was applied, 0).
  - 'X,Y' final position of the piece, specified as lower left X coordinate and lower left Y coordinate, comma separated.

An example output.txt could be:

```
7
78 0.0 0.0,436.0
81 0.0 0.0,81.0
56 90.0 1635.2214000000001,598.0
26 180.0 629.992,464.22140000000013
31 90.0 1645.9758000000002,217.20799999999997
57 90.0 324.2951300000001,445.20799999999997
99 0.0 1553.816054000008,5.0

```
The repository contains a file `Example.7z`, which contains an input file and its outputs (.png,.txt).

**Code modification**

The file structure of the project is as follows:

- src
  - core
    - Bin.java : Class that specifies a Bin object.
    - BinPacking.java : Class that executes the complete packing strategy.
    - Constants.java : Contains some constants used in the code.
  - geometricprimitives
    - MArea.java : Class that specifies a piece object.
    - MPointDouble.java : Extension of Java's Point2D.Double class.
    - MVector.java : Class that represents a simple geometric vector.
  - test_files
  - utils
    - Client.java : Execution client. Process the input and produces the outputs.
    - RecreateBinsClient.java : Client for the generation of bin images from the outputs.txt.
    - RedBlackBST.java : Red Black BST implementation from http://algs4.cs.princeton.edu/code/ (GPLv3)
    - Utils.java : Various utility functions.

Some modifications can be made in order to improve the results of the algorithm. In concrete, the class `Constants.java` contains some variables that if increased, will produce more accurate movements in the displacement of pieces during the algorithm. Also, this class contains rotation angles for which we will try to place a piece. If we extend this array, the algorithm will try to place the pieces in all defined angles. Please note that there is a tradeoff in time when modifying these parameters: increasing them will most likely improve the results, but also the computation time. For example, for the test set Shapes12.txt (220 pieces), and the standard parameters:

```
DIVE_HORIZONTAL_DISPLACEMENT_FACTOR = 3;
DX_SWEEP_FACTOR = 10;
DY_SWEEP_FACTOR = 2;
ROTATION_ANGLES = { 0, 90 };
```

We obtain 33 bins as a result. Increasing the parameters to:

```
DIVE_HORIZONTAL_DISPLACEMENT_FACTOR = 10;
DX_SWEEP_FACTOR = 10;
DY_SWEEP_FACTOR = 10;
ROTATION_ANGLES = { 0,10,20,30,40,50,60,70,80 90 };

```

We obtain 30 bins, thus obtaining a 3 bins gain, but also a significant increment in execution time. Please note that the modification of the parameters (especially rotation angles) depends of the types of pieces in your set.  It may be that your pieces are mostly oblique, so it can be interesting to include several angles. Nevertheless if, for example, you pieces are mostly rectangular shaped, it won’t make much difference the inclusion of a lot of different angles. The parameters are to use at discretion: some values that perform extremely well on a set can perform poorly on a different set with other characteristics.

**Recreating bins from output files**

The project includes a client `RecreateBinsClient.java` that recreates bin images from the algorithm’s outputs `Bin-*.txt` and the input points file. It’s a good way to have example code on the reconstruction of the bins from the output files, in case the project is integrated in a workflow. To execute from inside the project, modify the constant variables at the beginning of the class:
```
PIECES_PATH = "_your_original_points_file_path_";
BINS_PATH = "_your_directory_containing_output_points_files_path_";
OUTPUT = "_your_desired_output_directory_path_";
```

Please note that the input file must be in a different directory than the `Bin-*.txt` files.

```

PIECES_PATH = "C:Users/X/Desktop/Example/input/Shapes.txt";
BINS_PATH = "C:/Users/X/Desktop/Example/";
OUTPUT = "C:/Users/X/Desktop/Example/Output/";

```

**Documentation**

The project contains documentation (Javadoc format) under the `doc` directory. However, for improvement and extension purposes I will continue to add details on the algorithm itself, which I believe will allow developers using the code to improve the performance and results of the current  implementation.


