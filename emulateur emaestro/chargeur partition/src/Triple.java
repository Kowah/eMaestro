public class Triple<L,M,R> {

	  private final L left;
	  private final M mid;
	  private final R right;

	  public Triple(L left, M mid, R right) {
	    this.left = left;
	    this.mid = mid;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public M getMid() { return mid; }
	  public R getRight() { return right; }
	  public String toString(){return ("("+left.toString()+", "+mid.toString()+", "+right.toString()+")");}
}
