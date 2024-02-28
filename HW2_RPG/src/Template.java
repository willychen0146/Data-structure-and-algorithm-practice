class RPG {
    public RPG(int[] defence, int[] attack){
        // Initialize some variables
    }
    public int maxDamage(int n){
        // return the highest total damage after n rounds.
        return 0;
    }
    public static void main(String[] args) {
        RPG sol = new RPG(new int []{5,4,1,7,98,2},new int []{200,200,200,200,200,200});
        System.out.println(sol.maxDamage(6));
        //1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
        //maxDamage: 1187
    }
}