package eu.sanprojects.yanagaorders.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * An order contains several items.
 * 
 * In this case we have two List of Items:
 * - List of esires
 * - Effective Orders
 * 
 * @author sanjeya
 */
@Entity(name = "orders_table")
public class Order implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    /** Number of orders is a perfect candidate for Value Object*/
    private OrderNumber number;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    List<OrderItem> items = Lists.newLinkedList();
    
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    List<OrderItem> listOfDesires = Lists.newLinkedList();
    
    private Order(OrderNumber number) {
        this.number = number;
    }
    
    /** Use a static factory method because 
     * so it could be polymorphic and java has covariance **/
    public static Order of(OrderNumber number) {
        
        /** defensive **/
        Preconditions.checkNotNull(number);
        return new Order(number);
    }    
    
    /* Apply an operation to items without knowing of how items are implemented*/
    public void apply(OrderItemOperation operation){
        items = items.stream().map(operation).collect(Collectors.toList());
    }
    
    /** Abstracting Items implementation */ 
    public void addItem(OrderItem item){
        if ( item!=null && !items.contains(item) ){
            items.add(item);
        }
    }
    
    /** Abstracting Items implementation */ 
    public void addToListOfDesires(OrderItem item){
        if ( item!=null && !listOfDesires.contains(item) ){
            listOfDesires.add(item);
        }
    }    
    
    /** Use immutability to avoid concurrency problems */
    public List getItemAsList(){
        return ImmutableList.copyOf(items);
    }

    /** Using a List Wrapping class so I can share operations 
     * on collections of the same type 
     * and you don't want to expose the list implementation */
    
    /** PLEASE NOTE: this is an alternative to getItemAsList */
    public OrderItems getItems() {
        return OrderItems.of(items);
    }
    
    public OrderItems getListOfDesires() {
        return OrderItems.of(listOfDesires);
    }
    
    /** imposed by hibernate - should not be used **/
    @Deprecated
    public Order() {        
    }
        
}
