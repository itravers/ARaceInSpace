package com.araceinspace.GameObjectSubSystem.Components;
import com.araceinspace.GameObjectSubSystem.GameObject;

/**
 * Created by Isaac Assegai on 7/10/17.
 * The base abstract class that all the other Components are descended from.
 * Every object that extends Component must have an update(GameObject) method implemented.
 * The update message will be called from the GameObject that owns this Component.
 */
public interface Component {
    void update(float elapsedTime);
}