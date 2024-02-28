class ImageSegmentation {

    private int segmentCount;
    private int largestColor;
    private int largestSize;
    private int[][] canvas;
    private int[][] segCanvas;
    private int[] newColorId;

    public ImageSegmentation(int N, int[][] inputImage) {
        // Initialize a N-by-N image and new segmentation map to store the new segment id
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;
        canvas = new int[N][N];
        segCanvas = new int[N][N];
        newColorId = new int[N*N+1];

        // initialize the image with pixel and new segmentation map
        int tempColor = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                segCanvas[i][j] = tempColor;
                tempColor++;
                if (canvas[i][j] == 0 && inputImage[i][j] != 0) {
                    canvas[i][j] = inputImage[i][j];
                }
            }
        }

        // union find to create new segmentation map
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                // if pixel != 0
                if(canvas[i][j] != 0) {
                    // pixel at [0, 0]
                    if(i == 0 && j == 0){
                        continue;
                    }

                    // union the pixel with same color
                    union(i, j, canvas, segCanvas);
                }
                // if pixel == 0, 補0
                else{
                    segCanvas[i][j] = 0;
                }
            }
        }
    }

    public int countDistinctSegments() {
        // Count the number of distinct segments in the image.
        for(int i = 0; i < segCanvas.length; i++){
            for(int j = 0; j < segCanvas.length; j++){
                // 遇到背景值跳過
                if(segCanvas[i][j] == 0){
                    continue;
                }
                // 有數值的地方
                else {
                    // 如果是第一次遇到該數值，Id數量++, segmentCount++
                    if(newColorId[segCanvas[i][j]] == 0){
                        newColorId[segCanvas[i][j]]++;
                        segmentCount++;
                    }
                    // 如果不是第一次遇到該數值，Id數量++
                    else{
                        newColorId[segCanvas[i][j]]++;
                    }
                }
            }
        }
        return segmentCount;
    }

    public int[] findLargestSegment() {
        // Find the largest connected segment and return an array
        // containing the number of pixels and the color of the segment.
        // find the largest segment

        // 找出最大的segment
        int tempLargestsize = 0;
        int tempLargestColor = 0;
        for(int i = 1; i < ((segCanvas.length)*(segCanvas.length)+1); i++){
            // 遇到此顏色數量破紀錄時，更新
            if(newColorId[i] > tempLargestsize){
                tempLargestsize = newColorId[i];
                tempLargestColor = i;
            }
            // 遇到數量相等時，比較顏色大小
            else if(newColorId[i] == tempLargestsize && tempLargestsize != 0){
                int x = i/segCanvas.length;
                int y = i%segCanvas.length-1;
                if(y == -1){
                    y = segCanvas.length-1;
                }
                int nx = tempLargestColor/segCanvas.length;
                int ny = tempLargestColor%segCanvas.length-1;
                if(ny == -1){
                    ny = segCanvas.length-1;
                }
                if(canvas[x][y] < canvas[nx][ny]){
                    tempLargestColor = i;
                }
            }
        }
        largestSize = tempLargestsize;

        // 利用tempLargestColor找出最大的segment的顏色
        for(int i = 0; i < segCanvas.length; i++){
            for(int j = 0; j < segCanvas.length; j++){
                if(segCanvas[i][j] == tempLargestColor){
                    largestColor = canvas[i][j];
                    break;
                }
            }
        }
        return new int[]{largestSize, largestColor};
    }

    public void union(int x, int y, int[][] canvas, int[][] segCanvas){
        int tempPixel = canvas[x][y];
        if(x - 1 >= 0 && canvas[x-1][y] == tempPixel){
            segCanvas[x][y] = segCanvas[x-1][y];
        }
        else if(y - 1 >= 0 && canvas[x][y-1] == tempPixel){
            segCanvas[x][y] = segCanvas[x][y-1];
        }
        else if(x + 1 < canvas.length && canvas[x+1][y] == tempPixel){
            segCanvas[x][y] = segCanvas[x+1][y];
        }
        else if(y + 1 < canvas.length && canvas[x][y+1] == tempPixel){
            segCanvas[x][y] = segCanvas[x][y+1];
        }
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