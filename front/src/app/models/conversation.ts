import {User} from './user';

export class Conversation {
  constructor(
    public id?: string,
    public contact?: User,
    public emetteur?: User,
    public nbMessages?: string,
  ) {
  }
}
