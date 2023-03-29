package gbrstd.clinica.repository;

import gbrstd.clinica.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    Optional<Especialidade> findByDescricao(String descricao);

    boolean existsByDescricao(String descricao);

}
