package com.wallace.Library.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "user_name", unique = true)
    private var userName: String = ""

    @Column(name = "full_name")
    var fullName: String = ""

    @Column(name = "password")
    private var password: String = ""

    @Column(name = "account_non_expired")
    private var accountNonExpired: Boolean = true

    @Column(name = "account_non_locked")
    private var accountNonLocked: Boolean = true

    @Column(name = "credentials_non_expired")
    private var credentialsNonExpired: Boolean = true

    @Column(name = "enabled")
    private var enabled: Boolean = true

    // Inicializado como lista vazia para evitar NullPointerException
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "user_permission",
        joinColumns = [JoinColumn(name = "id_user", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "id_permission", referencedColumnName = "id")]
    )
    var permissions: List<Permission> = emptyList()

    // Sintaxe idiomática do Kotlin para mapear as permissões
    val roles: List<String>
        get() = permissions.map { it.description ?: "" }

    // 2. Implementação limpa dos métodos da interface usando overrides diretos nas funções
    override fun getAuthorities(): Collection<GrantedAuthority> = permissions

    override fun getUsername(): String = userName

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun isEnabled(): Boolean = enabled

    // Setters customizados caso você precise alterar os campos internos de segurança
    fun setUsername(username: String) { this.userName = username }
    fun setPassword(password: String) { this.password = password }
}