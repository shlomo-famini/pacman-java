package model;

import model.entity.GhostState;
import model.entity.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldModel {

    private final PlayerState playerState;
    private final List<GhostState> ghostStates;

    public WorldModel() {
        this.playerState = new PlayerState();
        this.ghostStates = new ArrayList<>();
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public List<GhostState> getGhostStates() {
        return Collections.unmodifiableList(ghostStates);
    }

    public void clearGhostStates() {
        ghostStates.clear();
    }

    public void addGhostState(GhostState ghostState) {
        ghostStates.add(ghostState);
    }
}
