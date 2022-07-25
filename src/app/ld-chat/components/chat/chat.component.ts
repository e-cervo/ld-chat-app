import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent {
  @Output() messageTyped = new EventEmitter<string>();

  message: string = '';

  send() {
    console.log(this.message);
    this.message = '';
  }
}
