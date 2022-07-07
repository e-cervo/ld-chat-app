import { User } from "./user";

export interface Message {
   id: number;
   sendDate: Date;
   content: string;
   isRead: boolean;
   sender: User;

}
