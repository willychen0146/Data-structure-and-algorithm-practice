import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import com.google.gson.*;

class OutputFormat{
    int[] defence;
    int[] attack;
    int k;
    int answer;
}

class test_RPG{
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        OutputFormat[] datas;
        int num_ac = 0;
        int user_ans;
        OutputFormat data;

        try {
            datas = gson.fromJson(new FileReader(args[0]), OutputFormat[].class);
            for(int i = 0; i<datas.length;++i)
            {
                data = datas[i];
                user_ans = new RPG(data.defence, data.attack).maxDamage(data.k);
                System.out.print("Sample"+i+": ");
                if(data.answer == user_ans)
                {
                    System.out.println("AC");
                    num_ac++;
                }
                else
                {
                    System.out.println("WA");
                    System.out.println("Data_atk:  " + Arrays.toString(data.attack));
                    System.out.println("Data_dfc:  " + Arrays.toString(data.defence));
                    System.out.println("Test_ans:  " + data.answer);
                    System.out.println("User_ans:  " + user_ans);
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
// paste your own RPG class here :)
class RPG {
    private int[] defense;
    private int[] attack;
    private int[][] memo;

    public RPG(int[] defence, int[] attack){
        // Initialize some variables
        this.defense = defence;
        this.attack = attack;
        this.memo = new int[attack.length + 1][2]; // Memoization array to store maximum damage achievable at each round
        for (int[] m : memo) {
            Arrays.fill(m, -1); // Initialize memoization array with -1
        }
    }

    // Method: dynamic programming - assume maximum damage store in memo array until n-1 rounds is always optimal
    public int maxDamage(int n){
        // return the highest total damage after n rounds.
        // You can only attack or boost in each round.
        // if you boost, you will get 2*attack[i] damage in the next round.

        // Iterate through each round (if attack or boost is chosen, store the value into memo array)
        for (int round = 1; round <= n; round++) {
            // Calculate damage in the first round
            if(round == 1){
                int damageIfAttack = Math.max(attack[round - 1] - defense[round - 1], 0);
                // Store the damage into memo array (0: attack, 1: boost)
                memo[round][0] = damageIfAttack;
                memo[round][1] = 0;
                continue;
            }
            // Calculate damage if attacking or boosting after x rounds
            int damageIfAttack = Math.max(attack[round - 1] - defense[round - 1], 0);
            int damageIfBoost = Math.max(2 * attack[round - 1] - defense[round - 1], 0);
            // Store the damage into memo array (0: attack, 1: boost), *** assume maximum damage until n rounds is always perfect ***
            // If attack is chosen, the maximum damage until this round:
            // maximum of the previous round's damage + this round's attack damage
//            memo[round][0] = Math.max(dp(round - 1, 0) + damageIfAttack, dp(round - 1, 1) + damageIfBoost);
            memo[round][0] = Math.max(memo[round - 1][0] + damageIfAttack, memo[round - 1][1] + damageIfBoost);
            // If boost is chosen, the maximum damage until this round:
            // maximum of the previous round's damage + 0 (boost damage)
//            memo[round][1] = Math.max(dp(round - 1, 0), dp(round - 1, 1));
            memo[round][1] = Math.max(memo[round - 1][0], memo[round - 1][1]);
        }
        // Return the maximum damage after n rounds (choose the maximum damage in the last round between attack and boost)
        return Math.max(memo[n][0], memo[n][1]);
    }

    // Method: return the maximum damage in the memo array
//    public int dp(int round, int choice){
//        return memo[round][choice];
//    }

    public static void main(String[] args) {
        RPG sol = new RPG(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}