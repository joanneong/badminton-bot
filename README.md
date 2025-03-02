# badminton-bot

## Introduction
This is a Telegram bot for managing badminton games. It is essentially like a typical CRUD app, but in the form of a telegram bot.

## Design Considerations
- Collect free-text info first (e.g. location/courts) as there is no way to get free-text inputs from users with keyboards/callbacks
- Store `/add` command params in Redis cache as callback data for a button is limited by Telegram to x bytes
