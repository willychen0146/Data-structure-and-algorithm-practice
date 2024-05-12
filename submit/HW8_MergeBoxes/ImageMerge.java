import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import edu.princeton.cs.algs4.StdDraw;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/* Basic Interval Search Tree */
class IntervalST<Key extends Comparable<Key>, Value>{
    private Node root;

    private class Node {
        private final Key lo, hi;
        private Key max;
        private Value val;
        private int size;
        private Node left, right;


        public Node(Key lo, Key hi, Value val, int size) {
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.max = hi; // The maximum ending point of nodes in the subtree
            this.size = size; // The number of nodes in the subtree
        }
    }

    public IntervalST()
    {
        // initializes the tree if required.
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    private Key max(Key a, Key b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a.compareTo(b) > 0) ? a : b;
    }

    private void updateMaxAndSize(Node x) {
        Key leftMax = (x.left == null) ? null : x.left.max;
        Key rightMax = (x.right == null) ? null : x.right.max;
        x.max = max(x.hi, max(leftMax, rightMax));
        x.size = 1 + size(x.left) + size(x.right);
    }

    public void put(Key lo, Key hi, Value val) {
        root = put(root, lo, hi, val);
    }

    private Node put(Node x, Key lo, Key hi, Value val) {
        if (x == null) return new Node(lo, hi, val, 1);
        // first compare the starting point of the interval to see if it is less than the starting point of the current node.
        int cmp = lo.compareTo(x.lo);
        if (cmp < 0) x.left = put(x.left, lo, hi, val);
        else if (cmp > 0) x.right = put(x.right, lo, hi, val);
            // If the starting point is equal, then compare the ending point.
        else {
            if (hi.compareTo(x.hi) < 0) x.left = put(x.left, lo, hi, val);
            else if (hi.compareTo(x.hi) > 0) x.right = put(x.right, lo, hi, val);
            else x.val = val; // Update the value if the interval is identical.
        }
        updateMaxAndSize(x);
        return x;
    }

    public void delete(Key lo, Key hi) {
        root = delete(root, lo, hi);
    }

    private Node delete(Node x, Key lo, Key hi) {
        if (x == null) return null; // If the node is not found, return null.
        int cmpLo = lo.compareTo(x.lo);
        int cmpHi = hi.compareTo(x.hi);

        if (cmpLo < 0) {
            // The interval to delete is in the left subtree.
            x.left = delete(x.left, lo, hi);
        } else if (cmpLo > 0) {
            // The interval to delete is in the right subtree.
            x.right = delete(x.right, lo, hi);
        } else {
            // The lo keys match, check the hi keys.
            if (cmpHi == 0) {
                // Found the exact interval to delete.
                if (x.right == null) return x.left;
                if (x.left == null) return x.right;
                Node t = x;
                x = min(t.right); // Find the minimum node in the right subtree.
                x.right = deleteMin(t.right);
                x.left = t.left;
            } else {
                // The hi keys do not match, continue searching in both subtrees.
                x.left = delete(x.left, lo, hi);
                x.right = delete(x.right, lo, hi);
            }
        }
        updateMaxAndSize(x); // Update the max and size after potential deletion.
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        updateMaxAndSize(x);
        return x;
    }

    public List<Value> intersects(Key lo, Key hi) {
        List<Value> result = new ArrayList<>();
        intersects(root, lo, hi, result);
        return result;
    }

    private void intersects(Node x, Key lo, Key hi, List<Value> result) {
        if (x == null) return;
        // If the maximum ending point of the left subtree is greater than or equal to the starting point of the query interval,
        // then the left subtree may contain the intersecting intervals.
        boolean overlap = x.lo.compareTo(hi) <= 0 && x.hi.compareTo(lo) >= 0; // Check if the interval intersects with the query interval.
        if (overlap) result.add(x.val);
        if (x.left != null && x.left.max.compareTo(lo) >= 0) intersects(x.left, lo, hi, result);
        if (x.right != null && x.lo.compareTo(hi) <= 0) intersects(x.right, lo, hi, result);
    }

    public void print()
    {
        Node x = root;
        printTree(x);
    }

    private void printTree(Node x)
    {
        if(x == null) return;
        printTree(x.left);
        System.out.println("["+x.lo+","+x.hi+"]"+" "+x.val);
        printTree(x.right);
    }
}

