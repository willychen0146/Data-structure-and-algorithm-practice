# Observation Stations (Graham scan and convex hull)
In this assignment, we delve into a scenario where a group of ecologists are assessing potential sites for establishing observation stations. The primary goal is to determine how these stations should be strategically placed to facilitate ecological studies.

## Description
Your mission is to assist these ecologists doing their research. You will receive coordinates representing potential stations and are required to investigate:

- **Farthest Observation Stations:** Determine the two station locations that are geographically most distant from each other. This analysis will help in assessing the spatial extent of the ecological study area.
- **Ecological Area Coverage:** Calculate the coverage area for the convex polygon formed by existing stations. This will help in understanding how effectively the network of stations can monitor and represent the region's ecological diversity.
- **A New Observation Station:** Assess how integrating a new station into the existing network alters the overall spatial arrangement and ecological coverage. This evaluation will provide insights into enhancing the network for comprehensive ecological observation.

## Template

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import edu.princeton.cs.algs4.Point2D;

class ObservationStationAnalysis {

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        // you can do something in Constructor or not
    }

    public Point2D[] findFarthestStations() {
        Point2D[] farthest = new Point2D[]{new Point2D(0,0), new Point2D(1,1)}; // Example
        // find the farthest two stations
        return farthest; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly) by y coordinate if there are ties in polar radius.

    }

    public double coverageArea() {
        double area = 0.0;
        // calculate the area surrounded by the existing stations
        return area;
    }

    public void addNewStation(Point2D newStation) {
        
    }
    
    public static void main(String[] args) throws Exception {

        ArrayList<Point2D> stationCoordinates = new ArrayList<>();
        stationCoordinates.add(new Point2D(0, 0));
        stationCoordinates.add(new Point2D(2, 0));
        stationCoordinates.add(new Point2D(3, 2));
        stationCoordinates.add(new Point2D(2, 6));
        stationCoordinates.add(new Point2D(0, 4));
        stationCoordinates.add(new Point2D(1, 1));
        stationCoordinates.add(new Point2D(2, 2));

        ObservationStationAnalysis Analysis = new ObservationStationAnalysis(stationCoordinates);
        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: "+Analysis.coverageArea());
        
        System.out.println("Add Station (10, 3): ");
        Analysis.addNewStation(new Point2D(10, 3));
        
        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: "+Analysis.coverageArea());
    }
}
```
## Expected Output
```java
Farthest Station A: (0.0, 0.0)
Farthest Station B: (2.0, 6.0)
Coverage Area: 13.0

Add Station (10, 3): 
Farthest Station A: (0.0, 0.0)
Farthest Station B: (10.0, 3.0)
Converage Area: 34.0

// Note: All the returns should be in 'double' datatype
```

## TestCase
```java
Time Limit: 1200 ms

All the provided coordinates are unique
N : num of observation stations
M : upper bound of x, y (0 < x,y < M)
A : num of new stations added
Permissible margin of error: 1e-4
20 points: N = 4, M <= 10, A <= 3
20 points: N <= 10, M <= 100, A <= 5
20 points: N <= 200, M <= 20, A <= 10
20 points: N <= 50000, M <= 400, A <= 1000 (Special Case)
20 points: N <= 200000, M <= 1000, A <= 50
```

## File Download
[Test Code](https://drive.google.com/file/d/1VQG2kuBRAjqtBJEJ2nxP3wg5f2tpt_Vj/view?usp=sharing)

[Test Case](https://drive.google.com/file/d/19c5FHnjAw6FxnC7gyzFJvRp_1R-y_AUF/view?usp=sharing)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/BJAJCPJja)