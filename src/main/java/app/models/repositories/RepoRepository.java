package app.models.repositories;

import app.models.Commit;
import app.models.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RepoRepository extends JpaRepository<Repo, Long> {
}
