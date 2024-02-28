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
    private int highestTotalDamage;
    private int rounds;
    private int[] hadCalculate;

    public RPG(int[] defence, int[] attack){
        // Initialize some variables
        this.defense = defence;
        this.attack = attack;
        highestTotalDamage = 0;
        rounds = 0;
        hadCalculate = new int[attack.length];
        Arrays.fill(hadCalculate, -1);
    }
    public int maxDamage(int n){
        // return the highest total damage after n rounds.
        int[] maxDamage = new int[n + 1]; // Array to store maximum damage achievable at each round

        // Iterate through each round
        for (int round = 1; round <= n; round++) {
            // Calculate damage if choosing to attack
            int damageIfAttack = attack[round - 1] - defense[round - 1];
            if (round == 1 || round % 2 == 1) {
                // If it's the first round or an odd round, directly calculate damage
                maxDamage[round] = maxDamage[round - 1] + damageIfAttack;
            } else {
                // If it's an even round, consider whether boosting would be better
                int damageIfBoost = 2 * attack[round - 1] - defense[round - 1];
                maxDamage[round] = Math.max(maxDamage[round - 1] + damageIfAttack, maxDamage[round - 2] + damageIfBoost);
            }
        }

        return maxDamage[n];
    }
    public static void main(String[] args) {
        RPG sol = new RPG(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}