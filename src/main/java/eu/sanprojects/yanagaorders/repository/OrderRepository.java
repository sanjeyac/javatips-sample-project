package eu.sanprojects.yanagaorders.repository;

import eu.sanprojects.yanagaorders.domain.Order;
import eu.sanprojects.yanagaorders.domain.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sanjeya
 */
public interface OrderRepository extends JpaRepository<Order, Long>{
    Order findByNumber(OrderNumber number);
}
