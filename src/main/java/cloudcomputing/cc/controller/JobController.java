package cloudcomputing.cc.controller;

import cloudcomputing.cc.config.SnsPublisher;
import cloudcomputing.cc.entity.Job;
import cloudcomputing.cc.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/openingjobs")
public class JobController {
    private final JobService jobService;
    private final SnsPublisher snsPublisher;
    @Autowired

    public JobController(JobService jobService, SnsPublisher snsPublisher) {
        this.jobService = jobService;
        this.snsPublisher = snsPublisher;
    }
    @PostMapping("/jobs")
    public Job createJob(@RequestBody Job job){
        Job savedJob=jobService.createJob(job);
        String subject = "New Jobs Posted!";
        String bodyText = "A new job has been posted on the platform. Check out the latest jobs at our website.";
        String platformUrl = "http://34.235.53.175:4200";
        bodyText += "\n\nVisit us at: " + platformUrl;
        snsPublisher.sendEmail(subject, bodyText);

        return savedJob;
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
