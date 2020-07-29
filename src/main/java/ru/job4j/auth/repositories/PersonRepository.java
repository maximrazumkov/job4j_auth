package ru.job4j.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.auth.domains.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}