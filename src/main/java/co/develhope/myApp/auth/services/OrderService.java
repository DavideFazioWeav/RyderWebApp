package co.develhope.myApp.auth.services;

import co.develhope.myApp.auth.entities.Order;
import co.develhope.myApp.auth.entities.OrderDTO;
import co.develhope.myApp.auth.repositories.OrdersRepository;
import co.develhope.myApp.users.entities.User;
import co.develhope.myApp.users.repositories.UserRepository;
import co.develhope.myApp.users.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    public Order save(OrderDTO orderInput)throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(user);
        order.setAddress(orderInput.getAddress());
        order.setCity(orderInput.getCity());
        order.setDescription(orderInput.getDescription());
        order.setState(orderInput.getState());
        order.setNumber(orderInput.getNumber());
        order.setZipCode(orderInput.getZipCode());

        order.setPrice(orderInput.getPrice());

        //check for restaurant
        if(orderInput.getRestaurant() == null) throw new Exception("Restaurant not found");
        Optional<User> restaurant = userRepository.findById(orderInput.getRestaurant());
        if(!restaurant.isPresent() || !Roles.hasRole(restaurant.get(), Roles.RESTAURANT)) throw new Exception("Restaurant not found");

        order.setRestaurant(restaurant.get());

        return ordersRepository.save(order);
    }

    public Order update(Long id, Order orderInput){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (orderInput == null)return null;
        orderInput.setId(id);
        orderInput.setUpdatedAt(LocalDateTime.now());
        orderInput.setUpdatedBy(user);
        return ordersRepository.save(orderInput);
    }

    public boolean canEdit(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Order> order = ordersRepository.findById(id);
        if(!order.isPresent())return false;
        return order.get().getCreatedBy().getId() == user.getId();
    }
}
