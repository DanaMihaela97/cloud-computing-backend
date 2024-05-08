package cloudcomputing.cc.controller;

import cloudcomputing.cc.entity.Job;
import cloudcomputing.cc.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/v1/openingjobs")
public class JobController {
    private final JobService jobService;
    @Autowired

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs")
    public Job createJob(@RequestBody Job job){
        return jobService.createJob(job);
    }
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> allJobs(){
       return ResponseEntity.ok(jobService.getAllJobs());
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Optional<Job>> getJobById(@PathVariable Long id){
        Optional<Job> jobInfo = jobService.getJobById(id);
        return ResponseEntity.ok(jobInfo);
    }

}
