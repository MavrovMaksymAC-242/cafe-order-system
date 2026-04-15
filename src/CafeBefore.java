public class CafeBefore {
    public static void main(String[] args) {
        // Перше замовлення
        String item1 = "Coffee";
        double price1 = 2.5;
        int qty1 = 2;
        double total1 = price1 * qty1;
        if (total1 > 10) {
            total1 = total1 * 0.9;
            System.out.println("Discount applied!");
        }
        System.out.println("Order: " + item1 + " x" + qty1 + " = $" + total1);

        // Друге замовлення
        String item2 = "Торт";
        double price2 = 4.0;
        int qty2 = 3;
        double total2 = price2 * qty2;
        if (total2 > 10) {
            total2 = total2 * 0.9;
            System.out.println("Discount applied!");
        }
        System.out.println("Order: " + item2 + " x" + qty2 + " = $" + total2);

        // Третє замовлення
        String item3 = "Tea";
        double price3 = 1.5;
        int qty3 = 1;
        double total3 = price3 * qty3;
        if (total3 > 10) {
            total3 = total3 * 0.9;
            System.out.println("Discount applied!");
        }
        System.out.println("Order: " + item3 + " x" + qty3 + " = $" + total3);
        System.out.println("Hello from GitHub!");
    }
}