package com.example.keycloakdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "functions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Function {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private Long parentId;

    /**
     * cây menu
     */
    private String path;

    /**
     * thứ tự hiển thị menu
     */
    @Column(name = "function_rank") // rank : 'Reserved Keyword'
    private Integer rank;

    private String realm;
}
