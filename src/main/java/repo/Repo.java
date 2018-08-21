package repo;

import commit.Commit;
import contributor.Contributor;

import java.util.Collection;

public class Repo {

    Collection<Contributor> contributors;
    Collection<Commit> commits;
    String name, desc, url;

    public Repo(String name, String desc, String url) {
        this.name = name;
        this.desc = desc;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }
}
