package app.models.repositories;

import app.models.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommitSpecification implements Specification<Commit> {

    private SearchCriteria criteria;

    public CommitSpecification(SearchCriteria criteria) {
        this.criteria = parseCriteria(criteria);
    }

    private SearchCriteria  parseCriteria(SearchCriteria criteria){
        if (criteria.getOperation().equals("timestamp")){
            criteria.setValue(convertStringToDate(criteria.getValue().toString()));
        } else if (criteria.getKey().equals("repo")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        } else if (criteria.getKey().equals("contributor")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        } else if (criteria.getKey().equals("commit")){
            criteria.setValue(Long.valueOf(criteria.getValue().toString()));
        }
        return criteria;
    }

    private Date convertStringToDate(String datetime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date date = formatter.parse(datetime.replaceAll("Z$", "+0000"));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Predicate toPredicate(

            Root<Commit> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.<String> get(
                        criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.<String> get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.<String> get(
                        criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }

}
