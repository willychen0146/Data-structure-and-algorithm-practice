import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import com.google.gson.*;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.GrahamScan;

class ObservationStationAnalysis {
    // you can add any global variables if you need
    ArrayList<Point2D> stations;

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        // you can do something in Constructor or not
        // create the temp array to store the station data
        Point2D[] tempArray = stations.toArray(new Point2D[stations.size()]);
        // new a GrahamScan object
        GrahamScan graham = new GrahamScan(tempArray);
//        GrahamScan graham = new GrahamScan(stations.toArray(new Point2D[stations.size()]));

        // get the convex hull
        ArrayList<Point2D> convexHull = new ArrayList<>();
        // Enhanced for loop version
        for(Point2D p: graham.hull()){
            convexHull.add(p);
        }
        // Iterator version
//        for (Iterator<Point2D> iterator = graham.hull().iterator(); iterator.hasNext();) {
//            Point2D p = iterator.next();
//            convexHull.add(p);
//        }
        // assign the convex hull to the this.stations
        this.stations = convexHull;

        // print the convex hull
//        for(Point2D p: convexHull){
//            System.out.println(p);
//        }
    }

    public Point2D[] findFarthestStations() {
//        Point2D[] farthest = new Point2D[]{new Point2D(0,0), new Point2D(1,1)}; // Example
        // find the farthest two stations using the polar radius
        Point2D[] farthest = new Point2D[2];
        double maxDistance = 0.0;
        for(int i=0; i<stations.size(); i++){
            for(int j=i+1; j<stations.size(); j++){
                double distance = stations.get(i).distanceTo(stations.get(j));
                if(distance > maxDistance){
                    maxDistance = distance;
                    farthest[0] = stations.get(i);
                    farthest[1] = stations.get(j);
                }
            }
        }

        // sort the farthest two stations by r coordinate
//        if(farthest[0].r() > farthest[1].r()){
//            Point2D temp = farthest[0];
//            farthest[0] = farthest[1];
//            farthest[1] = temp;
//            if(farthest[0].r() == farthest[1].r()){
//                if(farthest[0].y() > farthest[1].y()){
//                    temp = farthest[0];
//                    farthest[0] = farthest[1];
//                    farthest[1] = temp;
//                }
//            }
//        }
        Arrays.sort(farthest, (a, b) -> Double.compare(a.r(), b.r()));
        if (farthest[0].r() == farthest[1].r()) {
            Arrays.sort(farthest, (a, b) -> Double.compare(a.y(), b.y()));
        }
        return farthest; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly) by y coordinate if there are ties in polar radius.
    }

    public double coverageArea() {
        double area = 0.0;
        // calculate the area surrounded by the existing stations
        for(int i=0; i<stations.size()-2; i++){
            area += Point2D.area2(stations.get(0), stations.get(i+1), stations.get(i+2)) / 2.0;
            // print point and area
//            System.out.println(stations.get(0));
//            System.out.println(stations.get(i+1));
//            System.out.println(stations.get(i+2));
//            System.out.println(area);
        }
        return area;
    }

    public void addNewStation(Point2D newStation) {
        // add a new station to the existing stations
        stations.add(newStation);
        // new a GrahamScan object
        GrahamScan graham = new GrahamScan(stations.toArray(new Point2D[stations.size()]));
        // get the convex hull
        ArrayList<Point2D> convexHull = new ArrayList<>();
        for(Point2D p: graham.hull()){
            convexHull.add(p);
        }
        this.stations = convexHull;
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
        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]); // (0, 0)
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]); // (2, 6)
        System.out.println("Coverage Area: "+Analysis.coverageArea()); // 13.0

        System.out.println("Add Station (10, 3): ");
        Analysis.addNewStation(new Point2D(10, 3));

        System.out.println("Farthest Station A: "+Analysis.findFarthestStations()[0]); // (0, 0)
        System.out.println("Farthest Station B: "+Analysis.findFarthestStations()[1]); // (10, 3)
        System.out.println("Coverage Area: "+Analysis.coverageArea()); // 34.0
    }
}


class OutputFormat{
    ArrayList<Point2D> stations;
    ObservationStationAnalysis OSA;
    Point2D[] farthest;
    double area;
    Point2D[] farthestNew;
    double areaNew;
    ArrayList<Point2D> newStations;
}

class TestCase {
    int Case;
    int score;
    ArrayList<OutputFormat> data;
}


class test_ObservationStationAnalysis{
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        int num_ac = 0;
        int i = 1;

        try {
            // TestCase[] testCases = gson.fromJson(new FileReader(args[0]), TestCase[].class);
            TestCase[] testCases = gson.fromJson(new FileReader(args[0]), TestCase[].class);
            for (TestCase testCase : testCases) {
                System.out.println("Sample"+i+": ");
                i++;
                for (OutputFormat data : testCase.data) {
                    ObservationStationAnalysis OSA = new ObservationStationAnalysis(data.stations);
                    Point2D[] farthest;
                    double area;
                    Point2D[] farthestNew;
                    double areaNew;

                    farthest = OSA.findFarthestStations();
                    area = OSA.coverageArea();


                    if(data.newStations!=null){
                        for(Point2D newStation: data.newStations){
                            OSA.addNewStation(newStation);
                        }
                        farthestNew = OSA.findFarthestStations();
                        areaNew = OSA.coverageArea();
                    }else{
                        farthestNew = farthest;
                        areaNew = area;
                    }


                    if(farthest[0].equals(data.farthest[0]) && farthest[1].equals(data.farthest[1]) &&  Math.abs(area-data.area) < 0.0001
                            && farthestNew[0].equals(data.farthestNew[0]) && farthestNew[1].equals(data.farthestNew[1]) && Math.abs(areaNew-data.areaNew) < 0.0001)
                    {
                        System.out.println("AC");
                        num_ac++;
                    }
                    else
                    {
                        System.out.println("WA");
                        System.out.println("Ans-farthest: " + Arrays.toString(data.farthest));
                        System.out.println("Your-farthest:  " + Arrays.toString(farthest));
                        System.out.println("Ans-area:  " + data.area);
                        System.out.println("Your-area:  " + area);

                        System.out.println("Ans-farthestNew: " + Arrays.toString(data.farthestNew));
                        System.out.println("Your-farthestNew:  " + Arrays.toString(farthestNew));
                        System.out.println("Ans-areaNew:  " + data.areaNew);
                        System.out.println("Your-areaNew:  " + areaNew);
                        System.out.println("");
                    }
                }
                System.out.println("Score: "+num_ac+"/ 8");
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}