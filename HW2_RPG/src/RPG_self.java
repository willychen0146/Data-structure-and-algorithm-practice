class RPG_self {
    private int[] defense;
    private int[] attack;

    public RPG_self(int[] defence, int[] attack){
        // Initialize some variables
        this.defense = defence;
        this.attack = attack;
    }
    public int maxDamage(int n){
        // return the highest total damage after n rounds.
        // You can only attack or boost in each round.
        // if you boost, you will get 2*attack[i] damage in the next round.
        int[] maxDamage = new int[n + 1]; // Array to store maximum damage achievable at each round
        boolean boost = false;
        int tempMaxDamage = 0;

        // Iterate through each round (except the last round, which will be calculated separately
        for (int round = 1; round < n; round++) {
            // Calculate this round's damage and round+1's damage
            int twoRoundDamageIfAttack = attack[round - 1] + attack[round] - defense[round - 1] - defense[round];
            // Calculate this next round's damage if boosting
            int twoRoundDamageIfBoost = 2 * attack[round] - defense[round];
            // If boosting last round, calculate damage and continue to next round
            if(boost) {
                int damageIfBoost = 2 * attack[round - 1] - defense[round - 1];
                maxDamage[round] = tempMaxDamage + damageIfBoost;
                boost = false;
                continue;
            }

            // If attacking last round, choose to attack or boost
            // If attacking is better, calculate damage and continue to next round
            if (twoRoundDamageIfAttack >= twoRoundDamageIfBoost) {
                int damageIfAttack = attack[round - 1] - defense[round - 1];
                maxDamage[round] = maxDamage[round - 1] + damageIfAttack;
            }
            // If boosting is better, set boost to true and store current maxDamage
            else {
                boost = true;
                tempMaxDamage = maxDamage[round - 1];
            }

        }

        // last round, directly calculate damage
        if(boost) {
            int damageIfBoost = 2 * attack[n - 1] - defense[n - 1];
            maxDamage[n] = tempMaxDamage + damageIfBoost;
        }
        else{
            int damageIfAttack = attack[n - 1] - defense[n - 1];
            maxDamage[n] = maxDamage[n - 1] + damageIfAttack;
        }

        return maxDamage[n];
    }
    public static void main(String[] args) {
        RPG_self sol = new RPG_self(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}