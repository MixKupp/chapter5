package se233.chapter5;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5.model.GameCharacter;
import se233.chapter5.model.Keys;
import se233.chapter5.view.GameStage;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameCharacterTest {
    private GameCharacter gameCharacter1;
    private GameCharacter gameCharacter2;
    Field xVelocityField, yVelocityField, yAccelerationField;

    @BeforeAll
    public static void initJfxRuntime(){
        javafx.application.Platform.startup(() -> {});
    }
    @BeforeEach
    public void setUP() throws NoSuchFieldException{
        gameCharacter1 = new GameCharacter(0,30,30,"assets/Character1.png",4,3,
                2,111,97,KeyCode.A,KeyCode.D,KeyCode.W);
        xVelocityField = gameCharacter1.getClass().getDeclaredField("xVelocity");
        yVelocityField = gameCharacter1.getClass().getDeclaredField("yVelocity");
        yAccelerationField = gameCharacter1.getClass().getDeclaredField("yAcceleration");
        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        yAccelerationField.setAccessible(true);
    }
    @Test
    public void respawn_givenNewGameCharacter_thenCoordinatesAre30_30(){
        gameCharacter1.respawn();
        assertEquals(30, gameCharacter1.getX(),"Initial x");
        assertEquals(30, gameCharacter1.getY(),"Initial y");
    }
    @Test
    public void respawn_givenNewGameCharacter_thenScoreIs0(){
        gameCharacter1.respawn();
        assertEquals(0, gameCharacter1.getScore(),"Initial score");
    }
    @Test
    public void moveX_givenMoveRightOnce_thenXCoordinateIncreasedByXVelocity() throws IllegalAccessException{
        gameCharacter1.respawn();
        gameCharacter1.moveRight();
        gameCharacter1.moveX();
        assertEquals(30 + xVelocityField.getInt(gameCharacter1), gameCharacter1.getX(), "Move right x");
    }
    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYVelocityIncreases() throws IllegalAccessException{
        gameCharacter1.respawn();
        gameCharacter1.moveY();
        int yVelocity1 = yVelocityField.getInt(gameCharacter1);
        gameCharacter1.moveY();
        int yVelocity2 = yVelocityField.getInt(gameCharacter1);
        assertTrue(yVelocity2 > yVelocity1, "Velocity is increasing");
    }
    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYAccelerationUnchanged() throws IllegalAccessException{
        gameCharacter1.respawn();
        gameCharacter1.moveY();
        int yAcceleration1 = yAccelerationField.getInt(gameCharacter1);
        gameCharacter1.moveY();
        int yAcceleration2 = yAccelerationField.getInt(gameCharacter1);
        assertTrue(yAcceleration1 == yAcceleration2, "Acceleration is not change");
    }

    @Test
    public void checkReachGameWall_moveTowardTheleftBorder() {
        gameCharacter1.setX(0);
        gameCharacter1.checkReachGameWall();
        assertEquals(0, gameCharacter1.getX(),"Left wall reach");
    }

    @Test
    public void checkReachGameWall_moveTowardTherightBorder() {
        gameCharacter1.setX(GameStage.WIDTH);
        gameCharacter1.checkReachGameWall();
        assertTrue(gameCharacter1.getX() <= GameStage.WIDTH, "Right wall reach");
    }

    @Test
    public void jump_characterCanJump_thenIsJumpingTrue() {
        gameCharacter1.setCanJump(true);
        gameCharacter1.jump();
        assertTrue(gameCharacter1.isJumping(), "Character Jump");
    }

    @Test
    public void jump_characterCannotJump_thenIsJumpingFalse() {
        gameCharacter1.setCanJump(false);
        gameCharacter1.jump();
        assertFalse(gameCharacter1.isJumping(), "Character didn't Jump");
    }

    @Test
    public void collided_newCharacterCollideHorizontally_thenIsCollidedFalse() {
        gameCharacter2 = new GameCharacter(1, 35, 30, "assets/Character2.png", 4, 4 ,1, 129,66, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP);
        gameCharacter2.setxVelocity(gameCharacter2.getxMaxVelocity());
        gameCharacter2.moveX();
        assertFalse(gameCharacter1.collided(gameCharacter2), "Collided with other character");
    }

    @Test
    public void collided_newCharacterCollideVertically_thenIsCollidedTrue() {
        gameCharacter2 = new GameCharacter(1, 30, 40, "assets/Character2.png", 4, 4 ,1, 129,66, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP);
        gameCharacter2.setyVelocity(gameCharacter2.getyMaxVelocity());
        gameCharacter2.setFalling(true);
        gameCharacter2.moveY();
        assertTrue(gameCharacter1.collided(gameCharacter2), "Collided with other character");
    }

    @Test
    public void keypress_singlekeypress_thenIsPressedIsTrue() {
        KeyCode key = KeyCode.A;
        Keys keys = new Keys();
        keys.add(key);
        assertTrue(keys.isPressed(key), "Key pressed");
    }

    @Test
    public void keypress_sequencekeypress_thenIsPressedIsTrue() {
        KeyCode key1 = KeyCode.A;
        KeyCode key2 = KeyCode.B;
        Keys keys = new Keys();
        keys.add(key1);
        keys.add(key2);
        assertTrue(keys.isPressed(key1), "Key1 pressed");
        assertTrue(keys.isPressed(key2), "Key2 pressed");
    }
}
