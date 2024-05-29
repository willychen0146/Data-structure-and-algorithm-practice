# HW10: Best path to the Demon's Castle (Dijkstra's shortest path algorithm)

## Backstory:
The hero is on his way to the demon's castle. Luckily the area was already scouted and there is a complete map of it. The hero wants to find the best path to the castle based on the map.

## Description:
In this problem, you need to determine the fastest path from the starting point to the target point.

### Inputs:
1. **int[][] map**
There will be an N x M grid map where '2' represents roads, '0' indicates inaccessible areas, and '3' represents the presence of a monster on the path.

e.g.
```java
[0,0,0,0,0,0]
[0,2,3,2,0,0]
[0,0,2,0,2,0]
[0,0,2,2,3,0]
[0,0,2,2,2,0]
[0,0,0,0,0,0]
```

2. **int[] init_pos**
A 2-dimensional cordinate of the starting position, the value on the map can be accessed just like below:
```java
map[init_pos[0]][init_pos[1]] 
```

3. **int[] target_pos**
A 2-dimensional cordinate of the ending position, the value on the map can be accessed just like below:
```java
map[target_pos[0]][target_pos[1]] 
```

**Rules:**
- The hero can only move UP, DOWN, LEFT, or RIGHT, but not diagonal.
- Moving across road grids costs 1 time unit, e.g. moving from a map value of [2] to a neighboring [2] by one-unit UP, DOWN, LEFT, or RIGHT costs 1 time unit.
- Moving across grids TO a monster costs 5 time unit, e.g. moving from a map value of [2] to a neighboring [3] costs 5 time unit.
- Leaving a monster costs 1 time unit, e.g. moving from a map value of [3] to a neighboring [2] costs 1 time unit.
- Cost to reach init_pos is 0.

**Output format:**
The shortest_path function needs to return the cordinates of the best path including the starting point to the target point based on the input format as a List.
```java
public List<int[]> shortest_path() {
    // modify this
    return new ArrayList<int[]>();
}
// {{1, 1}, {1, 2}, {1, 3}}  
// meaning the best path starts from (1,1) => (1,2) => (1,3) 
// where (1,1) is the starting point and (1,3) is the target point
```

The shortest_path_len function needs to return the cost of the best path.
```java
public int shortest_path_len(){
    // modify this
    return 0;
}
```

## Template  

```java
class RoadToCastle {
    public List<int[]> shortest_path(){
        //return int[] in the format of {Y,X}
        return new ArrayList<int[]>();
    }
    public int shortest_path_len(){
        return 0;
    }
    public RoadToCastle(int[][] map, int[] init_pos, int[] target_pos){
        //map: [Y][X]
        //init_pos: 0:Y, 1:X
        //target_pos: 0:Y, 1:X
    }
    public static void main(String[] args) {
        RoadToCastle sol = new RoadToCastle(new int[][]{
                        {0,0,0,0,0},
                        {0,2,3,2,0},  //map[1][2]=3
                        {0,2,0,2,0},
                        {0,2,0,2,0},
                        {0,2,2,2,0},
                        {0,0,0,0,0}
                },
                new int[]{1,1},
                new int[]{1,3},
        );
        System.out.println(sol.shortest_path_len());
        List<int[]> path = sol.shortest_path();
        for(int[] coor : path)
            System.out.println("x: "+Integer.toString(coor[0]) + " y: "+Integer.toString(coor[1]));

        //ans: best_path:{{1, 1}, {1, 2}, {1, 3}}
        //Path 1 (the best): [1, 1] [1, 2] [1, 3] -> 0+5+1 = 6, cost to reach init_pos is zero!
        //Path 2 (example of other paths): [1, 1] [2, 1] [3, 1] [4, 1] [4, 2] [4, 3] [3, 3] [2, 3] [1, 3] -> 8
    }
}
```

## Test Data

We guarantee the following:
- All the mazes have walls at edges of the map, i.e. the surrounding '0's of the matrix.
init_pos and target_pos are always connected and have a path between them.
init_pos and target_pos would never be on a wall.
```java
Time Limit: 150ms

Case:
case1: 20 points, Width*Height <= 2304
case2: 20 points, Width*Height <= 2304
case3: 20 points, Width*Height <= 6400
case4: 20 points, Width*Height <= 10000
case5: 20 points, Width*Height <= 10000
```

## File Download
[Test Code](https://drive.google.com/file/d/1BxUZv7IJ88qoPywmQnRM36oVmvGlrdG3/view)

[Test Data](https://drive.google.com/file/d/1KryBXAEHvsFgp2lFqCiTFmS8sQ2Dgm0u/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/BJJ_dQisa)