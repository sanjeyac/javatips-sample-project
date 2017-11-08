package eu.sanprojects.yanagaorders.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Objects;

/**
 * This class is a Value Object,
 * that  abstracts from the type of the Order Number,
 * so it can change in other formats in the future 
 * without big changes in the codebase
 * 
 * @author sanjeya
 */
public class OrderNumber implements Serializable, Formattable{

    private final Integer value;
    
    /** Use Factory methods instead of Constructors ( Effective Java book )*/
    private OrderNumber(Integer value) {
        this.value = value;
    }
    
    /** This can potentially return any subclass of OrderNumber for future extensions */
    public static OrderNumber of(Integer value) {
        
        /** Defensive programming statemens here**/
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument( value > 0 );
        
        return new OrderNumber(value);
    }

    /** Equals should compare the "value" attribute */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderNumber){
            OrderNumber other = (OrderNumber) obj;
            return Objects.equals(this.value, other.value);
        }
        return false;
    }
    
    /** The hashcode will be based on the "value" attribute */
    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    /** toString is used to logging purpose */
    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("value", value)
                .toString();
    }
    
    /** This will show data to the user with the I18N support*/
    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        formatter.format("%d",value);
    }
    
    /** Use to integer to not expose that the value is an integer value, so it won't break the incapsulation */
    public Integer toInteger(){
        return value;
    }
}
