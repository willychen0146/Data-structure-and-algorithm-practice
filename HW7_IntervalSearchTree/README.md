# HW7: Interval Search Tree (Binary search tree and Red-Black BST)
## Description:
An Interval Search Tree (IST) is an advanced and dynamic data structure that efficiently manages a collection of closed intervals on an one-dimensional numerical line. These intervals are denoted as `[a, b]`, where a and b represent the starting and ending points, respectively, and it is guaranteed that (a <= b) for each interval.

This data structure is optimized for speed due to its clever organization. Each node in the tree stores an interval with its ending points and the maximum ending point in its subtree, allowing for quickly discarding non-relevant intervals during searching. This design allows for basic operations—insertion, deletion, and overlap searching—to be performed in logarithmic time relative to the number of intervals. Here’s how an IST operates:

- **Insertion:** An intervals is added to the tree based on its starting point. If starting points of two intervals are the same, the ending points are compared. If an interval is identical to an existing node, then the value of that node is updated accordingly.
- **Deletion:** If an interval to be deleted exists, it should be deleted after the call, and the tree will restructure itself accordingly. If there are no intervals eligible for removal, no action will be taken.
- **Overlap Searching:** The tree efficiently finds all intervals that overlap with a given interval by comparing endpoints. Values of the overlapping intervals are returned.

For this homework, you are required to implement an Interval Search Tree utilizing generic types in your code to ensure flexibility and type safety.

## Hint
- [Interval Search Tree Tutorial](https://algs4.cs.princeton.edu/93intersection/IntervalST.java.html)

- [Youtube](https://www.youtube.com/watch?v=BLsKHCtdiHs&t=304s&ab_channel=edXSeries)

## Template
```java
class IntervalST<Key extends Comparable<Key>, Value>{
    private Node root;

    private class Node {
        private Key lo, hi, max;
        private Value val;
        private int size;
        private Node left, right;
        
        public Node(Key lo, Key hi, Value val) {
            // initializes the node if required.
        }
    }

    public IntervalST()
    {
        // initializes the tree if required.
    }
    
    public void put(Key lo, Key hi, Value val)
    {
        // insert a new interval here.
        // lo    : the starting point of the interval. lo included
        // hi    : the ending point of the interval. hi included
        // val   : the value stored in the tree.
    }
    
    public void delete(Key lo, Key hi)
    {
        // remove an interval of [lo,hi]
        // do nothing if interval not found.
    }
    
    public List<Value> intersects(Key lo, Key hi)
    {
        // return the values of all intervals within the tree which intersect with [lo,hi].
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
        IST.delete(2,5); // delete "badminton"
        System.out.println(IST.intersects(1,2));
        
        IST.put(8,8,"Dinner");
        System.out.println(IST.intersects(6,10));
        
        IST.put(3,7,"Do something"); // If an interval is identical to an existing node, then the value of that node is updated accordingly
        System.out.println(IST.intersects(7,7));
        
        IST.delete(3,7); // delete "Do something"
        System.out.println(IST.intersects(7,7));
    }
}
```

## Expected Output
```java
[PDSA HW7]
[Workout, Do nothing, Dinner]
[Do something]
[]
```

## TestCase
```java
Time Limit: 20 ms

N = Number of function call
M = Maximum time

Case:
case1: 20 points, N <= 6, M <= 25
case2: 20 points, N <= 10, M <= 25
case3: 20 points, N <= 200, M <= 25
case4: 20 points, N <= 300, M <= 50
case5: 20 points, N <= 500, M <= 75
```

## File Download
[Test Code](https://drive.google.com/file/d/1UM2E54VVQGa9Tm1Y2VcJqFEY-WhfsPHm/view)

[Test Data](https://drive.google.com/file/d/1fs8GuB-CyvyLej0yg8peVEKlzvrCayl2/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/H10TwXjsa)