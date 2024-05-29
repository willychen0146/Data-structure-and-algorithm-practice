import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.*;

import java.util.*;

class RoadToCastle {
    private int[][] map;
    private int[] init_pos;
    private int[] target_pos;
    private final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // RIGHT, DOWN, LEFT, UP

    private class Node implements Comparable<Node> {
        int row;
        int col;
        int cost;
        int estimate; // Heuristic estimate to the target
        Node parent;

        public Node(int row, int col, int cost, int estimate, Node parent) {
            this.row = row;
            this.col = col;
            this.cost = cost;
            this.estimate = estimate;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost + this.estimate, other.cost + other.estimate);
        }
    }

    public List<int[]> shortest_path() {
        int rows = map.length;
        int cols = map[0].length;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        boolean[][] visited = new boolean[rows][cols];
        Node startNode = new Node(init_pos[0], init_pos[1], 0, heuristic(init_pos[0], init_pos[1]), null);
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (visited[current.row][current.col]) continue;
            visited[current.row][current.col] = true;

            if (current.row == target_pos[0] && current.col == target_pos[1]) {
                return reconstructPath(current);
            }

            for (int[] direction : directions) {
                int nextRow = current.row + direction[0];
                int nextCol = current.col + direction[1];

                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && map[nextRow][nextCol] != 0 && !visited[nextRow][nextCol]) {
                    int nextCost = current.cost + (map[nextRow][nextCol] == 3 ? 5 : 1);
                    Node nextNode = new Node(nextRow, nextCol, nextCost, heuristic(nextRow, nextCol), current);
                    pq.add(nextNode);
                }
            }
        }

        return new ArrayList<>();
    }

    private int heuristic(int row, int col) {
        // Manhattan distance
        return Math.abs(row - target_pos[0]) + Math.abs(col - target_pos[1]);
    }

    private List<int[]> reconstructPath(Node targetNode) {
        LinkedList<int[]> path = new LinkedList<>();
        Node current = targetNode;
        while (current != null) {
            path.addFirst(new int[]{current.row, current.col});
            current = current.parent;
        }
        return path;
    }

    public int shortest_path_len() {
        List<int[]> path = shortest_path();
        if (path.isEmpty()) return 0;

        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int[] current = path.get(i);
            int[] next = path.get(i + 1);
            cost += map[next[0]][next[1]] == 3 ? 5 : 1;
        }
        return cost;
    }

    public RoadToCastle(int[][] map, int[] init_pos, int[] target_pos) {
        this.map = map;
        this.init_pos = init_pos;
        this.target_pos = target_pos;
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
                new int[]{1,3}
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

class OutputFormat{
    int[][] map;
    int[] init_pos;
    int[] target_pos;
    int answer;
}

class test{
    static boolean are4Connected(int[] p1, int[] p2) {
        return (Math.abs(p1[0] - p2[0]) == 1 && p1[1] == p2[1]) || (Math.abs(p1[1] - p2[1]) == 1 && p1[0] == p2[0]);
    }
    static boolean isShortestPath(int[][] map, int path_len, List<int[]> path)
    {
        // check if the path is valid, (if the two node is actually neighbour, and if the path is not wall)
        int path_len2 = 0;
        for(int i = 1; i<path.size(); ++i){
            int[] pos_prev = path.get(i-1);
            int[] pos_now = path.get(i);
            int type = map[pos_now[0]][pos_now[1]];
            if(!are4Connected(pos_prev,pos_now) || type == 0) //type == 0 means that it is a wall.
                return false;
            path_len2 += (type == 3) ? 5 : 1;
        }
        return (path_len == path_len2);
    }
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        OutputFormat[] datas;
        OutputFormat data;
        int num_ac = 0;

        List<int[]> SHP;
        RoadToCastle sol;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat[].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];
                sol = new RoadToCastle(data.map, data.init_pos, data.target_pos);
                SHP = sol.shortest_path();

                System.out.print("Sample"+i+": ");
                if(sol.shortest_path_len() != data.answer)
                {
                    System.out.println("WA: incorrect path length");
                    System.out.println("Test_ans:  " + data.answer);
                    System.out.println("User_ans:  " + sol.shortest_path_len());
                    System.out.println("");
                }
                else if(!Arrays.equals(SHP.get(0),data.init_pos))
                {
                    System.out.println("WA: incorrect starting position");
                    System.out.println("Test_ans:  " + Arrays.toString(data.init_pos));
                    System.out.println("User_ans:  " + Arrays.toString(SHP.get(0)));
                    System.out.println("");
                }
                else if(!Arrays.equals(SHP.get(SHP.size()-1),data.target_pos))
                {
                    System.out.println("WA: incorrect goal position");
                    System.out.println("Test_ans:  " + Arrays.toString(data.target_pos));
                    System.out.println("User_ans:  " + Arrays.toString(SHP.get(SHP.size()-1)));
                    System.out.println("");
                }
                else if(!isShortestPath(data.map, data.answer,SHP))
                {
                    System.out.println("WA: Path Error, either not shortest Path or path not connected");
                    System.out.println("Map:      " + Arrays.deepToString(data.map));
                    System.out.println("User_Path:  " + Arrays.deepToString(SHP.toArray()));
                    System.out.println("Test_path_len:  " + data.answer);
                    System.out.println("User_path_len:  " + sol.shortest_path_len());
                    System.out.println("");

                }
                else
                {
                    System.out.println("AC");
                    num_ac++;
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