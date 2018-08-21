package contributor;

import commit.Commit;

import java.util.Collection;

public class Contributor {

    private Collection<Commit> commits;

    private String name;

    public Contributor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
