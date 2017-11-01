package eu.sanprojects.yanagaorders.domain;

import com.google.common.collect.ForwardingList;
import java.io.Serializable;
import java.util.List;

/**
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
    
    public Integer getTotalValue(){
        return delegate
                .stream()
                .map(OrderItem::getQuantity)
                .reduce(0,Integer::sum);
    }    
}
