package models.helpers;

import models.Commit;
import models.Contributor;

import java.util.Collection;

public interface Wrapper {

    public Collection<Commit> buildCommits();

    public Collection<Contributor> buildContributors();

}
