import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Message } from '../ld-chat/models/message';
import { webSocket } from 'rxjs/webSocket';
import { User } from '../ld-chat/models/user';

export interface Chat{

  /**
   *
   */
  messages(): Observable<Message[]>;


  /**
   *
   * @param message
   */
  push(message: Message): void;

}



/**
 * Le service "cache" le webscoket en proposant l'observable sur le behaviorSubject
 */
@Injectable({
  providedIn: 'root'
})
export class ChatService implements Chat{


  private socket = webSocket("ws://192.168.10.14:3001");


  private messagesSubject = new BehaviorSubject<Message[]>([]);


  /**
   *
   */
  constructor() {
    this.socket.subscribe(m => {
       if(typeof m == 'string'){
        this.messagesSubject.getValue().push(JSON.parse(m));
        this.messagesSubject.next(this.messagesSubject.getValue());
       }
      });

   }


   /**
    *
    * @returns
    */
  messages(): Observable<Message[]> {
    return this.messagesSubject.asObservable();
  }

  /**
   *
   * @param message
   */
  push(message: Message): void {
    this.socket.next(JSON.stringify(message));
  }
}



