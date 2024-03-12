import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays; // Used to print the arrays

import com.google.gson.*;

import java.util.Stack;

class Test{
    public static void main(String[] args){
        Mafia sol = new Mafia();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(args[0])){
            JsonArray all = gson.fromJson(reader, JsonArray.class);
            for(JsonElement caseInList : all){
                JsonArray a = caseInList.getAsJsonArray();
                int q_cnt = 0, wa = 0,ac = 0;
                for (JsonElement o : a) {
                    q_cnt++;
                    JsonObject person = o.getAsJsonObject();
                    JsonArray arg_lvl = person.getAsJsonArray("level");
                    JsonArray arg_rng = person.getAsJsonArray("range");
                    JsonArray arg_ans = person.getAsJsonArray("answer");
                    int LVL[] = new int[arg_lvl.size()];
                    int RNG[] = new int[arg_lvl.size()];
                    int Answer[] = new int[arg_ans.size()];
                    int Answer_W[] = new int[arg_ans.size()];
                    for(int i=0;i<arg_ans.size();i++){
                        Answer[i]=(arg_ans.get(i).getAsInt());
                        if(i<arg_lvl.size()){
                            LVL[i]=(arg_lvl.get(i).getAsInt());
                            RNG[i]=(arg_rng.get(i).getAsInt());
                        }
                    }
                    Answer_W = sol.result(LVL,RNG);
                    for(int i=0;i<arg_ans.size();i++){
                        if(Answer_W[i]==Answer[i]){
                            if(i==arg_ans.size()-1){
                                System.out.println(q_cnt+": AC");
                            }
                        }else {
                            wa++;
                            System.out.println(q_cnt+": WA");
                            break;
                        }
                    }

                }
                System.out.println("Score: "+(q_cnt-wa)+"/"+q_cnt);

            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

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

        // get the number of members
        int n = levels.length;

        // create an 1D array that stores the answer, length=n*2 (smallest and largest index of member that can be attacked by each member)
        int[] answer = new int[n*2];
//
//        // create an array of member objects
//        member[] members = new member[n];
//        for(int i = 0; i < n; i++){
//            members[i] = new member(levels[i], ranges[i], i);
//        }

        // stack
        Stack<Integer> stack = new Stack<>();

        // Process left limits
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && levels[stack.peek()] < levels[i]) {
                stack.pop();
            }
            int leftLimit = stack.isEmpty() ? 0 : stack.peek() + 1;
            answer[i * 2] = Math.max(i - ranges[i], leftLimit);
            stack.push(i);
        }

        // Clear the stack for the right limits
        stack.clear();

        // Process right limits
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && levels[stack.peek()] < levels[i]) {
                stack.pop();
            }
            int rightLimit = stack.isEmpty() ? n - 1 : stack.peek() - 1;
            answer[i * 2 + 1] = Math.min(i + ranges[i], rightLimit);
            stack.push(i);
        }

//        return answer;
/*
        // method: sorting
        // sort the members by their level in ascending order
        Arrays.sort(members, (a, b) -> a.Level - b.Level);
//        for(int i = 0; i < n; i++){
//            System.out.println("level:" + members[i].Level);
//            System.out.println("range:" + members[i].Range);
//            System.out.println("index:" + members[i].Index);
//        }


        // store the leftmost and rightmost index of member that can be attacked by each member
        for(int i = 0; i < n; i++){
            // if the member's level is the smallest, then it can attack no one
//            if(i == 0 || members[i].Level != members[i + 1].Level){
//                answer[members[i].Index * 2] = 0;
//                answer[members[i].Index * 2 + 1] = 0;
//            }
            int left = i;
            int right = i;
            int range_l = members[i].Range;
            int range_r = members[i].Range;
            // check left
            while(left > 0 && members[members[i].Index].Level != members[left - 1].Level && range_l > 0){
                left--;
                range_l--;
            }
            // check right
            while(right < n - 1 && members[members[i].Index].Level == members[i + 1].Level && range_r > 0){
                right++;
                range_r--;
            }
            System.out.println("index:" + members[i].Index);
            System.out.println("left:" + left);
            System.out.println("right:" + right);
            answer[members[i].Index * 2] = members[i- left].Index;
            answer[members[i].Index * 2 + 1] = members[i+ right].Index;
            // print this member's leftmost and rightmost index
//            System.out.println("Member:" + members[i].Index + "/" + answer[members[i].Index * 2] + " " + answer[members[i].Index * 2 + 1]);
        }
*/

        /* brute force N*N
        // screen the members by their level and range
        for(int i = 0; i < n; i++){
            int range_l = ranges[i];
            int range_r = ranges[i];
            int left = i;
            int right = i;
            // check left
            while(left > 0 && levels[i] > levels[left - 1] && range_l > 0){
                left--;
                range_l--;
            }
            // check right
            while(right < n - 1 && levels[i] > levels[right + 1] && range_r > 0){
                right++;
                range_r--;
            }
            // store the leftmost and rightmost index of member that can be attacked by each member
            answer[i * 2] = left;
            answer[i * 2 + 1] = right;
        }

         */

        return answer;
        // complete the code by returning an int[]
        // flatten the results since we only need an 1-dimentional array.
    }

    public static void main(String[] args) {
        Mafia sol = new Mafia();
        System.out.println(Arrays.toString(
                sol.result(new int[] {11, 13, 11, 7, 15},
                        new int[] { 1,  8,  1, 7,  2})));
        // Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
        //      => [a0, b0, a1, b1, a2, b2, a3, b3, a4, b4]
    }
}