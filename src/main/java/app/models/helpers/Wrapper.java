package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.repositories.CommitRepository;

import java.io.IOException;
import java.util.List;

public interface Wrapper {

    public List<Commit> buildCommits(Repo myRepo) throws IOException;

    public List<Contributor> buildContributors(Repo myRepo);

}
