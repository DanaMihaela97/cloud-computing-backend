package cloudcomputing.cc.service.impl;

import cloudcomputing.cc.entity.Job;
import cloudcomputing.cc.repository.JobRepository;
import cloudcomputing.cc.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobImpl implements JobService {
    private final JobRepository jobRepository;
    @Autowired
    public JobImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job) {
       return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }


    @Override
    public void deleteJobById(Long id) {
        jobRepository.deleteById(id);
    }
}
