package com.app.finder.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 文章过滤非法文字.
 */
@Entity
@Table(name = "fin_forbidden_word")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ForbiddenWord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //带有正则的文字
    @Column(name = "word")
    private String word;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForbiddenWord forbiddenWord = (ForbiddenWord) o;
        return Objects.equals(id, forbiddenWord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ForbiddenWord{" +
            "id=" + id +
            ", word='" + word + "'" +
            '}';
    }
}
