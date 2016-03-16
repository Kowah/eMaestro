package util;
public class Pair<L,R> {
	
	private final L left;
	private final R right;
	
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public L getLeft() { return left; }
	public R getRight() { return right; }
	public String toString(){return right.toString();}//Affichage uniquement de la droite utile pour afficher la liste des morceaux sans leurs id
}