package co.develhope.myApp.auth.entities;


import co.develhope.myApp.users.entities.User;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * is a base entity that contains attribute for Appointment class and other
 * it is mapped as a superclass
 */
@MappedSuperclass
@Data
public class BaseEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;
}
