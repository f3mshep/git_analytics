package app.models.repositories;

import app.models.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface CommitRepository extends JpaRepository<Commit, Long>, JpaSpecificationExecutor<Commit> {
    Collection<Commit> findByRepoId(Long id);
}
