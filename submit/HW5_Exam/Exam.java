import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.princeton.cs.algs4.MinPQ;

import com.google.gson.*;

class Exam {
    public static List<int[]> getPassedList(Integer[][] scores) {
        //input:
        //    scores: int[subject][id]
        //    eg. scores[0][0] -> subject: 0, ID: 0
        //        scores[1][5] -> subject: 1, ID: 5

        //return:
        //    return a List of {ID, totalScore}
        //    sorted in descending order of the total score

        // get how many subjects and students
        int subjectsCount = scores.length;
        int studentsCount = scores[0].length;
        // Calculate the max number based on the top 20% of students (directly rounded up)
        int topPercentNum = (int) Math.ceil(studentsCount * 0.2);

        // Array to keep track of each student's total score
        int[] totalScores = new int[studentsCount];
        // Boolean array to keep track of students passing all subjects (assume all students pass all subjects)
        boolean[] passedAllSubjects = new boolean[studentsCount];
        Arrays.fill(passedAllSubjects, true);

        /* Priority Queue method */
        for (int i = 0; i < subjectsCount; i++){
            // Create a min heap to store the top scores of each subject
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(topPercentNum);
            for (int j = 0; j < studentsCount; j++){
                minHeap.offer(scores[i][j]);
                if (minHeap.size() > topPercentNum){
                    minHeap.poll(); // Remove the smallest score to maintain the top scores
                }
            }

            // Select the smallest score in the top percent and find the students who did not pass all subjects
            int thresholdScore = minHeap.peek(); // The smallest score in the top percent
            for (int j = 0; j < studentsCount; j++){
                // If the student's score is less than the threshold score, the student did not pass all subjects
                if (scores[i][j] < thresholdScore){
                    passedAllSubjects[j] = false;
                }
                totalScores[j] += scores[i][j];
            }
        }

        // Create a list to store the result
        List<int[]> resultList = new ArrayList<>();
        for (int i = 0; i < studentsCount; i++){
            if (passedAllSubjects[i]){
                resultList.add(new int[]{i, totalScores[i]});
            }
        }

        // Custom comparator to sort the result list in descending order of the total score
        Comparator<int[]> customComparator = (a, b) -> {
            if (a[1] == b[1]){
                return a[0] - b[0]; // If the total score is the same, sort by ID in ascending order
            }
            return b[1] - a[1]; // Sort by total score in descending order
        };

        // Sort the result list in descending order of the total score
        resultList.sort(customComparator);
//        resultList.sort((a, b) -> b[1] - a[1]);
        return resultList;
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

class OutputFormat{
    Integer[][] scores;
    List<int[]> answer;
}

class test_Exam{
    static boolean deepEquals(List<int[]> answer,List<int[]> answer2)
    {
        if(answer.size() != answer2.size())
            return false;
        for(int i = 0; i< answer.size(); ++i)
        {
            int[] a = answer.get(i);
            int[] b = answer2.get(i);
            if(!Arrays.equals(a, b))
            {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        OutputFormat[] datas;
        int num_ac = 0;
        List<int[]> user_ans;
        OutputFormat data;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat[].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];
                user_ans = Exam.getPassedList(data.scores);
                System.out.print("Sample"+i+": ");

                if(deepEquals(user_ans, data.answer))
                {
                    System.out.println("AC");
                    num_ac++;
                }
                else
                {
                    System.out.println("WA");
                    System.out.println("Data:      " + Arrays.deepToString(data.scores));
                    System.out.println("Test_ans:  " + Arrays.deepToString(data.answer.toArray()));
                    System.out.println("User_ans:  " + Arrays.deepToString(user_ans.toArray()));
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