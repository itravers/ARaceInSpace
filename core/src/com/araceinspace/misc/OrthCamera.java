package com.araceinspace.misc;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Isaac Assegai on 8/14/2015.
 */
public class OrthCamera extends OrthographicCamera{
    Matrix4 parallaxView = new Matrix4();
    Matrix4 parallaxCombined = new Matrix4();
    Vector3 tmp = new Vector3();
    Vector3 tmp2 = new Vector3();

    public OrthCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public Matrix4 calculateParallaxMatrix (float parallaxX, float parallaxY) {
        update();
        tmp.set(position);
        tmp.x *= parallaxX;
        tmp.y *= parallaxY;

        parallaxView.setToLookAt(tmp, tmp2.set(tmp).add(direction), up);
        parallaxCombined.set(projection);
        Matrix4.mul(parallaxCombined.val, parallaxView.val);
        return parallaxCombined;
    }

    public void setToAngle(float aGoal){
        aGoal = (aGoal * (180 / (float)Math.PI));
        while(aGoal <= 0){
            aGoal += 360;
        }
        while(aGoal > 360){
            aGoal -= 360;
        }

        float aCam = -getCurrentAngle()+180;
        float aMove = (aCam-aGoal)+180;
        this.rotate(aMove);



       /* float aCurrent = getCurrentAngle();
        float aMove = aGoal - aCurrent;
        aMove = aMove % 360;
        System.out.println("setToAngle: " + aGoal*(180/Math.PI) + " aCurr: " + aCurrent*(180/Math.PI) + " aMove: " + aMove*(180/Math.PI));
        this.rotate(aMove);
*/
    }

    public float getCurrentAngle(){
        float aCurrent = (float)Math.atan2(this.up.x, this.up.y);
        return aCurrent * (180 / (float)Math.PI);
    }
}
