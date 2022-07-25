import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Message } from './models/message';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  messages$: Observable<Message[]> = of([
    {
      id: 1,
      sendDate: new Date(),
      content: 'Hello',
      isRead: false,
      sender: { id: 1, nickname: 'Bob', messages: [] },
    },
    {
      id: 2,
      sendDate: new Date(),
      content: 'Salut',
      isRead: false,
      sender: { id: 1, nickname: 'Jane', messages: [] },
    },
  ]);

  constructor() {}
}
