class ImageSegmentation_1 {

    private int segmentCount;
    private int largestColor;
    private int largestSize;
    private int[][] canvas;
    private int[][] segCanvas;
    private boolean[][] hadSeg;
    private Node[][] nodes;
    private int[] colorType;

    public ImageSegmentation_1(int N, int[][] inputImage) {
        // Initialize a N-by-N image
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;
        canvas = new int[N][N];
        segCanvas = new int[N][N];
        hadSeg = new boolean[N][N];
        nodes = new Node[N][N];
        colorType = new int[N*N+1];

        // initialize the image with pixel
        int tempColor = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (canvas[i][j] == 0) {
                    canvas[i][j] = inputImage[i][j];
                    segCanvas[i][j] = tempColor;
                    tempColor++;
                }
            }
        }
        System.out.println("segCanvas:");
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                System.out.print(segCanvas[i][j] + " ");
            }
            System.out.println();
        }
        // union find to create new pixel map
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
                else{
                    segCanvas[i][j] = 0;
                }
            }
        }

        System.out.println("canvas:");
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                System.out.print(canvas[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("segCanvas:");
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                System.out.print(segCanvas[i][j] + " ");
            }
            System.out.println();
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
                // 其他
                else {
                    if(colorType[segCanvas[i][j]] == 0){
                        colorType[segCanvas[i][j]]++;
                        segmentCount++;
                    }
                    else{
                        colorType[segCanvas[i][j]]++;
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
        int tempSize = 0;
        int tempColor = 0;
        for(int i = 0; i < (segCanvas.length)*(segCanvas.length)+1; i++){
            System.out.println(i + ": " + colorType[i]);
            if(colorType[i] == 0){
                continue;
            }
            if(colorType[i] > tempSize){
                tempSize = colorType[i];
                tempColor = i;
            }
            // 遇到數量相等時，比較顏色大小
            else if(colorType[i] == tempSize){
                System.out.println("New: " + canvas[i/segCanvas.length-1][(i-1)%segCanvas.length]);
                System.out.println("Old: " + canvas[tempColor/segCanvas.length-1][(tempColor-1)%segCanvas.length]);
                if(canvas[i/segCanvas.length-1][(i-1)%segCanvas.length] < canvas[tempColor/segCanvas.length-1][(tempColor-1)%segCanvas.length]){
                    tempColor = i;
                    System.out.println("change to: " + tempColor);
                }
                else{
                    System.out.println("no change");
                }
            }
        }
        largestSize = tempSize;
        System.out.println("tempColor: " + tempColor);

        // find the color of the largest segment
        for(int i = 0; i < segCanvas.length; i++){
            for(int j = 0; j < segCanvas.length; j++){
                if(segCanvas[i][j] == tempColor){
                    largestColor = canvas[i][j];
                    break;
                }
            }
        }
        return new int[]{largestSize, largestColor};
    }

    public void setUniqueId(int x, int y, int[][] segCanvas, boolean[] colorType){
        int tempColor = 1;
        while(true){
            if(colorType[tempColor] == true){
                tempColor++;
            }
            else{
                segCanvas[x][y] = tempColor;
                colorType[tempColor] = true;
                break;
            }
        }
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

    public boolean find(int x, int y, int[][] canvas, int[][] segCanvas){
        int tempPixel = canvas[x][y];
        if(x - 1 >= 0 && canvas[x-1][y] == tempPixel){
            if(segCanvas[x][y] != segCanvas[x-1][y]){
                return true;
            }
        }
        else if(y - 1 >= 0 && canvas[x][y-1] == tempPixel){
            if(segCanvas[x][y] != segCanvas[x][y-1]){
                return true;
            }
        }
        else if(x + 1 < canvas.length && canvas[x+1][y] == tempPixel){
            if(segCanvas[x][y] != segCanvas[x+1][y]){
                return true;
            }
        }
        else if(y + 1 < canvas.length && canvas[x][y+1] == tempPixel){
            if(segCanvas[x][y] != segCanvas[x][y+1]){
                return true;
            }
        }
        return false;
    }

    class Node {
        private boolean head = false;
        private boolean hadCount = false;
        private int[] parent;
        private int[] self;
        private int[] next;
        private int color = -1;
        private int memberCount;

        public Node(int x, int y) {
            parent = new int[] {-1, -1};
            parent = new int[] {x, y};
            next = new int[] {-1, -1};
            memberCount = 1;
        }
        public void setNext(int x, int y){
            next = new int[] {x, y};
            memberCount++;
        }
        public void setParent(int x, int y){
            parent = new int[] {x, y};
            memberCount++;
        }
        public int count(){
            return memberCount;
        }
        public boolean isHead(){
            return head;
        }
        public boolean isCount(){
            return hadCount;
        }
        public void setHead(){
            head = true;
        }
        public void setColor(int color){
            this.color = color;
        }
        public void setHadCount(){
            hadCount = true;
        }
        public int whichColor(){
            return color;
        }
        public int[] returnParent(){
            return parent;
        }
        public boolean hadParent(){
            if(parent[0] == -1 && parent[1] == -1){
                return false;
            }
            else{
                return true;
            }
        }
    }

    // private object mergeSegment (object XXX, ...){ 
    // Maybe you can use user-defined function to
    // facilitate you implement mergeSegment method.

    public static void main(String[] args) {

        // Example 1:
        int[][] inputImage1 = {
                {0, 0, 0, 3, 0},
                {0, 2, 3, 3, 0},
                {1, 2, 2, 0, 0},
                {1, 2, 2, 1, 1},
                {0, 0, 1, 1, 1}
        };

        System.out.println("Example 1:");

        ImageSegmentation_1 s = new ImageSegmentation_1(5, inputImage1);
        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());

        int[] largest = s.findLargestSegment();
        System.out.println("Size of the Largest Segment: " + largest[0]);
        System.out.println("Color of the Largest Segment: " + largest[1]);


        // Example 2:
//        int[][] inputImage2 = {
//                {0, 0, 0, 3, 0},
//                {0, 2, 3, 3, 0},
//                {1, 2, 2, 0, 0},
//                {1, 2, 2, 1, 1},
//                {0, 0, 1, 1, 1}
//        };
//
//        System.out.println("\nExample 2:");
//
//        s = new ImageSegmentation_1(5, inputImage2);
//        System.out.println("Number of Distinct Segments: " + s.countDistinctSegments());
//
//        largest = s.findLargestSegment();
//        System.out.println("Size of the Largest Segment: " + largest[0]);
//        System.out.println("Color of the Largest Segment: " + largest[1]);
    }

}