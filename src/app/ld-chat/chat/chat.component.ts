import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs'
import { ChatService } from '../chat.service'
import { Message } from '../models/message'

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  messages$: Observable<Message[]> = this.chatService.messages$

  constructor(
    private chatService: ChatService
  ) { }

  ngOnInit(): void {
  }

}
