// Author     : Marko Karjalainen <markotfk@gmail.com>

function Player(email, login, password) {
    this.id = 0;
    this.email = email;
    this.login = login;
    this.password = password;
    this.sessionId = "";
    this.isActive = false;
}

