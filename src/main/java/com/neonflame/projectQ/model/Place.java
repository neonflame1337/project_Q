package com.neonflame.projectQ.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.PrintStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Place {

    @ManyToOne(optional = false)
    private User user;

    private int index;
}
