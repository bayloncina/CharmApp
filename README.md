# 💅 CharmApp

**CharmApp** è un’applicazione **full-stack** sviluppata come progetto finale del corso ITS in *sviluppo software*.  
L’obiettivo dell’app è semplificare la **gestione degli appuntamenti** per **centri estetici e parrucchieri**, offrendo una soluzione digitale intuitiva, moderna e sicura.

---

## 🚀 Stack Tecnologico

**Backend**
- ☕ Java 1.8  
- 🌱 Spring Boot  
- 🧩 Spring MVC (pattern a strati)  
- 🔐 Spring Security con JSP  
- 🗃️ Spring Data JPA  
- 🐘 PostgreSQL (pgAdmin)

**Frontend**
- ⚡ Angular  
- 🎨 Angular Material  
- 🧠 TypeScript  
- 💻 HTML / SCSS  

---

## 🏗️ Architettura

CharmApp segue un’architettura **MVC multilayer**, con chiara separazione tra logica, dati e interfaccia.  
Il flusso principale è il seguente:

Angular (frontend) → REST API (Spring Boot) → Service → Repository → PostgreSQL


Le pagine di **login** e **registrazione** utilizzano JSP integrate con **Spring Security** per la gestione dell’autenticazione e dei ruoli.

---

## ✨ Funzionalità Principali

- 👤 Registrazione e autenticazione utenti (JSP + Spring Security)
- 📅 Creazione, modifica ed eliminazione appuntamenti
- 💇‍♀️ Gestione clienti e operatori
- 🕒 Visualizzazione calendario appuntamenti
- 🧾 Storico prenotazioni e gestione disponibilità
- 💾 Persistenza dati su PostgreSQL

---

## ⚙️ Come Avviare il Progetto

### 🔹 Backend
1. Clona il repository:
   ```bash
   git clone https://github.com/bayloncina/CharmApp.git
   
2. Vai nella cartella del backend  
3. Configura il file application.properties con le tue credenziali del db
4. Avvia l’applicazione

### 🔹 Frontend

1. Entra nella cartella del frontend
2. Installa le dipendenze:
    ```bash
   npm install
3. Avvia il server di sviluppo:
   ```bash
   ng serve
4. Apri il browser su
👉 http://localhost:4200

 
