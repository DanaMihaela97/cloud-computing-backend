package cloudcomputing.cc.repository;

import cloudcomputing.cc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
