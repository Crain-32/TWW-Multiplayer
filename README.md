## Read Me / Release Notes ##

Welcome to the 0.0.1 Server Release for the Wind Waker Randomizer Multiworld!

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
- Multiple Rooms per server instance is *not currently supported*
- All users are subscribed to the same queue.
- I have not cleaned the dependencies for the release, I expect to use them long term anyway.
- It really doesn't do a lot. This is on purpose.

### How to Connect ###
- Create a Websocket connection.
- Subscribe to `/topic/item`
- Send an ItemDto object to `/app/item`