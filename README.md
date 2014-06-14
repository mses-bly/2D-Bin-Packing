2D-Bin-Packing
==============

This project aims to provide basic functionality for solving 2D bin packing problems of irregular (and regular) sets of pieces.  The 2D Bin packing problem consists in that given a set of 2D pieces with unknown form or shape, we have to place then in a series of rectangular bins minimizing the material used; in other words, place all the pieces in as few bins as possible. The problem is NP-Hard, thus there is no guaranty that an algorithm will provide an optimal solution, although we can compare different solutions in terms of how many bins an algorithm has used for a particular set of pieces. This project tries a few heuristics and approximations in order to solve the problem with a reasonable amount of computational effort, given that a brute force approach to the problem would take virtually an infinite amount of time.

The main goal of the project is to provide a starting point for developers who don’t have time to write code from scratch for this particular problem, or anybody who would like to improve, modify, rewrite or integrate the code. 

This is a work in progress, so feel free to write me to moises.baly@gmail.com for bugs, suggestions, improvements or whatever you consider is appropriate. Also, feel free to download, modify or use the code in any project you need.

**Basic use**
You can find a jar package with executable code to give you an idea of what the application does. To execute the jar file use:

```
$ java –jar 2DBinPacking.jar <input_file> 
```

input_file: This is the pieces descriptor file, it contains the bin dimensions in which to place the pieces, the number of pieces, and the points that describe the 2D contour of the pieces themselves. An example of such a file would be:

```
2000 1170                 -> bin dimensions, separated by space.
2                         -> single integer, number of pieces.
X11,Y11 X12,Y12 … X1M,Y1M -> N lines, each with the description of a piece.
X21,Y21 X22,Y22 … X1M,Y1M -> Each line contains the points separated by a single space and X,Y position of the point separated by a comma.
```

You can find some test sets under `src/test_files`. If you launch the program with the set `Shapes2.txt` the algorithm should finish rather quickly and give you an idea of what are the inputs and outputs of the program. Please note that the points that describe the figures should be given in counterclockwise order.
