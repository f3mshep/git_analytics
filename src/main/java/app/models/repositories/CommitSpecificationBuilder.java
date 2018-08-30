package app.models.repositories;

import app.models.Commit;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class CommitSpecificationBuilder {

    private List<SearchCriteria> params;

    public CommitSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public CommitSpecificationBuilder with(
            String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<Commit> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Commit>> specs = new ArrayList<Specification<Commit>>();
        for (SearchCriteria param : params) {
            specs.add(new CommitSpecification(param));
        }

        Specification<Commit> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
