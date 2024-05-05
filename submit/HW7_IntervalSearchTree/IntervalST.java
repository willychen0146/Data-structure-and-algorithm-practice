import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import com.google.gson.*;

import edu.princeton.cs.algs4.In;

class IntervalST<Key extends Comparable<Key>, Value>{
    private Node root;

    private class Node {
        private Key lo, hi, max;
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

    private Key max(Node x) {
        if (x == null) return null;
        return x.max;
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
        IntervalST<Integer, String> IST = new IntervalST<>();
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

class OutputFormat{
    List<String> answer;
    String func;
    String[] args;
}

class test{
    static boolean deepEquals(List<String> a, List<String> b)
    {
        return Arrays.deepEquals(a.toArray(), b.toArray());
    }
    static boolean run_and_check(OutputFormat[] data, IntervalST <Integer,String> IST)
    {
        for(OutputFormat cmd : data)
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
        OutputFormat[][] datas;
        OutputFormat[] data;
        int num_ac = 0;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat[][].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];

                System.out.print("Sample"+i+": ");
                if(run_and_check(data, new IntervalST<>()))
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