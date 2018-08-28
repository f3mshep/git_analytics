package app.models.helpers.wrappers;
import app.models.Repo;

import java.io.IOException;

public interface APIWrapper {

    void updateCommits(Repo repo) throws IOException;

    Repo buildRepo(String url) throws IOException;

}
