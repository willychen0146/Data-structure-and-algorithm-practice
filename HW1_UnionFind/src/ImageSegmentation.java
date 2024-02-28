import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ImageSegmentation {

    private int segmentCount;
    private int largestColor;
    private int largestSize;
    private int[][] canvas;
    private int[][] segCanvas;
    private int[] newColorId;
    private int[] parent; // parent array for union find

    public ImageSegmentation(int N, int[][] inputImage) {
        // Initialize a N-by-N image and new segmentation map to store the new segment id
        segmentCount = 0;
        largestColor = 0;
        largestSize = 0;
        canvas = new int[N][N];
        segCanvas = new int[N][N];
        newColorId = new int[N*N+1];
        parent = new int[N * N]; // Initialize parent array for union find

        // Initialize parent array such that each pixel is its own parent
        for (int i = 0; i < N * N; i++) {
            parent[i] = i;
        }

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
//        System.out.println("segCanvas:");
//        for(int i = 0; i < N; i++){
//            for(int j = 0; j < N; j++){
//                System.out.print(segCanvas[i][j] + " ");
//            }
//            System.out.println();
//        }

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
//        System.out.println("parent:");
//        for(int i = 0; i < N*N; i++){
//            System.out.print(parent[i] + " ");
//        }
//        System.out.println("canvas:");
//        for(int i = 0; i < N; i++){
//            for(int j = 0; j < N; j++){
//                System.out.print(canvas[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("segCanvas:");
//        for(int i = 0; i < N; i++){
//            for(int j = 0; j < N; j++){
//                System.out.print(segCanvas[i][j] + " ");
//            }
//            System.out.println();
//        }
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
//            System.out.println(i + ": " + newColorId[i]);
            // 遇到此顏色數量破紀錄時，更新
            if(newColorId[i] > tempLargestsize){
                tempLargestsize = newColorId[i];
                tempLargestColor = i;
            }
            // 遇到數量相等時，比較顏色大小
            else if(newColorId[i] == tempLargestsize && tempLargestsize != 0){
//                System.out.println("i: " + i);
//                System.out.println("tempLargestColor: " + tempLargestColor);
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
//                System.out.println("x: " + x + " y: " + y);
//                System.out.println("nx: " + nx + " ny: " + ny);
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

    ////////
//    public int countDistinctSegments() {
//        boolean[] distinct = new boolean[canvas.length * canvas[0].length];
//        for (int i = 0; i < segCanvas.length; i++) {
//            for (int j = 0; j < segCanvas[0].length; j++) {
//                if (segCanvas[i][j] != 0 && !distinct[segCanvas[i][j]]) {
//                    distinct[segCanvas[i][j]] = true;
//                    segmentCount++;
//                }
//            }
//        }
//        System.out.println("segmentCount: " + segmentCount);
//        return segmentCount;
//    }

//    public int[] findLargestSegment() {
//        int[] segmentSizes = new int[canvas.length * canvas[0].length + 1];
//        int maxSegmentSize = 0;
//        for (int i = 0; i < segCanvas.length; i++) {
//            for (int j = 0; j < segCanvas[0].length; j++) {
//                segmentSizes[segCanvas[i][j]]++;
//                if (segmentSizes[segCanvas[i][j]] > maxSegmentSize) {
//                    maxSegmentSize = segmentSizes[segCanvas[i][j]];
//                }
//            }
//        }
//        int[] largestSegment = new int[maxSegmentSize];
//        int count = 0;
//        for (int i = 0; i < segmentSizes.length; i++) {
//            if (segmentSizes[i] == maxSegmentSize) {
//                largestSegment[count] = i;
//                count++;
//            }
//        }
//        return largestSegment;
//    }
    ////////
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

//    public void union(int x, int y, int[][] canvas, int[][] segCanvas) {
//        int[] dx = {-1, 0, 1, 0};
//        int[] dy = {0, -1, 0, 1};
//        int n = canvas.length;
//
//        int tempPixel = canvas[x][y];
//        int root = x * n + y;
////        System.out.println("root: " + root);
//
//        for (int i = 0; i < 4; i++) {
//            int nx = x + dx[i];
//            int ny = y + dy[i];
//
//            if (nx >= 0 && nx < n && ny >= 0 && ny < n && canvas[nx][ny] == tempPixel) {
//                int neighborRoot = find(nx, ny, parent, n);
//                parent[neighborRoot] = root;
//                return;
//            }
//        }
//        parent[root] = root;
//    }
//
//    // Find operation with path compression
//    public int find(int x, int y, int[] parent, int n) {
//        int root = x * n + y;
//        if (parent[root] != root) {
//            parent[root] = find(parent[root] / n, parent[root] % n, parent, n); // Path compression
//        }
//        return parent[root];
//    }

    // private object mergeSegment (object XXX, ...){ 
    // Maybe you can use user-defined function to
    // facilitate you implement mergeSegment method.
    // }

    public static void test(String[] args){
        ImageSegmentation s;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(args[0])){
            JSONArray all = (JSONArray) jsonParser.parse(reader);
            int count = 0;
            for(Object CaseInList : all){
                count++;
                JSONObject aCase = (JSONObject) CaseInList;
                JSONArray dataArray = (JSONArray) aCase.get("data");

                // JSONObject data = (JSONObject) aCase.get("data");
                // JSONArray dataArray = (JSONArray) data.get("data");

                int testSize = 0; int waSize = 0;
                System.out.print("Case ");
                System.out.println(count);
                for (Object dataObject : dataArray) {
                    JSONObject dataDetails = (JSONObject) dataObject;
                    int N = ((Long) dataDetails.get("N")).intValue();
                    JSONArray imageArray = (JSONArray) dataDetails.get("image");

                    int[][] image = new int[imageArray.size()][];
                    for (int i = 0; i < imageArray.size(); i++) {
                        JSONArray row = (JSONArray) imageArray.get(i);
                        image[i] = new int[row.size()];
                        for (int j = 0; j < row.size(); j++) {
                            image[i][j] = ((Long) row.get(j)).intValue();
                        }
                    }
                    // System.out.println("N: " + N);
                    // System.out.println("Image: " + Arrays.deepToString(image));

                    s = new ImageSegmentation(N, image);

                    int distinctSegments = ((Long) dataDetails.get("DistinctSegments")).intValue();

                    JSONArray largestSegmentArray = (JSONArray) dataDetails.get("LargestSegment");
                    int largestColor = ((Long) largestSegmentArray.get(0)).intValue();
                    int largestSize = ((Long) largestSegmentArray.get(1)).intValue();

                    int ans1 = s.countDistinctSegments();
                    int ans2 = s.findLargestSegment()[0];
                    int ans3 = s.findLargestSegment()[1];

                    testSize++;
                    if(ans1==distinctSegments && ans2==largestColor && ans3==largestSize){
                         System.out.println("AC");

                    }else{
                        waSize++;
                         System.out.println("WA");
                         System.out.println("ans1: " + ans1 + " ans2: " + ans2 + " ans3: " + ans3);
                            System.out.println("distinctSegments: " + distinctSegments + " largestColor: " + largestColor + " largestSize: " + largestSize);
                    }
                }
                System.out.println("Score: " + (testSize-waSize) + " / " + testSize + " ");

            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static int[] JSONArraytoIntArray(JSONArray x){
        int sizeLim = x.size();
        int MyInt[] = new int[sizeLim];
        for(int i=0;i<sizeLim;i++){
            MyInt[i]= Integer.parseInt(x.get(i).toString());
        }
        return MyInt;
    }

    public static void main(String[] args) {
        test(args);
    }
}