package Novamonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import Novamonitor.entity.Tutor;
import Novamonitor.repository.TutorRepository;

import java.util.List;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorRepository repository;

    @GetMapping
    public List<Tutor> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tutor cadastrar(@RequestBody Tutor tutor) {
        return repository.save(tutor);
    }
}