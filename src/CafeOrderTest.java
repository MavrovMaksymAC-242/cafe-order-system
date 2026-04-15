import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CafeOrderTest {

    interface DiscountPolicy {
        boolean isApplicable(double total);
        double apply(double total);
    }

    static class ThresholdDiscount implements DiscountPolicy {
        public boolean isApplicable(double t) { return t > 10.0; }
        public double apply(double t)         { return t * 0.9;  }
    }

    static class OrderItem {
        private final String name;
        private final double price;
        private final int    quantity;
        OrderItem(String name, double price, int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Кількість > 0");
            if (price    <= 0) throw new IllegalArgumentException("Ціна > 0");
            this.name = name; this.price = price; this.quantity = quantity;
        }
        public double getRawTotal() { return price * quantity; }
    }

    static class Order {
        private final List<OrderItem> items = new ArrayList<>();
        private final DiscountPolicy  policy;
        Order(DiscountPolicy policy) { this.policy = policy; }
        public void addItem(OrderItem i) { items.add(i); }
        public boolean isEmpty() { return items.isEmpty(); }
        private double getRawTotal() {
            return items.stream().mapToDouble(OrderItem::getRawTotal).sum();
        }
        public boolean hasDiscount()   { return policy.isApplicable(getRawTotal()); }
        public double  calculateTotal() {
            double r = getRawTotal();
            return policy.isApplicable(r) ? policy.apply(r) : r;
        }
    }

    private DiscountPolicy discount;

    @BeforeEach
    void setUp() { discount = new ThresholdDiscount(); }

    // Колонка 0: C1=0 → A4 Помилка
    @Test
    void test_Col0_InvalidQty_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderItem("Coffee", 2.5, 0));
    }

    // Колонка 4: C2=0 → A4 Помилка
    @Test
    void test_Col4_InvalidPrice_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderItem("Coffee", 0, 2));
    }

    // Колонка 6: C1=1,C2=1,C3=0 → A1+A3 без знижки
    @Test
    void test_Col6_ValidOrder_NoDiscount() {
        Order order = new Order(discount);
        order.addItem(new OrderItem("Coffee", 2.5, 2));
        assertFalse(order.isEmpty());
        assertFalse(order.hasDiscount());
        assertEquals(5.0, order.calculateTotal(), 0.001);
    }

    // Колонка 7: C1=1,C2=1,C3=1 → A1+A2+A3 зі знижкою
    @Test
    void test_Col7_ValidOrder_WithDiscount() {
        Order order = new Order(discount);
        order.addItem(new OrderItem("Cake", 4.0, 3));
        assertFalse(order.isEmpty());
        assertTrue(order.hasDiscount());
        assertEquals(10.8, order.calculateTotal(), 0.001);
    }

    // Від'ємна ціна > A4
    @Test
    void test_NegativePrice_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderItem("Tea", -1.5, 1));
    }
}
