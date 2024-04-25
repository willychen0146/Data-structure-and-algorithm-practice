# Exam (Priority queue)
## Description

A prestigious university admits only students who rank in the top 20% among each of all the subjects. Scores range from 0 to 100. Write a program to identify these students and report their IDs.

Round up any decimals in the ranking. For example, the top 20% of 99 students is 19.8, which rounds up to include the top 20 students. No admissions beyond the set quota will be allowed. Students with lower IDs are prioritized for admission.

The program should output the results in descending order of total scores. In case of a tie, sort by ascending student ID. For instance:
1: 100
2: 80 (ID = 3)
3: 80 (ID = 5)
4: 70

## Hint
Is sorting essential if the final result is our only concern?

## Template
```java
class Exam {
    public static List<int[]> getPassedList(Integer[][] scores)
    {
        //input:
        //    scores: int[subject][id] 
        //    eg. scores[0][0] -> subject: 0, ID: 0
        //        scores[1][5] -> subject: 1, ID: 5

        //return:
        //    return a List of {ID, totalScore} 
        //    sorted in descending order of the total score
    }
    public static void main(String[] args) {
        List<int[]> ans = getPassedList(new Integer[][]
            {
                // ID:[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                {67,82,98,32,65,76,87,12,43,75,25},
                {42,90,80,12,76,58,95,30,67,78,10}
            }
        );
        for(int[] student : ans)
            System.out.print(Arrays.toString(student));
            // 11 students * 0.2 = 2.2 -> Top 3 students 
            // Output -> [6, 182][2, 178][1, 172]
        
        System.out.println(); // For typesetting
        
        ans = getPassedList(new Integer[][]
            {
                // ID:[0, 1, 2, 3, 4, 5]
                {67,82,64,32,65,76},
                {42,90,80,12,76,58}
            }
        );
        for(int[] student : ans)
            System.out.print(Arrays.toString(student));
            // 6 students * 0.2 = 1.2 -> Top 2 students 
            // Output -> [1, 172]
    } 
}
```

## Expected Output
```java
[6, 182][2, 178][1, 172]
[1, 172]
```

## TestCase
```java
Time Limit: 120ms

N = Number Of Subjects
M = Number Of Students

Case
case1: 20 points, N <= 3, M <= 40
case2: 20 points, N <= 5, M <= 800
case3: 20 points, N <= 10, M <= 2000
case4: 20 points, N <= 14, M <= 4000
case5: 20 points, N <= 16, M <= 5000
```

## File Download
[Test Code](https://drive.google.com/file/d/1lfV6BNV1nB86rB2gnnmHSJUnMsT_xaiC/view)

[Test Case](https://drive.google.com/file/d/1Fn7m3VzFNvYQcQIO0fVDv6OAyLSbviMe/view)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/@CiqLOooyRwWmK--mMkfetA/Hy3JDQoop)