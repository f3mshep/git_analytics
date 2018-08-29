package unit.app.models.helpers;

import app.Application;
import app.models.Contributor;
import app.models.Repo;
import app.models.Commit;
import app.models.helpers.CommitBuilder;
import app.models.helpers.RepoBuilder;
import app.models.helpers.wrappers.*;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.kohsuke.github.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)

class GithubAPIWrapperTest {


    @Mock
    private RepoRepository repoRepository;

    @Mock
    private CommitRepository commitRepository;

    @Mock
    private ContributorRepository contributorRepository;

    private Contributor contributor;
    private Repo repo;
    private List <Repo> repoList = new ArrayList<>();
    private Commit commit;
    private List<Commit> commitList = new ArrayList<>();

    @InjectMocks
    GithubWrapper githubWrapper;

    @Mock
    GitHub github;

    @Mock
    GHRepository mockGHRepo;

    @Mock GHCommit a = new GHCommit();
    @Mock GHCommit b = new GHCommit();

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(githubWrapper, "github", github);
        Date timeStamp = new Date();
        this.contributor = new Contributor("f3mshep", "Github");
        this.repo = new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitHub")
                .setSummary("What are you worried about? come get FAKE DOORS!")
                .setTitle("f3mshep/real_fake_doors")
                .setUrl("http://www.github.com/f3mshep/")
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
    public void shouldBuildValidRepo() throws Exception{

        doReturn(mockGHRepo).when(github).getRepository("f3mshep/real_fake_doors");
        doReturn(repo.getTitle()).when(mockGHRepo).getName();
        doReturn(repo.getSummary()).when(mockGHRepo).getDescription();
        doReturn(new URL("http://www.github.com/f3mshep/real_fake_doors")).when(mockGHRepo).getHtmlUrl();
        doReturn("f3mshep").when(mockGHRepo).getOwnerName();
        doReturn(Optional.of(contributor)).when(contributorRepository).findByUsername("f3mshep");
        Repo testRepo = githubWrapper.buildRepo("f3mshep/real_fake_doors");
        assertEquals(repo.getTitle(), testRepo.getTitle());
        assertEquals(repo.getSummary(), testRepo.getSummary());
        assertEquals(repo.getPlatform(), testRepo.getPlatform());
    }


    @Test
    public void shouldPersistRepo() throws Exception {
        doReturn(mockGHRepo).when(github).getRepository("f3mshep/real_fake_doors");
        doReturn(repo.getTitle()).when(mockGHRepo).getName();
        doReturn(repo.getSummary()).when(mockGHRepo).getDescription();
        doReturn(new URL("http://www.github.com/f3mshep/real_fake_doors")).when(mockGHRepo).getHtmlUrl();
        doReturn("f3mshep").when(mockGHRepo).getOwnerName();
        doReturn(Optional.of(contributor)).when(contributorRepository).findByUsername("f3mshep");
        Repo testRepo = githubWrapper.buildRepo("f3mshep/real_fake_doors");
        verify(repoRepository).save(testRepo);

    }



    //it should create the ghrepo object upon instantion

    //buildRepo
        //it should throw an error if unable to find repo
        //it should properly fill out fields of new repo object
        //it should return a repo object
        //it should save the repo object

    //updateRepo
        //it should throw an error if unable to locate repo
        //it should properly add each commit to the repo
        //it should only add the most recent commits to the repo
        //it should properly update the time stamp

}