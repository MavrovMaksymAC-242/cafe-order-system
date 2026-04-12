import java.util.ArrayList;
import java.util.List;

public class CafeAfter {

    // Виведення інтерфейсу DiscountPolicy
    interface DiscountPolicy {
        boolean isApplicable(double total);
        double apply(double total);
    }

    // Зміна ThresholdDiscount / NoDiscount з Conditional на Polymorphism
    static class ThresholdDiscount implements DiscountPolicy {
        private static final double THRESHOLD    = 10.0;
        private static final double DISCOUNT_RATE = 0.10;

        @Override
        public boolean isApplicable(double total) {
            return total > THRESHOLD;
        }

        @Override
        public double apply(double total) {
            return total * (1 - DISCOUNT_RATE);
        }
    }

    static class NoDiscount implements DiscountPolicy {
        @Override public boolean isApplicable(double total) { return false; }
        @Override public double apply(double total)         { return total; }
    }

    // Виведення класу та поле інкапсуляції OrderItem
    static class OrderItem {
        private final String name;
        private final double price;
        private final int    quantity;

        OrderItem(String name, double price, int quantity) {
            this.name     = name;
            this.price    = price;
            this.quantity = quantity;
        }

        public String getName()     { return name; }
        public double getPrice()    { return price; }
        public int    getQuantity() { return quantity; }

        // Виведення методу getRawTotal
        public double getRawTotal() {
            return price * quantity;
        }
    }

    // Виведення класу та представлення параметра Order
    static class Order {
        private final List<OrderItem>  items = new ArrayList<>();
        private final DiscountPolicy   discountPolicy;

        // Представлення параметра discountPolicy через конструктор
        Order(DiscountPolicy discountPolicy) {
            this.discountPolicy = discountPolicy;
        }

        public void addItem(OrderItem item) {
            items.add(item);
        }

        public List<OrderItem> getItems() { return items; }

        // Виведення методу getRawTotal
        private double getRawTotal() {
            return items.stream()
                    .mapToDouble(OrderItem::getRawTotal)
                    .sum();
        }

        // Виведення методу calculateTotal
        public double calculateTotal() {
            double raw = getRawTotal();
            return discountPolicy.isApplicable(raw)
                    ? discountPolicy.apply(raw)
                    : raw;
        }

        // Виведення методу hasDiscount
        public boolean hasDiscount() {
            return discountPolicy.isApplicable(getRawTotal());
        }
    }

    // Логіка виведення -> OrderPrinter
    static class OrderPrinter {
        public void print(Order order) {
            for (OrderItem item : order.getItems()) {
                System.out.printf("  %s x%d = $%.2f%n",
                        item.getName(), item.getQuantity(), item.getRawTotal());
            }
            if (order.hasDiscount()) {
                System.out.println("  Discount applied!");
            }
            System.out.printf("  TOTAL: $%.2f%n", order.calculateTotal());
        }
    }

    // Main
    public static void main(String[] args) {
        DiscountPolicy discount = new ThresholdDiscount();
        OrderPrinter   printer  = new OrderPrinter();

        // Перше Замовлення — Coffee x2 = $5.00, знижки немає
        Order order1 = new Order(discount);
        order1.addItem(new OrderItem("Coffee", 2.5, 2));
        printer.print(order1);

        // Друге Замовлення — Cake x3 = $12.00, знижка є
        Order order2 = new Order(discount);
        order2.addItem(new OrderItem("Cake", 4.0, 3));
        printer.print(order2);

        // Третє Замовлення — Tea + Sandwich = $11.50, знижка є
        Order order3 = new Order(discount);
        order3.addItem(new OrderItem("Tea",      1.5, 1));
        order3.addItem(new OrderItem("Sandwich", 5.0, 2));
        printer.print(order3);
    }
}