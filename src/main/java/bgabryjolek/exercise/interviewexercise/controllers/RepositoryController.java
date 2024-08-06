package bgabryjolek.exercise.interviewexercise.controllers;

import bgabryjolek.exercise.interviewexercise.entities.Repository;
import bgabryjolek.exercise.interviewexercise.services.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RepositoryController {

    private final RepositoryService repositoryService;

    public RepositoryController(final RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Repository>> getRepository(@PathVariable String username) {
        return ResponseEntity.ok(repositoryService.dataFormatter(username));
    }
}
