package app.controllers;

import app.Application;
import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.helpers.CommitBuilder;
import app.models.helpers.RepoBuilder;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class CommitsControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Date timeStamp;

    private Commit commit;
    private Repo repo;
    private Contributor contributor;
    private List<Commit> commitList = new ArrayList<>();

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
        this.timeStamp = new Date();
        this.contributor = new Contributor("f3mshep", "Github");
        this.contributorRepository.save(contributor);
        this.repo = new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitHub")
                .setSummary("A real holler and a hootnanny!")
                .setTitle("The best Repo Stub ever")
                .setUrl("http://github.com/totally_real/really")
                .createRepo();
        this.repoRepository.save(repo);
        this.commit = new CommitBuilder()
                .setUrl("http://github.com/totally_real/really")
                .setTimestamp(timeStamp)
                .setStatus("super commit ftw")
                .setRepo(repo)
                .setContributor(contributor)
                .createCommit();
        this.commitRepository.save(commit);

    }

    @Test
    void getCommitReturnsCommit() throws Exception {
        mockMvc.perform(get("/commits/" + commit.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", equalTo(Math.toIntExact(commit.getId()))))
                //.andExpect(jsonPath("$.timestamp", equalTo(commit.getTimestamp())))
                .andExpect(jsonPath("$.url", equalTo(commit.getUrl())))
                .andExpect(jsonPath("$.status", equalTo(commit.getStatus())))
                .andExpect(jsonPath("$.repo.title", equalTo(repo.getTitle())))
                .andExpect(jsonPath("$.contributor.username", equalTo(contributor.getUsername())));
    }

    @Test
    void getCommitThrowsNotFound() throws Exception{
        long invalidId = commit.getId() + 1;
        mockMvc.perform(get("/commits/" + invalidId))
                .andExpect(status().isNotFound());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}