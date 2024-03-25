package org.bai.security.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
* Sample api model. TODO Delete later.
* */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message implements Serializable {
    private String value;
}