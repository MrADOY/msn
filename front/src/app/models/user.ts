export class User {
  constructor(
    public nom?: string,
    public prenom?: string,
    public email?: string,
    public password?: string,
    public confirmPassword?: string,
    public avatar?: string,
    public id?: string,
    public username?: string,
    public authority?: string,
  ) {
  }
}
