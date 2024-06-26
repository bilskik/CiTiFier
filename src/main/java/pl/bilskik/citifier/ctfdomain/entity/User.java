package pl.bilskik.citifier.ctfdomain.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    @Id
    private Integer id;
    private String login;
    private String password;
}
