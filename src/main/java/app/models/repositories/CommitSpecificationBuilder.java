package app.models.repositories;

import app.models.Commit;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class CommitSpecificationBuilder {

    private final List<SearchCriteria> params;


    public CommitSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public CommitSpecificationBuilder with (String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Commit> build(){
        if (params.size() == 0) return null;

        List<Specification<Commit>> specs = new ArrayList<Specification<Commit>>();
        for (SearchCriteria param : params){
            specs.add(new CommitSpecification(param));
        }

        Specification<Commit> result = specs.get(0);
        for (int i = 0; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }


}
