package eu.sanprojects.yanagaorders.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author sanjeya
 */
@Entity
public class Order implements Serializable{

    @Id
    @GeneratedValue
    Long id;
    
    /** Number of orders are a perfect candidate for Value Object*/
    private OrderNumber number;

    @ElementCollection
    List<OrderItem> items = Lists.newLinkedList();
    
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
        if ( !items.contains(item) ){
            items.add(item);
        }
        //TODO
        //can avoid duplicates here!!!
    }

    /** But the clinent should not add data freely to this model, 
     so it has to be immutable, step 2 use wrapper class for actions on lists 
     to make the code clearer
     i.e. order.getItems().getQuantity(); //get total amount of the items
     */
    public OrderItems getItems() {
        return OrderItems.of(items);
    }
    
}
