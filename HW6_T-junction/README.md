# HW6: T-junction (Event-Driven Simulation-Priority Queue Based)
## Problem
In a T-junction, there are 3 roads for cars to enter and exit the junction. In this problem, you will design an IoT road management system with traffic lights according to the following rules. We only need to determine which road is allowed to enter the junction. It does not matter which road a car selects to exit the junction, assuming the roads out of the junction are never congested.

## Rules
- There are three traffic lights controlling the three roads to enter the junction, respectively. Only at most one traffic light is Green at a time, while the others remain Red.
- The traffic lights will change state only at the end of a time unit.
- When all lights are Red or the current Green light is about to turn Red, the following rules are considered.
  - If there are one or multiple `addCar` events in that time unit, the next Green light will be determined RIGHT AFTER the first call of `addCar` by selecting the road with the most cars after the first `addCar` event. The duration of the Green light is determined by how many cars present on the selected road.
  - If no cars arrive in a time unit, the next Green light will be determined AT THE END of the time unit by selecting the road with the most cars. The duration of the Green light is determined by how many cars are present on the selected road.
  - If multiple roads have the same number of cars in that state, choose the road with the smallest index for the next Green light.
  - If no cars are present on any road, switch all traffic lights to Red.
- Cars always exit at the beginning of a time unit, before any new cars arrive.
- Only one car can exit in a time unit.

## Note
We guarantee that all the data would be input chronologically, and `roadStatus` time input is always later or equal to the last `addCars` time.

## Hint
You have to learn how to arrange the text into information! Try to give a little more patience on planning the logic before writing the codes.
Try to classify the rules first!

## Template
```java
class RoadStatus
{
    public int[] roadStatus(int time)
    {
        //return
        //    return the number of cars in each road.
        //    new int[]{num_of_car_0,num_of_car_1,num_of_car_2}
        return new int[]{0};
    }
    public void addCar(int time, int id, int num_of_cars)
    {
        //add a car to the queue of a specific id.
    }
    RoadStatus()
    {
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
```

## Expected Output
```java
Example1: 
t=0: [2, 0, 0] // output after first addCar call, chose road 0 as the green light with duration of 2
t=0: [2, 3, 0] // output after second addCar call
t=1: [1, 3, 0] // a car exits road 0
t=2: [4, 3, 0] // chose road 0 as the green light with duration of 4
t=3: [3, 3, 0] // a car exits road 0
t=4: [2, 3, 0] // a car exits road 0
t=5: [1, 3, 0] // a car exits road 0
t=6: [0, 3, 0] // a car exits road 0, chose road 1 as the next green light with duration of 3
t=7: [0, 2, 0] // a car exits road 1
t=8: [0, 1, 0] // a car exits road 1
t=9: [0, 0, 0] // a car exits road 1, no car on the road so switch red
t=10: [0, 0, 0] // no car on the road
-------------------------------------------------------------------
Example2: 
t=0: [2, 0, 0] // output after first addCar call, chose road 0 as the green light with duration of 2
t=0: [3, 0, 0] // output after second addCar call
t=1: [2, 0, 0] // a car exits road 0
t=2: [1, 2, 0] // a car exits road 0, 2 cars enter 1, chose road 1 as the green with duration of 2
t=3: [1, 1, 0] // a car exits road 1
t=4: [1, 0, 0] // a car exits road 1, chose road 0 as the next green light
t=5: [0, 0, 0] // a car exits road 0, no car on the road so switch red
t=6: [0, 0, 0] // no car on the road
-------------------------------------------------------------------
Example3: 
t=0: [1, 0, 0] // output after first addCar call, chose road 0 as the green light with duration of 1
t=1: [0, 0, 0] // a car exits road 0, switch to red
t=2: [0, 0, 0]
t=3: [0, 1, 0] // output after the first addcar call on time=3, chose road 1 as the next green light with duration of 1
t=3: [0, 2, 0] // a car enters road 1 at t=3 but it won't change the next green light choice and duration since it's already determined
t=4: [2, 1, 0] // a car exits road 1, and 2 cars enter road 0, choose road 0 as the next green light with duration of 2
t=5: [1, 1, 0]
t=6: [0, 1, 0]
t=7: [0, 0, 0]
t=8: [0, 0, 0]

// ...
```

## Test Data
```java
Time Limit: 20ms

N = Number of function calls

Case:
case1: 20 points, N <= 200
case2: 20 points, N <= 1000
case3: 20 points, N <= 2000
case4: 20 points, N <= 4000
case5: 20 points, N <= 5000
```

## File Download
[Test Code](https://drive.google.com/file/d/1IcLFj7b_38VUYlmc5L3AqeTCbVB9o1dw/view)

[Test Data](https://drive.google.com/file/d/1PPy8-O4kB7wgPvJtznPOGtFol8tyeI2c/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/HJwBD7osa)