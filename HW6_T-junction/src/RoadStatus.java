import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import com.google.gson.*;

class RoadStatus {
    private final int[] carsOnRoad;
    private final PriorityQueue<Event> eventQueue;
    private int systemTime;
    private int currentGreenRoad;
    private int timeToSwitchGreen;
    private boolean[] isGreen;
    private int hadSwitchGreenLight;
    private boolean wait;

    private static class Event implements Comparable<Event> {
        int time;
        int roadId;
        int numCars; // Number of cars to add or delete for car events
        int type; // 0 for car addition, 1 for car removal
        private static int globalSequence = 0; // A static counter for all events
        private final int sequenceNumber; // Sequence number for this event

        Event(int time, int roadId, int numCars, int type) {
            this.time = time;
            this.roadId = roadId;
            this.numCars = numCars;
            this.type = type;
            this.sequenceNumber = globalSequence++; // Assign and increment the global sequence number
        }

        @Override
        public int compareTo(Event other) {
            // First, compare the time of the events
            if (this.time != other.time) {
                return this.time - other.time;
            }
            // If times are equal, compare the type of the events
            if (this.type != other.type) {
                return other.type - this.type;
            }
            // If both time and type are equal, compare the sequence numbers
            return this.sequenceNumber - other.sequenceNumber;
        }
    }

    public RoadStatus() {
        carsOnRoad = new int[3];
        eventQueue = new PriorityQueue<>();
        systemTime = 0; // System time
        currentGreenRoad = -1; // No green light initially
        timeToSwitchGreen = 0; // Time to switch the green light
        isGreen = new boolean[3]; // Traffic light states
        hadSwitchGreenLight = -1;
        wait = false;
    }

    public int[] roadStatus(int time) {
        // Process events up to but not including the current time
//        System.out.println("check time: "+time);
//        System.out.println("currentGreenRoad: "+currentGreenRoad);
//        System.out.println("timeToSwitchGreen: "+timeToSwitchGreen);
//        // print the current status of the roads
//        for(int i = 0; i<carsOnRoad.length;++i)
//            System.out.println("carsOnRoad["+i+"]: "+carsOnRoad[i]);

        processEventsUpTo(time);

        return Arrays.copyOf(carsOnRoad, carsOnRoad.length);
    }

    public void addCar(int time, int roadId, int num_of_cars) {
        eventQueue.add(new Event(time, roadId, num_of_cars, 0));
//        System.out.println("Event added at time " + time + " for road " + roadId + " with " + num_of_cars + " cars");
    }

