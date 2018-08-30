package integration.app.controllers;

import app.models.repositories.*;

import app.Application;
import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.helpers.CommitBuilder;
import app.models.helpers.RepoBuilder;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class RepositoriesControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Date timeStamp;

    private Commit commit;
    private Repo repo;
    private Contributor contributor;
    private WireMockServer wireMockServer;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull(this.mappingJackson2HttpMessageConverter);
    }

    @BeforeEach
    public void setup() throws Exception{
        this.commitRepository.deleteAllInBatch();
        this.repoRepository.deleteAllInBatch();
        this.contributorRepository.deleteAllInBatch();
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        Date timeStamp = new Date();
        this.contributor = new Contributor("f3mshep", "Github");
        contributorRepository.save(contributor);
        this.repo = new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitHub")
                .setSummary("A real holler and a hootnanny!")
                .setTitle("The best Repo Stub ever")
                .setUrl("http://github.com/totally_real/really")
                .createRepo();
        repoRepository.save(repo);
        repoRepository.save(new RepoBuilder()
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
        commitRepository.save(commit);
        commitRepository.save(new CommitBuilder()
                .setUrl("http://github.com/totally_real/really")
                .setTimestamp(new Date())
                .setStatus("super commit pt 2")
                .setRepo(repo)
                .setContributor(contributor)
                .createCommit()
        );
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
//    @Test
//    void getAllRepos() {
//
//    }
//
//    @Test
//    void getCommits() {
//    }
//
//    @Test
//    void createRepo() {
//    }

    @Test
    void shouldSearch(){
        CommitSpecification commitSpecification;

        SearchCriteria criteria = new SearchCriteria("repo", ":", repo.getId());

        CommitSpecification spec = new CommitSpecification(criteria);

        List<Commit> commits = commitRepository.findAll(spec);
        System.out.println("wheee");
    }

    @Test
    void returnRepo() {
        Repo testRepo = new RepoBuilder().setOwner(contributor).setPlatform("GitHub").setSummary("Getting my feet wet with Spring Boot REST APIs").setTitle("git_analytics").setUrl("https://github.com/f3mshep/git_analytics").createRepo();

    }


}
