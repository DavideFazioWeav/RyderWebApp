package co.develhope.myApp.users.repositories;

import co.develhope.myApp.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * search user by email
     * @param email email of the user
     * @return an email
     */
    User findByEmail(String email);

    /**
     * search user by activationCode
     * @param activationCode for enable user
     * @return an activationCode
     */
    User getByActivationCode(String activationCode);

    /**
     * search user by PasswordResetCode
     * @param passwordResetCode for reset password
     * @return a passwordResetCode
     */
    User findByPasswordResetCode(String passwordResetCode);

    /**
     * @deprecated this method belongs to another exercise
     * @return a rider
     */
    @Query(nativeQuery = true, value = "SELECT * FROM (\n" +
            "SELECT u.*, COUNT(busyOrders.id) AS numberOfOrders\n" +
            "FROM `user`AS u\n" +
            "LEFT JOIN user_roles AS ur ON ur.user_id = u.id\n" +
            "LEFT JOIN (SELECT * FROM `orders` WHERE `status` IN(4)) AS busyOrders ON busyOrders.rider_id = u.id\n" +
            "WHERE ur.role_id = 3 AND u.is_active = 1\n" +
            "GROUP BY u.id\n" +
            ") AS allRiders\n" +
            "WHERE allRiders.numberOfOrders = 0 \n" +
            "LIMIT 1")
    Optional<User> pickRider();


}
