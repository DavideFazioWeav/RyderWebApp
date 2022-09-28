package co.develhope.myApp.auth.controller;

import co.develhope.myApp.auth.entities.Order;
import co.develhope.myApp.auth.entities.OrderDTO;
import co.develhope.myApp.auth.repositories.OrdersRepository;
import co.develhope.myApp.auth.services.OrderService;
import co.develhope.myApp.users.entities.User;
import co.develhope.myApp.users.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGISTERED')") //solo un user registrato può creare un ordine
    public ResponseEntity<Order> create(@RequestBody OrderDTO order) throws Exception{
        return ResponseEntity.ok(orderService.save(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getSingle(@PathVariable Long id, Principal principal){

        Optional<Order> order = ordersRepository.findById(id);
        if (!order.isPresent())return ResponseEntity.notFound().build();

        User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if(Roles.hasRole(user, Roles.REGISTERED) && order.get().getCreatedBy().getId() == user.getId()){
            return ResponseEntity.ok(order.get());

        }else if(Roles.hasRole(user, Roles.RESTAURANT) && order.get().getRestaurant().getId() == user.getId()) {
            return ResponseEntity.ok(order.get());

        }else if(Roles.hasRole(user, Roles.RIDER) && order.get().getRider().getId() == user.getId()){
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll(Principal principal){
        User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if(Roles.hasRole(user, Roles.REGISTERED)){
            return ResponseEntity.ok(ordersRepository.findByCreatedBy(user));
        } else if (Roles.hasRole(user, Roles.RESTAURANT)) {
            return ResponseEntity.ok(ordersRepository.findByRestaurant(user));
        } else if (Roles.hasRole(user, Roles.RIDER)){
            return ResponseEntity.ok(ordersRepository.findByRider(user));
        }
        return null;
    }
}