/* Main ImageMerge Class */
class ImageMerge {
    private final double[][] bbs;
    private final double iou_thresh;
    private final List<Event> events;
    private final int[] parent; // For union-find
    private final int[] rank; // For union-find

    private static class Event {
        double xCoordinate;
        double[] yInterval; // [upY, downY]
        int index; // Index of the bounding box in the original array
        boolean isStart;

        public Event(double xCoordinate, double[] yInterval, int index, boolean isStart) {
            this.xCoordinate = xCoordinate;
            this.yInterval = yInterval;
            this.index = index;
            this.isStart = isStart;
        }
    }

    public ImageMerge(double[][] bbs, double iou_thresh) {
        this.bbs = bbs;
        this.iou_thresh = iou_thresh;
        this.events = new ArrayList<>();
        this.parent = new int[bbs.length];
        this.rank = new int[bbs.length];
        for (int i = 0; i < bbs.length; i++) {
            parent[i] = i; // Initially, each bounding box is its own parent
            rank[i] = 0; // Initially, each bounding box has rank 0
        }
        preprocess();
    }

    private void preprocess() {
        for (int i = 0; i < bbs.length; i++) {
            double[] box = bbs[i];
            double leftX = box[0];
            double upY = box[1];
            double rightX = box[0] + box[2];
            double downY = box[1] + box[3];
            events.add(new Event(leftX, new double[]{upY, downY}, i, true));
            events.add(new Event(rightX, new double[]{upY, downY}, i, false));
        }
    }

    private void sortEvents() {
        events.sort((e1, e2) -> {
            if (e1.xCoordinate != e2.xCoordinate) {
                return Double.compare(e1.xCoordinate, e2.xCoordinate);
            } else if (e1.isStart && !e2.isStart) {
                return -1;
            } else if (!e1.isStart && e2.isStart) {
                return 1;
            }
            return 0;
        });
    }

    private int find(int p) {
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];    // path compression by halving
            p = parent[p];
        }
        return p;
    }

    private void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make root of smaller rank point to root of larger rank
        if      (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
        else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
        else {
            parent[rootQ] = rootP;
            rank[rootP]++;
        }
    }

    private boolean isOverlap(int i, int j) {
        double[] box1 = bbs[i];
        double[] box2 = bbs[j];
        double leftX = Math.max(box1[0], box2[0]);
        double rightX = Math.min(box1[0] + box1[2], box2[0] + box2[2]);
        double upY = Math.max(box1[1], box2[1]);
        double downY = Math.min(box1[1] + box1[3], box2[1] + box2[3]);

        if (leftX < rightX && upY < downY) {
            double intersectionArea = (rightX - leftX) * (downY - upY);
            double box1Area = box1[2] * box1[3];
            double box2Area = box2[2] * box2[3];
            double iou = intersectionArea / (box1Area + box2Area - intersectionArea);
            return iou >= iou_thresh;
        }
        return false;
    }

    private void sweepLine() {
        IntervalST<Double, Integer> tree = new IntervalST<>();
        for (Event event : events) {
            if (event.isStart) {
                // Query the tree for overlapping y-intervals
                List<Integer> overlappingIndices = tree.intersects(event.yInterval[0], event.yInterval[1]);
                for (int overlappingIndex : overlappingIndices) {
                    if (isOverlap(overlappingIndex, event.index)) {
                        union(overlappingIndex, event.index);
                    }
                }
                // Insert the y-interval into the tree
                tree.put(event.yInterval[0], event.yInterval[1], event.index);
            } else {
                // Remove the y-interval from the tree
                tree.delete(event.yInterval[0], event.yInterval[1]);
            }
        }
    }

    private Map<Integer, List<Integer>> collectSets() {
        Map<Integer, List<Integer>> sets = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            sets.putIfAbsent(root, new ArrayList<>());
            sets.get(root).add(i);
        }
        return sets;
    }

    private double[][] constructMergedBoxes(Map<Integer, List<Integer>> sets) {
        List<double[]> mergedBoxes = new ArrayList<>();
        for (List<Integer> set : sets.values()) {
            double minLeftX = Double.MAX_VALUE;
            double minUpY = Double.MAX_VALUE;
            double maxRightX = Double.MIN_VALUE;
            double maxDownY = Double.MIN_VALUE;
            for (int index : set) {
                double[] box = bbs[index];
                minLeftX = Math.min(minLeftX, box[0]);
                minUpY = Math.min(minUpY, box[1]);
                maxRightX = Math.max(maxRightX, box[0] + box[2]);
                maxDownY = Math.max(maxDownY, box[1] + box[3]);
            }
            double width = maxRightX - minLeftX;
            double height = maxDownY - minUpY;
            mergedBoxes.add(new double[]{minLeftX, minUpY, width, height});
        }
        return mergedBoxes.toArray(new double[0][]);
    }

    public double[][] mergeBox() {
        sortEvents();
        sweepLine();
        Map<Integer, List<Integer>> sets = collectSets();
        double[][] mergedBoxes = constructMergedBoxes(sets);
        Arrays.sort(mergedBoxes, (a, b) -> {
            if (a[0] != b[0]) return Double.compare(a[0], b[0]);
            else if (a[1] != b[1]) return Double.compare(a[1], b[1]);
            else if (a[2] != b[2]) return Double.compare(a[2], b[2]);
            else return Double.compare(a[3], b[3]);
        });
        return mergedBoxes;
    }

    public static void draw(double[][] bbs)
    {
        // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
        // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT**
        StdDraw.setCanvasSize(960,540);
        for(double[] box : bbs)
        {
            double half_width = (box[2]/2.0);
            double half_height = (box[3]/2.0);
            double center_x = box[0]+ half_width;
            double center_y = box[1] + half_height;
            //StdDraw use y = 0 at the bottom, 1-center_y to flip
            StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
        }
    }

    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][]{
                        {0.02,0.01,0.1,0.05},{0.0,0.0,0.1,0.05},{0.04,0.02,0.1,0.05},{0.06,0.03,0.1,0.05},{0.08,0.04,0.1,0.05},
                        {0.24,0.01,0.1,0.05},{0.20,0.0,0.1,0.05},{0.28,0.02,0.1,0.05},{0.32,0.03,0.1,0.05},{0.36,0.04,0.1,0.05},
                },
                0.5
        );
        double[][] temp = sol.mergeBox();
        ImageMerge.draw(temp); // Assuming draw method is implemented elsewhere
    }
}

