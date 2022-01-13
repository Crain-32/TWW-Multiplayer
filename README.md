## Read Me ##

Welcome to the Server implementation for the Wind Waker Multiworld project! 
Please understand that the project is still in early Development. At times one aspect
may seem more refined than others, and that is to be expected.

Long term the goal is to put this Server into the Cloud, and provide it free of charge.

I ***will not be making a guide*** on how to use the Server yet, as the current implementation 
has no security.

### Set Up ###
1. Download the Source Code.
2. Open up your preferred shell and install the dependencies `mvn clean install`
3. Run `Application.java`

### Application Properties ###
The `application.properties` in the resource folder is *not* going to be included after the initial commit.
If you plan on running the Server from Source beyond testing, make sure to change the defaults.

### FAQ ###
- You will have to remake a room if you shut down the server, this *can* delete items in certain edge cases.
- Yes, nothing works! Welcome to the early stages of development.
- I'm using Java version 13.

### Current Plans ###
- Get STOMP to actually work.
- Additional endpoints for full MVP functionality.
- Initial Documentation
- Add a file for H2 to read from for better Persistence.
- Better Handling for Default Authentication Credentials.
- Implement Test Suites.

### Known Problems ###
- HTTP Token Error, this is likely due to the Spring configuration, I'm new to STOMP/WebSockets, 
so that not working doesn't surprise me.
- Bloated Additional Libraries, I've burned through *a lot* of guides trying to get STOMP to work. Once STOMP does work,
I plan on cleaning up the artifacts.