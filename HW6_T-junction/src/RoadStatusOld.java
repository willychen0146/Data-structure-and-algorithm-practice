import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import com.google.gson.*;

//class RoadStatus2{
//    private int[] carsOnRoad;
//    private boolean[] greenLight;
//    private int[] greenLightSince;
//    private int lastUpdateTime;
//
//    public RoadStatus2() {
//        carsOnRoad = new int[3]; // 3 roads
//        greenLight = new boolean[3]; // 3 traffic lights
//        greenLightSince = new int[3]; // the time when the traffic light turns green
//        lastUpdateTime = -1; // last update time
//    }
//
//    public int[] roadStatus(int time){
//        //    return the number of cars in each road.
//        //    new int[]{num_of_car_0,num_of_car_1,num_of_car_2}
//        updateTrafficLights(time);
//        return Arrays.copyOf(carsOnRoad, carsOnRoad.length);
////        return new int[]{0};
//    }
//    public void addCar(int time, int id, int num_of_cars) {
//        //add a car to the queue of a specific id.
//        updateTrafficLights(time);
//        carsOnRoad[id] += num_of_cars;
//    }
//    private void updateTrafficLights(int currentTime) {
//        if (currentTime != lastUpdateTime) {
//            int maxCars = 0;
//            int roadWithMaxCars = -1;
//            for (int i = 0; i < carsOnRoad.length; i++) {
//                if (greenLight[i] && currentTime - greenLightSince[i] >= carsOnRoad[i]) {
//                    greenLight[i] = false;
//                }
//                if (carsOnRoad[i] > maxCars || (carsOnRoad[i] == maxCars && roadWithMaxCars == -1)) {
//                    maxCars = carsOnRoad[i];
//                    roadWithMaxCars = i;
//                }
//            }
//            if (maxCars > 0) {
//                greenLight[roadWithMaxCars] = true;
//                greenLightSince[roadWithMaxCars] = currentTime;
//                carsOnRoad[roadWithMaxCars]--;
//            }
//            lastUpdateTime = currentTime;
//        }
//    }
//    public static void main(String[] args)
//    {
//        RoadStatus2 sol = new RoadStatus2(); // create a T-junction; all traffic lights are Red at the beginning
//
//        sol.addCar(0,0,10);
//        System.out.println("0: "+Arrays.toString(sol.roadStatus(0)));
//        System.out.println("1: "+Arrays.toString(sol.roadStatus(1)));
//        sol.addCar(2,0,1);
//        System.out.println("2: "+Arrays.toString(sol.roadStatus(2)));
//        sol.addCar(3,1,1);
//        System.out.println("3: "+Arrays.toString(sol.roadStatus(3)));
//        System.out.println("4: "+Arrays.toString(sol.roadStatus(4)));
//        sol.addCar(5,1,1);
//        for(int i = 5; i<15;++i)
//            System.out.println(i+": "+Arrays.toString(sol.roadStatus(i)));
//        sol.addCar(15, 1, 10);
//        System.out.println("15: "+Arrays.toString(sol.roadStatus(15)));
//        System.out.println("16: "+Arrays.toString(sol.roadStatus(16)));
//        // check below for full output explaination
//    }
//}

class RoadStatus2 {
    private int[] carsOnRoad;
    private PriorityQueue<Event> eventQueue;
    private int systemTime;
    private int currentGreenRoad;
    private int timeToSwitchGreen;
    private boolean[] isGreen;
    private boolean checkPoint;
    private int carCalculateNextTime;
    private int hadSwitchedGreenAt;
    private boolean calculateFirst;

    private static class Event implements Comparable<Event> {
        int time;
        int roadId;
        int numCars; // Number of cars to add for car addition events
        int type; // 0 for car addition, 1 for car removal

        Event(int time, int roadId, int numCars, int type) {
            this.time = time;
            this.roadId = roadId;
            this.numCars = numCars;
            this.type = type;
        }

        @Override
        public int compareTo(Event other) {
            // compare the time of the events if equal compare the type of the events
            if (this.time == other.time) {
                return this.type - other.type;
//                return other.type - this.type;
            }
            return this.time - other.time;
        }
    }

    public RoadStatus2() {
        carsOnRoad = new int[3];
        eventQueue = new PriorityQueue<>();
        systemTime = 0; // System time
        currentGreenRoad = -1; // No green light initially
        timeToSwitchGreen = 0; // Time to switch the green light
        isGreen = new boolean[3]; // Traffic light states
        checkPoint = false;
        carCalculateNextTime = 0;
        hadSwitchedGreenAt = -1;
        calculateFirst = false;
    }

    public int[] roadStatus(int time) {
        // Process events up to but not including the current time
        System.out.println("check time: "+time);
        System.out.println("currentGreenRoad: "+currentGreenRoad);
        System.out.println("timeToSwitchGreen: "+timeToSwitchGreen);
        // print the current status of the roads
        for(int i = 0; i<carsOnRoad.length;++i)
            System.out.println("carsOnRoad["+i+"]: "+carsOnRoad[i]);

        processEventsUpTo(time);


        return Arrays.copyOf(carsOnRoad, carsOnRoad.length);
    }

