package Novamonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Novamonitor.entity.Pet;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByNivelRisco(String nivelRisco);

    List<Pet> findByEspecie(String especie);
}