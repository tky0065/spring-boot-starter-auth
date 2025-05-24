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
    <version>1.0.6</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.tky0065:spring-boot-starter-auth:1.0.6'
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

## 🔑 Fonctionnalités OAuth2 détaillées

Le starter prend en charge l'authentification OAuth2 complète avec plusieurs fournisseurs d'identité.

### Avantages OAuth2

- **Authentification sociale simplifiée** : Connectez-vous via Google, GitHub, Facebook, etc.
- **Sécurité améliorée** : Pas besoin de stocker les mots de passe des utilisateurs
- **Expérience utilisateur améliorée** : Connexion en un clic
- **Intégration automatique** : Configuration minimale requise

### Fournisseurs supportés

- Google
- GitHub
- Facebook
- Autres fournisseurs conformes à OAuth2

### Fonctionnalités OAuth2 incluses

1. **Auto-configuration** : Configuration automatique des endpoints OAuth2
2. **Gestion des callbacks** : Traitement automatique des redirections OAuth2
3. **Conversion d'identité** : Conversion automatique des profils OAuth2 en utilisateurs locaux
4. **Gestion des tokens** : Génération de JWT après authentification OAuth2
5. **Gestionnaires personnalisables** : Handlers de succès/échec facilement extensibles
6. **Support des APIs protégées** : Accès aux ressources protégées avec token JWT

### Options de configuration OAuth2

Le starter utilise le préfixe `spring-boot-starter-auth.oauth2.*` pour la configuration :

| Propriété | Description | Valeur par défaut |
|-----------|-------------|-------------------|
| `enabled` | Active/désactive l'authentification OAuth2 | `false` |
| `success-url` | URL de redirection après connexion réussie | `/` |
| `failure-url` | URL de redirection en cas d'échec | `/login?error=true` |
| `authorized-redirect-uris` | Liste des URIs de redirection autorisées | Liste vide |

### Fichier de configuration OAuth2 complet

Un fichier d'exemple complet `oauth2-example.properties` est disponible dans les ressources du projet :

```
src/main/resources/oauth2-example.properties
```

Ce fichier contient des exemples détaillés pour configurer OAuth2 avec différents fournisseurs.

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

### Exemple d'intégration OAuth2 complet

Voici un exemple plus détaillé pour implémenter l'authentification OAuth2 avec ce starter :

#### 1. Configuration dans application.properties

```properties
# Activation d'OAuth2
spring-boot-starter-auth.oauth2.enabled=true
spring-boot-starter-auth.oauth2.success-url=/dashboard
spring-boot-starter-auth.oauth2.failure-url=/login?error=oauth2

# Configuration des fournisseurs OAuth2
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=email,profile
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/oauth2/callback/{registrationId}

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email,read:user
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/oauth2/callback/{registrationId}

spring.security.oauth2.client.registration.facebook.client-id=${FACEBOOK_CLIENT_ID}
spring.security.oauth2.client.registration.facebook.client-secret=${FACEBOOK_CLIENT_SECRET}
spring.security.oauth2.client.registration.facebook.scope=email,public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri={baseUrl}/oauth2/callback/{registrationId}

# URIs de redirection autorisées après authentification
spring-boot-starter-auth.oauth2.authorized-redirect-uris=http://localhost:8080/oauth2/redirect,http://localhost:3000/oauth2/redirect
```

#### 2. Ajout des liens d'authentification dans votre formulaire de connexion (HTML)

