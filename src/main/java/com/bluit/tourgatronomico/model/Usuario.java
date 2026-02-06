/*package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String foto;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "fecha_ultima_invitacion")
    private LocalDateTime fechaUltimaInvitacion;

    // 🔗 Relación con Rol
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // ===== Verificación de email =====
    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @Column(name = "verification_token", length = 80)
    private String verificationToken;

    @Column(name = "verification_token_expira")
    private LocalDateTime verificationTokenExpira;

    public Usuario() {}

    public Usuario(String nombre, String email, String password, String foto, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.foto = foto;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public LocalDateTime getFechaUltimaInvitacion() { return fechaUltimaInvitacion; }
    public void setFechaUltimaInvitacion(LocalDateTime fechaUltimaInvitacion) { this.fechaUltimaInvitacion = fechaUltimaInvitacion; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Boolean getEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public LocalDateTime getVerificationTokenExpira() { return verificationTokenExpira; }
    public void setVerificationTokenExpira(LocalDateTime verificationTokenExpira) { this.verificationTokenExpira = verificationTokenExpira; }
}*/

package com.bluit.tourgatronomico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String foto;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "fecha_ultima_invitacion")
    private LocalDateTime fechaUltimaInvitacion;

    

    // 🔗 Relación con Rol
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // ===== Verificación de email =====
    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @Column(name = "verification_token", length = 255)
    private String verificationToken;

    @Column(name = "verification_token_expira")
    private LocalDateTime verificationTokenExpira;

    // ===== Reset password =====
    @Column(name = "reset_password_token", length = 255)
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expira")
    private LocalDateTime resetPasswordTokenExpira;

    public Usuario() {}

    public Usuario(String nombre, String email, String password, String foto, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.foto = foto;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }

    public LocalDateTime getFechaUltimaInvitacion() { return fechaUltimaInvitacion; }
    public void setFechaUltimaInvitacion(LocalDateTime fechaUltimaInvitacion) { this.fechaUltimaInvitacion = fechaUltimaInvitacion; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Boolean getEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public LocalDateTime getVerificationTokenExpira() { return verificationTokenExpira; }
    public void setVerificationTokenExpira(LocalDateTime verificationTokenExpira) { this.verificationTokenExpira = verificationTokenExpira; }

    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

    public LocalDateTime getResetPasswordTokenExpira() { return resetPasswordTokenExpira; }
    public void setResetPasswordTokenExpira(LocalDateTime resetPasswordTokenExpira) { this.resetPasswordTokenExpira = resetPasswordTokenExpira; }
}
