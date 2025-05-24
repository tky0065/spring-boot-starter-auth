# Spring Boot Starter Auth

Un starter Spring Boot complet pour l'authentification JWT et OAuth2 avec gestion des utilisateurs, historique des connexions, confirmation d'email et documentation Swagger UI intégrée.

## 📋 Caractéristiques

- ✅ Authentification JWT complète
- 🌐 Support OAuth2 pour l'authentification sociale
- 🔐 Gestion des rôles et autorisations
- 📧 Confirmation d'email et réinitialisation de mot de passe
- 🔒 Verrouillage de compte après tentatives échouées
- 📊 Historique des connexions
- 📝 Documentation Swagger UI automatique
- 🔄 Endpoints REST prêts à l'emploi
- 🛡️ Configuration de sécurité Spring préconfigurée
- 📦 Auto-configuration Spring Boot
- 🎯 Support des bases de données JPA

## 📥 Installation

### Maven

Ajoutez la dépendance suivante à votre `pom.xml` :
```xml
<dependency>
    <groupId>io.github.tky0065</groupId>
    <artifactId>spring-boot-starter-auth</artifactId>
    <version>1.0.5</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.tky0065:spring-boot-starter-auth:1.0.5'
```

## ⚙️ Configuration

Le starter utilise un système de configuration unifié sous le préfixe `spring-boot-starter-auth`.

### Configuration dans application.properties/yml

```properties
# Configuration JWT
spring-boot-starter-auth.jwt.secret=VotreClefSecrete
spring-boot-starter-auth.jwt.expiration=86400000  # 24 heures en millisecondes

# Configuration OAuth2
spring-boot-starter-auth.oauth2.enabled=true
spring-boot-starter-auth.oauth2.success-url=/home
spring-boot-starter-auth.oauth2.failure-url=/login?error=true
spring-boot-starter-auth.oauth2.authorized-redirect-uris=http://localhost:8080/oauth2/redirect,http://localhost:3000/oauth2/redirect

# Configuration Swagger
spring-boot-starter-auth.swagger.title=API d'Authentification
spring-boot-starter-auth.swagger.description=API pour la gestion complète de l'authentification
spring-boot-starter-auth.swagger.version=1.0

# Base de données
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Email Configuration
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=${SMTP_USERNAME}
spring.mail.password=${SMTP_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Application Configuration
app.email.from=noreply@yourdomain.com
app.base-url=http://localhost:8080
app.security.max-failed-attempts=3
app.security.lock-duration-hours=24

# Logging
logging.level.com.enokdev=DEBUG
```

## 🚀 Utilisation

### Endpoints disponibles

#### Authentification
- **POST** `/api/auth/register` - Inscription d'un nouvel utilisateur
  ```json
  {
    "username": "user",
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```

- **POST** `/api/auth/login` - Connexion
  ```json
  {
    "username": "user",
    "password": "password123"
  }
  ```

#### OAuth2
- **GET** `/oauth2/authorization/{provider}` - Redirection vers le fournisseur OAuth2 (Google, Facebook, etc.)
- **GET** `/oauth2/callback/{provider}` - Callback après authentification OAuth2

#### Gestion du compte
- **GET** `/api/auth/current-user` - Obtenir les informations de l'utilisateur courant
- **POST** `/api/auth/logout` - Déconnexion
- **GET** `/api/auth/confirm-email` - Confirmation d'email
- **POST** `/api/auth/forgot-password` - Demande de réinitialisation de mot de passe
- **POST** `/api/auth/reset-password` - Réinitialisation du mot de passe
- **PUT** `/api/users/{userId}/profile` - Mise à jour du profil
- **GET** `/api/users/{userId}/login-history` - Historique des connexions


## 📖 Documentation API
La documentation Swagger UI est disponible à l'URL :
```
http://votre-serveur:port/swagger-ui.html
```
### 📍 swagger-ui
![swagger ui](swagger-ui.png)

## 🔧 Personnalisation

### Configuration personnalisée du JWT
Créez une classe de configuration :
```java
@Configuration
public class CustomJwtConfig {
    
    @Bean
    public JwtService customJwtService() {
        return new CustomJwtService();
    }
}
```

### Personnalisation du UserDetailsService
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Votre implémentation
    }
}
```

### Configuration OAuth2
Pour activer l'authentification OAuth2 avec des fournisseurs comme Google, GitHub, etc., ajoutez la configuration suivante :

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret
spring.security.oauth2.client.registration.google.scope=email,profile

# GitHub OAuth2
spring.security.oauth2.client.registration.github.client-id=your-client-id
spring.security.oauth2.client.registration.github.client-secret=your-client-secret
```

### Modèles de réponse

#### AuthResponse
```json
{
  "token": "eyJhbG...",
  "type": "Bearer",
  "username": "user",
  "roles": ["ROLE_USER"],
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

## 🛠️ Publication sur Maven Central

Pour publier cette bibliothèque sur Maven Central, suivez ces étapes :

1. **Configurer votre environnement**

   Assurez-vous d'avoir configuré votre environnement avec les clés GPG :
   ```bash
   gpg --gen-key
   gpg --list-keys
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   ```

2. **Configurer votre fichier settings.xml de Maven**

   Ajoutez vos identifiants Sonatype dans `~/.m2/settings.xml` :
   ```xml
   <settings>
     <servers>
       <server>
         <id>central</id>
         <username>sonatype_username</username>
         <password>sonatype_password</password>
       </server>
     </servers>
   </settings>
   ```

3. **Exécuter la commande de publication**

   Utilisez le plugin Central Publishing de Sonatype :
   ```bash
   mvn clean deploy -P release
   ```
   
   Ou exécutez le script deploy.bat fourni :
   ```bash
   ./deploy.bat
   ```

## 📄 Licence

Ce projet est sous licence MIT - consultez le fichier LICENSE pour plus de détails.

## 👨‍💻 Auteur

Yacouba KONE - [EnokDev](https://enok-dev.vercel.app/)

