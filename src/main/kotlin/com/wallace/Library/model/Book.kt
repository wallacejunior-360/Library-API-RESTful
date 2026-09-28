package com.wallace.Library.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

@Entity
@Table(name = "books")
data class Book (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var author: String = "",

    @Column(name = "launch_date")
    var launchDate: String = "",

    @Column(nullable = false)
    var price: Double = 0.0,

    @Column(nullable = false)
    var title: String = "",
)