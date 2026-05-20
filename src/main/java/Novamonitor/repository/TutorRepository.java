package Novamonitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Novamonitor.entity.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByEmail(String email);

}