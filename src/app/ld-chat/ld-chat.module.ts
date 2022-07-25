import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatComponent } from './chat/chat.component';
import { MessageComponent } from './message/message.component';

@NgModule({
  declarations: [ChatComponent, MessageComponent],
  imports: [CommonModule],
  exports: [ChatComponent],
})
export class LdChatModule {}
