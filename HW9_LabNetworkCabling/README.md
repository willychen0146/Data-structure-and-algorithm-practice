# HW8: Lab Network Cabling (Minimum Spanning Tree and Traversing Graph)

Your lab advisor aims to create an ideal study environment with stable network connections for the router, the server, printers, and students' computers. To reduce cost and ensure optimal communication, **the network design should minimize the total cable length while connecting all devices**. Given your background in 'Practical Data Structures and Algorithms,' your advisor has assigned you the task of designing the lab's cabling.

## Description:
After implementing **Minimum Spanning Tree** for network planning based on device information and the distances between devices, your advisor asks you:

- **The Total Cabling Cost**: This refers to the overall cost of cabling, with the assumption that each unit length of cable costs 1 dollar.
- **Server-Router Distance**: The distance of the connection between the server and router after cabling, which is crucial for fast and stable server operations.
- **Most Popular Printer**: This analysis identifies which printer is the busiest among the options. It assumes that **each student selects the nearest printer to their computer**.

Here are some supplementary instructions:

- Your lab has **a router, a server, few printers, and several computers**.
- Definition of **Distance**: the cumulative length of the cables that connect them.
- No two devices have the same distance between them.
- The index corresponding to each device is **random**.
- Each student has their own computer. They use the nearest printer, and if two printers are equally close, they choose the one with the **lower device index**.
- If two or more printers have the same amount of usage, return the **smallest index**.

## Template  

```java
import java.util.List;
import java.util.Map;

class LabNetworkCabling {
    
    public LabNetworkCabling(Map<Integer, String> deviceTypes, List<int[]> links){
        // create a Minimum Spanning Tree
    };
    
    public int cablingCost() {
        int cost = 0;
        // calculate the total cost
        return cost;
    }

    public int serverToRouter(){
        int srDistance = 0;
        // find the path distance between the server and the router
        return srDistance;
    }

    public int mostPopularPrinter(){
        int printerIndex = 0;
        // find the most popular printer and return its index
        return printerIndex;
    }

    public static void main(String[] args) {
        
        // [device index, device type]
        Map<Integer, String> deviceTypes = Map.of(
            0, "Router",
            1, "Server",
            2, "Printer",
            3, "Printer",
            4, "Computer",
            5, "Computer",
            6, "Computer"
        );

        // [device a, device b, link distance (cable length)]
        List<int[]> links = List.of(
                    new int[]{0, 1, 4},
                    new int[]{1, 2, 2},
                    new int[]{2, 4, 1},
                    new int[]{0, 3, 3},
                    new int[]{1, 3, 8},
                    new int[]{3, 5, 7},
                    new int[]{3, 6, 9},
                    new int[]{0, 6, 5}
                );

        LabNetworkCabling Network = new LabNetworkCabling(deviceTypes, links);
        System.out.println("Total Cabling Cost: " + Network.cablingCost());
        System.out.println("Distance between Server and Router: " + Network.serverToRouter());
        System.out.println("Most Popular Printer: " + Network.mostPopularPrinter());
    }
}
```

## Expected Output
```java
Total Cabling Cost: 22
Distance between Server and Router: 4
Most Popular Printer: 3
```

## Hint
- Both the Kruskal and Prim algorithms can be implemented in this assignment. Interested students are encouraged to try both to compare the performance speed of the two Minimum Spanning Trees (MSTs).
- In the implementation of the MST, you may use the library provided in the course materials. It is advisable to enhance the MST class with some additional functions for better functionality.
- Both Depth-First Search and Breadth-First Search are valid methods for pathfinding, though other techniques may be suitable as well. If time permits, experimenting with various methods to observe the differences in their performance speeds is recommended. In fact, there is quite a significant difference in speed.

## TestCase
```java
Time Limit: 720 ms

P : num of printers
C : num of computers
L : num of links

20 points: P <= 3, C <= 3, L <= 8
20 points: P <= 5, C <= 5, L <= 10 (Special Case)
20 points: P <= 20, C <= 20, L <= 80
20 points: P <= 400, C <= 400, L <= 800
20 points: P <= 2000, C <= 2000, L <= 4000
```

## File Download
[Test Code](https://drive.google.com/file/d/1Wq5lNr5TUas1paPOapgfHYCllcSVXhUy/view)

[Test Data](https://drive.google.com/file/d/13JAzDQ7MjlJeaSDfDZt72TgAKVkApXzo/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/rJL4O7ojp)