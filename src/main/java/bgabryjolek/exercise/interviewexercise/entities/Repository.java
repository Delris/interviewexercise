package bgabryjolek.exercise.interviewexercise.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    private String ownerLogin;
    private String repositoryName;
    @JsonProperty("branches")
    private List<Branch> listOfBranches = new ArrayList<>();

    public Repository(String ownerLogin, String repositoryName) {
        this.ownerLogin = ownerLogin;
        this.repositoryName = repositoryName;
    }

    public void addBranch(Branch branch) {
        listOfBranches.add(branch);
    }

    public void displayAllFields() {
        System.out.println("=====================================");
        System.out.println("Owner :" + ownerLogin);
        System.out.println("Repository :" + repositoryName);
        for (Branch branch : listOfBranches) {
            System.out.println("Branch name :" + branch.getName());
            System.out.println("Branch SHA :" + branch.getCommitSha());
        }
    }
}
