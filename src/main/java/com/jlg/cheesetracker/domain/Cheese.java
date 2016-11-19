package com.jlg.cheesetracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cheese.
 */
@Entity
@Table(name = "cheese")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cheese implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "milk")
    private String milk;

    @OneToMany(mappedBy = "cheese")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vote> votes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Cheese name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMilk() {
        return milk;
    }

    public Cheese milk(String milk) {
        this.milk = milk;
        return this;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Cheese votes(Set<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public Cheese addVote(Vote vote) {
        votes.add(vote);
        vote.setCheese(this);
        return this;
    }

    public Cheese removeVote(Vote vote) {
        votes.remove(vote);
        vote.setCheese(null);
        return this;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cheese cheese = (Cheese) o;
        if(cheese.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cheese.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cheese{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", milk='" + milk + "'" +
            '}';
    }
}
