package bgabryjolek.exercise.interviewexercise.services;

import bgabryjolek.exercise.interviewexercise.entities.Branch;
import bgabryjolek.exercise.interviewexercise.entities.Repository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryService {

    private final RestClient restClient = RestClient.builder().defaultHeader("Authorization", "Bearer " + System.getenv("GITHUB_SECRET")).build();

    private final String reposURI = "https://api.github.com/users/{username}/repos";
    private final String branchesURI = "https://api.github.com/repos/{username}/{repository}/branches";
    private final String usersURI = "https://api.github.com/users/{username}";

    public ResponseEntity<String> findAllRepositoriesByUsername(String username) {
        return restClient
                .get()
                .uri(reposURI, username)
                .retrieve()
                .toEntity(String.class);
    }

    public ResponseEntity<String> findAllCommitsByRepository(String repository, String username) {
        return restClient
                .get()
                .uri(branchesURI, username, repository)
                .retrieve()
                .toEntity(String.class);
    }

    public void checkIfUserExists(String username) {
        ResponseEntity<String> response = restClient
                .get()
                .uri(usersURI, username)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, response.getBody());
        }
    }

    public List<Repository> dataFormatter(String username) {
        checkIfUserExists(username);
        ResponseEntity<String> response = findAllRepositoriesByUsername(username);
        if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return List.of();
        }

        List<Repository> repositoryList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response.getBody());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ownerObject = jsonArray.getJSONObject(i).getJSONObject("owner");
            if (!jsonArray.getJSONObject(i).get("fork").toString().equals("true")) {
                repositoryList.add(new Repository(ownerObject.get("login").toString(), jsonArray.getJSONObject(i).get("name").toString()));
            }
        }
        for (Repository repository : repositoryList) {
            ResponseEntity<String> commitResponse = findAllCommitsByRepository(repository.getRepositoryName(), repository.getOwnerLogin());
            if (commitResponse.getStatusCode() == HttpStatus.OK) {
                JSONArray commitArray = new JSONArray(commitResponse.getBody());
                for (int i = 0; i < commitArray.length(); i++) {
                    Branch branch = new Branch(commitArray.getJSONObject(i).getString("name"), commitArray.getJSONObject(i).getJSONObject("commit").getString("sha"));
                    repository.addBranch(branch);
                }
            }
        }

        return repositoryList;
    }
}
