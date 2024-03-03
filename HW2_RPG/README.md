# RPG (Dynamic programming)

## Backstory
You are playing a console game called "Dragon and Hero". You've finally reached the final boss battle. Your hero must beat up the Evil Dragon in order to win the game, but it's a big challenge. After failing countless times, You decided to find other ways to win the game (Cheating). You found a way to hack into the game's source code and discovered that the Evil Dragon's defense and the hero's attack was hardcoded into the program. The defense of the Evil Dragon and the hero's attack on each round doesn't change every time you replay the game. Therefore, you just have to find the perfect attack and boost combo with the knowledge you just learned from PDSA-2024-spring to win.

## Description
The game is round-based and for each round, the Hero can choose from one of the following actions:
1. **Attack:** The Hero attacks the Dragon with a weapon.
2. **Boost:** Instead of attacking right away, the Hero prepares for a super-strong attack on the next round. The attack is doubled only for the next round. Also, do remember that there's no damage done to the Dragon on the round the Hero chooses to boost. Since the boost only works for one round, the effect doesn't accumulate.

The defense of the Evil Dragon and the attack of the Hero are represented as arrays of integers, with each index corresponding to a round.
- Attack: `{a1, a2..., an}`
- Defense: `{d1, d2..., dn}`

The amount of damage caused on the Dragon after each round is obtained by subtracting the Evil Dragon's defense from the Hero's attack according to the arrays above based on your choice.
- Damage: `{e1, e2..., en}`
  - If you choose to attack: `Damage(k) = (a)k - (d)k`
  - If you choose to boost: `Damage(k) = 0`, and `Damage(k+1) = (2a)k+1 - (d)k+1`

## Specific task
For each round 1, 2, 3, and so on, we need to decide whether it's better to attack or boost. We want to figure out the best strategy that will give us the highest total damage after a specific number of rounds (let's call it n). Please return the highest total damage after n rounds.
`maxDamage(n) = max(sum(Damagek(k=1~n)))`

## Hint
Try memorization.

## Template
```java
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
```
## Examples
```Java
Example 1:
Attack: {235, 234}
Defense: {100, 20}
k = 1
Round 1: Attack
Damage = (235 - 100) = 135
Round 1: Boost
Damage = 0
maxDamage(1) = 135
```
```Java
Example 2:
Attack: {235, 234}
Defense: {100, 20}
k = 2
Round 1: Attack, Round 2: Attack
Damage = (235 - 100) + (234 - 20) = 349
Round 1: Boost, Round 2: Attack
Damage = 0 + (234*2 - 20) = 448
Round 1: Boost, Round 2: Boost
Damage = 0 + 0 = 0
Round 1: Attack, Round 2: Boost
Damage = (235 - 100) + 0 = 135
maxDamage(2) = 448
```
```Java
Example 3:
Attack: {235, 234, 200}
Defense: {100, 20, 150}
k = 3
Round 1: Attack, Round 2: Attack, Round 3: Attack
Damage = (235 - 100) + (234 - 20) + (200 - 150) = 399
Round 1: Boost, Round 2: Attack, Round 3: Attack
Damage = 0 + (234*2 - 20) + (200 - 150) = 498
Round 1: Attack, Round 2: Boost, Round 3: Attack
Damage = (235 - 100) + 0 + (2*200 - 150) = 385
Round 1: Attack, Round 2: Attack, Round 3: Boost
Damage = (235 - 100) + (234 - 20) + 0 = 349
Round 1: Boost, Round 2: Boost, Round 3: Attack
Damage = 0 + 0 + (2*200 - 150) = 250
Round 1: Attack, Round 2: Boost, Round 3: Boost
Damage = (235 - 100) + 0 + 0 = 135
Round 1: Boost, Round 2: Boost, Round 3: Boost
Damage = 0 + 0 + 0 = 0
maxDamage(3) = 498
```