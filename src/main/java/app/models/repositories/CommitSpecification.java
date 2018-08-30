package app.models.repositories;

import app.models.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class CommitSpecification implements Specification<Commit> {

    private SearchCriteria criteria;

    public CommitSpecification(SearchCriteria criteria) {
        this.criteria = parseCriteria(criteria);
    }

    private SearchCriteria  parseCriteria(SearchCriteria criteria){
        if (criteria.getOperation().equals("timestamp")){
        } else if (criteria.getKey().equals("repo")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        } else if (criteria.getKey().equals("contributor")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        } else if (criteria.getKey().equals("commit")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        }
        return criteria;
    }

    @Override
    public Predicate toPredicate
            (Root<Commit> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("~")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

}
