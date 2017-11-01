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
    
    /** private construct so this constructor can't be used outside */
    private Order(OrderNumber number) {
        this.number = number;
    }
    
    /** Use a factory method because 
     * so it could be polymorphic and java has covariance **/
    public static Order of(OrderNumber number) {
        
        /** defensive **/
        Preconditions.checkNotNull(number);
        return new Order(number);
    }    
    
    public void apply(OrderItemOperation operation){
        items = items.stream().map(operation).collect(Collectors.toList());
    }
    
    /** Business logic of adding item here! */ 
    public void addItem(OrderItem item){
        if ( item!=null && !items.contains(item) ){
            items.add(item);
        }
        //TODO
        //can avoid duplicates here!!!
    }
    
    
    /** Business logic of adding item here! */ 
    public void addToListOfDesires(OrderItem item){
        if ( item!=null && !listOfDesires.contains(item) ){
            listOfDesires.add(item);
        }
        //TODO
        //can avoid duplicates here!!!
    }    
    
    /** Use immutability to avoid editing on values */
    public List getItemList(){
        return ImmutableList.copyOf(items);
    }

    /** Using a List Wrapping class I can share operations on collections of the same type*/
    public OrderItems getItems() {
        return OrderItems.of(items);
    }
    
    public OrderItems getListOfDesires() {
        return OrderItems.of(items);
    }
    
    // imposed by hibernate -should not be used
    @Deprecated
    public Order() {        
    }
        
}
