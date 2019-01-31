package maxcompany.realcloudapp.controller;

import maxcompany.realcloudapp.domain.Project;
import maxcompany.realcloudapp.service.MapValidationErrorService;
import maxcompany.realcloudapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap!=null)return errorMap;

       Project project1 = projectService.saveOrUpdate(project,principal.getName());
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id,Principal principal){

        Project project = projectService.findByProjectId(id.toUpperCase(),principal.getName());

        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Project> findAllProjects(Principal principal){
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id,Principal principal) {
        projectService.deleteProjectById(id.toUpperCase(),principal.getName());

        return new ResponseEntity<>("Project with id "+id+" was deleted",HttpStatus.OK);
    }
}
