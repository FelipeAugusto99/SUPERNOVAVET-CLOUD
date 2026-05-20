package Novamonitor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import Novamonitor.entity.Pet;
import Novamonitor.entity.Tutor;
import Novamonitor.repository.PetRepository;
import Novamonitor.repository.TutorRepository;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @GetMapping
    public List<Pet> listar() {
        return petRepository.findAll();
    }

    @PostMapping
    public Pet cadastrar(@RequestBody Pet pet) {

        Tutor tutorRecebido = pet.getTutor();

        Optional<Tutor> tutorExistente =
                tutorRepository.findByEmail(tutorRecebido.getEmail());

        Tutor tutorFinal;

        if (tutorExistente.isPresent()) {

            tutorFinal = tutorExistente.get();

        } else {

            tutorFinal = tutorRepository.save(tutorRecebido);
        }

        pet.setTutor(tutorFinal);

        return petRepository.save(pet);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {

        petRepository.deleteById(id);

        return "Pet deletado com sucesso!";
    }

    @PutMapping("/{id}")
    public Pet atualizar(@PathVariable Long id,
                         @RequestBody Pet petAtualizado) {

        Pet pet = petRepository.findById(id).orElseThrow();

        pet.setNome(petAtualizado.getNome());
        pet.setEspecie(petAtualizado.getEspecie());
        pet.setIdade(petAtualizado.getIdade());
        pet.setNivelRisco(petAtualizado.getNivelRisco());

        return petRepository.save(pet);
    }
}