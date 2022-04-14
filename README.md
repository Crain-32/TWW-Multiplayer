## Read Me / Release Notes ##

Welcome to the 0.1.0 Server Release for the Wind Waker Randomizer Multiplayer!

The current implementation of the Server is *not very good*. I recommend either putting this behind a 
whitelisted IP firewall, or using a VPN like Hamachi to avoid exposing yourself.

### Set Up ###
1. Download the Source Code.
2. Open up your preferred shell and install the dependencies `mvn clean install`
3. Run `Application.java`

### Application Properties ###
The `application.properties` in the resource folder only has the port the Server is listening to. If you encounter
problems while running the source code, you can set `root.logging.level` to `TRACE` for a detailed output.

### Server Information ###
- It really doesn't do a lot. This is on purpose.

### How to Connect ###
- Create a Websocket connection.
- Use the STOMP Protocol

#### Multiworld ####
- Subscribe to `/topic/multiworld/<gameroom>`
- Send an ItemDto object to `/app/multiworld/<gameroom>`

#### Coop ####
- Subscribe to `/topic/coop/<gameroom>`
- Send a CoopDto object to `/app/coop/<gameroom>`
