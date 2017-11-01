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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    
    //persist on db ( in-memory-db )
    @Autowired OrderRepository repository;
    
    Order baseOrder;
    
    public OrderTest() {
         baseOrder = Order.of( OrderNumber.of(1) );
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Tip 1 - Use Static Factory Methods instead of constructors
     */
    @Test
    public void should_use_factory() {
        
        BigDecimal cost = new BigDecimal(30);
        
        //old code
        // OrderItem shoes = new OrderItem("shoes", 3, cost );
        
        //new code - covariance supported
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        
    }

    /**
     * Tip 2 - Use Static Factory Methods instead of constructors
     */
    @Test
    public void defensive_programming() {
        
        Order someOrder = Order.of( OrderNumber.of(1) );
        
        //should not throw strange exceptions
        someOrder.addItem(null);
        
        assertTrue( someOrder.getItems().isEmpty() );
    }    

    /**
     * Tip 3 - Ovverride Equals
     */
    @Test
    public void override_equals() {
        
        OrderNumber loadFromSomeWhere = OrderNumber.of(1);
        
        //another object that rappresent the same order number
        OrderNumber loadFromSomeWhereElse = OrderNumber.of(1);
        
        assertTrue( loadFromSomeWhere.equals(loadFromSomeWhereElse) );
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
        
        assertTrue( number.contains(loadFromSomeWhereElse) );
    }  

    /**
     * Tip 4 - Ovverride Equals
     */
    @Test
    public void shoud_override_to_string_for_logging() {
        
        OrderNumber number = OrderNumber.of(1);
        
        System.out.println("LOG: "+number);
        
    }   

    /**
     * Tip 5 - Use formattable interface
     */
    @Test
    public void should_use_formattable_interface() {
        
        BigDecimal cost = new BigDecimal(30);
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        
        //TODO USE FORMATTABLE
    }    
    
    /**
     * Tip 6 - using ColumnConverter for single Value Objects
     */
    @Test
    public void should_use_ColumnConverter_for_value_objects() {
        
        //this is a Value Object - it will be put in the same table of Order as Column
        OrderNumber number = OrderNumber.of(5);
        
        Order order = Order.of( number );
        repository.save(order);
    }       

    /**
     * Tip 7 - Use @Embdeddable and @ElementCollection for list of Value Objects
     */
    @Test
    public void embeddable_and_embededd_collection() {
        
        BigDecimal cost = new BigDecimal(30);        
        
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        OrderItem shirts = OrderItem.of("shirts", 2, cost );
        
        Order order = Order.of( OrderNumber.of(2) );
        
        order.addItem(shoes);
        order.addItem(shirts);
        
        repository.save(order);
        
        Order reloadFromSomeWhereElse = repository.findByNumber( OrderNumber.of(2) );
        
        assertTrue( reloadFromSomeWhereElse.getItems().contains(shoes) );
    }    

    /**
     * Tip 8 - Use Immutability as much as possibile to reduce cuncurrency problems
     */
    @Test
    public void should_use_immutability() {
        
                
        BigDecimal cost = new BigDecimal(30);        
        
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        OrderItem shirts = OrderItem.of("shirts", 2, cost );
        
        Order order = Order.of( OrderNumber.of(2) );
        
        int quantity = order.getItems().getTotalQuantity();
        
        order.addItem(shoes);
        order.addItem(shirts);
        
        OrderItem caps = OrderItem.of("caps", 3, cost );

        //cannot add to getItemsList because it's immutable
        //order.getItemsList().add(caps);
                        
    }        
    

    /**
     * Tip 9 - Use a wrappers to share methods between list or set of the same object
     */
    @Test
    public void should_use_wrapper_for_managing_lists() {
        
                
        BigDecimal cost = new BigDecimal(30);        
        
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        OrderItem shirts = OrderItem.of("shirts", 2, cost );
        
        Order order = Order.of( OrderNumber.of(2) );
        
        int quantity = order.getItems().getTotalQuantity();
        
        order.addItem(shoes);
        order.addItem(shirts);
        
        OrderItem caps = OrderItem.of("caps", 3, cost );
        order.addToListOfDesires(caps);
        
        // items and listOfDesires are sharing same methods
        // improved redability
        order.getItems().getTotalValue();
        order.getListOfDesires().getTotalValue();
        
    } 

    
    /**
     * Tip 10 - Use the Java 8's Function to improve incapsulation
     */
    @Test
    public void should_use_function() {
                
        BigDecimal cost = new BigDecimal(30);        
        
        OrderItem shoes = OrderItem.of("shoes", 3, cost );
        OrderItem shirts = OrderItem.of("shirts", 2, cost );
        
        Order order = Order.of( OrderNumber.of(2) );
        order.addItem(shoes);
        order.addItem(shirts);
        
        BigDecimal totalBeforDiscount = order.getItems().getTotalValue();
        
        // order items collection not exposed
        order.apply( OrderItemOperations.DISCOUNT );
        
        BigDecimal discounted = order.getItems().getTotalValue();
        
        assertTrue( discounted.compareTo(totalBeforDiscount) == -1 );
    } 
}
