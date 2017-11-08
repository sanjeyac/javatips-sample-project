package eu.sanprojects.yanagaorders.domain;

import com.google.common.collect.ForwardingList;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * A decorator pattern for List,
 * Adds functionalities to List of OrderItem, 
 * in this way several list of this type
 * can share methods in a same place ( this class )
 * 
 * @author sanjeya
 */
public class OrderItems extends ForwardingList<OrderItem> implements Serializable{
    
    private final List<OrderItem> delegate;

    @Override
    protected List<OrderItem> delegate() {
        return delegate;
    }

    private OrderItems(List<OrderItem> delegate) {
        this.delegate = delegate;
    }
    

    public static OrderItems of(List<OrderItem> delegate) {
        return new OrderItems(delegate);
    }
    
    
    public Integer getTotalQuantity(){
        return delegate
                .stream()
                .map(OrderItem::getQuantity)
                .reduce(0,Integer::sum);
    }
    
    public BigDecimal getTotalValue(){
        return delegate
                .stream()
                .map(OrderItem::getValue)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }    
}
