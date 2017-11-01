package eu.sanprojects.yanagaorders.database.converters;

import eu.sanprojects.yanagaorders.domain.OrderNumber;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author sanjeya
 */
@Converter(autoApply = true)
public class OrderNumberAttributesConverter implements AttributeConverter<OrderNumber, Integer>{

    @Override
    public Integer convertToDatabaseColumn(OrderNumber orderNumber) {
        if (orderNumber == null ){
            return null;
        }
        return orderNumber.toInteger();
    }

    @Override
    public OrderNumber convertToEntityAttribute(Integer value) {
        if ( value == null ){
            return null;
        }
        return OrderNumber.of(value);
    }
    
}
