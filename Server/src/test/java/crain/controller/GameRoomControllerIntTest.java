package crain.controller;


import annotation.ServerIntegrationTest;
import constants.WorldType;
import crain.repository.PlayerRepo;
import crain.util.JsonTestUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import records.INFO;
import records.ROOM;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ServerIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameRoomControllerIntTest {
    private static final String GAMEROOM = "GameRoom";
    private static final String PASSWORD = "Password";
    private static final String PLAYER_NAME = "PlayerName";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JsonTestUtil jsonTestUtil;


    @Test
    @Order(1)
    void itShould_CreateAGameRoom() throws Exception {
        mockMvc.perform(post("/rest/gameroom")
                        .content(jsonTestUtil.mapToJson(
                                        getGameRoomBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    void itShould_failToMakeTheSameGameRoomAgain() throws Exception {
        mockMvc.perform(post("/rest/gameroom")
                        .content(jsonTestUtil.mapToJson(
                                        getGameRoomBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("A Game Room with this name already exists!"));
    }

    @Test
    @Order(3)
    void itShould_workIfTheNameIsDifferent() throws Exception {
        mockMvc.perform(post("/rest/gameroom")
                        .content(jsonTestUtil.mapToJson(
                                        getGameRoomBuilder()
                                                .gameRoomName("OtherGameRoom").build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void itShould_addAPlayerToAWorld() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM)
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", PASSWORD))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(5)
    void itShould_failToAddTheSamePlayerAgain() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM)
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The provided Player cannot be added for GameRoom"));
    }

    @Test
    @Order(5)
    void itShould_AddADifferentPlayer() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM)
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder()
                                                .playerName("OTHER" + PLAYER_NAME)
                                                .build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", PASSWORD))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    void itShould_FailOnAnIncorrectPassword() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM)
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", "NOT" + PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(content().string(GameRoomController.GAMEROOM_MSG + " for " + GAMEROOM));
    }


    @Test
    @Order(7)
    void itShould_GetAPlayersStatus() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM + "/player")
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value(PLAYER_NAME))
                .andExpect(jsonPath("$.worldId").value(1))
                .andExpect(jsonPath("$.worldType").value("SHARED"))
                .andExpect(jsonPath("$.connected").value("false"));
    }

    @Test
    @Order(8)
    void itShould_FailPlayerStatusOnAnIncorrectPassword() throws Exception {
        mockMvc.perform(post("/rest/gameroom/" + GAMEROOM + "/player")
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", "NOT" + PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(content().string(GameRoomController.GAMEROOM_MSG + " for " + GAMEROOM));
    }

    @Test
    @Order(9)
    void itShould_getDetailedPlayerStatus() throws Exception {
        mockMvc.perform(get("/rest/gameroom/" + GAMEROOM + "/player/detail")
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.playerName").value(PLAYER_NAME))
                .andExpect(jsonPath("$.worldType").value("SHARED"))
                .andExpect(jsonPath("$.connected").value("false"))
                .andExpect(jsonPath("$.queuedItems.length()").value(0))
                .andExpect(jsonPath("$.lastInteractionDate").exists());
    }

    @Test
    @Order(10)
    void itShould_FailDetailedPlayerStatusOnAnIncorrectPassword() throws Exception {
        mockMvc.perform(get("/rest/gameroom/" + GAMEROOM + "/player/detail")
                        .content(jsonTestUtil.mapToJson(
                                        getPlayerBuilder().build()
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("password", "NOT" + PASSWORD))
                .andExpect(status().isNotFound())
                .andExpect(content().string(GameRoomController.GAMEROOM_MSG + " for " + GAMEROOM));
    }

    @Test
    @Order(11)
    void itShould_GetAllPlayersForARoom() throws Exception {
        mockMvc.perform(get("/rest/gameroom/" + GAMEROOM + "/players")
                        .queryParam("password", PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    private INFO.CreateRoomRecord.CreateRoomRecordBuilder getGameRoomBuilder() {
        return INFO.CreateRoomRecord.builder()
                .gameRoomName(GAMEROOM)
                .gameRoomPassword(PASSWORD)
                .playerAmount(2)
                .worldAmount(2)
                .worldType(WorldType.MULTIWORLD);
    }

    private ROOM.PlayerRecord.PlayerRecordBuilder getPlayerBuilder() {
        return ROOM.PlayerRecord.builder()
                .playerName(PLAYER_NAME)
                .worldId(1)
                .worldType(WorldType.SHARED)
                .connected(false);
    }
}

