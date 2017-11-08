/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.sanprojects.yanagaorders.test;

import eu.sanprojects.yanagaorders.domain.Order;
import eu.sanprojects.yanagaorders.domain.OrderItem;
import eu.sanprojects.yanagaorders.domain.OrderItemOperations;
import eu.sanprojects.yanagaorders.domain.OrderNumber;
import eu.sanprojects.yanagaorders.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author sanjeya
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    OrderRepository repository;

    /**
     * Tip 1 - Use Static Factory Methods instead of constructors
     */
    @Test
    public void should_use_static_factory() {

        BigDecimal cost = new BigDecimal(30);

        //new code - covariance supported
        OrderItem shoes = OrderItem.of("shoes", 3, cost);

        Assert.assertNotNull(shoes);

    }

    /**
     * Tip 2 - Use defensive programming with untrusted inputs on methods
     */
    @Test
    public void should_use_defensive_programming() {

        Order someOrder = Order.of(OrderNumber.of(1));
        someOrder.addItem(null);
        assertTrue(someOrder.getItems().isEmpty());
    }

    /**
     * Tip 3 - Ovverride Equals
     */
    @Test
    public void should_override_equals() {

        OrderNumber number = OrderNumber.of(1);
        //another object that rappresent the same order number
        OrderNumber sameNumber = OrderNumber.of(1);
        assertTrue(number.equals(sameNumber));

        OrderNumber differentNumber = OrderNumber.of(2);
        assertFalse(number.equals(differentNumber));

    }

    /**
     * Tip 3 bis - Override Equals
     */
    @Test
    public void override_equals_2() {

        OrderNumber loadFromSomeWhere = OrderNumber.of(1);

        //another object that rappresent the same order number
        OrderNumber loadFromSomeWhereElse = OrderNumber.of(1);

        List<OrderNumber> number = new ArrayList<>();
        number.add(loadFromSomeWhere);

        assertTrue(number.contains(loadFromSomeWhereElse));
    }

    /**
     * Tip 4 - Ovverride Equals
     */
    @Test
    public void shoud_override_to_string_for_logging() {
        OrderNumber number = OrderNumber.of(1);
        assertEquals("OrderNumber{value=1}", number.toString());
    }

    /**
     * Tip 5 - Use formattable interface
     */
    @Test
    public void should_use_formattable_interface() {

        BigDecimal cost = new BigDecimal(30);
        OrderItem shoes = OrderItem.of("shoes", 3, cost);

        //TODO: USE OF FORMATTABLE SAMPLE
    }

    /**
     * Tip 6 - using ColumnConverter for single Value Objects
     */
    @Test
    public void should_use_ColumnConverter_for_value_objects() {

        //OrderNumber a Value Object - it will be coverted in a number column
        OrderNumber number = OrderNumber.of(5);
        Order order = Order.of(number);
        repository.save(order);
    }

    /**
     * Tip 7 - Use @Embdeddable and @ElementCollection for list of Value Objects
     */
    @Test
    public void embeddable_and_embededd_collection() {

        BigDecimal cost = new BigDecimal(30);

        OrderItem shoes = OrderItem.of("shoes", 3, cost);
        OrderItem shirts = OrderItem.of("shirts", 2, cost);

        Order order = Order.of(OrderNumber.of(2));

        order.addItem(shoes);
        order.addItem(shirts);

        repository.save(order);

        Order reloadFromSomeWhereElse = repository.findByNumber(OrderNumber.of(2));

        assertTrue(reloadFromSomeWhereElse.getItems().contains(shoes));
    }

    /**
     * Tip 8 -  Use Immutability as much as possibile to reduce cuncurrency
     *          problems
     */
    @Test(expected = UnsupportedOperationException.class)
    public void should_use_immutability_where_possibile() {

        BigDecimal cost = new BigDecimal(30);
        OrderItem shoes = OrderItem.of("shoes", 3, cost);
        Order order = Order.of(OrderNumber.of(2));

        //cannot add to getItemsList because it's immutable
        order.getItemAsList().add(shoes);
    }

    /**
     * Tip 9 -  Use a decorator pattern to share methods between list or sets 
     *          of the same object
     */
    @Test
    public void should_use_decorator_pattern_for_managing_lists() {

        BigDecimal cost = new BigDecimal(30);

        OrderItem shoes = OrderItem.of("shoes", 3, cost);
        OrderItem shirts = OrderItem.of("shirts", 2, cost);

        Order order = Order.of(OrderNumber.of(2));
        order.addItem(shoes);
        order.addItem(shirts);

        OrderItem caps = OrderItem.of("caps", 3, cost);
        order.addToListOfDesires(caps);

        // items and listOfDesires are sharing same methods
        // improved redability using Decorator Pattern
        int quantity = order.getItems().getTotalQuantity();
        assertEquals(5, quantity);
        
        int desiredItemsQuantity = order.getListOfDesires().getTotalQuantity();
        assertEquals(3, desiredItemsQuantity);

    }

    /**
     * Tip 10 - Use the Java 8's Function to improve incapsulation
     *          Here we can apply an operation to all items without
     *          knowing how items are represented
     */
    @Test
    public void should_use_function_for_incapsulation() {

        BigDecimal cost = new BigDecimal(30);

        OrderItem shoes = OrderItem.of("shoes", 3, cost);
        OrderItem shirts = OrderItem.of("shirts", 2, cost);

        Order order = Order.of(OrderNumber.of(2));
        order.addItem(shoes);
        order.addItem(shirts);

        BigDecimal totalBeforeDiscount = order.getItems().getTotalValue();

        // order items collection not exposed
        // here we
        order.apply(OrderItemOperations.DISCOUNT);

        BigDecimal discounted = order.getItems().getTotalValue();
        
        //discounted is less the totalBeforeDiscount
        assertTrue(discounted.compareTo(totalBeforeDiscount) == -1);
    }
}
