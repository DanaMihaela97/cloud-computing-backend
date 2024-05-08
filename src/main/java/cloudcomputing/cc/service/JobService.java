package cloudcomputing.cc.service;

import cloudcomputing.cc.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job createJob(Job job);
    List<Job> getAllJobs();
    Optional<Job> getJobById(Long id);

}