    public void addCar(int time, int roadId, int num_of_cars) {
        eventQueue.add(new Event(time, roadId, num_of_cars, 0));
//        System.out.println("Event added at time " + time + " for road " + roadId + " with " + num_of_cars + " cars");
    }
/*
    private void processEventsUpTo(int currentTime) {
        // If the green light is on, resume processing from the last system time
        if(checkPoint){
            eventQueue.add(new Event(systemTime, currentGreenRoad, carCalculateNextTime, 1));
            checkPoint = false;
        }

        // Process all events up to the current time
        while (!eventQueue.isEmpty() && eventQueue.peek().time <= currentTime) {
            Event event = eventQueue.poll();
            System.out.println("Processing event at time " + event.time + " for road " + event.roadId + " with type " + event.type);

//            if (carsOnRoad[0] == 0 && carsOnRoad[1] == 0 && carsOnRoad[2] == 0) {
//                systemTime = event.time;
//            }

            if (event.type == 0) { // Car addition event
                if(currentGreenRoad == -1 && hadSwitchedGreenAt != event.time){
                    carsOnRoad[event.roadId] += event.numCars;
                    switchGreenLight(event.time);
                    hadSwitchedGreenAt = event.time;
                }
                else{
                    carsOnRoad[event.roadId] += event.numCars;
                }
                // If the time to switch green light is less than the current time, switch the green light immediately after first adding event
//                if (systemTime == event.time && (currentGreenRoad == -1 || timeToSwitchGreen <= event.time))
//                    switchGreenLight(timeToSwitchGreen);
                systemTime = event.time;
            }
            else if(event.type == 1 && event.time != currentTime){ // Car removal event
                // If time to switch green light is less than the current time, switch the green light again
                if(timeToSwitchGreen < currentTime){
                    carsOnRoad[event.roadId] -= event.numCars;
                    systemTime = timeToSwitchGreen; // update the system time
                    checkPoint = false;
                    if(hadSwitchedGreenAt != systemTime) {
                        switchGreenLight(systemTime); // switch the green light
                        hadSwitchedGreenAt = systemTime;
                    }
                }
                // If the time to switch green light is greater than the current time, calculate the number of cars to remove this time
                // and add a new event to remove the remaining cars next time
                else{
                    int carCalculateThisTime = currentTime - systemTime;
//                    if(checkPoint)
//                        carCalculateThisTime = carCalculateNextTime;
                    carCalculateNextTime = event.numCars - carCalculateThisTime;
                    carsOnRoad[event.roadId] -= carCalculateThisTime;
                    System.out.println("Road "+event.roadId+" has "+carCalculateThisTime+" cars out at "+currentTime);
                    systemTime = currentTime;
                    checkPoint = true;
//                    eventQueue.add(new Event(currentTime, event.roadId, carCalculateNextTime, 1));
                }
            }
            // Update the system time to the event time this time process
//            systemTime = event.time;

            // If the time to switch green light is less than the current time, switch the green light again
//            if(timeToSwitchGreen <= systemTime){
//                if(!eventQueue.isEmpty() && eventQueue.peek().type == 0 && eventQueue.peek().time == event.time)
//                    continue;
//                switchGreenLight(systemTime);
//            }
//            else
//                systemTime = event.time;

//            if(event.type == 0 && timeToSwitchGreen <= systemTime){
//                switchGreenLight(systemTime);
//            }
//            if(eventQueue.isEmpty() && timeToSwitchGreen <= systemTime){
//                switchGreenLight(systemTime);
//            }
        }
        // update the system time to the current time
        systemTime = currentTime;
        if(timeToSwitchGreen <= currentTime && hadSwitchedGreenAt != currentTime){
            switchGreenLight(currentTime);
        }

//        // Switch traffic lights if necessary
//        if(systemTime <= currentTime && timeToSwitchGreen <= currentTime){
//            switchGreenLight(systemTime);
////            System.out.println("timeToSwitchGreen: "+timeToSwitchGreen);
//        }
//
    }
*/

    private void processEventsUpTo(int currentTime) {
        while (!eventQueue.isEmpty() && eventQueue.peek().time <= currentTime) {
            Event event = eventQueue.poll();
            systemTime = event.time; // Update system time to the time of the event

            if (event.type == 0) { // Car addition event
                if(calculateFirst){
                    carsOnRoad[event.roadId] --;
                    calculateFirst = false;
                }
                carsOnRoad[event.roadId] += event.numCars;
//                System.out.println("Road " + event.roadId + " has " + event.numCars + " cars at time " + systemTime);
                if (currentGreenRoad == -1 || systemTime >= timeToSwitchGreen) {
                    switchGreenLight(systemTime);
                }
            } else if (event.type == 1) { // Car removal event
                int carsToRemove = Math.min(event.numCars, currentTime - systemTime + 1);
                carsOnRoad[event.roadId] -= carsToRemove;
                if (carsToRemove < event.numCars) {
                    // If not all cars could exit, schedule another removal event
                    if(currentTime + 1 != timeToSwitchGreen)
                        eventQueue.add(new Event(currentTime + 1, event.roadId, event.numCars - carsToRemove, 1));
                    else
                        calculateFirst = true;
                }
                if (systemTime >= timeToSwitchGreen) {
                    switchGreenLight(systemTime);
                }
            }
        }
        systemTime = currentTime; // Update system time to the current time
    }

