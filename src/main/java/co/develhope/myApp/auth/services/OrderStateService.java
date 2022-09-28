package co.develhope.myApp.auth.services;

import co.develhope.myApp.auth.entities.Order;
import co.develhope.myApp.auth.entities.OrderStateEnum;
import co.develhope.myApp.auth.repositories.OrdersRepository;
import co.develhope.myApp.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderStateService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private RiderService riderService;

    public Order setAccept(Order order) throws Exception{
        if(order == null) throw new NullPointerException();
        if(order.getStatus() != OrderStateEnum.CREATED) throw new Exception("Cannot edit order");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order.getRestaurant().getId() != user.getId()) throw new Exception("This is not your order");
        // Additional actions (!)

        //go ahead one step
        order.setStatus(OrderStateEnum.ACCEPTED);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user);
        return ordersRepository.save(order);
    }

    public Order setInPreparation(Order order) throws Exception{
        if(order == null) throw new NullPointerException();
        if(order.getStatus() != OrderStateEnum.ACCEPTED) throw new Exception("Cannot edit order");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order.getRestaurant().getId() != user.getId()) throw new Exception("This is not your order");

        //go ahead one step
        order.setStatus(OrderStateEnum.IN_PREPARATION);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user);
        return ordersRepository.save(order);
    }

    public Order setReady(Order order) throws Exception{
        if(order == null) throw new NullPointerException();
        if(order.getStatus() != OrderStateEnum.IN_PREPARATION) throw new Exception("Cannot edit order");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order.getRestaurant().getId() != user.getId()) throw new Exception("This is not your order");

        //Rider selection
        User rider = riderService.pickRider();
        order.setRider(rider);

        //go ahead one step
        order.setStatus(OrderStateEnum.READY);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user);
        return ordersRepository.save(order);
    }

    public Order setDelivery(Order order) throws Exception{
        if(order == null) throw new NullPointerException();
        if(order.getStatus() != OrderStateEnum.READY) throw new Exception("Cannot edit order");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order.getRider().getId() != user.getId()) throw new Exception("This is not your order");

        //go ahead one step
        order.setStatus(OrderStateEnum.DELIVERING);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user);
        return ordersRepository.save(order);
    }

    public Order setComplete(Order order) throws Exception{
        if(order == null) throw new NullPointerException();
        if(order.getStatus() != OrderStateEnum.DELIVERING) throw new Exception("Cannot edit order");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(order.getRider().getId() != user.getId()) throw new Exception("This is not your order");

        //go ahead one step
        order.setStatus(OrderStateEnum.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(user);
        return ordersRepository.save(order);
    }
}

