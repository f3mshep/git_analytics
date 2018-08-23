package app.models;

import app.models.helpers.RepoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class RepoTest {

    private Repo repo;

    @BeforeEach
    void setUp() {
        RepoBuilder rb = new RepoBuilder();
        this.repo = rb
                .setPlatform("GitHub")
                .setOwner("f3mshep")
                .setSummary("A mocked repo")
                .setTitle("Mock_Me")
                .setUrl("http://www.fakeurl.com")
                .createRepo();
    }

    @Test
    void addContributor() {
        //it should add a contributor
        //it should not add a contributor if they exist
    }

    @Test
    void getContributors() {
        //it should return a list of unique contributors
    }

    @Test
    void getLastUpdated() {
        //it returns the last updated timestamp
    }

    @Test
    void setLastUpdated() {
        //it sets the timestamp
    }

    @Test
    void getOwner() {
        //it sets 
    }

    @Test
    void getPlatform() {
    }

    @Test
    void getId() {
    }

    @Test
    void getTitle() {
    }

    @Test
    void getSummary() {
    }

    @Test
    void getUrl() {
    }
}