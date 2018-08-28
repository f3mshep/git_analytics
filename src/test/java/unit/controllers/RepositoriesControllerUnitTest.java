package unit.controllers;

import app.Application;
import app.controllers.ContributorsController;
import app.controllers.RepositoriesController;
import app.models.Contributor;
import app.models.Repo;
import app.models.Commit;
import app.models.helpers.*;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GitHub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;
import app.models.helpers.wrappers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class RepositoriesControllerUnitTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private Contributor contributor;
    private Repo repo;
    private Commit commit;

    private List<Commit> commitList = new ArrayList<>();
    private List<Repo> repoList = new ArrayList<>();

    @Mock
    ContributorRepository contributorRepository;


    @Mock
    CommitRepository commitRepository;

    @Mock
    RepoRepository repoRepository;

    @Mock
    APIWrapper apiWrapper = createMockWrapper(contributorRepository, repoRepository, commitRepository);

    @InjectMocks
    RepositoriesController repositoriesController;

    public RepositoriesControllerUnitTest() throws Exception {
    }

    @BeforeEach
    public void setup() throws Exception{

        this.mockMvc = MockMvcBuilders.standaloneSetup(repositoriesController).build();
        repositoriesController.setWrapper(apiWrapper);
        Date timeStamp = new Date();
        this.contributor = new Contributor("f3mshep", "Github");
        this.repo = new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitHub")
                .setSummary("A real holler and a hootnanny!")
                .setTitle("The best Repo Stub ever")
                .setUrl("http://github.com/totally_real/really")
                .createRepo();
        repoList.add(repo);
        repoList.add(new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitBucket")
                .setSummary("Whoo doggies")
                .setTitle("The best Repo Stub ever")
                .setUrl("http://gitbucket.com/totally_real/really")
                .createRepo());
        this.commit = new CommitBuilder()
                .setUrl("http://github.com/totally_real/really")
                .setTimestamp(timeStamp)
                .setStatus("super commit ftw")
                .setRepo(repo)
                .setContributor(contributor)
                .createCommit();
        commitList.add(commit);
        commitList.add(new CommitBuilder()
                .setUrl("http://github.com/totally_real/really")
                .setTimestamp(new Date())
                .setStatus("super commit pt 2")
                .setRepo(repo)
                .setContributor(contributor)
                .createCommit()
                );

    }

    @Test
    void shouldReturnRepoWithValidId() throws Exception {
        long id = 1;
        // when(repoRepository.findById(fakeID)).thenReturn(Optional.of(repo));
        System.out.println("Looking for mock call:");
        doReturn(Optional.of(repo)).when(repoRepository).findById(id);

        mockMvc.perform(get("/repos/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.title", equalTo(repo.getTitle())))
                .andExpect(jsonPath("$.summary",  equalTo(repo.getSummary())))
                .andExpect(jsonPath("$.url",  equalTo(repo.getUrl())))
                .andExpect(jsonPath("$.platform",  equalTo(repo.getPlatform())))
                .andExpect(jsonPath("$.contributors",  hasSize(1)));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        long invalidId = 999;
        doReturn(Optional.empty()).when(repoRepository).findById(invalidId);

        mockMvc.perform(get("/repos" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRepoObject() throws Exception {
        doReturn(printMe(repo)).when(apiWrapper).buildRepo("totally_real/really");
        doNothing().when(apiWrapper).updateCommits(repo);
        mockMvc.perform(post("/repos?url=totally_real/really"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.title", equalTo(repo.getTitle())))
                .andExpect(jsonPath("$.summary",  equalTo(repo.getSummary())))
                .andExpect(jsonPath("$.url",  equalTo(repo.getUrl())))
                .andExpect(jsonPath("$.platform",  equalTo(repo.getPlatform())));
        verify(apiWrapper).buildRepo("totally_real/really");
        verify(apiWrapper).updateCommits(repo);
    }

    @Test
    void shouldReturnAllCommitsForRepo() throws Exception {
        fail("Not implemented yet");
    }


    @Test
    void shouldGetAllRepos() throws Exception {
        //when(repoRepository.findAll()).thenReturn(repoList);
        doReturn(repoList).when(repoRepository).findAll();
        Repo repoA = repoList.get(0);
        Repo repoB = repoList.get(1);
        mockMvc.perform(get("/repos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]title", equalTo(repoA.getTitle())))
                .andExpect(jsonPath("$[0]summary",  equalTo(repoA.getSummary())))
                .andExpect(jsonPath("$[0]url",  equalTo(repoA.getUrl())))
                .andExpect(jsonPath("$[0]platform",  equalTo(repoA.getPlatform())))
                .andExpect(jsonPath("$[0]contributors",  hasSize(1)))
                .andExpect(jsonPath("$[1]title", equalTo(repoB.getTitle())))
                .andExpect(jsonPath("$[1]summary",  equalTo(repoB.getSummary())))
                .andExpect(jsonPath("$[1]url",  equalTo(repoB.getUrl())))
                .andExpect(jsonPath("$[1]platform",  equalTo(repoB.getPlatform())));
    }

    private APIWrapper createMockWrapper(ContributorRepository contributorRepository, RepoRepository repoRepository, CommitRepository commitRepository) throws Exception{
        return new GithubWrapper(repoRepository, commitRepository, contributorRepository);
    }

    private Repo printMe(Repo repo){
        System.out.println("Called test wrapper");
        return repo;
    }

}
