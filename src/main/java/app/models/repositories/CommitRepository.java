package app.models.repositories;

import app.models.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommitRepository extends JpaRepository<Commit, Long> {
    Collection<Commit> findByRepoId(Long id);
}