    private void processEventsUpTo(int currentTime) {
        // 如果被call的時候是等待模式，則先處理等待模式
        if(wait){
            // 如果同一時間有加車事件，先處理加第一個加車事件，再選擇下一個綠燈的路
            if (!eventQueue.isEmpty() && eventQueue.peek().time == timeToSwitchGreen && eventQueue.peek().type == 0) {
                Event additionEvent = eventQueue.poll();
                carsOnRoad[additionEvent.roadId] += additionEvent.numCars;
                int tempTimeToSwitchGreen = timeToSwitchGreen;
                switchGreenLight(timeToSwitchGreen);
                hadSwitchGreenLight = tempTimeToSwitchGreen;
                systemTime = tempTimeToSwitchGreen; // new
                wait = false;
            // 如果時間已經超過上次切換的時間，直接切換燈號
            }else if(currentTime > timeToSwitchGreen){
                int tempTimeToSwitchGreen = timeToSwitchGreen;
                switchGreenLight(timeToSwitchGreen);
                hadSwitchGreenLight = tempTimeToSwitchGreen;
                wait = false;
            }
            // 如果時間還沒超過上次切換的時間又沒其他事件，則繼續等待
            else if(currentTime == systemTime && eventQueue.isEmpty()){
                wait = true;
            }
            else{
                wait = false;
            }
        }
        // 如果被call的時候紅燈，且沒有任何事件，則直接切換燈號
        if(eventQueue.isEmpty() && currentGreenRoad == -1 && currentTime >= timeToSwitchGreen){
            if(carsOnRoad[0] != 0 || carsOnRoad[1] != 0 || carsOnRoad[2] != 0){
                int tempTimeToSwitchGreen = timeToSwitchGreen;
                switchGreenLight(timeToSwitchGreen);
                hadSwitchGreenLight = tempTimeToSwitchGreen;
                systemTime = tempTimeToSwitchGreen; // new
            }
        }

        while (!eventQueue.isEmpty() && eventQueue.peek().time <= currentTime) {
            Event event = eventQueue.poll();
            systemTime = event.time; // Update system time to the time of the event

            // 如果目前是綠燈開著，且是移除事件，則先處理移除事件
            if(currentGreenRoad != -1 && event.type == 1){
//                System.out.println("Start to remove cars from road " + event.roadId + " at time " + currentTime);
                // 先計算這次要移除的車數
                int carsToRemove = Math.min(event.numCars, currentTime - systemTime + 1);
                carsOnRoad[event.roadId] -= carsToRemove;
//                System.out.println("Removed " + carsToRemove + " cars from road " + event.roadId + " at time " + systemTime);
                // 如果要離開的車超出這次範圍, 維持綠燈模式，並且加入下一個移除事件
                if (carsToRemove < event.numCars) {
//                    System.out.println("Car removal event from time " + systemTime + " to " + currentTime + " for road " + event.roadId + " with " + carsToRemove + " cars");
//                    System.out.println("Remaining cars to remove next time: " + (event.numCars - carsToRemove));
                    eventQueue.add(new Event(currentTime + 1, event.roadId, event.numCars - carsToRemove, 1));
                }
                else{
                    // 如果車子都移除完畢，則開始選擇下一個綠燈的路
//                    System.out.println("All cars removed from road " + event.roadId + " from time " + systemTime + " to " + timeToSwitchGreen);
//                    System.out.println("Remove total " + event.numCars + " cars from road " + event.roadId);
                    currentGreenRoad = -1;
                    int tempTimeToSwitchGreen = timeToSwitchGreen;
                    // 先把所有的加車事件處理完畢到目前時間
                    while (!eventQueue.isEmpty() && eventQueue.peek().time < timeToSwitchGreen && eventQueue.peek().type == 0) {
                        Event addEvent = eventQueue.poll();
                        carsOnRoad[addEvent.roadId] += addEvent.numCars;
                    }
                    // 如果同一時間有加車事件，先處理加第一個加車事件，再選擇下一個綠燈的路
                    if (!eventQueue.isEmpty() && eventQueue.peek().time == timeToSwitchGreen && eventQueue.peek().type == 0) {
                        Event addEvent = eventQueue.poll();
                        carsOnRoad[addEvent.roadId] += addEvent.numCars;
                        switchGreenLight(timeToSwitchGreen);
                        hadSwitchGreenLight = tempTimeToSwitchGreen;
                        systemTime = tempTimeToSwitchGreen; // new
                        continue;
                    }
                    // 如果當下沒有加車事件
                    else {
                        // 如果此刻還沒超出check的時間，直接切換燈號
                        if(timeToSwitchGreen < currentTime){
                            switchGreenLight(timeToSwitchGreen);
                            hadSwitchGreenLight = tempTimeToSwitchGreen;
                            systemTime = tempTimeToSwitchGreen; // new
                            continue;
                        }
                        // 如果此刻已經等於check的時間，先進入等待模式
                        else{
                            wait = true;
                            break;
                        }
                    }
                }
            }

            // 如果目前是綠燈開著，且上方的移除事件處理完畢 (Priority Queue 中同一時間的移除事件會先被處理)，則開始加車
            if(currentGreenRoad != -1 && event.type == 0){
                carsOnRoad[event.roadId] += event.numCars;
            }

            // 如果目前是全部是紅燈，而且是加車事件，則開始加車
            if(currentGreenRoad == -1 && event.type == 0){
                carsOnRoad[event.roadId] += event.numCars;
                // 先處理加第一個加車事件，再選擇下一個綠燈的路
                if(hadSwitchGreenLight != event.time && event.time >= timeToSwitchGreen){
                    switchGreenLight(event.time);
                    hadSwitchGreenLight = event.time;
                }
            }

            // 更新系統時間到最新的事件時間
            systemTime = event.time;
        }

        // 此次時間內的事件處理完畢，更新系統時間到當前時間
        systemTime = currentTime;
    }

    private void switchGreenLight(int currentTime) {
//        System.out.println("Switching green light at time " + currentTime);
        // Turn all lights to red
        Arrays.fill(isGreen, false);

        // Find the road with the most cars or the smallest index
        int roadWithMaxCars = -1;
        int maxCars = 0;
        for (int i = 0; i < carsOnRoad.length; i++) {
            if (carsOnRoad[i] > maxCars) {
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

        // add a new event to remove the cars from the road
        eventQueue.add(new Event(currentTime + 1, currentGreenRoad, carsOnRoad[currentGreenRoad], 1));

//        System.out.println("Green light on road " + currentGreenRoad + " from " + currentTime + " until time " + timeToSwitchGreen);
    }

    public static void main(String[] args)
    {
        // Example 1
        System.out.println("Example 1: ");
        RoadStatus sol1 = new RoadStatus(); // create a T-junction; all traffic lights are Red at the beginning
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
        RoadStatus sol2 = new RoadStatus(); // create a T-junction; all traffic lights are Red at the beginning
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
        RoadStatus sol3 = new RoadStatus(); // create a T-junction; all traffic lights are Red at the beginning
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

class OutputFormat{
    int[] answer;
    String func;
    int[] args;
}

class test{
    static boolean run_and_check(OutputFormat[] data, RoadStatus roadStat)
    {
        for(OutputFormat cmd : data)
        {
            if(cmd.func.equals("addCar"))
            {
                roadStat.addCar(cmd.args[0], cmd.args[1], cmd.args[2]);
            }
            else if(cmd.func.equals("roadStatus"))
            {
                int[] arr = roadStat.roadStatus(cmd.args[0]);
//                System.out.println("Expected: "+Arrays.toString(cmd.answer));
//                System.out.println("Output: "+Arrays.toString(arr));
                if(!Arrays.equals(arr,cmd.answer))
                    return false;
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
                if(run_and_check(data, new RoadStatus()))
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