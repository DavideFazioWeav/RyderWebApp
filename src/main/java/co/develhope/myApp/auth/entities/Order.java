package co.develhope.myApp.auth.entities;

import co.develhope.myApp.users.entities.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    private String address;
    private String number;
    private String city;
    private String zipCode;
    private String state;

    private OrderStateEnum status = OrderStateEnum.CREATED;

    @ManyToOne
    private User restaurant;

    @ManyToOne
    private User rider;

    private double price;

}