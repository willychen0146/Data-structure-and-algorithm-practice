import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ImageSegmentation_ver2 {

    private int segmentCount;
    private int largestColor;
    private int largestSize;
    private int[][] canvas;
    int rows;
    int cols;
    WeightedQuickUnionUF uf;

    public ImageSegmentation_ver2(int N, int[][] inputImage) {
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

    public static void test(String[] args){
        ImageSegmentation_ver2 s;
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

                    s = new ImageSegmentation_ver2(N, image);

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