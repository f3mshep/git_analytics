package unit.controllers;

import app.Application;
import app.controllers.CommitsController;
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
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class CommitsControllerUnitTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Date timeStamp;

    private Commit commit;
    private Repo repo;
    private Contributor contributor;

    @Mock
    private CommitRepository commitRepository;

    @InjectMocks
    private CommitsController commitsController;

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

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(commitsController).build();
        this.timeStamp = new Date();
        this.contributor = new Contributor("f3mshep", "Github");
        this.repo = new RepoBuilder()
                .setOwner(contributor)
                .setPlatform("GitHub")
                .setSummary("A real holler and a hootnanny!")
                .setTitle("The best Repo Stub ever")
                .setUrl("http://github.com/totally_real/really")
                .createRepo();
        this.commit = new CommitBuilder()
                .setUrl("http://github.com/totally_real/really")
                .setTimestamp(timeStamp)
                .setStatus("super commit ftw")
                .setRepo(repo)
                .setContributor(contributor)
                .createCommit();

    }

    @Test
    void getCommitReturnsCommit() throws Exception {

        when(commitRepository.findById(commit.getId())).thenReturn(Optional.of(commit));

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

        when(commitRepository.findById(invalidId)).thenReturn(Optional.empty());
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