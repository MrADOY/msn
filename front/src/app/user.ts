export class User {
  constructor(
    public password: string,
    public confirmPassword: string,
    public email: string,
    public nom?: string,
    public prenom?: string
  ) {  }
}
