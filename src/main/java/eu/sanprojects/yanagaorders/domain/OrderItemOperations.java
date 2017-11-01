package eu.sanprojects.yanagaorders.domain;

import java.math.BigDecimal;

/**
 *
 * @author sanjeya
 */

/** array of function that can be used in java streams */
public enum OrderItemOperations implements OrderItemOperation {
    
    DISCOUNT {
        @Override
        public OrderItem apply(OrderItem item) {
            return OrderItem.of(
                    item.getProduct(),
                    item.getQuantity(), 
                    item.getValue().multiply(BigDecimal.valueOf(0.8))
            );
        }
    };
}