class OutputFormat{
    double[][] box;
    double iou;
    double[][] answer;
}

class test2{
    private static boolean deepEquals(double[][] test_ans, double[][] user_ans)
    {
        if(test_ans.length != user_ans.length)
            return false;
        for(int i = 0; i < user_ans.length; ++i)
        {
            if(user_ans[i].length != test_ans[i].length)
                return false;
            for(int j = 0; j < user_ans[i].length; ++j)
            {
                if(Math.abs(test_ans[i][j]-user_ans[i][j]) > 0.00000000001)
                    return false;
            }
        }
        return true;
    }
    public static void draw(double[][] user, double[][] test2)
    {
        StdDraw.setCanvasSize(960,540);
        for(double[] box : user)
        {
            StdDraw.setPenColor(StdDraw.BLACK);
            double half_width = (box[2]/2.0);
            double half_height = (box[3]/2.0);
            double center_x = box[0]+ half_width;
            double center_y = box[1] + half_height;
            //StdDraw use y = 0 at the bottom, 1-center_y to flip

            StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
        }
        for(double[] box : test2)
        {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
            double half_width = (box[2]/2.0);
            double half_height = (box[3]/2.0);
            double center_x = box[0]+ half_width;
            double center_y = box[1] + half_height;
            //StdDraw use y = 0 at the bottom, 1-center_y to flip

            StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
        }
    }
    public static void main(String[] args) throws InterruptedException
    {
        Gson gson = new Gson();
        OutputFormat[] datas;
        OutputFormat data;
        int num_ac = 0;

        double[][] user_ans;
        ImageMerge sol;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat[].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];
                sol = new ImageMerge(data.box, data.iou);
                user_ans = sol.mergeBox();
                System.out.print("Sample"+i+": ");
                if(deepEquals(user_ans, data.answer))
                {
                    System.out.println("AC");
                    num_ac++;
                }
                else
                {
                    System.out.println("WA");
                    System.out.println("Data:      " + "\n    iou: "+data.iou + "\n" +
                            "    box: "+Arrays.deepToString(data.box));
                    System.out.println("Test_ans:  " + Arrays.deepToString(data.answer));
                    System.out.println("User_ans:  " + Arrays.deepToString(user_ans));
                    System.out.println("");
                    draw(user_ans,data.answer);
                    Thread.sleep(5000);
                }
            }
            System.out.println("Score: "+num_ac+"/"+datas.length);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}