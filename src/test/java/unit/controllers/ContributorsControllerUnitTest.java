package unit.controllers;

import app.Application;
import app.controllers.ContributorsController;
import app.models.Contributor;
import app.models.repositories.ContributorRepository;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
class ContributorsControllerUnitTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Contributor contributor;
    private List<Contributor> contributorList = new ArrayList<>();

    @Mock
    private ContributorRepository contributorRepository;

    @InjectMocks
    private ContributorsController contributorsController;

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

        mockMvc = MockMvcBuilders.standaloneSetup(contributorsController).build();
        this.contributor = new Contributor("f3mshep", "Github");
        this.contributorList.add(contributor);
        this.contributorRepository.save(contributor);
        this.contributorList.add((new Contributor("meFace", "GitBucket")));
        System.out.println("Setup complete");
    }

    @Test
    void getContributorReturnsContributor() throws Exception {

        when(contributorRepository.findByUsername(contributor.getUsername())).thenReturn(Optional.of(contributor));

        mockMvc.perform(get("/contributors/" + contributor.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", equalTo(Math.toIntExact(contributor.getId()))))
                .andExpect(jsonPath("$.username", equalTo(contributor.getUsername())))
                .andExpect(jsonPath("$.platform",  equalTo(contributor.getPlatform())));

    }

    @Test
    void getContributorThrowsNotFound() throws Exception{
        String invalidName = "rawr";
        when(contributorRepository.findByUsername(invalidName)).thenReturn(Optional.empty());
        mockMvc.perform(get("/contributors/" + invalidName))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllContributorReturnsAllRepos() throws Exception{
        Contributor contributorA = contributorList.get(0);
        Contributor contributorB = contributorList.get(1);
        when(contributorRepository.findAll()).thenReturn(this.contributorList);
        mockMvc.perform(get("/contributors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]id", equalTo(Math.toIntExact(contributorA.getId()))))
                .andExpect(jsonPath("$[0]username", equalTo(contributorA.getUsername())))
                .andExpect(jsonPath("$[0]platform",  equalTo(contributorA.getPlatform())))
                .andExpect(jsonPath("$[1]id", equalTo(Math.toIntExact(contributorB.getId()))))
                .andExpect(jsonPath("$[1]username", equalTo(contributorB.getUsername())))
                .andExpect(jsonPath("$[1]platform",  equalTo(contributorB.getPlatform())));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}