package hellojpa;

public class ValueMain {

  public static void main(String[] args) {
    Integer a = Integer.valueOf(10);
    Integer b = a;

    b = 50;

    System.out.println("a = " + a);
    System.out.println("b = " + b);
  }
}
