package app.controllers;

import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
class RepositoriesControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
        this.mockMvc  = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        this.commitRepository.deleteAllInBatch();
        this.repoRepository.deleteAllInBatch();
        this.contributorRepository.deleteAllInBatch();

    }

    @Test
    void getAllRepos() {
    }

    @Test
    void getCommits() {
    }

    @Test
    void createRepo() {
    }

    @Test
    void returnRepo() {
    }
}