package repo;

import commit.Commit;
import contributor.Contributor;

import java.util.Collection;

interface Wrapper {

    public Collection<Commit> buildCommits();

    public Collection<Contributor> buildContributors();

}
