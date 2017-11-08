package eu.sanprojects.yanagaorders.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author sanjeya
 */
@Embeddable
public class OrderItem implements Formattable{

    private String product;

    private Integer quantity;

    private BigDecimal value;

    private OrderItem(String product, Integer quantity, BigDecimal value) {
        this.product = product;
        this.quantity = quantity;
        this.value = value;
    }

    public static OrderItem of(String product, Integer quantity, BigDecimal value) {
        Preconditions.checkNotNull(product);
        Preconditions.checkNotNull(quantity);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(quantity > 0);
        return new OrderItem(product, quantity, value);
    }

    /* two items are equals if they have the same name */
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        if (obj instanceof OrderItem) {
            final OrderItem other = (OrderItem) obj;
            return Objects.equals(this.product, other.product);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value,this.product,this.quantity);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("product", product)
                .add("quantity", quantity)
                .add("value", value)
                .toString();
    }

    //TODO: correct
    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Integer getQuantity(){
        return quantity;
    }

    public String getProduct() {
        return product;
    }

    public BigDecimal getValue() {
        return value;
    }
    
    //imposed by hibernate - but should not be used, use factory method instead
    @Deprecated
    public OrderItem(){}

}
