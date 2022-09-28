package co.develhope.myApp.auth.repositories;

import co.develhope.myApp.auth.entities.Order;
import co.develhope.myApp.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrdersRepository extends JpaRepository <Order,Long> {

    List<Order> findByCreatedBy(User user);

    List<Order> findByRestaurant(User user);

    List<Order> findByRider(User user);
}
