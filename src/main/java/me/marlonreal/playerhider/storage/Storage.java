package me.marlonreal.playerhider.storage;

import java.util.UUID;

public interface Storage {

    void saveMode(UUID uuid, int mode);

    int loadMode(UUID uuid);

    void close();
}