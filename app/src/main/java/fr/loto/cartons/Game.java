package fr.loto.cartons;
import java.util.*;
public final class Game {
 public static class Card { public String id; public int[][] grid=new int[3][9]; public Card(String id){this.id=id;} }
 public final ArrayList<Card> cards=new ArrayList<>(); public final ArrayList<Integer> drawn=new ArrayList<>(); public final HashSet<String> acknowledged=new HashSet<>(); public int mode=1;
 public void add(int n){if(n<1||n>90)throw new IllegalArgumentException("Numéro hors limites");if(!drawn.contains(n))drawn.add(n);}
 public void remove(int n){drawn.remove(Integer.valueOf(n)); acknowledged.clear();}
 public void replace(int oldN,int newN){if(newN<1||newN>90||drawn.contains(newN))throw new IllegalArgumentException("Numéro invalide ou déjà tiré");int i=drawn.indexOf(oldN);if(i<0)throw new IllegalArgumentException("Numéro absent");drawn.set(i,newN);acknowledged.clear();}
 public void clear(){drawn.clear();acknowledged.clear();}
 public int missing(Card c){int[] misses=new int[3];for(int r=0;r<3;r++)for(int n:c.grid[r])if(n!=0&&!drawn.contains(n))misses[r]++;Arrays.sort(misses);return misses[0]+(mode>=2?misses[1]:0)+(mode==3?misses[2]:0);}
 public boolean winner(Card c){return missing(c)==0;}
 public String winKey(Card c){return c.id+":"+mode;}
 public void validate(Card c){HashSet<Integer> seen=new HashSet<>();for(int r=0;r<3;r++){int count=0;for(int col=0;col<9;col++){int n=c.grid[r][col];if(n==0)continue;count++;if(n<1||n>90||!seen.add(n)||((n==90?8:n/10)!=col))throw new IllegalArgumentException("Carton invalide : "+c.id); }if(count!=5)throw new IllegalArgumentException("Ligne invalide : "+c.id);} }
}