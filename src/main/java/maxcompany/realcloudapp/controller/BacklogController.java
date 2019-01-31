package maxcompany.realcloudapp.controller;


import maxcompany.realcloudapp.domain.Backlog;
import maxcompany.realcloudapp.domain.ProjectTask;
import maxcompany.realcloudapp.exceptions.NotFoundException;
import maxcompany.realcloudapp.repository.BacklogRepository;
import maxcompany.realcloudapp.service.MapValidationErrorService;
import maxcompany.realcloudapp.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult bindingResult, @PathVariable String backlog_id, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask,principal.getName());

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlog_id, Principal principal){
        return new ResponseEntity<>(projectTaskService.findBacklogicByID(backlog_id,principal.getName()),HttpStatus.OK);
    }

    @GetMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<ProjectTask> getProjectBySequence(@PathVariable String backlog_id,@PathVariable String task_id, Principal principal){

        ProjectTask projectTask = projectTaskService.findByProjectIDAndProjectSequence(backlog_id,task_id,principal.getName());

        return new ResponseEntity<>(projectTask,HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,BindingResult result,
                                               @PathVariable String backlog_id,@PathVariable String task_id, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateProjectTask(projectTask,backlog_id,task_id,principal.getName());

        return new ResponseEntity<>(updatedTask,HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id,@PathVariable String task_id, Principal principal){

        projectTaskService.deleteProjectTask(backlog_id,task_id,principal.getName());

        return new ResponseEntity<>("Project Task with ID: '"+ task_id.toUpperCase()+"' was deleted from project with ID: '"+ backlog_id.toUpperCase()+"'",HttpStatus.OK);
    }
}
