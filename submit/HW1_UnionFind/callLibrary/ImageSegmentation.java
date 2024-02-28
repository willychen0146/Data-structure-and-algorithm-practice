class ImageSegmentation {

    private int segmentCount;
    private int largestColor;
    private int largestSize;
    private int[][] canvas;
    int rows;
    int cols;
    WeightedQuickUnionUF uf;

    public ImageSegmentation(int N, int[][] inputImage) {
        // Initialize a N-by-N image
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;
        canvas = new int[N][N];
        rows = inputImage.length;
        cols = inputImage[0].length;
        // Create a UF object to manage the segments
        uf = new WeightedQuickUnionUF(rows * cols);

        // initialize the image with pixel to memorize the original image with 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (canvas[i][j] == 0 && inputImage[i][j] != 0) {
                    canvas[i][j] = inputImage[i][j];
                }
            }
        }

        // Union pixels in the same segment
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int currentPixel = i * cols + j;

                // Check if neighboring pixels in left, right, up, down directions are the same
                if (j > 0 && inputImage[i][j] == inputImage[i][j - 1]) {
                    uf.union(currentPixel, currentPixel - 1); // Union with left pixel
                }
                if (j < cols - 1 && inputImage[i][j] == inputImage[i][j + 1]) {
                    uf.union(currentPixel, currentPixel + 1); // Union with right pixel
                }
                if (i > 0 && inputImage[i][j] == inputImage[i - 1][j]) {
                    uf.union(currentPixel, currentPixel - cols); // Union with upper pixel
                }
                if (i < rows - 1 && inputImage[i][j] == inputImage[i + 1][j]) {
                    uf.union(currentPixel, currentPixel + cols); // Union with lower pixel
                }
            }
        }
    }

    public int countDistinctSegments() {
        for (int i = 0; i < rows * cols; i++) {
            if (canvas[i / cols][i % cols] != 0 && uf.find(i) == i) { // Check if pixel is not 0 and is the root of its component
                segmentCount++;
            }
        }
        return segmentCount;
    }

    public int[] findLargestSegment() {
        int[] segmentSize = new int[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            if (canvas[i / cols][i % cols] != 0) { // Skip if pixel value is 0
                segmentSize[uf.find(i)]++;
            }
        }
        int largestSegmentSize = 0;
        int largestSegmentColor = -1;
        for (int i = 0; i < rows * cols; i++) {
            if (segmentSize[i] > largestSegmentSize && canvas[i / cols][i % cols] != 0) { // Skip if pixel value is 0
                largestSegmentSize = segmentSize[i];
                largestSegmentColor = canvas[i / cols][i % cols];
            }
            else if (segmentSize[i] == largestSegmentSize && canvas[i / cols][i % cols] != 0) { // Skip if pixel value is 0
                largestSegmentColor = Math.min(largestSegmentColor, canvas[i / cols][i % cols]); // Choose the color with the smallest value
            }
        }
        largestSize = largestSegmentSize;
        largestColor = largestSegmentColor;
        return new int[]{largestSize, largestColor};
    }

    public static void main(String args[]) {

        // // Example 1:
        // int[][] inputImage1 = {
        //     {0, 0, 0},
        //     {0, 1, 1},
        //     {0, 0, 1}
        // };

        // System.out.println("Example 1:");

        // ImageSegmentation s = new ImageSegmentation(3, inputImage1);
        // System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        // int[] largest = s.findLargestSegment();
        // System.out.println("Size of the Largest Segment: " + largest[0]);
        // System.out.println("Color of the Largest Segment: " + largest[1]);


        // // Example 2:
        // int[][] inputImage2 = {
        //        {0, 0, 0, 3, 0},
        //        {0, 2, 3, 3, 0},
        //        {1, 2, 2, 0, 0},
        //        {1, 2, 2, 1, 1},
        //        {0, 0, 1, 1, 1}
        // };

        // System.out.println("\nExample 2:");

        // s = new ImageSegmentation(5, inputImage2);
        // System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        // largest = s.findLargestSegment();
        // System.out.println("Size of the Largest Segment: " + largest[0]);
        // System.out.println("Color of the Largest Segment: " + largest[1]);

    }
}