package ru.job4j.auth.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.App;
import ru.job4j.auth.domains.Person;
import ru.job4j.auth.repositories.PersonRepository;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @Test
    public void shouldReturnAllPersons() throws Exception {
        this.mockMvc.perform(get("/person"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnPersonsById() throws Exception {
        Optional<Person> optPerson = Optional.of(new Person(1));
        doReturn(optPerson).when(personRepository).findById(1);
        this.mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreatePerson() throws Exception {
        this.mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                        + "\t\"login\":\"user\",\n"
                        + "\t\"password\":\"123\"\n"
                        + "}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(argument.capture());
        assertThat(argument.getValue().getLogin(), is("user"));
        assertThat(argument.getValue().getPassword(), is("123"));
    }

    @Test
    public void shouldModifyPerson() throws Exception {
        this.mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                        + "\t\"id\":\"1\",\n"
                        + "\t\"login\":\"user\",\n"
                        + "\t\"password\":\"123\"\n"
                        + "}"))
                .andDo(print())
                .andExpect(status().isOk());
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(argument.capture());
        assertThat(argument.getValue().getId(), is(1));
        assertThat(argument.getValue().getLogin(), is("user"));
        assertThat(argument.getValue().getPassword(), is("123"));
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        this.mockMvc.perform(delete("/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(personRepository).delete(new Person(1));
    }
}

