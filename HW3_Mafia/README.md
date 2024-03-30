# Mafia (Data Structure: Stack)

## Backstory
Following the death of the mafia's big boss in a recent police raid, a power vacuum has thrown the organization into chaos, with members fighting among themselves due to old grudges and issues. Without a clear leader, conflicts arise as former allies turn into rivals. As an undercover agent hiding in the organization, you have gathered all the needed information among all those mafia members. Your goal is to find out what will happen in the fight. The mafia members follow a very specific rule when it comes to fighting, it's called "tradition". The rules and other information you need to know are listed in the section below.

## Description
- To follow the tradition, the members line up in a straight line. 
- There are N members in the fight, with an index i denoting their position on the line. {i| 0, 1,....N-1}
- Each mafia member has two important traits: Level and Range. So there will be two sequences containing the level and range of the members according to their positions.
    - Level = {L0, L1, L2, ..., L(N-1)}
    - Range = {R0, R1, R2, ..., R(N-1)}

- To determine whether a member at position i can attack a member at position j, it must satisfy the following three conditions:

    1. |j-i| <= Ri: Member(j) must be within the range of member(i)'s range.
    2. L(j) < L(i): Member(i) must have a higher level than member(j).
    3. {Lk} < Li for all k{k|i+1 <= k <= j-1}: Member(i) can't attack member(j) if there's a member(k) with a higher level between member(i) and member(j).

- Here we denote that ai and bi will be the smallest index and the largest index the member(i) can attack. 
- Please determine the sequence of pairs Template {(a0, b0), ....., (a(N-1), b(N-1))}.

## Template
```java
import java.util.Arrays; // Used to print the arrays
class member{
    int Level;
    int Range;
    int Index;
    member(int _level,int _range, int i){
        Level=_level;
        Range=_range;
        Index=i;
    }
}
class Mafia {
    public int[] result(int[] levels, int[] ranges) {
          // Given the traits of each member and output 
          // the leftmost and rightmost index of member
          // can be attacked by each member.
        return ???; 
        // complete the code by returning an int[]
        // flatten the results since we only need a one-dimensional array.
    }

    public static void main(String[] args) {
        Mafia sol = new Mafia();
        System.out.println(Arrays.toString(
            sol.result(new int[] {11, 13, 11, 7, 15},
                         new int[] { 1,  8,  1, 7,  2})));
        // Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
        // => [a0, b0, a1, b1, a2, b2, a3, b3, a4, b4]
    }
}
```

## Test Data
N is the number of members

```Java
0 <= level <= 1000000000
0 <= range <= M

Time Limit: 300ms

We guarantee the array length of Level is always equal to the length of Range.

Case:
case1: 20 points: N <= 10, M < 10
case2: 20 points: N <= 200000, M <= 200000
case3: 20 points: N <= 10000, M <= 5000
case4: 20 points: N <= 400000, M <= 200000
case5: 20 points: N <= 1000000, M <= 500000
```

## File Download
[Test Code](https://drive.google.com/file/d/1yVh94U3YBC50RBbKHgRBBaTTrG67y5z-/view?usp=sharing)

[Test Data](https://drive.google.com/file/d/1yDRTo08srv67IAKu_ylB8T-7BUlm8zma/view?usp=sharing)

[algs4.jar library](https://algs4.cs.princeton.edu/code/algs4.jar)

[gson.jar library](https://drive.google.com/file/d/1gUhlPLTc4EA8P-R_qf3a4uynCQeR0TgH/view?usp=drive_link)

## Reference
[PDSA](https://hackmd.io/6NArMefdRTSkQvsgbUnnOw?view)