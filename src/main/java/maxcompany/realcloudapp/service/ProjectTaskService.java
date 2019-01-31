package maxcompany.realcloudapp.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import maxcompany.realcloudapp.domain.Backlog;
import maxcompany.realcloudapp.domain.Project;
import maxcompany.realcloudapp.domain.ProjectTask;
import maxcompany.realcloudapp.exceptions.NotFoundException;
import maxcompany.realcloudapp.repository.BacklogRepository;
import maxcompany.realcloudapp.repository.ProjectRepository;
import maxcompany.realcloudapp.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findByProjectId(projectIdentifier, username).getBacklog();
        projectTask.setBacklog(backlog);

        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;

        backlog.setPTSequence(backlogSequence);
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public List<ProjectTask> findBacklogicByID(String backlog_id,String username) {
        projectService.findByProjectId(backlog_id,username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findByProjectIDAndProjectSequence(String backlog_id, String sequence,String username) {

        projectService.findByProjectId(backlog_id,username);

        ProjectTask projectTask = projectTaskRepository.findByProjectIdentifierAndProjectSequence(backlog_id, sequence);
        if (projectTask == null) {
            throw new NotFoundException("Project Task with ID: '" + sequence.toUpperCase() + "' not found in project '" + backlog_id.toUpperCase() + "' ");
        }
        return projectTask;
    }

    public ProjectTask updateProjectTask(ProjectTask projectTask, String project_id, String sequence,String username) {
        ProjectTask updateTask = findByProjectIDAndProjectSequence(project_id, sequence,username);

        updateTask = projectTask;

        return projectTaskRepository.save(updateTask);
    }

    public void deleteProjectTask(String project_id, String sequence,String username) {
        ProjectTask projectTask = findByProjectIDAndProjectSequence(project_id, sequence,username);

        projectTaskRepository.delete(projectTask);
    }
}
