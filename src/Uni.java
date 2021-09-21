import java.util.Scanner;

public class Uni {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int index = sc.nextInt();
        if (index <= '/' && index >= '!') {
            System.out.println(index - '!' + 1 + '' - '');
        }
        else if((index <= '' && index >= '')) {
            System.out.println(index - '');
        }
        else if (index >= ':'){
            System.out.println(index - ':' + (1 + '/' - '!' + '' - ''));
        }
        sc.close();
    }
}
