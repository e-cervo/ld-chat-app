import { Message } from "./message";

export interface User {
  id: number;
  nickname: string;
  messages: Message[];
}