```html
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="login-container">
        <h1>Connexion</h1>

        <!-- Formulaire de connexion standard -->
        <form action="/api/auth/login" method="post">
            <!-- Champs de connexion standard -->
            <div class="form-group">
                <label for="username">Nom d'utilisateur :</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-primary">Se connecter</button>
        </form>
        
        <div class="social-login">
            <h3>Ou connectez-vous avec :</h3>
            <div class="social-buttons">
                <a href="/oauth2/authorization/google" class="btn-google">
                    <img src="/images/google-icon.png" alt="Google"> Google
                </a>
                <a href="/oauth2/authorization/github" class="btn-github">
                    <img src="/images/github-icon.png" alt="GitHub"> GitHub
                </a>
                <a href="/oauth2/authorization/facebook" class="btn-facebook">
                    <img src="/images/facebook-icon.png" alt="Facebook"> Facebook
                </a>
            </div>
        </div>
        
        <div class="register-link">
            <p>Pas encore de compte ? <a href="/register">Inscrivez-vous</a></p>
        </div>
    </div>
</body>
</html>
```

#### 3. Personnalisation du traitement OAuth2 (facultatif)

Si vous souhaitez personnaliser le comportement après une authentification OAuth2 réussie, vous pouvez créer un gestionnaire personnalisé :

```java
@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private JwtService jwtService;
    
    @Value("${app.oauth2.default-redirect-uri}")
    private String defaultRedirectUri;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();
        
        // Générer un JWT pour l'utilisateur authentifié via OAuth2
        String jwt = jwtService.generateToken(oauth2User);
        
        // Rediriger vers la page client avec le token
        String targetUrl = UriComponentsBuilder.fromUriString(defaultRedirectUri)
                .queryParam("token", jwt)
                .build().toUriString();
                
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
```

#### 4. Intégration avec une application frontend (React, Angular, Vue.js)

Exemple de code React pour gérer le callback OAuth2 :

```jsx
// OAuthRedirectHandler.jsx
import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  useEffect(() => {
    // Récupérer le token depuis les paramètres d'URL
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    
    if (token) {
      // Stocker le token dans localStorage
      localStorage.setItem('authToken', token);
      
      // Rediriger vers le tableau de bord
      navigate('/dashboard');
    } else {
      // Gérer l'erreur d'authentification
      navigate('/login?error=oauth2_failed');
    }
  }, [location, navigate]);

  return (
    <div className="oauth2-redirect">
      Authentification en cours...
    </div>
  );
};

export default OAuth2RedirectHandler;
```

#### 5. Accès aux API protégées avec le token JWT

Une fois l'authentification OAuth2 réussie et le token JWT obtenu, vous pouvez utiliser ce token pour accéder aux API protégées :

```javascript
// Exemple d'appel API avec le token d'authentification
const fetchUserData = async () => {
  try {
    const token = localStorage.getItem('authToken');
    
    const response = await fetch('http://localhost:8082/api/auth/current-user', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des données utilisateur');
    }
    
    const userData = await response.json();
    return userData;
  } catch (error) {
    console.error('Erreur:', error);
    return null;
  }
};
```

#### 6. Architecture interne OAuth2

Le starter fournit une architecture complète pour l'authentification OAuth2 :

- `OAuth2AuthenticationSuccessHandler` : Gère les connexions réussies
- `OAuth2AuthenticationFailureHandler` : Gère les échecs d'authentification
- `OAuth2UserInfo` : Abstraction des informations utilisateur OAuth2
- `OAuth2UserPrincipal` : Wrapper pour l'utilisateur OAuth2 authentifié
- `OAuth2UserInfoFactory` : Fabrique d'informations utilisateur selon le fournisseur

#### 7. Résolution des problèmes courants

##### Erreurs CORS lors des callbacks OAuth2

Ajoutez ces configurations à votre application.properties :

```properties
# Configuration CORS pour OAuth2
spring.mvc.cors.allowed-origins=http://localhost:3000
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.mvc.cors.allowed-headers=*
spring.mvc.cors.allow-credentials=true
```

##### Erreur "redirect_uri_mismatch"

Assurez-vous que l'URI de redirection configurée dans la console du fournisseur OAuth2 correspond exactement à celle de votre application :
```properties
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/oauth2/callback/{registrationId}
```

##### Page blanche après redirection OAuth2

Vérifiez que la configuration CORS est correcte et que le handler de succès OAuth2 redirige vers la bonne URL.
