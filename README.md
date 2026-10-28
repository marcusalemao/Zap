# Zap — WhatsApp on Rokid Glasses

[🇬🇧 English](#en--english) | [🇧🇷 Português](#pt--português)

Download and install guide: **[z.alema.io](https://z.alema.io)**

## EN — English

Zap is a [Rokid Nexus](https://github.com/Anezium) plugin that brings your WhatsApp chats to the Rokid Glasses lens. It runs on the phone: no code on the glasses, no server, no unofficial WhatsApp client.

**On the glasses**
- Chat list, newest first, unread chats highlighted, short times.
- Open a chat: read, play voice messages, read aloud, see photos.
- Reply or start a new chat by voice. You always see the text first; nothing is sent until you tap **Send**.
- Send photos: the one you just took with the glasses' button ("Send with Zap?"), or any from the glasses' or the phone's gallery.
- React with an emoji, sent as a message, in your skin tone.
- Optional AI summary when messages pile up, with one-tap actions (ready replies, calendar, reminder, Maps). Uses your own API key.
- Skills for the Nexus Assistant ("what did I miss on WhatsApp?").
- Portuguese and English.

**How it works**
- Chats come from WhatsApp's notifications and stay on the phone, encrypted with an Android Keystore key.
- Sending opens the WhatsApp chat with the confirmed text. The optional accessibility helper then presses Send for you; it only acts inside WhatsApp, on the text you confirmed.
- Internet is used only for the optional AI summary, straight to the provider you chose.

**Build**

```bash
./gradlew :core:testDebugUnitTest :app:assembleNexusRelease   # the Nexus Store package
./gradlew :app:assembleSiteRelease                            # the package served on z.alema.io
```

JDK 17 and Android SDK 36. Release signing lives outside the repo (`~/.zap/signing.properties`).

| Folder | What |
|---|---|
| `core/` | Pure Kotlin with unit tests: the screens, the `ComposeFlow` state machine, contact matching by voice, summaries, reactions. |
| `app/` | The Nexus plugin: notification listener, send helper, Quick Settings tile, settings. |
| `upstream/` | A patch proposed to Anezium's Relay. |

## PT — Português

O Zap é um plugin do [Rokid Nexus](https://github.com/Anezium) que leva as suas conversas do WhatsApp para a lente dos Rokid Glasses. Ele roda no celular: sem código nos óculos, sem servidor, sem cliente não oficial do WhatsApp.

**Nos óculos**
- Lista de conversas, as mais novas em cima, não lidas em destaque, horários curtos.
- Abra uma conversa: leia, ouça áudios, ouça em voz alta, veja fotos.
- Responda ou comece uma conversa por voz. Você sempre vê o texto antes; nada é enviado até você tocar em **Enviar**.
- Mande fotos: a que você acabou de tirar com o botão dos óculos ("Enviar pelo Zap?") ou qualquer uma da galeria dos óculos ou do celular.
- Reaja com emoji, enviado como mensagem, no seu tom de pele.
- Opcional: resumo com IA quando as mensagens acumulam, com ações em um toque (respostas prontas, agenda, lembrete, Maps). Usa a sua própria chave de API.
- Skills para o Assistant do Nexus ("o que eu perdi no WhatsApp?").
- Português e inglês.

**Como funciona**
- As conversas vêm das notificações do WhatsApp e ficam no celular, criptografadas com uma chave do Android Keystore.
- Para enviar, o Zap abre a conversa do WhatsApp com o texto confirmado. O ajudante de acessibilidade (opcional) aperta Enviar por você; ele só age dentro do WhatsApp, no texto que você confirmou.
- A internet só é usada no resumo com IA (opcional), direto com o provedor que você escolheu.

**Compilar:** veja os comandos acima (JDK 17, Android SDK 36). A assinatura de release fica fora do repositório.

---

Apache-2.0 · Marcus de Magalhães ([alema.io](https://alema.io)). Built on the Rokid Nexus plugin SDK by Anezium. Not affiliated with WhatsApp, Meta or Rokid.