    private void switchGreenLight(int currentTime) {
//        System.out.println("Switching green light at time " + currentTime);
        // Turn all lights to red
        Arrays.fill(isGreen, false);

        // Find the road with the most cars or the smallest index
        int roadWithMaxCars = -1;
        int maxCars = 0;
        for (int i = 0; i < carsOnRoad.length; i++) {
            if (carsOnRoad[i] > maxCars || (carsOnRoad[i] == maxCars && roadWithMaxCars > i)) {
                maxCars = carsOnRoad[i];
                roadWithMaxCars = i;
            }
        }

        // If no cars are present, keep all lights red
        if (maxCars == 0) {
            currentGreenRoad = -1;
            return;
        }

        // Set the new green light
        currentGreenRoad = roadWithMaxCars;
        isGreen[currentGreenRoad] = true;

        // Keep the green light on until all cars have exited
        timeToSwitchGreen = currentTime + carsOnRoad[currentGreenRoad];
//        timeToSwitchGreen = currentTime + maxCars;

        // add a new event to remove the cars from the road
        eventQueue.add(new Event(currentTime+1, currentGreenRoad, carsOnRoad[currentGreenRoad], 1));
//        eventQueue.add(new Event(timeToSwitchGreen, currentGreenRoad, maxCars, 1));

        System.out.println("Green light on road " + currentGreenRoad + " from " + currentTime + " until time " + timeToSwitchGreen);
    }

    public static void main(String[] args)
    {
        // Example 1
        System.out.println("Example 1: ");
        RoadStatus2 sol1 = new RoadStatus2(); // create a T-junction; all traffic lights are Red at the beginning
        sol1.addCar(0, 0, 2);
        System.out.println("0: " + Arrays.toString(sol1.roadStatus(0)));
        sol1.addCar(0, 1, 3);
        System.out.println("0: " + Arrays.toString(sol1.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol1.roadStatus(1)));
        sol1.addCar(2, 0, 4);
        for (int i = 2; i < 12; ++i)
            System.out.println(i + ": " + Arrays.toString(sol1.roadStatus(i)));
        //______________________________________________________________________
        // Example 2
        RoadStatus2 sol2 = new RoadStatus2(); // create a T-junction; all traffic lights are Red at the beginning
        System.out.println("Example 2: ");
        sol2.addCar(0, 0, 2);
        System.out.println("0: " + Arrays.toString(sol2.roadStatus(0)));
        sol2.addCar(0, 0, 1);
        System.out.println("0: " + Arrays.toString(sol2.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol2.roadStatus(1)));
        sol2.addCar(2, 1, 2);
        for (int i = 2; i < 7; ++i)
            System.out.println(i + ": " + Arrays.toString(sol2.roadStatus(i)));
        //______________________________________________________________________
        // Example 3
        RoadStatus2 sol3 = new RoadStatus2(); // create a T-junction; all traffic lights are Red at the beginning
        System.out.println("Example 3: ");
        sol3.addCar(0, 0, 1);
        System.out.println("0: " + Arrays.toString(sol3.roadStatus(0)));
        System.out.println("1: " + Arrays.toString(sol3.roadStatus(1)));
        System.out.println("2: " + Arrays.toString(sol3.roadStatus(2)));
        sol3.addCar(3, 1, 1);
        System.out.println("3: " + Arrays.toString(sol3.roadStatus(3)));
        sol3.addCar(3, 1, 1);
        System.out.println("3: " + Arrays.toString(sol3.roadStatus(3)));
        sol3.addCar(4, 0, 2);
        for (int i = 4; i < 10; i++) {
            System.out.println(i + ": " + Arrays.toString(sol3.roadStatus(i)));
        }
        // check below for full output explaination
    }
}

class OutputFormat2{
    int[] answer;
    String func;
    int[] args;
}

class test2{
    static boolean run_and_check(OutputFormat2[] data, RoadStatus2 roadStat)
    {
        for(OutputFormat2 cmd : data)
        {
            if(cmd.func.equals("addCar"))
            {
                roadStat.addCar(cmd.args[0], cmd.args[1], cmd.args[2]);
            }
            else if(cmd.func.equals("roadStatus"))
            {
                int[] arr = roadStat.roadStatus(cmd.args[0]);
                System.out.println("Expected: "+Arrays.toString(cmd.answer));
                System.out.println("Output: "+Arrays.toString(arr));
                if(!Arrays.equals(arr,cmd.answer))
                    return false;
            }
        }
        return true;
    }
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        OutputFormat2[][] datas;
        OutputFormat2[] data;
        int num_ac = 0;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat2[][].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];

                System.out.print("Sample"+i+": ");
                if(run_and_check(data, new RoadStatus2()))
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