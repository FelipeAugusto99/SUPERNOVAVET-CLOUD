package Novamonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Novamonitor.entity.Pet;
import Novamonitor.entity.Tutor;
import Novamonitor.repository.PetRepository;
import Novamonitor.repository.TutorRepository;

@Service
public class Petservice {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public List<Pet> listar() {
        return petRepository.findAll();
    }

    public Pet buscarPorId(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public List<Pet> buscarPorRisco(String nivelRisco) {

        return petRepository.findByNivelRisco(nivelRisco);

    }

    public Pet cadastrar(Pet pet) {

        Tutor tutorExistente = tutorRepository.findByEmail(
                pet.getTutor().getEmail()
        ).orElseThrow(() -> new RuntimeException("Tutor não encontrado"));

        if (tutorExistente != null) {
            pet.setTutor(tutorExistente);
        }

        return petRepository.save(pet);
    }


    public Pet atualizar(Long id, Pet petAtualizado) {

        Pet pet = buscarPorId(id);

        if (pet != null) {

            pet.setNome(petAtualizado.getNome());
            pet.setIdade(petAtualizado.getIdade());
            pet.setNivelRisco(petAtualizado.getNivelRisco());

            pet.setEspecie(petAtualizado.getEspecie());

            pet.setTutor(petAtualizado.getTutor());

            return petRepository.save(pet);
        }

        return null;
    }

    public void deletar(Long id) {
        petRepository.deleteById(id);
    }
}