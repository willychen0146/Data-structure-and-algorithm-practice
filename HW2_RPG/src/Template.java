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
        RPG_org sol = new RPG_org(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}