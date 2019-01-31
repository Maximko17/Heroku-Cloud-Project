package maxcompany.realcloudapp.service;

import maxcompany.realcloudapp.domain.Backlog;
import maxcompany.realcloudapp.domain.Project;
import maxcompany.realcloudapp.domain.User;
import maxcompany.realcloudapp.exceptions.NotFoundException;
import maxcompany.realcloudapp.exceptions.ProjectIdException;
import maxcompany.realcloudapp.repository.BacklogRepository;
import maxcompany.realcloudapp.repository.ProjectRepository;
import maxcompany.realcloudapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdate(Project project,String username){

        if (project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                throw new NotFoundException("Project Not Found in your account");
            }else if (existingProject == null){
                throw new NotFoundException("Project with ID: '"+project.getProjectLeader()+"' can't updated because it doesn't exist");
            }
        }

        try{
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return  projectRepository.save(project);

        }catch (Exception e){
            throw new ProjectIdException("Project ID "+project.getProjectIdentifier().toUpperCase()+" already exists");
        }
    }

    public Project findByProjectId(String id, String username){

        Project project = projectRepository.findByProjectIdentifier(id.toUpperCase());

        if (project==null){
            throw new ProjectIdException("Project with id "+ id + " doesn't exist");
        }
        if (!project.getProjectLeader().equals(username)){
            throw new NotFoundException("Project Not Found in your account");
        }
        return project;
    }

    public List<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectById(String id,String username){

        projectRepository.delete(findByProjectId(id,username));
    }
}
