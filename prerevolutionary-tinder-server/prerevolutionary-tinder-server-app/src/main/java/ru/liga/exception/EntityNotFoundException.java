package ru.liga.exception;

public class EntityNotFoundException extends  RuntimeException {

    public EntityNotFoundException(Long id) {
        super("Entity with id " + id + " was no found");
    }
}
