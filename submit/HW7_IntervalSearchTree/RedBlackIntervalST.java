import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import com.google.gson.*;

import edu.princeton.cs.algs4.In;

// RED-BLACK TREE
class RedBlackIntervalST<Key extends Comparable<Key>, Value>{
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private class Node {
        private Key lo, hi, max;
        private Value val;
        private int size;
        private Node left, right;
        boolean color; // Node color for the red-black tree


        public Node(Key lo, Key hi, Value val, int size, boolean color) {
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.max = hi; // The maximum ending point of nodes in the subtree
            this.size = size; // The number of nodes in the subtree
            this.color = color;
        }
    }

    public RedBlackIntervalST()
    {
        // initializes the tree if required.
    }

    /***************************************************************************
     *  Node helper function
     ***************************************************************************/
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /***************************************************************************
     *  Red-black tree balance function
     ***************************************************************************/
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        updateMaxAndSize(h); // Update max and size of h
        updateMaxAndSize(x); // Update max and size of x
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        updateMaxAndSize(h); // Update max and size of h
        updateMaxAndSize(x); // Update max and size of x
        return x;
    }

    // Not finish
    private void flipColors(Node h) {
        assert !isRed(h);
        assert isRed(h.left);
        assert isRed(h.right);
        if (h == null || h.left == null || h.right == null) {
            return;
        }
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    // Not finish
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Not finish
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        updateMaxAndSize(h);
        return h;
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

    /***************************************************************************
     *  Red-black tree operation
     ***************************************************************************/
    public void put(Key lo, Key hi, Value val) {
        root = put(root, lo, hi, val);
        root.color = BLACK;
    }

    private Node put(Node x, Key lo, Key hi, Value val) {
        if (x == null) return new Node(lo, hi, val, 1, RED);
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
        return balance(x);
    }

    // Not finish
    public void delete(Key lo, Key hi) {
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = delete(root, lo, hi);
        if (root != null) root.color = BLACK;
    }

    // Not finish
    private Node delete(Node x, Key lo, Key hi) {
        if (x == null) return null; // If the node is not found, return null.

        int cmpLo = lo.compareTo(x.lo);
        int cmpHi = hi.compareTo(x.hi);

        if (cmpLo < 0) {
            // The interval to delete is in the left subtree.
            if (x.left != null) {
                if (!isRed(x.left) && !isRed(x.left.left))
                    x = moveRedLeft(x);
                x.left = delete(x.left, lo, hi);
            }
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
        return balance(x);
    }

    // Not finish
    private Node min(Node x) {
        if (x == null) return null; // Handle the case when x is null
        if (x.left == null) return x; // If left child is null, return the current node
        else return min(x.left); // Otherwise, recursively find the minimum in the left subtree
    }

    // Not finish
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right; // If left child is null, return the right child

        if (!isRed(x.left) && !isRed(x.left.left)) // If both x and x.left are black, move red link to x
            x = moveRedLeft(x);

        x.left = deleteMin(x.left); // Recursively delete the minimum node in the left subtree
        return balance(x); // Balance the tree after deletion
    }

    /***************************************************************************
     *  Intersects operation
     ***************************************************************************/
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

    /***************************************************************************
     *  Print
     ***************************************************************************/
    public void print()
    {
        Node x = root;
        printTree(x);
    }

    public void printTree(Node x)
    {
        if(x == null) return;
        printTree(x.left);
        System.out.println("["+x.lo+","+x.hi+"]"+" "+x.val);
        printTree(x.right);
    }

    public static void main(String[]args)
    {
        // Example
        RedBlackIntervalST<Integer, String> IST = new RedBlackIntervalST<>();
        IST.put(2,5,"badminton");
        IST.put(1,5,"PDSA HW7");
        IST.put(3,5,"Lunch");
        IST.put(3,6,"Workout");
        IST.put(3,7,"Do nothing");
//        IST.print();
        IST.delete(2,5); // delete "badminton"
//        System.out.println("After deleting:");
//        IST.print();
        System.out.println(IST.intersects(1,2));

        IST.put(8,8,"Dinner");
//        IST.print();
        System.out.println(IST.intersects(6,10));

        IST.put(3,7,"Do something"); // If an interval is identical to an existing node, then the value of that node is updated accordingly
        System.out.println(IST.intersects(7,7));

        IST.delete(3,7); // delete "Do something"
//        System.out.println("After deleting:");
//        IST.print();
        System.out.println(IST.intersects(7,7));
    }
}

class RedBlackOutputFormat{
    List<String> answer;
    String func;
    String[] args;
}

class RedBlackTest{
    static boolean deepEquals(List<String> a, List<String> b)
    {
        return Arrays.deepEquals(a.toArray(), b.toArray());
    }
    static boolean run_and_check(RedBlackOutputFormat[] data, RedBlackIntervalST <Integer,String> IST)
    {
        for(RedBlackOutputFormat cmd : data)
        {
            if(cmd.func.equals("intersects"))
            {
                int lo = Integer.parseInt(cmd.args[0]);
                int hi = Integer.parseInt(cmd.args[1]);

                List<String> student_answer = IST.intersects(lo, hi);
                Collections.sort(cmd.answer);
                Collections.sort(student_answer);
                if(!deepEquals(student_answer, cmd.answer))
                {
                    return false;
                }
            }
            else if(cmd.func.equals("put"))
            {
                IST.put(Integer.parseInt(cmd.args[0]), Integer.parseInt(cmd.args[1]), cmd.args[2]);
            }
            else if(cmd.func.equals("delete"))
            {
                IST.delete(Integer.parseInt(cmd.args[0]), Integer.parseInt(cmd.args[1]));
            }
        }
        return true;
    }
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        RedBlackOutputFormat[][] datas;
        RedBlackOutputFormat[] data;
        int num_ac = 0;

        try {
            datas = gson.fromJson(new FileReader(args[0]), RedBlackOutputFormat[][].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];

                System.out.print("Sample"+i+": ");
                if(run_and_check(data, new RedBlackIntervalST<>()))
                {
                    System.out.println("AC");
                    num_ac++;
                }
                else
                {
                    System.out.println("WA");
                    System.out.println("");
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