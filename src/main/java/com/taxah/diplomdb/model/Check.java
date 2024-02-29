package com.taxah.diplomdb.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "my_check")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private Session session;

    private String name;

    @OneToMany(mappedBy = "check",cascade = CascadeType.ALL)
    private List<ProductUsing> productUsingList;

    public void addProductUsing(ProductUsing pu){
        productUsingList.add(pu);
    }
//    public void addProductUsing(ProductUsing pu, List<TempUser> users){
//        pu.setUsers(users);
//        productUsingList.add(pu);
//    }
}
