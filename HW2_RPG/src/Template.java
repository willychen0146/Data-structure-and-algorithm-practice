import java.util.Arrays;

class RPG_org {
    private int[] defense;
    private int[] attack;

    public RPG_org(int[] defence, int[] attack){
        // Initialize some variables
        this.defense = defence;
        this.attack = attack;
    }
    public int maxDamage(int n){
        // return the highest total damage after n rounds.
        // You can only attack or boost in each round.
        // if you boost, you will get 2*attack[i] damage in the next round.
        boolean[] boostIsbetter = new boolean[n]; // Array to store whether boosting is better at each round
        int[] maxDamage = new int[n + 1]; // Array to store maximum damage achievable at each round
        int[] boostValue = new int[n]; // Array to store boost value gain for each round

        // Calculate boost value gain for each round
        for (int round = 0; round < n; round++) {
            if(round == n-1){
                break;
            }
            int twoRoundDamageIfBoost = 2 * attack[round+1] - defense[round+1];
            int twoRoundDamageIfAttack = attack[round] + attack[round+1] - defense[round] - defense[round+1];
            boostValue[round] = twoRoundDamageIfBoost - twoRoundDamageIfAttack;
        }
        for(int i = 0; i < n; i++){
            System.out.println(boostValue[i]);
        }

        // find the greatest boost value gain and set it to true unless it's left or right is also true
        int round = 0;
        while (true) {
            System.out.println(round);
            if(boostValue[round] > 0) {
                int greatestBoostValue = -1;
                int greatestBoostValueIndex = -1;
                for (int i = round; i < n; i++) {
                    if (boostValue[i] > greatestBoostValue && boostIsbetter[i] == false) {
                        if(i > 0 && i < (n - 1)){
                            if(boostIsbetter[i-1] == false && boostIsbetter[i+1] == false){
                                greatestBoostValue = boostValue[i];
                                greatestBoostValueIndex = i;
                            }
                        }
                        else if(i == 0){
                            if(boostIsbetter[i+1] == false){
                                greatestBoostValue = boostValue[i];
                                greatestBoostValueIndex = i;
                            }
                        }
                        else if(i == n-1){
                            if(boostIsbetter[i-1] == false){
                                greatestBoostValue = boostValue[i];
                                greatestBoostValueIndex = i;
                            }
                        }
                        else {
                            greatestBoostValue = boostValue[i];
                            greatestBoostValueIndex = i;
                        }
                    }
                }
                System.out.println("greatestBoostValueIndex: "+greatestBoostValueIndex);
                if(greatestBoostValueIndex != -1){
                    boostIsbetter[greatestBoostValueIndex] = true;
                }
                else{
                    break;
                }
            }
            else if(round == n-1) {
                break;
            }
            else{
                round++;
            }
        }
        for(int i = 0; i < n; i++){
            System.out.println(boostIsbetter[i]);
        }

        int tempMaxDamage = 0;
        // Calculate sum of the maximum damage
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                if(boostIsbetter[i]){
                    continue;
                }
                else{
                    maxDamage[i] = attack[i] - defense[i];
                }
            }
            else {
                if(boostIsbetter[i-1] == true){
                    maxDamage[i] = 2 * attack[i] - defense[i] + maxDamage[i - 1];
                    tempMaxDamage = maxDamage[i];
                }
                else if(boostIsbetter[i-1] != true && boostIsbetter[i] != true){
                    maxDamage[i] = attack[i] - defense[i] + maxDamage[i - 1];
                    tempMaxDamage = maxDamage[i];
                }
                else if(boostIsbetter[i-1] != true && boostIsbetter[i] == true){
                    maxDamage[i] = tempMaxDamage;
                    continue;
                }
            }
        }
        for(int i = 0; i < n; i++){
            System.out.println("maxDamage"+i+" "+maxDamage[i]);
        }
        return maxDamage[n-1];
    }
    public static void main(String[] args) {
        RPG_org sol = new RPG_org(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}