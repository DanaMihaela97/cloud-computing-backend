package cloudcomputing.cc.repository;

import cloudcomputing.cc.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
