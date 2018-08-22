package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;

import java.util.Collection;

public interface Wrapper {

    public Collection<Commit> buildCommits();

    public Collection<Contributor> buildContributors();

}
